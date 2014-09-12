package edu.stanford.slac.pinger.main.pre;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.utils.Utils;

public class CreateNodeDetailsJson {	

	public static void main(String[] args)  {
		StringBuilder nodeDetails = new StringBuilder("");
		StringBuilder urlContent = new StringBuilder("");

		try {
			URL nodesCfUrl = new URL(C.NODE_DETAILS_CF);
			URLConnection uc = nodesCfUrl.openConnection();

			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String inputLine = null;

			Boolean descriptionBeginning = false;
			Boolean descriptionEnding = false;

			while ((inputLine = in.readLine()) != null && descriptionBeginning == false){
				urlContent.append(inputLine + "\n");

				if (inputLine.startsWith("%NODE_DETAILS")){
					descriptionBeginning = true;
				}
			}

			urlContent.append(inputLine + "\n");
			nodeDetails.append(inputLine + "\n");

			while ((inputLine = in.readLine()) != null && descriptionEnding == false){
				urlContent.append(inputLine + "\n");

				if (inputLine.startsWith(");")){
					descriptionEnding = true;
				}else{
					nodeDetails.append(inputLine + "\n");
				}
			}

			while ((inputLine = in.readLine()) != null){
				urlContent.append(inputLine + "\n");
			}

			in.close();

		} catch (IOException e) {
			Logger.error(e);
		}

		String[] eachNodeDetails = nodeDetails.toString().split("],\n\n");	
		String[] temp = null;
		String nodeName = null;
		String nodeInfo = null;
		String[] nodeLineInfo = null;

		String value = null;
		int nodeID = 0;

		StringBuilder nodeDetailsContent = new StringBuilder("{\n\n");

		Pattern latlongPattern = Pattern.compile("([0-9]+([.][0-9]+)?)([ ]|[\t])([0-9]+([.][0-9]+)?)");

		for (int i = 0; i < eachNodeDetails.length; i++){
			try {
				temp = eachNodeDetails[i].split(" => [^-a-zA-Z0-9]");
				nodeName = temp[0].trim();
				nodeInfo = temp[1].trim();

				nodeLineInfo = nodeInfo.split("\",\n");

				for (int j = 0; j < nodeLineInfo.length; j++){
					value = nodeLineInfo[j].trim();

					switch (j) {
					case 0:
						nodeID++;
						nodeDetailsContent.append("\t" + nodeName + ": {\n");
						nodeDetailsContent.append("\t\t\"NodeID\":\"" + nodeID + "\",\n");
						nodeDetailsContent.append("\t\t\"NodeName\":" + nodeName + ",\n");
						nodeDetailsContent.append("\t\t\"NodeIP\":" + value.substring(1).trim() + "\",\n");	//Ignore the initial "["
						break;
					case 1:
						nodeDetailsContent.append("\t\t\"NodeSiteName\":" + value + "\",\n");
						break;
					case 2:
						nodeDetailsContent.append("\t\t\"NodeNickName\":" + value + "\",\n");
						break;
					case 3:
						nodeDetailsContent.append("\t\t\"NodeFullName\":" + value + "\",\n");
						break;
					case 4:
						nodeDetailsContent.append("\t\t\"LocationDescription\":" + value + "\",\n");
						break;
					case 5:
						nodeDetailsContent.append("\t\t\"Country\":" + value + "\",\n");
						break;
					case 6:
						nodeDetailsContent.append("\t\t\"Continent\":" + value + "\",\n");
						break;
					case 7:
						try {
							if (value.contains("\"\"") || value.contains("NOT-SET")) {
								nodeDetailsContent.append("\t\t\"Latitude\":\"\",\n");
								nodeDetailsContent.append("\t\t\"Longitude\":\"\",\n");

							} else {
								Matcher m = latlongPattern.matcher(value);
								if( m.matches()) {
									nodeDetailsContent.append("\t\t\"Latitude\":\"" + m.group(1).trim() + "\",\n");
									nodeDetailsContent.append("\t\t\"Longitude\":\"" + m.group(4).trim() + "\",\n");
								}
							}
						} catch (Exception e) {
							System.out.println(value);
						}
						break;
					case 8:
						nodeDetailsContent.append("\t\t\"ProjectType\":" + value + "\",\n");
						break;
					case 9:
						nodeDetailsContent.append("\t\t\"PingServer\":" + value + "\",\n");
						break;
					case 10:
						nodeDetailsContent.append("\t\t\"TraceServer\":" + value + "\",\n");
						break;
					case 11:
						nodeDetailsContent.append("\t\t\"DataServer\":" + value + "\",\n");
						break;
					case 12:
						nodeDetailsContent.append("\t\t\"NodeURL\":" + value + "\",\n");
						break;
					case 13:
						nodeDetailsContent.append("\t\t\"NodeGMT\":" + value + "\",\n");
						break;
					case 14:
						nodeDetailsContent.append("\t\t\"Group\":" + value + "\",\n");
						break;
					case 15:
						nodeDetailsContent.append("\t\t\"AppUser\":" + value + "\",\n");
						break;
					case 16:
						nodeDetailsContent.append("\t\t\"ContactInformation\":" + value + "\",\n");
						break;
					case 17:
						if (value.contains("\n")) {
							value = value.replace("\n", " ");
						}
						nodeDetailsContent.append("\t\t\"NodeComments\":" + value.substring(0, value.length()-1) + "\n");	//The last line of node description doesn't end with comma
						break;
					default:
						break;
					}
				}
				nodeDetailsContent.append("\t},");

			} catch (Exception e) {
				Logger.error(e);
				continue;
			}
		}	

		String nodeDetailsContentStr = Utils.removeLastCharacterFromString(nodeDetailsContent.toString());
		nodeDetailsContentStr += "\n}";

		Utils.writeIntoFile(urlContent.toString(), C.PERL_DIR+"nodes.cf");
		Utils.writeIntoFile(nodeDetailsContentStr, C.NODEDETAILS_JSON_FILE);

		try {
			Utils.getNodeDetails();
		} catch (Exception e) {
			Logger.error(e);
		}
	}

}
