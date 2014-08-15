package edu.stanford.slac.pinger.instantiator.physicallocation;

import java.util.Iterator;
import java.util.Set;

import org.openrdf.model.Model;
import org.openrdf.model.Value;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.stanford.slac.pinger.bean.ContinentBean;
import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.P;
import edu.stanford.slac.pinger.general.vocabulary.RDFS;
import edu.stanford.slac.pinger.model.general.GeneralModelSingletonSequential;
import edu.stanford.slac.pinger.rest.pingtable.HttpGetter;
import edu.stanford.slac.pinger.rest.query.RDFReaderOpenRDF;

public class CountryInstantiator {

	/**
	 * 
	 * @param file Set to true to work with the Json stored file.  Otherwise, it will generate a Json file from Geonames. Using "file" is recommended since the list of countries does not change often, unless you want to update the file.
	 * @param setdbpedia Set to true to access geonames rdfs to get Dbpedia
	 */
	public static void start(boolean file, boolean setdbpedia) {
		JsonArray jArr = null; 
		if (file) {
			jArr = C.getJsonAsArray(C.COUNTRIES_JSON);
		} else {
			String url = "http://api.geonames.org/countryInfoJSON?username="+C.GEONAMES_USERNAME[0];
			jArr = HttpGetter.getJsonArrayGeonames(url);
			if (jArr != null){
				C.writeIntoFile(jArr.toString(), C.COUNTRIES_JSON);
			}
		}
		if (jArr==null) {
			Logger.error("Could not instantiate countries");
			return;
		} 

		GeneralModelSingletonSequential gm = null; 
		try {
			gm = GeneralModelSingletonSequential.getInstance();
			for (int i = 0; i < jArr.size(); i++) {
				JsonObject json = jArr.get(i).getAsJsonObject();

				if (!C.continue_country) break; //this is set in: HttpGetter.getJsonArrayGeonames

				String gnName = json.get("countryName").toString().replace("\"", "");
				String gnPopulation = json.get("population").toString().replace("\"", "");
				String geonamesId = json.get("geonameId").toString().replace("\"", "");
				String languages = json.get("languages").toString().replace("\"", "");
				String currencyCode = json.get("currencyCode").toString().replace("\"", "");
				String areaInSqKm = json.get("areaInSqKm").toString().replace("\"", "");
				String continentCode = json.get("continent").toString().replace("\"", "");
				String countryCode = json.get("countryCode").toString().replace("\"", "");
				String capital = json.get("capitalName").toString().replace("\"", "");
				ContinentBean cb = ContinentBean.MAP.get(continentCode);

				gm.begin();
				String countryURI = P.BASE+"Country"+geonamesId;
				gm.addTripleResource(countryURI, P.RDF, "type", P.MGC, "Country");
				try {
					gm.addTripleLiteral(countryURI, P.GN_ONT, "name", gnName);
				} catch (Exception e) {
					Logger.error(CountryInstantiator.class + " CountryURI: "+countryURI, e);
				}
				try {
					gm.addTripleLiteral(countryURI, P.GN_ONT, "population", Integer.parseInt(gnPopulation));
				} catch (Exception e) {
					Logger.error(CountryInstantiator.class + " CountryURI: "+countryURI, e);
				}
				try {
					gm.addTripleLiteral(countryURI, P.MGC, "areaInSqKm", Double.parseDouble(areaInSqKm));
				} catch (Exception e) {
					Logger.error(CountryInstantiator.class + " CountryURI: "+countryURI, e);
				}	
				try {
					gm.addTripleLiteral(countryURI, P.MGC, "currency", currencyCode);
				} catch (Exception e) {
					Logger.error(CountryInstantiator.class + " CountryURI: "+countryURI, e);
				}			
				try {
					gm.addTripleLiteral(countryURI, P.MGC, "languages", languages);
				} catch (Exception e) {
					Logger.error(CountryInstantiator.class + " CountryURI: "+countryURI, e);
				}				
				try {
					gm.addTripleLiteral(countryURI, P.MGC, "countryCode", countryCode);
				} catch (Exception e) {
					Logger.error(CountryInstantiator.class + " CountryURI: "+countryURI, e);
				}	
				try {
					gm.addTripleLiteral(countryURI, P.MGC, "capitalName", capital);
				} catch (Exception e) {
					Logger.error(CountryInstantiator.class + " CountryURI: "+countryURI, e);
				}
				try {
					gm.addTripleLiteral(countryURI, P.MGC, "ContinentName", cb.getGnName());
				} catch (Exception e) {
					Logger.error(CountryInstantiator.class + " CountryURI: "+countryURI, e);
				}
				try {
					gm.addTripleResource(countryURI, P.MGC, "GeonamesLink", "http://sws.geonames.org/"+geonamesId+"/");
				} catch (Exception e) {
					Logger.error(CountryInstantiator.class + " CountryURI: "+countryURI, e);
				}
				if (setdbpedia) {
					String url_rdf = "http://sws.geonames.org/"+geonamesId+"/about.rdf";
					String subj = "http://sws.geonames.org/"+geonamesId+"/";
					String pred = RDFS.seeAlso;
					RDFReaderOpenRDF rdfReader = new RDFReaderOpenRDF(url_rdf);
					Model model = rdfReader.filterWeb(subj, pred, null);
					Set<Value> objs = model.objects();
					Iterator<Value> it = objs.iterator();
					while(it.hasNext()) {
						Value v = (Value) it.next();
						gm.addTripleResource(countryURI, P.MGC, "DBPediaLink", v.stringValue());
					}
				}
				try {
					gm.addTripleResource(countryURI, P.MGC, "isInContinent", P.BASE + "Continent"+cb.getGeoNamesId());
				} catch (Exception e) {
					Logger.error(CountryInstantiator.class + " CountryURI: "+countryURI, e);
				}
				gm.commit();
			}
		}catch (Exception e) {
			Logger.error(CountryInstantiator.class + " gnName ", e);
		} finally {
			gm.close();
		}



	}

}


