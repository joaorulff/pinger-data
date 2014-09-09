package edu.stanford.slac.pinger.main.pre;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.utils.MeasurementUtils;
import edu.stanford.slac.pinger.general.utils.NodesUtils;
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

		
		ArrayList<String> sourceNodes = NodesUtils.getSourceNodes();

		String tick = "daily";
		StringBuilder output = new StringBuilder();
		//output.append("YEAR;MONTH;METRIC;TICK;MAXATTEMPT;TIMEOUT\n");
		output.append("YEAR;METRIC;TICK;MONITOR\n");
		ArrayList<String> years = new ArrayList<String>();
		for (int i = 2003; i <= 2014; i++) {
			years.add(i+"");
		}
		
		String MAXATTEMPT = "35";
		String TIMEOUT = "500";
		
		for (String monitor : sourceNodes)
		for (String year : years) 
			for (String metric : MeasurementUtils.METRICS)
				//for (String tick : MeasurementUtils.TICKS)
				for (String month : Utils.getMonths()) 
						//output.append(year+";"+month+";"+metric+";"+tick+";"+MAXATTEMPT+";"+TIMEOUT+"\n");
						output.append(year+";"+metric+";"+tick+";"+monitor+"\n");

		Utils.writeIntoFile(output.toString(), inputDatasetFile);

		Logger.log("Input dataset was generated!");
	}


}
