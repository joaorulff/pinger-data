package edu.stanford.slac.pinger.rest.pingtable;

import java.util.HashMap;

import edu.stanford.slac.pinger.general.Logger;

/**
 * This class is to GET the content of Pingtable.
 * The content is in the HTML source code of each measurement in a comment HTML tag.
 * This class supposes that if the HTML content has a message of "Sorry" means that a measurement is not available in those parameters.
 * @author Renan
 *
 */
@Deprecated
public class GetPingTableHostToHost {


	/**
	 *  This HashMap maps each metric to the pair (time, value). For example,
	 *  avg -->  (2003, 0.000) -  (1998, 0.000) -  (2012, 221.782) -  (2011, 227.693) -  (2010, 238.845)
	 *  statisticalMetric -> (time, value)
	 * @param from
	 */
	public static HashMap<String, HashMap<String,String>> getMeasurementMap(String from, String to, String metric, String pktSize, String tickParams) {
		String url =     	
				"http://www-wanmon.slac.stanford.edu/cgi-wrap/pingtable.pl?"+
						"file="+metric+"&"+
						"by=by-node&" +
						"size="+pktSize+"&"+
						"from="+from+"&"+
						"to="+to+"&" +
						"ex=none&only=all&dataset=hep&percentage=any&dnode=on&"+
						tickParams;


		Logger.log(url);
		long t1 = System.currentTimeMillis();
		String htmlContent = getPingTable(url);
		long t2 = System.currentTimeMillis();
		Logger.log("It took " + (t2-t1)/1000.0 + " seconds to GET pingtable.", "measurements/time" );
		if (htmlContent==null) return null;
		
		t1 = System.currentTimeMillis();
		String metrics[] = htmlContent.split(",");
		HashMap<String, HashMap<String,String>> hmMetrics = new HashMap<String, HashMap<String,String>>();
		String times[] = metrics[0].split("[+]");
		String metricLabels[] = {"tick","Min","Twentyfifth","Mean","Median","Seventyfifth","Ninetieth","Ninetyfifth","Max","InterquartileRange","StandardDeviation","NumberOfPairs"};

		for (int i = 1; i < metrics.length; i++) {

			HashMap<String,String> timeValue = new HashMap<String, String>();
			String values[] = metrics[i].split("[+]");
			for (int j = 1; j < times.length; j++) {
				try {
					if (times[j].contains("?")) continue;
					String value = String.valueOf(Double.parseDouble(values[j]));
					timeValue.put(times[j],value);
				} catch (Exception e) {}
			}	
			hmMetrics.put(metricLabels[i], timeValue);		
		}
		t2 = System.currentTimeMillis();
		Logger.log("It took " + (t2-t1)/1000.0 + " to process the valid result from pingtable.", "measurements/time" );
		return hmMetrics;
	}

	private static String getPingTable(String URL) {
		String s = HttpGetter.readPage(URL);
		if (s.contains("<h1>Sorry</h1>")) return null;
		//<!--?+Oct2007+Sep2007...+Nov2005,+0.141+0.000...+737.923+734.756,,-->
		//The format of the line in @contents (contains pingtable.pl web page) that we want is:
		if (s.indexOf("<!--")!=-1)
			return s.split("<!--")[1].split("-->")[0];
		else
			return null;
	}
	public static void main(String args[]) {
		getMeasurementMap("EDU.SLAC.STANFORD.N3","BR.UFRJ.N1","average_rtt","100","allyearly");
	}
	
}
