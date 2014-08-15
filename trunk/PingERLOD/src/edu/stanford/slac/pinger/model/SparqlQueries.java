package edu.stanford.slac.pinger.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.model.general.GeneralModelSingletonSequential;

class NodeAndCountry {
	String node, country;
	NodeAndCountry(String node, String country) { this.node = node; this.country = country; }
	public String toString() {
		return "{"+node+":\""+country+"\"}";				
	}
}	

public class SparqlQueries {


	public static void main(String args[]) {
		buildMonitoringMonitored();
	}
	
	public static void buildMonitoringMonitored() {
		
		class MonitoringMonitred {
			String fromCountry;
			ArrayList<NodeAndCountry> remotes;		
		}
		String query = C.join(new String[]{
				"SELECT ?SourceName ?CountrySource ?DestinationName ?CountryDest  WHERE { ",
				"	  ?metric MD:hasSourceNodeInformation ?source . ",
				"	  ?metric MD:hasDestinationNodeInformation ?dest . ",
					  
				"	  ?source MD:hasNodeInformation ?sn . ",
				"	  ?sn rdf:type MD:SourceName . ",
				"	  ?sn MD:SourceNameValue ?SourceName . ",
					  
				"	  ?source MGC:isInTown ?TownSource . ",
				"	  ?TownSource MGC:GeoCountry ?CountrySource . ",
					  
				"	  ?dest MD:hasNodeInformation ?dn . ",
				"	  ?dn rdf:type MD:SourceName . ",
				"	  ?dn MD:SourceNameValue ?DestinationName . ",
					  
				"	  ?dest MGC:isInTown ?TownDest . ",
				"	  ?TownDest MGC:GeoCountry ?CountryDest . ",
					  
				"} order by ?SourceName ?DestinationName "	
		}, "\n");
	
		GeneralModelSingletonSequential gm = GeneralModelSingletonSequential.getInstance();
		String jsonResult = gm.queryResultAsJSON(query);
		JsonObject result = (JsonObject) new JsonParser().parse(jsonResult);		
		HashSet<String> monitorings = new HashSet<String>();
		
		JsonArray jArr = result.get("results").getAsJsonObject().get("bindings").getAsJsonArray();
		HashMap<String, MonitoringMonitred> MonitoringMonitoredMap = new HashMap<String, MonitoringMonitred>();
		for (int i = 0; i < jArr.size(); i++ ) {
			JsonObject row = jArr.get(i).getAsJsonObject();
			
			String monitoring = row.get("SourceName").getAsJsonObject().get("value").getAsString();
			
			if (!monitorings.contains(monitoring)) monitorings.add(monitoring);
			
			
			if (!MonitoringMonitoredMap.containsKey(monitoring)) {
				
				MonitoringMonitred mm = new MonitoringMonitred();
				
				String fromCountry = row.get("CountrySource").getAsJsonObject().get("value").getAsString();
				String remoteNode = row.get("DestinationName").getAsJsonObject().get("value").getAsString();
				String remoteCountry = row.get("CountryDest").getAsJsonObject().get("value").getAsString();

				mm.fromCountry = fromCountry;				

				NodeAndCountry nc = new NodeAndCountry(remoteNode, remoteCountry);
				mm.remotes = new ArrayList<NodeAndCountry>();		
				mm.remotes.add(nc);
				
				MonitoringMonitoredMap.put(monitoring, mm);				
			} else {

				MonitoringMonitred mm = MonitoringMonitoredMap.get(monitoring);
			
				String remoteNode = row.get("DestinationName").getAsJsonObject().get("value").getAsString();
				String remoteCountry = row.get("CountryDest").getAsJsonObject().get("value").getAsString();
				
				NodeAndCountry nc = new NodeAndCountry(remoteNode, remoteCountry);
				mm.remotes.add(nc);
			}
		}
			
		
		JsonObject MonitoringMonitoredJson = new JsonObject();
		
		for (String monitoring : MonitoringMonitoredMap.keySet()) {
			MonitoringMonitred mm = MonitoringMonitoredMap.get(monitoring);
			JsonArray arrRemotes =  (JsonArray) new JsonParser().parse(mm.remotes.toString());
			
			JsonObject MonitoringObj = new JsonObject();			
			MonitoringObj.addProperty("fromCountry", mm.fromCountry);
			MonitoringObj.add("remotes", arrRemotes);	
			
			MonitoringMonitoredJson.add(monitoring, MonitoringObj);			
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(MonitoringMonitoredJson);		
		C.writeIntoFile(json, C.MONITORING_MONITORED_COUNTRIES);
		
		json = "var MonitoringMonitoredCountries = " + json;
		C.writeIntoFile(json, C.MONITORING_MONITORED_COUNTRIES_WWW);
		
		
		
		
		
		
		
		
		
		/*
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(result);		
		C.writeIntoFile(json, "data/delete.json");
		*/
		//System.out.println(result);
		
	}
	
	
	
}
