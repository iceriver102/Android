package com.example.bmsaltamedia;

import static com.google.GCM.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.google.GCM.CommonUtilities.EXTRA_ACTION;
import static com.google.GCM.CommonUtilities.EXTRA_MESSAGE;
import static com.google.GCM.CommonUtilities.SENDER_ID;

import java.sql.Date;
import java.util.List;

import com.example.Json.userDataJson;
import com.example.appData.userData;
import com.example.data.Validate;
import com.example.http.RequestTask;
import com.example.sqllite.MySQLiteHelper;
import com.google.GCM.ServerUtilities;
import com.google.GCM.WakeLocker;
import com.google.android.gcm.GCMRegistrar;

import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	EditText mEdit_User;
	EditText mEdit_Pass;
	AsyncTask<Void, Void, Void> mRegisterTask;

	public static String newAction = "";
	private String regId;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		// GCMRegistrar.checkManifest(this);
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));
		regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
			GCMRegistrar.register(this, SENDER_ID);
		} else {
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.
				Toast.makeText(getApplicationContext(),
						"Already registered with GCM", Toast.LENGTH_LONG)
						.show();
				Log.i("Main", "Already registered with GCM");
			} else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						// Register on our server
						// On server creates a new user
						ServerUtilities.register(context, "demo", "demo", regId);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}
		//Toast.makeText(this, regId, Toast.LENGTH_LONG).show();
		final Button btn_login = (Button) findViewById(R.id.btnlogin);
		btn_login.setOnClickListener(this);
		mEdit_User = (EditText) findViewById(R.id.txtUser);
		mEdit_Pass = (EditText) findViewById(R.id.txtPass);
		
		//this.deleteDatabase("Alta_BMS");
		/*
		userData user= new userData();
		user.setId(1);
		user.date= new Date(12,12,2014);
		user.fullName="Phan Thanh Giang";
		user.setPass("25.25.1325");
		user.setUserName("giang.phan");
		
		
		MySQLiteHelper db = new MySQLiteHelper(this);
		db.addUser(user);		
		List<userData> tmp=db.getAllUsers();
		Log.i("tmp count",Integer.toString(tmp.size()));
		db.deleteUser(tmp.get(0));*/
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int num=0;
		switch (v.getId()) {
		case R.id.btnlogin:
			if(regId.equals(""))
				Toast.makeText(this, "Req ID blank", Toast.LENGTH_LONG).show();
			else {
				String jsonString=ServerUtilities.login(mEdit_User.getText().toString(), mEdit_Pass.getText().toString(), this.regId);				
				userDataJson userJson= new userDataJson(jsonString, mEdit_User.getText().toString(), mEdit_Pass.getText().toString());
				if(userJson.result){
					MySQLiteHelper db= new MySQLiteHelper(this);
					db.emptyUser();
					db.addUser(userJson);
					Log.i("dang nhap",userJson.toString());
					num=db.countUser();
				}else{
					Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_LONG).show();
					Log.e("dang nhap ERR",userJson.toString());
				}
				Log.d("Check Num user","sl"+Integer.toString(num));
				
			}				//
			break;
		default:
			break;
		}

	}

	private final BroadcastReceiver mHandleMessageReceiver = new GcmBroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			newAction = intent.getExtras().getString(EXTRA_ACTION);

			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());

			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 * */
			if (!context.getPackageName().equalsIgnoreCase(
					((ActivityManager) context
							.getSystemService(Context.ACTIVITY_SERVICE))
							.getRunningTasks(1).get(0).topActivity
							.getPackageName())) {
				// App is not in the foreground
				Toast.makeText(getApplicationContext(),
						"New Message: " + newMessage, Toast.LENGTH_LONG).show();
			}

			// Releasing wake lock
			WakeLocker.release();
		}
	};

	private void submitDataLogin() {
		if (Validate.isEmpty(mEdit_User.getText().toString())) {
			Toast.makeText(this, "Hãy nhập tên đăng nhập", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (Validate.isEmpty(mEdit_Pass.getText().toString())) {
			Toast.makeText(this, "Hãy nhập mật khẩu", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String url = "http://bmsled.altamedia.vn/demo/api.php?mod=ALTA_LOGIN&"
				+ "user=" + mEdit_User.getText().toString() + "&pass="
				+ mEdit_Pass.getText().toString() + "&ime="
				+ mngr.getDeviceId();
		Log.i("url", url);
		try {
			AsyncTask<String, String, String> jsonString = new RequestTask()
					.execute(url);
			Log.i("Json String", jsonString.get());
		} catch (Exception ex) {
			Log.d("ERR", ex.getMessage());
		}

	}

	public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			// Explicitly specify that GcmIntentService will handle the intent.
			ComponentName comp = new ComponentName(context.getPackageName(),
					GCMIntentService.class.getName());
			// Start the service, keeping the device awake while it is
			// launching.
			startWakefulService(context, (intent.setComponent(comp)));
			setResultCode(Activity.RESULT_OK);
		}
	}

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

}
