package edu.stanford.slac.retriplifier.off;

import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;

import edu.stanford.slac.pinger.general.P;
import edu.stanford.slac.pinger.general.QueryString;
import edu.stanford.slac.pinger.general.vocabulary.GN_ONT;
import edu.stanford.slac.pinger.general.vocabulary.OWL;
import edu.stanford.slac.pinger.general.vocabulary.POS;
import edu.stanford.slac.pinger.general.vocabulary.PingER_ONT;
import edu.stanford.slac.pinger.general.vocabulary.RDF;
import edu.stanford.slac.retriplifier.TripleModelOff;

public class TownAndSchoolAndState {

	public static void towns() throws QueryEvaluationException {

		QueryString qs = new QueryString(
			"SELECT ?Town ?name ?DBPediaLink ?GeonamesLink ?population ?isInContinent ?isInCountry ",
			"?PingERLat ?PingERLong ?lat ?long ",
			"?NearestCityName ?NearestCityPop ?CountyName ?StateName ?CountryName ?ContinentName ?Offset ",
			"?FreebaseLink ?WikipediaLink ?nearestCityGeonamesLink ?nearestCityDBPediaLink ",
			"?parentCountry ?parentADM1 ?isInState",
			"",
			"WHERE {",
			"	?Town a MGC:Town .",
			"	?Town gn-ont:name ?name .",
			"	?Town MGC:DBPediaLink ?DBPediaLink .",
			"	?Town MGC:GeonamesLink ?GeonamesLink .",
			"	?Town gn-ont:population ?population .",
			"	?Town MGC:isInState ?isInState .",
			"	?Town MGC:isInCountry ?isInCountry .",
			"	?Town MGC:isInContinent ?isInContinent .",
			"	?Town MGC:PingERLat ?PingERLat .",
			"	?Town MGC:PingERLong ?PingERLong .",
			"	?Town pos:lat ?lat .",
			"	?Town pos:long ?long .",
			"	?Town MGC:GeoNearestCity ?NearestCityName .",
			"	?Town MGC:GeoNearestCityPopulation ?NearestCityPop .",
			"	?Town MGC:GeoCounty ?CountyName .",
			"	?Town MGC:GeoState ?StateName .",
			"	?Town MGC:GeoCountry ?CountryName .",
			"	?Town MGC:GeoContinent ?ContinentName .",
			"	?Town MGC:GeoGMTOffset ?Offset .",
			"	?Town MGC:FreebaseLink ?FreebaseLink .",
			"	?Town gn-ont:wikipediaArticle ?WikipediaLink .",
			"	?Town MGC:nearestCityGeonames ?nearestCityGeonamesLink .",
			"	?Town MGC:nearestCityDBPedia ?nearestCityDBPediaLink .",
			"	?Town gn-ont:parentCountry ?parentCountry .",
			"	?Town gn-ont:parentADM1 ?parentADM1 .",			
			"}"
		);
		qs.addPrefix(P.PREFIXES);
		System.out.println(qs.toString());
		
		TripleModelOff tm = TripleModelOff.getInstance();
		
		TupleQueryResult result = SPARQLQuery.query(qs.toString());
		
		while (result.hasNext()) {
			BindingSet bindingSet  = result.next();
			String Town = bindingSet.getValue("Town").stringValue();
			String name = bindingSet.getValue("name").stringValue();
			String DBPediaLink = bindingSet.getValue("DBPediaLink").stringValue();
			String GeonamesLink = bindingSet.getValue("GeonamesLink").stringValue();
			int population = Integer.parseInt(bindingSet.getValue("population").stringValue());
			String isInState = bindingSet.getValue("isInState").stringValue();
			String isInCountry = bindingSet.getValue("isInCountry").stringValue();
			String isInContinent = bindingSet.getValue("isInContinent").stringValue();
			double PingERLat = Double.parseDouble(bindingSet.getValue("PingERLat").stringValue());
			double PingERLong = Double.parseDouble(bindingSet.getValue("PingERLong").stringValue());
			double lat = Double.parseDouble(bindingSet.getValue("lat").stringValue());
			double Long = Double.parseDouble(bindingSet.getValue("long").stringValue());
			String NearestCityName = bindingSet.getValue("NearestCityName").stringValue();
			String NearestCityPop = bindingSet.getValue("NearestCityPop").stringValue();
			String CountyName = bindingSet.getValue("CountyName").stringValue();
			String StateName = bindingSet.getValue("StateName").stringValue();
			String CountryName = bindingSet.getValue("CountryName").stringValue();
			String ContinentName = bindingSet.getValue("ContinentName").stringValue();
			String Offset = bindingSet.getValue("Offset").stringValue();
			String FreebaseLink = bindingSet.getValue("FreebaseLink").stringValue();
			String WikipediaLink = bindingSet.getValue("WikipediaLink").stringValue();
			String nearestCityGeonamesLink = bindingSet.getValue("nearestCityGeonamesLink").stringValue();
			String nearestCityDBPediaLink = bindingSet.getValue("nearestCityDBPediaLink").stringValue();

			tm.addTripleResource(Town, RDF.type, PingER_ONT.Town);
			tm.addTripleLiteral(Town, GN_ONT.name, name);
			tm.addTripleResource(Town, PingER_ONT.WikipediaLink, WikipediaLink);
			tm.addTripleResource(Town, PingER_ONT.DBPediaLink, DBPediaLink);
			tm.addTripleResource(Town, PingER_ONT.GeonamesLink, GeonamesLink);
			tm.addTripleResource(Town, PingER_ONT.FreebaseLink, FreebaseLink);
			tm.addTripleResource(Town, OWL.sameAs, DBPediaLink);
			tm.addTripleResource(Town, OWL.sameAs, GeonamesLink);
			tm.addTripleResource(Town, OWL.sameAs, FreebaseLink);
			tm.addTripleLiteral(Town, GN_ONT.population, population);
			tm.addTripleResource(Town, GN_ONT.parentADM1, isInState);
			tm.addTripleResource(Town, GN_ONT.parentCountry, isInCountry);
			tm.addTripleResource(Town, PingER_ONT.parentContinent, isInContinent);
			tm.addTripleLiteral(Town, PingER_ONT.PingERLat, PingERLat);
			tm.addTripleLiteral(Town, PingER_ONT.PingERLong, PingERLong);
			tm.addTripleLiteral(Town, POS.lat, lat);
			tm.addTripleLiteral(Town, POS.Long, Long);
			tm.addTripleLiteral(Town, PingER_ONT.nearestCityName, NearestCityName);
			tm.addTripleLiteral(Town, PingER_ONT.nearestCityPopulation, NearestCityPop);
			tm.addTripleLiteral(Town, PingER_ONT.countyName, CountyName);
			tm.addTripleLiteral(Town, PingER_ONT.stateName, StateName);
			tm.addTripleLiteral(Town, PingER_ONT.countryName, CountryName);
			tm.addTripleLiteral(Town, PingER_ONT.continentName, ContinentName);
			tm.addTripleLiteral(Town, PingER_ONT.hasGMTOffset, Offset);
			tm.addTripleResource(Town, PingER_ONT.nearestCityDBPediaLink, nearestCityDBPediaLink);
			tm.addTripleResource(Town, PingER_ONT.nearestCityGeonamesLink, nearestCityGeonamesLink);
			
			tm.writeTriplesAndClean();
		}
		SPARQLQuery.closeConnection();
		
	}

	
	public static void schools() throws QueryEvaluationException {

		QueryString qs = new QueryString(
			"SELECT ?School ?schoolName ?DBPediaLink ?FreebaseLink ?WikipediaLink ?isInContinent ?isInCountry ?isInState ",
			"?PingERLat ?PingERLong ?lat ?long ",
			"?schoolType ?numberOfStudents ?numberOfUgradStudents ?numberOfGradStudents ?endowment ?facultySize ?PingERName ",
			"?Town",
			"",
			"WHERE {",
			"	?School a MGC:School .",
			"	?School MGC:SchoolName ?schoolName .",
			"	?School MGC:DBPediaLink ?DBPediaLink .",
			"	?School MGC:FreebaseLink ?FreebaseLink .",
			"	?School MGC:WikipediaLInk ?WikipediaLink .",
			"	?School MGC:PingERLat ?PingERLat .",
			"	?School MGC:PingERLong ?PingERLong .",
			"	?School pos:lat ?lat .",
			"	?School pos:long ?long .",
			"	OPTIONAL { ?School MGC:SchoolNumberOfStudents ?numberOfStudents . }",
			"	OPTIONAL { ?School MGC:SchoolNumberOfUgradStudents ?numberOfUgradStudents . }",
			"	OPTIONAL { ?School MGC:SchoolNumberOfGradStudents ?numberOfGradStudents . }",
			"	?School MGC:SchoolPingerName ?PingERName .",
			"	OPTIONAL { ?School MGC:SchoolType ?schoolType . } ",
			"	OPTIONAL { ?School MGC:SchoolFacultySize ?facultySize . } ",
			"	OPTIONAL { ?School MGC:SchoolEndowment ?endowment . }",
			"	?School MGC:isInTown ?Town .",
			"	?Town MGC:isInState ?isInState .",
			"	?Town MGC:isInCountry ?isInCountry .",
			"	?Town MGC:isInContinent ?isInContinent .",
			"}"
		);
		qs.addPrefix(P.PREFIXES);
		System.out.println(qs.toString());
		
		TripleModelOff tm = TripleModelOff.getInstance();
		TupleQueryResult result = SPARQLQuery.query(qs.toString());
		while (result.hasNext()) {
			BindingSet bindingSet  = result.next();
			String School = bindingSet.getValue("School").stringValue();
			String schoolName = bindingSet.getValue("schoolName").stringValue();
			String DBPediaLink = bindingSet.getValue("DBPediaLink").stringValue();
			String FreebaseLink = bindingSet.getValue("FreebaseLink").stringValue();
			String WikipediaLink = bindingSet.getValue("WikipediaLink").stringValue();
			double PingERLat = Double.parseDouble(bindingSet.getValue("PingERLat").stringValue());
			double PingERLong = Double.parseDouble(bindingSet.getValue("PingERLong").stringValue());
			double lat = Double.parseDouble(bindingSet.getValue("lat").stringValue());
			double Long = Double.parseDouble(bindingSet.getValue("long").stringValue());
			
			tm.addTripleResource(School, RDF.type, PingER_ONT.School);
			
			Value val;
			val = bindingSet.getValue("numberOfStudents");
			if (val != null) {
				int numberOfStudents = Integer.parseInt(val.stringValue());
				tm.addTripleLiteral(School, PingER_ONT.numberOfStudents, numberOfStudents);
			}
			val = bindingSet.getValue("numberOfUgradStudents");
			if (val != null) {
				int numberOfUgradStudents = Integer.parseInt(val.stringValue());
				tm.addTripleLiteral(School, PingER_ONT.numberOfUgradStudents, numberOfUgradStudents);
			}
			val = bindingSet.getValue("numberOfGradStudents");
			if (val != null) {
				int numberOfGradStudents = Integer.parseInt(val.stringValue());
				tm.addTripleLiteral(School, PingER_ONT.numberOfGradStudents, numberOfGradStudents);
			}
			val = bindingSet.getValue("endowment");
			if (val != null) {
				float endowment = Float.parseFloat(val.stringValue());
				tm.addTripleLiteral(School, PingER_ONT.endowment, endowment);
			}
			val = bindingSet.getValue("facultySize");
			if (val != null) {
				int facultySize = Integer.parseInt(val.stringValue());
				tm.addTripleLiteral(School, PingER_ONT.facultySize, facultySize);
			}
			String PingERName = bindingSet.getValue("PingERName").stringValue();
			String Town = bindingSet.getValue("Town").stringValue();
			String isInContinent = bindingSet.getValue("isInContinent").stringValue();
			String isInCountry = bindingSet.getValue("isInCountry").stringValue();
			String isInState = bindingSet.getValue("isInState").stringValue();

			tm.addTripleLiteral(School, PingER_ONT.schoolName, schoolName);
			tm.addTripleResource(School, PingER_ONT.WikipediaLink, WikipediaLink);
			tm.addTripleResource(School, PingER_ONT.DBPediaLink, DBPediaLink);
			tm.addTripleResource(School, PingER_ONT.FreebaseLink, FreebaseLink);
			tm.addTripleResource(School, OWL.sameAs, DBPediaLink);
			tm.addTripleResource(School, OWL.sameAs, FreebaseLink);
			tm.addTripleLiteral(School, PingER_ONT.PingERLat, PingERLat);
			tm.addTripleLiteral(School, PingER_ONT.PingERLong, PingERLong);

			tm.addTripleResource(School, PingER_ONT.parentTown, Town);
			tm.addTripleResource(School, GN_ONT.parentADM1, isInState);
			tm.addTripleResource(School, GN_ONT.parentCountry, isInCountry);
			tm.addTripleResource(School, PingER_ONT.parentContinent, isInContinent);
			
			tm.addTripleLiteral(School, POS.lat, lat);
			tm.addTripleLiteral(School, POS.Long, Long);

			tm.addTripleLiteral(School, PingER_ONT.PingERName, PingERName);
			tm.writeTriplesAndClean();
		}
		SPARQLQuery.closeConnection();
	}
	
	public static void states() throws QueryEvaluationException {

		QueryString qs = new QueryString(
			"SELECT ?State ?name ?DBPediaLink ?GeonamesLink ?population ?isInContinent ?isInCountry ",
			"?FreebaseLink ?WikipediaLink ",
			"",
			"WHERE {",
			"	?State a MGC:State .",
			"	?State gn-ont:name ?name .",
			"	?State MGC:DBPediaLink ?DBPediaLink .",
			"	?State MGC:GeonamesLink ?GeonamesLink .",
			"	?State gn-ont:wikipediaArticle ?WikipediaLink .",
			"	?State gn-ont:population ?population .",
			"	?State MGC:isInCountry ?isInCountry .",
			"	?isInCountry MGC:isInContinent ?isInContinent .",
			"}"
		);
		qs.addPrefix(P.PREFIXES);
		System.out.println(qs.toString());
		
		TripleModelOff tm = TripleModelOff.getInstance();
		
		TupleQueryResult result = SPARQLQuery.query(qs.toString());
		
		while (result.hasNext()) {
			BindingSet bindingSet  = result.next();
			String State = bindingSet.getValue("State").stringValue();
			String name = bindingSet.getValue("name").stringValue();
			String DBPediaLink = bindingSet.getValue("DBPediaLink").stringValue();
			String GeonamesLink = bindingSet.getValue("GeonamesLink").stringValue();
			String WikipediaLink = bindingSet.getValue("WikipediaLink").stringValue();
			int population = Integer.parseInt(bindingSet.getValue("population").stringValue());
			String isInCountry = bindingSet.getValue("isInCountry").stringValue();
			String isInContinent = bindingSet.getValue("isInContinent").stringValue();

			tm.addTripleResource(State, RDF.type, PingER_ONT.State);
			tm.addTripleLiteral(State, GN_ONT.name, name);
			tm.addTripleResource(State, PingER_ONT.WikipediaLink, WikipediaLink);
			tm.addTripleResource(State, PingER_ONT.DBPediaLink, DBPediaLink);
			tm.addTripleResource(State, PingER_ONT.GeonamesLink, GeonamesLink);
			tm.addTripleResource(State, OWL.sameAs, DBPediaLink);
			tm.addTripleResource(State, OWL.sameAs, GeonamesLink);
			tm.addTripleLiteral(State, GN_ONT.population, population);
			tm.addTripleResource(State, GN_ONT.parentCountry, isInCountry);
			tm.addTripleResource(State, PingER_ONT.parentContinent, isInContinent);
			tm.writeTriplesAndClean();
		}
		SPARQLQuery.closeConnection();
		
	}

	
}
