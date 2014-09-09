package edu.stanford.slac.pinger.main;

import edu.stanford.slac.pinger.general.Logger;

public class RunAll {

	public static void main(String[] args) {
		Logger.setDebugLevel(0);
		Logger.log("Running " + GenerateERelation.class.getName());
		GenerateERelation.main(args);
		Logger.log("Running " + GetPingtableCSV.class.getName());
		GetPingtableCSV.main(args);
		Logger.log("Running " + TransformAndSavePingtableCSV.class.getName());
		TransformAndSavePingtableCSV.main(args);
	}

}
