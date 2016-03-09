package org.dcq.crawl.util;
import java.io.DataOutputStream;  
import java.io.File;  
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.IOException;  
  




import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;  
import org.apache.commons.httpclient.HttpClient;  
import org.apache.commons.httpclient.HttpException;  
import org.apache.commons.httpclient.HttpStatus;  
import org.apache.commons.httpclient.methods.GetMethod;  
import org.apache.commons.httpclient.params.HttpMethodParams;  
  
  
public class DownloadFile {  
//	final static String basePath = "D:\\tmp\\crawl";
  
    public String getFileNameByUrl(String url, String contentType) {  
        url = url.substring(7);  
        if(contentType.indexOf("html") != -1) {  
            url = url.replaceAll("[//?/:*|<>/\"]", "_") + ".html";  
            return url;  
        } else {  
            return url.replaceAll("[//?/:*|<>/\"]","_") + "." +  
                contentType.substring(contentType.lastIndexOf("/") + 1);  
        }  
    }  
      
    private void saveToLocal(byte[] data, String filePath) {  
            try {  
                DataOutputStream out =   
                    new DataOutputStream(new FileOutputStream(new File(filePath)));  
                for(int i=0; i<data.length; i++) {  
                    out.write(data[i]);  
                }  
                out.flush();  
                out.close();  
            } catch (FileNotFoundException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
    }  
      
    public String downLoadFile(String url) {
  
        String filePath = null;  
        HttpClient httpClient = new HttpClient();  
        //set time out   
        httpClient.getHttpConnectionManager()  
                  .getParams()  
                  .setConnectionTimeout(5000);  
          
        GetMethod getMethod = new GetMethod(url);  
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);  
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,   
                new DefaultHttpMethodRetryHandler()); //retry  
          
        //execute HTTP GET request  
        try {  
            getMethod.addRequestHeader("Content-Type", "text/html; charset=UTF-8");    
              
            int statusCode = httpClient.executeMethod(getMethod);  
            if(statusCode != HttpStatus.SC_OK) {  
                System.out.println("Method failed:" + getMethod.getStatusLine());  
                filePath = null;  
            }  
              
            //execute HTTP content  
            byte[] responseBody = getMethod.getResponseBody();  
//            File file = new File("temp");  
            File file=new File(Constant.BASE_FILE_PATH);
            if(!file.exists()) {  
                file.mkdir();  
            }  
            filePath = Constant.BASE_FILE_PATH + "//" +   
                getFileNameByUrl(url,   
                        getMethod.getResponseHeader("Content-Type").getValue());  
            System.out.println("---------"+url+"-------");  
            saveToLocal(responseBody, filePath);  
        } catch (HttpException e) {  
            System.out.println("Please check you provided http address!");  
        } catch (IOException e) {  
        } catch (RuntimeException e) {  
            System.out.println("error");  
        }finally {  
            getMethod.releaseConnection();  
        }  
        return filePath;  
    }
}  