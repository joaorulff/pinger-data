package edu.stanford.slac.pinger.model.general;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;

import org.openrdf.model.Resource;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.resultio.QueryResultIO;
import org.openrdf.query.resultio.TupleQueryResultFormat;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.n3.N3Writer;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.P;
import edu.stanford.slac.pinger.general.vocabulary.XSD;

public abstract class GeneralModelSingletonParent {

	private static final int TYPE_CVS = 1;
	private static final int TYPE_RDF = 2;
	private static final int TYPE_JSON = 3;
	
	private static String SesameServer = null, RepositoryID = null;
	
	protected RepositoryConnection con;
	protected Repository repo;
	protected ValueFactory factory;
	
	
	protected static GeneralModelSingletonParent instance = null;

	protected GeneralModelSingletonParent() {
	}
	protected void connect() {
		try {
			if (SesameServer==null && RepositoryID==null) {
				SesameServer = C.SESAME_SERVER;
				RepositoryID = C.REPOSITORY_ID;
			}
			repo = new HTTPRepository(SesameServer, RepositoryID);
			repo.initialize();
			con = repo.getConnection();
			factory = repo.getValueFactory();
		} catch (RepositoryException e) {
			Logger.error("GeneralModelSingletonParent.connect " , e);
		}
	}
	public static void setServerFromPropertiesFile(String propertiesFilePath) {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(propertiesFilePath));
			SesameServer = prop.getProperty("sesame_server");
			RepositoryID = prop.getProperty("repository_id");			
		} catch (Exception e) {
			Logger.error("GeneralModelSingletonParent.connect " , e);		
		} 
	}
	public static void setServerAndRepositoryID(String sesameServer, String repoId) {
		try {
			SesameServer = sesameServer;
			RepositoryID = repoId;
		} catch (Exception e) {
			Logger.error("GeneralModelSingletonParent.connect " , e);		
		} 
	}
	public void close() {
		try {
			repo.shutDown();
			con.close();
			instance = (GeneralModelSingletonParent) null;
		} catch (Exception e) {
			Logger.error("GeneralModelSingletonParent.close " , e);
		}
	}
	public void begin() {
		try {
			con.begin();
			Logger.log("Connected to the repository.");
		} catch (Exception e) {
			Logger.error("GeneralModelSingletonParent.begin " , e);
		}
	}
	public boolean commit() {
		try {
			con.commit();
			Logger.log("Transactions commited successfully.");
			return true;
		} catch (Exception e) {
			Logger.error("GeneralModelSingletonParent.commit ",  e);
			return false;
		}
	}

	protected abstract void _addTripleLiteral(String resourceURI, 
			String propertyNS, String dataProperty,
			String obj);

	/**
	 * Adds a triple of the form [Resource, Datatype Property, Data Value]
	 * @param resourceURI - The absolute URI for a new Resource
	 * @param propertyNS
	 * @param dataProperty
	 * @param String value
	 */
	public void addTripleLiteral(String resourceURI, 
			String propertyNS, String dataProperty,
			String value) {
		StringBuffer obj = new StringBuffer("\""+value+"\"^^<"+XSD.string+">");
		_addTripleLiteral(resourceURI, propertyNS, dataProperty, obj.toString());
	}
	/**
	 * Adds a triple of the form [Resource, Datatype Property, Data Value]
	 * @param resourceURI - The absolute URI for a new Resource
	 * @param propertyNS
	 * @param dataProperty
	 * @param float value
	 */
	public void addTripleLiteral(String resourceURI, 
			String propertyNS, String dataProperty,
			float value) {
		StringBuffer obj = new StringBuffer("\""+value+"\"^^<"+XSD.Float+">");
		_addTripleLiteral(resourceURI, propertyNS, dataProperty, obj.toString());		
	}
	/**
	 * Adds a triple of the form [Resource, Datatype Property, Data Value]
	 * @param resourceURI - The absolute URI for a new Resource
	 * @param propertyNS
	 * @param dataProperty
	 * @param double value
	 */
	public void addTripleLiteral(String resourceURI, 
			String propertyNS, String dataProperty,
			double value) {
		StringBuffer obj = new StringBuffer("\""+value+"\"^^<"+XSD.Double+">");
		_addTripleLiteral(resourceURI, propertyNS, dataProperty, obj.toString());
	}
	/**
	 * Adds a triple of the form [Resource, Datatype Property, Data Value]
	 * @param resourceURI - The absolute URI for a new Resource
	 * @param propertyNS
	 * @param dataProperty
	 * @param int value
	 */
	public void addTripleLiteral(String resourceURI, 
			String propertyNS, String dataProperty,
			int value) {
		StringBuffer obj = new StringBuffer("\""+value+"\"^^<"+XSD.integer+">");
		_addTripleLiteral(resourceURI, propertyNS, dataProperty, obj.toString());
	}

	/**
	 * Adds a triple of the form [Resource, Datatype Property, Data Value]
	 * @param resourceURI - The absolute URI for a new Resource
	 * @param propertyNS
	 * @param dataProperty
	 * @param NonNegativeInteger value
	 */
	public void addTripleLiteralNonNegativeInteger(String resourceURI, 
			String propertyNS, String dataProperty,
			int value) {
		StringBuffer obj = new StringBuffer("\""+value+"\"^^<"+XSD.nonNegativeInteger+">");
		_addTripleLiteral(resourceURI, propertyNS, dataProperty, obj.toString());
	}

	/**
	 * Adds a triple of the form [Resource, Datatype Property, Data Value]
	 * @param resourceURI - The absolute URI for a new Resource
	 * @param propertyNS
	 * @param dataProperty
	 * @param NonNegativeInteger value
	 */
	public void addTripleLiteralDateTime(String resourceURI, 
			String propertyNS, String dataProperty,
			String value) {
		StringBuffer obj = new StringBuffer("\""+value+"\"^^<"+XSD.dateTime+">");
		_addTripleLiteral(resourceURI, propertyNS, dataProperty, obj.toString());
	}
	
	protected abstract void _addTripleResource(String subj, String pred, String obj);
	
	/**
	 * Adds a triple of the form [Absolute URI, NS:Property, NS:Resource]
	 * @param resourceURI Absolute URI
	 * @param propertyNS
	 * @param ojectProperty
	 * @param resourceBns
	 * @param resourceB
	 */
	public void addTripleResource(String resourceURI, 
			String propertyNS, String ojectProperty,
			String resourceBns, String resourceB) {
		_addTripleResource(resourceURI, P.MAP_PREFIXES.get(propertyNS)+ojectProperty, P.MAP_PREFIXES.get(resourceBns)+resourceB);
	}


	/**
	 * Adds a triple of the form [Absolute URI, NS:Property, Resource]
	 * @param resourceURI
	 * @param propertyNS
	 * @param ojectProperty
	 * @param resourceB
	 */
	public void addTripleResource(String resourceURI, 
			String propertyNS, String ojectProperty,
			String resourceB) {
		_addTripleResource(resourceURI, P.MAP_PREFIXES.get(propertyNS)+ojectProperty, resourceB);
	}

	/**
	 * Adds a triple of the form [Absolute URI, Absolute URI Property, Absolute URI Resource]
	 * @param resourceAns
	 * @param resourceA
	 * @param propertyNS
	 * @param ojectProperty
	 * @param resourceBns
	 * @param resourceB
	 */
	public void addTripleResource(String resourceURI, 
			String property,
			String resourceB) {
		_addTripleResource(resourceURI, property, resourceB);
	}

	public String queryServerEndpoint(String query) {
		TupleQueryResult result = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			TupleQuery tupleQuery = null;
			tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, query);		
			result = tupleQuery.evaluate();
			QueryResultIO.write(result, TupleQueryResultFormat.SPARQL, baos);
			return baos.toString("UTF-8");
		} catch (Exception e) {
			Logger.log("queryResult", e);
			return e.getMessage();
		} finally {
			try {
				result.close();
			} catch (Exception e) {
				Logger.log("queryResult", e);
			}
		}
	}
	
	
	private String queryResult(String sparqlQuery, int outputType) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		TupleQueryResult result = null;
		try {
			result = query(sparqlQuery);
			TupleQueryResultFormat format = null;
			if (outputType==TYPE_CVS) format = TupleQueryResultFormat.CSV;
			else if (outputType==TYPE_JSON) format = TupleQueryResultFormat.JSON;
			else if (outputType==TYPE_RDF) format = TupleQueryResultFormat.SPARQL;

			QueryResultIO.write(result, format, baos);
			return baos.toString("UTF-8");
		} catch (Exception e) {
			Logger.log("queryResult", e);
			return e.getMessage();
		} finally {
			try {
				result.close();
			} catch (Exception e) {
				Logger.log("queryResult", e);
			}
		}
	}

	public String setBindingURI(String variable, String absoluteURI, String sparqlQuery) throws Exception  {
		TupleQuery query = con.prepareTupleQuery(QueryLanguage.SPARQL, sparqlQuery);
		Value uri = con.getValueFactory().createURI(absoluteURI);
		query.setBinding(variable, uri);
		return query.toString();
	}
	
	public TupleQueryResult query(String sparqlQuery) throws Exception  {
		TupleQueryResult result = null;
		TupleQuery tupleQuery = null;
		String prefixes = "PREFIX : <"+P.BASE+">\n"+P.PREFIXES;
		tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, prefixes + sparqlQuery);		
		result = tupleQuery.evaluate();
		return result;
	}
	public String queryResultAsCVS(String sparqlQuery) {
		return queryResult(sparqlQuery, TYPE_CVS);
	}
	public String queryResultAsRDF(String sparqlQuery) {
		return queryResult(sparqlQuery, TYPE_RDF);
	}

	public String queryResultAsJSON(String sparqlQuery) {
		return queryResult(sparqlQuery, TYPE_JSON);
	}
	public void addRDFXMLFile(String filePath, String baseURI) {
		try {
			File file = new File(filePath);
			con.add(file, baseURI, RDFFormat.RDFXML);
		} catch (Exception e) {
			Logger.log("addRDFXMLFile", e, "errors");
		}
	}
	public void addNTriplesFile(File file) {
		try {
			Logger.log("Loading the file " + file.getPath() + " into the repository...");
			long t1 = System.currentTimeMillis();
			con.add(file, P.BASE, RDFFormat.NTRIPLES, (Resource) null);
			long t2 = System.currentTimeMillis();
			Logger.log("...done! It took " + (t2-t1)/1000.0/60.0 + " minutes to load the file " + file.getPath() + " into the repository...", "measurements/time");			
		} catch (Exception e) {
			Logger.error("GeneralModelSingletonParent.addNTriplesFile - Error while loading file " + file.getAbsolutePath() + " to the repository.", e);
		}
	}
	public void addNTripleString(String NTriple) {
		Reader reader = new StringReader(NTriple);		
		try {
			con.add(reader, P.BASE, RDFFormat.NTRIPLES);
		} catch (Exception e) {
			Logger.log("addNTripleString",e);
		}		
	}
	/**
	 * This method exports the whole database format into the specified file in N3.
	 * @param filePath
	 */
	public void export(String filePath) {
		try {
			File f = new File(filePath);
			if (!f.exists()) {
				File dir = new File(f.getParent());
				if (!dir.exists()) dir.mkdirs();
				f.createNewFile();
			}
			FileOutputStream fos;
			fos = new FileOutputStream(f);
			//TurtleWriter writer = new TurtleWriter(fos);
			N3Writer writer = new N3Writer(fos);
			con.export(writer, (Resource) null);
			Logger.log("Export success!");
		} catch (Exception e) {
			Logger.error("Error while trying to export",e);
		}
	}
}
