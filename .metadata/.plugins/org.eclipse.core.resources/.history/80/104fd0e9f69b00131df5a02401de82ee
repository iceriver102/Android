package com.google.GCM;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class dataAppSave {

	public static String sharePreferencesKey = "altakey";

	public static String loadSavedPreferences(Context context, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				sharePreferencesKey, Context.MODE_PRIVATE);
		String value = sharedPreferences.getString(key, "");
		return value;
	}

	public static boolean savePreferences(Context context, String[] key,
			String[] value) {
		boolean result = true;
		try {
			SharedPreferences sharedPreferences = context.getSharedPreferences(
					sharePreferencesKey, Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();
			for (int i = 0; i < key.length; i++) {
				if (!value[i].trim().equals(""))
					editor.putString(key[i], value[i]);
				else
					return false;
			}
			editor.commit();
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	public static boolean savePreferences(Context context, String key,
			String value) {
		boolean result = true;
		try {
			SharedPreferences sharedPreferences = context.getSharedPreferences(
					sharePreferencesKey, Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();

			if (!value.trim().equals(""))
				editor.putString(key, value);
			else
				return false;

			editor.commit();
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

}
