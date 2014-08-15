package edu.stanford.slac.retriplifier.off;

import java.util.HashSet;

import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;

import edu.stanford.slac.pinger.general.P;
import edu.stanford.slac.pinger.general.QueryString;
import edu.stanford.slac.pinger.general.vocabulary.PingER_ONT;
import edu.stanford.slac.pinger.general.vocabulary.RDF;
import edu.stanford.slac.retriplifier.TripleModelOff;

public class MeasurementInsantiator {
	
	public static String queryPage(long init, long length) {
		
		QueryString qs = new QueryString(
				"SELECT ?MeasurementURI ?SourceDestinationNodes ?DateTime ?Metric ?Value ",
				"",
				"WHERE {",
				"	?MeasurementURI a MD:StatisticalAnalysis . ",
				"	?MeasurementURI MD:timestamp ?DateTime . ",
				"	?MeasurementURI MD:measuresMetric ?SourceDestinationNodes . ",
				"	?MeasurementURI MD:measurementsAnalyzed ?Metric . ",
				"	?MeasurementURI MD:StatisticalAnalysisValue ?Value . ",
				"}",
				//"ORDER BY ?MeasurementURI",
				"LIMIT "+length,
				"OFFSET "+init,
				""
			);
			qs.addPrefix(P.PREFIXES);
		return qs.toString();		
	}
	
	public static void main(String args[]) {
		//System.out.println(queryPage(8000, 8000));
	}

	public static void measurements() throws QueryEvaluationException {
		TripleModelOff tm = TripleModelOff.getInstance();
	
		
		final long pageLength = 6000;
		long row = 9258000;
		
		HashSet<String> URIs = new HashSet<String>();
		int hashSetCut = 1000;
		String lastURI = "";
		while (true) {
			String q = queryPage(row, pageLength);
			System.out.println(q);
			TupleQueryResult result = SPARQLQuery.query(q.toString());
			if (!result.hasNext()) break;
			while (result.hasNext()) {
				String MeasurementURI = null;
				try {
					BindingSet bindingSet  = result.next();
					MeasurementURI = bindingSet.getValue("MeasurementURI").stringValue();
					if (URIs.size() > hashSetCut) {
						URIs = new HashSet<String>();
						URIs.add(lastURI);
					}
					
					if (URIs.contains(MeasurementURI)) continue;
					
					URIs.add(MeasurementURI);
					
					
					String DateTime = bindingSet.getValue("DateTime").stringValue();
					String SourceDestinationNodes = bindingSet.getValue("SourceDestinationNodes").stringValue();
					SourceDestinationNodes = SourceDestinationNodes.replace("Metric", "");
					String Metric = bindingSet.getValue("Metric").stringValue();
					Metric = Metric.replace("SimpleMeasurement-", "Metric");
					Metric = Metric.replace("Measurement", "");
					if (Metric.contains("ConditionalLossProability")) {
						Metric = Metric.replace("ConditionalLossProability", "ConditionalLossProbability");
					}
					float Value = Float.parseFloat(bindingSet.getValue("Value").stringValue());

					tm.addTripleResource(MeasurementURI, RDF.type, PingER_ONT.Measurement);
					tm.addTripleResource(MeasurementURI, PingER_ONT.hasSourceDestinationNodes, SourceDestinationNodes);
					tm.addTripleResource(MeasurementURI, PingER_ONT.hasDateTime, DateTime);
					tm.addTripleResource(MeasurementURI, PingER_ONT.measuresMetric, Metric);
					tm.addTripleLiteral(MeasurementURI, PingER_ONT.hasValue, Value);
					
					
					lastURI = MeasurementURI;
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(MeasurementURI);
				}
			}
			row += pageLength;
			continue;			
		}
		tm.writeTriplesAndClean();
		SPARQLQuery.closeConnection();
	}

	
}
