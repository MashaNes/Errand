package runners.errand.utils;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

public class LoadingDrawable {

	public static Drawable get(Activity activity, int colorId) {
		CircularProgressDrawable d = new CircularProgressDrawable(activity);
		d.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(activity, colorId), PorterDuff.Mode.SRC));
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		d.setStrokeWidth(3 * metrics.density);
		d.start();
		return d;
	}
}
