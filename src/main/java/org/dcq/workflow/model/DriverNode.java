package org.dcq.workflow.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class DriverNode {

	@Override
	public String toString() {
		return "DriverNode [id=" + id + ", results=" + results + "]";
	}

	private String id;
	private Map<String,String> results = new LinkedHashMap<String,String>() ;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void addResults(String key, String value){
		results.put(key, value);
	}

	public Map<String, String> getResults() {
		return results;
	}

	public void setResults(Map<String, String> results) {
		this.results = results;
	}
	
}
