package edu.stanford.slac.pinger.rest.query.town;

import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.bean.TownBean;
import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.rest.query.QuerySparqlEndpoints;

public class TownModelSparql {

	/**
	 * Function used to set up the sameAs attributes of a Geonames individual.
	 * @param tb TownBean to be filled with the missing values.
	 * @return A TownBean with an attempt to set the following fields: <br>
	 *         <b>FreebaseId.</b>
	 * @category DBPedia Query
	 */
	public static TownBean getFreebaseResourceFromDBPedia(TownBean tb) {
		String query =
				"SELECT ?FreeBase WHERE {" +
						"<"+tb.getDbpediaLink()+"> owl:sameAs ?FreeBase ." +
						"filter regex(?FreeBase, 'freebase', 'i') ."+
						"}";
		try {
			JsonObject json = QuerySparqlEndpoints.getResultAsJson(query, C.DBPEDIA_ENDPOINT);
			tb.setFreebaseLink(C.getValue(json, "FreeBase"));
		} catch (Exception e) {
			Logger.log("getFreebaseResourceFromDBPedia", e, "towns/errors");
			tb.setFreebaseLink(null);
		}
		return tb;
	}

	/**
	 * Function used to set up the sameAs attributes of a Geonames individual.
	 * @param tb TownBean to be filled with the missing values.
	 * @return A TownBean with an attempt to set the following fields: <br>
	 *         <b>DbpediaId, FreebaseId.</b>
	 * @category DBPedia Query
	 */
	public static TownBean getDBPediaAndFreebaseResourcesFromGeonamesId(TownBean tb) {
		String query =
				"SELECT ?dbpediaRsrc ?FreeBase WHERE {" +
						"?dbpediaRsrc owl:sameAs <http://sws.geonames.org/"+tb.getGeonamesId()+"/> ." +
						"?dbpediaRsrc owl:sameAs ?FreeBase ." +
						"filter regex(?FreeBase, 'freebase', 'i') ."+
						"}";
		JsonObject json = QuerySparqlEndpoints.getResultAsJson(query, C.DBPEDIA_ENDPOINT);
		String dbp = C.getValue(json, "dbpediaRsrc");
		if (dbp!=null) {
			if (tb.getWikiPediaLink()==null)
				tb.setWikiPediaLink("http://en.wikipedia.org/wiki/"+dbp.replace("http://dbpedia.org/resource/", ""));
			tb.setDbpediaLink(dbp);
			tb.setFreebaseLink(C.getValue(json, "FreeBase"));
		} else {
			getDBPediaFromGeonamesId(tb);			
		}		
		return tb;
	}

	/**
	 * Function used to set up the sameAs attributes of a Geonames individual.
	 * @param tb TownBean to be filled with the missing values.
	 * @return A TownBean with an attempt to set the following fields: <br>
	 *         <b>DbpediaId</b>
	 * @category DBPedia Query
	 */
	public static TownBean getDBPediaFromGeonamesId(TownBean tb) {
		String query =
				"SELECT ?dbpediaRsrc WHERE {" +
						"?dbpediaRsrc owl:sameAs <http://sws.geonames.org/"+tb.getGeonamesId()+"/> ." +
						"}";
		JsonObject json = QuerySparqlEndpoints.getResultAsJson(query, C.DBPEDIA_ENDPOINT);
		String dbp = C.getValue(json, "dbpediaRsrc");
		if (dbp!=null) {
			tb.setDbpediaLink(dbp);
			tb.setDbPediaId(tb.getDbpediaLink().replace("http://dbpedia.org/resource/", ""));
		}
		return tb;
	}

	/**
	 * This function is to be used when the town has no state defined.  
	 * @param tb TownBean to be filled with the missing values.
	 * @category FactForge Query
	 * @return A TownBean with an attempt to set the following fields: <br>
	 *         <b>countryDBPediaID, continentName, continentGeoId continentDBPediaId</b> 
	 */
	public static TownBean getCountryContinentFromGeonamesId(TownBean tb) {
		String query =
				"SELECT distinct * WHERE { " +
						"<http://sws.geonames.org/"+tb.getGeonamesId()+"/> gn-ont:parentCountry ?country ." +
						"?country rdfs:seeAlso ?countryDBPedia ." +
						"FILTER regex(str(?countryDBPedia), 'dbpedia', 'i') ." +
						"?country gn-ont:name ?countryName ." +
						"BIND(replace(str(?countryDBPedia), '_', ' ', 'i') AS ?b) ." +
						"BIND(replace(str(?b), 'http://dbpedia.org/resource/', '', 'i') AS ?countryFilter) ." +
						"FILTER ( ?countryName = ?countryFilter )" +
						"?country gn-ont:parentFeature ?continentDBPedia ." +
						"FILTER regex(str(?continentDBPedia), 'dbpedia', 'i')" +
						"?continentDBPedia gn-ont:name ?continentName ." +
						"?continentDBPedia rdfs:isDefinedBy ?continentGeoId ." +
						"} limit 1";

		JsonObject json = QuerySparqlEndpoints.getResultAsJsonUsingHttpGetFactForge(query);
		if (C.getValue(json, "continentGeoId") != null) {
			tb.setCountryDBPediaID(C.getValue(json, "countryDBPedia"));
			tb.setContinentName(C.getValue(json, "continentName"));
			tb.setContinentGeoId(C.getValue(json, "continentGeoId").replace("/about.rdf", ""));
			tb.setContinentDBPediaId(C.getValue(json, "continentDBPedia"));
		}
		return tb;
	}

	/**
	 * This function is to be used when the town has no state defined.  
	 * @param tb TownBean to be filled with the missing values.
	 * @category FactForge Query
	 * @return A TownBean with an attempt to set the following fields: <br>
	 *         <b>StateDBPediaId, countryDBPediaID, continentName, continentGeoId continentDBPediaId</b> 
	 */
	public static TownBean getStateCountryContinentFromGeonamesId(TownBean tb) {

		String query = 
				"SELECT distinct * WHERE { " +
						"<http://sws.geonames.org/"+tb.getGeonamesId()+"/> gn-ont:parentADM1 ?stateDBPedia . " +
						"filter regex(str(?stateDBPedia), 'dbpedia', 'i') . " +
						"?stateDBPedia gn-ont:name ?stateName . " +
						"BIND(REPLACE(str(?stateDBPedia), '_', ' ', 'i') AS ?a) . " +
						"BIND(REPLACE(str(?a), 'http://dbpedia.org/resource/', '', 'i') AS ?stateFilter) . " +
						"filter (?stateFilter = ?stateName) ." +
						"<http://sws.geonames.org/"+tb.getGeonamesId()+"/> gn-ont:parentCountry ?countryDBPedia ." +
						"filter regex(str(?countryDBPedia), 'dbpedia', 'i') ." +
						"?countryDBPedia gn-ont:name ?countryName ." +
						"BIND(REPLACE(str(?countryDBPedia), '_', ' ', 'i') AS ?b) ." +
						"BIND(REPLACE(str(?b), 'http://dbpedia.org/resource/', '', 'i') AS ?countryFilter) ." +
						"filter (?countryName = ?countryFilter) ." +
						"?countryDBPedia gn-ont:parentFeature ?continentDBPedia ." +
						"filter regex(str(?continentDBPedia), 'dbpedia', 'i') ." +
						"?continentDBPedia gn-ont:name ?continentName ." +
						"?continentDBPedia rdfs:isDefinedBy ?continentGeoId ." +
						"} limit 1";

		JsonObject json = QuerySparqlEndpoints.getResultAsJsonUsingHttpGetFactForge(query);
		if (C.getValue(json, "stateDBPedia") != null) {
			tb.setStateDBPediaId(C.getValue(json, "stateDBPedia"));
			tb.setCountryDBPediaID(C.getValue(json, "countryDBPedia"));
			tb.setContinentName(C.getValue(json, "continentName"));
			tb.setContinentGeoId(C.getValue(json, "continentGeoId").replace("/about.rdf", ""));
			tb.setContinentDBPediaId(C.getValue(json, "continentDBPedia"));
		} else tb.setStateDBPediaId(null);
		return tb;
	}

}
