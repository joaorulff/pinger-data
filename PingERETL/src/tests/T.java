package tests;

import java.util.ArrayList;

import edu.stanford.slac.pinger.general.utils.MeasurementUtils;

public class T {

	public static void main(String[] args) {
		ArrayList<String> months = MeasurementUtils.getMonthNames();
		ArrayList<String> years =MeasurementUtils.getYears();
		ArrayList<String> days = MeasurementUtils.getDays();
		ArrayList<String> alldaily = new ArrayList<String>();
		for (String year : years)
			for (String month : months)
				for (String day : days)
					alldaily.add(year.substring(2, 4)+month+day);
		System.out.println(alldaily.size());
	}
}
