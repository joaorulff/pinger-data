package edu.stanford.slac.pinger.main.pre;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.beans.CountryBean;
import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.utils.Utils;
import edu.stanford.slac.pinger.rest.HttpGetter;

public class CreateCountryCSV {

	public static void main (String args[]) {
		start(false);
	}
	/**
	 * @param file Set to true to work with the Json stored file.  Otherwise, it will generate a Json file from Geonames. Using "file" is recommended since the list of countries does not change often, unless you want to update the file.
	 */
	public static void start(boolean file) {
		JsonArray jArr = null; 
		if (file) {
			jArr = Utils.getJsonAsArray(C.COUNTRIES_JSON);
		} else {
			String url = "http://api.geonames.org/countryInfoJSON?username="+C.GEONAMES_USERNAME[0];
			jArr = HttpGetter.getJsonArrayGeonames(url);
			if (jArr != null){
				Utils.writeIntoFile(jArr.toString(), C.COUNTRIES_JSON);
			}
		}
		if (jArr==null) {
			Logger.error("Could not instantiate countries");
			return;
		} 

		StringBuilder sb = new StringBuilder();
		sb.append(CountryBean.CSV_HEADER);
		try {
			
			for (int i = 0; i < jArr.size(); i++) {
				JsonObject json = jArr.get(i).getAsJsonObject();

				String gnName = json.get("countryName").toString().replace("\"", "");
				if (gnName.length()==0) gnName = C.NULL_CHARACTER;
								
				String gnPopulation = json.get("population").toString().replace("\"", "");
				if (gnPopulation.length()==0) gnPopulation = C.NULL_CHARACTER;
				
				String languages = json.get("languages").toString().replace("\"", "");
				
				String lggs[] = languages.split(",");
				String firstLanguage = null;
				if (lggs.length > 1) {
					firstLanguage = lggs[0];
				} else 
					firstLanguage = languages;
				if (firstLanguage.length()==0) firstLanguage = C.NULL_CHARACTER;
				
				String areaInSqKm = json.get("areaInSqKm").toString().replace("\"", "");
				if (areaInSqKm.length()==0) areaInSqKm = C.NULL_CHARACTER;
				
				String continentCode = json.get("continent").toString().replace("\"", "");
				if (continentCode.length()==0) continentCode = C.NULL_CHARACTER;
				
				String countryCode = json.get("countryCode").toString().replace("\"", "");
				if (countryCode.length()==0) countryCode = C.NULL_CHARACTER;
				
				CountryBean cb = new CountryBean((i+1)+"", gnName, countryCode, gnPopulation, areaInSqKm, firstLanguage, continentCode);

				sb.append(cb.toString(','));
			}
			Utils.writeIntoFile(sb.toString(), C.COUNTRY_CSV);
			Logger.log("Successfully written into " + C.COUNTRY_CSV);
		}catch (Exception e) {
			Logger.error(CreateCountryCSV.class + " gnName ", e);
		} finally {
		}



	}

}


