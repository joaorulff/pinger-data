package edu.stanford.slac.pinger.beans;

public class PingMeasurementBean {

	private long id;
	private int sourceNodeId, sourceCountryId, sourceContinentId;
	private int destinationNodeId, destinationCountryId, destinationContinentId; 
	private int metricId;
	private long timeId;
	private String value;
	public PingMeasurementBean() {}
	
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
				sourceCountryId + "," +
				sourceContinentId + "," +
				destinationNodeId + "," +
				destinationCountryId + "," +
				destinationContinentId + "," +
				metricId + "," +
				timeId + "," +
				value + "\n";
	}
	public int getSourceCountryId() {
		return sourceCountryId;
	}
	public void setSourceCountryId(int sourceCountryId) {
		this.sourceCountryId = sourceCountryId;
	}
	public int getSourceContinentId() {
		return sourceContinentId;
	}
	public void setSourceContinentId(int sourceContinentId) {
		this.sourceContinentId = sourceContinentId;
	}
	public int getDestinationCountryId() {
		return destinationCountryId;
	}
	public void setDestinationCountryId(int destinationCountryId) {
		this.destinationCountryId = destinationCountryId;
	}
	public int getDestinationContinentId() {
		return destinationContinentId;
	}
	public void setDestinationContinentId(int destinationContinentId) {
		this.destinationContinentId = destinationContinentId;
	}
	
}
