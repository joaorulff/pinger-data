package edu.stanford.slac.pinger.instantiator.measurement.threads;

import com.google.gson.JsonArray;

import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.instantiator.measurement.threads.starters.MeasurementInstantiatorThreadsStarter;

public class MonitoringNodesThread extends Thread {

	private String tick;
	private JsonArray monitoringNodes;
	private String metric;
	private String packetSize;
	public MonitoringNodesThread(String metric, String tick, String packetSize, JsonArray monitoringNodes) {
		this.tick = tick; 
		this.monitoringNodes = monitoringNodes;
		this.metric = metric;
		this.packetSize = packetSize;
	}

	public void run() {
		long t1 = System.currentTimeMillis();
		try {
			MeasurementInstantiatorThreadsStarter.start(monitoringNodes, metric, packetSize, tick, this.getId());
		} catch (Exception e) {
			Logger.log(MonitoringNodesThread.class + " ----- " + monitoringNodes.toString(), e, "measurements/errors");
		}
		long t2 = System.currentTimeMillis();
		Logger.log("It took " + (t2 - t1)/1000.0/60.0 + " minutes to instantiate for metric: "+metric+ " " + tick + " for the monitoring nodes: " + this.monitoringNodes.toString(), "measurements/time");
	}
}
