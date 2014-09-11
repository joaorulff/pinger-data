package edu.stanford.slac.pinger.etl.transformer;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.beans.ContinentBean;
import edu.stanford.slac.pinger.beans.MetricBean;
import edu.stanford.slac.pinger.beans.PingMeasurementBean;
import edu.stanford.slac.pinger.etl.loader.local.FileHandlerHourly;
import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.ErrorCode;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.utils.MeasurementUtils;
import edu.stanford.slac.pinger.general.utils.Utils;

public class PingHourlyMeasurementCSVBuilder {

	private FileHandlerHourly outputFileHandler;
	private String month, metric, tickParameter;
	private String year, inputDir;

	private JsonObject hours = Utils.getJsonAsObject(C.HOURS_JSON);

	public PingHourlyMeasurementCSVBuilder(FileHandlerHourly outputFileHandler, String inputDir, String year, String month, String metric, String tickParameter) {
		this.inputDir = inputDir;
		this.outputFileHandler = outputFileHandler;
		this.month = month;
		this.year = year;
		this.metric = metric;
		this.tickParameter = tickParameter;
	}

	public void run() {
		ArrayList<String> daysOfaMonth = Utils.getDaysOfAMonth(year, month);
		for (String day : daysOfaMonth) {
			instantiateDay(day);
		}
	}

	public void instantiateDay(String day) {

		//Here we may change
		String filePath = inputDir + Utils.getFileNameBeginningHourly(tickParameter, metric, year, month) + "_" + day + ".csv";

		String content = Utils.readFile(filePath);
		String lines[] = content.split("\n");

		for (int i = 1; i < lines.length; i++) {
			String separetedValues[] = lines[i].split("\\s");
			if (separetedValues.length==0) {
				continue;
			}
			
			String sourceNode = null;
			try {
				sourceNode = separetedValues[27]; 
			} catch (Exception e) {
				Logger.log("Could not retrieve Source node from " + filePath, e);
				continue;
			}
			JsonObject sourceNodeDetails = null;
			try {
				sourceNodeDetails = Utils.getNodeDetails().get(sourceNode)
						.getAsJsonObject();
			} catch (Exception e) {continue;}		
			JsonObject countriesJson = Utils.getCountriesJson();
			int sourceId = 0, sourceCountryId = 0, sourceContinentId = 0;
			String sourceCountryName = null;
			try {
				sourceId = Integer.parseInt(sourceNodeDetails.get("NodeID")
						.getAsString());
			} catch (Exception e) {continue;}
			try {
				sourceCountryName = sourceNodeDetails.get("Country").getAsString();
				sourceCountryId = countriesJson.get(sourceCountryName).getAsJsonObject()
						.get("isoNumeric").getAsInt();
			} catch (Exception e) {}
			try {
				String continentSourceInitials = countriesJson.get(sourceCountryName)
						.getAsJsonObject().get("continent").getAsString();
				sourceContinentId = Integer.parseInt(ContinentBean.MAP.get(
						continentSourceInitials).getId());
			} catch (Exception e) {}

			String destinationNode = null;
			try {
				destinationNode = separetedValues[30];
			} catch (Exception e) {
				Logger.log("Could not retrieve Destination Node node from " + filePath, e);
				continue;
			}
			
			JsonObject destinationNodeDetails = null; String destinationNodeName = null;
			int destinationId = 0, destinationCountryId = 0, destinationContinentId = 0;			
			try {
				destinationNodeDetails = Utils.getNodeDetails().get(destinationNode)
						.getAsJsonObject();
			} catch (Exception e) {continue;}
			try {
				destinationId = Integer.parseInt(destinationNodeDetails.get(
						"NodeID").getAsString());
			} catch (Exception e) {continue;}
			try {
				destinationNodeName = destinationNodeDetails.get("Country")
						.getAsString();
			} catch (Exception e) {}
			try {
				destinationCountryId = countriesJson.get(destinationNodeName)
						.getAsJsonObject().get("isoNumeric").getAsInt();
			} catch (Exception e) {}

			try {
				String destinationContinentInitials = countriesJson.get(destinationNodeName)
						.getAsJsonObject().get("continent").getAsString();
				destinationContinentId = Integer.parseInt(ContinentBean.MAP
						.get(destinationContinentInitials).getId());
			} catch (Exception e) {}	

			int startValueIndex = 3;
			int endValueIndex = 26;

			int hour = 0;
			for (int j = startValueIndex; j <= endValueIndex; j++) {

				if (separetedValues[j] != null && !separetedValues.equals(".")) {
					instantiate(separetedValues[j], sourceId, sourceCountryId,
							sourceContinentId, destinationId, destinationCountryId,
							destinationContinentId, tickParameter, day, hour);
				}
				hour++;

			}
		}



	}

	/**
	 * @param metric
	 * @param metricURI
	 * @param packetSize
	 * @param tickParameter
	 */
	private void instantiate(String value, int sourceId,
			int sourceCountryId, int sourceContinentId, int destinationId,
			int destinationCountryId, int destinationContinentId,
			String tickParameter, String day, int hour) {
		HashMap<String, MetricBean> mapMetricBean = null;
		try {
			mapMetricBean = MeasurementUtils.mapMetricBean;
		} catch (Exception e) {
			Logger.error("Error when getting map.", ErrorCode.MAP_ERROR);
			System.exit(-1);
		}
		MetricBean metricBean = null;
		try {
			metricBean = mapMetricBean.get(metric);
		} catch (Exception e) {
			Logger.error("Error when getting Metric Bean.", ErrorCode.MAP_ERROR);
			System.exit(-1);
		}

		String hourStr = (hour<10)?"0"+hour:hour+"";
		String timeLabel =  year + "-" + month + "-" +  day + "-" + hourStr;
		PingMeasurementBean pmb = new PingMeasurementBean();
		pmb.setMetricId(metricBean.getId());
		pmb.setSourceNodeId(sourceId);
		pmb.setSourceCountryId(sourceCountryId);
		pmb.setSourceContinentId(sourceContinentId);
		pmb.setDestinationNodeId(destinationId);
		pmb.setDestinationCountryId(destinationCountryId);
		pmb.setDestinationContinentId(destinationContinentId);
		long timeFromHoursJson = 0;
		try {
			timeFromHoursJson = Long.parseLong(hours.get(timeLabel).getAsString());
		} catch (Exception e) {
			Logger.error(timeFromHoursJson + " is not a valid time.", ErrorCode.TIME_FORMAT);
			return;
		}

		pmb.setTimeId(timeFromHoursJson);

		pmb.setValue(value);

		outputFileHandler.addRow(pmb.toString());
	}

}
