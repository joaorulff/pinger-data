package edu.stanford.slac.pinger.general;

import java.util.HashMap;

public final class P {
	
	public static final String DEFAULT_GRAPH = "http://www.pingerlod.slac.stanford.edu";
	
	public static final String MEASUREMENTS_CONTEXT = null;//"MeasurementsContext";
	public static final String NODES_CONTEXT = null;//"NodesContext";
	public static final String SCHOOLS_CONTEXT = null;//"SchoolsContext";
	public static final String TOWN_CONTEXT = null;//"TownsContext";
	public static final String CONTINENTS_CONTEXT = null;//"ContinentsContext";
	public static final String COUNTRIES_CONTEXT = null;//"CountriesContext";
	/***********************************************
	 ************** Prefixes ***********************
	 ***********************************************/
	public static final String BASE = "http://www-iepm.slac.stanford.edu/pinger/lod/resource#";
	public static final String PINGER_ONT = "PingER-ont";
	public static final String OWL = "owl";
	public static final String XSD = "xsd";
	public static final String RDFS = "rdfs";
	public static final String RDF = "rdf";
	public static final String FOAF = "foaf";
	public static final String DC = "dc";
	public static final String MD = "MD";
	public static final String MGC = "MGC";
	public static final String UNITS = "Units";
	public static final String MU = "MU";
	public static final String GN = "gn";
	public static final String GN_ONT = "gn-ont";
	public static final String POS = "pos";
	public static final String DBPRSRC = "dbp-rsrc";
	public static final String DBPPROP = "dbp-prop";
	public static final String DBP_OWL = "dbp-owl";
	public static final String FB = "fb";
	public static final String TIME = "time";
	
	static String prefixes = "";
	static HashMap<String,String> mapPrfxs = new HashMap<String, String>();
	static {
		mapPrfxs.put(OWL, "http://www.w3.org/2002/07/owl#");
		mapPrfxs.put(XSD, "http://www.w3.org/2001/XMLSchema#");
		mapPrfxs.put(RDFS, "http://www.w3.org/2000/01/rdf-schema#");
		mapPrfxs.put(RDF, "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		mapPrfxs.put(FOAF, "http://xmlns.com/foaf/0.1/");
		mapPrfxs.put(DC, "http://purl.org/dc/elements/1.1/");
		mapPrfxs.put(MD, "http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentDataV2.owl#");
		mapPrfxs.put(MGC, "http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#");
		mapPrfxs.put(UNITS, "http://www-iepm.slac.stanford.edu/pinger/lod/ontology/Units.owl/#");
		mapPrfxs.put(MU, "http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentUnits.owl#");
		mapPrfxs.put(GN, "http://sws.geonames.org/");
		mapPrfxs.put(GN_ONT, "http://www.geonames.org/ontology#");
		mapPrfxs.put(POS, "http://www.w3.org/2003/01/geo/wgs84_pos#");
		mapPrfxs.put(DBPRSRC, "http://dbpedia.org/resource/");
		mapPrfxs.put(DBPPROP, "http://dbpedia.org/property/");
		mapPrfxs.put(DBP_OWL, "http://dbpedia.org/ontology/");
		mapPrfxs.put(FB, "http://rdf.freebase.com/ns/");
		mapPrfxs.put(TIME, "http://www.w3.org/2006/time#");
		
		
		for (String prfx : mapPrfxs.keySet()) {
			prefixes += "PREFIX "+prfx+": <"+mapPrfxs.get(prfx)+">\n";
		}		
	}	
	public final static HashMap<String,String> MAP_PREFIXES = mapPrfxs;
	public final static String PREFIXES = prefixes;
	
	
}
