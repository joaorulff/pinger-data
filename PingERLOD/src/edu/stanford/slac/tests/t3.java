package edu.stanford.slac.tests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.QueryString;

public class t3 {


	public static void main(String[] args) throws IOException {
		String path = "C:/pinger-export/t06122013.n3";
		
		final long LIMIT = 100*C.MB;
		
		
		long index = 1;
		
		String currentFilePath = "C:/pinger-export/parts/file"+index+".turtle";
		BufferedWriter bw = new BufferedWriter(new FileWriter(currentFilePath));
		
		
		
		
		
		
		try (BufferedReader br = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)) {
			long i = 0;
			String triplesBlock = "";
			Pattern p = Pattern.compile("^[\\w|<|:|@]");
		    for (String line = null; (line = br.readLine()) != null;) {
		    	i += line.length();
				Matcher m = p.matcher(line); 
				if (m.find()) {
		    		if (i > LIMIT) {
		    			System.out.println(index + " file");
		    			bw.close();
		    			i = 0;
		    			String cIndex = C.extractNumbersFromString(currentFilePath);
		    			currentFilePath = currentFilePath.replace(cIndex, ++index+"");
		    			System.out.println(currentFilePath);
		    			bw = new BufferedWriter(new FileWriter(currentFilePath));
		    			bw.write(header());
		    		}	    		
		    		bw.write(triplesBlock);
		    		triplesBlock = "";		    		
		    	}
		    	triplesBlock += line + "\n"; 	
		    }
		}
		bw.close();
		
		
		
		/*
		
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
			
			List<String> lst = IOUtils.readLines(fis, "UTF-8");
			
			System.out.println(lst.size());
			
			
			
		} catch (Exception e) {
			Logger.log("FILE NOT FOUND! -- Could not read the file " + path, e, "fileNotFound");
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
				Logger.log("readFile " + path + " ", e, "errors");
			}
		}
		*/
	}
	
	public static String header() {
		QueryString qs = new QueryString(
			"@prefix : <http://www-iepm.slac.stanford.edu/pinger/lod/resource#> . ",
			"@prefix dc: <http://purl.org/dc/elements/1.1/> . ",
			"@prefix MU: <http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentUnits.owl#> . ",
			"@prefix foaf: <http://xmlns.com/foaf/0.1/> . ",
			"@prefix dbp-rsrc: <http://dbpedia.org/resource/> . ",
			"@prefix dbp-prop: <http://dbpedia.org/property/> . ",
			"@prefix fb: <http://rdf.freebase.com/ns/> . ",
			"@prefix pos: <http://www.w3.org/2003/01/geo/wgs84_pos#> . ",
			"@prefix gn: <http://sws.geonames.org/> . ", 
			"@prefix dbp-owl: <http://dbpedia.org/ontology/> . ",
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> . ",
			"@prefix time: <http://www.w3.org/2006/time#> . ",
			"@prefix Units: <http://www-iepm.slac.stanford.edu/pinger/lod/ontology/Units.owl/#> . ", 
			"@prefix MGC: <http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentGeneralConcepts.owl#> . ",
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> . ",
			"@prefix owl: <http://www.w3.org/2002/07/owl#> . ",
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> . ",
			"@prefix MD: <http://www-iepm.slac.stanford.edu/pinger/lod/ontology/MomentDataV2.owl#> . ",
			"@prefix gn-ont: <http://www.geonames.org/ontology#> . ",
			""
		);
		return qs.toString();
	}

}
