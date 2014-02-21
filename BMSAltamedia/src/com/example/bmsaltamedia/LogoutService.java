package com.example.bmsaltamedia;

import com.example.sqllite.MySQLiteHelper;
import com.google.GCM.ServerUtilities;
import com.google.GCM.dataAppSave;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

public class LogoutService extends Service {
	public Thread thread;
	Context context;
	private static int time = 3600 * 3;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startid) {
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					while (true) {
						runLogout();
						Thread.sleep(LogoutService.time);
						// runNotifycation();
						

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

	public void runLogout() {
		if (checkNetwork()) {
			String user_id = dataAppSave.loadSavedPreferences(context,
					"log_out_id");
			String reg_id = dataAppSave.loadSavedPreferences(context, "reg_id");
			String acess=dataAppSave.loadSavedPreferences(context, "access_token");
			if (!user_id.equals("") && !reg_id.equals("")) {
				if (ServerUtilities.logOut(Integer.parseInt(user_id), reg_id,acess)) {
					MySQLiteHelper db= new MySQLiteHelper(context);
					try {
						if (thread.isAlive())
							this.thread.stop();
					} catch (Exception ex) {

					} finally {
						dataAppSave.savePreferences(context, "log_out_id", "");
						dataAppSave.savePreferences(context, "reg_id", "");
						dataAppSave.savePreferences(context, "access_token","");
						db.emptyUser();
						db.emptyReminder();
						stopService(new Intent(this, LogoutService.class));						
					}
				} else {

				}
			}
		}
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

	@Override
	public void onDestroy() {
		try {
			if (thread.isAlive())
				thread.stop();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
