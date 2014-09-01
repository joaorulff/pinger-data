package edu.stanford.slac.pinger.general.utils;

import java.util.ArrayList;

import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.rest.HttpGetter;

public class NodesUtils {
	
	public static ArrayList<String> getSourceNodes() {
		return getHosts("http://www-wanmon.slac.stanford.edu/cgi-wrap/dbprac.pl?monalias=all", "\\s+([0-9])+\\s+");
	}
	
	private static ArrayList<String> getHosts(String URL, String regexPattern) {
		String content = HttpGetter.readPage(URL);	
		if (content == null) {
			Logger.error("Error while attempting to read page " + URL);
			return null;
		}
		
		
		ArrayList<String> hosts = new ArrayList<String>();

		if (content.indexOf("<pre>")!=-1) {
			content = content.split("<pre>")[1].split("</pre>")[0];
			String lines[] = content.split("\n");
			for (String host : lines) {
				String arr[] = host.split(regexPattern);
				if (arr.length > 1) {
					String h = arr[1].replaceAll("\\s", "");
					hosts.add(h);
				}
			}
			return hosts;
		} else
			return null;
	}
}
