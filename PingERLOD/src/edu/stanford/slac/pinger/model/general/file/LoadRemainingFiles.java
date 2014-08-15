package edu.stanford.slac.pinger.model.general.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;

public class LoadRemainingFiles {

	public static void start() {

		try {
			List<String> lstDirsStr = IOUtils.readLines(new FileInputStream(C.REMAINING_NTRIPLES));
			List<?> lstDirsStrCpy =  copyList(lstDirsStr);
			for (String dirStr : lstDirsStr) {
				File dir = new File(dirStr);
				if (dir.exists()) {
					if (dir.isDirectory()) {
						if (dir.listFiles().length > 0) {
							if (FileToRepositoryThreadsStarter.loadNTriplesDirectorySequentially(dir)) {
								lstDirsStrCpy.remove(dirStr);
							}							
						} else { 
							//Delete the directory entry if there are no files in it.
							if (C.deleteDirAndContents(dir.getParent())) {
								lstDirsStrCpy.remove(dirStr);
							}
						}
					}
				} else {
					lstDirsStrCpy.remove(dirStr);
				}
			}
			IOUtils.writeLines(lstDirsStrCpy, "\n", new FileOutputStream(C.REMAINING_NTRIPLES));
		} catch (Exception e) {
			Logger.error("An error occurred while loading remaning NTriple files.",e);
		}

	}

	private static List<?> copyList(List<?> lst) {
		List<Object> lstCpy = new ArrayList<Object>();
		for (Object o : lst) {
			lstCpy.add(o);
		}
		return lstCpy;
	}


	public static void addNTriplesDirectory(String dirPath) {
		try {
			List<String> lstDirsStr = IOUtils.readLines(new FileInputStream(C.REMAINING_NTRIPLES));
			if (!lstDirsStr.contains(dirPath)) {
				PrintWriter out;
				out = new PrintWriter(new BufferedWriter(new FileWriter(C.REMAINING_NTRIPLES, true)));
				out.write(dirPath+"\n");
				out.close();
			}
		} catch (Exception e) {
			Logger.log("LoadRemainingFiles.addNTriplesDirectory("+dirPath+")", e, "errors");
		}
	}

	public static boolean thereAreRemainingFiles() {
		try {
			List<String> lstDirsStr = IOUtils.readLines(new FileInputStream(C.REMAINING_NTRIPLES));
			return ( lstDirsStr.size() > 0);
		} catch (Exception e) {
			Logger.log("Error while reading file " + C.REMAINING_NTRIPLES, e);
			return true;
		}

	}
}
