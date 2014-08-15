package edu.stanford.slac.pinger.model.general.file;

import java.io.File;
import java.util.ArrayList;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.model.general.GeneralModelSingletonParallelized;

public class FileToRepositoryThreadsStarter {

	/**
	 * Parallelizing the process of loading files into the repository has shown to be less safe (higher risks to corrupt the database) and not significantly faster than the sequential process.
	 */
	@Deprecated 
	public static void startParalellized() {
		GeneralModelSingletonParallelized gm = GeneralModelSingletonParallelized.getInstance();
		gm.writeTriplesAndClean();
		File ntriplesDir = new File(C.NTRIPLES_DIR);
		File files[] = ntriplesDir.listFiles();
		int nThreads = files.length;
		FileToRepositoryThread[] threads = new FileToRepositoryThread[nThreads];
		int i = 0;
		for (File f : files) {
			threads[i++] =  new FileToRepositoryThread(f);
		}
		for (i = 0; i < threads.length; i++) {
			threads[i].start();
		}
		for (i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
				Logger.log("Thread " + threads[i].getId() + " has finished loading the file "+threads[i].getFile().getPath(), "threads");
			} catch (Exception e) { 
				Logger.log(FileToRepositoryThreadsStarter.class + ".start() ", e, "threads/errors"); 
			}
		}
	}
	
	public static void startSequential() {
		GeneralModelSingletonParallelized gm = GeneralModelSingletonParallelized.getInstance();
		gm.writeTriplesAndClean();
		loadNTriplesDirectorySequentially(new File(C.NTRIPLES_DIR));
	}
	
	public static void startGrouped() {
		GeneralModelSingletonParallelized gm = GeneralModelSingletonParallelized.getInstance();
		gm.writeTriplesAndClean();
		
		File ntriplesDir[] = new File(C.NTRIPLES_DIR).listFiles();
		ArrayList<ArrayList<File>> filesToLoad = new ArrayList<ArrayList<File>>();
		int i = 0;
		ArrayList<File> fileList = new ArrayList<File>();
		for (File f : ntriplesDir) {
			fileList.add(f);
			if (i < C.NUM_FILES_PER_TIME) {			
				i++;
			} else {
				filesToLoad.add(fileList);
				fileList = new ArrayList<File>();
				i = 0;
			}
		}
		for (ArrayList<File> fLst : filesToLoad) {
			threadsInitializerForFilesList(fLst);
		}
	}
	
	private static void threadsInitializerForFilesList(ArrayList<File> lstFiles) {
		int nThreads = lstFiles.size();
		FileToRepositoryThread[] threads = new FileToRepositoryThread[nThreads];
		int i = 0;
		for (File f : lstFiles) {
			threads[i++] =  new FileToRepositoryThread(f);
		}
		for (i = 0; i < threads.length; i++) {
			threads[i].start();
		}
		for (i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
				Logger.log("Thread " + threads[i].getId() + " has finished loading the file "+threads[i].getFile().getPath(), "threads");
			} catch (Exception e) { 
				 Logger.log(FileToRepositoryThreadsStarter.class + ".threadsInitializerForFilesList() " , e, "threads/errors");
			}
		}
	}
	
	public static boolean loadNTriplesDirectorySequentially(File ntriplesDir) {
		try {
			GeneralModelSingletonParallelized gm = GeneralModelSingletonParallelized.getInstance();
			File files[] = ntriplesDir.listFiles();
			int nFiles = files.length;
			for (File f : files) {
				gm.addNTriplesFile(f);
				Logger.log("File " + f.getAbsolutePath() + " was sucessfully included and is now being deleted.");
				f.delete();
				nFiles--;
				Logger.log("There are still " + nFiles + " files remaining in the triples directory to be inserted.");
			}
			return true;
		} catch (Exception e) {
			Logger.error("An error occurred while loading NTriple directory " + ntriplesDir.getAbsolutePath() + " into the repository.", e);
			return false;
		}
	}
	
}
