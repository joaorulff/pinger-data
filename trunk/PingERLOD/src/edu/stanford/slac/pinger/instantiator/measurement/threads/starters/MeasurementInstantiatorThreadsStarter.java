package edu.stanford.slac.pinger.instantiator.measurement.threads.starters;

import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.instantiator.measurement.threads.MeasurementInstantiatorThread;
import edu.stanford.slac.pinger.instantiator.measurement.tsv.TSVProcessorFromFile;

public class MeasurementInstantiatorThreadsStarter {


	public static void start(JsonArray monitoringNodes, String metric, String packetSize, String tickParameter, long tid) {

		for (JsonElement monitoringEl : monitoringNodes) {
			try {
				String monitoring = monitoringEl.getAsString();
				JsonObject monitoringNodeDetails = null;
				try {
					monitoringNodeDetails = C.getNodeDetails().get(monitoring).getAsJsonObject();
				} catch (Exception e) {
					Logger.error(monitoringEl + " was not found in NodeDetails JSON.", e, "node_details");
					continue;
				}
				String fromNickName =  monitoringNodeDetails.get("SourceNickName").getAsString();
				long t1 = System.currentTimeMillis();
				TSVProcessorFromFile tsv = new TSVProcessorFromFile(fromNickName, metric, packetSize, tickParameter);
				HashMap<String,HashMap<String, String>> map = tsv.getMap();					
				long t2 = System.currentTimeMillis();
				Logger.log("It took " + (t2-t1)/1000.0 + " to get the tsv file and process its content from "+monitoring+".");
				if (map == null) {
					continue;
				}
				JsonObject monitoringObj = C.getMonitoringMonitoredGroupedJSON().get(monitoring).getAsJsonObject();
				int nThreads = monitoringObj.entrySet().size(); //# of monitored nodes by the monitoring node
				Thread[] threads = new Thread[nThreads];
				int i = 0;
				for (Entry<String,JsonElement> entry : monitoringObj.entrySet()) {
					JsonArray monitoredNodes = (JsonArray) entry.getValue();
					threads[i++] = new MeasurementInstantiatorThread(monitoring, map, monitoredNodes, metric, packetSize, tickParameter);
				}
				for (i = 0; i < threads.length; i++) {
					threads[i].start();
				}
				for (i = 0; i < threads.length; i++) {
					try {
						threads[i].join();
					} catch (Exception e) { Logger.log("start " + monitoringEl, e, "errors" ); }
				}

			} catch (Exception e) {
				Logger.error(monitoringEl, e);
			}
		}
	}
}
