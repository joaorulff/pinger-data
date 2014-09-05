package edu.stanford.slac.pinger.main.pre.dimensions;

import java.util.ArrayList;

import com.google.gson.JsonObject;

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
		 
		 for (String sourceNode : sourceNodes) {
			 
			 
			 JsonObject eachNodeDetails = nodeDetails.get(sourceNode).getAsJsonObject();
			 String nodeName =  eachNodeDetails.get("SourceName").getAsString();
			 
			 
			 
		 }
		 
		 
		 

	}

}
