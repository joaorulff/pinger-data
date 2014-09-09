package edu.stanford.slac.pinger.main.pre;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.utils.NodesUtils;
import edu.stanford.slac.pinger.general.utils.Utils;

public class CreateMonitoringMonitoredJson {	

	public static void generateMonitoringMonitoredJSON() {
		Logger.setDebugLevel(1);
		JsonObject MonitoringNodes = new JsonObject();
		ArrayList<String> sourceList = NodesUtils.getSourceNodes();
		if (sourceList==null) {
			Logger.error("Could not generate MonitoringNodes JSON", "errors");
			return;
		}
		for (String sourceNode : sourceList) {
			ArrayList<String> destinationNodeLst = NodesUtils.getDestinationNodesFromSourceNode(sourceNode);
			if (destinationNodeLst!=null) {
				JsonArray monitoredArr = (JsonArray) new JsonParser().parse(destinationNodeLst.toString());
				MonitoringNodes.add(sourceNode, monitoredArr);
			}
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(MonitoringNodes);		
		Utils.writeIntoFile(json, C.MONITORING_MONITORED_JSON_FILE);
	}
	
	public static void main(String[] args)  {
		generateMonitoringMonitoredJSON();
	}

}
