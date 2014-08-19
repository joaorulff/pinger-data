package edu.stanford.slac.pinger.main;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import edu.stanford.slac.pinger.general.utils.Utils;

public class CreateTimeTableCSV {

	public static final String NULL_VALUE = "\\N";
	
	public static void main(String[] args) {
		String timeTableFilePath = "./databaseTablesCSV/timeTable.csv";
		
		//Time table fields
		int id = 0;
		String year = null;
		String month = null;
		String day = null;
		//String hour = null;
		String unit = null;
		String timeStamp = null;
		String label = null;
		
		String monthShortName = null;
		String yearShortFormat = null;
		
		String timeTableContent = "ID,Year,Month,Day,Unit,TimeStamp,Label\n";
		
		Calendar initialDate = Calendar.getInstance();
		Calendar finalDate = Calendar.getInstance();
		
		initialDate.set(Calendar.DAY_OF_MONTH, 1);
		initialDate.set(Calendar.MONTH, Calendar.JANUARY);
		initialDate.set(Calendar.YEAR, 1998);
		
		finalDate.set(Calendar.DAY_OF_MONTH, 1);
		finalDate.set(Calendar.MONTH, Calendar.JANUARY);
		finalDate.set(Calendar.YEAR, 2015);
        
        NumberFormat f = new DecimalFormat("00");        
        
        while (initialDate.getTime().before(finalDate.getTime())){
        	
        	/* ***** Day Granularity ***** */
        	id++;        	
            year = String.valueOf(initialDate.get(Calendar.YEAR));
            yearShortFormat = year.substring(2, 4);
            month = f.format(initialDate.get(Calendar.MONTH)+1); //A month is represented by an integer from 0 to 11
            monthShortName = initialDate.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH);
            day = f.format(initialDate.get(Calendar.DAY_OF_MONTH));
            unit = "Day";
            timeStamp = year + "-" + month + "-" + day;
            label = yearShortFormat + monthShortName + day;
            timeTableContent += id + "," + year + "," + month + "," + day + "," + unit + "," + timeStamp + "," + label + "\n";
            
            /* ***** Month Granularity ***** */
        	id++;        	
            year = String.valueOf(initialDate.get(Calendar.YEAR));
            yearShortFormat = year.substring(2, 4);
            month = f.format(initialDate.get(Calendar.MONTH)+1); //A month is represented by an integer from 0 to 11
            monthShortName = initialDate.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH);
            day = NULL_VALUE;
            unit = "Month";
            timeStamp = year + "-" + month;
            label = monthShortName + year;
            timeTableContent += id + "," + year + "," + month + "," + day + "," + unit + "," + timeStamp + "," + label + "\n";
            
            /* ***** Year Granularity ***** */
        	id++;        	
            year = String.valueOf(initialDate.get(Calendar.YEAR));
            month = NULL_VALUE;
            day = NULL_VALUE;
            unit = "Year";
            timeStamp = year;
            label = year;
            timeTableContent += id + "," + year + "," + month + "," + day + "," + unit + "," + timeStamp + "," + label + "\n";
            
            initialDate.set(Calendar.DAY_OF_MONTH, initialDate.get(Calendar.DAY_OF_MONTH)+1); //Next day
        }
        
        Utils.createFileGrantingPermissions(timeTableFilePath);
        Utils.writeIntoFile(timeTableContent, timeTableFilePath);
		
	}

}
