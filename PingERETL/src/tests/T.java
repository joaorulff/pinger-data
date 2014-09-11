package tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.beans.ContinentBean;
import edu.stanford.slac.pinger.general.utils.Utils;

public class T {
	public static void main(String[] args) throws ParseException, FileNotFoundException, IOException {
		File f = new File("C:/Users/Renan/Desktop/average_rtt-100-by-node-2011-10-26.txt.gz");
		InputStream is = new GZIPInputStream(new FileInputStream(f));
		
		StringWriter writer = new StringWriter();
		IOUtils.copy(is, writer);
		String theString = writer.toString();
		
		
		System.out.println(theString);
		
	}
	public static void main2(String[] args) throws ParseException {

		String content = Utils.readFile("C:/Users/Renan/Documents/Sample/Sample/1998_01_maximum_rtt_hourly_09.csv");
		String lines[] = content.split("\n");

		for (int i = 1; i < lines.length; i++) {
			String separetedValues[] = lines[i].split("\\s");

			String sourceNode = separetedValues[27];

			JsonObject sourceNodeDetails = Utils.getNodeDetails().get(sourceNode)
					.getAsJsonObject();
			JsonObject countriesJson = Utils.getCountriesJson();
			int sourceId = 0, sourceCountryId = 0, sourceContinentId = 0;
			String sourceCountryName = null;
			try {
				sourceId = Integer.parseInt(sourceNodeDetails.get("NodeID")
						.getAsString());
			} catch (Exception e) {}
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
			
			
			String destinationNode = separetedValues[30];
			JsonObject destinationNodeDetails = null; String destinationNodeName = null;
			int destinationId = 0, destinationCountryId = 0, destinationContinentId = 0;			
			try {
				destinationNodeDetails = Utils.getNodeDetails().get(destinationNode)
						.getAsJsonObject();
			} catch (Exception e) {}
			try {
				destinationId = Integer.parseInt(destinationNodeDetails.get(
						"NodeID").getAsString());
			} catch (Exception e) {}
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


			}

		}



	}



}
