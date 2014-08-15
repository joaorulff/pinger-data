package edu.stanford.slac.pinger.etl.pre_extractor.time;

import java.util.ArrayList;
import java.util.HashMap;

import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.utils.MeasurementUtils;

public class GenerateTimeMap {

	public static HashMap<String, Long> getMap() {
		HashMap<String, Long> timeMap = new HashMap<String, Long>();
		ArrayList<String> years = MeasurementUtils.getYears();
		ArrayList<String> allmonthly = MeasurementUtils.generateMonthly();
		ArrayList<String> alldaily = MeasurementUtils.generateDaily();

		long sequential = 1;

		Logger.log("Instantiating years...","measurement_parameters");
		for (String year : years) {
			timeMap.put(year, sequential++);
		}
		Logger.log("Instantiating allmonthly...","measurement_parameters");
		for (String monthly : allmonthly) {
			timeMap.put(monthly, sequential++);
		}

		Logger.log("Instantiating alldaily...","measurement_parameters");
		for (String daily : alldaily) {
			timeMap.put(daily, sequential++);
		}
		return timeMap;
	}

}
