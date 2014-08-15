package edu.stanford.slac.pinger.rest;


import java.util.Iterator;
import java.util.Set;

import org.openrdf.model.Model;
import org.openrdf.model.Resource;

import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.rest.pingtable.HttpGetter;
import edu.stanford.slac.pinger.rest.query.QuerySparqlEndpoints;
import edu.stanford.slac.pinger.rest.query.RDFReaderOpenRDF;

public class CheckEndpoints {

	public static final long MAX = 5000; //max time in ms
	public static final long MAX_FACTFORGE = 8000;
	
	public static boolean GeoNamesIsUp() {
		String url = "http://api.geonames.org/findNearbyPlaceNameJSON?&style=FULL&username="+C.GEONAMES_USERNAME[0]+"&lat=37.448&lng=-122.1745&cities=cities15000";
		Logger.log("Checking Geonames API");
		long t1 = System.currentTimeMillis();
		String s = HttpGetter.readPage(url);
		long t2 = System.currentTimeMillis();
		if (s == null || s.contains("sorry")) {
			Logger.error("Attempt to access " + s + " failed.");
			return false;
		}
		if ((t2-t1) > MAX) {
			Logger.log(s);
			Logger.log(url);
			Logger.log("It took " + (t2-t1) + " ms to run the test GeoNames. Server may be down.", "errors");				
			return false;
		}
		return true;
	}
	
	public static boolean GeoNamesRDFIsUp() {
		String url_rdf = "http://sws.geonames.org/"+"5332921"+"/about.rdf";
		Logger.log("Checking Geonames RDF");
		long t1 = System.currentTimeMillis();
		RDFReaderOpenRDF rdfReader = new RDFReaderOpenRDF(url_rdf);
		long t2 = System.currentTimeMillis();
		if ((t2-t1) > MAX) {
			Logger.log(url_rdf);
			Logger.log("It took " + (t2-t1) + " ms to run the test query GeoNamesRDF. Server may be down.", "errors");				
			return false;
		}
		Model model = rdfReader.filterWeb(null, null, null);
		Set<Resource> subjs = model.subjects();
		Iterator<Resource> it = subjs.iterator();
		return (it.hasNext());
	}
	
	public static boolean FactForgeIsUP() {
		try {
			Logger.log("Checking FactForge");
			long t1 = System.currentTimeMillis();
			JsonObject json = QuerySparqlEndpoints.getResultAsJsonUsingHttpGetFactForge(C.STANDARD_SPARQLQUERY);			
			long t2 = System.currentTimeMillis();
			if ((t2-t1) > MAX_FACTFORGE) {
				Logger.log(json);
				Logger.log("It took " + (t2-t1) + " ms to run a standard query FactForge. Server may be down.", "errors");				
				return false;
			}
		} catch (Exception e) {
			Logger.log("FactForge does not seem to be up.", e);
			return false;
		}
		return true;
	}
	
	public static boolean DBPediaIsUP() {
		try {
			Logger.log("Checking DBPedia");
			long t1 = System.currentTimeMillis();
			String result = QuerySparqlEndpoints.getResultAsText(C.STANDARD_SPARQLQUERY, C.DBPEDIA_ENDPOINT);
			long t2 = System.currentTimeMillis();
			if ((t2-t1) > MAX) {
				Logger.log(result);
				Logger.log("It took " + (t2-t1) + " ms to run a standard query DBPedia. Server may be down.", "errors");
				return false;
			}
		} catch (Exception e) {
			Logger.log("DBPedia does not seem to be up." , e );
			return false;
		}
		return true;
	}


}
