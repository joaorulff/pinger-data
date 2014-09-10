package tests;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import edu.stanford.slac.pinger.general.ErrorCode;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.utils.Utils;

public class T {

	public static void main(String[] args) throws ParseException {

		String content = Utils.readFile("C:/Users/Renan/Documents/Sample/Sample/1998_01_maximum_rtt_hourly_09.csv");
		String lines[] = content.split("\n");
		
		for (int i = 1; i < lines.length; i++) {
			String separetedValues[] = lines[i].split("\\s");
			
			String sourceNode = separetedValues[27];
			String destinationNode = separetedValues[30];
			
			System.out.println("Source: " + sourceNode);
			System.out.println("Destination: " + destinationNode);
			
		}
		
		
		
	}



}
