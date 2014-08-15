package edu.stanford.slac.pinger.etl.loader.local;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.utils.Utils;

/**
 * This class is to be used to manipulate the RDF Storage.
 * Please, make sure to close() the object when you finish using.
 * Attention: However, close() the connection only once.
 * @author Renan
 *
 */
public class FileHandler  {

	
	private int fileContentSize;
	private int currentFileIndex;
	private String currentFilePath;
	
	private static int CUT_HEAP_MEMORY = -1;
	private static int CUT_HD = -1;
	private StringBuffer fileContent;

	
	private String transformedFilesDirectory;
	private String currentTick;
	private String currentMetric;
	
	public FileHandler(String transformedFilesDirectory, String currentTick, String currentMetric) {
		this.transformedFilesDirectory = transformedFilesDirectory;
		this.currentTick = currentTick;
		this.currentMetric = currentMetric;
		start();
	}
	
	public void addRow(String row) {
		fileContent.append(row);
		checkHeap();
	}
	
	/**
	 * This method prevents the StringBuffer variable to exceed the available memory.
	 */
	private synchronized void checkHeap() {
		if (fileContent.length() >= CUT_HEAP_MEMORY) {
			try {
				if (fileContentSize >= CUT_HD) {
					fileContentSize = 0;
					String fileName;
					if (currentTick != null && currentMetric != null)
						fileName = currentMetric + "_" +  currentTick + "_" + currentFileIndex++;
					else {
						fileName = "n"+currentFileIndex++;
					}
					currentFilePath = transformedFilesDirectory+fileName+".csv";
					Utils.createFileGrantingPermissions(currentFilePath);
				}
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(currentFilePath, true)));
				out.write(fileContent.toString());
				out.close();
				fileContentSize += fileContent.length();
				fileContent = new StringBuffer();
			} catch (Exception e) {
				Logger.error(FileHandler.class.getName()+".checkHeap " , e);
			}
		}
	}

	public void writeTriplesAndClean() {
		try {
			PrintWriter out;
			out = new PrintWriter(new BufferedWriter(new FileWriter(currentFilePath, true)));
			out.write(fileContent.toString());
			out.close();
			fileContent = new StringBuffer();
		} catch (Exception e) {
			Logger.error(FileHandler.class.getName()+".writeTriplesAndClean " ,  e);
		}
	}

	public void start() {
		File dir = new File(transformedFilesDirectory);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		transformedFilesDirectory = dir.getAbsolutePath() + File.separator;
		fileContent = new StringBuffer();
		fileContentSize = 0;
		currentFileIndex = 1;
		String fileName = null;
		if (currentTick != null && currentMetric != null)
			fileName = currentMetric + "_" +  currentTick+ "_" + currentFileIndex;
		else {
			fileName = "n"+currentFileIndex;
		}
		currentFilePath = transformedFilesDirectory+fileName+".csv";
		Utils.createFileGrantingPermissions(currentFilePath);
		
		CUT_HEAP_MEMORY = (int) (Runtime.getRuntime().freeMemory()/C.CUT_HEAP_COEF); //divided by 2 is the standard.. divided by 10 generates lots of files of ~ 13 MB
		CUT_HD = C.CUT_HD*C.MB;
		
		
	}
	
	public void setCurrentTick(String currentTick) {this.currentTick = currentTick;}
	public void setCurrentMetric(String currentMetric) {this.currentMetric = currentMetric;}


}
