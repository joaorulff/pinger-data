package edu.stanford.slac.pinger.bean;

public class StateBean {
	private String population, DBPediaLink, WikipediaLink, geoCountry;

	public String getPopulation() {
		return population;
	}

	public void setPopulation(String population) {
		this.population = population;
	}

	public String getDBPediaLink() {
		return DBPediaLink;
	}

	public void setDBPediaLink(String dBPediaLink) {
		DBPediaLink = dBPediaLink;
	}

	public String getWikipediaLink() {
		return WikipediaLink;
	}

	public void setWikipediaLink(String wikipediaLink) {
		WikipediaLink = wikipediaLink;
	}

	public String getGeoCountry() {
		return geoCountry;
	}

	public void setGeoCountry(String geoCountry) {
		this.geoCountry = geoCountry;
	}

	@Override
	public String toString() {
		return "StateBean [population=" + population + ", DBPediaLink="
				+ DBPediaLink + ", WikipediaLink=" + WikipediaLink
				+ ", geoCountry=" + geoCountry + "]";
	}
	
	
}
