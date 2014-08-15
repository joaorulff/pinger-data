package edu.stanford.slac.pinger.main;

import java.util.HashMap;

import com.google.gson.JsonArray;

import edu.stanford.slac.pinger.etl.loader.local.FileHandler;
import edu.stanford.slac.pinger.etl.transformer.PingMeasurementCSVBuilder;
import edu.stanford.slac.pinger.etl.transformer.CSVProcessorFromFile;
import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.main.commons.MainCommons;

public class TransformAndSavePingtableCSV {

	public static void main(String[] args) {
		
		if (args.length == 0) {
			args = new String[]{
				"debug=0",
				"transformFile=1,inputFilePath=./downloadedCSV/throughput_allyearly_100_pinger.slac.stanford.edu.csv,transformedFilesDirectory=./transformedFiles,monitorNode=pinger.slac.stanford.edu,metric=throughput,tick=last365days",
			};
		}		
	
		start(args);
	}

	public static void transformFile(String arg) {
			
		String ags[] = arg.split(",");
		String inputFilePath = null;
		String monitorNode = null;	
		String metric = null;
		String tickParameter = null;
		String transformedFilesDirectory = null;
		for (String ag : ags) {
			if (ag.contains("metric")) {
				metric = ag.replace("metric=", "").trim();
			} else if (ag.contains("inputFilePath")) {
				inputFilePath = ag.replace("inputFilePath=", "").trim();
			} else if (ag.contains("tick")) {
				tickParameter = ag.replace("tick=", "").trim();
			} else if (ag.contains("monitorNode")) {
				monitorNode = ag.replace("monitorNode=", "").trim();				
			} else if (ag.contains("transformedFilesDirectory")) {
				transformedFilesDirectory = ag.replace("transformedFilesDirectory=", "").trim();				
			} 
		}
		
		CSVProcessorFromFile csvProcessor = new CSVProcessorFromFile(inputFilePath); 
		HashMap<String,HashMap<String, String>> map = csvProcessor.getMap();
		if (map==null) return;
		
		FileHandler outputFileHandler = new FileHandler(transformedFilesDirectory, tickParameter, metric); 
		JsonArray monitoredArr = C.getMonitorMonitoredJSON().get(monitorNode).getAsJsonArray();
	
		PingMeasurementCSVBuilder measurement = new PingMeasurementCSVBuilder(outputFileHandler, monitorNode, map, monitoredArr, metric, C.DEFAULT_PACKET_SIZE, tickParameter);
		measurement.run();
		
		outputFileHandler.writeTriplesAndClean();
	}

	public static void start(String[] args) {	
		try {
			long t1 = System.currentTimeMillis();
			for (String arg : args) {
				if (arg.contains("debug")) {
					MainCommons.debug(arg);
				} if (arg.contains("transformFile=1")) {
					transformFile(arg);
				}
			}
			long t2 = System.currentTimeMillis();
			Logger.log("Finally done! It took " + ((t2-t1)/1000.0/60.0) + " minutes.");
		} catch (Exception e) {
			Logger.log("start", e, "errors");
		}
	}

}
