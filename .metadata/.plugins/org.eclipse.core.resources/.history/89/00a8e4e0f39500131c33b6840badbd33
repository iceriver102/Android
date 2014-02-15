package com.example.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.util.Log;

public class httpPost {
	  private String result;
	  public String getStringJson(){
		  return this.result;
	  }
	  public JSONObject getJSON(){
		  try{
			  return new JSONObject(this.result);
		  }catch(Exception ex){
			  return null;
		  }
	  }
	  private static String convertStreamToString(InputStream is) {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	        StringBuilder sb = new StringBuilder();	 
	        String line = null;
	        try {
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                is.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return sb.toString();
	    }	
	 public void connect(String url)
	    {	
	        try {
	            HttpClient httpclient = new DefaultHttpClient();
		        HttpGet httpget = new HttpGet(url); 
		        HttpResponse response;
	            response = httpclient.execute(httpget);	           
	            HttpEntity entity = response.getEntity();          
	            if (entity != null) {
	                InputStream instream = entity.getContent();
	                result= convertStreamToString(instream);              
	                instream.close();
	            } 
	        } catch (ClientProtocolException e) {
	            Log.i("ERR", e.getMessage());
	        } catch (IOException e) {
	        	Log.i("ERR", e.getMessage());
	        }
	    }
}
