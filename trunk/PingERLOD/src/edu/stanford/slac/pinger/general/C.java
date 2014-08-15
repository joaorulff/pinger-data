package edu.stanford.slac.pinger.general;

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



public final class C {

	public static String SESAME_SERVER;
	public static String PROJECT_HOME = null;
	static {	
		SESAME_SERVER = "http://wanmon.slac.stanford.edu:8181/openrdf-sesame";
		setInitConstantsBasedOnTheOS();		
		setProjectHome();
	}

	
	/* *******************************************************************
	 * ************* Initial Properties ************************************
	 ******************************************************************* */
	/*
	 * The following line should be uncommented when putting in production.
	 */
	//public static final String SESAME_SERVER = "http://wanmon.slac.stanford.edu:8181/openrdf-sesame";
	public static final String REPOSITORY_ID =  "pinger";

	
	public static String PERL_HOME;
	public static String TMP_DIR;
	public static String OS;
	
	public static final String DATA_TMP_DIR = TMP_DIR+"data_pid"+getCurrentPID()+"/";
	public static final int CUT_HEAP_COEF = 10;
	public static final int CUT_HD = 5;
	public static final String[] GEONAMES_USERNAME = {"pinger","renansouza","renan2","renan3","demo"};
	//http://www.geonames.org/manageaccount


	/* *******************************************************************
	 * ************* Other General Public Constants *******************
	 ******************************************************************* */ 	
	public static final String STANDARD_SPARQLQUERY = "SELECT * WHERE { ?a ?b ?c } LIMIT 10";
	public static final int MAX_ATTEMPT_INSTANTIATOR = 10;
	public static final int NUM_THREADS_MONITORING_NODES_TSV = 20;
	public static final int KB = (int) Math.pow(2, 10);
	public static final int MB = (int) Math.pow(2, 20);

	public static final int NUM_FILES_PER_TIME = 10;
	
	public static boolean continue_town = true;
	public static boolean continue_country = true;
	
	
	public static int DEBUG_LEVEL=-1;
	public static boolean IS_TO_LOAD_REMAINING=false;
	
	/* *******************************************************
	 * ***************** JSON Files **************************
	 ********************************************************* */ 
	public static final String NODEDETAILS_JSON_FILE = PROJECT_HOME+"data/json/NodeDetails.json";
	public static final String MONITORING_MONITORED_JSON_FILE = PROJECT_HOME+"data/json/MonitoringMonitoredNodes.json";
	public static final String MONITORING_MONITORED_GROUPED_JSON_FILE = PROJECT_HOME+"data/json/MonitoringMonitoredNodesGrouped.json";
	public static final String MONITORING_NODES_GROUPED = PROJECT_HOME+"data/json/MonitoringNodesGrouped.json"; //used for parallelizing the process of creating a NTriple file. It could create more than 1 file at time, 1 for each monitoring node.
	public static final String MONITORING_NODES_GROUPED_FOR_TSV = PROJECT_HOME+"data/json/MonitoringNodesGroupedForTSV.json";
	public static final String COUNTRIES_JSON = PROJECT_HOME+"data/json/countries.json";
	public static final String MONITORING_MONITORED_COUNTRIES = PROJECT_HOME+"data/json/monitoring_monitored_countries.json";
	
	public static final String MONITORING_MONITORED_JSON_FILE_WWW = "/afs/slac.stanford.edu/g/www/www-iepm/pinger/lod/data/json/MonitoringMonitoredNodes.js";
	public static final String MONITORING_MONITORED_COUNTRIES_WWW = "/afs/slac.stanford.edu/g/www/www-iepm/pinger/lod/data/json/monitoring_monitored_countries.js";

	/* *******************************************************
	 * ***************** RDF Files **************************
	 ********************************************************* */ 
	public static final String PREFIXES_FILE = PROJECT_HOME+"data/rdf/prefixes.rdf";

	/* *******************************************************
	 * ***************** NTRIPLES Files **************************
	 ********************************************************* */ 
	public static final String NTRIPLES_DIR = DATA_TMP_DIR+"ntriples/";

	
	/* *******************************************************
	 * ***************** Other Paths **************************
	 ********************************************************* */ 
	
	public static final String TSV_DIR = PROJECT_HOME+"data/tsv/";
	public static final String PERL_DIR = PROJECT_HOME+"data/perl/";
	public static final String REMAINING_NTRIPLES =  PROJECT_HOME+"data/remaining_ntriples.txt";
	public static final String EXPORT_FILE = "C:/pinger-export/export.n3";
	/* *****************************************************************
	 * ***************** Web Sparql Endpoints **************************
	 ******************************************************************* */ 	
	public static final String DBPEDIA_ENDPOINT = "http://dbpedia.org/sparql/";
	public static final String FACTFORGE_ENDPOINT = "http://factforge.net/sparql";
	public static final String FACTFORGE_ENDPOINT_JSON = FACTFORGE_ENDPOINT+".json";

	/* *******************************************************************
	 * ************* General Functions ***********************************
	 ******************************************************************* */ 
	private static void setInitConstantsBasedOnTheOS() {
		String OSname = System.getProperty("os.name").toLowerCase();
		boolean windows = OSname.contains("windows");
		boolean linux = ( OSname.contains("linux") || OSname.contains("unix") );
		if (windows) {
			OS = "windows";
			TMP_DIR = "C:/tmp/pinger/";
			PERL_HOME = "C:/strawberry/perl/bin/";
		} else if (linux) {
			OS = "linux";
			TMP_DIR = "/scratch/pinger/lod/";
			PERL_HOME = "/usr/bin/";
		} else {
			Logger.error("Operating System not supported: " + OSname, "errors");
			System.exit(-1);
		}
	}	
	
	private static JsonObject _MonitoringMonitoredGroupedJSON = null;
	public static JsonObject getMonitoringMonitoredGroupedJSON() {
		if (_MonitoringMonitoredGroupedJSON==null) {
			Logger.log("Generating the MonitoringMonitoredGroupedJSON from JSON file...");
			long t1 = System.currentTimeMillis();
			_MonitoringMonitoredGroupedJSON = getJsonAsObject(MONITORING_MONITORED_GROUPED_JSON_FILE);
			long t2 = System.currentTimeMillis();
			Logger.log("...done! It took " + (t2-t1)/1000.0 + " seconds.");
		} 
		return _MonitoringMonitoredGroupedJSON;
	}
	public static void setMonitoringMonitoredGroupedJSON(JsonObject MonitoringMonitoredGroupedJSON) {
		_MonitoringMonitoredGroupedJSON = MonitoringMonitoredGroupedJSON;
	}

	private static JsonObject NODE_DETAILS = null;
	public static JsonObject getNodeDetails() {
		if (NODE_DETAILS==null) {
			Logger.log("Generating the HashMap NodeDetails from JSON file...");
			long t1 = System.currentTimeMillis();
			NODE_DETAILS = getJsonAsObject(NODEDETAILS_JSON_FILE);
			long t2 = System.currentTimeMillis();
			Logger.log("...done! It took " + (t2-t1)/1000.0 + " seconds.");
		} 
		return NODE_DETAILS;
	}

	private static void setProjectHome() {
		ClassLoader classLoader = C.class.getClassLoader();
        String path = classLoader.getResource("").getPath().replace("/bin/", "/");
        
        if (OS.equals("windows")) {
        	path = path.replace("\\", "/");
			if (path.matches("^/.*$")) {
				path = path.replaceFirst("/", "");
			}
        }        
		PROJECT_HOME = path;
	}
	
	public static String readFile(String filePath) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
			String everything = IOUtils.toString(fis);
			return everything;
		} catch (Exception e) {
			Logger.log("FILE NOT FOUND! -- Could not read the file " + filePath, e, "fileNotFound");
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
			if (OS.equals("linux")) {
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
			if (OS.equals("linux")) {
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
			if (OS.equals("linux"))
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
	
	/**
	 * level > 0 prints in console.
	 * @param level
	 */
	public static void setDebugLevel(int level) {
		DEBUG_LEVEL = level;
	}

}




