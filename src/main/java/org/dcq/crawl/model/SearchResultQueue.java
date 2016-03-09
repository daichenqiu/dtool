package org.dcq.crawl.model;

import org.apache.commons.lang3.StringUtils;

public class SearchResultQueue {

	private static Queue queue = new Queue();
	
	
	public static void addSearchResult(String url){
		if(StringUtils.isNotBlank(url) 
				&& !queue.contains(url)){
			queue.enQueue(url);
		}
	}
}
