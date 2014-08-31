package edu.stanford.slac.pinger.general;

import edu.stanford.slac.pinger.general.utils.Utils;



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
	
	public static final String DATA_TMP_DIR = TMP_DIR+"data_pid"+Utils.getCurrentPID()+"/";
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
	
	public static final String DEFAULT_PACKET_SIZE = "100";
	
	public static boolean continue_town = true;
	public static boolean continue_country = true;
	
	public static final String NULL_CHARACTER = "\\N";
	
	
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
	
	public static final String CSV_DIR = PROJECT_HOME+"data/csv/";
	public static final String PERL_DIR = PROJECT_HOME+"data/perl/";
	public static final String REMAINING_NTRIPLES =  PROJECT_HOME+"data/remaining_ntriples.txt";
	public static final String EXPORT_FILE = "C:/pinger-export/export.n3";
	
	
	public static final String CONTINENT_CSV = CSV_DIR + "continent.csv";
	public static final String COUNTRY_CSV = CSV_DIR + "country.csv";
	public static final String METRIC_CSV = CSV_DIR + "metric.csv";
	
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
	
	/**
	 * level > 0 prints in console.
	 * @param level
	 */
	public static void setDebugLevel(int level) {
		DEBUG_LEVEL = level;
	}

}




