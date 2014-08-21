package edu.stanford.slac.pinger.main;

import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.utils.MeasurementUtils;
import edu.stanford.slac.pinger.general.utils.Utils;
import edu.stanford.slac.pinger.main.commons.MainCommons;

public class GenerateInputDataset {

	public static void main(String[] args) {
		if (args.length == 0) {
			args = new String[]{
				"debug=0",
				"inputDatasetFile=./Input.dataset"
			};
		}	
		start(args);
	}
	
	public static void start(String[] args) {

		String inputDatasetFile = null;
		try {
			for (String arg : args) {
				if (arg.contains("debug")) {
					MainCommons.debug(arg);
				} else if (arg.contains("inputDatasetFile")) {
					inputDatasetFile = arg.replace("inputDatasetFile=", "");
				} 
			}
		} catch (Exception e) {
			Logger.log("start", e, "errors");
		}
		
		JsonObject monitorMonitoredJson = Utils.getMonitorMonitoredJSON();
		if (monitorMonitoredJson == null) {
			Logger.log(GenerateInputDataset.class + "Json Null", "errors");
			return;
		}

		StringBuilder output = new StringBuilder();
		output.append("METRIC;TICK;MONITOR\n");
		for (String metric : MeasurementUtils.METRICS) {
			for (String tick : MeasurementUtils.TICKS) {
				for (Entry<String, JsonElement> entry : monitorMonitoredJson.entrySet()) {
					String monitor = entry.getKey();
					output.append(metric+";"+tick+";"+monitor+"\n");
				}
			}
		}

		Utils.writeIntoFile(output.toString(), inputDatasetFile);
		
		Logger.log("Input dataset was generated!");
	}

	
}
