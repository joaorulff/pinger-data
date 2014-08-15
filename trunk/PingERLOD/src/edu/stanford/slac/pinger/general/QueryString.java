package edu.stanford.slac.pinger.general;

import java.util.ArrayList;
import java.util.Collections;


public class QueryString {

	private ArrayList<String> query;
	
	public QueryString(String... queryLines) {
		this.query = new ArrayList<String>();
		Collections.addAll(this.query, queryLines);
	}
	
	public QueryString(String query, String delimiter) {
		String s[] = query.split(delimiter);
		this.query = new ArrayList<String>();
		Collections.addAll(this.query, s);
	}
	/**
	 * Constructor for QueryString.
	 * @param query - It is assumed that the query parameter is able to be broken into lines, having '\n' as line breaker.
	 */
	public QueryString(String query) {
		String s[] = query.split("\n");
		this.query = new ArrayList<String>();
		Collections.addAll(this.query, s);
	}
	
	public String join(String joiner) {
		String ret = "";
		for (String s : query) {
			ret += s + joiner;
		}
		return ret;	
	}
	public QueryString push(String str) {
		query.add(str);
		return this;
	}
	public QueryString push() {
		query.add("");
		return this;
	}
	public QueryString push(String... queryLines) {
		ArrayList<String> list = new ArrayList<String>();
		Collections.addAll(list, queryLines);
		query.addAll(list);
		return this;
	}
	@Override
	public String toString() {
		return join("\n");
	}
	
	public QueryString addPrefix(String prefixStatement) {
		addOnTop(prefixStatement);
		return this;
	}
	private QueryString addOnTop(String s) {
		ArrayList<String> list = new ArrayList<String>();
		list.add(s);
		list.addAll(query);
		query = list;
		return this;
	}
}
