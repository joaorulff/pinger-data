package edu.stanford.slac.pinger;

import java.net.InetAddress;
import java.util.Arrays;

import edu.stanford.slac.pinger.etl.extractor.pingtabe.PingtableCSVDownloader;
import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.utils.Utils;

public class Start {

	@Deprecated
	public static void main(String[] args) {
		
		if (args.length == 0) {
			args = new String[]{
					
				/**
				 *debug=0 -> it prints in stdout most of the log messages;
				 *debug=-1 -> only critical errors 
				 */
				"debug=0",
				
				/**
				 * generate=1 -> generate the RDF file to be used to set up the prefixes.
				 */
				//"setupprefixes=0,generate=0",
				
				//"continents=0",
				
				/**
				 * file=0 -> Set to true to work with the Json stored file.  Otherwise, it will generate a Json file from Geonames. Using "file=1" is recommended since the list of countries does not change often, unless you want to update or generate the file.
	  			 * setdbpedia=1 -> Set to true to access geonames rdfs to get Dbpedia
				 */
				//"countries=0,file=0,setdbpedia=1",
								
				/**
				 * generateNodeDetails=1 -> generates the NodeDetailsJson from the Perl object %NODE_DETAILS 
				 * downloadNodesCF=1 -> download the nodes.cf, which has the %NODE_DETAILS object for the Perl script, from the web.
				 * all=1 -> generates all-nodes.cf, which has information about all nodes, including the not used and inactive ones. //this is discouraged to be used.
				 */
				//"generateNodeDetails=1,downloadNodesCF=1,all=0",
				
				//"towns=1",
				//"schools=1",
				//"nodes=	1",

				/**
				 * generateGrouped=1 -> generates a json to be used in the thread division for monitoring nodes. Each thread will take care of a group of monitoring nodes. Right now it is done with 80 groups of 1 monitoring node each. In other words, each of the 80 threads will take care of a single monitoring node.
				 * monitoringMonitoredGrouped=1 -> generates a json to be used in the thread division for instantiating measurements. Each monitoring node launches a thread  for each group of monitored nodes. Then, each monitored node job instantiates measurements related to its monitoring node.
				 * generateMonitoringGroupedForTSV=1 -> generates a json to be used in the thread division for downloading the tsv files. The list of monitoring node is divided by N groups. Each of the N thread executes HTTP GETs to download the tsv files for the group of monitoring node.
				 * generateMonitoringMonitored=1 -> generates a json mapping each monitoring host to its monitored hosts.
				 */
				//"generateMonitoring=1,generateGrouped=1,monitoringMonitoredGrouped=1,generateMonitoringGroupedForTSV=1,generateMonitoringMonitored=1,monitoringMonitoredCountries=1",
				
				/**
				 * timestamp=1 -> This will instantiate all possible timestamps.
				 */
				//"measurementParameters=1,timestamp=1",
								
				/**
				 * downloadTSVFiles=1 -> This will download the tsv files for the metrics and ticks indicated
				 * metrics=[throughput-packet_loss] -> This is a dash-separated list (enclosed by square brackets) indicating the metrics that will be considered
				 * ticks=[allyearly-allmonthly-last365days] -> This is a dash-separated list (enclosed by square brackets) indicating the ticks that will be considered
				 */
				//"loadRemainingFiles=0",
				
				"loadTSVFilesIntoRepository=1,downloadTSVFiles=1,monitorNodes=[pinger.slac.stanford.edu],metrics=[throughput],ticks=[allyearly]",
				//"test=1"
			};
		}		
	
		start(args);
	}


	public static void start(String[] args) {	
		try {
			Logger.log("Local host name: " + InetAddress.getLocalHost().getHostName());
		
			long t1 = System.currentTimeMillis();
			for (String arg : args) {
				if (arg.contains("debug")) {
					debug(arg);
				} else if (arg.contains("loadRemaining=1")) {
					loadRemaining(arg);
				} else if (arg.contains("setupprefixes=1")) {
					//setupprefixes(arg);
				} else if (arg.contains("towns=1")) {
					//towns(arg);
				} else if (arg.contains("schools=1")) {
					//schools(arg);
				} else if (arg.contains("nodes=1")) {
					//nodes(arg);
				} else if (arg.contains("continents=1")) {
					//continents(arg);
				} else if (arg.contains("countries=1")) {
					//countries(arg);
				} else if (arg.contains("generateMonitoring=1")) {
					//generateMonitoring(arg);
				} else if (arg.contains("generateNodeDetails=1")) {
					//generateNodeDetails(arg);
				} else if (arg.contains("measurementParameters=1")) {
					//measurementParameters(arg);		
				} else if (arg.contains("loadTSVFilesIntoRepository=1")) {
					loadTSVFilesIntoRepository(arg);
				} else if(arg.contains("test=1")) {
					//T.main(args);
				}
			}
			long t2 = System.currentTimeMillis();
			Logger.log("Finally done! It took " + ((t2-t1)/1000.0/60.0) + " minutes.");
		} catch (Exception e) {
			Logger.log("start", e, "errors");
		}
	}

	public static void nodes(String arg) {
		Logger.FILE_PREFIX += "nodes_";
		Logger.log("Instantiating Nodes...");
		//NodesInstantiator.start();
	}
	
	public static void generateMonitoring(String arg) {
		Logger.FILE_PREFIX += "jsons_aux_";
		Logger.log("Generating JSON Monitoring Hosts...");
		boolean grouped = arg.contains("generateGrouped=1");
		boolean monitoringMonitored = arg.contains("generateMonitoringMonitored=1");
		boolean monitoringMonitoredGrouped = arg.contains("monitoringMonitoredGrouped=1");
		boolean generateMonitoringGroupedForTSV = arg.contains("generateMonitoringGroupedForTSV=1");
		boolean monitoringMonitoredCountries = arg.contains("monitoringMonitoredCountries=1");
		//GenerateMonitoringJSON.start(monitoringMonitored, grouped, monitoringMonitoredGrouped,generateMonitoringGroupedForTSV);
		//if (monitoringMonitoredCountries)SparqlQueries.buildMonitoringMonitored();
	}
	
	public static void generateNodeDetails(String arg) {
		boolean generateNodesFile=false, all=false;
		String ags[] = arg.split(",");
		for (String ag : ags) {
			if (ag.contains("downloadNodesCF"))
				generateNodesFile = ag.contains("1");
			else if (ag.contains("all"))
				all = ag.contains("1");
		}
		Logger.FILE_PREFIX += "node_details_";
		Logger.log("Generating NodeDetails JSON. 'Downloading Nodes.cf from web'="+generateNodesFile + ", 'all nodes'="+all);
		//GenerateNodeDetailsJSON.start(generateNodesFile, all);
	}
	
	public static void measurementParameters(String arg) {
		startingProcedures();
		Logger.FILE_PREFIX += "meas_params_";
		Logger.log("Instantiating Measurement Parameters...");
		boolean timestamp = arg.contains("timestamp=1");
		//MeasurementParametersInstantiator.start(timestamp);
		closingProcedures();
	}
	
	public static void loadTSVFilesIntoRepository(String arg) {
		startingProcedures();
		
		boolean download = arg.contains("downloadTSVFiles=1");
		String ags[] = arg.split(",");
		String metrics[] = null;
		String ticks[] = null;
		String monitorNodes[] = null;
		for (String ag : ags) {
			if (ag.contains("metrics")) {
				String s = ag.replace("metrics=[", "");
				s = s.replace("]", "");
				metrics = s.split("-");
			} else if (ag.contains("ticks")) {
				String s = ag.replace("ticks=[", "");
				s = s.replace("]", "");
				ticks = s.split("-");
			} else if (ag.contains("monitorNodes")) {
				String s = ag.replace("monitorNodes=[", "");
				s = s.replace("]", "");
				monitorNodes = s.split("-");
			} 
		}
				
		
		Logger.FILE_PREFIX += Arrays.toString(metrics).replace("[", "").replace("]", "").replace(", ", "_") + "_";
		Logger.FILE_PREFIX += Arrays.toString(ticks).replace("[", "").replace("]", "").replace(", ", "_") + "_";
		
		if (download) {
			for (String monitorNode : monitorNodes)  
				for (String metric : metrics) 
					for (String tick : ticks) {
						Logger.log("Downloading TSV files for " + metric + " " + tick );
						//PingtableCSVDownloader pingtableCSVgetter = new PingtableCSVDownloader(monitorNode, metric, C.DEFAULT_PACKET_SIZE, tick);
						//pingtableCSVgetter.run();
					}
		}

		/*
		for (String metric : metrics) {
			Logger.log("Generating NTriples files and Loading the Repository for " + metric + " " + Arrays.toString(ticks) );
			MonitoringNodesThreadsStarter.start(ticks, metric);
		}
		*/
		closingProcedures();
	
	}
	public static void debug(String arg) {
		if (arg.contains("debug=0")) {
			C.DEBUG_LEVEL=0;
		} else if (arg.contains("debug=-1")) {
			C.DEBUG_LEVEL=-1;
		}
	}
	
	public static void loadRemaining(String arg) {
		if (arg.contains("debug=1")) {
			C.IS_TO_LOAD_REMAINING = true;
		}
	}
	
	public static void startingProcedures() {
		Utils.mkTmpDirGrantingPermissions(C.NTRIPLES_DIR);
	}
	public static void closingProcedures() {
		Logger.log("Deleting tmp directory...");
		Utils.deleteDirAndContents(C.DATA_TMP_DIR);
		Logger.log("Deleted!");
	}
	
}
