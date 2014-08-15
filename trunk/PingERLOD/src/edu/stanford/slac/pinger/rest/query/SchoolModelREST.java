package edu.stanford.slac.pinger.rest.query;

import java.util.ArrayList;
import java.util.HashSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.bean.SchoolBean;
import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.P;
import edu.stanford.slac.pinger.general.QueryString;

public class SchoolModelREST {

	public static void main(String args[]) {

	}
	
	/**
	 * Once a University DBPedia URI was found, this method queries DBPedia Endpoint to try to retrieve any useful information about the university.  
	 * @param sb A SchoolBean with the DBPediaLink property set
	 * @return A SchoolBean with an attempt to set any useful information about a University such as Number of Grad Students, Faculty Size, Endowment, etc.
	 */
	public static SchoolBean getInfoFromDBPedia(SchoolBean sb) {
		if (sb.getSchoolDBPediaLink() == null) return sb;
		sb.setSchoolWikipediaLink(sb.getSchoolDBPediaLink().replace("http://dbpedia.org/resource/","http://wikipedia.org/wiki/"));
		String query =
				"SELECT distinct * WHERE {" +
						"<resource> ?prop ?value . " +
						"}";
		query = query.replace("resource", sb.getSchoolDBPediaLink());
		try {
			JsonObject json = QuerySparqlEndpoints.getResultAsJson(query, C.DBPEDIA_ENDPOINT);
			String owl = P.MAP_PREFIXES.get(P.OWL);
			String rdf = P.MAP_PREFIXES.get(P.RDF);
			String dbpowl = P.MAP_PREFIXES.get(P.DBP_OWL);
			String dbpprop = P.MAP_PREFIXES.get(P.DBPPROP);
			String foaf = P.MAP_PREFIXES.get(P.FOAF);
			String geo = P.MAP_PREFIXES.get(P.POS);

			HashSet<String> props = new HashSet<String>();
			props.add(owl+"sameAs");
			props.add(dbpowl+"type");
			props.add(rdf+"label");
			props.add(foaf+"name");
			props.add(dbpprop+"name");
			props.add(dbpowl+"endowment");
			props.add(dbpowl+"facultySize");
			props.add(dbpowl+"numberOfPostgraduateStudents");	
			props.add(dbpowl+"numberOfStudents");
			props.add(dbpowl+"numberOfUndergraduateStudents");	
			props.add(dbpowl+"wikiPageExternalLink");
			props.add(dbpprop+"undergrad");
			props.add(dbpprop+"students");
			props.add(dbpprop+"postgrad");
			props.add(geo+"lat");
			props.add(geo+"long");

			JsonObject j = C.getValues(json, props);

			
			//SchoolName
			try {
				if (j.get(dbpprop+"name") != null) {
					JsonArray jarr = j.get(dbpprop+"name").getAsJsonArray();
					if (jarr.size()>0){
						sb.setSchoolName(jarr.get(0).getAsString());
					}
				}

			} catch (Exception e) {
				Logger.log(SchoolModelREST.class + ".getInfoFromDBPedia." + "name -- ", e, "schools/errors");
			}
			//SchoolEndowment
			try {
				if (j.get(dbpowl+"endowment") != null) {
					JsonArray jarr = j.get(dbpowl+"endowment").getAsJsonArray();
					if (jarr.size()>0){
						sb.setSchoolEndowment(jarr.get(0).getAsString());
					}
				}
			} catch (Exception e) {
				Logger.log(SchoolModelREST.class + ".getInfoFromDBPedia." + sb.getSchoolName() + " endowment -- ", e, "schools/errors");
			}
			//SchoolType
			try {
				if (j.get(dbpowl+"type") != null) {
					JsonArray jarr = j.get(dbpowl+"type").getAsJsonArray();
					if (jarr.size()>0){
						ArrayList<String> lst = new ArrayList<String>();
						for (int i = 0; i < jarr.size(); i++) {
							lst.add(jarr.get(i).getAsString());
						}
						sb.setSchoolType(lst);
					}
				}
			} catch (Exception e) {
				Logger.log(SchoolModelREST.class + ".getInfoFromDBPedia."  + sb.getSchoolName() + " type -- " , e,  "schools/errors");
			}
			//SchoolFacultySize
			try {
				if (j.get(dbpowl+"facultySize") != null) {
					JsonArray jarr = j.get(dbpowl+"facultySize").getAsJsonArray();
					if (jarr.size()>0){
						sb.setSchoolFacultySize(jarr.get(0).getAsString());
					}
				}
			} catch (Exception e) {
				Logger.log(SchoolModelREST.class + ".getInfoFromDBPedia."  + sb.getSchoolName() + " facultySize -- ", e,  "schools/errors");
			}
			//GeoLatitude
			try {
				if (j.get(geo+"lat") != null) {
					JsonArray jarr = j.get(geo+"lat").getAsJsonArray();
					if (jarr.size()>0){
						sb.setGeoLatitude(jarr.get(0).getAsString());
					}
				}
			} catch (Exception e) {
				Logger.log(SchoolModelREST.class + ".getInfoFromDBPedia."  + sb.getSchoolName() + " GeoLatitude -- " , e,  "schools/errors");
			}
			//GeoLongitude
			try {
				if (j.get(geo+"long") != null) {
					JsonArray jarr = j.get(geo+"long").getAsJsonArray();
					if (jarr.size()>0){
						sb.setGeoLongitude(jarr.get(0).getAsString());
					}
				}
			} catch (Exception e) {
				Logger.log(SchoolModelREST.class + ".getInfoFromDBPedia."  + sb.getSchoolName() + " GeoLongitude -- " , e,  "schools/errors");
			}
			//NumberOfStudents
			try {
				if (j.get(dbpowl+"numberOfStudents") != null) {
					JsonArray jarr = j.get(dbpowl+"numberOfStudents").getAsJsonArray();
					if (jarr.size()>0){
						sb.setSchoolNumberOfStudents(jarr.get(0).getAsString());
					} 
				} else if (j.get(dbpprop+"students") != null){
					JsonArray jarr = j.get(dbpprop+"students").getAsJsonArray();
					if (jarr.size()>0){
						sb.setSchoolNumberOfStudents(jarr.get(0).getAsString());
					} 
				}
			} catch (Exception e) {
				Logger.log(SchoolModelREST.class + ".getInfoFromDBPedia."  + sb.getSchoolName() + " facultySize -- " , e,  "schools/errors");
			}
			//NumberOfGradStudents
			try {
				if (j.get(dbpowl+"numberOfPostgraduateStudents") != null) {
					JsonArray jarr = j.get(dbpowl+"numberOfPostgraduateStudents").getAsJsonArray();
					if (jarr.size()>0){
						sb.setSchoolNumberOfGradStudents(jarr.get(0).getAsString());
					} 
				} else if (j.get(dbpprop+"postgrad") != null){
					JsonArray jarr = j.get(dbpprop+"postgrad").getAsJsonArray();
					if (jarr.size()>0){
						sb.setSchoolNumberOfGradStudents(jarr.get(0).getAsString());
					} 
				}
			} catch (Exception e) {
				Logger.log(SchoolModelREST.class + ".getInfoFromDBPedia."  + sb.getSchoolName() + " facultySize -- " , e,  "schools/errors");
			}
			//NumberOfUgradStudents
			try {
				if (j.get(dbpowl+"numberOfUndergraduateStudents") != null) {
					JsonArray jarr = j.get(dbpowl+"numberOfUndergraduateStudents").getAsJsonArray();
					if (jarr.size()>0){
						sb.setSchoolNumberOfUgradStudents(jarr.get(0).getAsString());
					} 
				} else if (j.get(dbpprop+"undergrad") != null){
					JsonArray jarr = j.get(dbpprop+"undergrad").getAsJsonArray();
					if (jarr.size()>0){
						sb.setSchoolNumberOfUgradStudents(jarr.get(0).getAsString());
					} 
				}
			} catch (Exception e) {
				Logger.log(SchoolModelREST.class + ".getInfoFromDBPedia."  + sb.getSchoolName() + " facultySize -- " , e,  "schools/errors");
			}
			//SchoolFreebaseLink
			try {
				if (j.get(owl+"sameAs") != null) {
					JsonArray jarr = j.get(owl+"sameAs").getAsJsonArray();
					if (jarr.size()>0){
						for (int i = 0; i < jarr.size(); i++) {
							String s = jarr.get(i).getAsString();
							if (s.contains("freebase")) {
								sb.setSchoolFreebaseLink(s);
								break;
							}
						}
					}
				}
			} catch (Exception e) {
				Logger.log(SchoolModelREST.class + ".getInfoFromDBPedia."  + sb.getSchoolName() + " type -- " , e,  "schools/errors");
			}

			/*
			 * SchoolFreebaseLink
			 */

		} catch (Exception e) {
			Logger.log("SchoolModelREST.getInfoFromDBPedia ", e, "schools/errors");
		}
		return sb;
	}

	/**
	 * This method executes a Sparql Query on DBPedia Endpoint to try to find a DBPedia resource that is a University given the PingER Node's name. If found, the method returns a SchoolBean with the DBPedia resource link set.
	 * @param sb A SchoolBean with the PingER Node's full name set.
	 * @return A SchoolBean with the property DBPediaLink set to either a valid DBPedia URI if a University was found or null otherwise.
	 */
	public static SchoolBean getDBPediaSchool(SchoolBean sb) {
		if (sb.getSchoolPingerName().contains("'")) sb.setSchoolPingerName(sb.getSchoolPingerName().replace("'", "")); //Jena queries dont like " ' " in strings		
		QueryString qs = new QueryString(
				"SELECT DISTINCT ?school WHERE { ",
				"	{ ?school dbp-owl:type dbp-rsrc:Private_university. } ",
				"	UNION ",
				"	{ ?school dbp-owl:type dbp-rsrc:Public_university. } ",
				"	UNION ",
				"	{ ?school rdf:type dbp-owl:University.} ",
				"	UNION ",
				"	{ ?school rdf:type <http://schema.org/EducationalOrganization>. } ", 
				"	UNION ",
				"	{ ?school rdf:type <http://schema.org/CollegeOrUniversity>. } ", 
				"	UNION ",
				"	{ ?school rdf:type dbp-owl:EducationalInstitution.} ", 
				"",
				"	{ ",
				"		?school rdfs:label ?label . ", 
				"		FILTER REGEX( ",
				"			str(?label), '_PINGERNAME_' ",
				"		) ",
				"	}  ",
				"	UNION { ",	
				"		?school dbp-prop:name ?dbpname . ",
				"		FILTER REGEX( str(?dbpname), '_PINGERNAME_' ) ",
				"	} ",
				"	UNION { ", 
				"		?school foaf:name ?foafname . ", 
				"		FILTER REGEX( str(?foafname), '_PINGERNAME_' )  ",
				"	} ", 
				"} ",
				"LIMIT 10" 
		);
		String q = qs.toString().replace("_PINGERNAME_", sb.getSchoolPingerName());
		
		try {
			JsonObject json = QuerySparqlEndpoints.getResultAsJson(q, C.DBPEDIA_ENDPOINT);

			sb.setSchoolDBPediaLink(C.getValue(json, "school"));

		} catch (Exception e) {
			Logger.log("getInfoFromDBPedia", e,  "schools/errors");
			sb.setSchoolDBPediaLink(null);
		}
		return sb;
	}

}


