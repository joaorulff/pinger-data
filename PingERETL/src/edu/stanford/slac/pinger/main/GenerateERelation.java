package edu.stanford.slac.pinger.main;

import java.io.File;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.utils.Utils;
import edu.stanford.slac.pinger.main.commons.MainCommons;

public class GenerateERelation {

	public static void main(String[] args) {
		if (args.length == 0) {
			args = new String[]{
				"debug=0",
				//C:\Users\Renan\Dropbox\PendriveOnline\_Mestrado\WfC\WfETL\exp\csvDownloader\0\.\downloadedCSV\allyearly_pinger.slac.stanford.edu_100_throughput.csv
				//"activityTag=csvDownloader,extractorFile=c:/extractor/file.txt,fields=METRIC;TICK;MONITOR;CSV_FILE,inputDataset=throughput;allyearly;pinger.slac.stanford.edu,downloadedCSVDirectory=./downloadedCSV"
				"activityTag=transform,extractorFile=./ERelation.txt,fields=YEAR;MONTH;METRIC;TICK;MONITOR;TRANSFORMED_FILE,inputDataset=2014;minimum_rtt;daily;pinger.nwfpuet.edu.pk,transformedFilesDirectory=/C:/Users/Renan/Dropbox/PendriveOnline/_Mestrado/WfC/WfETL/exp/_shared/downloadedCSV"
			};
		}	
		start(args);
	}

	public static void start(String[] args) {
		try {
			for (String arg : args) {
				if (arg.contains("debug")) {
					MainCommons.debug(arg);
				} else if (arg.contains("activityTag")) {
					generateER(arg);
				}
			}
		} catch (Exception e) {
			Logger.log("start", e, "errors");
		}
	}

	public static void generateER(String arg) {
		String ags[] = arg.split(",");
		String activityTag = null, extractorFile = null, content = null;
		for (String ag : ags) {
			if (ag.contains("activityTag")) {
				activityTag = ag.replace("activityTag=", "");
				if (activityTag.contains("csvDownloader"))
					content = generateERForCsvDownloader(ags);
				else if (activityTag.contains("transformer"))
					content = generateERForTransformer(ags);
				else if (activityTag.contains("generateInput"))
					content = generateERForGenerateInput(ags);
			} else if (ag.contains("extractorFile")) {
				extractorFile = ag.replace("extractorFile=", "");
			}
		}

		Utils.createFileGrantingPermissions(extractorFile);
		Utils.writeIntoFile(content, extractorFile);

	}

	public static String generateERForCsvDownloader(String[] ags) {
		String fields = null, downloadedCSVDirectory = null, inputDataset = null;
		for (String ag : ags) {
			if (ag.contains("fields")) {
				fields = ag.replace("fields=", "");
			} else if (ag.contains("downloadedCSVDirectory")) {
				downloadedCSVDirectory = ag.replace("downloadedCSVDirectory=",
						"");
			} else if (ag.contains("inputDataset")) {
				inputDataset = ag.replace("inputDataset=", "");
			}
		}

		String separatedInputDataset[] = inputDataset.split(";");

		String dirPath = downloadedCSVDirectory;
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String year = separatedInputDataset[0];
		String metric = separatedInputDataset[1];
		String tick = separatedInputDataset[2];
		String monitor = separatedInputDataset[3];
		String filePath = dir.getPath() + File.separator + Utils.getFileNameBeginning(tick, metric, year, monitor) + "_MonthNumber.csv";

		StringBuilder sb = new StringBuilder();
		sb.append(fields + "\n");
		sb.append(inputDataset + ";" + filePath);

		return sb.toString();

	}

	public static String generateERForTransformer(String[] ags) {
		String fields = null, transformedFilesDirectory = null, inputDataset = null;
		for (String ag : ags) {
			if (ag.contains("fields")) {
				fields = ag.replace("fields=", "");
			} else if (ag.contains("transformedFilesDirectory")) {
				transformedFilesDirectory = ag.replace(
						"transformedFilesDirectory=", "");
			} else if (ag.contains("inputDataset")) {
				inputDataset = ag.replace("inputDataset=", "");
			}
		}

		String separatedInputDataset[] = inputDataset.split(";");

		String dirPath = transformedFilesDirectory;
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String metric = separatedInputDataset[0];
		String tickParameter = separatedInputDataset[1];

		String filePath = dir.getPath() + File.separator + metric + "_"
				+ tickParameter + "_" + 1 + ".csv";

		StringBuilder sb = new StringBuilder();
		sb.append(fields + "\n");
		sb.append(inputDataset + ";" + filePath);
		return sb.toString();

	}

	public static String generateERForGenerateInput(String[] ags) {
		String inputDatasetFile = null;
		for (String ag : ags) {
			if (ag.contains("inputDatasetFile")) {
				inputDatasetFile = ag.replace("inputDatasetFile=", "");
			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append("INPUTDATASET_FILE\n");
		sb.append(inputDatasetFile);
		return sb.toString();

	}

}
