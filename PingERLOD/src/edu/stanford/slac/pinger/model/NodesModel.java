package edu.stanford.slac.pinger.model;

import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResult;

import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.P;
import edu.stanford.slac.pinger.model.general.GeneralModelSingletonSequential;

public final class NodesModel {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		getTownResourceFromPingerLatLong("27.72", "85.32");
	}

	public static String getTownResourceFromPingerLatLong(String pingerLat, String pingerLong) {
		GeneralModelSingletonSequential gm = GeneralModelSingletonSequential.getInstance();
		String q = 
				"select ?town where {" +
				"?town "+P.RDF+":type " + P.MGC+":Town . " +
				"?town "+P.MGC+":PingERLat ?lat . " +
				"?town "+P.MGC+":PingERLong ?lng . " +
				"filter (xsd:double("+ pingerLat + ") = xsd:double(?lat) && xsd:double(" + pingerLong + ") = xsd:double(?lng) ) . " +
				"}";
		TupleQueryResult result = null;		
		try {
			result = gm.query(q);
			if (result.hasNext()) {
				BindingSet bindingSet;
				bindingSet = result.next();
				Value town = bindingSet.getValue("town");
				return town.stringValue();
			}
		} catch (Exception e) {
			return null;
		} finally {
			try {
				result.close();
			} catch (Exception e) {
				Logger.log("getTownResourceFromPingerLatLong",e);
			}
		}
		return null;
	}


}
