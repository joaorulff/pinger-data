package edu.stanford.slac.pinger.instantiator.physicallocation;

import edu.stanford.slac.pinger.bean.ContinentBean;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.P;
import edu.stanford.slac.pinger.model.general.GeneralModelSingletonSequential;

public class ContinentInstantiator {

	public static void start() {

		GeneralModelSingletonSequential gm = null; 
		try {
			gm = GeneralModelSingletonSequential.getInstance();
			for (ContinentBean cb : ContinentBean.MAP.values()) {
				gm.begin();
				String continentURI = P.BASE+"Continent"+cb.getGeoNamesId();
				gm.addTripleResource(continentURI, P.RDF, "type", P.MGC, "Continent");
				gm.addTripleLiteral(continentURI, P.GN_ONT, "name", cb.getGnName());
				gm.addTripleResource(continentURI, P.MGC, "DBPediaLink", cb.getDBPediaLink());
				gm.addTripleResource(continentURI, P.MGC, "GeonamesLink",cb.getGeonamesLink());
				gm.addTripleLiteral(continentURI, P.MGC, "continentCode",cb.getContinentCode());
				gm.commit();
			}
		}catch (Exception e) {
			Logger.error("ContinentInstantiator " , e);
		} finally {
			gm.close();
		}
	}
}
