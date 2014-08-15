package edu.stanford.slac.pinger.rest.pingtable.tsvdownloader;

import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.utils.MeasurementUtils;
import edu.stanford.slac.pinger.rest.pingtable.GetPingTableTSVThread;

public class GetPingTableTSVThreadsStarter {

	public static void start(String metric, String tickParameter){
		JsonObject json = C.getJsonAsObject(C.MONITORING_NODES_GROUPED_FOR_TSV);
		
		String dirPath = C.TSV_DIR+metric+"/"+tickParameter;
		C.cleanDirectory(dirPath);
		
		for (String packetSize : MeasurementUtils.packetSizes) {

			int nThreads = json.entrySet().size();
			Thread[] threads = new Thread[nThreads];
			int i = 0;
			for (Entry<String,JsonElement> entry : json.entrySet()) {
				JsonArray monitoringNodes = (JsonArray) entry.getValue();
				threads[i++] = new GetPingTableTSVThread(monitoringNodes, metric, packetSize, tickParameter);
			}
			for (i = 0; i < threads.length; i++) {
				threads[i].start();
			}
			for (i = 0; i < threads.length; i++) {
				try {
					threads[i].join();
				} catch (Exception e) { Logger.error("GetPingTableTSVThreadsStarter", e); }
			}			 

		}

	}

}
