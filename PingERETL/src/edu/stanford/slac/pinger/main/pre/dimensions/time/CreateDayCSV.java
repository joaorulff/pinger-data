package edu.stanford.slac.pinger.main.pre.dimensions.time;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.utils.Utils;

public class CreateDayCSV {

	public static void main(String[] args) {
		int id = 0;
		String year = null;
		String month = null;
		String day = null;
		String timeStamp = null;
		String label = null;
		
		String monthShortName = null;
		String yearShortFormat = null;
		
		NumberFormat f = new DecimalFormat("00"); 
		
		String dayFileContent = "#ID,Year,Month,Day,TimeStamp,Label\n";
		
		Calendar date = Utils.getInitialDate();		
		Calendar finalDate = Utils.getFinalDate();

		String jsonDays = "{";
		
		while (date.getTime().before(finalDate.getTime())){			
        	id++;        	
            year = String.valueOf(date.get(Calendar.YEAR));
            yearShortFormat = year.substring(2, 4);
            month = f.format(date.get(Calendar.MONTH)+1); //A month is represented by an integer from 0 to 11
            monthShortName = date.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH);
            day = f.format(date.get(Calendar.DAY_OF_MONTH));
            timeStamp = year + "-" + month + "-" + day;
            label = yearShortFormat + monthShortName + day;
            dayFileContent += id + "," + year + "," + month + "," + day + "," + timeStamp + "," + label + "\n";
            
            jsonDays += "\""+ label + "\"" + ":" + id + ",";
            
            date.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH)+1); //Next day
		}
		
		jsonDays =  jsonDays.substring(0,jsonDays.length()-1) + "}";
		
		Utils.writeIntoFile(jsonDays, C.DAYS_JSON);
		Utils.writeIntoFile(dayFileContent, C.DAY_CSV);

	}

}
