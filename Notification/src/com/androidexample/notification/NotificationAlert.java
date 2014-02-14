package com.androidexample.notification;


import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationAlert extends Activity implements OnClickListener {
	boolean flag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_alert);
		this.flag=false;
		final Button notify = (Button) findViewById(R.id.notify);	
		final Button Stop =(Button) findViewById(R.id.btnStop);
		notify.setOnClickListener(this);
		Stop.setOnClickListener(this);
	}
	
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.notify:	
			TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
			Toast.makeText(this, mngr.getDeviceId(), 3).show();
			httpSevicePush.setTime(60000);
			startService(new Intent(this, httpSevicePush.class));			
			break;
		case R.id.btnStop:
			stopService(new Intent(this, httpSevicePush.class));
			break;
		default:
			break;
		}
	}
}
