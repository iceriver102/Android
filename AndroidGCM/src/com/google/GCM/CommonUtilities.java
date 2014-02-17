package com.google.GCM;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public final class CommonUtilities {
	// give your server registration url here
	public static final String SERVER_URL = "http://192.168.1.102/gcm/register.php";

	// Google project id
	public static final String SENDER_ID = "737164671250";

	/**
	 * Tag used on log messages.
	 */
	static final String TAG = "AndroidHive GCM";

	public static final String DISPLAY_MESSAGE_ACTION = "com.altamedia.androidgcm.DISPLAY_MESSAGE";

	public static final String EXTRA_MESSAGE = "message";
	public static final String EXTRA_ACTION = "action";
	public static final String EXTRA_regid = "regid";

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
