package edu.stanford.slac.pinger.main.pre;

import java.util.Iterator;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.P;
import edu.stanford.slac.pinger.general.utils.Utils;
import edu.stanford.slac.pinger.rest.HttpGetter;

public class CountryInstantiator {

	public static void main (String args[]) {
		start(false, false);
	}
	/**
	 * 
	 * @param file Set to true to work with the Json stored file.  Otherwise, it will generate a Json file from Geonames. Using "file" is recommended since the list of countries does not change often, unless you want to update the file.
	 * @param setdbpedia Set to true to access geonames rdfs to get Dbpedia
	 */
	public static void start(boolean file, boolean setdbpedia) {
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
		
		try {
			
			for (int i = 0; i < jArr.size(); i++) {
				JsonObject json = jArr.get(i).getAsJsonObject();

				if (!C.continue_country) break; //this is set in: HttpGetter.getJsonArrayGeonames

				String gnName = json.get("countryName").toString().replace("\"", "");
				String gnPopulation = json.get("population").toString().replace("\"", "");
				String geonamesId = json.get("geonameId").toString().replace("\"", "");
				String languages = json.get("languages").toString().replace("\"", "");
				String currencyCode = json.get("currencyCode").toString().replace("\"", "");
				String areaInSqKm = json.get("areaInSqKm").toString().replace("\"", "");
				String continentCode = json.get("continent").toString().replace("\"", "");
				String countryCode = json.get("countryCode").toString().replace("\"", "");
				
				
				//String capital = json.get("capitalName").toString().replace("\"", "");
				//ContinentBean cb = ContinentBean.MAP.get(continentCode);
				
				System.out.println(gnName);
				
			}
		}catch (Exception e) {
			Logger.error(CountryInstantiator.class + " gnName ", e);
		} finally {
		}



	}

}


