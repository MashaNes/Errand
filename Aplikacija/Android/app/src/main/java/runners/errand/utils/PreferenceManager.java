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

	public static final String GROUP_SETTINGS = "runners.errand.utils.PreferenceManager.GROUP_SETTINGS";
	public static final String KEY_REQUEST_DIRECT = "runners.errand.utils.PreferenceManager.KEY_REQUEST_DIRECT";
	public static final String KEY_REQUEST_FAILED = "runners.errand.utils.PreferenceManager.KEY_REQUEST_FAILED";
	public static final String KEY_OFFER_CREATED = "runners.errand.utils.PreferenceManager.KEY_OFFER_CREATED";
	public static final String KEY_OFFER_ACCEPTED = "runners.errand.utils.PreferenceManager.KEY_OFFER_ACCEPTED";
	public static final String KEY_OFFER_CANCELED = "runners.errand.utils.PreferenceManager.KEY_OFFER_CANCELED";
	public static final String KEY_EDIT_CREATED = "runners.errand.utils.PreferenceManager.KEY_EDIT_CREATED";
	public static final String KEY_EDIT_ACCEPTED = "runners.errand.utils.PreferenceManager.KEY_EDIT_ACCEPTED";
	public static final String KEY_EDIT_CANCELED = "runners.errand.utils.PreferenceManager.KEY_EDIT_CANCELED";
	public static final String KEY_RATING = "runners.errand.utils.PreferenceManager.KEY_RATING";
	public static final String KEY_ACHIEVEMENT = "runners.errand.utils.PreferenceManager.KEY_ACHIEVEMENT";
	public static final String KEY_REQUEST_SUCCESS = "runners.errand.utils.PreferenceManager.KEY_REQUEST_SUCCESS";

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

	public static void putBoolean(Context context, String group, String key, boolean value) {
		SharedPreferences.Editor editor = context.getSharedPreferences(group, Context.MODE_PRIVATE).edit();
		editor.putBoolean(key, value);
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

	public static boolean getBoolean(Context context, String group, String key) {
		SharedPreferences prefs = context.getSharedPreferences(group, Context.MODE_PRIVATE);
		return prefs.getBoolean(key, true);
	}
}
