package edu.stanford.slac.pinger.rest.query;

import java.io.InputStream;
import java.net.URL;

import org.openrdf.model.Model;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.StatementCollector;

import edu.stanford.slac.pinger.general.Logger;

/**
 * This class reads RDF/XML files.
 * @author Renan
 *
 */
public class RDFReaderOpenRDF {

	private Model graph;
	public Model getGraph() {
		return graph;
	}
	public RDFReaderOpenRDF(String URL) {
		readWeb(URL);
	}

	private void readWeb(String URL) {
		try {
			URL url = new URL(URL);
			InputStream inputStream = url.openStream();
			RDFParser rdfParser = Rio.createParser(RDFFormat.RDFXML);
			this.graph = new LinkedHashModel();
			rdfParser.setRDFHandler(new StatementCollector(graph));
			rdfParser.parse(inputStream, url.toString());
		} catch (Exception e) {
			this.graph = null;
			Logger.log("readWeb", e, "errors");
		}
	}

	/**
	 * Use null in either one of the parameters to get the result of that parameter.
	 * @param URL_RDF URL of the RDF to be read over the web.
	 * @param subjURI
	 * @param predURI
	 * @param objURI
	 * @return
	 */
	public Model filterWeb(String subjURI, String predURI, String objURI) {
		ValueFactory factory = ValueFactoryImpl.getInstance();
		URI subj = (subjURI==null)?null:factory.createURI(subjURI);
		URI pred = (predURI==null)?null:factory.createURI(predURI);
		URI obj = (objURI==null)?null:factory.createURI(objURI);

		try {
			Model res = graph.filter(subj, pred, obj);
			return res;
		} catch (Exception e) {
			Logger.log("filterWeb", e, "errors");
			return null;
		}
	}


	public static void main(String[] args) {

	}

}
