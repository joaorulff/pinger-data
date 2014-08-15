package edu.stanford.slac.pinger.instantiator.physicallocation;

import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.bean.SchoolBean;
import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.P;
import edu.stanford.slac.pinger.model.NodesModel;
import edu.stanford.slac.pinger.model.general.GeneralModelSingletonSequential;
import edu.stanford.slac.pinger.rest.query.SchoolModelREST;

public class SchoolInstantiator {


	public static void start() {

		int contSchools = 0;
		JsonObject json = C.getNodeDetails();
		int total = json.entrySet().size();

		for (Entry<String,JsonElement> entry : json.entrySet()) {
			String key = entry.getKey();
			Logger.log("*******************#######################==="+key);
			JsonObject j = json.get(key).getAsJsonObject();

			SchoolBean sb = new SchoolBean();
			sb.setSchoolPingerName(j.get("SourceFullName").toString().replace("\"", ""));
			sb.setPingERLat(j.get("latitude").toString().replace("\"", ""));
			sb.setPingERLong(j.get("longitude").toString().replace("\"", ""));

			SchoolModelREST.getDBPediaSchool(sb);
			if (sb.getSchoolDBPediaLink()!=null) {
				GeneralModelSingletonSequential gm = null; 
				try {
					gm = GeneralModelSingletonSequential.getInstance();
					contSchools++;
					SchoolModelREST.getInfoFromDBPedia(sb);

					Logger.log(sb);
					Logger.log("||||||||||||||||||||||||||||||||");

					String schoolURI = P.BASE + sb.getSchoolDBPediaLink().replace("http://dbpedia.org/resource/", "");
					
					gm.begin();
					gm.addTripleResource(schoolURI, P.RDF, "type", P.MGC, "School");
					gm.addTripleResource(schoolURI, P.MGC, "DBPediaLink", sb.getSchoolDBPediaLink());
					gm.addTripleResource(schoolURI, P.MGC, "WikipediaLInk", sb.getSchoolWikipediaLink());
					if(sb.getSchoolFreebaseLink()!=null)
						gm.addTripleResource(schoolURI, P.MGC, "FreebaseLink", sb.getSchoolFreebaseLink());

					if(sb.getSchoolType()!=null) {
						for (String t : sb.getSchoolType()) {
							gm.addTripleResource(schoolURI, P.MGC, "SchoolType", t);
						}
					}
					try {
						if(sb.getSchoolNumberOfStudents()!=null)
							gm.addTripleLiteral(schoolURI, P.MGC, "SchoolNumberOfStudents", Integer.parseInt(sb.getSchoolNumberOfStudents()));
					} catch (Exception e) {
						Logger.log(SchoolInstantiator.class + " " + entry.getKey(), e, "schools");
					}
					try {
						if(sb.getSchoolNumberOfUgradStudents()!=null)
							gm.addTripleLiteral(schoolURI, P.MGC, "SchoolNumberOfUgradStudents", Integer.parseInt(sb.getSchoolNumberOfUgradStudents()));
					} catch (Exception e) {
						Logger.log(SchoolInstantiator.class + " " + entry.getKey(), e, "schools");
					}
					try {
						if(sb.getSchoolNumberOfGradStudents()!=null)
							gm.addTripleLiteral(schoolURI, P.MGC, "SchoolNumberOfGradStudents", Integer.parseInt(sb.getSchoolNumberOfGradStudents()));
					} catch (Exception e) {
						Logger.log(SchoolInstantiator.class + " " + entry.getKey(), e, "schools");
					}	
					try {
						if(sb.getSchoolEndowment()!=null)
							gm.addTripleLiteral(schoolURI, P.MGC, "SchoolEndowment", Float.parseFloat(sb.getSchoolEndowment()));
					} catch (Exception e) {
						Logger.log(SchoolInstantiator.class + " " + entry.getKey(), e, "schools");
					}		
					try {
						if(sb.getSchoolFacultySize()!=null)
							gm.addTripleLiteral(schoolURI, P.MGC, "SchoolFacultySize", Integer.parseInt(sb.getSchoolFacultySize()));
					} catch (Exception e) {
						Logger.log(SchoolInstantiator.class + " " + entry.getKey(), e, "schools");
					}	
					try {
						if(sb.getSchoolName()!=null)
							gm.addTripleLiteral(schoolURI, P.MGC, "SchoolName", sb.getSchoolName());
					} catch (Exception e) {
						Logger.log(SchoolInstantiator.class + " " + entry.getKey(), e, "schools");
					}		
					try {
						if(sb.getSchoolPingerName()!=null)
							gm.addTripleLiteral(schoolURI, P.MGC, "SchoolPingerName", sb.getSchoolPingerName());
					} catch (Exception e) {
						Logger.log(SchoolInstantiator.class + " " + entry.getKey(), e, "schools");
					}		
					if(sb.getPingERLat()!=null)
						gm.addTripleLiteral(schoolURI, P.MGC, "PingERLat", Double.parseDouble(sb.getPingERLat()));		
					if(sb.getPingERLong()!=null)
						gm.addTripleLiteral(schoolURI, P.MGC, "PingERLong", Double.parseDouble(sb.getPingERLong()));
					try {
						if(sb.getGeoLatitude()!=null)
							gm.addTripleLiteral(schoolURI, P.POS, "lat", Double.parseDouble(sb.getGeoLatitude()));
					} catch (Exception e) {
						Logger.log(SchoolInstantiator.class + " " + entry.getKey() + " " + e, "schools");
					}	
					try {
						if(sb.getGeoLongitude()!=null)
							gm.addTripleLiteral(schoolURI, P.POS, "long", Double.parseDouble(sb.getGeoLongitude()));
					} catch (Exception e) {
						Logger.log(SchoolInstantiator.class + " " + entry.getKey() + " " + e, "schools");
					}		
					String townURI = NodesModel.getTownResourceFromPingerLatLong(sb.getPingERLat(), sb.getPingERLong());
					if (townURI != null)
						gm.addTripleResource(schoolURI, P.MGC, "isInTown", townURI);
					gm.commit();
				} catch (Exception e) {
					Logger.log(SchoolInstantiator.class + " " + entry.getKey() + " " + e, "schools");
					continue;
				} finally {
					gm.close();
				}
			}	
		}

		Logger.log("Number of schools="+contSchools + ", out of: "+total);
	}

}
