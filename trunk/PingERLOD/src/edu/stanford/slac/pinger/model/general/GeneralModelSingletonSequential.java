package edu.stanford.slac.pinger.model.general;

import java.io.Reader;
import java.io.StringReader;

import org.openrdf.model.URI;
import org.openrdf.rio.RDFFormat;

import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.P;



/**
 * This class is to be used to manipulate the RDF Storage.
 * Please, make sure to close() the object when you finish using.
 * Attention: However, close() the connection only once.
 * @author Renan
 *
 */
public class GeneralModelSingletonSequential extends GeneralModelSingletonParent {

	protected GeneralModelSingletonSequential() {}
	
	public static GeneralModelSingletonSequential getInstance() {
		if (instance == null) {			
			instance = new GeneralModelSingletonSequential();
			instance.connect();
		}
		return (GeneralModelSingletonSequential) instance;
	}
	
	@Override
	protected void _addTripleLiteral(String resourceURI, String propertyNS,
			String dataProperty, String obj) {
		StringBuffer sb = new StringBuffer();
		sb.append(
				"<"+resourceURI+"> " +
				"<"+P.MAP_PREFIXES.get(propertyNS)+dataProperty+"> " +
				obj + " ."
		);
		Reader reader = new StringReader(sb.toString());		
		try {
			con.add(reader, P.BASE, RDFFormat.NTRIPLES);
		} catch (Exception e) {
			Logger.error("-----!!!!!!!!!!----- " + resourceURI + " " + P.MAP_PREFIXES.get(propertyNS)+dataProperty + " " + obj, e);
		}		
	}
	@Override
	protected void _addTripleResource(String subj, String pred, String obj) {
		try {
			URI subjURI = factory.createURI(subj);		 
			URI predURI = factory.createURI(pred);
			URI objURI = factory.createURI(obj);
			con.add(subjURI, predURI, objURI);
		} catch (Exception e) {
			Logger.error("-----!!!!!!!!!!----- " + subj + " " + pred + " " + obj, e);
		}
	}
}
