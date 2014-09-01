package edu.stanford.slac.pinger.beans;

public class NetworkNodeBean {

	public static final String CSV_HEADER = "#id,node_name,node_ip\n";
	String id, nodeName, nodeIP;
	
	public NetworkNodeBean(String id, String nodeName, String nodeIP) {
		this.id = id;
		this.nodeName = nodeName;
		this.nodeIP = nodeIP;
	}
	
	public String toString(char dmtr) {
		return id + dmtr + nodeName + dmtr + nodeIP + "\n";
	}
}
