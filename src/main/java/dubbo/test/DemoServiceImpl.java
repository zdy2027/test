package dubbo.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class DemoServiceImpl implements DemoService {

	public long ipToLong(String strIp) {  
        long[] ip = new long[4];  
        //先找到IP地址字符串中.的位置  
        int position1 = strIp.indexOf(".");  
        int position2 = strIp.indexOf(".", position1 + 1);  
        int position3 = strIp.indexOf(".", position2 + 1);  
        //将每个.之间的字符串转换成整型  
        ip[0] = Long.parseLong(strIp.substring(0, position1));  
        ip[1] = Long.parseLong(strIp.substring(position1+1, position2));  
        ip[2] = Long.parseLong(strIp.substring(position2+1, position3));  
        ip[3] = Long.parseLong(strIp.substring(position3+1));  
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];  
    } 
	
	@SuppressWarnings("deprecation")
	public String ip2location(String strIp) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("search ip is === " + strIp);
		long ip = ipToLong(strIp);
		String parameterData = URLEncoder.encode("'q':'end_ip_num:["+ip+" TO *] AND start_ip_num:[* TO "+ip+"]'");
        
        URL localURL = new URL("http://solr.wizeye.com.cn/solr/ip_location_mapper/select?"+parameterData);
        URLConnection connection = localURL.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
        
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
        httpURLConnection.setRequestProperty("Content-Type", "text/xml");
        
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        
        try {
            
            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }
            
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
            
        } finally {         
            
            if (reader != null) {
                reader.close();
            }
            
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            
            if (inputStream != null) {
                inputStream.close();
            }
            
        }

        return resultBuffer.toString();
	}

}
