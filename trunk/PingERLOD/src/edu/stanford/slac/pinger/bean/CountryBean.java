package edu.stanford.slac.pinger.bean;

public class CountryBean {

	String gnName, gnPopulation, geonamesId, languages, areaInSqKm, continentCode, currencyCode;
	String dbpediaLink;
	ContinentBean cb;
	public CountryBean(String gnName, String gnPopulation, String geonamesId,
			String languages, String areaInSqKm, String continentCode, String currencyCode,
			String dbpediaLink, ContinentBean cb) {
		super();
		this.gnName = gnName;
		this.gnPopulation = gnPopulation;
		this.geonamesId = geonamesId;
		this.languages = languages;
		this.areaInSqKm = areaInSqKm;
		this.continentCode = continentCode;
		this.currencyCode = currencyCode;
		this.dbpediaLink = dbpediaLink;
		this.cb = cb;
	}
	@Override
	public String toString() {
		return "CountryBean [gnName=" + gnName + ", gnPopulation="
				+ gnPopulation + ", geonamesId=" + geonamesId + ", languages="
				+ languages + ", areaInSqmKm=" + areaInSqKm
				+ ", continentCode=" + continentCode + ", currencyCode="
				+ currencyCode + ", dbpediaLink=" + dbpediaLink + ", ContinentName=" + cb.getGnName()
				+ "]";
	}
}
