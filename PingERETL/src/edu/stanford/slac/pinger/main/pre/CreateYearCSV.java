package edu.stanford.slac.pinger.main.pre;

import java.util.Calendar;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.utils.Utils;

public class CreateYearCSV {
	
	public static void main(String[] args) {
		int id = 0;
		String year = null;
		String timeStamp = null;
		String label = null;
		
		String yearFileContent = "ID,Year,TimeStamp,Label\n";
		
		Calendar date = Utils.setInitialDate();
		Calendar finalDate = Utils.setFinalDate();
		
		while (date.getTime().before(finalDate.getTime())){			
        	id++;        	
            year = String.valueOf(date.get(Calendar.YEAR));
            timeStamp = year;
            label = year;
            yearFileContent += id + "," + year + "," + timeStamp + "," + label + "\n";
            
            date.set(Calendar.YEAR, date.get(Calendar.YEAR)+1); //Next year
		}
		
		Utils.writeIntoFile(yearFileContent, C.YEAR_CSV);

	}

}
