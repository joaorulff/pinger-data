package edu.stanford.slac.pinger.etl.extractor.pingtabe;

import java.io.File;

import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.utils.Utils;
import edu.stanford.slac.pinger.rest.HttpGetter;

public class PingtableCSVDownloader {

	private String metric, tickParameter;
	private String monitorNode, packetSize;
	private String downloadedCSVDirectory;
	public PingtableCSVDownloader(String downloadedCSVDirectory, String monitorNode, String metric, String packetSize, String tickParameter) {
		this.downloadedCSVDirectory = downloadedCSVDirectory;
		this.tickParameter = tickParameter;
		this.metric = metric;
		this.monitorNode = monitorNode;
		this.packetSize = packetSize;
	}
	public void run() {
			JsonObject monitoringNodeDetails = null;
			try {
				monitoringNodeDetails = C.getNodeDetails().get(monitorNode).getAsJsonObject();
			} catch (Exception e) {
				Logger.log("Error with " + monitorNode, e, "errors");
				return;
			}
			String fromNickName =  monitoringNodeDetails.get("SourceNickName").getAsString();
			getPingtableAndWriteTSVFile(fromNickName);
	}
	private void getPingtableAndWriteTSVFile(String fromNickName) {
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
	private static String getPingTableTSV(String URL) {
		Logger.log(URL);
		String s = HttpGetter.readPage(URL);		
		if (s.contains("<h1>Sorry</h1>")) return null;
		return s;
	}
	
}
