package edu.stanford.slac.pinger.main.pre.dimensions.time;

import java.util.Calendar;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.utils.Utils;

public class CreateYearCSV {
	
	public static void main(String[] args) {
		int id = 0;
		String year = null;
		String timeStamp = null;
		String label = null;
		
		String yearFileContent = "#ID,Year,TimeStamp,Label\n";
		
		Calendar date = Utils.setInitialDate();
		Calendar finalDate = Utils.setFinalDate();
		
		String jsonYears = "{";
		
		while (date.getTime().before(finalDate.getTime())){			
        	id++;        	
            year = String.valueOf(date.get(Calendar.YEAR));
            timeStamp = year;
            label = year;
            yearFileContent += id + "," + year + "," + timeStamp + "," + label + "\n";
            
            jsonYears += "\""+ label + "\"" + ":" + id + ",";
            
            date.set(Calendar.YEAR, date.get(Calendar.YEAR)+1); //Next year
		}
		
		jsonYears =  jsonYears.substring(0,jsonYears.length()-1) + "}";
		
		Utils.writeIntoFile(jsonYears, C.YEARS_JSON);
		
		Utils.writeIntoFile(yearFileContent, C.YEAR_CSV);

	}

}
