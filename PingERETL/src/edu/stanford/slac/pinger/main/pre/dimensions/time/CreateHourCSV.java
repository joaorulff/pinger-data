package edu.stanford.slac.pinger.main.pre.dimensions.time;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.utils.Utils;

public class CreateHourCSV {

	public static void main(String[] args) {
		int id = 0;
		String year = null;
		String month = null;
		String day = null;
		String timeStamp = null;
		String label = null;
		
		
		NumberFormat f = new DecimalFormat("00"); 
		
		StringBuilder dayFileContent = new StringBuilder("#ID,Year,Month,Day,Hour,TimeStamp,Label\n");;
		
		Calendar date = Utils.getInitialDate();		
		Calendar finalDate = Utils.getFinalDate();

		StringBuilder jsonDays = new StringBuilder("{");;
		
		while (date.getTime().before(finalDate.getTime())){	
          year = String.valueOf(date.get(Calendar.YEAR));
            month = f.format(date.get(Calendar.MONTH)+1); //A month is represented by an integer from 0 to 11
            
            day = f.format(date.get(Calendar.DAY_OF_MONTH));
            
            for (int i = 0; i <= 23; i++) {
            	id++;        	
            	String hour = (i<10)?"0"+i:i+"";
            	label = year + "-" + month + "-"  + day + "-" + hour;
            	timeStamp = year + "-" + month + "-" + day + " " + hour + ":00:00";
            	dayFileContent.append(id + "," + year + "," + month + "," + day + "," + hour + "," + timeStamp + "," + label + "\n");
            	jsonDays.append("\""+ label + "\"" + ":" + id + ",");
            }
            
            date.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH)+1); //Next day
		}
		
		String jsonDaysStr =  Utils.removeLastCharacterFromString(jsonDays.toString()) + "}";
		
		Utils.writeIntoFile(jsonDaysStr, C.HOURS_JSON);
		Utils.writeIntoFile(dayFileContent.toString(), C.HOUR_CSV);

	}

}
