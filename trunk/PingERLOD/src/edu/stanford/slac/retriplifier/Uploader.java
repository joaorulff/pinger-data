package edu.stanford.slac.retriplifier;

import java.io.File;

import org.apache.commons.io.FileUtils;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;

public class Uploader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		C.setDebugLevel(1);
		String dir = "C:/tmp/pinger/data_pid11008/ntriples";
		TripleModelOff tm = TripleModelOff.getInstance();
		try {
			File fileDir = new File(dir);
			File files[] = fileDir.listFiles();
			
			for (File f : files) {
				Logger.log("Uploading file " + f);
				long t1 = System.currentTimeMillis();
				tm.addNTriplesFile(f);
				long t2 = System.currentTimeMillis();
				Logger.log("File uploaded successfully. It took " + (t2-t1)/1000.0 + " seconds.");
			}
		} catch (Exception e) {
			Logger.error("Uploading file ", e, "errors");
		}
		
	}

}
