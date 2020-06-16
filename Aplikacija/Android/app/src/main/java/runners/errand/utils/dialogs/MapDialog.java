package runners.errand.utils.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import runners.errand.MainActivity;
import runners.errand.R;
import runners.errand.model.Address;
import runners.errand.utils.BitmapUtils;
import runners.errand.utils.LoadingDrawable;

public class MapDialog extends AlertDialog implements OnMapReadyCallback, LocationListener, DialogInterface.OnDismissListener {
	private AlertDialog dialog;
	private Address address;
	private TextView tvAddress, tvLabel;
	private MapView mapView;
	private GoogleMap map;
	private MainActivity activity;
	private Marker marker = null;
	private LocationManager locationManager;
	private LocationListener locationListener;
	private FloatingActionButton fab;
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			geocode(tvAddress.getText().toString());
		}
	};
	private boolean isRunning = false, userSet = false;

	protected MapDialog(MainActivity activity, Address a) {
		super(activity);
		View dialogView = View.inflate(activity, R.layout.dialog_map, null);

		this.address = a;
		this.dialog = this;
		this.locationListener = this;

		dialogView.findViewById(R.id.dialog_button_positive).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				positive(dialog);
			}
		});

		dialogView.findViewById(R.id.dialog_button_negative).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				negative(dialog);
			}
		});

		this.tvLabel = dialogView.findViewById(R.id.dialog_map_address_label);
		this.tvAddress = dialogView.findViewById(R.id.dialog_map_address_et);
		this.tvAddress.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				handler.removeCallbacks(runnable);
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (userSet && (address == null || !s.toString().equals(address.getName()))) {
					handler.postDelayed(runnable, 1000);
				}
				userSet = true;
			}
		});

		this.mapView = dialogView.findViewById(R.id.dialog_map);
		mapView.onCreate(null);
		mapView.getMapAsync(this);
		mapView.onResume();

		fab = dialogView.findViewById(R.id.dialog_map_search_name);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getLocation();
			}
		});

		this.activity = activity;
		this.setView(dialogView);

		setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				if (locationManager != null) locationManager.removeUpdates(locationListener);
				handler.removeCallbacks(runnable);
				runnable = null;
				handler = null;
				isRunning = true;
			}
		});
	}

	private String buildAddressName(android.location.Address a) {
		StringBuilder sb = new StringBuilder();
		if (a.getThoroughfare() != null) {
			sb.append(a.getThoroughfare());
		}
		if (a.getFeatureName() != null) {
			if (!a.getFeatureName().equals(a.getThoroughfare())) {
				if (!sb.toString().isEmpty()) {
					sb.append(" ");
				}
				sb.append(a.getFeatureName());
			}
		}
		if (a.getLocality() != null) {
			if (!sb.toString().isEmpty()) {
				sb.append(", ");
			}
			sb.append(a.getLocality());
		}
		if (a.getCountryName() != null) {
			if (!sb.toString().isEmpty()) {
				sb.append(", ");
			}
			sb.append(a.getCountryName());
		}

		return sb.toString();
	}

	private void updateMarker(boolean zoom) {
		if (marker == null) {
			MarkerOptions mo = new MarkerOptions();
			mo.position(new LatLng(0, 0));
			mo.title(address.getName());
			DisplayMetrics metrics = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
			mo.icon(BitmapUtils.getMapMarkerBitmap(activity, metrics.density, BitmapUtils.MARKER_ICON_RED));
			mo.draggable(true);
			marker = map.addMarker(mo);
		}

		LatLng latLng = new LatLng(address.getLat(), address.getLng());
		marker.setPosition(latLng);
		marker.setTitle(address.getName());
		tvAddress.setText(address.getName());
		if (zoom) map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15f));
	}

	private void getLocation() {
		if (locationManager == null) locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
		if (activity.permissionsGranted(MainActivity.PERMISSION_LOCATION)) {
			if (locationManager != null) {
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P || locationManager.isLocationEnabled()) {
					String provider = locationManager.getBestProvider(new Criteria(), true);
					if (provider != null && (provider.equals(LocationManager.GPS_PROVIDER) || provider.equals(LocationManager.NETWORK_PROVIDER))) {
						try {
							locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);
							fab.setImageDrawable(LoadingDrawable.get(activity, R.color.colorWhite));
							return;
						} catch (SecurityException e) {
							e.printStackTrace();
						}
					}
				}
			}
			Toast.makeText(activity, activity.getString(R.string.error_location_required), Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(activity, activity.getString(R.string.permissions_location_map_dialog), Toast.LENGTH_LONG).show();
		}
	}

	public void positive(AlertDialog dialog) {
		dialog.dismiss();
	}

	public void negative(AlertDialog dialog) {
		dialog.dismiss();
	}

	protected Address getAddress() {
		return address;
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
		map.setBuildingsEnabled(false);
		map.setIndoorEnabled(false);
		map.setTrafficEnabled(false);
		UiSettings ui = map.getUiSettings();
		ui.setCompassEnabled(false);
		ui.setMyLocationButtonEnabled(false);
		ui.setTiltGesturesEnabled(false);
		ui.setRotateGesturesEnabled(false);
		ui.setMapToolbarEnabled(false);

		if (address != null) {
			updateMarker(true);
		}

		map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				return true;
			}
		});

		map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latLng) {
				geocode(latLng.latitude, latLng.longitude, false);
			}
		});

		map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
			@Override
			public void onMarkerDragStart(Marker marker) {

			}

			@Override
			public void onMarkerDrag(Marker marker) {

			}

			@Override
			public void onMarkerDragEnd(Marker marker) {
				geocode(marker.getPosition().latitude, marker.getPosition().longitude, false);
			}
		});
	}

	private void showAddressError() {
		Toast.makeText(activity, activity.getString(R.string.error_address), Toast.LENGTH_LONG).show();
	}

	private void geocodeFailed() {
		showAddressError();
		tvLabel.setBackground(activity.getResources().getDrawable(R.drawable.service_background_red));
	}

	private void geocodeSuccess() {
		tvLabel.setBackground(activity.getResources().getDrawable(R.drawable.service_background));
	}

	private void geocode(double lat, double lng, boolean zoom) {
		if (isRunning) return;
		isRunning = true;
		Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
		try {
			List<android.location.Address> la = geocoder.getFromLocation(lat, lng, 1);
			if (la.size() == 0) {
				geocodeFailed();
				isRunning = false;
				return;
			}
			android.location.Address a = la.get(0);
			address = new Address(buildAddressName(a), (float) lat, (float) lng);
			userSet = false;
			tvAddress.setText(address.getName());
			updateMarker(zoom);
			geocodeSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			showAddressError();
		}
		isRunning = false;
	}

	private void geocode(String name) {
		if (isRunning) return;
		isRunning = true;
		Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
		try {
			List<android.location.Address> la = geocoder.getFromLocationName(name, 1);
			if (la.size() == 0) {
				geocodeFailed();
				isRunning = false;
				if (!name.equals(tvAddress.getText().toString())) {
					geocode(tvAddress.getText().toString());
				}
				return;
			}
			android.location.Address a = la.get(0);
			address = new Address(buildAddressName(a), (float) a.getLatitude(), (float) a.getLongitude());
			userSet = false;
			tvAddress.setText(address.getName());
			updateMarker(true);
			geocodeSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			showAddressError();
		}
		isRunning = false;
	}

	@Override
	public void onLocationChanged(Location location) {
		fab.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_my_location_white));
		geocode(location.getLatitude(), location.getLongitude(), true);
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

	@Override
	public void onDismiss(DialogInterface dialog) {
		mapView.onDestroy();
	}
}
