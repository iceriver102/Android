package com.androidexample.notification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class httpSevicePush extends Service {
	private static final String TAG = "httpSevicePush";
	private String url;
	private String result;
	private httpPost http;
	private jsonNotifycation noti;
	private Thread thread;
	private boolean flag;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, "My Service Created", Toast.LENGTH_SHORT).show();
		http = new httpPost();
		this.setUrl("http://bmsled.altamedia.vn/demo/api.php");
		noti = new jsonNotifycation();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onDestroy() {
		Toast.makeText(this, "My Service Stopped", Toast.LENGTH_SHORT).show();
		http = null;
		this.result = null;
		
		try{
			if(thread.isAlive())
				thread.stop();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	public void onStart(Intent intent, int startid) {		
		thread = new Thread(new Runnable() {
			@Override
			public void run() {		
				// TODO Auto-generated method stub
				try {
					while (true) {
						runNotifycation();
						Thread.sleep(5000);					
					}
				} catch (Exception e) {
					
				}
			}
		});
		try{
			thread.start();		
		}catch(Exception ex){
			
		}
	
	}
	public void runNotifycation(){
		flag = getHttpJson();
		if (flag) {
			try {
				boolean flagnoti = noti.parseJson(result);
				if (flagnoti) {
					noti.createNotification(getBaseContext());
				}else{
					
				}
			} catch (Exception ex) {
				
			}
		} else {							
			onDestroy();
		}
	}
	public void setUrl(String url) {
		if (url == null || url == "")
			this.url = "http://bmsled.altamedia.vn/demo/api.php";
		else
			this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public String getStringRepone() {
		return this.result;
	}

	public boolean getHttpJson() {
		// this.setUrl("");
		// Toast.makeText(this, this.url, Toast.LENGTH_LONG).show();
		if (this.url == "")
			return false;
		http.connect(this.url);
		this.result = this.http.getString();
		return true;
	}

}
