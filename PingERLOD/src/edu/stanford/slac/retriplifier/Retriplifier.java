package edu.stanford.slac.retriplifier;

import org.openrdf.query.QueryEvaluationException;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.retriplifier.off.MeasurementInsantiator;
import edu.stanford.slac.retriplifier.off.NetworkNodes;

public class Retriplifier {

	/**
	 * @param args
	 * @throws QueryEvaluationException 
	 */
	public static void main(String[] args) throws QueryEvaluationException {
		C.setDebugLevel(1);
		C.mkTmpDirGrantingPermissions(C.NTRIPLES_DIR);
		TripleModelOff tm = TripleModelOff.getInstance();
		tm.setTick("measurementsss2_");
		tm.start();
		//MeasurementParametersInstantiatorOff.start(true);
		
		//ContinentInstantiator.start();
		
		MeasurementInsantiator.measurements();
		
		
		
		System.out.println("Done!");
		
	
	}

}
