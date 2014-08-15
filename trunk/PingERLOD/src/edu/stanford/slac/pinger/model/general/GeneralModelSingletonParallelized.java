package edu.stanford.slac.pinger.model.general;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.P;
import edu.stanford.slac.pinger.model.general.file.FileToRepositoryThreadsStarter;
import edu.stanford.slac.pinger.model.general.file.LoadRemainingFiles;

/**
 * This class is to be used to manipulate the RDF Storage.
 * Please, make sure to close() the object when you finish using.
 * Attention: However, close() the connection only once.
 * @author Renan
 *
 */
public class GeneralModelSingletonParallelized extends GeneralModelSingletonParent {
	protected GeneralModelSingletonParallelized() {}
	
	public static GeneralModelSingletonParallelized getInstance() {
		if (instance == null) {			
			instance = new GeneralModelSingletonParallelized();
			instance.connect();
		}
		return (GeneralModelSingletonParallelized) instance;
	}
	
	@Override
	protected void _addTripleLiteral(String resourceURI, 
			String propertyNS, String dataProperty,
			String obj) {
		checkHeap();
		allTriples.append(
				"<"+resourceURI+"> " +
				"<"+P.MAP_PREFIXES.get(propertyNS)+dataProperty+"> " +
				obj + " . \n"			
		);
	}

	@Override
	protected void _addTripleResource(String subj, String pred, String obj) {
		checkHeap();
		allTriples.append(
				"<"+subj+"> " +
				"<"+pred+"> " +
				"<"+obj+"> . \n"
		);
	}

	/* **************************************************************************************
	 ********************************** FILES PART ******************************************
	 ****************************************************************************************/
	private String currentTick = null;
	private String currentMetric = null;
	public void setCurrentTick(String currentTick) {this.currentTick = currentTick;}
	public void setCurrentMetric(String currentMetric) {this.currentMetric = currentMetric;}
	
	private int fileContentSize;
	private int currentFileIndex;
	private String currentFilePath;
	
	private static int CUT_HEAP_MEMORY = -1;
	private static int CUT_HD = -1;
	private StringBuffer allTriples;

	/**
	 * This method prevents the StringBuffer variable to exceed the available memory.
	 */
	private synchronized void checkHeap() {
		if (allTriples.length() >= CUT_HEAP_MEMORY) {
			try {
				if (fileContentSize >= CUT_HD) {
					fileContentSize = 0;
					String fileName;
					if (currentTick != null && currentMetric != null)
						fileName = currentTick + "_" + currentMetric + "_" + currentFileIndex++;
					else {
						fileName = "n"+currentFileIndex++;
					}
					currentFilePath = C.NTRIPLES_DIR+fileName+".ntriples";
					C.createFileGrantingPermissions(currentFilePath);
				}
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(currentFilePath, true)));
				out.write(allTriples.toString());
				out.close();
				fileContentSize += allTriples.length();
				allTriples = new StringBuffer();
			} catch (Exception e) {
				Logger.error("GeneralModelSingletonParallelized.checkHeap " , e);
			}
		}
	}

	public void writeTriplesAndClean() {
		try {
			PrintWriter out;
			out = new PrintWriter(new BufferedWriter(new FileWriter(currentFilePath, true)));
			out.write(allTriples.toString());
			out.close();
			allTriples = new StringBuffer();
		} catch (Exception e) {
			Logger.error("GeneralModelSingletonParallelized.writeTriplesAndClean " ,  e);
		}
	}

	public void start() {
		C.cleanDirectory(C.NTRIPLES_DIR);
		allTriples = new StringBuffer();
		fileContentSize = 0;
		currentFileIndex = 1;
		String fileName = null;
		if (currentTick != null && currentMetric != null)
			fileName = currentTick + "_" + currentMetric + "_" + currentFileIndex;
		else {
			fileName = "n"+currentFileIndex;
		}
		currentFilePath = C.NTRIPLES_DIR+fileName+".ntriples";
		C.createFileGrantingPermissions(currentFilePath);
		
		CUT_HEAP_MEMORY = (int) (Runtime.getRuntime().freeMemory()/C.CUT_HEAP_COEF); //divided by 2 is the standard.. divided by 10 generates lots of files of ~ 13 MB
		CUT_HD = C.CUT_HD*C.MB;
		
		if (currentTick != null && currentTick.contains("days"))
			LoadRemainingFiles.addNTriplesDirectory(C.NTRIPLES_DIR);
	}

	
	public void saveNTriplesIntoRepository() {
		FileToRepositoryThreadsStarter.startSequential();
	}

}
