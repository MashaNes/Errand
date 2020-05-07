package runners.errand.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
	public static final String GROUP_LOGIN = "runners.errand.utils.PreferenceManager.GROUP_LOGIN";
	public static final String KEY_EMAIL = "runners.errand.utils.PreferenceManager.KEY_EMAIL";
	public static final String KEY_TOKEN = "runners.errand.utils.PreferenceManager.KEY_TOKEN";
	public static final String KEY_USER_ID = "runners.errand.utils.PreferenceManager.KEY_USER_ID";

	public static final String GROUP_API = "runners.errand.utils.PreferenceManager.GROUP_API";
	public static final String KEY_SERVER_IP = "runners.errand.utils.PreferenceManager.KEY_SERVER_IP";

	public static void putString(Context context, String group, String key, String value) {
		SharedPreferences.Editor editor = context.getSharedPreferences(group, Context.MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.apply();
	}

	public static void putInt(Context context, String group, String key, int value) {
		SharedPreferences.Editor editor = context.getSharedPreferences(group, Context.MODE_PRIVATE).edit();
		editor.putInt(key, value);
		editor.apply();
	}

	public static String getString(Context context, String group, String key) {
		SharedPreferences prefs = context.getSharedPreferences(group, Context.MODE_PRIVATE);
		return prefs.getString(key, null);
	}

	public static int getInt(Context context, String group, String key) {
		SharedPreferences prefs = context.getSharedPreferences(group, Context.MODE_PRIVATE);
		return prefs.getInt(key, -1);
	}
}
