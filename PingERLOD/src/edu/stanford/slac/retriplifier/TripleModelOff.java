package edu.stanford.slac.retriplifier;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;

import org.openrdf.model.Resource;
import org.openrdf.model.URI;
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
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.turtle.TurtleWriter;

import virtuoso.sesame2.driver.VirtuosoRepository;
import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.P;
import edu.stanford.slac.pinger.general.vocabulary.XSD;
import edu.stanford.slac.pinger.model.general.file.LoadRemainingFiles;

public class TripleModelOff {

	private static final int TYPE_CVS = 1;
	private static final int TYPE_RDF = 2;
	private static final int TYPE_JSON = 3;
	
	private static String server = null, user = null, password = null;
	
	private RepositoryConnection con;
	private Repository repo;
	private ValueFactory factory;
	
	private static TripleModelOff instance = null;

	private StringBuffer allTriples;

	
	private TripleModelOff() {
	}
	
	public static TripleModelOff getInstance() {
		if (instance == null) {			
			instance = new TripleModelOff();
			instance.connect();
		}
		return (TripleModelOff) instance;
	}	
	private void connect() {
		try {
			if (server==null && user==null && password == null) {
				server = "jdbc:virtuoso://localhost:1111";
				user = "dba";
				password = "dba";
			}
			
			repo = new VirtuosoRepository(server, user, password);
			repo.initialize();
			con = repo.getConnection();
			factory = repo.getValueFactory();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}
	public static void setServerFromPropertiesFile(String propertiesFilePath) {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(propertiesFilePath));
			server = prop.getProperty("server");
			user = prop.getProperty("user");
			password = prop.getProperty("password");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	public static void setServer(String server_, String user_, String password_) {
		try {
			server = server_;
			user = user_;
			password = password_;
		} catch (Exception e) {	e.printStackTrace(); } 
	}
	/**
	 * Shuts down the connection with the RDF database.
	 */
	private void close() {
		try {
			repo.shutDown();
			con.close();
			instance = (TripleModelOff) null;
		} catch (Exception e) { e.printStackTrace(); }
	}
	/**
	 * Begins a transaction.
	 */
	private void begin() {
		try {
			con.begin();
		} catch (Exception e) { e.printStackTrace(); }
	}
	/**
	 * Commits a transaction if successful. Otherwise, rolls back.
	 * @return
	 */
	private boolean commit() {
		try {
			con.commit();
			return true;
		} catch (Exception e) { 
			e.printStackTrace();
			return false;
		}
	}
	
	private static int CUT_HEAP_MEMORY = -1;
	private static int CUT_HD = -1;
	
	/**
	 * This method prevents the StringBuffer variable to exceed the available memory.
	 */
	private synchronized void checkHeap() {
		if (allTriples.length() >= CUT_HEAP_MEMORY) {
			System.out.println("Cutting StringBuffer!");
			try {
				if (fileContentSize >= CUT_HD) {
					fileContentSize = 0;
					String fileName;
					if (currentTick != null && currentMetric != null)
						fileName = currentTick + "_" + currentMetric + "_" + currentFileIndex++;
					else {
						fileName = "n"+currentFileIndex++;
					}
					currentFilePath = C.NTRIPLES_DIR+fileName+".ntriples";
					C.createFileGrantingPermissions(currentFilePath);
				}
				fileContentSize += allTriples.length();
				writeTriplesAndClean();
			} catch (Exception e) {
				Logger.error("GeneralModelSingletonParallelized.checkHeap " , e);
			}
		}
	}

	private int fileContentSize;
	private int currentFileIndex;
	private String currentFilePath;
	
	public void setTick(String tick) {
		this.currentTick = tick;
	}
	
	
	private String currentTick = "";
	private String currentMetric = "";
	public void setCurrentTick(String currentTick) {this.currentTick = currentTick;}
	public void setCurrentMetric(String currentMetric) {this.currentMetric = currentMetric;}
	
	public void writeTriplesAndClean() {
		try {
			PrintWriter out;
			out = new PrintWriter(new BufferedWriter(new FileWriter(currentFilePath, true)));
			out.write(allTriples.toString());
			out.close();
			allTriples = new StringBuffer();
		} catch (Exception e) {
			Logger.error("GeneralModelSingletonParallelized.writeTriplesAndClean " ,  e);
		}
	}
	
	public void start() {
		C.cleanDirectory(C.NTRIPLES_DIR);
		allTriples = new StringBuffer();
		fileContentSize = 0;
		currentFileIndex = 1;
		String fileName = null;
		if (currentTick != null && currentMetric != null)
			fileName = currentTick + "_" + currentMetric + "_" + currentFileIndex;
		else {
			fileName = "n"+currentFileIndex;
		}
		currentFilePath = C.NTRIPLES_DIR+fileName+".ntriples";
		C.createFileGrantingPermissions(currentFilePath);
		
		CUT_HEAP_MEMORY = (int) (Runtime.getRuntime().freeMemory()/C.CUT_HEAP_COEF); //divided by 2 is the standard.. divided by 10 generates lots of files of ~ 13 MB
		CUT_HD = 150*C.MB;
		
		if (currentTick != null && currentTick.contains("days"))
			LoadRemainingFiles.addNTriplesDirectory(C.NTRIPLES_DIR);
	}
	
	/**
	 * Adds a triple of the form [Subject Absolute URI, Predicate Absolute URI, Object Absolute URI]
	 * @param resourceA
	 * @param objectPropertyURI
	 * @param resourceB
	 */
	public void addTripleResource(String resourceA, String objectPropertyURI, String resourceB) {
		try {
			checkHeap();
			allTriples.append(
					"<"+resourceA+"> " +
					"<"+objectPropertyURI+"> " +
					"<"+resourceB+"> . \n"
			);
		} catch (Exception e) { e.printStackTrace(); }
	}	
	
	/**
	 * Adds a triple of the form [Resource, Data Property, Float Data Value]
	 * @param resourceURI - The absolute URI of the resource subject
	 * @param dataPropertyURI - The absolute URI of the data property.
	 * @param value - Must be either integer, float, double or string.
	 */
	public void addTripleLiteral(String resourceURI, 
			String dataPropertyURI,
			Object value) {
		String type = null;
		if (value.getClass().equals(Integer.class)) {			
			type = XSD.integer;
		} else if (value.getClass().equals(Float.class)) {
			type = XSD.Float;
		} else if (value.getClass().equals(Double.class)) {
			type = XSD.Double;
		} else if (value.getClass().equals(String.class)) {
			type = XSD.string;
		} else {			
			throw new IllegalArgumentException("The parameter value must be either integer, float, double or string.");
		}
		if (type != null) {
			StringBuffer obj = new StringBuffer("\""+value+"\"^^<"+type+">");
			_addTripleLiteral(resourceURI, dataPropertyURI, obj.toString());
		}
	}
		
	
	/**
	 * Adds a triple of the form [Resource, Data Property, NonNegativeInteger Data Value]
	 * @param resourceURI - The absolute URI of the resource subject
	 * @param dataPropertyURI - The absolute URI of the data property.
	 * @param value - a decimal value
	 */
	public void addTripleLiteralDecimal(String resourceURI, 
			String dataPropertyURI,
			float value) {
		StringBuffer obj = new StringBuffer("\""+value+"\"^^<"+XSD.decimal+">");
		_addTripleLiteral(resourceURI, dataPropertyURI, obj.toString());
	}

	/**
	 * Adds a triple of the form [Resource, Data Property, NonNegativeInteger Data Value]
	 * @param resourceURI - The absolute URI of the resource subject
	 * @param dataPropertyURI - The absolute URI of the data property.
	 * @param value - a NonNegativeInteger value
	 */
	public void addTripleLiteralNonNegativeInteger(String resourceURI, 
			String dataPropertyURI,
			int value) {
		StringBuffer obj = new StringBuffer("\""+value+"\"^^<"+XSD.nonNegativeInteger+">");
		_addTripleLiteral(resourceURI, dataPropertyURI, obj.toString());
	}
	/**
	 * Adds a triple of the form [Resource, Data Property, DateTime Data Value]
	 * @param resourceURI - The absolute URI of the resource subject
	 * @param dataPropertyURI - The absolute URI of the data property.
	 * @param value - a DateTime value
	 */
	public void addTripleLiteralDateTime(String resourceURI, 
			String dataPropertyURI,
			String value) {
		StringBuffer obj = new StringBuffer("\""+value+"\"^^<"+XSD.dateTime+">");
		_addTripleLiteral(resourceURI, dataPropertyURI, obj.toString());
	}

	/**
	 * Removes a triple of the form [Subject Absolute URI, Predicate Absolute URI, Object Absolute URI]
	 * Leave any of the parameters null to delete all triples that refers to the non-null parameters. 
	 * @param resourceA
	 * @param objectPropertyURI
	 * @param resourceB
	 */	
	public void removeTriple(String subj, String pred, String obj){
		try{
			URI subjURI = (subj==null)?null:factory.createURI(subj);		 
			URI predURI = (pred==null)?null:factory.createURI(pred);
			URI objURI = (obj==null)?null:factory.createURI(obj);
			URI graphURI = factory.createURI(P.DEFAULT_GRAPH);
			con.remove(subjURI, predURI, objURI, graphURI);
		}catch(Exception e){e.printStackTrace();}
	}
	
	public void clearDB() {
		try {
			URI graphURI = factory.createURI(P.DEFAULT_GRAPH);
			con.remove((URI)null, (URI)null, (URI)null, graphURI);
		} catch (RepositoryException e) { e.printStackTrace(); }
	}
	
	/**
	 * Removes a triple of the form [Resource, Data Property, Primitive Data Value]
	 * Leave any of the parameters null to delete all triples that refers to the non-null parameters. 
	 * @param resourceURI - The absolute URI of the resource subject
	 * @param dataPropertyURI - The absolute URI of the data property.
	 * @param value - Must be either integer, float, double or string.
	 */
	public void removeTripleLiteral(String resourceURI, 
			String dataPropertyURI,
			Object value) {
		URI datatype = null;
		Value v = null;
		if (value != null) { 
			if (value.getClass().equals(Integer.class)) {			
				datatype = factory.createURI(XSD.integer);
			} else if (value.getClass().equals(Float.class)) {
				datatype = factory.createURI(XSD.Float);
			} else if (value.getClass().equals(Double.class)) {
				datatype = factory.createURI(XSD.Double);
			} else if (value.getClass().equals(String.class)) {
				datatype = factory.createURI(XSD.string);
			} else {			
				throw new IllegalArgumentException("The parameter value must be either integer, float, double or string.");
			}
			v = factory.createLiteral(value.toString(), datatype);
		}
		_removeTripleLiteral(resourceURI, dataPropertyURI, v);		
	}
		

	/**
	 * Adds a triple of the form [Resource, Data Property, NonNegativeInteger Data Value]
	 * @param resourceURI - The absolute URI of the resource subject
	 * @param dataPropertyURI - The absolute URI of the data property.
	 * @param value - a NonNegativeInteger value
	 */
	public void removeTripleLiteralNonNegativeInteger(String resourceURI, 
			String dataPropertyURI,
			int value) {
		URI datatype = factory.createURI(XSD.nonNegativeInteger);		
		Value v = factory.createLiteral(Integer.toString(value), datatype);		
		_removeTripleLiteral(resourceURI, dataPropertyURI, v);		
	}
	/**
	 * Adds a triple of the form [Resource, Data Property, DateTime Data Value]
	 * @param resourceURI - The absolute URI of the resource subject
	 * @param dataPropertyURI - The absolute URI of the data property.
	 * @param value - a DateTime value
	 */
	public void removeTripleLiteralDateTime(String resourceURI, 
			String dataPropertyURI,
			String value) {
		URI datatype = factory.createURI(XSD.dateTime);		
		Value v = factory.createLiteral(value, datatype);		
		_removeTripleLiteral(resourceURI, dataPropertyURI, v);		
	}

	public String setBindingURI(String variable, String absoluteURI, String sparqlQuery) throws Exception  {
		TupleQuery query = con.prepareTupleQuery(QueryLanguage.SPARQL, sparqlQuery);
		Value uri = con.getValueFactory().createURI(absoluteURI);
		query.setBinding(variable, uri);
		return query.toString();
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
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void addNTriplesFile(File file) {
		try {
			URI graph = factory.createURI("http://pinger.slac.stanford.edu");
			con.add(file, P.BASE, RDFFormat.NTRIPLES, graph);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	/*
	public void addNTriplesFile(File file) {
		try {
			con.add(file, P.BASE, RDFFormat.NTRIPLES, (Resource) null);
		} catch (Exception e) { e.printStackTrace(); }
	}
	*/
	/**
	 * This method adds a triple in NTriple format.
	 * @param NTriple
	 */
	public void addNTripleString(String NTriple) {
		Reader reader = new StringReader(NTriple);		
		try {
			con.add(reader, P.BASE, RDFFormat.NTRIPLES);
		} catch (Exception e) { e.printStackTrace();
		}		
	}
	/**
	 * This method exports the whole RDF database into the specified file in Turtle format.
	 * @param filePath
	 */
	public void exportDumpFile(String filePath) {
		try {
			File f = new File(filePath);
			if (!f.exists()) {
				File dir = new File(f.getParent());
				if (!dir.exists()) dir.mkdirs();
				f.createNewFile();
			}
			FileOutputStream fos;
			fos = new FileOutputStream(f);
			TurtleWriter writer = new TurtleWriter(fos);
			con.export(writer, (Resource) null);
		} catch (Exception e) { e.printStackTrace(); }
	}	
	
	private void _addTripleLiteral(String resourceURI, 
			String dataPropertyURI,
			String obj) {
		checkHeap();
		allTriples.append(
				"<"+resourceURI+"> " +
				"<"+dataPropertyURI+"> " +
				obj + " . \n"			
		);
	}
	
	private void _removeTripleLiteral(String resourceURI, 
			String dataPropertyURI,
			Value value) {
		try{
			URI subjURI = (resourceURI==null)?null:factory.createURI(resourceURI);		 
			URI predURI = (dataPropertyURI==null)?null:factory.createURI(dataPropertyURI);
			URI graphURI = factory.createURI(P.DEFAULT_GRAPH);
			con.remove(subjURI, predURI, value, graphURI);
		}catch(Exception e){e.printStackTrace();}
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
			return e.getMessage();
		} finally {
			try {
				result.close();
			} catch (Exception e) { e.printStackTrace(); }
		}
	}
	private TupleQueryResult query(String sparqlQuery) throws Exception  {
		TupleQueryResult result = null;
		TupleQuery tupleQuery = null;
		tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, sparqlQuery);		
		result = tupleQuery.evaluate();
		return result;
	}
}
