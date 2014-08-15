package edu.stanford.slac.pinger.rest.query.town;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.bean.TownBean;
import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.rest.pingtable.HttpGetter;

/**
 * @see <a href="http://www.geonames.org/export/web-services.html">http://www.geonames.org/export/web-services.html</a>
 * @see <a href="http://www.geonames.org/export/codes.html">http://www.geonames.org/export/codes.html</a>
 * @author Renan
 *
 */
public class TownModelGeonamesQuery {

	/**
	 * Get city with population greater than 1000.
	 * @param latitude of any place in the globe.
	 * @param longitude of any place in the globe.
	 * @return A TownBean representing a town near the latitude and longitude parameters. It is an attempt to set the following fields: <br>
	 *           <b>GeonamesId, Name, Latitude, Longitude, Population, setStateGeoId, StateName, CountryGeoId, County, CountryName, GmtOffset, GeonamesLink, WikiPediaLink, DbpediaId.</b>
	 */
	public static TownBean getCity1000(double latitude, double longitude) {
		String url = "http://api.geonames.org/findNearbyPlaceNameJSON?&style=FULL" +
				"&username="+C.GEONAMES_USERNAME[0] +
				"&lat="+latitude+"&lng="+longitude +
				"&cities=cities1000";
		return getCity(url);
	}

	/**
	 * Get city with population greater than 1000.
	 * @param latitude of any place in the globe.
	 * @param longitude of any place in the globe.
	 * @return A TownBean representing a town near the latitude and longitude parameters. It is an attempt to set the following fields: <br>
	 *           <b>GeonamesId, Name, Latitude, Longitude, Population, setStateGeoId, StateName, CountryGeoId, County, CountryName, GmtOffset, GeonamesLink, WikiPediaLink, DbpediaId.</b>
	 */
	public static TownBean getCity15000(double latitude, double longitude) {
		String url = "http://api.geonames.org/findNearbyPlaceNameJSON?&style=FULL" +
				"&username="+C.GEONAMES_USERNAME[0] +
				"&lat="+latitude+"&lng="+longitude +
				"&cities=cities15000";
		return getCity(url);
	}


	private static TownBean getCity(String url) {
		Logger.log(url, "towns");
		JsonObject json = HttpGetter.getJsonGeonames(url);
		if (json==null)return null;
		TownBean tb = new TownBean();

		try {
			String geoNamesId = json.get("geonameId").toString().replace("\"", "");
			tb.setGeonamesId( (geoNamesId.equals("")?null:geoNamesId ));
		} catch (Exception e) {
			Logger.log("Town geoNamesId undefined - ", e, "towns/errors");
			tb.setGeonamesId(null);
		}
		try {
			String name = json.get("name").toString().replace("\"", "");
			tb.setName( (name.equals("")?null:name ));
		} catch (Exception e) {
			Logger.log("Town name undefined - ", e,  "towns/errors");
			tb.setName(null);
		}
		try {
			tb.setLatitude(Double.parseDouble(json.get("lat").toString().replace("\"", "")));
		} catch (Exception e) {
			Logger.log(tb.getName() + " - lat undefined - ", e, "towns/errors");
			tb.setLatitude(0);
		}
		try {
			tb.setLongitude(Double.parseDouble(json.get("lng").toString().replace("\"", "")));
		} catch (Exception e) {
			Logger.log(tb.getName() + " - lng undefined - " , e,  "towns/errors");
			tb.setLongitude(0);
		}
		try {
			tb.setPopulation(Integer.parseInt(json.get("population").toString().replace("\"", "")));
		} catch (Exception e) { 
			Logger.log(tb.getName() + " - population undefined - ", e,  "towns");
			tb.setPopulation(0);
		}
		try {
			String stateGeoId = json.get("adminId1").toString().replace("\"", "");
			tb.setStateGeoId( (stateGeoId.equals("")?null:stateGeoId ));
		} catch (Exception e) { 
			Logger.log(tb.getName() + " - StateId undefined - " , e,  "towns");
			tb.setStateGeoId(null);
		}
		try {
			String stateName = json.get("adminName1").toString().replace("\"", "");
			tb.setStateName( (stateName.equals("")?null:stateName ));
		} catch (Exception e) {
			Logger.log(tb.getName() + " - State Name undefined - " , e,  "towns");
			tb.setStateName(null);
		}
		try {
			String countryId = json.get("countryId").toString().replace("\"", "");
			tb.setCountryGeoId( (countryId.equals("")?null:countryId ));
		} catch (Exception e) { 
			Logger.log(tb.getName() + " - countryId undefined - " , e,  "towns");
			tb.setCountryGeoId(null);
		}
		try {
			String county = json.get("adminName2").toString().replace("\"", "");
			tb.setCounty( (county.equals("")?null:county ));
		} catch (Exception e) { 
			Logger.log(tb.getName() + " - adminName2 undefined - " , e,  "towns");
			tb.setCounty(null);
		}
		try {
			String countryName = json.get("countryName").toString().replace("\"", "");
			tb.setCountryName( (countryName.equals("")?null:countryName ));
		} catch (Exception e) { 
			Logger.log(tb.getName() + " - countryName undefined - " , e,  "towns");
			tb.setCountryName(null);
		}
		try {
			tb.setGmtOffset(Float.parseFloat(json.get("timezone").getAsJsonObject().get("gmtOffset").toString().replace("\"", "")));
		} catch (Exception e) { 
			Logger.log(tb.getName() + " - timezone undefined - " , e,  "towns");
			tb.setGmtOffset(9999);
		}
		try {
			String geonamesLink = "http://sws.geonames.org/"+json.get("geonameId").toString().replace("\"", "")+"/";
			tb.setGeonamesLink(geonamesLink);
		} catch (Exception e) { 
			Logger.log(tb.getName() + " - geonameId undefined - " , e,  "towns");
			tb.setCounty(null);
		}
		try {
			Iterator<JsonElement> it = json.get("alternateNames").getAsJsonArray().iterator();
			tb.setWikiPediaLink(null);			
			ArrayList<String> lstPosts = new ArrayList<String>();
			while (it.hasNext()) {
				JsonObject js = (JsonObject) it.next().getAsJsonObject();
				String lang = js.get("lang").toString().replace("\"", "");
				if (lang.equals("link")) {
					String wikiLink = js.get("name").toString().replace("\"", "");
					if (!wikiLink.equals("")) {
						tb.setWikiPediaLink(wikiLink);
						tb.setDbpediaLink(wikiLink.replace("http://en.wikipedia.org/wiki/", "http://dbpedia.org/resource/"));
						tb.setDbPediaId(tb.getDbpediaLink().replace("http://dbpedia.org/resource/", ""));
					}
				} else
					if (lang.equals("post")) {
						String post = js.get("name").toString().replace("\"", "");
						if (!post.equals("")) {
							lstPosts.add(post);
						}
					}
			}
			tb.setPostalCodes(lstPosts);
		} catch (Exception e) {
			Logger.log(TownModelGeonamesQuery.class + " Iterator<JsonValue> it = json.get(alternateNames)", e, "towns");
			tb.setDbPediaId(null);
			tb.setDbpediaLink(null);
			tb.setPostalCodes(null);
			tb.setWikiPediaLink(null);
		}	

		return tb;
	}



}
