package edu.stanford.slac.retriplifier.off;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import edu.stanford.slac.pinger.bean.MetricBean;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.P;
import edu.stanford.slac.pinger.general.utils.MeasurementUtils;
import edu.stanford.slac.pinger.general.vocabulary.MD;
import edu.stanford.slac.pinger.general.vocabulary.MU;
import edu.stanford.slac.pinger.general.vocabulary.PingER_ONT;
import edu.stanford.slac.pinger.general.vocabulary.RDF;
import edu.stanford.slac.pinger.general.vocabulary.Time;
import edu.stanford.slac.pinger.model.general.GeneralModelSingletonParallelized;
import edu.stanford.slac.pinger.model.general.GeneralModelSingletonParent;
import edu.stanford.slac.pinger.model.general.GeneralModelSingletonSequential;
import edu.stanford.slac.retriplifier.TripleModelOff;

public class MeasurementParametersInstantiatorOff {

	public static void start(boolean timestamp) {
		instatiatePacketSize();
		instantiateUnits();
		instantiateMetrics();
		if (timestamp)
			instantiateTimeStamp();
	}
	
	private static void instantiateUnits() {
		TripleModelOff tm = null;
		try {
			tm =  TripleModelOff.getInstance();
			HashMap<String,String> mapUnitsSymbols = MeasurementUtils.getMapUnitsSymbols();
			for (String key : mapUnitsSymbols.keySet()) {
				String unitURI = MU.URI + key;
				tm.addTripleResource(unitURI, RDF.type, MU.Unit);	
				tm.addTripleLiteral(unitURI, MU.hasSymbol, mapUnitsSymbols.get(key));
			}
		} catch (Exception e) {
			Logger.log(MeasurementUtils.class + ".instantiateUnits() " , e, "measurement_parameters/errors" );
		} finally {
			tm.writeTriplesAndClean();
		}
		
	}
	
	private static void instantiateMetrics() {
		TripleModelOff tm = null;
		try {
			tm =  TripleModelOff.getInstance();
			HashMap<String,MetricBean> mapMetricBean = MeasurementUtils.mapMetricBean;
			for (MetricBean mb : mapMetricBean.values()) {
				String metric = mb.getInstantiationName().replace("Measurement", "");
				String metricURI = P.BASE + "Metric"+metric;
				tm.addTripleResource(metricURI, RDF.type, MD.Metric);		
				tm.addTripleResource(metricURI, PingER_ONT.defaultUnit, MU.URI + mb.getDefaultUnit());
				tm.addTripleLiteral(metricURI, PingER_ONT.displayValue, mb.getDisplayName());				
			}
		} catch (Exception e) {
			Logger.log(MeasurementUtils.class + ".instantiateSimpleMeasurement() " , e, "measurement_parameters/errors" );
		} finally {
			tm.writeTriplesAndClean();
		}
	}
	

	private static void instatiatePacketSize() {
		TripleModelOff tm = null;
		try {		
			tm =  TripleModelOff.getInstance();
			for (String s : MeasurementUtils.packetSizes) {
				String packetSizeURI = P.BASE + "PacketSize"+s;
				tm.addTripleResource(packetSizeURI, RDF.type, MD.PacketSize);
				tm.addTripleLiteralDecimal(packetSizeURI, MD.PacketSizeValue, Float.parseFloat(s));
				tm.addTripleResource(packetSizeURI, PingER_ONT.defaultUnit, MU.URI + "bit");
			}
		} catch (Exception e) {
			Logger.log(MeasurementUtils.class + ".instatiatePacketSize() ", e, "measurement_parameters/errors" );
		} finally {
			tm.writeTriplesAndClean();
		}
	}	

	private static void instantiateTimeStamp() {
		ArrayList<String> years = MeasurementUtils.getYears();
		ArrayList<String> allmonthly = MeasurementUtils.generateMonthly();
		ArrayList<String> alldaily = MeasurementUtils.generateDaily();

		TripleModelOff tm = null;
		try {
			tm = TripleModelOff.getInstance();
			Logger.log("Instantiating years...","measurement_parameters");
			for (String year : years) {
				timeStamp(year, "allyearly", tm);
			}
			Logger.log("Instantiating allmonthly...","measurement_parameters");
			for (String monthly : allmonthly) {
				timeStamp(monthly, "allmonthly", tm);
			}

			Logger.log("Instantiating alldaily...","measurement_parameters");
			for (String daily : alldaily) {
				timeStamp(daily, "days", tm);
			}

		} catch (Exception e) {
			Logger.log(MeasurementUtils.class + ".instantiateTimeStamp() ", e, "measurement_parameters/errors" );
		} finally {
			tm.writeTriplesAndClean();
		}
	}

	public static String timeStamp(String time, String tickParameter, TripleModelOff tm) {
		String timeURI = null;

		if (tickParameter.equals("allyearly")) {
			timeURI = P.BASE + "Time"+time;
			tm.addTripleResource(timeURI, Time.unitType, Time.unitYear);
			tm.addTripleLiteralNonNegativeInteger(timeURI, Time.year, Integer.parseInt(time));
			String year = time;
			String startDate = year + "-01-01T00:00:00";
			String endDate = year + "-12-31T23:59:59";
			tm.addTripleLiteral(timeURI, PingER_ONT.displayValue, time);
			tm.addTripleLiteralDateTime(timeURI, PingER_ONT.startTime, startDate);
			tm.addTripleLiteralDateTime(timeURI, PingER_ONT.endTime, endDate);
		} else if (tickParameter.equals("allmonthly")) {
			try {
				timeURI = P.BASE + "Time"+time;
				tm.addTripleResource(timeURI, Time.unitType, Time.unitMonth);
				String month = time.substring(0, 3);
				String year = time.substring(3, 7);
				tm.addTripleLiteralNonNegativeInteger(timeURI, Time.year, Integer.parseInt(year));
				tm.addTripleLiteralNonNegativeInteger(timeURI, Time.month,  MeasurementUtils.getMonthNumberByMonthInitials(month));
				String startDate = year + "-" + MeasurementUtils.getMonthNumberStringByMonthInitials(month) + "-01T00:00:00";
				String endDate = year + "-" + MeasurementUtils.getMonthNumberStringByMonthInitials(month) + "-"+MeasurementUtils.getLastDayOfMonth(month)+"T23:59:59";
				tm.addTripleLiteral(timeURI, PingER_ONT.displayValue, time);
				tm.addTripleLiteralDateTime(timeURI, PingER_ONT.startTime, startDate);
				tm.addTripleLiteralDateTime(timeURI, PingER_ONT.endTime, endDate);
			} catch (Exception e) {
				Logger.log(MeasurementUtils.class + "  " + tickParameter + " " + timeURI, e, "measurement_parameters/errors" );
			}
		} else if (tickParameter.contains("days")) {
			timeURI = P.BASE + "Time"+time;
			tm.addTripleResource(timeURI, Time.unitType, Time.unitDay);
			SimpleDateFormat df = new SimpleDateFormat("yyMMMdd", Locale.ENGLISH);
			Date date = null;
			try {
				date = df.parse(time);
			} catch (Exception e) {
				Logger.log(MeasurementUtils.class + " " + tickParameter + " " + timeURI, e, "measurement_parameters/errors" );
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int month = cal.get(Calendar.MONTH) + 1;
			int year = cal.get(Calendar.YEAR);
			int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			tm.addTripleLiteralNonNegativeInteger(timeURI, Time.year, year);
			tm.addTripleLiteralNonNegativeInteger(timeURI, Time.month, month);
			tm.addTripleLiteralNonNegativeInteger(timeURI, Time.day, day);
			tm.addTripleLiteralNonNegativeInteger(timeURI, Time.dayOfYear, dayOfYear);			
			tm.addTripleResource(timeURI, Time.dayOfWeek, Time.URI + MeasurementUtils.getDayOfWeek(dayOfWeek));
			tm.addTripleLiteral(timeURI, PingER_ONT.displayValue, time);
			String startDate = year + "-" + MeasurementUtils.getMonthNumberString(month) + "-"+MeasurementUtils.getDayStringByDayNumber(day)+"T00:00:00";
			String endDate = year + "-" + MeasurementUtils.getMonthNumberString(month) + "-"+MeasurementUtils.getDayStringByDayNumber(day)+"T23:59:59";
			tm.addTripleLiteralDateTime(timeURI, PingER_ONT.startTime, startDate);
			tm.addTripleLiteralDateTime(timeURI, PingER_ONT.endTime, endDate);
			
		} 
		tm.addTripleResource(timeURI, RDF.type, PingER_ONT.DateTime);
		return timeURI;
	}




}
