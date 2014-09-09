package edu.stanford.slac.pinger.general.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.ErrorCode;
import edu.stanford.slac.pinger.general.Logger;

public class Utils {
	public static String readFile(String filePath) {
		FileInputStream fis = null; File f = null;
		try {
			f = new File(filePath);
			fis = new FileInputStream(f.getAbsolutePath());
			String everything = IOUtils.toString(fis);
			return everything;
		} catch (Exception e) {
			Logger.log("FILE NOT FOUND! -- Could not read the file " + f.getAbsolutePath(), e, "fileNotFound");
			return null;
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
				Logger.log("readFile " + filePath + " ", e, "errors");
				return null;
			}
		}
	}

	private static JsonElement getJsonElement(String jsonFilePath) {
		String content = readFile(jsonFilePath);
		JsonElement el = new JsonParser().parse(content);
		return el;
	}


	public static JsonObject getJsonAsObject(String jsonFilePath) {
		try {
			return (JsonObject) getJsonElement(jsonFilePath);
		} catch (Exception e) {
			Logger.log("getJsonAsObject", e, "errors");
			return null;
		}
	}

	public static JsonArray getJsonAsArray(String jsonFilePath) {
		try {
			return (JsonArray) getJsonElement(jsonFilePath);
		} catch (Exception e) {
			Logger.log("getJsonAsArray", e, "errors");
			return null;
		}
	}	

	/**
	 * This function is used to get the values of the properties you need from a resource.
	 * It is to be used with a sparql query of the format " select * where { :resource ?property ?value } " 
	 * @param json The json Result (It is expected a RDF Json format).
	 * @param properties A HashSet with the variables to be searched for.  
	 * @return The JsonObject with the variables and their values.
	 */
	public static JsonObject  getValues(JsonObject json, HashSet<String> properties) {
		JsonArray head = json.get("head").getAsJsonObject().get("vars").getAsJsonArray();
		JsonObject propertiesAndValues = new JsonObject();
		if (head.size()==2) {
			String prop = head.get(0).getAsString().toString().replace("\"", "");
			String value = head.get(1).getAsString().toString().replace("\"", "");			
			JsonArray jArr = json.get("results").getAsJsonObject().get("bindings").getAsJsonArray();
			if (jArr.size()>0) {
				for (int i = 0; i < jArr.size(); i++ ) {
					JsonObject j = jArr.get(i).getAsJsonObject();
					String p = j.get(prop).getAsJsonObject().get("value").toString().replace("\"", "");
					if (properties.contains(p)) {
						String v = j.get(value).getAsJsonObject().get("value").toString().replace("\"", "");
						JsonArray j1;
						if (propertiesAndValues.get(p) == null) { 
							j1 = new JsonArray(); 
							j1.add(new JsonPrimitive(v));
							propertiesAndValues.add(p, j1);
						}
						else {
							j1 = propertiesAndValues.get(p).getAsJsonArray();
							j1.add(new JsonPrimitive(v));
						}						
					}
				}
			}			
		} 
		return propertiesAndValues;
	}




	/**
	 * @param json The json Result (It is expected a Jena Api Json format).
	 * @param variable The head variable you want the result from.
	 * @return The value of a variable in a Jena Json Query Result
	 */
	public static String getValue(JsonObject json, String variable) {
		return getValue(json, variable, 0);
	}

	/**
	 * @param json The json Result (It is expected a Jena Api Json format).
	 * @param variable The head variable you want the result from.
	 * @param index The index in the result table.
	 * @return The value of a variable in a Jena Json Query Result
	 */
	public static String getValue(JsonObject json, String variable, int index) {
		try {
			JsonArray jArr = json.get("results").getAsJsonObject().get("bindings").getAsJsonArray();
			if (jArr.size() > 0) {
				JsonObject j = jArr.get(index).getAsJsonObject();
				return j.get(variable).getAsJsonObject().get("value").toString().replace("\"", ""); 
			} else
				return null;
		} catch (Exception e) {
			Logger.log("getValue", e, "errors");
			return null;
		}
	}

	public static void writeIntoFile(String content, String filePath) {
		try {
			File fout = new File(filePath);
			if (!fout.exists()) {
				fout.createNewFile();
			}
			PrintWriter out = new PrintWriter(filePath, "UTF-8");
			out.println(content);
			out.close();
			Logger.log("Written into file " + filePath);
		} catch (Exception e){
			Logger.log("C.writeIntoFile", e, "errors");
		}
	}

	public static void cleanDirectory(String dir) {
		try {
			File fileDir = new File(dir);
			if (!fileDir.isDirectory()) {
				Logger.log("There is no such directory to delete its content: " + dir, "errors");
				return;
			}
			File files[] = fileDir.listFiles();
			for (File f : files) {
				if (f.isDirectory())
					FileUtils.deleteDirectory(f);
				else if (!f.delete()) {
					Logger.log("Could not delete file " + f.getPath(), "errors");
				}				
			}
		} catch (Exception e) {
			Logger.log("cleanDirectory", e, "errors");
		}
	}



	public static boolean deleteDirAndContents(String dir) {
		try {
			FileUtils.deleteDirectory(new File(dir));
			return true;
		} catch (Exception e) {
			Logger.log("deleteDirAndContents", e, "errors");
			return false;
		}
	}

	private static String currentPID = null;
	public static String getCurrentPID() {
		if (currentPID==null) {
			String s = ManagementFactory.getRuntimeMXBean().getName();
			currentPID = extractNumbersFromString(s);
		}
		return currentPID;
	}

	public static String extractNumbersFromString(String s) {
		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(s); 
		while (m.find()) {
			sb.append(m.group());
		}
		return sb.toString();
	}

	public static void mkDir(String dirStr) {
		File dir = new File(dirStr);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	public static void mkTmpDirGrantingPermissions(String dirStr) {
		mkDir(dirStr);
		try {
			if (C.OS.equals("linux")) {
				String scratchDir = "/scratch/pinger/lod/";
				Runtime.getRuntime().exec("chmod -R 777 " + scratchDir);
			}
		} catch (Exception e) {
			Logger.log(e + " mkDirGrantingPermissions("+dirStr+")");
		}
	}

	public static void mkDirGrantingPermissions(String dirStr) {
		mkDir(dirStr);
		try {
			if (C.OS.equals("linux")) {
				Runtime.getRuntime().exec("chmod -R 777 " + dirStr);
			}
		} catch (Exception e) {
			Logger.log(e + " mkDirGrantingPermissions("+dirStr+")");
		}
	}


	public static File createFileGrantingPermissions(String filePath) {
		try {
			File file = new File(filePath);
			file.createNewFile();
			if (C.OS.equals("linux"))
				Runtime.getRuntime().exec("chmod 777 " + file.getAbsolutePath());
			return file;
		} catch (Exception e) {
			Logger.log(e + " createFileGrantingPermissions("+filePath+")");
			return null;
		}
	}

	public static String join(String arr[], String joiner) {
		String ret = "";
		for (String s : arr) {
			ret += s + joiner;
		}
		return ret;	
	}

	public static String removeLastCharacterFromString(String s) {
		if (s.length()==0) return s;
		else return s.substring(0, s.length()-1);
	}

	private static JsonObject NODE_DETAILS = null;
	public static JsonObject getNodeDetails() {
		if (NODE_DETAILS==null) {
			Logger.log("Generating the HashMap NodeDetails from JSON file...");
			long t1 = System.currentTimeMillis();
			NODE_DETAILS = Utils.getJsonAsObject(C.NODEDETAILS_JSON_FILE);
			long t2 = System.currentTimeMillis();
			Logger.log("...done! It took " + (t2-t1)/1000.0 + " seconds.");
		} 
		return NODE_DETAILS;
	}

	private static JsonObject PINGER_COUNTRIES = null;
	public static JsonObject getPingERCountries() {
		if (PINGER_COUNTRIES==null) {
			PINGER_COUNTRIES = Utils.getJsonAsObject(C.COUNTRIES_JSON);
		} 
		return PINGER_COUNTRIES;
	}

	private static JsonObject _MonitorMonitoredJSON = null;
	public static JsonObject getMonitorMonitoredJSON() {
		if (_MonitorMonitoredJSON==null) {
			Logger.log("Generating the MonitoringMonitoredGroupedJSON from JSON file...");
			long t1 = System.currentTimeMillis();
			_MonitorMonitoredJSON = Utils.getJsonAsObject(C.MONITORING_MONITORED_JSON_FILE);
			long t2 = System.currentTimeMillis();
			Logger.log("...done! It took " + (t2-t1)/1000.0 + " seconds.");
		} 
		return _MonitorMonitoredJSON;
	}
	public static void setMonitoringMonitoredGroupedJSON(JsonObject MonitoringMonitoredGroupedJSON) {
		_MonitorMonitoredJSON = MonitoringMonitoredGroupedJSON;
	}

	public static Calendar getInitialDate(){
		Calendar date = Calendar.getInstance();

		date.set(Calendar.DAY_OF_MONTH, 1);
		date.set(Calendar.MONTH, Calendar.JANUARY);
		date.set(Calendar.YEAR, 1998);

		return date;
	}

	public static Calendar getFinalDate(){
		Calendar date = Calendar.getInstance();

		int currentYear = date.get(Calendar.YEAR);

		date.set(Calendar.DAY_OF_MONTH, 1);
		date.set(Calendar.MONTH, Calendar.JANUARY);
		date.set(Calendar.YEAR, currentYear+1);

		return date;
	}

	public static ArrayList<String> getDaysOfAMonth(String yyyy, String mm) {
		try {
			String dd = "01";
			String defaultDateStr = mm + "/" + dd + "/" + yyyy;
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			Date defaultDate = sdf.parse(defaultDateStr);
			Calendar cal = new GregorianCalendar();
			cal.setTime(defaultDate);
			int maximumOfDaysInTheMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			ArrayList<String> daysOfMonth = new ArrayList<String>();
			for (int i = 1; i <= maximumOfDaysInTheMonth; i++) {
				daysOfMonth.add((i < 10)?"0"+i:""+i);
			}
			return daysOfMonth;
		} catch (Exception e) {
			Logger.error("" + mm + " or " + yyyy + " are not valid parameters.", ErrorCode.DATE_FORMAT);
			return null;
		}
	}
	
	public static String getCurrentMethodName() {
		try {
			return Thread.currentThread().getStackTrace()[2].getMethodName(); //shows name of the function which called this function
		} catch (Exception e) {
			return "unknownMethodName";
		}
	}

	public static String[] getMonths() {
		String months[] = {"01","02","03","04","05","06","07","08","09","10","11","12"};
		return months;
	}
	
	public static String[] getYears() {
		int firstYear = getInitialDate().get(Calendar.YEAR);
		int lastYear = getFinalDate().get(Calendar.YEAR);
		String years[] = new String[lastYear-firstYear+1];
		for (int i = firstYear; i <= lastYear; i++) {
			years[i-firstYear] = i+"";
		}
		return years;
	}
	

	private static String fileNameBeginning;
	public static String getFileNameBeginning(String tick, String metric, String year, String monitor) {
		if (fileNameBeginning!=null) {
			return fileNameBeginning;
		} else {
			if (tick != null && metric != null && year != null) {
				fileNameBeginning = year + "_" + metric + "_" +  monitor + "_" + tick;
			} else {
				fileNameBeginning = "n_";
			}
			return fileNameBeginning;
		}
	}
	
	private static String fileNameBeginningHourly;
	public static String getFileNameBeginningHourly(String tick, String metric, String year, String month) {
		if (fileNameBeginningHourly!=null) {
			return fileNameBeginningHourly;
		} else {
			if (tick != null && metric != null && year != null) {
				fileNameBeginningHourly = year + "_" + month + "_" + metric + "_" +  tick;
			} else {
				fileNameBeginningHourly = "n_";
			}
			return fileNameBeginningHourly;
		}
	}

	private static JsonObject countriesJson;
	public static JsonObject getCountriesJson() {
		if (countriesJson==null) {
			countriesJson = Utils.getJsonAsObject(C.COUNTRIES_JSON);
		}
		return countriesJson;
	}

}
