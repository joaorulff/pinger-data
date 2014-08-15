package edu.stanford.slac.pinger.main.commons;

import edu.stanford.slac.pinger.general.C;

public class MainCommons {
	public static void debug(String arg) {
		if (arg.contains("debug=0")) {
			C.DEBUG_LEVEL=0;
		} else if (arg.contains("debug=-1")) {
			C.DEBUG_LEVEL=-1;
		}
	}
}
