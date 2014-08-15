package edu.stanford.slac.pinger.instantiator.measurement.tsv;

import java.util.Arrays;
import java.util.HashMap;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;

public class TSVProcessorFromFile {

	private String fromNickName, metric, packetSize, tickParameter;
	public TSVProcessorFromFile(String fromNickName, String metric, String packetSize, String tickParameter) {
		this.fromNickName = fromNickName;
		this.metric = metric;
		this.packetSize = packetSize;
		this.tickParameter = tickParameter;
	}
 	
	public HashMap<String,HashMap<String, String>> getMap() {
		String dirPath = C.TSV_DIR+metric+"/"+tickParameter;
		String filePath = dirPath+"/"+fromNickName+"_"+packetSize+".tsv";
		String tsv = null;
		tsv = C.readFile(filePath);
		if (tsv==null) return null;
		String []lines = tsv.split("\n");
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
		String []line = null;
		try {
			for (String line_str : lines) {
				line = line_str.split("\\s");
				if (line.length==0)continue;
				HashMap<String,String> timeValue = new HashMap<String, String>();
				for (int j = start; j < end; j++) {
					try {
					if (!line[j].equals("."))
						timeValue.put(times.get(j), line[j]);
					} catch (Exception e) {
						Logger.log("TSVProcessorFromFile.mapMetrics " + line_str, e, "errors");
					}
				}
				mapMetrics.put(line[remoteNodeIndex], timeValue);
			}
		} catch (Exception e) {
			Logger.error(Arrays.toString(line), e);
		}
		return mapMetrics;
	}
	
	
}
