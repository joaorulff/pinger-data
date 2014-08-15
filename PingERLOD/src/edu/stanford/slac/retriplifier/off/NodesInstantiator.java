package edu.stanford.slac.retriplifier.off;

import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.P;
import edu.stanford.slac.pinger.general.vocabulary.PingER_ONT;
import edu.stanford.slac.pinger.general.vocabulary.RDF;
import edu.stanford.slac.pinger.model.NodesModel;
import edu.stanford.slac.pinger.model.SchoolModel;
import edu.stanford.slac.pinger.model.general.GeneralModelSingletonSequential;
import edu.stanford.slac.retriplifier.TripleModelOff;

public class NodesInstantiator {


	public static void start() {

		JsonObject json = C.getNodeDetails();
		if (json == null) {
			Logger.log(NodesInstantiator.class + "Json Null", "errors");
			return;
		}
		TripleModelOff tm = null; 
		try {
			tm = TripleModelOff.getInstance();
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

					//Instantiating a PhysicalLocation
					j.remove("latitude");
					j.remove("longitude");
					j.remove("country");
					j.remove("continent");
					j.remove("group");
					/*
					String plURI = P.BASE+"PL-"+sourceName;
					tm.addTripleResource(plURI, P.RDF, "type", P.MGC, "PhysicalLocation");
					tm.addTripleLiteral(plURI, P.MGC, "latitude", Double.parseDouble(latitude));
					tm.addTripleLiteral(plURI, P.MGC, "country", country);
					tm.addTripleLiteral(plURI, P.MGC, "longitude",  Double.parseDouble(longitude));
					tm.addTripleLiteral(plURI, P.MGC, "continent", continent); 
					tm.addTripleLiteral(plURI, P.MGC, "group", group);
					tm.addTripleResource(nodeURI, P.MD, "isInPhysicalLocation", plURI);			
					*/
					
					//Instantiating a NodeInformation
					String nodeURI = P.BASE+"Node-"+sourceName;		
					tm.addTripleResource(nodeURI, RDF.type, PingER_ONT.NetworkNode);

					String townURI = NodesModel.getTownResourceFromPingerLatLong(latitude, longitude);
					if (townURI != null)
						tm.addTripleResource(nodeURI, PingER_ONT.isInTown, townURI);
					else
						Logger.error("Node Key: " + key + " - Could not find a town for the node with PingERLat="+latitude+", PingERLong="+longitude, "nodes/errors");
					String schoolURI = SchoolModel.getSchoolResourceFromPingerLatLong(latitude, longitude);
					if (schoolURI != null)
						tm.addTripleResource(nodeURI, PingER_ONT.isInSchool, schoolURI);

					//Instantiating information about a node
					for (Entry<String,JsonElement> e : j.entrySet()) {
						String k = e.getKey();
						String nodeInformation = "has"+k;
						String value = j.get(k).toString().replace("\"", "");
						if (value.equals("")) value = "undefined";
						tm.addTripleLiteral(nodeURI, PingER_ONT.PREFIX + nodeInformation, value);
					}
				} catch (Exception e) {
					Logger.log(NodesInstantiator.class + "Key: " + entry.getKey(), e, "nodes/errors");
					continue;
				}
			}
		}catch (Exception e) {
			Logger.error(NodesInstantiator.class, e, "nodes");
		} finally {
			tm.writeTriplesAndClean();
		}

	}

}
