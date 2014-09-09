package edu.stanford.slac.pinger.main;

import java.util.Arrays;

import edu.stanford.slac.pinger.etl.extractor.pingtabe.PingtableCSVDownloader;
import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.main.commons.MainCommons;

public class GetPingtableCSV {
	public static void main(String[] args) {

		if (args.length == 0) {
			args = new String[]{
					/**
					 *debug=0 -> it prints in stdout most of the log messages;
					 *debug=-1 -> only critical errors 
					 */
					"debug=0",
					"downlaodCSVFiles=1,downloadedCSVDirectory=c:\\downloadedCSV,monitorNodes=[airuniversity.seecs.edu.pk],metrics=[alpha],ticks=[daily],years=[2007],timeout=[2000],maxattempt=[35]",
			};
		}		

		start(args);
	}


	public static void start(String[] args) {	
		try {
			long t1 = System.currentTimeMillis();
			for (String arg : args) {
				if (arg.contains("debug")) {
					MainCommons.debug(arg);
				} else if (arg.contains("downlaodCSVFiles=1")) {
					downlaodCSVFiles(arg);
				} 
			}
			long t2 = System.currentTimeMillis();
			Logger.log("Finally done! It took " + ((t2-t1)/1000.0/60.0) + " minutes.");
		} catch (Exception e) {
			Logger.log("start", e, "errors");
		}
	}

	public static void downlaodCSVFiles(String arg) {
		String ags[] = arg.split(",");
		String metrics[] = null;
		String ticks[] = null;
		String years[] = null;
		String months[] = null;
		String timeouts[] = null;
		String maxattempts[] = null;
		String monitorNodes[] = null;
		String downloadedCSVDirectory = null;
		for (String ag : ags) {
			if (ag.contains("metrics")) {
				String s = ag.replace("metrics=[", "");
				s = s.replace("]", "");
				metrics = s.split("-");
			} else if (ag.contains("ticks")) {
				String s = ag.replace("ticks=[", "");
				s = s.replace("]", "");
				ticks = s.split("-");
			} else if (ag.contains("years")) {
				String s = ag.replace("years=[", "");
				s = s.replace("]", "");
				years = s.split("-");
			}else if (ag.contains("months")) {
				String s = ag.replace("months=[", "");
				s = s.replace("]", "");
				months = s.split("-");
			} else if (ag.contains("timeout")) {
				String s = ag.replace("timeout=[", "");
				s = s.replace("]", "");
				timeouts = s.split("-");
			} else if (ag.contains("maxattempt")) {
				String s = ag.replace("maxattempt=[", "");
				s = s.replace("]", "");
				maxattempts = s.split("-");
			} 
			else if (ag.contains("downloadedCSVDirectory")) {
				downloadedCSVDirectory = ag.replace("downloadedCSVDirectory=", "");
			} else if (ag.contains("monitorNodes")) {
				String s = ag.replace("monitorNodes=[", "");
				s = s.replace("]", "");
				monitorNodes = s.split("-");
			} 
		}

		Logger.FILE_PREFIX += Arrays.toString(metrics).replace("[", "").replace("]", "").replace(", ", "_") + "_";
		Logger.FILE_PREFIX += Arrays.toString(ticks).replace("[", "").replace("]", "").replace(", ", "_") + "_";

		for (String monitorNode : monitorNodes)  
			for (String metric : metrics) 
				for (String tick : ticks)
					for (String year : years)
						for (String maxattempt : maxattempts) 
							for (String timeout : timeouts) {
								Logger.log("Downloading CSV files for " + metric + " " + tick );
								PingtableCSVDownloader pingtableCSVDownloader = new PingtableCSVDownloader(downloadedCSVDirectory, monitorNode, metric, C.DEFAULT_PACKET_SIZE, tick, year, null, maxattempt, timeout);
								pingtableCSVDownloader.run();
							}


	}

}