package org.dcq.crawl.util;  
  
import java.net.ProtocolException;
import java.util.HashSet;
import java.util.Set;

import org.dcq.crawl.model.LinkFilter;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
  
public class HtmlParserTool {  
   
	/**
	 * 本方法用于提取某个html文档中内嵌的链接 
	 * @param url
	 * @param filter
	 * @return
	 */
    @SuppressWarnings("serial")  
    public static Set<String> extracLinks(String url, LinkFilter filter) {  
    	System.out.println("分析url节点 : " + url);
        Set<String> links = new HashSet<String> ();  
        try {  
            Parser parser = new Parser(url);  
            parser.setEncoding("UTF-8");  
              
            NodeFilter frameFilter = new NodeFilter() {  
//                @Override  
                public boolean accept(Node node) {  
                    if(node.getText().startsWith("frame src=")) {  
                        return true;  
                    }  
                      
                    return false;  
                }  
            };  
              
            OrFilter linkFilter =   
                new OrFilter(new NodeClassFilter(LinkTag.class), frameFilter);  
            NodeList list = parser.extractAllNodesThatMatch(linkFilter);  
            for(int i=0; i<list.size(); i++) { 
                Node tag = list.elementAt(i);  
                if( tag instanceof LinkTag) { 
                    LinkTag link = (LinkTag) tag;  
                    String linkUrl = link.getLink();  
                    if(filter.accept(url)) {  
                        links.add(linkUrl);  
                    } else {  
                        String frame = tag.getText();  
                        int start  = frame.indexOf("src=");  
                        if( start != -1) {  
                            frame = frame.substring(start);  
                        }  
                        int end = frame.indexOf(" ");  
                        String frameUrl = "";  
                        if(end == -1) {  
                            end = frame.indexOf(">");  
                            if(end-1 > 5) {  
                               frameUrl = frame.substring(5, end - 1);  
                            }  
                        }  
                          
                        if(filter.accept(frameUrl)) {  
                            links.add(frameUrl);  
                        }  
                          
                    }  
                }  
            } 
            
//            for(int i = 0; i<list.size();i++){  
//                Node node = list.elementAt(i);  
//                String linkURL = "";  
//                //如果链接类型为<a />  
//                if(node instanceof LinkTag){  
//                    LinkTag link = (LinkTag)node;  
//                    linkURL= link.getLink();  
//                }else{  
//                    //如果类型为<frame />  
//                    String nodeText = node.getText();  
//                    int beginPosition = nodeText.indexOf("src=");  
//                    nodeText = nodeText.substring(beginPosition);  
//                    int endPosition = nodeText.indexOf(" ");  
//                    if(endPosition == -1){  
//                        endPosition = nodeText.indexOf(">");  
//                    }  
//                    linkURL = nodeText.substring(5, endPosition - 1);  
//                }  
//                //判断是否属于本次搜索范围的url  
//                if(filter.accept(linkURL)){  
//                    links.add(linkURL);  
//                }  
//            }
              
        } catch (ParserException e) {  
//            e.printStackTrace();  
        	System.out.println("ParserException!");  
        } catch (Exception e) {  
            System.out.println("Please check you provided http address!");  
        }     
        return links;  
    }  
}  