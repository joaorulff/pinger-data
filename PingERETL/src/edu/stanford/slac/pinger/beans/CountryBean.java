package edu.stanford.slac.pinger.beans;

public class CountryBean {

	public static final String CSV_HEADER = "#id,name,country_code,population,area_in_sqkm,first_language,continent_code\n";
	
	String id, gnName, countryCode, gnPopulation, geonamesId, languages, areaInSqKm, continentCode, currencyCode;
	String dbpediaLink;
	ContinentBean cb;
	public CountryBean(String id, String gnName, String isoAlpha3, String gnPopulation, String geonamesId,
			String languages, String areaInSqKm, String continentCode, String currencyCode,
			String dbpediaLink, ContinentBean cb) {
		super();
		this.id = id;
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

	public CountryBean(String id, String gnName, String countryCode, String gnPopulation, String areaInSqKm, String languages, String continentCode) {
		this.id = id;
		this.countryCode = countryCode;
		this.gnName = gnName;
		this.gnPopulation = gnPopulation;
		this.languages = languages;
		this.areaInSqKm = areaInSqKm;
		this.continentCode = continentCode;
	}

	public String toString(char dmtr) {
		return id + dmtr + gnName + dmtr + countryCode + dmtr + gnPopulation + dmtr +  areaInSqKm  + dmtr +  languages + dmtr + continentCode + "\n";
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
