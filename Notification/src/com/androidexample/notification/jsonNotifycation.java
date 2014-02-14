package com.androidexample.notification;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class jsonNotifycation extends Activity {
	private static int NOTIFY_ME_ID = 1337;
	private JSONObject json;
	public String msg;
	public String title;
	public boolean result;
	private String Err = null;

	public jsonNotifycation() {
		super();
		this.result = false;
		title = "";
		msg = "";
	}

	public String getErr() {
		String tmp = this.Err;
		this.Err = "";
		return tmp;
	}

	public boolean parseJson(String jsonString) {
		try {
			this.json = new JSONObject(jsonString);
			msg = json.getString("msg");
			title = json.getString("title");
			result = json.getBoolean("result");
			this.Err = "";
			return true;
		} catch (JSONException ex) {
			this.Err = ex.getMessage();
			return false;
		} catch (Exception ex) {
			this.Err = ex.getMessage();
			return false;
		}
	}

	
	@SuppressWarnings("deprecation")
	public void createNotification(Context context) {
		if (this.result) {
			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notification = new Notification(
					R.drawable.stat_notify_chat, "Thông báo mới",
					System.currentTimeMillis());
			// Hide the notification after its selected
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			Intent intent = new Intent(context, NotifyMessage.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					intent, PendingIntent.FLAG_CANCEL_CURRENT);
			notification.setLatestEventInfo(context, title, msg, pendingIntent);
			
			notificationManager.notify(NOTIFY_ME_ID, notification);
			jsonNotifycation.NOTIFY_ME_ID++;
		}

	}

	public void createNotification(Runnable runnable) {
		// TODO Auto-generated method stub

	}

}
