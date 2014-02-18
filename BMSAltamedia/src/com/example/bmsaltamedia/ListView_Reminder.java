/**
 * 
 */
package com.example.bmsaltamedia;

import java.util.ArrayList;
import java.util.List;

import com.example.appData.reminderArrayAdapter;
import com.example.appData.reminderData;
import com.example.appData.userData;
import com.example.sqllite.MySQLiteHelper;
import com.google.GCM.CommonUtilities;
import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

/**
 * @author Giang.Phan
 * 
 */
public class ListView_Reminder extends Activity implements OnClickListener {

	Context context;
	userData user;

	ArrayList<reminderData> arrReminder = new ArrayList<reminderData>();
	reminderArrayAdapter adapter = null;
	ListView listView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_view_reminder);
		context = this;
		MySQLiteHelper dp = new MySQLiteHelper(this);
		List<userData> tmp = dp.getAllUsers();
		user = tmp.get(0);
		Button btn_logOut = (Button) findViewById(R.id.btn_logOut);
		btn_logOut.setOnClickListener(this);
		Log.i("curent User ", user.toString());
		CommonUtilities.flag_login = true;

		listView = (ListView) this.findViewById(R.id.ListReminder);
		arrReminder = new ArrayList<reminderData>();
		adapter = new reminderArrayAdapter(this, R.layout.item_listview,
				arrReminder);
		listView.setAdapter(adapter);		
		applyDemoData(100);
	}

	private void applyDemoData(int num) {

		for (int i = 0; i < num; i++) {
			try {
				reminderData tmpReminder = new reminderData(i, "title " + i,
						"content " + i, i % 2, "demo");
				tmpReminder.setDate("22/12/2014", "dd/MM/yyyy");
				//Log.i("Item", tmpReminder.toString());
				
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
			MySQLiteHelper dp = new MySQLiteHelper(this);
			dp.emptyUser();
			Log.i("Log Out", "Log out user ");
			CommonUtilities.flag_login = false;
			GCMRegistrar.unregister(context);
			Intent i = new Intent(ListView_Reminder.this, MainActivity.class);
			startActivity(i);
			this.finish();
			break;
		default:
			break;
		}

	}

}
