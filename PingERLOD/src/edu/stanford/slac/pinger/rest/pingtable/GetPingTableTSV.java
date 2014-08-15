package edu.stanford.slac.pinger.rest.pingtable;

import java.util.HashMap;

import edu.stanford.slac.pinger.general.Logger;

/**
 * This class is to GET the content of Pingtable.
 * The content is in the HTML source code of each measurement in a comment HTML tag.
 * This class supposes that if the HTML content has a message of "Sorry" means that a measurement is not available in those parameters.
 * @author Renan
 *
 */
@Deprecated
public class GetPingTableTSV {


	/**
	 *  This HashMap maps each metric to the pair (time, value). For example,
	 *  
	 * (time, value)
	 * @param from
	 */
	public static HashMap<String,HashMap<String, String>> getMeasurementMap(String from, String metric, String pktSize, String tickParameter) {
		String url =     	
				"http://www-wanmon.slac.stanford.edu/cgi-wrap/pingtable.pl?format=tsv&"+
						"file="+metric+"&"+
						"by=by-node&" +
						"size="+pktSize+"&"+
						"from="+from+"&"+
						"to=WORLD&" +
						"ex=none&only=all&dataset=hep&percentage=any&dnode=on&"+
						tickParameter;
		Logger.log(url);
		String htmlContent = getPingTableTSV(url);
		if (htmlContent==null) {
			Logger.log("Error on getting the map..."+from + " " + metric + " " + pktSize + " " + tickParameter + "\n"+ "URL of the map Error: " + url, "errors");
			return null;
		}
		
		String []lines = htmlContent.split("\n");
		String []head = lines[0].split("\\s");
		
		//The next block retrieves only the times from the header and stores the start and end indexes.
		HashMap<Integer,String> times = new HashMap<Integer,String>();
		boolean nextIsTime = false;
		int start=0, end=0, remoteNodeIndex=0;
		for (int i = 0; i < head.length; i++) {
			if (head[i].equals("?")) {
				nextIsTime = true;
				start = i+1;
				continue;
			}
			if (nextIsTime) {
				times.put(i,head[i]);
				if (head[i+1].equals("Monitoring-Node")) {
					nextIsTime = false;
					end = i;
				}
			} else if (head[i].equals("Remote-Node")) {
				remoteNodeIndex = i;
			}
		}		
		
		HashMap<String,HashMap<String, String>> mapMetrics = new HashMap<String,HashMap<String, String>>();
		for (int i = 1; i < lines.length; i++) {
			HashMap<String,String> timeValue = new HashMap<String, String>();
			String[] line = lines[i].split("\\s");
			for (int j = start; j < end; j++) {
				if (!line[j].equals("."))
					timeValue.put(times.get(j), line[j]);
			}
			mapMetrics.put(line[remoteNodeIndex], timeValue);
		}
		return mapMetrics;
	}

	private static String getPingTableTSV(String URL) {
		String s = HttpGetter.readPage(URL);
		if (s.contains("<h1>Sorry</h1>")) return null;
		return s;
	}
}
