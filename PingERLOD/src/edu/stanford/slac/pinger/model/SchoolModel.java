package edu.stanford.slac.pinger.model;

import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResult;

import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.P;
import edu.stanford.slac.pinger.model.general.GeneralModelSingletonSequential;

public class SchoolModel {

	public static String getSchoolResourceFromPingerLatLong(String pingerLat, String pingerLong) {
		GeneralModelSingletonSequential gm = GeneralModelSingletonSequential.getInstance();
		String q = 
				"select ?school where {" +
				"?school "+P.RDF+":type " + P.MGC+":School . " +
				"?school "+P.MGC+":PingERLat ?lat . " +
				"?school "+P.MGC+":PingERLong ?lng . " +
				"filter (xsd:double("+ pingerLat + ") = xsd:double(?lat) && xsd:double(" + pingerLong + ") = xsd:double(?lng) ) . " +
				"}";
		TupleQueryResult result = null;
		try {
			result = gm.query(q);
			if (result.hasNext()) {
				BindingSet bindingSet;
				bindingSet = result.next();
				Value school = bindingSet.getValue("school");
				return school.stringValue();
			}
		} catch (Exception e) {
			return null;
		} finally {
			try {
				result.close();
			} catch (Exception e) {
				Logger.log("getSchoolResourceFromPingerLatLong",e);
			}
		}
		return null;
	}
	
}
