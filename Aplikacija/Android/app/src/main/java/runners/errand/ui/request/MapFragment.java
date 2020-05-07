package runners.errand.ui.request;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.VectorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import runners.errand.MainActivity;
import runners.errand.R;

public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener {
	private MapView mapView;
	private GoogleMap map;
	private int previousProvider = 0;
	private MainActivity activity;
	private LocationManager locationManager;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_map, container, false);

		RequestFragment parent = ((RequestFragment) getParentFragment());
		if (parent == null) return root;

		activity = parent.getMainActivity();

		mapView = root.findViewById(R.id.request_map_view);
		mapView.onCreate(savedInstanceState);
		mapView.getMapAsync(this);

		locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager != null && activity.permissionsGranted(MainActivity.PERMISSION_LOCATION)) {
			try {
				locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
			} catch (SecurityException e) {
				e.printStackTrace();
				Log.e("LM", "SecurityException");
			}
		} else {
			Log.e("LM", "locationManager is null");
		}

		return root;
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
	}

	@Override
	public void onDetach() {
		locationManager.removeUpdates(this);
		super.onDetach();
	}

	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}

	@Override
	public void onLocationChanged(Location location) {
		int provider = 0;
		switch (location.getProvider()) {
			case LocationManager.PASSIVE_PROVIDER: provider = 1; break;
			case LocationManager.NETWORK_PROVIDER: provider = 2; break;
			case LocationManager.GPS_PROVIDER: provider = 3; break;
		}

		if (provider > previousProvider) {
			LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f));

			MarkerOptions mo = new MarkerOptions();
			mo.position(latLng);

			Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
			try {
				Address address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);
				mo.title(address.getThoroughfare() + ", " + address.getFeatureName());
				mo.snippet(address.getLocality() + ", " + address.getCountryName());
			} catch (IOException e) {
				e.printStackTrace();
				mo.title("Ohno");
			}

			DisplayMetrics metrics = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

			int pinSize = 43;
			mo.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_pin), (int) Math.ceil(.7 * pinSize * metrics.density), (int) Math.ceil(pinSize * metrics.density), false)));
			map.addMarker(mo);
			previousProvider = provider;
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}
}
