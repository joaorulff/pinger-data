package edu.stanford.slac.retriplifier.off;

import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;

import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.retriplifier.OWLIMConnection;

public class SPARQLQuery {

	public static TupleQueryResult query(String query) {
		TupleQueryResult result = null;
		try {
			TupleQuery tupleQuery = null;
			tupleQuery = OWLIMConnection.getCon().prepareTupleQuery(QueryLanguage.SPARQL, query);		
			result = tupleQuery.evaluate();
			return result;
		} catch (Exception e) {
			Logger.log("queryResult", e);
			return null;
		}
	}
	
	public static void closeConnection() {
		OWLIMConnection.close();
	}
	
	
}
