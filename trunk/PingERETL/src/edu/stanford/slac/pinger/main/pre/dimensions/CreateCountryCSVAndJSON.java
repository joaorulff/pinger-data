package edu.stanford.slac.pinger.main.pre.dimensions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.beans.CountryBean;
import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.utils.Utils;
import edu.stanford.slac.pinger.rest.HttpGetter;

public class CreateCountryCSVAndJSON {

	public static void main(String args[]) {
		start(getCountriesWithContinentsOnPingER(), false);
	}

	public static HashMap<String, String> getCountriesWithContinentsOnPingER() {
		HashMap<String, String> countriesOnPinger = new HashMap<String, String>();
		JsonObject nodeDetails = Utils.getNodeDetails();
		for (Entry<String, JsonElement> entry : nodeDetails.entrySet()) {
			JsonObject json = entry.getValue().getAsJsonObject();
			String country = json.get("country").getAsString();
			String continent = json.get("continent").getAsString();
			countriesOnPinger.put(country, continent);
		}
		return countriesOnPinger;
	}

	public static void start(
			HashMap<String, String> countriesWithContinentsOnPingER,
			boolean withStoredFile) {
		JsonArray jArr = null;
		if (withStoredFile) {
			jArr = Utils.getJsonAsArray(C.COUNTRIES_GEONAMES_JSON);
		} else {
			String url = "http://api.geonames.org/countryInfoJSON?username="
					+ C.GEONAMES_USERNAME[0];
			jArr = HttpGetter.getJsonArrayGeonames(url);
			if (jArr != null) {
				Utils.writeIntoFile(jArr.toString(), C.COUNTRIES_GEONAMES_JSON);
			}
		}
		if (jArr == null) {
			Logger.error("Could not instantiate countries");
			return;
		}

		JsonObject countriesJson = new JsonObject();

		ArrayList<CountryBean> pingERCountriesList = new ArrayList<CountryBean>();

		HashSet<String> matchedCountries = new HashSet<String>();
		try {
			// Getting most countries...
			for (int i = 0; i < jArr.size(); i++) {
				JsonObject json = jArr.get(i).getAsJsonObject();
				String geonamesCountryName = json.get("countryName").toString()
						.replace("\"", "");
				CountryBean cb = getCountryBean(json);

				if (countriesWithContinentsOnPingER
						.containsKey(geonamesCountryName)) {
					matchedCountries.add(geonamesCountryName);
					if (cb != null) {
						countriesJson.add(geonamesCountryName, json);
						pingERCountriesList.add(cb);
					}
				} else {
					// try to get countries that do not have matching names
					HashMap<String, String> mapCountriesOnPingERWithGeonames = getMapCountriesOnPingERWithGeonames();
					if (mapCountriesOnPingERWithGeonames
							.containsKey(geonamesCountryName)) {
						countriesJson.add(mapCountriesOnPingERWithGeonames
								.get(geonamesCountryName), json);
						pingERCountriesList.add(cb);
					}
				}
			}

			HashMap<String, String> pingERCountriesWithContinentsThatWereNotFoundOnGeonames = getPingERCountriesThatWereNotFoundOnGeonames(
					countriesWithContinentsOnPingER, matchedCountries);
			HashMap<String, String> mapNonCountriesWithContinents = getMapNonCountriesWithContinents();
			int iso = 10000;
			for (String country : pingERCountriesWithContinentsThatWereNotFoundOnGeonames
					.keySet()) {
				CountryBean cb = new CountryBean(iso++, country,
						C.NULL_CHARACTER, C.NULL_CHARACTER, C.NULL_CHARACTER,
						C.NULL_CHARACTER,
						pingERCountriesWithContinentsThatWereNotFoundOnGeonames
								.get(country));
				JsonObject countryJsonObj = new JsonObject();
				countryJsonObj.addProperty("isoNumeric", iso+"");
				countryJsonObj.addProperty("countryName", country);
				
				if (mapNonCountriesWithContinents.containsKey(country)) {
					String continent = mapNonCountriesWithContinents.get(country);
					countryJsonObj.addProperty("continent", continent);
					cb.setContinentCode(continent);
				}
				countriesJson.add(country, countryJsonObj);
				pingERCountriesList.add(cb);
			}

			Object[] countriesArr = pingERCountriesList.toArray();
			Arrays.sort(countriesArr);

			StringBuilder sb = new StringBuilder();
			sb.append(CountryBean.CSV_HEADER);
			for (Object countryObj : countriesArr) {
				CountryBean cb = (CountryBean) countryObj;
				sb.append(cb.toString(','));
			}
			Utils.writeIntoFile(sb.toString(), C.COUNTRY_CSV);

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(countriesJson);
			Utils.writeIntoFile(json, C.COUNTRIES_JSON);

			Logger.log("Successfully written into " + C.COUNTRY_CSV);
		} catch (Exception e) {
			Logger.error(CreateCountryCSVAndJSON.class + " gnName ", e);
		}
	}

	public static HashMap<String, String> getPingERCountriesThatWereNotFoundOnGeonames(
			HashMap<String, String> countriesWithContinentsOnPingER,
			HashSet<String> matchedCountries) {
		// Checking if getMapCountriesOnPingERWithGeonames is still valid. If
		// not, an error message will be thrown.
		HashMap<String, String> mapCountriesOnPingERWithGeonames = getMapCountriesOnPingERWithGeonames();
		HashMap<String, String> pingERCountriesThatWereNotFoundOnGeonames = new HashMap<String, String>();
		for (String countryOnPinger : countriesWithContinentsOnPingER.keySet()) {
			if (!matchedCountries.contains(countryOnPinger)) { 
				// pinger countries whose names could NOT be matched with
				// geonamescountries
				if (!mapCountriesOnPingERWithGeonames
						.containsValue(countryOnPinger)) {
					Logger.log("WARNING: Country " + countryOnPinger
							+ " could not be retrieved from Geonames! ");
					pingERCountriesThatWereNotFoundOnGeonames.put(
							countryOnPinger, countriesWithContinentsOnPingER
									.get(countryOnPinger));
				}
			}
		}
		
		return pingERCountriesThatWereNotFoundOnGeonames;
	}

	/**
	 * These are the corrected names. Keys are Geonames country names and values
	 * are PingER country names.
	 * 
	 * @return
	 */
	public static HashMap<String, String> getMapCountriesOnPingERWithGeonames() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("Bosnia and Herzegovina", "Bosnia Herzegovina");
		map.put("Gibraltar", "Gibralter");
		map.put("Réunion", "Reunion-French Colony");
		map.put("South Korea", "Korea, Rep");
		map.put("Slovakia", "Slovak Republic");
		map.put("Libya", "Libya Arab Jamahiriya");
		map.put("Democratic Republic of the Congo",
				"Democratic Republic of Congo");
		return map;
	}
	
	public static HashMap<String, String> getMapNonCountriesWithContinents() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("Serbia and Montenegro", "EU");
		map.put("Netherlands Antilles", "NA");
		return map;
	}

	private static CountryBean getCountryBean(JsonObject json) {

		int isoNumeric = 0;
		String isoNumericStr = json.get("isoNumeric").toString()
				.replace("\"", "");
		if (isoNumericStr.length() == 0) {
			Logger.error("One of the countries could not be retrieved.");
			return null;
		} else
			isoNumeric = Integer.parseInt(isoNumericStr);

		String gnName = json.get("countryName").toString().replace("\"", "");
		if (gnName.length() == 0)
			gnName = C.NULL_CHARACTER;

		String gnPopulation = json.get("population").toString()
				.replace("\"", "");
		if (gnPopulation.length() == 0)
			gnPopulation = C.NULL_CHARACTER;

		String languages = json.get("languages").toString().replace("\"", "");

		String lggs[] = languages.split(",");
		String firstLanguage = null;
		if (lggs.length > 1) {
			firstLanguage = lggs[0];
		} else
			firstLanguage = languages;
		if (firstLanguage.length() == 0)
			firstLanguage = C.NULL_CHARACTER;

		String areaInSqKm = json.get("areaInSqKm").toString().replace("\"", "");
		if (areaInSqKm.length() == 0)
			areaInSqKm = C.NULL_CHARACTER;

		String continentCode = json.get("continent").toString()
				.replace("\"", "");
		if (continentCode.length() == 0)
			continentCode = C.NULL_CHARACTER;

		String countryCode = json.get("countryCode").toString()
				.replace("\"", "");
		if (countryCode.length() == 0)
			countryCode = C.NULL_CHARACTER;

		CountryBean cb = new CountryBean(isoNumeric, gnName, countryCode,
				gnPopulation, areaInSqKm, firstLanguage, continentCode);

		return cb;
	}

}
