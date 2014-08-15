package edu.stanford.slac.pinger.instantiator;

import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.P;
import edu.stanford.slac.pinger.model.general.GeneralModelSingletonSequential;

public class SetUpPrefixes {

	public static void start(boolean isToGenerate) {
		if (isToGenerate)
			C.writeIntoFile(getPrefixesRDFXML(), C.PREFIXES_FILE);
		GeneralModelSingletonSequential gm = GeneralModelSingletonSequential.getInstance();
		gm.addRDFXMLFile(C.PREFIXES_FILE, P.BASE);
		gm.close();
	}

	public static String getPrefixesRDFXML() {
		String s = "";
		s +="<?xml version=\"1.0\"?>\n";
		s +="<rdf:RDF xmlns=\""+P.BASE+"\"\n";
		s +=" \txml:base=\""+P.BASE+"\"\n";
		for (String p : P.MAP_PREFIXES.keySet()) {
			
			s +=" \txmlns:"+p+"=\""+P.MAP_PREFIXES.get(p)+"\"\n";
			
		}
		s +=">\n";
		s = s.replace("\n>\n", ">\n");
		s +="</rdf:RDF>";
		return s;
	}
	public static void main(String[] args) {
		C.writeIntoFile(getPrefixesRDFXML(), C.PREFIXES_FILE);
		GeneralModelSingletonSequential gm = GeneralModelSingletonSequential.getInstance();
		gm.addRDFXMLFile(C.PREFIXES_FILE, P.BASE);
		gm.close();
	}
}
