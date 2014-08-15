package edu.stanford.slac.pinger.instantiator.measurement.threads.starters;

import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.utils.MeasurementUtils;
import edu.stanford.slac.pinger.instantiator.measurement.threads.MonitoringNodesThread;
import edu.stanford.slac.pinger.model.general.GeneralModelSingletonParallelized;

public class MonitoringNodesThreadsStarter {

	static JsonObject MonitoringGrouped = C.getJsonAsObject(C.MONITORING_NODES_GROUPED);
	public static void start(String[] ticks, String metric) {
		
		for (String tick : ticks) {
			for (String packetSize : MeasurementUtils.packetSizes) {
				long t1 = System.currentTimeMillis();
				Logger.log("Generating NTriples Files for: " + metric + " " + tick + " " + packetSize);
				GeneralModelSingletonParallelized gm = GeneralModelSingletonParallelized.getInstance();
				gm.setCurrentTick(tick);
				gm.setCurrentMetric(metric);
				gm.start();
				int nThreads = MonitoringGrouped.entrySet().size();
				Thread[] threads = new Thread[nThreads];
				int i = 0;
				for (Entry<String,JsonElement> entry : MonitoringGrouped.entrySet()) {
					JsonArray monitoringNodes = (JsonArray) entry.getValue();
					threads[i++] = new MonitoringNodesThread(metric, tick, packetSize, monitoringNodes);
				}
				for (i = 0; i < threads.length; i++) {
					
					threads[i].start();
					
				}
				for (i = 0; i < threads.length; i++) {
					try {
						threads[i].join();
					} catch (Exception e) { Logger.log("Generating NTriples Files for: " + metric + " " + tick + " " + packetSize, e, "errors"); }
				}
				long t2 = System.currentTimeMillis();
				Logger.log("Time to process the TSV files and to generate the NTriples files: " + (t2-t1)/1000.0 + " seconds.");
				Logger.log("Now, loading the Ntriples files into the Repository...");
				t1 = System.currentTimeMillis();
				
				gm.saveNTriplesIntoRepository();
				gm.close();
				
				t2 = System.currentTimeMillis();
				Logger.log("It took " + (t2 - t1)/1000.0/60.0 + " minutes to load into the repository: " + metric + " " + tick + " " + packetSize, "measurements/time");
			}
		}
	}
	
}
