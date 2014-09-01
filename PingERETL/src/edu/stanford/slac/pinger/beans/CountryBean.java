package edu.stanford.slac.pinger.beans;


public class CountryBean implements Comparable<CountryBean> {

	public static final String CSV_HEADER = "#iso_numeric,name,country_code,population,area_in_sqkm,first_language,continent_code\n";
	int isoNumeric;
	String gnName, countryCode, gnPopulation, geonamesId, languages, areaInSqKm, continentCode, currencyCode;
	String dbpediaLink;
	ContinentBean cb;
	
	public CountryBean(int isoNumeric, String gnName, String isoAlpha3, String gnPopulation, String geonamesId,
			String languages, String areaInSqKm, String continentCode, String currencyCode,
			String dbpediaLink, ContinentBean cb) {
		super();
		this.isoNumeric = isoNumeric;
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

	public CountryBean(int isoNumeric, String gnName, String countryCode, String gnPopulation, String areaInSqKm, String languages, String continentCode) {
		this.countryCode = countryCode;
		this.gnName = gnName;
		this.gnPopulation = gnPopulation;
		this.languages = languages;
		this.areaInSqKm = areaInSqKm;
		this.continentCode = continentCode;
		this.isoNumeric = isoNumeric;
	}

	public String toString(char dmtr) {
		return (isoNumeric+"") + dmtr + gnName + dmtr + countryCode + dmtr + gnPopulation + dmtr +  areaInSqKm  + dmtr +  languages + dmtr + continentCode + "\n";
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

	@Override
	public int compareTo(CountryBean other) {
		if (this.isoNumeric > other.isoNumeric)
			return 1;
		else if (this.isoNumeric <  other.isoNumeric)
			return -1;
		else //(thisId == otherId)
			return 0;
	}

	public String getContinentCode() {
		return continentCode;
	}

	public void setContinentCode(String continentCode) {
		this.continentCode = continentCode;
	}
}
