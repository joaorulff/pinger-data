package edu.stanford.slac.retriplifier.off;

import edu.stanford.slac.pinger.bean.ContinentBean;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.P;
import edu.stanford.slac.pinger.general.vocabulary.GN_ONT;
import edu.stanford.slac.pinger.general.vocabulary.OWL;
import edu.stanford.slac.pinger.general.vocabulary.PingER_ONT;
import edu.stanford.slac.pinger.general.vocabulary.RDF;
import edu.stanford.slac.retriplifier.TripleModelOff;

public class ContinentInstantiator {

	public static void start() {

		TripleModelOff tm = null;
		try {
			tm = TripleModelOff.getInstance();
			for (ContinentBean cb : ContinentBean.MAP.values()) {
				String continentURI = P.BASE+"Continent"+cb.getGeoNamesId();
				tm.addTripleResource(continentURI, RDF.type, PingER_ONT.Continent);
				tm.addTripleLiteral(continentURI, GN_ONT.name, cb.getGnName());
				tm.addTripleResource(continentURI, PingER_ONT.DBPediaLink, cb.getDBPediaLink());
				tm.addTripleResource(continentURI, PingER_ONT.GeonamesLink, cb.getGeonamesLink());
				tm.addTripleResource(continentURI, OWL.sameAs, cb.getDBPediaLink());
				tm.addTripleResource(continentURI, OWL.sameAs, cb.getGeonamesLink());
				tm.addTripleLiteral(continentURI, PingER_ONT.continentCode, cb.getContinentCode());
			}
		}catch (Exception e) {
			Logger.error("ContinentInstantiator " , e);
		} finally {
			tm.writeTriplesAndClean();
		}
	}
}
