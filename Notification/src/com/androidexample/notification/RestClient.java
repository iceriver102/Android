package com.androidexample.notification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.R;



import android.R.string;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 




import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
 
public class RestClient {
 
	private String result; 
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
    public String getResult(){
    	return this.result;
    }
 
    /* This is a test function which will connects to a given
     * rest service and prints it's response to Android Log with
     * labels "Praeda".
     */
    public void connect(String url)
    {
    	//Context context = (Context) ;
    	
        HttpClient httpclient = new DefaultHttpClient();
 
        // Prepare a request object
        HttpGet httpget = new HttpGet(url); 
 
        // Execute the request
        HttpResponse response;
        try {
            response = httpclient.execute(httpget);
            // Examine the response status
            Log.i("Praeda",response.getStatusLine().toString());
         //   Toast.makeText(context,response.getStatusLine().toString(),10).show();
            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            // If the response does not enclose an entity, there is no need
            // to worry about connection release
 
            if (entity != null) {
 
                // A Simple JSON Response Read
                InputStream instream = entity.getContent();
                result= convertStreamToString(instream);              
                instream.close();
            } 
 
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        	// Toast.makeText(context,e.getMessage(),10).show();
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	//Toast.makeText(context,e.getMessage(),10).show();
            e.printStackTrace();
        }
    }
}