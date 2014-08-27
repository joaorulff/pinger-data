package edu.stanford.slac.pinger.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import edu.stanford.slac.pinger.general.utils.Utils;

public class CreateNodeDetailsJson {	
	
	public static void main(String[] args) throws IOException {
		URL nodesCfUrl = new URL("http://www-iepm.slac.stanford.edu/pinger/pingerworld/nodes.cf");
		URLConnection uc = nodesCfUrl.openConnection();
		String urlContent = "";
		
		BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		String inputLine = null;
		
		String nodesFilePath = "./data/perl/nodes.cf";
		String nodeDetailsJsonFilePath = "./data/json/NodeDetails.json";
		
		String nodeDetails = "";
		
		Boolean descriptionBeginning = false;
		Boolean descriptionEnding = false;
		
		while ((inputLine = in.readLine()) != null && descriptionBeginning == false){
			urlContent += inputLine + "\n";
			
			if (inputLine.startsWith("%NODE_DETAILS")){
				descriptionBeginning = true;
			}
		}
		
		urlContent += inputLine + "\n";
		nodeDetails += inputLine + "\n";
		
		while ((inputLine = in.readLine()) != null && descriptionEnding == false){
			urlContent += inputLine + "\n";
			
			if (inputLine.startsWith(");")){
				descriptionEnding = true;
			}else{
				nodeDetails += inputLine + "\n";
			}
		}
		
		while ((inputLine = in.readLine()) != null){
			urlContent += inputLine + "\n";
		}
		
		in.close();
		
		String[] eachNodeDetails = nodeDetails.split("],\n\n");	
		String[] temp = null;
		String nodeName = null;
		String nodeInfo = null;
		String[] nodeLineInfo = null;
				
		String value = null;
		int nodeID = 0;
		
		String nodeDetailsContent = "{\n\n";
		
		for (int i = 0; i < eachNodeDetails.length; i++){			
			temp = eachNodeDetails[i].split("=>");
			nodeName = temp[0].trim();
			nodeInfo = temp[1].trim();
			
			nodeLineInfo = nodeInfo.split("\",\n");
			
			for (int j = 0; j < nodeLineInfo.length; j++){
				value = nodeLineInfo[j].trim();
				
				switch (j) {
					case 0:
						nodeID++;
						nodeDetailsContent += "\t{\n\t\t\"NodeID\":\"" + nodeID + "\",\n"
											+ "\t\t\"NodeName\":" + nodeName + ",\n"
											+ "\t\t\"NodeIP\":" + value.substring(1).trim() + "\",\n";	//Ignore the initial "["
						break;
					case 1:
						nodeDetailsContent += "\t\t\"NodeSiteName\":" + value + "\",\n";
						break;
					case 2:
						nodeDetailsContent += "\t\t\"NodeNickName\":" + value + "\",\n";
						break;
					case 3:
						nodeDetailsContent += "\t\t\"NodeFullName\":" + value + "\",\n";
						break;
					case 4:
						nodeDetailsContent += "\t\t\"LocationDescription\":" + value + "\",\n";
						break;
					case 5:
						nodeDetailsContent += "\t\t\"Country\":" + value + "\",\n";
						break;
					case 6:
						nodeDetailsContent += "\t\t\"Continent\":" + value + "\",\n";
						break;
					case 7:
						String[] coord = value.split(" ");
						nodeDetailsContent += "\t\t\"Latitude\":" + coord[0].trim() + "\",\n"
											+ "\t\t\"Longitude\":\"" + coord[1].trim() + "\",\n";
						break;
					case 8:
						nodeDetailsContent += "\t\t\"ProjectType\":" + value + "\",\n";
						break;
					case 9:
						nodeDetailsContent += "\t\t\"PingServer\":" + value + "\",\n";
						break;
					case 10:
						nodeDetailsContent += "\t\t\"TraceServer\":" + value + "\",\n";
						break;
					case 11:
						nodeDetailsContent += "\t\t\"DataServer\":" + value + "\",\n";
						break;
					case 12:
						nodeDetailsContent += "\t\t\"NodeURL\":" + value + "\",\n";
						break;
					case 13:
						nodeDetailsContent += "\t\t\"NodeGMT\":" + value + "\",\n";
						break;
					case 14:
						nodeDetailsContent += "\t\t\"Group\":" + value + "\",\n";
						break;
					case 15:
						nodeDetailsContent += "\t\t\"AppUser\":" + value + "\",\n";
						break;
					case 16:
						nodeDetailsContent += "\t\t\"ContactInformation\":" + value + "\",\n";
						break;
					case 17:
						nodeDetailsContent += "\t\t\"NodeComments\":" + value.substring(0, value.length()-1) + "\n";	//The last line of description node doesn't end with comma
						break;
					default:
						break;
				}
			}
			nodeDetailsContent += "\t}\n\n";
		}	
		
        Utils.createFileGrantingPermissions(nodesFilePath);
        Utils.writeIntoFile(urlContent, nodesFilePath);
        
        Utils.createFileGrantingPermissions(nodeDetailsJsonFilePath);
        Utils.writeIntoFile(nodeDetailsContent, nodeDetailsJsonFilePath);
	}

}
