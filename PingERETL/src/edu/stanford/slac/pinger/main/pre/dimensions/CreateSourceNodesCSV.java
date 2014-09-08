package edu.stanford.slac.pinger.main.pre.dimensions;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.beans.NetworkNodeBean;
import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.utils.NodesUtils;
import edu.stanford.slac.pinger.general.utils.Utils;

public class CreateSourceNodesCSV {

	public static void main(String[] args) {
		
		 JsonObject nodeDetails = Utils.getNodeDetails();
		 
		 ArrayList<String> sourceNodes = NodesUtils.getSourceNodes();
		 if (sourceNodes==null) {
			 Logger.error("Could not retrieve source nodes.");
			 return;
		 }
		 
		 int id = 1;
		 		 
		 StringBuilder sb = new StringBuilder();
		 sb.append(NetworkNodeBean.CSV_HEADER);
		 
		 for (String sourceNode : sourceNodes) {
			 
				 JsonObject eachSourceNodeDetails = nodeDetails.get(sourceNode).getAsJsonObject();
				 String nodeID =  eachSourceNodeDetails.get("NodeID").getAsString();
				 String nodeName =  eachSourceNodeDetails.get("NodeName").getAsString();
				 String nodeIP =  eachSourceNodeDetails.get("NodeIP").getAsString();
				 String latitude =  eachSourceNodeDetails.get("Latitude").getAsString();
				 String longitude =  eachSourceNodeDetails.get("Longitude").getAsString();
				 String nodeNickName =  eachSourceNodeDetails.get("NodeNickName").getAsString();
				 String nodeFullName =  eachSourceNodeDetails.get("NodeFullName").getAsString();
				 String nodeSiteName =  eachSourceNodeDetails.get("NodeSiteName").getAsString();
				 String projectType =  eachSourceNodeDetails.get("ProjectType").getAsString();
				 
				 String idStr = String.valueOf(id++);
				 
				 NetworkNodeBean nsb = new NetworkNodeBean(idStr, nodeName, nodeIP, latitude, longitude, nodeNickName, nodeFullName, nodeSiteName, projectType);
				 
				 sb.append(nsb.toString(','));			 
				 
		 }
		 
		 Utils.writeIntoFile(sb.toString(), C.DESTINATION_SOURCE_NODE_CSV);
		 

	}

}
