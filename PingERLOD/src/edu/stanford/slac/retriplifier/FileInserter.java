package edu.stanford.slac.retriplifier;

import java.io.File;

import org.openrdf.model.Resource;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.rio.RDFFormat;

import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.P;

public class FileInserter {

	public static final int OWLIM = 1;
	public static final int VIRTUOSO = 2;
	public static final int SESAME = 3;
	
	private RepositoryConnection con;
	public FileInserter(int repositoryType) {
		if (repositoryType == OWLIM) {
			con = OWLIMConnection.getCon();
		}
	}
	
	public void insertSingleFile(String filePath) {
		File file = new File(filePath);
		try {
			Logger.log("Loading the file " + file.getPath() + " into the repository...");
			long t1 = System.currentTimeMillis();
			con.add(file, P.BASE, RDFFormat.NTRIPLES, (Resource) null);
			long t2 = System.currentTimeMillis();
			Logger.log("...done! It took " + (t2-t1)/1000.0/60.0 + " minutes to load the file " + file.getPath() + " into the repository...", "measurements/time");			
		} catch (Exception e) {
			Logger.error("GeneralModelSingletonParent.addNTriplesFile - Error while loading file " + file.getAbsolutePath() + " to the repository.", e);
		}
	}
	
	public void insertDirectory(String filePath) {
		File ntriplesDir = new File(filePath);
		try {
			File files[] = ntriplesDir.listFiles();
			for (File f : files) {
				insertSingleFile(f.getAbsolutePath());
			}
		} catch (Exception e) {
			Logger.error("An error occurred while loading NTriple directory " + ntriplesDir.getAbsolutePath() + " into the repository.", e);
		}
	}
	
	
}
