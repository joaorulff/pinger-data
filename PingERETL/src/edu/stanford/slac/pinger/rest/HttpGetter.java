package edu.stanford.slac.pinger.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.ErrorCode;
import edu.stanford.slac.pinger.general.Logger;

public class HttpGetter {

	private static final int MAX_ATTEMPT = 50;
	private static final int TIMEOUT = 6000; //ms

	private int maxAttempt, timeOut;
	public HttpGetter(int maxAttempt, int timeOut) {
		this.maxAttempt = maxAttempt;
		this.timeOut = timeOut;
	}
	public String readPageConstrained(String URL) {
		int attempt = 0;
		while (attempt++ < maxAttempt) {
			BufferedReader in = null;
			try {
				URL url = new URL(URL);
				URLConnection con = url.openConnection();
				con.setConnectTimeout(timeOut);
				in = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
				StringBuffer sb = new StringBuffer();
				int read;
				char[] cbuf = new char[1024];
				while ((read = in.read(cbuf)) != -1)
					sb.append(cbuf, 0, read);
				String s = sb.toString();
				return s;
			}
			catch (Exception e){				
				Logger.log("Attempt " + attempt + " to access URL: "+ URL + " timed out.", e);
				continue;
			}finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						Logger.log("readPage", e);
					}
				}
			}
		}
		Logger.error("Maximum attempts exceeded to access the URL ", ErrorCode.UNREACHED);
		return null;
	}
	
	public static String readPage(String URL) {
		int attempt = 0;
		while (attempt++ < MAX_ATTEMPT) {
			BufferedReader in = null;
			try {
				URL url = new URL(URL);
				URLConnection con = url.openConnection();
				con.setConnectTimeout(TIMEOUT);
				in = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
				StringBuffer sb = new StringBuffer();
				int read;
				char[] cbuf = new char[1024];
				while ((read = in.read(cbuf)) != -1)
					sb.append(cbuf, 0, read);
				String s = sb.toString();
				return s;
			}
			catch (Exception e){				
				Logger.log("Attempt " + attempt + " to access URL: "+ URL + " timed out.", e);
				continue;
			}finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						Logger.log("readPage", e);
					}
				}
			}
		}
		Logger.error("Maximum attempts exceeded to access the URL " + URL, ErrorCode.UNREACHED);
		return null;
	}

	public static JsonObject readPageJson(String URL) {
		JsonObject json = null;
		try {
			json = (JsonObject) new JsonParser().parse(readPage(URL));
		} catch (Exception e) {
			Logger.log("Could not retrieve a valid JSON from the url: " + URL, "errors");
		}
		return json;
	}

	public static JsonObject getJsonGeonames(String URL) {
		JsonArray jArr = getJsonArrayGeonames(URL);
		if (jArr!=null) return jArr.get(0).getAsJsonObject();
		else {
			return null;
		}
	}
	
	public static JsonArray getJsonArrayGeonames(String URL) {
		try {
			JsonObject json = readPageJson(URL);
			int maxAttempt = C.GEONAMES_USERNAME.length, i=1;
			while (json.get("geonames") == null && i < maxAttempt) {
				URL = URL.replace(C.GEONAMES_USERNAME[i-1], C.GEONAMES_USERNAME[i++]);
				json = readPageJson(URL);
			}			
			if (json.get("geonames") != null) {
				return json.get("geonames").getAsJsonArray();	
			} else {
				Logger.log("All Geonames usernames seem to be blocked. You need to wait or add more valid usernames.", "errors");
				C.continue_town = false;
				C.continue_country = false;
				return null;
			}
		} catch (Exception e) {
			Logger.log(HttpGetter.class, e, "errors");
			return null;
		}
	}

}