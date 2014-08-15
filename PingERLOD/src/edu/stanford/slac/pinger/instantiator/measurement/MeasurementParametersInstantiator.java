package edu.stanford.slac.pinger.instantiator.measurement;

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
import edu.stanford.slac.pinger.model.general.GeneralModelSingletonParallelized;
import edu.stanford.slac.pinger.model.general.GeneralModelSingletonParent;
import edu.stanford.slac.pinger.model.general.GeneralModelSingletonSequential;

public class MeasurementParametersInstantiator {

	public static void start(boolean timestamp) {
		instatiatePacketSize();
		instantiateUnits();
		instantiateSimpleMeasurement();
		if (timestamp)
			instantiateTimeStamp();
	}
	
	private static void instantiateUnits() {
		GeneralModelSingletonSequential gm = null;
		try {
			gm =  GeneralModelSingletonSequential.getInstance();
			HashMap<String,String> mapUnitsSymbols = MeasurementUtils.getMapUnitsSymbols();
			gm.begin();
			for (String key : mapUnitsSymbols.keySet()) {
				String unitURI = P.MAP_PREFIXES.get(P.MU) + key;
				gm.addTripleResource(unitURI, P.RDF, "type", P.MU, "Unit");	
				gm.addTripleLiteral(unitURI, P.MU, "hasSymbol", mapUnitsSymbols.get(key));
			}
			gm.commit();
		} catch (Exception e) {
			Logger.log(MeasurementUtils.class + ".instantiateUnits() " , e, "measurement_parameters/errors" );
		} finally {
			gm.close();
		}
	}
	
	private static void instantiateSimpleMeasurement() {
		GeneralModelSingletonSequential gm = null;
		try {
			gm =  GeneralModelSingletonSequential.getInstance();
			HashMap<String,MetricBean> mapMetricBean = MeasurementUtils.mapMetricBean;
			gm.begin();
			for (MetricBean mb : mapMetricBean.values()) {
				String simpleMeasurementURI = P.BASE + "SimpleMeasurement-"+mb.getInstantiationName();
				gm.addTripleResource(simpleMeasurementURI, P.RDF, "type", P.MD, "SimpleMeasurement");		
				gm.addTripleResource(simpleMeasurementURI, P.RDF, "type", P.MD, mb.getInstantiationName());				
				gm.addTripleResource(simpleMeasurementURI, P.MD, "defaultUnit", P.MU, mb.getDefaultUnit());
				gm.addTripleLiteral(simpleMeasurementURI, P.MD, "DisplayName", mb.getDisplayName());				
				gm.addTripleResource(simpleMeasurementURI, P.MD, "hasDefaultMeasurementParameters", P.BASE + "PacketSize100");
			}
			gm.commit();
		} catch (Exception e) {
			Logger.log(MeasurementUtils.class + ".instantiateSimpleMeasurement() " , e, "measurement_parameters/errors" );
		} finally {
			gm.close();
		}
	}
	private static void instatiatePacketSize() {
		GeneralModelSingletonSequential gm = null;
		try {		
			gm =  GeneralModelSingletonSequential.getInstance();
			gm.begin();
			for (String s : MeasurementUtils.packetSizes) {
				String packetSizeURI = P.BASE + "PacketSize"+s;
				gm.addTripleResource(packetSizeURI, P.RDF, "type", P.MD, "PacketSize");
				gm.addTripleLiteral(packetSizeURI, P.MD, "PacketSizeValue", Float.parseFloat(s));
				gm.addTripleResource(packetSizeURI, P.MD, "defaultUnit", P.MU, P.MAP_PREFIXES.get(P.MU) + "bit");
			}
			gm.commit();
		} catch (Exception e) {
			Logger.log(MeasurementUtils.class + ".instatiatePacketSize() ", e, "measurement_parameters/errors" );
		} finally {
			gm.close();
		}
	}	

	private static void instantiateTimeStamp() {
		ArrayList<String> years = MeasurementUtils.getYears();
		ArrayList<String> allmonthly = MeasurementUtils.generateMonthly();
		ArrayList<String> alldaily = MeasurementUtils.generateDaily();

		GeneralModelSingletonSequential gmSeq = null;
		GeneralModelSingletonParallelized gmPar = null;
		try {
			gmSeq =  GeneralModelSingletonSequential.getInstance();
			gmSeq.begin();
			Logger.log("Instantiating years...","measurement_parameters");
			for (String year : years) {
				timeStamp(year, "allyearly", gmSeq);
			}
			gmSeq.commit();
			gmSeq.begin();
			Logger.log("Instantiating allmonthly...","measurement_parameters");
			for (String monthly : allmonthly) {
				timeStamp(monthly, "allmonthly", gmSeq);
			}
			gmSeq.commit();
			gmSeq.close();

			gmPar = GeneralModelSingletonParallelized.getInstance();
			gmPar.start();
			Logger.log("Instantiating alldaily...","measurement_parameters");
			for (String daily : alldaily) {
				timeStamp(daily, "days", gmPar);
			}
			gmPar.saveNTriplesIntoRepository();

		} catch (Exception e) {
			Logger.log(MeasurementUtils.class + ".instantiateTimeStamp() ", e, "measurement_parameters/errors" );
		} finally {
			gmSeq.close();
			gmPar.close();
		}
	}

	public static String timeStamp(String time, String tickParameter, GeneralModelSingletonParent gm) {
		String timeURI = null;

		if (tickParameter.equals("allyearly")) {
			timeURI = P.BASE + "Time"+time;
			gm.addTripleResource(timeURI, P.TIME, "unitType", P.TIME, "unitYear");
			gm.addTripleLiteralNonNegativeInteger(timeURI, P.TIME, "year", Integer.parseInt(time));
			String year = time;
			String startDate = year + "-01-01T00:00:00";
			String endDate = year + "-12-31T23:59:59";
			gm.addTripleLiteral(timeURI, P.MGC, "displayValue", time);
			gm.addTripleLiteralDateTime(timeURI, P.MGC, "startDate", startDate);
			gm.addTripleLiteralDateTime(timeURI, P.MGC, "endDate", endDate);
		} else if (tickParameter.equals("allmonthly")) {
			try {
				timeURI = P.BASE + "Time"+time;
				gm.addTripleResource(timeURI, P.TIME, "unitType", P.TIME, "unitMonth");
				String month = time.substring(0, 3);
				String year = time.substring(3, 7);
				gm.addTripleLiteralNonNegativeInteger(timeURI, P.TIME, "month", MeasurementUtils.getMonthNumberByMonthInitials(month));
				gm.addTripleLiteralNonNegativeInteger(timeURI, P.TIME, "year", Integer.parseInt(year));
				
				String startDate = year + "-" + MeasurementUtils.getMonthNumberStringByMonthInitials(month) + "-01T00:00:00";
				String endDate = year + "-" + MeasurementUtils.getMonthNumberStringByMonthInitials(month) + "-"+MeasurementUtils.getLastDayOfMonth(month)+"T23:59:59";
				
				gm.addTripleLiteral(timeURI, P.MGC, "displayValue", time);
				gm.addTripleLiteralDateTime(timeURI, P.MGC, "startDate", startDate);
				gm.addTripleLiteralDateTime(timeURI, P.MGC, "endDate", endDate);
				
			} catch (Exception e) {
				Logger.log(MeasurementUtils.class + "  " + tickParameter + " " + timeURI, e, "measurement_parameters/errors" );
			}
		} else if (tickParameter.contains("days")) {
			timeURI = P.BASE + "Time"+time;
			gm.addTripleResource(timeURI, P.TIME, "unitType", P.TIME, "unitDay");
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
			gm.addTripleLiteralNonNegativeInteger(timeURI, P.TIME, "day", day);
			gm.addTripleLiteralNonNegativeInteger(timeURI, P.TIME, "month", month);
			gm.addTripleLiteralNonNegativeInteger(timeURI, P.TIME, "year", year);
			gm.addTripleLiteralNonNegativeInteger(timeURI, P.TIME, "dayOfYear", dayOfYear);
			gm.addTripleResource(timeURI, P.TIME, "dayOfWeek", P.TIME, MeasurementUtils.getDayOfWeek(dayOfWeek));
			
			gm.addTripleLiteral(timeURI, P.MGC, "displayValue", time);
			
			String initDate = year + "-" + MeasurementUtils.getMonthNumberString(month) + "-"+MeasurementUtils.getDayStringByDayNumber(day)+"T00:00:00";
			String endDate = year + "-" + MeasurementUtils.getMonthNumberString(month) + "-"+MeasurementUtils.getDayStringByDayNumber(day)+"T23:59:59";
			
			gm.addTripleLiteralDateTime(timeURI, P.MGC, "startDate", initDate);
			gm.addTripleLiteralDateTime(timeURI, P.MGC, "endDate", endDate);
			
			
		} else { //Hourly
			String params[] = tickParameter.replace("tick=hourly&", "").split("&");
			String day="",month="",year="";
			for (String s : params) {
				if (s.contains("day"))
					day = s.replace("day=", "");
				else if (s.contains("month"))
					month = s.replace("month=", "");
				else if (s.contains("year"))
					year = s.replace("year=", "");
			}
			timeURI = P.BASE + "Time"+time+"-"+year+MeasurementUtils.getInitialsByMonthNumber(month)+day;
			gm.addTripleResource(timeURI, P.TIME, "unitType", P.TIME, "unitHour");
			gm.addTripleLiteralNonNegativeInteger(timeURI, P.TIME, "day", Integer.parseInt(day));
			gm.addTripleLiteralNonNegativeInteger(timeURI, P.TIME, "month", MeasurementUtils.getMonthNumberByMonthInitials(month));
			gm.addTripleLiteralNonNegativeInteger(timeURI, P.TIME, "year", Integer.parseInt(year));
			gm.addTripleLiteralNonNegativeInteger(timeURI, P.TIME, "hour", Integer.parseInt(time));
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day));
			int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
			gm.addTripleLiteralNonNegativeInteger(timeURI, P.TIME, "dayOfYear", dayOfYear);
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			gm.addTripleResource(timeURI, P.TIME, "dayOfWeek", P.TIME, MeasurementUtils.getDayOfWeek(dayOfWeek));

		}
		gm.addTripleResource(timeURI, P.RDF, "type", P.MGC, "TimeStamp");
		return timeURI;
	}




}
