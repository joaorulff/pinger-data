package edu.stanford.slac.pinger.instantiator.physicallocation;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.openrdf.model.Model;
import org.openrdf.model.Value;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.bean.TownBean;
import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.P;
import edu.stanford.slac.pinger.general.vocabulary.GN_ONT;
import edu.stanford.slac.pinger.general.vocabulary.RDFS;
import edu.stanford.slac.pinger.instantiator.nodes.NodesInstantiator;
import edu.stanford.slac.pinger.model.general.GeneralModelSingletonSequential;
import edu.stanford.slac.pinger.rest.query.RDFReaderOpenRDF;
import edu.stanford.slac.pinger.rest.query.town.TownModelGeonamesQuery;
import edu.stanford.slac.pinger.rest.query.town.TownModelSparql;

public class TownInstantiator {


	public static void start() {

		JsonObject json = C.getNodeDetails();
		if (json == null) {
			Logger.error(NodesInstantiator.class + " Json NodeDetails is Null. You need to run the job generateNodeDetails first. If the problem remains, try analyzing the NodeDetails json file.");
			return;
		}


		GeneralModelSingletonSequential gm = null; 
		try {
			gm = GeneralModelSingletonSequential.getInstance();
			int attempt = 0;	
			for (Entry<String,JsonElement> entry : json.entrySet()) {

				if (!C.continue_town) break; //this is set in: HttpGetter.getJsonArrayGeonames

				String key = entry.getKey();

				JsonObject j = json.get(key).getAsJsonObject();			

				double lat = Double.parseDouble(j.get("latitude").toString().replace("\"", ""));
				double lng = Double.parseDouble(j.get("longitude").toString().replace("\"", ""));

				try {

					Logger.log(j.get("SourceFullName") + "--- [lat,lng]= "+lat +", "+lng );
					TownBean mainTown = townInstantiator(lat,lng);
					Logger.log(mainTown);
					if (mainTown==null) {
						Logger.log(TownInstantiator.class + " key: " + key + "  Could not instantiate Town for [lat,lng]= "+lat +", "+lng, "towns/errors");
						continue;
					}
					gm.begin();
					String townURI = P.BASE+"Town"+mainTown.getGeonamesId();

					//Datatype Properties
					gm.addTripleLiteral(townURI, P.MGC, "PingERLat", lat);
					gm.addTripleLiteral(townURI, P.MGC, "PingERLong", lng);
					gm.addTripleLiteral(townURI, P.POS, "lat", mainTown.getLatitude());
					gm.addTripleLiteral(townURI, P.POS, "long", mainTown.getLongitude());
					gm.addTripleLiteral(townURI, P.GN_ONT, "population", mainTown.getPopulation());

					if (mainTown.getName() != null)
						gm.addTripleLiteral(townURI, P.GN_ONT, "name", mainTown.getName());


					if (mainTown.getPostalCodes()!=null) {
						for (String postalCode : mainTown.getPostalCodes()) 
							gm.addTripleLiteral(townURI, P.GN_ONT, "postalCode", postalCode);
					}

					//MGC Datatype Properties
					if (mainTown.getNearestCity() != null) {
						gm.addTripleLiteral(townURI, P.MGC, "GeoNearestCity", mainTown.getNearestCity().getName());
						gm.addTripleLiteral(townURI, P.MGC, "GeoNearestCityPopulation", mainTown.getNearestCity().getPopulation());
					}
					if (mainTown.getCounty()!=null)
						gm.addTripleLiteral(townURI, P.MGC, "GeoCounty", mainTown.getCounty());
					if (mainTown.getStateName()!=null)
						gm.addTripleLiteral(townURI, P.MGC, "GeoState", mainTown.getStateName());
					if (mainTown.getCountryName()!=null)
						gm.addTripleLiteral(townURI, P.MGC, "GeoCountry", mainTown.getCountryName());
					if (mainTown.getContinentName()!=null)
						gm.addTripleLiteral(townURI, P.MGC, "GeoContinent",  mainTown.getContinentName());
					gm.addTripleLiteral(townURI, P.MGC, "GeoGMTOffset", mainTown.getGmtOffset());

					//Object Properties
					gm.addTripleResource(townURI, P.RDF, "type", P.MGC, "Town");		

					if (mainTown.getGeonamesLink()!=null)
						gm.addTripleResource(townURI, P.MGC, "GeonamesLink", mainTown.getGeonamesLink());
					if (mainTown.getDbpediaLink()!=null)
						gm.addTripleResource(townURI, P.MGC, "DBPediaLink", mainTown.getDbpediaLink());
					if (mainTown.getFreebaseLink()!=null)
						gm.addTripleResource(townURI, P.MGC, "FreebaseLink", mainTown.getFreebaseLink());
					if (mainTown.getWikiPediaLink()!=null)
						gm.addTripleResource(townURI, P.GN_ONT, "wikipediaArticle", mainTown.getWikiPediaLink());
					if (mainTown.getCountryGeoId()!=null) {
						gm.addTripleResource(townURI, P.GN_ONT, "parentCountry", P.GN, mainTown.getCountryGeoId());
						gm.addTripleResource(townURI, P.MGC, "isInCountry", P.BASE+"Country"+mainTown.getCountryGeoId().replace("http://sws.geonames.org/",""));
					}
					//Instantiating a State
					if (mainTown.getStateGeoId()!=null) {
						gm.addTripleResource(townURI, P.GN_ONT, "parentADM1", P.GN, mainTown.getStateGeoId());

						String geoStateId = mainTown.getStateGeoId().replace("http://sws.geonames.org/","");
						String stateURI = P.BASE + "State"+geoStateId;
						gm.addTripleResource(stateURI, P.RDF, "type", P.MGC, "State");

						gm.addTripleResource(stateURI, P.MGC, "GeonamesLink",  P.GN, mainTown.getStateGeoId());

						String url_rdf = "http://sws.geonames.org/"+geoStateId+"/about.rdf";
						RDFReaderOpenRDF rdfReader = new RDFReaderOpenRDF(url_rdf);

						String subj = "http://sws.geonames.org/"+geoStateId+"/";

						Model model = rdfReader.filterWeb(subj, RDFS.seeAlso, null);
						Set<Value> objs = model.objects();
						Iterator<Value> it = objs.iterator();
						while(it.hasNext()) {
							Value v = (Value) it.next();
							gm.addTripleResource(stateURI, P.MGC, "DBPediaLink", v.stringValue());
						}


						model = rdfReader.filterWeb(subj, GN_ONT.wikipediaArticle, null);
						objs = model.objects();
						it = objs.iterator();
						while(it.hasNext()) {
							Value v = (Value) it.next();
							gm.addTripleResource(stateURI, GN_ONT.wikipediaArticle, v.stringValue());
						}

						model = rdfReader.filterWeb(subj, GN_ONT.name, null);
						objs = model.objects();
						it = objs.iterator();
						while(it.hasNext()) {
							Value v = (Value) it.next();
							gm.addTripleLiteral(stateURI, GN_ONT.PREFIX, "name", v.stringValue());
						}	

						model = rdfReader.filterWeb(subj, GN_ONT.population, null);
						objs = model.objects();
						it = objs.iterator();
						while(it.hasNext()) {
							Value v = (Value) it.next();
							gm.addTripleLiteral(stateURI, GN_ONT.PREFIX, "population", Integer.parseInt(v.stringValue()));
						}		

						model = rdfReader.filterWeb(subj, GN_ONT.parentCountry, null);
						objs = model.objects();
						it = objs.iterator();
						while(it.hasNext()) {
							Value v = (Value) it.next();
							String geoCountry = v.stringValue().replace("http://sws.geonames.org/", "").replace("/", "");
							gm.addTripleResource(stateURI, P.MGC, "isInCountry", P.BASE+"Country"+geoCountry);
						}	

						gm.addTripleResource(townURI, P.MGC, "isInState", stateURI);

					}
					//MGC Object Properties
					if (mainTown.getNearestCity() != null) {
						if (mainTown.getNearestCity().getGeonamesLink()!=null)
							gm.addTripleResource(townURI, P.MGC, "nearestCityGeonames", mainTown.getNearestCity().getGeonamesLink());
						if (mainTown.getNearestCity().getDbpediaLink()!=null)
							gm.addTripleResource(townURI, P.MGC, "nearestCityDBPedia", mainTown.getNearestCity().getDbpediaLink());
						if (mainTown.getNearestCity().getFreebaseLink()!=null)
							gm.addTripleResource(townURI, P.MGC, "nearestCityFreebase", mainTown.getNearestCity().getFreebaseLink());
					}
					if (mainTown.getContinentGeoId()!=null) {
						gm.addTripleResource(townURI, P.MGC, "isInContinent", P.BASE+"Continent"+mainTown.getContinentGeoId().replace("http://sws.geonames.org/",""));				
					}

					gm.commit();

				} catch (Exception e) {
					Logger.log(TownInstantiator.class + " Could not instantiate town for Key: " + key + " [lat,lng]= "+lat +", "+lng, "towns");
					attempt++;
					if (attempt > C.MAX_ATTEMPT_INSTANTIATOR) {
						Logger.error(TownInstantiator.class + " Maximum attempts reached... ", e, "towns/errors");
					}
					continue;
				}

			}
		}catch (Exception e) {
			Logger.log("TownInstantiator " , e, "errors");
		} finally {
			gm.close();
		}
	}
	/**
	 * Routine to fill the fields of a TownBean
	 * @param lat
	 * @param lng
	 * @return
	 */
	public static TownBean townInstantiator(double lat, double lng) {
		Logger.log("Getting city1000", "towns");
		TownBean mainTown = null;
		try {
			mainTown = TownModelGeonamesQuery.getCity1000(lat,lng);	
		} catch (Exception e) {
			mainTown = null;
			Logger.log("Could not instantiate Town with 1000 people for [lat,lng]=Error", e, "towns/errors");
		}
		if (mainTown==null) {
			try {
				mainTown = TownModelGeonamesQuery.getCity15000(lat, lng);
				if (mainTown==null) return null;
			} catch (Exception e) {
				Logger.error("Could not instantiate Town for [lat,lng]= "+lat +", "+lng, "towns/errors");
				return null;
			} 
		}
		try {
			Logger.log("Getting nearest city", "towns");
			mainTown.setNearestCity(TownModelGeonamesQuery.getCity15000(lat, lng));
		} catch (Exception e) {
			mainTown.setNearestCity(null);
			Logger.log("There is no city with 15000 people for [lat,lng]= "+lat +", "+lng, "towns/errors");
		}
		

		mainTown.setPingerLat(lat);
		mainTown.setPingerLong(lng);

		//Try to set up DbpediaId, FreebaseId
		Logger.log("Accessing dbpedia to get DBPedia and Freebase resources", "towns");
		TownModelSparql.getDBPediaAndFreebaseResourcesFromGeonamesId(mainTown);

		//If we don't have the FreebaseID, but we got the DBPediaID from either one of the functions above (both of them try to set DBPedia ID)
		//Try to set up FreebaseID from DBPedia
		if (mainTown.getFreebaseLink()==null && mainTown.getDbpediaLink() != null) {
			Logger.log("Freebase not set yet... Trying to get it from DBPedia...", "towns");
			TownModelSparql.getFreebaseResourceFromDBPedia(mainTown);
		}
		//If after these queries, we still do not have DBPedia link or Population=0, it is likely that the town is very small.
		//Then the town becomes the nearest city.
		if ((mainTown.getDbpediaLink() == null || mainTown.getPopulation() == 0) && mainTown.getNearestCity() != null) {
			Logger.log("Town --- [lat,lng]= "+lat +", "+lng + " not found in DBPedia or its population is 0... The town becomes the nearest city and another attempt to get the DBPedia ID", "towns");
			mainTown = mainTown.getNearestCity();
			mainTown.setNearestCity(mainTown);
			TownModelSparql.getDBPediaAndFreebaseResourcesFromGeonamesId(mainTown);
			if (mainTown.getFreebaseLink()==null && mainTown.getDbpediaLink() != null) {
				Logger.log("Another attempt to get Freebase id...", "towns");
				TownModelSparql.getFreebaseResourceFromDBPedia(mainTown);
			}	
		}
		
		/*
		 If at this point maintown exists and it  has no NearestCity, there is something strange with Geonames API.
		 Example of when this happens:
		 http://api.geonames.org/findNearbyPlaceNameJSON?&style=FULL&username=pinger&lat=42.258&lng=-77.783&cities=cities1000
		 For this town (Alfred, NY), Geonames can retrieve the nearest town but it cannot retrieve the nearest city, which is very strange.
		 Anyhow, to solve this, the nearest city just becomes the maintown itself.		 
		*/ 
		if (mainTown.getNearestCity() == null) {
			mainTown.setNearestCity(mainTown);
		}

		//Try to set up StateDBPediaId, countryDBPediaID, continentName, continentGeoId continentDBPediaId
		TownModelSparql.getStateCountryContinentFromGeonamesId(mainTown);
		if (mainTown.getStateDBPediaId()==null)
			TownModelSparql.getCountryContinentFromGeonamesId(mainTown);

		return mainTown;
	}




}


