package edu.stanford.slac.pinger.etl.extractor.pingtabe;

import java.io.File;

import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.general.ErrorCode;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.utils.Utils;
import edu.stanford.slac.pinger.rest.HttpGetter;

/**
 * We can improve this class (programming patterns only) by making a PingtableCSVDownloader which instantiates PingtableCSVDownloaderDaily or PingtableCSVDownloaderHourly
 * @author Renan
 *
 */
public class PingtableCSVDownloader {

	private String metric, tickParameter, year, month, maxattempt, timeout;
	private String monitorNode, packetSize;
	private String downloadedCSVDirectory;
	public PingtableCSVDownloader(String downloadedCSVDirectory, String monitorNode, String metric, String packetSize, String tickParameter, String year, String month, String maxattempt, String timeout) {
		this.year = year;
		this.month = month;
		this.maxattempt = maxattempt;
		this.timeout = timeout;
		this.downloadedCSVDirectory = downloadedCSVDirectory;
		this.tickParameter = tickParameter;
		this.metric = metric;
		this.monitorNode = monitorNode;
		this.packetSize = packetSize;
	}
	public void run() {
		if (month==null) {
			JsonObject monitoringNodeDetails = null;
			try {
				monitoringNodeDetails = Utils.getNodeDetails().get(monitorNode).getAsJsonObject();
			} catch (Exception e) {
				Logger.log("Error with " + monitorNode, e, "errors");
				return;
			}
			String fromNickName =  monitoringNodeDetails.get("NodeNickName").getAsString();
			getPingtableAndWriteTSVFileDaily(fromNickName);
		}
		else
			getPingtableAndWriteTSVFileHourly(null);
	}

	private void getPingtableAndWriteTSVFileDaily(String fromNickName) {
		for (String month : Utils.getMonths()) {
			String url =     	
					"http://www-wanmon.slac.stanford.edu/cgi-wrap/pingtable.pl?format=tsv&"+
							"file="+metric+"&"+
							"by=by-node&" +
							"size="+packetSize+"&"+
							"from="+fromNickName+"&"+
							"to=WORLD&" +
							"ex=none&only=all&dataset=hep&percentage=any&dnode=on&"+
							"tick=hourly&"+
							"year="+year+"&"+
							"month="+month;


			String htmlContent = getPingTableTSV(url);
			if (htmlContent == null) {
				//Logger.log("Warning! Could not get TSV file for the URL: " + url, "errors");
				continue;
			}
			File downloadedDir = new File(downloadedCSVDirectory);
			if (!downloadedDir.exists()) {
				downloadedDir.mkdirs();
			}
			downloadedCSVDirectory = downloadedDir.getAbsolutePath() + File.separator;
			String dirPath = downloadedCSVDirectory;
			File dir = new File(dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String filePath = dirPath + year + "_" + metric + "_" + monitorNode + "_" + tickParameter + "_" + month +".csv";
			Utils.createFileGrantingPermissions(filePath);
			Utils.writeIntoFile(htmlContent, filePath);
		}
	}

	private void getPingtableAndWriteTSVFileHourly(String fromNickName) {
		for (String day : Utils.getDaysOfAMonth(year, month)) {
			String url =     	
					"http://www-wanmon.slac.stanford.edu/cgi-wrap/pingtable.pl?format=tsv&"+
							"file="+metric+"&"+
							"by=by-node&" +
							"size="+packetSize+"&"+
							"from=WORLD&"+
							"to=WORLD&" +
							"ex=none&only=all&dataset=hep&percentage=any&dnode=on&"+
							"tick="+tickParameter+"&"+
							"year="+year+"&"+
							"month="+month+"&"+
							"day="+day;


			String htmlContent = getPingTableTSV(url);
			if (htmlContent == null) {
				//Logger.log("Warning! Could not get TSV file for the URL: " + url, "errors");
				continue;
			}
			File downloadedDir = new File(downloadedCSVDirectory);
			if (!downloadedDir.exists()) {
				downloadedDir.mkdirs();
			}
			downloadedCSVDirectory = downloadedDir.getAbsolutePath() + File.separator;
			String dirPath = downloadedCSVDirectory;
			File dir = new File(dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String filePath = dirPath + year + "_" + month +  "_" + metric + "_" + tickParameter + "_" + day +".csv";
			Utils.createFileGrantingPermissions(filePath);
			Utils.writeIntoFile(htmlContent, filePath);
		}
	}

	
	@Deprecated
	private void getPingtableAndWriteTSVFileOriginal(String fromNickName) {
		String url =     	
				"http://www-wanmon.slac.stanford.edu/cgi-wrap/pingtable.pl?format=tsv&"+
						"file="+metric+"&"+
						"by=by-node&" +
						"size="+packetSize+"&"+
						"from="+fromNickName+"&"+
						"to=WORLD&" +
						"ex=none&only=all&dataset=hep&percentage=any&dnode=on&"+
						"tick="+tickParameter;
		String htmlContent = getPingTableTSV(url);
		if (htmlContent == null) {
			Logger.log("Warning: Could not get TSV file for the URL: " + url, "errors");
			return;
		}
		File downloadedDir = new File(downloadedCSVDirectory);
		if (!downloadedDir.exists()) {
			downloadedDir.mkdirs();
		}
		downloadedCSVDirectory = downloadedDir.getAbsolutePath() + File.separator;

		String dirPath = downloadedCSVDirectory;
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String filePath = dirPath + metric + "_" + tickParameter + "_" + packetSize + "_" + monitorNode+".csv";
		Utils.createFileGrantingPermissions(filePath);
		Utils.writeIntoFile(htmlContent, filePath);
	}
	private String getPingTableTSV(String URL) {
		Logger.log(URL);
		HttpGetter httpGetter = new HttpGetter(Integer.parseInt(maxattempt), Integer.parseInt(timeout));
		String s = httpGetter.readPageConstrained(URL);
		if (s != null && s.contains(">Sorry<")){
			Logger.error("Sorry message appeared in the URL: " + URL, ErrorCode.SORRY_MESSAGE);
			return null;
		} else if (s==null) return null;
		else return s;
	}



}
