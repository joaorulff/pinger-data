package edu.stanford.slac.pinger.bean;

import java.util.ArrayList;

/**
 * 
 * @author Renan
 * @see http://www.geonames.org/export/codes.html
 */
public class TownBean {

	private double pingerLat;
	private double pingerLong; //data used to retrieve the information about the town
	private double latitude, longitude;
	private String geonamesId;
	private String geonamesLink;
	private String name;
	private String county; //adminName2
	private float gmtOffset; 
	private int population;
	private String wikiPediaLink; 
	
	private ArrayList<String> postalCodes;
	
	private String dbpediaLink, dbPediaId, freebaseLink;

	private String stateName; //adminName1	
	private String stateGeoId; //adminId1
	private String stateDBPediaId; 


	private String countryName;
	private String countryGeoId; 
	private String countryDBPediaID;

	private String continentName;
	private String continentGeoId;
	private String continentDBPediaId;
	
	private TownBean nearestCity;
	
	public TownBean(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public TownBean(String geonamesId) {
		this.geonamesId = geonamesId;
	}
	
	public TownBean() {}

	public String getGeonamesId() {
		return geonamesId;
	}
	
	public void setGeonamesId(String geonamesId) {
		this.geonamesId = geonamesId;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getWikiPediaLink() {
		return wikiPediaLink;
	}
	public void setWikiPediaLink(String wikiPediaLink) {
		this.wikiPediaLink = wikiPediaLink;
	}
	public float getGmtOffset() {
		return gmtOffset;
	}
	public void setGmtOffset(float gmtOffset) {
		this.gmtOffset = gmtOffset;
	}
	public int getPopulation() {
		return population;
	}
	public void setPopulation(int population) {
		this.population = population;
	}

	public String getStateGeoId() {
		return stateGeoId;
	}

	public void setStateGeoId(String stateGeoId) {
		this.stateGeoId = stateGeoId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getStateDBPediaId() {
		return stateDBPediaId;
	}

	public void setStateDBPediaId(String stateDBPediaId) {
		this.stateDBPediaId = stateDBPediaId;
	}

	public String getCountryGeoId() {
		return countryGeoId;
	}

	public void setCountryGeoId(String countryGeoId) {
		this.countryGeoId = countryGeoId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCountryDBPediaID() {
		return countryDBPediaID;
	}

	public void setCountryDBPediaID(String countryDBPediaID) {
		this.countryDBPediaID = countryDBPediaID;
	}


	public String getContinentGeoId() {
		return continentGeoId;
	}

	public void setContinentGeoId(String continentGeoId) {
		this.continentGeoId = continentGeoId;
	}

	public String getContinentName() {
		return continentName;
	}

	public void setContinentName(String continentName) {
		this.continentName = continentName;
	}

	public String getContinentDBPediaId() {
		return continentDBPediaId;
	}

	public void setContinentDBPediaId(String continentDBPediaId) {
		this.continentDBPediaId = continentDBPediaId;
	}
	
	public String getDbpediaLink() {
		return dbpediaLink;
	}
	public void setDbpediaLink(String dbpediaLink) {
		this.dbpediaLink = dbpediaLink;
	}
	public String getFreebaseLink() {
		return freebaseLink;
	}
	public void setFreebaseLink(String freebaseLink) {
		this.freebaseLink = freebaseLink;
	}
	public TownBean getNearestCity() {
		return nearestCity;
	}
	public void setNearestCity(TownBean nearestCity) {
		this.nearestCity = nearestCity;
	}
	public String getGeonamesLink() {
		return geonamesLink;
	}
	public void setGeonamesLink(String geonamesLink) {
		this.geonamesLink = geonamesLink;
	}
	public ArrayList<String> getPostalCodes() {
		return postalCodes;
	}
	public void setPostalCodes(ArrayList<String> postalCode) {
		this.postalCodes = postalCode;
	}
	public String getDbPediaId() {
		return dbPediaId;
	}
	public void setDbPediaId(String dbPediaId) {
		this.dbPediaId = dbPediaId;
	}
	public double getPingerLat() {
		return pingerLat;
	}
	public void setPingerLat(double pingerLat) {
		this.pingerLat = pingerLat;
	}
	public double getPingerLong() {
		return pingerLong;
	}
	public void setPingerLong(double pingerLong) {
		this.pingerLong = pingerLong;
	}
	@Override
	public String toString() {
		String nearestCity = (this.nearestCity!=null)?this.nearestCity.getName():"null" ;
		return "TownBean [latitude=" + latitude + ", longitude=" + longitude
				+ ", geonamesId=" + geonamesId + ", name=" + name + ", "
				+ " Nearest City=" + nearestCity + ", "
				+ "county=" + county + ", gmtOffset=" + gmtOffset + ", population="  
				+ population + ", wikiPediaLink=" + wikiPediaLink + ", "
				+ "Geonameslink=" + geonamesLink
				+ ", dbpediaLink=" + dbpediaLink + ", dbpediaId="+dbPediaId+", freebaseLink=" + freebaseLink
				+ ", stateName=" + stateName + ", stateGeoId=" + stateGeoId
				+ ", stateDBPediaId=" + stateDBPediaId + ", countryName="
				+ countryName + ", countryGeoId=" + countryGeoId
				+ ", countryDBPediaID=" + countryDBPediaID + ", continentName="
				+ continentName + ", continentGeoId=" + continentGeoId
				+ ", postalCodes="+postalCodes
				+ ", continentDBPediaId=" + continentDBPediaId + "]";
	}


}
