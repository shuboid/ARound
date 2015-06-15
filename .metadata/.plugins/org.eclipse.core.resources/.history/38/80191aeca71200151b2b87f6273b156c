package com.theark.emergency;

import java.io.IOException;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.content.Entity;
import android.net.http.AndroidHttpClient;

public class HttpManager {
	
	public static String getData(String url) {
		
		AndroidHttpClient client = AndroidHttpClient.newInstance("AndroidAgent");
		HttpGet request = new HttpGet(url);
		HttpResponse response;
		
		try {
			response = client.execute(request);
			return EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}finally{
			client.close();
		}
		
	}
	
	
}
