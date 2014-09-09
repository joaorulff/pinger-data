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
	private StringBuffer fileContent; //StringBuffer is synchronized
	
	private String transformedFilesDirectory;
	private String tick;
	private String metric;
	private String year;
	private String monitor;
	
	public FileHandler(String transformedFilesDirectory, String tick, String metric, String year, String monitor) {
		this.year = year;
		this.transformedFilesDirectory = transformedFilesDirectory;
		this.tick = tick;
		this.metric = metric;
		this.monitor = monitor;
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
					currentFileIndex++;
					currentFilePath = transformedFilesDirectory+getFileName();
					Utils.createFileGrantingPermissions(currentFilePath);
				}
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(currentFilePath, false)));
				out.write(fileContent.toString());
				out.close();
				fileContentSize += fileContent.length();
				fileContent = new StringBuffer();
			} catch (Exception e) {
				Logger.error(FileHandler.class.getName()+".checkHeap " , e);
			}
		}
	}

	public void writeContentAndClean() {
		try {
			if (fileContent==null || fileContent.toString().equals(""))
				return;
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(currentFilePath, false)));
			out.write(fileContent.toString());
			out.close();
			fileContent = new StringBuffer();
		} catch (Exception e) {
			Logger.error(FileHandler.class.getName()+".writeContentAndClean " ,  e);
		}
	}
	
	private String getFileName() {
		String fileName = Utils.getFileNameBeginning(tick, metric, year, monitor) + "_" + currentFileIndex + ".csv";
		return fileName;
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
		currentFilePath = transformedFilesDirectory+getFileName();
		//Utils.createFileGrantingPermissions(currentFilePath);
		
		CUT_HEAP_MEMORY = (int) (Runtime.getRuntime().freeMemory()/C.CUT_HEAP_COEF); //divided by 2 is the standard.. divided by 10 generates lots of files of ~ 13 MB
		CUT_HD = C.CUT_HD;
		
		
	}
	
	public void setCurrentTick(String currentTick) {this.tick = currentTick;}
	public void setCurrentMetric(String currentMetric) {this.metric = currentMetric;}


}
