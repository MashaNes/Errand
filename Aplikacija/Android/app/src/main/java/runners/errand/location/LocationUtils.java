package runners.errand.location;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LocationUtils {
	public static void becomeActive(Context context, int id) {
		Log.e("LocationUtils", "becomeActive()");
		Intent intent = new Intent(context, LocationService.class);
		intent.putExtra("id", id);
		context.startService(intent);
	}

	public static void becomeInactive(Context context) {
		Log.e("LocationUtils", "becomeInactive()");
		context.stopService(new Intent(context, LocationService.class));
	}
}
