package edu.stanford.slac.pinger.model.general.file;

import java.io.File;

import edu.stanford.slac.pinger.model.general.GeneralModelSingletonParallelized;

public class FileToRepositoryThread extends Thread {
	private File file;
	
	public File getFile() {
		return file;
	}
	public FileToRepositoryThread(File file) {
		this.file = file;
	}
	public void run() {
		GeneralModelSingletonParallelized gm = GeneralModelSingletonParallelized.getInstance();
		gm.addNTriplesFile(file);
	}
}
