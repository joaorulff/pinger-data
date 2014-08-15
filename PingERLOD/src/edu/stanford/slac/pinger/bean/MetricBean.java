package edu.stanford.slac.pinger.bean;

public class MetricBean {

	String key, defaultUnit, displayName, instantiationName;
	
	public MetricBean(String key, String defaultUnit, String displayName,
			String instantiationName) {
		this.key = key;
		this.defaultUnit = defaultUnit;
		this.displayName = displayName;
		this.instantiationName = instantiationName;
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
				+ instantiationName + "]";
	}
	
	
}
