package com.example.bmsaltamedia;


import com.google.GCM.dataAppSave;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {
	// Splash screen timer
	private static int SPLASH_TIME_OUT = 3000;
	public static boolean flag=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flash_layout);
		String login=dataAppSave.loadSavedPreferences(this,
				"login");
		if(!login.equals(""))
			flag = (Integer.parseInt(login) == 1);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(SplashScreen.flag){
					Intent i = new Intent(SplashScreen.this, ListView_Reminder.class);
					startActivity(i);
				}else{				
					Intent i = new Intent(SplashScreen.this, MainActivity.class);
					startActivity(i);
				}
				finish();
			}
		}, SPLASH_TIME_OUT);
	}

}