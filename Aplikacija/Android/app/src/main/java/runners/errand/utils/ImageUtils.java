package runners.errand.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.VectorDrawable;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.common.util.IOUtils;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import runners.errand.R;

public class ImageUtils {
	public static final int MARKER_ICON_RED = 0;
	public static final int MARKER_ICON_GREEN = 1;

	public static String encode(InputStream input) {
		Bitmap bitmap = BitmapFactory.decodeStream(input);
		bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500 * bitmap.getHeight() / bitmap.getWidth(), true);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
		return new String(Base64.encode(stream.toByteArray(), Base64.NO_WRAP));
	}

	public static Bitmap decode(String b64) {
		byte[] b = Base64.decode(b64, Base64.NO_WRAP);
		return BitmapFactory.decodeByteArray(b, 0, b.length);
	}

	public static String encodeBig(InputStream input) {
		Bitmap bitmap = BitmapFactory.decodeStream(input);
		bitmap = Bitmap.createScaledBitmap(bitmap, 1080, 1080 * bitmap.getHeight() / bitmap.getWidth(), true);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);
		return new String(Base64.encode(stream.toByteArray(), Base64.NO_WRAP));
	}

	public static BitmapDescriptor getMapMarkerBitmap(Context context, float density, int icon) {
		int res;
		if (icon == MARKER_ICON_GREEN) {
			res = R.drawable.img_pin_complete;
		} else {
			res = R.drawable.img_pin;
		}
		return BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), res), (int) Math.ceil(.7 * 43 * density), (int) Math.ceil(43 * density), false));
	}
}
