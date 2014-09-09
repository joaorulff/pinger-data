package edu.stanford.slac.pinger.general.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import edu.stanford.slac.pinger.beans.MetricBean;

public final class MeasurementUtils {
	
	public static final String packetSizes[] = {
		"100"
		//"1000"
	};
	
	public static final String[] TICKS =  {
		"allyearly",
		"allmonthly",
		//"last365days"
	};
	
	public static String[] METRICS = {
		/*
		"throughput",
		"packet_loss",
		"average_rtt",
		"MOS",
		"alpha",
		"conditional_loss_probability",
		"duplicate_packets",
		"ipdv",
		"unreachability",
		"zero_packet_loss_frequency",
		"minimum_rtt",
		*/
		
		"iqr",
		"maximum_rtt",
		"minimum_packet_loss",
		"out_of_order_packets",
		"unpredictability",
	};
	
	public static HashMap<String,MetricBean> mapMetricBean = null;
	static {
		mapMetricBean = new HashMap<String, MetricBean>();
		mapMetricBean.put("throughput", new MetricBean("throughput", "Kilobitpersec", "Throughput", "TCPThroughputMeasurement", 1));
		mapMetricBean.put("packet_loss", new MetricBean("packet_loss", "percent", "Packet Loss", "PacketLossMeasurement", 2));
		mapMetricBean.put("average_rtt", new MetricBean("average_rtt", "millisecond", "Average RTT", "AverageRoundTripDelayMeasurement", 3));
		mapMetricBean.put("MOS", new MetricBean("MOS", "Dimensionless", "MOS", "MOSMeasurement", 4));
		mapMetricBean.put("alpha", new MetricBean("alpha", "Dimensionless", "Directivity", "DirectivityMeasurement", 5));
		mapMetricBean.put("conditional_loss_probability", new MetricBean("conditional_loss_probability", "percent", "Conditional Loss Probability", "ConditionalLossProbabilityMeasurement", 6));
		mapMetricBean.put("duplicate_packets", new MetricBean("duplicate_packets", "Dimensionless", "Duplicate Packets", "DuplicatePacketsMeasurement", 7));
		mapMetricBean.put("ipdv", new MetricBean("ipdv", "millisecond", "Inter Packet Delay Variation", "InterPacketDelayVariationMeasurement", 8));
		mapMetricBean.put("unreachability", new MetricBean("unreachability", "Dimensionless", "Unreachability", "PingUnreachabilityMeasurement", 9));
		mapMetricBean.put("zero_packet_loss_frequency", new MetricBean("zero_packet_loss_frequency", "percent", "Zero Packet Loss Frequency", "ZeroPacketLossFrequencyMeasurement", 10));
		mapMetricBean.put("minimum_rtt", new MetricBean("minimum_rtt", "millisecond", "Minimum RTT", "MinimumRoundTripDelayMeasurement", 11));

		/*
		//These following metrics are not being considered:
		mapMetricBean.put("iqr", new MetricBean("iqr", "millisecond", "Interquartile Range", "SimpleMeasurementIQRMeasurement", 12));
		mapMetricBean.put("maximum_rtt", new MetricBean("maximum_rtt", "millisecond", "Maximum RTT", "MaximumRoundTripDelayMeasurement", 13));
		mapMetricBean.put("minimum_packet_loss", new MetricBean("minimum_packet_loss", "percent", "Minimum Packet Loss", "MinimumPacketLossMeasurement", 14));
		mapMetricBean.put("out_of_order_packets", new MetricBean("out_of_order_packets", "Dimensionless", "Packets Out of Order", "OutOfOrderPacketsMeasurement", 15));
		mapMetricBean.put("unpredictability", new MetricBean("unpredictability", "Dimensionless", "Unpredictability", "PingUnpredictabilityMeasurement", 16));
		*/
	}
	


	private static HashMap<String,String> mapUnitsSymbols = null;
	public static  HashMap<String,String> getMapUnitsSymbols() {
		if (mapUnitsSymbols == null) {
			mapUnitsSymbols = new HashMap<String, String>();
			mapUnitsSymbols.put("Dimensionless", "Dimensionless");
			mapUnitsSymbols.put("millisecond", "ms");
			mapUnitsSymbols.put("percent", "%");
			mapUnitsSymbols.put("Kilobitpersec", "kbit/s");
			mapUnitsSymbols.put("bit", "bit");
		}
		return mapUnitsSymbols;
	}
	
	
	public static void includeHourly(ArrayList<String> tickParams) {
		ArrayList<String> days = new ArrayList<String>();
		ArrayList<String> months = new ArrayList<String>();
		for (int i = 1; i <= 9; i++) {
			days.add("0"+i);
			months.add("0"+i);
		}
		for (int i = 10; i <= 31; i++) {
			days.add(i+"");
			if (i<=12)months.add(i+"");
		}

		ArrayList<String> years = new ArrayList<String>();
		GregorianCalendar gc = new GregorianCalendar();
		int year = gc.get(Calendar.YEAR) + 1;
		for (int i = 1998; i <= year; i++) {
			years.add(i+"");
		}

		for (String d : days) {
			for (String m : months) {
				for (String y : years) {
					String date = "year="+y+"&month="+m+"&day="+d;
					tickParams.add("tick=hourly&"+date);
				}
			}
		}
	}
	public static String getInitialsByMonthNumber(String monthNumber) {
		if (monthNumber.equals("01")) return "Jan";
		else if (monthNumber.equals("02")) return "Feb";
		else if (monthNumber.equals("03")) return "Mar";
		else if (monthNumber.equals("04")) return "Apr";
		else if (monthNumber.equals("05")) return "May";
		else if (monthNumber.equals("06")) return "Jun";
		else if (monthNumber.equals("07")) return "Jul";
		else if (monthNumber.equals("08")) return "Aug";
		else if (monthNumber.equals("09")) return "Sep";
		else if (monthNumber.equals("10")) return "Oct";
		else if (monthNumber.equals("11")) return "Nov";
		else if (monthNumber.equals("12")) return "Dec";
		else return null;
	}

	public static String getDayStringByDayNumber(int dayNumber) {
		if (dayNumber >= 1 && dayNumber <= 9) 
			return "0"+dayNumber;
		else
			return dayNumber+"";
	}
	
	public static String getMonthNumberString(int month) {
		if (month >= 1 && month <= 9) 
			return "0"+month;
		else
			return month+"";
	}
	
	public static String getLastDayOfMonth(String monthInitials) {
		if (monthInitials.equals("Jan")) return "31";
		else if (monthInitials.equals("Feb")) return "30";
		else if (monthInitials.equals("Mar")) return "31";
		else if (monthInitials.equals("Apr")) return "30";
		else if (monthInitials.equals("May")) return "31";
		else if (monthInitials.equals("Jun")) return "30";
		else if (monthInitials.equals("Jul")) return "31";
		else if (monthInitials.equals("Aug")) return "31";
		else if (monthInitials.equals("Sep")) return "30";
		else if (monthInitials.equals("Oct")) return "31";
		else if (monthInitials.equals("Nov")) return "30";
		else if (monthInitials.equals("Dec")) return "31";
		else return null;
	}
	
	public static String getMonthNumberStringByMonthInitials(String monthInitials) {
		if (monthInitials.equals("Jan")) return "01";
		else if (monthInitials.equals("Feb")) return "02";
		else if (monthInitials.equals("Mar")) return "03";
		else if (monthInitials.equals("Apr")) return "04";
		else if (monthInitials.equals("May")) return "05";
		else if (monthInitials.equals("Jun")) return "06";
		else if (monthInitials.equals("Jul")) return "07";
		else if (monthInitials.equals("Aug")) return "08";
		else if (monthInitials.equals("Sep")) return "09";
		else if (monthInitials.equals("Oct")) return "10";
		else if (monthInitials.equals("Nov")) return "11";
		else if (monthInitials.equals("Dec")) return "12";
		else return null;
	}
	
	public static int getMonthNumberByMonthInitials(String monthInitials) {
		if (monthInitials.equals("Jan")) return 1;
		else if (monthInitials.equals("Feb")) return 2;
		else if (monthInitials.equals("Mar")) return 3;
		else if (monthInitials.equals("Apr")) return 4;
		else if (monthInitials.equals("May")) return 5;
		else if (monthInitials.equals("Jun")) return 6;
		else if (monthInitials.equals("Jul")) return 7;
		else if (monthInitials.equals("Aug")) return 8;
		else if (monthInitials.equals("Sep")) return 9;
		else if (monthInitials.equals("Oct")) return 10;
		else if (monthInitials.equals("Nov")) return 11;
		else if (monthInitials.equals("Dec")) return 12;
		else return -1;
	}
	public static String getDayOfWeek(int day) {
		switch (day) {
		case 1: return "Sunday";
		case 2: return "Monday";
		case 3: return "Tuesday";
		case 4: return "Wednesday";
		case 5: return "Thursday";
		case 6: return "Friday";
		case 7: return "Saturday";
		default: return null;
		}
	}
	
	public static ArrayList<String> generateDaily() {
		ArrayList<String> months = getMonthNames();
		ArrayList<String> years = getYears();
		ArrayList<String> days = getDays();
		ArrayList<String> alldaily = new ArrayList<String>();
		for (String year : years)
			for (String month : months)
				for (String day : days)
					alldaily.add(year.substring(2, 4)+month+day);
		return alldaily;
	}
	public static ArrayList<String> generateMonthly() {
		ArrayList<String> months = getMonthNames();
		ArrayList<String> years = getYears();
		ArrayList<String> allmonthly = new ArrayList<String>();
		for (String year : years) {
			for (String month : months) {
				allmonthly.add(month+year);
			}
		}
		return allmonthly;
	}
	public static ArrayList<String> getDays() {
		ArrayList<String> days = new ArrayList<String>();
		for (int i = 1; i <= 9; i++) {
			days.add("0"+i);
		}
		for (int i = 10; i <= 31; i++) {
			days.add(i+"");
		}
		return days;
	}
	public static ArrayList<String> getMonthNames() {
		ArrayList<String> monthNames = new ArrayList<String>();
		monthNames.add("Jan");
		monthNames.add("Feb");
		monthNames.add("Mar");
		monthNames.add("Apr");
		monthNames.add("May");
		monthNames.add("Jun");
		monthNames.add("Jul");
		monthNames.add("Aug");
		monthNames.add("Sep");
		monthNames.add("Oct");
		monthNames.add("Nov");
		monthNames.add("Dec");
		return monthNames;
	}
	public static ArrayList<String>  getYears() {
		ArrayList<String> years = new ArrayList<String>();
		GregorianCalendar gc = new GregorianCalendar();
		int year = gc.get(Calendar.YEAR) + 1;
		for (int i = 1998; i <= year; i++) {
			years.add(i+"");
		}
		return years;
	}
}
