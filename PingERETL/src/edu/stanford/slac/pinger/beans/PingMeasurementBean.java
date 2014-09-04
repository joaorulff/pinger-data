package edu.stanford.slac.pinger.beans;

public class PingMeasurementBean {

	private long id;
	private int sourceNodeId, destinationNodeId; 
	private int metricId;
	private long timeId;
	private String value;
	public PingMeasurementBean() {}
	public PingMeasurementBean(long id, int sourceNodeId, int destinationNodeId,
			int metricId, long timeId, String value) {
		this.id = id;
		this.sourceNodeId = sourceNodeId;
		this.destinationNodeId = destinationNodeId;
		this.metricId = metricId;
		this.timeId = timeId;
		this.value = value;
	}
	
	public int getSourceNodeId() {
		return sourceNodeId;
	}
	public void setSourceNodeId(int sourceNodeId) {
		this.sourceNodeId = sourceNodeId;
	}
	public int getDestinationNodeId() {
		return destinationNodeId;
	}
	public void setDestinationNodeId(int destinationNodeId) {
		this.destinationNodeId = destinationNodeId;
	}
	public int getMetricId() {
		return metricId;
	}
	public void setMetricId(int metricId) {
		this.metricId = metricId;
	}
	public long getTimeId() {
		return timeId;
	}
	public void setTimeId(long timeId) {
		this.timeId = timeId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return	//id + "," +
				sourceNodeId + "," +
				destinationNodeId + "," +
				metricId + "," +
				timeId + "," +
				value + "\n";
	}
	
}
