package edu.stanford.slac.pinger.rest.json;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.rest.pingtable.HttpGetter;

public class GenerateMonitoringJSON {

	public static void main(String[] args) {
		//start(false,true);
		generateMonitoringMonitoredGroupedJSON();
	}
	public static void start(boolean generateMonitoringMonitred, boolean generateGrouped, boolean generateMonitoringMonitoredGrouped, boolean generateMonitoringGroupedForTSV) {
		if (generateMonitoringMonitred)
			generateMonitoringMonitoredJSON();
		if (generateGrouped)
			generateGroupedMonitoringJSON();
		if (generateMonitoringMonitoredGrouped)
			generateMonitoringMonitoredGroupedJSON();
		if (generateMonitoringGroupedForTSV) 
			generateGroupedMonitoringJSONForTSV();
	}
	private static void generateGroupedMonitoringJSON() {
		JsonObject MonitoringNodesGrouped = new JsonObject();		

		ArrayList<String> monitoringLst = getMonitoring();
		if (monitoringLst==null) {
			Logger.log("Could not generate MonitoringNodes JSON");
			return;
		}
		int numThreads = monitoringLst.size();
		int breakLimit = (int) monitoringLst.size()/numThreads;				
		int groupNumber = 0;
		JsonArray monitoringArr = null; 
		for (int i = 0; i <= monitoringLst.size(); i++) {
			if (i % breakLimit == 0) {
				MonitoringNodesGrouped.add("threadMonitoring"+groupNumber++, monitoringArr);
				monitoringArr = new JsonArray();
			}
			if (i<monitoringLst.size()) {
				String monitoringHost = monitoringLst.get(i);
				monitoringArr.add(new JsonPrimitive(monitoringHost));
			}
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(MonitoringNodesGrouped);
		C.writeIntoFile(json, C.MONITORING_NODES_GROUPED);
	}
	private static void generateGroupedMonitoringJSONForTSV() {
		JsonObject MonitoringNodesGrouped = new JsonObject();		

		ArrayList<String> monitoringLst = getMonitoring();
		if (monitoringLst==null) {
			Logger.log("Could not generate MonitoringNodes JSON");
			return;
		}
		int numThreads = C.NUM_THREADS_MONITORING_NODES_TSV;
		if (numThreads > monitoringLst.size()) numThreads = monitoringLst.size();
		int breakLimit = (int) monitoringLst.size()/numThreads;				
		int groupNumber = 0;
		JsonArray monitoringArr = null; 
		for (int i = 0; i <= monitoringLst.size(); i++) {
			if (i % breakLimit == 0) {
				MonitoringNodesGrouped.add("threadMonitoring"+groupNumber++, monitoringArr);
				monitoringArr = new JsonArray();
			}
			if (i<monitoringLst.size()) {
				String monitoringHost = monitoringLst.get(i);
				monitoringArr.add(new JsonPrimitive(monitoringHost));
			}
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(MonitoringNodesGrouped);
		C.writeIntoFile(json, C.MONITORING_NODES_GROUPED_FOR_TSV);
	}
	private static void generateMonitoringMonitoredGroupedJSON() {
		JsonObject MonitoringMonitoredGrouped = new JsonObject();
		ArrayList<String> monitoringLst = getMonitoring();
		if (monitoringLst==null) {
			Logger.log("Could not generate MonitoringNodes JSON");
			return;
		}
		for (String monitoringHost : monitoringLst) {
			JsonObject monitoringObj = new JsonObject();
			
			ArrayList<String> monitoredLst = getMonitored(monitoringHost);
			if (monitoredLst==null) {
				Logger.log("Could not get monitored list for " + monitoringHost);
				continue;				
			}
			//int breakLimit = 2; //each thread will take care of 2 monitored nodes.
			int breakLimit = 60;
			JsonArray monitoredArr = null;
			int groupNumber = 0;
			for (int i = 0; i <= monitoredLst.size(); i++) {
				if (i % breakLimit == 0) {
					monitoringObj.add("threadMonitored"+groupNumber++, monitoredArr);
					monitoredArr = new JsonArray();
				}
				if (i<monitoredLst.size()) {
					String monitored = monitoredLst.get(i);
					monitoredArr.add(new JsonPrimitive(monitored));
				}
			}			
			monitoringObj.add(monitoringHost, monitoredArr);
			MonitoringMonitoredGrouped.add(monitoringHost, monitoringObj);
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(MonitoringMonitoredGrouped);
		C.writeIntoFile(json, C.MONITORING_MONITORED_GROUPED_JSON_FILE);
		C.setMonitoringMonitoredGroupedJSON(MonitoringMonitoredGrouped);
	}
	private static void generateMonitoringMonitoredJSON() {
		JsonObject MonitoringNodes = new JsonObject();
		ArrayList<String> monitoringLst = getMonitoring();
		if (monitoringLst==null) {
			Logger.error("Could not generate MonitoringNodes JSON", "errors");
			return;
		}
		for (String monitoringHost : monitoringLst) {
			ArrayList<String> monitoredLst = getMonitored(monitoringHost);
			if (monitoredLst!=null) {
				JsonArray monitoredArr = (JsonArray) new JsonParser().parse(monitoredLst.toString());
				MonitoringNodes.add(monitoringHost, monitoredArr);
			}
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(MonitoringNodes);		
		C.writeIntoFile(json, C.MONITORING_MONITORED_JSON_FILE);
		
		json = "var MonitoringMonitored = " + json;
		C.writeIntoFile(json, C.MONITORING_MONITORED_JSON_FILE_WWW);
	}



	private static ArrayList<String> getMonitoring() {
		return getHosts("http://www-wanmon.slac.stanford.edu/cgi-wrap/dbprac.pl?monalias=all", "\\s+([0-9])+\\s+");
	}
	private static ArrayList<String> getMonitored(String monitoringHost) {
		try {
			String nickname = C.getNodeDetails().get(monitoringHost).getAsJsonObject().get("SourceNickName").getAsString();
			String URLMonitoredHosts = "http://www-wanmon.slac.stanford.edu/cgi-wrap/dbprac.pl?monalias="+nickname+"&find=1";
			return getHosts(URLMonitoredHosts, monitoringHost);
		} catch (Exception e) {
			Logger.log(monitoringHost, e, "errors");
			return null;
		}
	}
	private static ArrayList<String> getHosts(String URL, String regexPattern) {
		String content = HttpGetter.readPage(URL);		
		ArrayList<String> hosts = new ArrayList<String>();

		if (content.indexOf("<pre>")!=-1) {
			content = content.split("<pre>")[1].split("</pre>")[0];
			String lines[] = content.split("\n");
			for (String host : lines) {
				String arr[] = host.split(regexPattern);
				if (arr.length > 1) {
					String h = arr[1].replaceAll("\\s", "");
					hosts.add(h);
				}
			}
			return hosts;
		} else
			return null;
	}
}
