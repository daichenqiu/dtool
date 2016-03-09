package org.dcq.crawl.util;
import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.dcq.crawl.model.SearchResultQueue;
  
  
public class SearchTool {  
	
    public static String search(String url , String keywords) throws HttpException {
  
    	System.out.println("在  " + url +" 页面内搜索关键字");
        String filePath = null;  
        HttpClient httpClient = new HttpClient();  
        //set time out   
        httpClient.getHttpConnectionManager()  
                  .getParams()  
                  .setConnectionTimeout(5000);  
          
        GetMethod getMethod = null;
        try {  
        	
            getMethod = new GetMethod(url);  
            getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);  
            getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,   
                    new DefaultHttpMethodRetryHandler()); //retry  
            
          //execute HTTP GET request  
            getMethod.addRequestHeader("Content-Type", "text/html; charset=UTF-8");    
              
            int statusCode = httpClient.executeMethod(getMethod);  
            if(statusCode != HttpStatus.SC_OK) {  
                System.out.println("Method failed:" + getMethod.getStatusLine());  
                filePath = null;  
            }  
              
            //execute HTTP content  
            byte[] responseBody = getMethod.getResponseBody();  
            String contents = new String(responseBody);
            if(contents.indexOf(keywords) == -1){
            	System.out.println("页面 : " + url + " 内不含有关键字 " + keywords);
            }else{
            	SearchResultQueue.addSearchResult(url);
            	System.out.println("页面 : " + url + " 内含有关键字 " + keywords + "第一次出现位置 : " + contents.indexOf(keywords));
            }
        } catch (IllegalArgumentException e) {
        	System.out.println("Url 不正确 " + url );  
        	throw e;
		} catch (HttpException e) {  
            System.out.println("Please check you provided http address!");  
        	throw e;
        } catch (IOException e) {  
        } catch (RuntimeException e) {  
            System.out.println("error");  
        }finally {  
            getMethod.releaseConnection();  
        }  
        return filePath;  
    }
}  