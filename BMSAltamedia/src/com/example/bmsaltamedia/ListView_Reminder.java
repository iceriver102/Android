/**
 * 
 */
package com.example.bmsaltamedia;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.Json.reminderDataJson;
import com.example.appData.reminderArrayAdapter;
import com.example.appData.reminderData;
import com.example.appData.userData;
import com.example.sqllite.MySQLiteHelper;
import com.google.GCM.CommonUtilities;
import com.google.GCM.ServerUtilities;
import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author Giang.Phan
 * 
 */
public class ListView_Reminder extends Activity implements OnClickListener {

	Context context;
	public static userData user;
	public static String regID = "";
	ArrayList<reminderData> arrReminder = new ArrayList<reminderData>();
	public static reminderArrayAdapter adapter = null;
	ListView listView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_view_reminder);
		context = this;
		try {
			MySQLiteHelper dp = new MySQLiteHelper(this);
			List<userData> tmp = dp.getAllUsers();
			user = tmp.get(0);
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
			String jsonStr = ServerUtilities.getRemindData(user.user_id);
			adapter.clear();
			Log.d("Json", jsonStr);
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
								Log.i("Reminder", tmpReminder.toString());
								adapter.add(tmpReminder);
								adapter.notifyDataSetChanged();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}else{
						
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		} else {
			Toast.makeText(this, this.getString(R.string.Err_404), Toast.LENGTH_LONG)
					.show();
		}
	}

	private void applyDemoData(int num) {

		for (int i = 0; i < num; i++) {
			try {
				reminderData tmpReminder = new reminderData(i, "title " + i,
						"content " + i, i % 2, "demo");
				tmpReminder.setDate("22/12/2014", "dd/MM/yyyy");
				this.adapter.add(tmpReminder);
				this.adapter.notifyDataSetChanged();
			} catch (Exception e) {
				Log.e("ERR adapter", e.getMessage());
			}

		}
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
			break;
		default:
			break;
		}

	}

	public void logOut() {
		ServerUtilities.network_check = this.checkNetwork();
		if (ServerUtilities.network_check) {
			boolean flag = ServerUtilities.logOut(user.user_id,
					ListView_Reminder.regID);

		} else {
			// check internet
			Log.i("conecttion", "Không thể kết nối internet");

		}
		GCMRegistrar.unregister(this);
		MySQLiteHelper dp = new MySQLiteHelper(this);
		dp.emptyUser();
		Log.i("Log Out", "Log out user ");
		GCMIntentService.flag = CommonUtilities.flag_login = false;
		// GCMRegistrar.unregister(context);
		Intent i = new Intent(ListView_Reminder.this, MainActivity.class);
		startActivity(i);
		this.finish();

	}

	public void ConfirmDialogLogout() {

		// boolean flag=false;

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

}
