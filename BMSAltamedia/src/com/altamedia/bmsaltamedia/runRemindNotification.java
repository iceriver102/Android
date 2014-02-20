package com.altamedia.bmsaltamedia;

import java.util.Date;

import com.altamedia.sqllite.MySQLiteHelper;
import com.altamedia.bmsaltamedia.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;


public class runRemindNotification extends Service {
	private static final String TAG = "httpSevicePush";
	private static int NOTIFY_ME_ID = 1337;
	private Thread thread;
	private boolean flag;
	private static int time = 3600000*3;
	Context context;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		context = this;	

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
	@SuppressWarnings("deprecation")
	@Override
	public void onDestroy() {
		try {
			if (thread.isAlive())
				thread.stop();
		} catch (Exception ex) {
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
						Thread.sleep(runRemindNotification.time);
						runNotifycation();
						
					}
				} catch (Exception e) {

				}
			}
		});
		try {
			thread.start();
		} catch (Exception ex) {

		}

	}

	public static void setTime(int time) {
		if (time > 0)
			runRemindNotification.time = time;
	}

	@SuppressWarnings("deprecation")
	public void createNotification(Context context, String title, String msg) {

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher,
				this.getString(R.string.app_name), System.currentTimeMillis());
		// Hide the notification after its selected
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		Intent intent = new Intent(context, SplashScreen.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);
		notification.setLatestEventInfo(context, title, msg, pendingIntent);
		notification.defaults |= Notification.DEFAULT_SOUND;

		// Vibrate if vibrate is enabled
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(NOTIFY_ME_ID, notification);

	}

	public void runNotifycation() {
		int num = countRemind();
		if (num>0) {
			try {
				this.createNotification(context, this.getString(R.string.work_wait_title),"Bạn có "+num+" công việc chưa hoàn thành");
			} catch (Exception ex) {

			}
		}
	}

	public int countRemind() {
		MySQLiteHelper db= new MySQLiteHelper(context);
		int num=db.CountReminder(new Date());	
		return num;
		
	}

}
