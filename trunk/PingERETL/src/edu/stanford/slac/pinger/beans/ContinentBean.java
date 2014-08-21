package edu.stanford.slac.pinger.beans;

import java.util.HashMap;

public class ContinentBean {


	private String gnName, continentCode, geoNamesId, DBPediaLink, geonamesLink;


	public ContinentBean(String gnName, String continentCode, String geoNamesId, String dBPediaLink, String geonamesLink) {
		this.gnName = gnName;
		this.geoNamesId = geoNamesId;
		this.DBPediaLink = dBPediaLink;
		this.geonamesLink = geonamesLink;
		this.continentCode = continentCode;
	}
	public ContinentBean() {}

	public String getGnName() {
		return gnName;
	}
	public void setGnName(String gnName) {
		this.gnName = gnName;
	}
	public String getContinentCode() {
		return continentCode;
	}
	public void setContinentCode(String continentCode) {
		this.continentCode = continentCode;
	}
	public String getGeoNamesId() {
		return geoNamesId;
	}
	public void setGeoNamesId(String geoNamesId) {
		this.geoNamesId = geoNamesId;
	}
	public String getDBPediaLink() {
		return DBPediaLink;
	}
	public void setDBPediaLink(String dBPediaLink) {
		DBPediaLink = dBPediaLink;
	}
	public String getGeonamesLink() {
		return geonamesLink;
	}
	public void setGeonamesLink(String geonamesLink) {
		this.geonamesLink = geonamesLink;
	}

	public static HashMap<String,ContinentBean> MAP;
	static {
		MAP = new HashMap<String, ContinentBean>();
		ContinentBean africa = new ContinentBean(
				"Africa",
				"AF",
				"6255146",
				"http://dbpedia.org/resource/"+"Africa",
				"http://sws.geonames.org/"+"6255146"+"/"
				);
		ContinentBean asia = new ContinentBean(
				"Asia",
				"AS",
				"6255147",
				"http://dbpedia.org/resource/"+"Asia",
				"http://sws.geonames.org/"+"6255147"+"/"
				);
		ContinentBean europe = new ContinentBean(
				"Europe",
				"EU",
				"6255148",
				"http://dbpedia.org/resource/"+"Europe",
				"http://sws.geonames.org/"+"6255148"+"/"
				);
		ContinentBean north_america = new ContinentBean(
				"North America",
				"NA",
				"6255149",
				"http://dbpedia.org/resource/"+"North_America",
				"http://sws.geonames.org/"+"6255149"+"/"
				);
		ContinentBean south_america = new ContinentBean(
				"South America",
				"SA",
				"6255150",
				"http://dbpedia.org/resource/"+"South_America",
				"http://sws.geonames.org/"+"6255150"+"/"
				);
		ContinentBean oceania = new ContinentBean(
				"Oceania",
				"OC",
				"6255151",
				"http://dbpedia.org/resource/"+"Oceania",
				"http://sws.geonames.org/"+"6255151"+"/"
				);
		ContinentBean antartica = new ContinentBean(
				"Antartica",
				"AN",
				"6255152",
				"http://dbpedia.org/resource/"+"Antartica",
				"http://sws.geonames.org/"+"6255152"+"/"
				);
		MAP.put("AF", africa);
		MAP.put("AS", asia);
		MAP.put("EU", europe);
		MAP.put("NA", north_america);
		MAP.put("SA", south_america);
		MAP.put("OC", oceania);
		MAP.put("AN", antartica);
	}


}
