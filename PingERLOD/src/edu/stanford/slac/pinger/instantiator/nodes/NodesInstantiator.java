package edu.stanford.slac.pinger.instantiator.nodes;

import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.P;
import edu.stanford.slac.pinger.model.NodesModel;
import edu.stanford.slac.pinger.model.SchoolModel;
import edu.stanford.slac.pinger.model.general.GeneralModelSingletonSequential;

public class NodesInstantiator {


	public static void start() {

		JsonObject json = C.getNodeDetails();
		if (json == null) {
			Logger.log(NodesInstantiator.class + "Json Null", "errors");
			return;
		}
		GeneralModelSingletonSequential gm = null; 
		try {
			gm = GeneralModelSingletonSequential.getInstance();
			for (Entry<String,JsonElement> entry : json.entrySet()) {
				try {
					String key = entry.getKey();
					JsonObject j = json.get(key).getAsJsonObject();			
					
					String sourceName =  j.get("SourceName").toString().replace("\"", "");
					String latitude = j.get("latitude").toString().replace("\"", "");
					String longitude = j.get("longitude").toString().replace("\"", "");
					String continent = j.get("continent").toString().replace("\"", "");
					String country = j.get("country").toString().replace("\"", "");
					String group = j.get("group").toString().replace("\"", "");

					gm.begin();
					//Instantiating a PhysicalLocation
					String plURI = P.BASE+"PL-"+sourceName;
					gm.addTripleResource(plURI, P.RDF, "type", P.MGC, "PhysicalLocation");

					gm.addTripleLiteral(plURI, P.MGC, "latitude", Double.parseDouble(latitude));
					j.remove("latitude");
					gm.addTripleLiteral(plURI, P.MGC, "longitude",  Double.parseDouble(longitude));
					j.remove("longitude");
					gm.addTripleLiteral(plURI, P.MGC, "country", country);
					j.remove("country");
					gm.addTripleLiteral(plURI, P.MGC, "continent", continent); 
					j.remove("continent");
					gm.addTripleLiteral(plURI, P.MGC, "group", group);
					j.remove("group");

					//Instantiating a NodeInformation
					String nodeURI = P.BASE+"Node-"+sourceName;		

					gm.addTripleResource(nodeURI, P.RDF, "type", P.MD, "NodeInformation");
					gm.addTripleResource(nodeURI, P.MD, "isInPhysicalLocation", plURI);			

					String townURI = NodesModel.getTownResourceFromPingerLatLong(latitude, longitude);
					if (townURI != null)
						gm.addTripleResource(nodeURI, P.MGC, "isInTown", townURI);
					else
						Logger.error("Node Key: " + key + " - Could not find a town for the node with PingERLat="+latitude+", PingERLong="+longitude, "nodes/errors");
					String schoolURI = SchoolModel.getSchoolResourceFromPingerLatLong(latitude, longitude);
					if (schoolURI != null)
						gm.addTripleResource(nodeURI, P.MGC, "isInSchool", schoolURI);

					//Instantiating information about a node
					for (Entry<String,JsonElement> e : j.entrySet()) {
						String k = e.getKey();
						String property = k+"Value";
						String value = j.get(k).toString().replace("\"", "");
						if (value.equals("")) value = "undefined";
						String uri = P.BASE+k+"-"+sourceName;
						gm.addTripleResource(uri, P.RDF, "type", P.MD, k);
						gm.addTripleLiteral(uri, P.MD, property, value);

						gm.addTripleResource(nodeURI, P.MD, "hasNodeInformation", uri);
					}
					gm.commit();
				} catch (Exception e) {
					Logger.log(NodesInstantiator.class + "Key: " + entry.getKey(), e, "nodes/errors");
					continue;
				}
			}
		}catch (Exception e) {
			Logger.error(NodesInstantiator.class, e, "nodes");
		} finally {
			gm.close();
		}

	}

}
