package edu.stanford.slac.pinger.main.pre;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.utils.Utils;

public class CreateNodeDetailsJson {	

	public static void main(String[] args)  {
		String nodeDetails = "";
		String urlContent = "";
		
		try {
			URL nodesCfUrl = new URL("http://www-iepm.slac.stanford.edu/pinger/pingerworld/nodes.cf");
			URLConnection uc = nodesCfUrl.openConnection();

			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String inputLine = null;

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

		} catch (IOException e) {
			Logger.error(e);
		}

		String[] eachNodeDetails = nodeDetails.split("],\n\n");	
		String[] temp = null;
		String nodeName = null;
		String nodeInfo = null;
		String[] nodeLineInfo = null;

		String value = null;
		int nodeID = 0;

		String nodeDetailsContent = "{\n\n";

		for (int i = 0; i < eachNodeDetails.length; i++){
			try {
				temp = eachNodeDetails[i].split("=>");
				nodeName = temp[0].trim();
				nodeInfo = temp[1].trim();

				nodeLineInfo = nodeInfo.split("\",\n");

				for (int j = 0; j < nodeLineInfo.length; j++){
					value = nodeLineInfo[j].trim();

					switch (j) {
						case 0:
							nodeID++;
							nodeDetailsContent += "\t" + nodeName + ": {\n"
												+ "\t\t\"NodeID\":\"" + nodeID + "\",\n"
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
							if (!value.contains("\"\"") && !value.contains("NOT-SET")) {
								String[] coord = value.split(" ");
								nodeDetailsContent += "\t\t\"Latitude\":" + coord[0].trim() + "\",\n"
													+ "\t\t\"Longitude\":\"" + coord[1].trim() + "\",\n";
							} else {
								nodeDetailsContent += "\t\t\"Latitude\":" + "\"\" " + "\",\n"
													+ "\t\t\"Longitude\":\"" + "\"\" " + "\",\n";
							}
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
							if (value.contains("\n")) {
								value = value.replace("\n", " ");
							}
							nodeDetailsContent += "\t\t\"NodeComments\":" + value.substring(0, value.length()-1) + "\n";	//The last line of node description doesn't end with comma
							break;
						default:
							break;
					}
				}
				nodeDetailsContent += "\t},\n\n";

			} catch (Exception e) {
				Logger.error(e);
				continue;
			}
		}	

		Utils.writeIntoFile(urlContent, "./data/perl/nodes.cf");
		Utils.writeIntoFile(nodeDetailsContent, "./data/json/NodeDetails.json");
	}

}
