package runners.errand.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.common.util.IOUtils;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import runners.errand.R;

public class BitmapUtils {

	// TODO: encode is for pfp, add stuff for higher quality pics for requests

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

	public static BitmapDescriptor getMapMarkerBitmap(Context context, float density) {
		return BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.img_pin), (int) Math.ceil(.7 * 43 * density), (int) Math.ceil(43 * density), false));
	}
}
