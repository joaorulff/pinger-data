package edu.stanford.slac.pinger.etl.transformer;

import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.beans.MetricBean;
import edu.stanford.slac.pinger.beans.PingMeasurementBean;
import edu.stanford.slac.pinger.etl.loader.local.FileHandler;
import edu.stanford.slac.pinger.etl.pre_extractor.time.GenerateTimeMap;
import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.utils.MeasurementUtils;
import edu.stanford.slac.pinger.general.utils.Utils;

public class PingMeasurementCSVBuilder {

	private FileHandler fileHandler; 
	private String monitoring, metric, packetSize, tickParameter;
	private JsonArray monitoredNodes;
	private HashMap<String,HashMap<String, String>> map;
	public PingMeasurementCSVBuilder(FileHandler fileHandler, String monitoring, HashMap<String,HashMap<String, String>> map, JsonArray monitoredNodes, String metric, String packetSize, String tickParameter){
		this.fileHandler = fileHandler;
		this.map = map;
		this.monitoredNodes = monitoredNodes;
		this.monitoring = monitoring;
		this.metric = metric;
		this.packetSize = packetSize;
		this.tickParameter = tickParameter;
	}

	//TODO: It will not work.
	static long sequence = 1;

	//TODO: This will not work either... maybe utilization of Lucene to index...
	static HashMap<String, Long> timeMap = GenerateTimeMap.getMap();
	
	public void run() {

		JsonObject monitoringNodeDetails = Utils.getNodeDetails().get(monitoring).getAsJsonObject();
		String fromSourceName =  monitoringNodeDetails.get("SourceName").getAsString();
		String fromNickName =  monitoringNodeDetails.get("SourceNickName").getAsString();

		for (JsonElement monitoredEl : monitoredNodes) {
			String monitored = monitoredEl.getAsString();
			HashMap<String, String> timeVal = map.get(monitored);
			if (timeVal==null) continue; //Some nodes that are said to be monitored by the monitoring host do not have data on the map retrieved from Pintable TSV.

			JsonObject monitoredNodeDetails = null;
			try {
				monitoredNodeDetails = Utils.getNodeDetails().get(monitored).getAsJsonObject();
			} catch (Exception e) {
				Logger.log("Tried to get details about " + monitored, e, "errors");
				continue;
			}
			String toSourceName =  monitoredNodeDetails.get("SourceName").getAsString();
			String toNickName =  monitoredNodeDetails.get("SourceNickName").getAsString();
			//Begin Metric
			
			
			//End Metric
			instantiate(fileHandler, timeVal, fromNickName, toNickName, packetSize, tickParameter);
		}
	}

	/**
	 * It was verified that this is the function that takes the longest. For each monitored host, this function currently takes approximately 9 seconds to run (last365days). 
	 * This function should be especially parallelized.
	 * @param timeValue
	 * @param metric
	 * @param metricURI
	 * @param packetSize
	 * @param tickParameter
	 */
	private void instantiate(FileHandler fileHandler, HashMap<String, String> timeValue, String from, String to, String packetSize, String tickParameter) {
		HashMap<String,MetricBean> mapMetricBean = null;
		try {
			mapMetricBean = MeasurementUtils.mapMetricBean;
		} catch (Exception e) {
			Logger.error("Error when getting map.",e);
			System.exit(-1);
		}
		MetricBean metricBean = null;
		try {
			metricBean = mapMetricBean.get(metric);
		} catch (Exception e) {
			Logger.error("Error when getting Metric Bean.",e);
			System.exit(-1);
		}
		
		for (String time : timeValue.keySet()) {
			
			PingMeasurementBean pmb = new PingMeasurementBean();
			pmb.setId(sequence++);
			pmb.setMetricId(metricBean.getId());
			pmb.setSourceNodeId(2);
			pmb.setDestinationNodeId(3);
			pmb.setTimeId(timeMap.get(time));
			pmb.setValue(timeValue.get(time));
			
			fileHandler.addRow(pmb.toString());
		}		
	}


}
