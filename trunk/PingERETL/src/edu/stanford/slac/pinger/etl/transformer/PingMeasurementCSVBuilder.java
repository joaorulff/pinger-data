package edu.stanford.slac.pinger.etl.transformer;

import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.beans.MetricBean;
import edu.stanford.slac.pinger.beans.PingMeasurementBean;
import edu.stanford.slac.pinger.etl.loader.local.FileHandler;
import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.utils.MeasurementUtils;
import edu.stanford.slac.pinger.general.utils.Utils;

public class PingMeasurementCSVBuilder {

	private FileHandler fileHandler; 
	private String monitoring, metric, packetSize, tickParameter, year, month;
	private JsonArray monitoredNodes;
	private HashMap<String,HashMap<String, String>> map;
	
	private JsonObject days = Utils.getJsonAsObject(C.DAYS_JSON);
	
	public PingMeasurementCSVBuilder(FileHandler fileHandler, String monitoring, HashMap<String,HashMap<String, String>> map, JsonArray monitoredNodes, String metric, String packetSize, String tickParameter, String year, String month){
		this.fileHandler = fileHandler;
		this.map = map;
		this.monitoredNodes = monitoredNodes;
		this.monitoring = monitoring;
		this.metric = metric;
		this.packetSize = packetSize;
		this.tickParameter = tickParameter;
		this.year = year;
	}

	//TODO: It will not work.
	static long sequence = 1;

	public void run() {

		JsonObject sourceNodeDetails = Utils.getNodeDetails().get(monitoring).getAsJsonObject();
		int sourceId =  0;
		try {
			sourceId = Integer.parseInt(sourceNodeDetails.get("NodeID").getAsString());
		}catch (Exception e) {
			Logger.log("Tried to get details about " + monitoring, e, "errors");
			return;
		}
		
		for (JsonElement monitoredEl : monitoredNodes) {
			String monitored = monitoredEl.getAsString();
			HashMap<String, String> timeVal = map.get(monitored);
			if (timeVal==null) continue; //Some nodes that are said to be monitored by the monitoring host do not have data on the map retrieved from Pintable TSV.

			JsonObject monitoredNodeDetails = null;
			int destinationId = 0;
			try {
				monitoredNodeDetails = Utils.getNodeDetails().get(monitored).getAsJsonObject();
				destinationId = Integer.parseInt(monitoredNodeDetails.get("NodeID").getAsString());
			} catch (Exception e) {
				Logger.log("Tried to get details about " + monitored, e, "errors");
				continue;
			}
			instantiate(fileHandler, timeVal, sourceId, destinationId, packetSize, tickParameter);
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
	private void instantiate(FileHandler fileHandler, HashMap<String, String> timeValue, int sourceId, int destinationId, String packetSize, String tickParameter) {
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
			//pmb.setId(sequence++);
			pmb.setMetricId(metricBean.getId());
		
			pmb.setSourceNodeId(sourceId);
			pmb.setDestinationNodeId(destinationId);
			
			long timeFromDaysJson = 0;
			try {
				timeFromDaysJson = Long.parseLong(days.get(time).getAsString());
			} catch (Exception e) {
				Logger.error(time + " is not a valid time.");
				continue;
			}
			
			pmb.setTimeId(timeFromDaysJson);
			
			pmb.setValue(timeValue.get(time));
			
			fileHandler.addRow(pmb.toString());
		}		
	}


}
