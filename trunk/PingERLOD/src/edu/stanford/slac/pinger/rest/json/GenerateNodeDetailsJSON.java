package edu.stanford.slac.pinger.rest.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.rest.pingtable.HttpGetter;

public class GenerateNodeDetailsJSON {

	static String project_home;
	
	private static void generateNodesFile(boolean all) {
		if (all) {
			String url = "http://www-iepm.slac.stanford.edu/pinger/pingerworld/all-nodes.cf";
			String htmlContent = HttpGetter.readPage(url);
			htmlContent = htmlContent.replaceAll("\"(.*)\"(.*)\"(.*)\"", "\"$1'$2'$3\"");
			C.writeIntoFile(htmlContent, C.PERL_DIR+"all-nodes.cf");
		} else {
			String url = "http://www-iepm.slac.stanford.edu/pinger/pingerworld/nodes.cf";
			String htmlContent = HttpGetter.readPage(url);
			C.writeIntoFile(htmlContent, C.PERL_DIR+"nodes.cf");
		}
	}

	/**
	 * This function uses a perl script to generate the JSON for NodeDetails.
	 * It first tries to use the environment variable 'perl' to run the script. If the variable is not set, it tries to use the PERL_HOME set in the class C.java.
	 * @param generateNodesFile
	 * @param all
	 */
	public static void start(boolean generateNodesFile, boolean all) {
		
		if (generateNodesFile)
			generateNodesFile(all);

		
		
		generateNodeDetailsJSON(true);
		
		
		/*
		if (success)
			boolean tests[] = testPerl();
			if (tests[0])
			generate(tests[1]);
		 */


	}

	private static void generateNodeDetailsJSON(boolean withPerlHome) {
		try {
			PrintWriter out = new PrintWriter(C.NODEDETAILS_JSON_FILE);
			generatePerlScript();
			String cmd = null;
			if (withPerlHome)
				cmd = C.PERL_HOME+"perl "+C.PERL_DIR+"getNodeDetails.pl";
			else
				cmd = "perl "+C.PERL_DIR+"getNodeDetails.pl";
			Logger.log(cmd);
			Process proc = Runtime.getRuntime().exec(cmd);
			BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				out.println(line);
			}
			input.close();
			out.close();
			Logger.log("JSON NODE_DETAILS generated!");
		} catch (Exception e) {
			Logger.error("generateNodeDetailsJSON", e);
		}
	}

	private static void generatePerlScript() {
		try {
			String filePath = C.PERL_DIR+"getNodeDetails.pl";

			BufferedReader br = new BufferedReader(new FileReader(filePath));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			String content = sb.toString();
			br.close();
			String rplc = C.PROJECT_HOME.replace("\\", "/");
			content = content.replaceAll("my [$]home = '.*';", "my \\$home = '"+rplc+"';");
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath, false)));
			out.print(content);
			out.close();
		} catch (Exception e) {
			Logger.error("generatePerlScript", e);
		}
	}

	
	@SuppressWarnings("unused")
	private static boolean [] testPerl(){
		boolean success = false;
		boolean withPerlHome = false;
		try {
			String line = null;
			Process proc = null;
			BufferedReader input = null;

			try {
				String cmd = "perl -e \"print 'Testing'\"";
				proc = Runtime.getRuntime().exec(cmd);
				input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				line = input.readLine();				
				success = line.equals("Testing");
				Integer.parseInt("p");
			} catch (Exception e) {
				Logger.log("testPerl", e);
				Logger.log("Could not execute a perl test. Environment variable 'perl' is not set properly.");
				Logger.log("Now trying with the PERL_HOME set in the project...");
				String cmd = C.PERL_HOME+"perl -e \"print 'Testing'\"";
				proc = Runtime.getRuntime().exec(cmd);
				input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				line = input.readLine();
				input.close();
				proc.destroy();
				if (line.equals("Testing")) {
					withPerlHome = true;
					success = true;
				}
			} finally {
				input.close();
				proc.destroy();
			}
		} catch (Exception e) {
			Logger.log("testPerl",e);
			return new boolean[]{false,false};
		}
		return new boolean[]{success,withPerlHome};
	}



	public static void main (String args[]) {
		start(false, false);
	}


}
