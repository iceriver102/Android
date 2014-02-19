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
import com.google.GCM.CommonUtilities;
import com.google.GCM.ServerUtilities;
import com.google.GCM.WakeLocker;
import com.google.android.gcm.GCMRegistrar;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
			regId = GCMRegistrar.getRegistrationId(this);

		} else {

			final Context context = this;
			mRegisterTask = new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					// Register on our server
					// On server creates a new user
					//ServerUtilities.register(context, "", "", regId);
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					mRegisterTask = null;
				}

			};
			mRegisterTask.execute(null, null, null);

		}
		final Button btn_login = (Button) findViewById(R.id.btnlogin);
		btn_login.setOnClickListener(this);
		mEdit_User = (EditText) findViewById(R.id.txtUser);
		mEdit_Pass = (EditText) findViewById(R.id.txtPass);
		GCMIntentService.flag = CommonUtilities.flag_login = false;
	}

	public boolean checkNetwork() {
		try {
			ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo nf = cn.getActiveNetworkInfo();
			if (nf != null && nf.isConnected() == true) {
				// Toast.makeText(this, "Network Available",
				// Toast.LENGTH_LONG).show();
				Log.i("check net", "Network Available");
				return true;
			} else {
				// Toast.makeText(this, "Network Not Available",
				// Toast.LENGTH_LONG)
				// .show();
				Log.i("check net", "Network not Available");
				return false;

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return true;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.btnlogin:
			String username = mEdit_User.getText().toString().trim();
			String pass = mEdit_Pass.getText().toString().trim();
			if (username.equals("")) {
				Log.e("validate", "username empty");
				Toast.makeText(this,
						this.getText(R.string.Err_validate_Username),
						Toast.LENGTH_LONG).show();
				return;
			}

			if (pass.equals("")) {
				Log.e("validate", "mật khẩu rỗng");
				Toast.makeText(this,
						this.getText(R.string.Err_validate_password),
						Toast.LENGTH_LONG).show();
				return;
			}
			ServerUtilities.network_check = checkNetwork();
			if (ServerUtilities.network_check) {
				if (regId.equals("")) {
					GCMRegistrar.register(this, SENDER_ID);
					regId = GCMRegistrar.getRegistrationId(this);
					Log.d("register", "start register");
				}
				if (regId.equals("")) {
					Toast.makeText(this, this.getString(R.string.Err_101),
							Toast.LENGTH_LONG).show();
					// regId = GCMRegistrar.getRegistrationId(this);
					// Log.e("try again Reg Id",regId);
				} else {
					String jsonString = ServerUtilities.login(username, pass,
							this.regId);
					userDataJson userJson = new userDataJson(jsonString,
							username, pass);
					if (userJson.result) {
						MySQLiteHelper db = new MySQLiteHelper(this);
						db.addUser(userJson);
						Log.i("dang nhap", userJson.toString());
						GCMIntentService.flag = CommonUtilities.flag_login = true;
						Intent i = new Intent(MainActivity.this,
								ListView_Reminder.class);
						startActivity(i);
						ListView_Reminder.regID = regId;
						this.finish();
					} else {
						Toast.makeText(this,
								this.getString(R.string.Err_login),
								Toast.LENGTH_LONG).show();
						Log.e("dang nhap ERR", userJson.toString());
					}
				}
			} else {
				Toast.makeText(this, "Không có kết nối internet!",
						Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}

	}

	public final BroadcastReceiver mHandleMessageReceiver = new GcmBroadcastReceiver() {
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
			WakeLocker.release();
		}

	};

	public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			ComponentName comp = new ComponentName(context.getPackageName(),
					GCMIntentService.class.getName());
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
			if (CommonUtilities.flag_login)
				GCMRegistrar.unregister(this);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

}
