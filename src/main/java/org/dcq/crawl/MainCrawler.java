package org.dcq.crawl;  
  
import java.util.Set;

import org.dcq.crawl.model.LinkFilter;
import org.dcq.crawl.model.LinkQueue;
import org.dcq.crawl.util.Constant;
import org.dcq.crawl.util.HtmlParserTool;
import org.dcq.crawl.util.SearchTool;
  
  
public class MainCrawler {  
	
//	final static String WEB_URL = "http://www.zcib.edu.cn";
  
    private void initCrawlerWithSeeds(String[] seeds) {  
        for(int i=0; i<seeds.length; i++) {  
            LinkQueue.addUnvisitedUrl(seeds[i]);  
        }  
    }  
      
    public void crawling(String[] seeds) { 
        LinkFilter filter = new LinkFilter() { 
            public boolean accept(String url) {  
                if(url.startsWith(Constant.WEB_URL)) {  
                    return true;  
                }  
                return false;  
            }  
        };  
          
        initCrawlerWithSeeds(seeds);  
          
        while(!LinkQueue.unVisitedUrlsEmpty()   
                && LinkQueue.getVisitedUrlNum()<=1000) { 
            String visitUrl = (String) LinkQueue.unVisitedUrlDeQueue();  
            if(visitUrl == null) {  
                continue;  
            }  
//            DownloadFile downLoader = new DownloadFile();  
//            downLoader.downLoadFile(visitUrl); 
            try {
                SearchTool.search(visitUrl, "使徒");
                LinkQueue.addVisitedUrl(visitUrl);  
                  
                Set<String> links =   
                    HtmlParserTool.extracLinks(visitUrl, filter);  
                for(String link:links) {  
                    LinkQueue.addUnvisitedUrl(link);  
                }  
			} catch (Exception e) {
				// TODO: handle exception
			}
        }  
    }  
      
    public static void main(String[] args) {  

        MainCrawler crawler = new MainCrawler();  
        crawler.crawling(new String[]{Constant.WEB_URL});  
        
    }
}  
