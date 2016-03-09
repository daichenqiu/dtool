package org.dcq.crawl.model;  
  
public interface LinkFilter {  
    public boolean accept(String url);  
}  