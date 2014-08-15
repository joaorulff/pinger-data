package edu.stanford.slac.pinger.general.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
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
			PrintWriter out = new PrintWriter(filePath);
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
}
