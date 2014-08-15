package edu.stanford.slac.pinger.general;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Calendar;

public final class Logger {

	private static String LOG_DIR = C.PROJECT_HOME+"data/log/";
	public static String FILE_PREFIX = "log_";
	private static final int MAX_LOGSIZE = 1*C.MB;
	
	private static void _log(Object msg, String filePath, boolean criticalError, Exception e) {
		try {
			String strMsg = msg.toString();
			if (e!=null)
				strMsg += " -- " + e + " " + Arrays.toString(e.getStackTrace()) + " ";			
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(System.currentTimeMillis()); int day = c.get(Calendar.DAY_OF_MONTH); int month = c.get(Calendar.MONTH)+1; int year = c.get(Calendar.YEAR);
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int min = c.get(Calendar.MINUTE);
			int sec = c.get(Calendar.SECOND);
			String date = month+"/"+day+"/"+year + " " +hour+":"+min+":"+sec;
			
			strMsg = date + " -- PID=" + C.getCurrentPID() + " -- " + strMsg;
			
			String logfilePath = checkLogFile(filePath);
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(logfilePath, true)));
			out.println(strMsg);
			out.close();
			if (criticalError) {
				strMsg += "| The rest of this log is in: " + logfilePath;
				System.err.println(strMsg);
			} else if (C.DEBUG_LEVEL >= 0)
				System.out.println(strMsg);
		} catch (IOException e1) {
			System.out.println(e1);
		}
	}

	public static void log(Object msg) {
		_log(msg, LOG_DIR+FILE_PREFIX+"1.txt",false, null);
	}

	public static void log(Object msg, Exception e) {
		_log(msg, LOG_DIR+FILE_PREFIX+"1.txt",false, e);
	}

	public static void log(Object msg, String subDirectory) {
		String dir = LOG_DIR+FILE_PREFIX+subDirectory+"/";
		C.mkDir(dir);
		_log(msg, dir+"1.txt",false, null);
	}

	public static void log(Object msg, Exception e, String subDirectory) {
		String dir = LOG_DIR+FILE_PREFIX+subDirectory+"/";
		C.mkDir(dir);
		_log(msg, dir+"1.txt",false, e);
	}

	public static void error(Object msg) {
		_log("ERROR: " + msg, LOG_DIR+FILE_PREFIX+"err1.txt",true, null);
	}

	public static void error(Object msg, Exception e) {
		_log("ERROR: " + msg, LOG_DIR+FILE_PREFIX+"err1.txt",true, e);
	}

	public static void error(Object msg, String subDirectory) {
		String dir = LOG_DIR+FILE_PREFIX+subDirectory+"/";
		C.mkDir(dir);
		_log("ERROR: " + msg, dir+"err1.txt",true, null);
	}

	public static void error(Object msg, Exception e, String subDirectory) {
		String dir = LOG_DIR+FILE_PREFIX+subDirectory+"/";
		C.mkDir(dir);
		_log("ERROR: " + msg, dir+"err1.txt",true, e);
	}

	public static String checkLogFile(String filePath) {
		File logfile = new File(filePath);
		if (logfile.exists()) {
			try {
				if (logfile.length() > MAX_LOGSIZE) {
					File dir = new File(logfile.getParent());
					File[] listLogs = dir.listFiles();
					int newIndex = 1;
					for (File f : listLogs) {
						if (f.length() > MAX_LOGSIZE) {
							newIndex++;
						}					
					}
					int currentIndex = Integer.parseInt(C.extractNumbersFromString(logfile.getName()));
					String newFileName = logfile.getName().replace(currentIndex+"", newIndex+"");
					String newFilepath = logfile.getParent() + File.separator + newFileName;
					File newLogfile = new File(newFilepath);
					return newLogfile.getAbsolutePath();
				}
			} catch (Exception e) {
				System.out.println(e + " " + e);
			}
		} else {
			C.mkDirGrantingPermissions(logfile.getParent());
			C.mkDirGrantingPermissions(LOG_DIR);
			C.createFileGrantingPermissions(filePath);
		}
		return filePath;
	}
	
	public static void setLOG_DIR(String log_dir) {
		LOG_DIR =  log_dir;
	}
}
