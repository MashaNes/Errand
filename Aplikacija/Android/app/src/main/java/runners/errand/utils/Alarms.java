package runners.errand.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import runners.errand.model.WorkingHours;
import runners.errand.services.LocationService;

public class Alarms {
	private static final int ID_BASE = 100000;

	public static void add(Context context, WorkingHours hours) {
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		if (manager == null) return;

		int day = hours.getDay() + 2;
		if (day > 7) day = 1;

		Calendar calFrom = Calendar.getInstance();
		calFrom.set(Calendar.DAY_OF_WEEK, day);
		calFrom.set(Calendar.HOUR_OF_DAY, hours.getFromHours());
		calFrom.set(Calendar.MINUTE, hours.getFromMinutes());
		calFrom.set(Calendar.SECOND, 0);

		Calendar calUntil = Calendar.getInstance();
		calUntil.set(Calendar.DAY_OF_WEEK, day);
		calUntil.set(Calendar.HOUR_OF_DAY, hours.getUntilHours());
		calUntil.set(Calendar.MINUTE, hours.getUntilMinutes());
		calUntil.set(Calendar.SECOND, 0);

		if (calFrom.getTimeInMillis() < System.currentTimeMillis()) {
			calFrom.add(Calendar.WEEK_OF_YEAR, 1);
			calUntil.add(Calendar.WEEK_OF_YEAR, 1);
		}

		Intent intentStart = new Intent(context, LocationService.class);
		PendingIntent pendingStart = PendingIntent.getService(context, ID_BASE + hours.getId(), intentStart, PendingIntent.FLAG_ONE_SHOT);

		Intent intentStop = new Intent(context, LocationService.class);
		intentStop.setAction(LocationService.ACTION_STOP_SERVICE);
		PendingIntent pendingStop = PendingIntent.getService(context, ID_BASE + hours.getId(), intentStop, PendingIntent.FLAG_ONE_SHOT);

		manager.setRepeating(AlarmManager.RTC_WAKEUP, calFrom.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingStart);
		manager.setRepeating(AlarmManager.RTC_WAKEUP, calUntil.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingStop);

		Log.e("ALARM A", "id: " + hours.getId());
	}

	public static void remove(Context context, WorkingHours hours) {
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		if (manager == null) return;

		Intent intentStart = new Intent(context, LocationService.class);
		PendingIntent pendingStart = PendingIntent.getService(context, ID_BASE + hours.getId(), intentStart, PendingIntent.FLAG_ONE_SHOT);

		Intent intentStop = new Intent(context, LocationService.class);
		intentStop.setAction(LocationService.ACTION_STOP_SERVICE);
		PendingIntent pendingStop = PendingIntent.getService(context, ID_BASE + hours.getId(), intentStop, PendingIntent.FLAG_ONE_SHOT);

		manager.cancel(pendingStart);
		manager.cancel(pendingStop);

		Log.e("ALARM R", "id: " + hours.getId());
	}
}
