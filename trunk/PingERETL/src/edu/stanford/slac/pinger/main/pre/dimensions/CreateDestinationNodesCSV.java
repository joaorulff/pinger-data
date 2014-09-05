package edu.stanford.slac.pinger.main.pre.dimensions;

import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.beans.NetworkNodeBean;
import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.utils.Utils;

public class CreateDestinationNodesCSV {

	public static void main(String[] args) {
		
		 JsonObject nodeDetails = Utils.getNodeDetails();
		 
		 int id = 1;
		 
		 StringBuilder sb = new StringBuilder();
		 sb.append(NetworkNodeBean.CSV_HEADER);
		 
		 for (Entry<String, JsonElement> eachNodeDetailsEntry : nodeDetails.entrySet()) {
			 
			 JsonObject eachNodeDetails = (JsonObject) eachNodeDetailsEntry.getValue();
			 
			 String nodeName =  eachNodeDetails.get("SourceName").getAsString();
			 String nodeIP =  eachNodeDetails.get("SourceIP").getAsString();
			 String idStr = String.valueOf(id++);
			 
			 
			 NetworkNodeBean nb = new NetworkNodeBean(idStr, nodeName, nodeIP);
			 
			 
			 sb.append(nb.toString(','));
			 
			 
			 
		 }
		 
		 Utils.writeIntoFile(sb.toString(), C.DESTINATION_NODE_CSV);
		 

	}

}
