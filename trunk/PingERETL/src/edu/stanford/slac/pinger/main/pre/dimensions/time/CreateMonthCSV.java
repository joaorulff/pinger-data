package edu.stanford.slac.pinger.main.pre.dimensions.time;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.utils.Utils;

public class CreateMonthCSV {

	public static void main(String[] args) {	
		int id = 0;
		String year = null;
		String month = null;
		String timeStamp = null;
		String label = null;
		
		String monthShortName = null;
		
		NumberFormat f = new DecimalFormat("00"); 
		
		String monthFileContent = "#ID,Year,Month,TimeStamp,Label\n";
		
		Calendar date = Utils.getInitialDate();		
		Calendar finalDate = Utils.getFinalDate();

		String jsonMonths = "{";
		
		while (date.getTime().before(finalDate.getTime())){			
        	id++;        	
            year = String.valueOf(date.get(Calendar.YEAR));
            month = f.format(date.get(Calendar.MONTH)+1); //A month is represented by an integer from 0 to 11
            monthShortName = date.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH);
            timeStamp = year + "-" + month;
            label = monthShortName + year;
            monthFileContent += id + "," + year + "," + month + "," + timeStamp + "," + label + "\n";
            
            jsonMonths += "\""+ label + "\"" + ":" + id + ",";
            
            date.set(Calendar.MONTH, date.get(Calendar.MONTH)+1); //Next month
		}
		
		jsonMonths =  jsonMonths.substring(0,jsonMonths.length()-1) + "}";
		
		Utils.writeIntoFile(jsonMonths, C.MONTHS_JSON);
		
        Utils.writeIntoFile(monthFileContent, C.MONTH_CSV);

	}

}
