package com.example.bmsaltamedia;

import com.example.sqllite.MySQLiteHelper;
import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

public class SplashScreen extends Activity {

	// Splash screen timer
	private static int SPLASH_TIME_OUT = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.flash_layout);
		final Context context= this;
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity
				MySQLiteHelper db= new MySQLiteHelper(context);
				if(db.countUser()>0){
					Intent i = new Intent(SplashScreen.this, ListView_Reminder.class);
					startActivity(i);
				}else{				
					Intent i = new Intent(SplashScreen.this, MainActivity.class);
					startActivity(i);
				}
				// close this activity
				finish();
			}
		}, SPLASH_TIME_OUT);
	}

}