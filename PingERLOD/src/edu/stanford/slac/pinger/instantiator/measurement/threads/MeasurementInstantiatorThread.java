package edu.stanford.slac.pinger.instantiator.measurement.threads;

import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.bean.MetricBean;
import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.P;
import edu.stanford.slac.pinger.general.utils.MeasurementUtils;
import edu.stanford.slac.pinger.model.general.GeneralModelSingletonParallelized;

public class MeasurementInstantiatorThread extends Thread {

	private String monitoring, metric, packetSize, tickParameter;
	private JsonArray monitoredNodes;
	private HashMap<String,HashMap<String, String>> map;
	public MeasurementInstantiatorThread(String monitoring, HashMap<String,HashMap<String, String>> map, JsonArray monitoredNodes, String metric, String packetSize, String tickParameter){
		this.map = map;
		this.monitoredNodes = monitoredNodes;
		this.monitoring = monitoring;
		this.metric = metric;
		this.packetSize = packetSize;
		this.tickParameter = tickParameter;
	}

	public void run() {

		JsonObject monitoringNodeDetails = C.getNodeDetails().get(monitoring).getAsJsonObject();
		String fromSourceName =  monitoringNodeDetails.get("SourceName").getAsString();
		String fromNickName =  monitoringNodeDetails.get("SourceNickName").getAsString();

		GeneralModelSingletonParallelized gm = GeneralModelSingletonParallelized.getInstance();

		for (JsonElement monitoriedEl : monitoredNodes) {
			String monitored = monitoriedEl.getAsString();
			HashMap<String, String> timeVal = map.get(monitored);
			if (timeVal==null) continue; //Some nodes that are said to be monitored by the monitoring host do not have data on the map retrieved from Pintable TSV.

			JsonObject monitoredNodeDetails = null;
			try {
				monitoredNodeDetails = C.getNodeDetails().get(monitored).getAsJsonObject();
			} catch (Exception e) {
				Logger.log("Tried to get details about " + monitored, e, "errors");
				continue;
			}
			String toSourceName =  monitoredNodeDetails.get("SourceName").getAsString();
			String toNickName =  monitoredNodeDetails.get("SourceNickName").getAsString();
			//Begin Metric
			String metricURI = P.BASE+"MetricFROM-"+fromNickName+"-TO-"+toNickName;
			{
				gm.addTripleResource(metricURI, P.RDF, "type", P.MD, "Metric");
								
				String sourceNode =  P.BASE+"Node-"+fromSourceName;	
				gm.addTripleResource(metricURI, P.MD, "hasSourceNodeInformation", sourceNode);
				
				
				String destinationNode =  P.BASE+"Node-"+toSourceName;			
				gm.addTripleResource(metricURI, P.MD, "hasDestinationNodeInformation", destinationNode);
				
			}
			//End Metric
			instantiate(timeVal, metricURI, fromNickName, toNickName, packetSize, tickParameter);
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
	private void instantiate(HashMap<String, String> timeValue, String metricURI, String from, String to, String packetSize, String tickParameter) {
		GeneralModelSingletonParallelized gm = null;
		try {
			gm = GeneralModelSingletonParallelized.getInstance();
		} catch (Exception e) {
			Logger.error("Error when getting GeneralModelSingletonParallelized.",e);
			System.exit(-1);
		}
		String base = null;
		try {
			base = P.BASE;
		} catch (Exception e) {
			Logger.error("Error when getting base.",e);
			System.exit(-1);
		}
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
		String instantiationName = null;
		try {
			instantiationName = metricBean.getInstantiationName();
		} catch (Exception e) {
			Logger.error("Error when getting Metric Bean.",e);
			System.exit(-1);
		}		
		
		String simpleMeasurementURI = base + "SimpleMeasurement-"+instantiationName;

		for (String time : timeValue.keySet()) {
			String timeURI = P.BASE + "Time"+time;
			{
				String statisticalAnalysisURI = P.BASE + "M-"+from+"_"+to+"_"+metric+"_"+time;
				gm.addTripleResource(statisticalAnalysisURI, P.RDF, "type", P.MD, "StatisticalAnalysis");				
				gm.addTripleResource(statisticalAnalysisURI, P.MD, "measurementsAnalyzed", simpleMeasurementURI);				
				gm.addTripleResource(statisticalAnalysisURI, P.MD, "measuresMetric", metricURI);				
				gm.addTripleResource(statisticalAnalysisURI, P.MD, "timestamp", timeURI);
				
				//Linking PacketSize
				if (!packetSize.equals("100")) { //PacketSize 100 is the default size. Does not need to be included.
					String packetSizeURI = P.BASE + "PacketSize"+packetSize;
					gm.addTripleResource(statisticalAnalysisURI, P.MD, "hasMeasurementParameters", packetSizeURI);
				}
				gm.addTripleLiteral(statisticalAnalysisURI, P.MD, "StatisticalAnalysisValue", Float.parseFloat(timeValue.get(time)));
			}
		}		
	}


}
