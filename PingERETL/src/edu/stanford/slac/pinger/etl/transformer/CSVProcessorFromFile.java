package edu.stanford.slac.pinger.etl.transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.utils.Utils;

public class CSVProcessorFromFile {

	private String inputDir;
	private String year, metric, tick, monitor;
	
	public CSVProcessorFromFile(String inputDir, String year, String metric, String tick, String monitor) {
		this.inputDir = inputDir;
		this.year = year;
		this.metric = metric;
		this.tick = tick;
		this.monitor = monitor;
	}
	
	public ArrayList<HashMap<String,HashMap<String, String>>> getMonthsInAnYearMaps() {
		 ArrayList<HashMap<String,HashMap<String, String>>> monthsInAnYearMaps = new  ArrayList<HashMap<String,HashMap<String, String>>>();
		 for (String month : Utils.getMonths()) {
			 HashMap<String,HashMap<String, String>> map = getMap(month);
			 if (map!=null)
				 monthsInAnYearMaps.add(map);
		 }
		 return monthsInAnYearMaps;
	}
	
	private HashMap<String,HashMap<String, String>> getMap(String month) {
		String filePath = inputDir + Utils.getFileNameBeginning(tick, metric, year, monitor) +"_"+month+".csv";
		String csv = Utils.readFile(filePath);
		if (csv==null) {
			Logger.error("Error code 01: File does not exist: " + filePath);
			return null;
		}
		String []lines = csv.split("\n");
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

		//The next block retrieves the actual values, after the first line (i.e., header)
		HashMap<String,HashMap<String, String>> mapMetrics = new HashMap<String,HashMap<String, String>>();
		String []line = null;
		try {
			for (int i = 1; i < lines.length; i++) {
				String line_str = lines[i];
				line = line_str.split("\\s");
				if (line.length==0)continue;
				HashMap<String,String> timeValue = new HashMap<String, String>();
				for (int j = start; j < end; j++) {
					try {
						if (!line[j].equals(".")) {
							try {
								Float.parseFloat(line[j]);
								timeValue.put(times.get(j), line[j]);
							} catch (Exception e) {
								Logger.error("Ooops " + line[j] + " is not a number! It was not added to the measurement table.",  e);
								continue;
							}
						}
					} catch (Exception e) {
						Logger.log(CSVProcessorFromFile.class.getName()+".mapMetrics " + line_str, e, "errors");
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
