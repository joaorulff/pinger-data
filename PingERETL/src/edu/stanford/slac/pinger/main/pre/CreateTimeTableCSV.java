package edu.stanford.slac.pinger.main.pre;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import edu.stanford.slac.pinger.general.utils.Utils;

public class CreateTimeTableCSV {

	public static final String NULL_VALUE = "\\N";
	
	private static Calendar setInitialDate(){
		Calendar date = Calendar.getInstance();
		
		date.set(Calendar.DAY_OF_MONTH, 1);
		date.set(Calendar.MONTH, Calendar.JANUARY);
		date.set(Calendar.YEAR, 1998);
		
		return date;
	}
	
	private static Calendar setFinalDate(){
		Calendar date = Calendar.getInstance();
		
		int currentYear = date.get(Calendar.YEAR);
		
		date.set(Calendar.DAY_OF_MONTH, 1);
		date.set(Calendar.MONTH, Calendar.JANUARY);
		date.set(Calendar.YEAR, currentYear+1);
		
		return date;
	}
	
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
		
		NumberFormat f = new DecimalFormat("00"); 
		
		String timeTableContent = "ID,Year,Month,Day,Unit,TimeStamp,Label\n";
		
		Calendar date = setInitialDate();		
		Calendar finalDate = setFinalDate();
		
		/* ***** Day Granularity ***** */
		while (date.getTime().before(finalDate.getTime())){			
        	id++;        	
            year = String.valueOf(date.get(Calendar.YEAR));
            yearShortFormat = year.substring(2, 4);
            month = f.format(date.get(Calendar.MONTH)+1); //A month is represented by an integer from 0 to 11
            monthShortName = date.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH);
            day = f.format(date.get(Calendar.DAY_OF_MONTH));
            unit = "Day";
            timeStamp = year + "-" + month + "-" + day;
            label = yearShortFormat + monthShortName + day;
            timeTableContent += id + "," + year + "," + month + "," + day + "," + unit + "," + timeStamp + "," + label + "\n";
            
            date.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH)+1); //Next day
		}
		
		/* ***** Month Granularity ***** */
		date = setInitialDate();
		while (date.getTime().before(finalDate.getTime())){			
        	id++;        	
            year = String.valueOf(date.get(Calendar.YEAR));
            yearShortFormat = year.substring(2, 4);
            month = f.format(date.get(Calendar.MONTH)+1); //A month is represented by an integer from 0 to 11
            monthShortName = date.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH);
            day = NULL_VALUE;
            unit = "Month";
            timeStamp = year + "-" + month;
            label = monthShortName + year;
            timeTableContent += id + "," + year + "," + month + "," + day + "," + unit + "," + timeStamp + "," + label + "\n";
            
            date.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH)+1); //Next day
		}
        
		/* ***** Year Granularity ***** */
		date = setInitialDate();
		while (date.getTime().before(finalDate.getTime())){			
        	id++;        	
            year = String.valueOf(date.get(Calendar.YEAR));
            month = NULL_VALUE;
            day = NULL_VALUE;
            unit = "Year";
            timeStamp = year;
            label = year;
            timeTableContent += id + "," + year + "," + month + "," + day + "," + unit + "," + timeStamp + "," + label + "\n";
            
            date.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH)+1); //Next day
		}
        
        Utils.createFileGrantingPermissions(timeTableFilePath);
        Utils.writeIntoFile(timeTableContent, timeTableFilePath);
		
	}

}
