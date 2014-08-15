package edu.stanford.slac.retriplifier.off;

import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;

import edu.stanford.slac.pinger.general.P;
import edu.stanford.slac.pinger.general.QueryString;
import edu.stanford.slac.pinger.general.vocabulary.PingER_ONT;
import edu.stanford.slac.pinger.general.vocabulary.RDF;
import edu.stanford.slac.retriplifier.TripleModelOff;

public class NetworkNodes {

	public static void nodes() throws QueryEvaluationException {

		QueryString qs = new QueryString(
			"SELECT ?Node ?hasNodeIP ?hasNodeName ?hasNodeFullName ?hasNodeNickName ?hasNodeSiteName  ",
			"?hasNodeURL ?hasComments ",
			"?hasContactInformation ?hasGMTOffset ?hasLocationDescription ?hasProjectType ?hasPingServer ",
			"?hasTraceServer ?hasDataServer ?hasAppUser ?PingERRegionalGroup ?PingERLat ?PingERLong ",
			"?TownURI ?SchoolURI ?StateURI ?ContinentURI ?CountryURI",
			"",
			"WHERE {",
			"	?Node a MD:NodeInformation .",
			
			"	?Node MD:hasNodeInformation ?hasNodeIPURI .",
			"	?hasNodeIPURI MD:SourceIPValue ?hasNodeIP .",

			"	?Node MD:hasNodeInformation ?hasNodeNameURI .",
			"	?hasNodeNameURI MD:SourceNameValue ?hasNodeName .",
			
			"	?Node MD:hasNodeInformation ?hasNodeFullNameURI .",
			"	?hasNodeFullNameURI MD:SourceFullNameValue ?hasNodeFullName .",

			"	?Node MD:hasNodeInformation ?hasNodeNickNameURI .",
			"	?hasNodeNickNameURI MD:SourceNickNameValue ?hasNodeNickName .",
			
			"	?Node MD:hasNodeInformation ?hasNodeSiteNameURI .",
			"	?hasNodeSiteNameURI MD:SiteNameValue ?hasNodeSiteName .",

			"	?Node MD:hasNodeInformation ?hasNodeURLURI .",
			"	?hasNodeURLURI MD:NodeURLValue ?hasNodeURL .",

			"	?Node MD:hasNodeInformation ?hasCommentsURI .",
			"	?hasCommentsURI MD:NodeCommentsValue ?hasComments .",

			"	?Node MD:hasNodeInformation ?hasContactInformationURI .",
			"	?hasContactInformationURI MD:ContactInformationValue ?hasContactInformation .",

			"	?Node MD:hasNodeInformation ?hasGMTOffsetURI .",
			"	?hasGMTOffsetURI MD:NodeGMTValue ?hasGMTOffset .",

			"	?Node MD:hasNodeInformation ?hasLocationDescriptionURI .",
			"	?hasLocationDescriptionURI MD:LocationDescriptionValue ?hasLocationDescription .",

			"	?Node MD:hasNodeInformation ?hasProjectTypeURI .",
			"	?hasProjectTypeURI MD:ProjectTypeValue ?hasProjectType .",

			"	?Node MD:hasNodeInformation ?hasPingServerURI .",
			"	?hasPingServerURI MD:PingServerValue ?hasPingServer .",

			"	?Node MD:hasNodeInformation ?hasTraceServerURI .",
			"	?hasTraceServerURI MD:TraceServerValue ?hasTraceServer .",

			"	?Node MD:hasNodeInformation ?hasDataServerURI .",
			"	?hasDataServerURI MD:DataServerValue ?hasDataServer .",

			"	?Node MD:hasNodeInformation ?hasAppUserURI .",
			"	?hasAppUserURI MD:AppUserValue ?hasAppUser .",
			
			"	?Node MD:isInPhysicalLocation ?PL .",
			"	?PL MGC:group ?PingERRegionalGroup .",
			"	?PL MGC:latitude ?PingERLat .",
			"	?PL MGC:longitude ?PingERLong .",
			
			"	OPTIONAL { ?Node MGC:isInTown ?TownURI . } ",
			"	OPTIONAL { ?TownURI MGC:isInState ?StateURI . } ",
			"	OPTIONAL { ?TownURI MGC:isInCountry ?CountryURI . }",
			"	OPTIONAL { ?TownURI MGC:isInContinent ?ContinentURI . }",
			"",
			"	OPTIONAL { ?Node MGC:isInSchool ?SchoolURI }"  ,
			"}"
		);
		qs.addPrefix(P.PREFIXES);
		System.out.println(qs.toString());
		
		TripleModelOff tm = TripleModelOff.getInstance();
		
		TupleQueryResult result = SPARQLQuery.query(qs.toString());
		
		while (result.hasNext()) {
			BindingSet bindingSet  = result.next();
			String Node = bindingSet.getValue("Node").stringValue();
			String hasNodeIP = bindingSet.getValue("hasNodeIP").stringValue();
			String hasNodeName = bindingSet.getValue("hasNodeName").stringValue();
			String hasNodeFullName = bindingSet.getValue("hasNodeFullName").stringValue();
			String hasNodeNickName = bindingSet.getValue("hasNodeNickName").stringValue();
			String hasNodeSiteName = bindingSet.getValue("hasNodeSiteName").stringValue();
			String hasNodeURL = bindingSet.getValue("hasNodeURL").stringValue();
			String hasComments = bindingSet.getValue("hasComments").stringValue();
			String hasContactInformation = bindingSet.getValue("hasContactInformation").stringValue();
			String hasGMTOffset = bindingSet.getValue("hasGMTOffset").stringValue();
			String hasLocationDescription = bindingSet.getValue("hasLocationDescription").stringValue();
			String hasProjectType = bindingSet.getValue("hasProjectType").stringValue();
			String hasPingServer = bindingSet.getValue("hasPingServer").stringValue();
			String hasTraceServer = bindingSet.getValue("hasTraceServer").stringValue();
			String hasDataServer = bindingSet.getValue("hasDataServer").stringValue();
			String hasAppUser = bindingSet.getValue("hasAppUser").stringValue();

			
			tm.addTripleResource(Node, RDF.type, PingER_ONT.NetworkNode);
			
			Value val;
			val = bindingSet.getValue("TownURI");
			if (val != null) {
				String town = val.stringValue();
				tm.addTripleResource(Node, PingER_ONT.isInTown, town);
				
			}
			val = bindingSet.getValue("CountryURI");
			if (val != null) {
				String country = val.stringValue();
				tm.addTripleResource(Node, PingER_ONT.isInCountry, country);
			}
			val = bindingSet.getValue("StateURI");
			if (val != null) {
				String state = val.stringValue();
				tm.addTripleResource(Node, PingER_ONT.isInState, state);
			}
			val = bindingSet.getValue("ContinentURI");
			if (val != null) {
				String continent = val.stringValue();
				tm.addTripleResource(Node, PingER_ONT.isInContinent, continent);
			}
			val = bindingSet.getValue("SchoolURI");
			if (val != null) {
				String school = val.stringValue();
				tm.addTripleResource(Node, PingER_ONT.isInSchool, school);
			}
			
			tm.addTripleLiteral(Node, PingER_ONT.hasNodeIP, hasNodeIP);
			tm.addTripleLiteral(Node, PingER_ONT.hasNodeFullName, hasNodeFullName);
			tm.addTripleLiteral(Node, PingER_ONT.hasNodeNickName, hasNodeNickName);
			tm.addTripleLiteral(Node, PingER_ONT.hasNodeName, hasNodeName);
			tm.addTripleLiteral(Node, PingER_ONT.hasNodeSiteName, hasNodeSiteName);
			tm.addTripleLiteral(Node, PingER_ONT.hasNodeURL, hasNodeURL);
			tm.addTripleLiteral(Node, PingER_ONT.hasComments, hasComments);
			tm.addTripleLiteral(Node, PingER_ONT.hasContactInformation, hasContactInformation);
			tm.addTripleLiteral(Node, PingER_ONT.hasLocationDescription, hasLocationDescription);
			tm.addTripleLiteral(Node, PingER_ONT.hasProjectType, hasProjectType);
			tm.addTripleLiteral(Node, PingER_ONT.hasTraceServer, hasTraceServer);
			tm.addTripleLiteral(Node, PingER_ONT.hasGMTOffset, hasGMTOffset);
			tm.addTripleLiteral(Node, PingER_ONT.hasPingServer, hasPingServer);
			tm.addTripleLiteral(Node, PingER_ONT.hasDataServer, hasDataServer);
			tm.addTripleLiteral(Node, PingER_ONT.hasAppUser, hasAppUser);
			

			double PingERLat = Double.parseDouble(bindingSet.getValue("PingERLat").stringValue());
			double PingERLong = Double.parseDouble(bindingSet.getValue("PingERLong").stringValue());
			String PingERRegionalGroup = bindingSet.getValue("PingERRegionalGroup").stringValue();
			
			tm.addTripleLiteral(Node, PingER_ONT.PingERLat, PingERLat);
			tm.addTripleLiteral(Node, PingER_ONT.PingERLong, PingERLong);
			tm.addTripleLiteral(Node, PingER_ONT.PingERRegionalGroup, PingERRegionalGroup);
			
			tm.writeTriplesAndClean();
		}
		SPARQLQuery.closeConnection();
		
	}

	
}
