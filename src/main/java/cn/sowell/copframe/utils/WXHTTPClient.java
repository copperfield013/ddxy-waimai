package cn.sowell.copframe.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class WXHTTPClient {
	
	public static String request(String urlStr,String method, String outputStr) {
		StringBuilder temp = new StringBuilder("{");
		InputStream in = null;
		InputStreamReader isr = null;
		BufferedReader rd = null;
		OutputStream os = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(method);
			conn.setDoOutput(true);
			if(outputStr!=null) {
				os = conn.getOutputStream();
				os.write(outputStr.getBytes("UTF-8"));
			}
			conn.getOutputStream().flush();
			conn.getOutputStream().close();
			in = conn.getInputStream();
			isr = new InputStreamReader(in,"UTF-8"); 
			rd = new BufferedReader(isr);
			while(rd.read()!=-1){
			    temp.append(rd.readLine());
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(in!=null) {
					in.close();
				}
				if(isr!=null) {
					isr.close();
				}
				if(rd!=null) {
					rd.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return temp.toString();
	}

}
