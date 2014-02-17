package com.google.GCM;

import com.example.bmsaltamedia.R;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public final class CommonUtilities {
	
	// give your server registration url here
	public static final String SERVER_URL = "http://bmsled.altamedia.vn/demo/api.php";

	// Google project id
	public static final String SENDER_ID = "737164671250";

	/**
	 * Tag used on log messages.
	 */
	static final String TAG = "AndroidHive GCM";

	public static final String DISPLAY_MESSAGE_ACTION = "com.example.bmsaltamedia.DISPLAY_MESSAGE";

	public static final String EXTRA_MESSAGE = "message";
	public static final String EXTRA_ACTION = "action";
	public static final String EXTRA_regid = "regid";
	public static boolean flag_login=false;

	/**
	 * Notifies UI to display a message.
	 * <p>
	 * This method is defined in the common helper because it's used both by the
	 * UI and the background service.
	 * 
	 * @param context
	 *            application's context.
	 * @param message
	 *            message to be displayed.
	 */
	public static void displayMessage(Context context, String message,
			String action) {
		Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);		
		Log.i(TAG, "we get data : " + message);		
		intent.putExtra(EXTRA_MESSAGE, message);
		intent.putExtra(EXTRA_ACTION, action);
		context.sendBroadcast(intent);
	}
}
