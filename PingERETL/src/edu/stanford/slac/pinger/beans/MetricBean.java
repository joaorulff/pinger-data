package edu.stanford.slac.pinger.beans;

public class MetricBean {

	String key, defaultUnit, displayName, instantiationName;
	int id;
	
	public MetricBean(String key, String defaultUnit, String displayName,
			String instantiationName, int id) {
		this.key = key;
		this.defaultUnit = defaultUnit;
		this.displayName = displayName;
		this.instantiationName = instantiationName;
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDefaultUnit() {
		return defaultUnit;
	}

	public void setDefaultUnit(String defaultUnit) {
		this.defaultUnit = defaultUnit;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getInstantiationName() {
		return instantiationName;
	}

	public void setInstantiationName(String instantiationName) {
		this.instantiationName = instantiationName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	@Override
	public String toString() {
		return "MetricBean [key=" + key + ", defaultUnit=" + defaultUnit
				+ ", displayName=" + displayName + ", instantiationName="
				+ instantiationName + ", id=" + id + "]";
	}

}
