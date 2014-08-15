package edu.stanford.slac.retriplifier;

import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;

public class OWLIMConnection {
	
	private static String server = null, id = null;
	
	private RepositoryConnection con;
	private Repository repo;
	
	private static OWLIMConnection instance = null;
	
	private static OWLIMConnection getInstance() {
		if (instance == null) {			
			instance = new OWLIMConnection();
			instance.connect();
		}
		return (OWLIMConnection) instance;
	}	
	public void connect() {
		try {
			if (server==null && id==null) {
				server = "http://localhost:8181/openrdf-sesame";
				id = "pinger2";
			}
			repo = new HTTPRepository(server, id);
			repo.initialize();
			con = repo.getConnection();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}
	
	private void closeInstance() {
		try {
			repo.shutDown();
			con.close();
			instance = (OWLIMConnection) null;
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public static void close() {
		getInstance().closeInstance();
	}
	
	public static RepositoryConnection getCon() {
		return getInstance().con;
	}
}
