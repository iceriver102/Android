/**
 * 
 */
package com.altamedia.bmsaltamedia;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.altamedia.GCM.CommonUtilities;
import com.altamedia.GCM.ServerUtilities;
import com.altamedia.GCM.WakeLocker;
import com.altamedia.GCM.dataAppSave;
import com.altamedia.Json.reminderDataJson;
import com.altamedia.appData.reminderArrayAdapter;
import com.altamedia.appData.reminderData;
import com.altamedia.appData.userData;
import com.altamedia.sqllite.MySQLiteHelper;
import com.altamedia.bmsaltamedia.R;
import com.google.android.gcm.GCMRegistrar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Giang.Phan
 * 
 */
public class ListView_Reminder extends Activity implements OnClickListener {

	public static Context context;
	public TextView txt_msg;
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;
	public static String newAction = "";

	public static userData user;
	public static String regID = "";
	ArrayList<reminderData> arrReminder = new ArrayList<reminderData>();
	public static reminderArrayAdapter adapter = null;
	ListView listView = null;
	private MySQLiteHelper db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startService(new Intent(this, runRemindNotification.class));
		setContentView(R.layout.main_view_reminder);
		context = this;
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				GCMIntentService.DISPLAY_MESSAGE_ACTION));
		try {
			db = new MySQLiteHelper(this);
			List<userData> tmp = db.getAllUsers();
			user = tmp.get(0);
			dataAppSave.savePreferences(context, "access_token", user.access_token);
			Toast.makeText(context, this.getString(R.string.hello_title)+" "+ user.fullName+"!", Toast.LENGTH_LONG).show();
			Log.i("curent User ", user.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Button btn_logOut = (Button) findViewById(R.id.btn_logOut);
		btn_logOut.setOnClickListener(this);
		Button btn_refresh = (Button) findViewById(R.id.btn_refesh);
		btn_refresh.setOnClickListener(this);
		CommonUtilities.flag_login = GCMIntentService.flag = true;
		
		listView = (ListView) this.findViewById(R.id.ListReminder);
		arrReminder = new ArrayList<reminderData>();
		adapter = new reminderArrayAdapter(this, R.layout.item_listview,
				arrReminder);
		listView.setAdapter(adapter);
		// applyDemoData(10);
		getData();
		loadData();
		dataAppSave.savePreferences(this, "login", "1");
		dataAppSave.savePreferences(this, "user_id",
				String.valueOf(user.user_id));

	}

	public boolean checkNetwork() {
		try {
			ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo nf = cn.getActiveNetworkInfo();
			if (nf != null && nf.isConnected() == true) {

				Log.i("check net", "Network Available");
				return true;
			} else {

				Log.i("check net", "Network not Available");
				return false;

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return true;
		}
	}

	private void getData() {
		ServerUtilities.network_check = this.checkNetwork();
		if (ServerUtilities.network_check) {
			httpGetData();
		} else {
			Toast.makeText(this, this.getString(R.string.Err_404),
					Toast.LENGTH_LONG).show();
		}
	}

	public void httpGetData() {
		// Log.e("static","save data base");
		String jsonStr = ServerUtilities.getRemindData(user.user_id,user.access_token);
		db.emptyReminder();
		if (jsonStr != null) {
			try {
				JSONObject jsonObj = new JSONObject(jsonStr);
				JSONArray reminders = jsonObj.getJSONArray("reminders");
				int length = reminders.length();
				if (length > 0) {
					for (int i = 0; i < length; i++) {
						try {
							JSONObject item = reminders.getJSONObject(i);
							reminderData tmpReminder = new reminderData(
									new reminderDataJson(item));
							db.addReminder(tmpReminder);
							// Log.i("Reminder", tmpReminder.toString());

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {

				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	@SuppressLint("SimpleDateFormat")
	private List<reminderData> Where(Date date, int stt, List<reminderData> list) {
		List<reminderData> listReminder = new ArrayList<reminderData>();
		int size = list.size();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");

		String strDate = formatter.format(date);
		if (stt == 0) {
			for (int i = 0; i < size; i++) {
				reminderData item = list.get(i);
				if (item.getDate("yyyy-MM-dd").equals(strDate)) {
					listReminder.add(item);
				}
			}
		} else if (stt == 1) {
			for (int i = 0; i < size; i++) {
				reminderData item = list.get(i);
				if (item.getDate("yyyy-MM-dd").compareTo(strDate) >= 0) {
					listReminder.add(item);
				}
			}
		} else if (stt == -1) {
			for (int i = 0; i < size; i++) {
				reminderData item = list.get(i);
				if (item.getDate("yyyy-MM-dd").compareTo(strDate) < 0) {
					listReminder.add(item);
				}
			}
		}

		return listReminder;
	}

	public void loadData() {
		List<reminderData> listReminder = db.getAllReminder();
		List<reminderData> last_listReminder;
		// List<reminderData> cur_listReminder;
		List<reminderData> fur_listReminder;
		Date dateNow = new Date();
		Format formatter = new SimpleDateFormat("dd/MM/yyyy");
		String strDate = formatter.format(dateNow);
		last_listReminder = this.Where(dateNow, -1, listReminder);
		// cur_listReminder= this.Where(new Date(), 0, listReminder);
		fur_listReminder = this.Where(dateNow, 1, listReminder);

		Log.e("check", "last:" + last_listReminder.size() + ", fur:"
				+ fur_listReminder.size());

		adapter.clear();
		int size = listReminder.size();
		if (size > 0) {
			int lastSize = last_listReminder.size();
			if (lastSize > 0) {
				reminderData title = new reminderData(
						this.getString(R.string.title_group_last)
								+ " "
								+ last_listReminder.get(0)
										.getDate("dd/MM/yyyy"), this
								.getResources().getColor(
										R.color.color_last_remender));
				adapter.addSeparatorItem(title);
				for (int i = 0; i < lastSize; i++) {
					reminderData tmp = last_listReminder.get(i);
					adapter.add(tmp);
				}

			}
			/*
			 * int cur_size= cur_listReminder.size(); if(cur_size>0){
			 * reminderData title= new
			 * reminderData("Công việc hôm nay",this.getResources
			 * ().getColor(R.color.color_curent));
			 * adapter.addSeparatorItem(title); for(int i=0;i<cur_size;i++){
			 * reminderData tmp= cur_listReminder.get(i); adapter.add(tmp); }
			 * 
			 * }
			 */
			int fur_size = fur_listReminder.size();
			if (fur_size > 0) {
				reminderData title = new reminderData(
						this.getString(R.string.title_group_next) + " "
								+ strDate, this.getResources().getColor(
								R.color.color_upcomming));
				adapter.addSeparatorItem(title);
				for (int i = 0; i < fur_size; i++) {
					reminderData tmp = fur_listReminder.get(i);
					adapter.add(tmp);
				}

			}
		}else{
			Toast.makeText(context, this.getString(R.string.empty_reminder), Toast.LENGTH_LONG).show();
			Log.d("Reminder","không có công viêc");
		}
		adapter.notifyDataSetChanged();
		Log.e("layout", "reload layout");
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.btn_logOut:
			ConfirmDialogLogout();
			break;
		case R.id.btn_refesh:
			this.getData();
			this.loadData();
			break;
		default:
			break;
		}
	}

	public void logOut() {
		int mode_flag = -1;
		ServerUtilities.network_check = this.checkNetwork();
		stopService(new Intent(this, runRemindNotification.class));
		if (ServerUtilities.network_check) {
			boolean flag = ServerUtilities.logOut(user.user_id,
					ListView_Reminder.regID,user.access_token);
			if (flag) {
				mode_flag = 0;
				dataAppSave.savePreferences(context, "access_token","");
				Log.i("Log Out", "Log out user thanh cong");
				db.emptyUser();
				db.emptyReminder();
			}

		} else {
			// check internet
			mode_flag = 1;
			dataAppSave.savePreferences(context, "log_out_id",
					String.valueOf(user.user_id));
			dataAppSave.savePreferences(context, "reg_id", regID);
			startService(new Intent(this, LogoutService.class));
			Log.e("conecttion", "Không thể kết nối internet");
			Log.i("Log Out", "Log out user offline");
		}
		if (mode_flag != -1) {			
			dataAppSave.savePreferences(this, "login", "0");
			dataAppSave.savePreferences(this, "user_id", "");
			GCMRegistrar.unregister(this);
			GCMIntentService.flag = CommonUtilities.flag_login = false;
			// GCMRegistrar.unregister(context);
			Intent i = new Intent(ListView_Reminder.this, MainActivity.class);
			startActivity(i);
			this.finish();
		}else{
			Toast.makeText(context, "ERR 101: Bạn không thể đăng xuất", Toast.LENGTH_LONG).show();
			Log.e("Err logout","user_id"+user.user_id+", regid:"+regID);
		}
	}

	public void ConfirmDialogLogout() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
		builder1.setMessage(this.getString(R.string.confirm_logout));
		builder1.setCancelable(true);
		builder1.setIcon(R.drawable.ic_log_out);
		builder1.setTitle(this.getString(R.string.title_dialog_logout));

		builder1.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						logOut();
						// flag=true;
					}
				});
		builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();

				// flag=false;
			}
		});

		AlertDialog alert11 = builder1.create();
		alert11.show();

		// return true;
	}

	/**
	 * Receiving push messages
	 * */

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

	private final BroadcastReceiver mHandleMessageReceiver = new GcmBroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			WakeLocker.acquire(getApplicationContext());
			if (ListView_Reminder.this != null) {
				ListView_Reminder.this.httpGetData();
				Log.i("updateSql","update database");
			}

			// Showing received message
			// lblMessage.append(newMessage + "\n");
			if (!context.getPackageName().equalsIgnoreCase(
					((ActivityManager) context
							.getSystemService(Context.ACTIVITY_SERVICE))
							.getRunningTasks(1).get(0).topActivity
							.getPackageName())) {
				// App is not in the foreground
				httpGetData();Log.i("updateSql","update database");
				//loadData();Log.i("view","update view");

			} else {
				loadData();
				Log.i("view","update view 1");
			}

			// Releasing wake lock
			WakeLocker.release();
		}
	};

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			// GCMRegistrar.unregister(this);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

}
