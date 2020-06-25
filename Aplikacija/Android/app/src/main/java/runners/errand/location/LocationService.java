package runners.errand.location;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import runners.errand.model.User;
import runners.errand.utils.PreferenceManager;
import runners.errand.utils.Static;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
	public static final String ACTION_BROADCAST_STATUS_CHANGED = "runners.errand.services.ACTION_BROADCAST_STATUS_CHANGED";
	public static final String ACTION_BROADCAST_EXTRA_STATUS = "runners.errand.services.ACTION_BROADCAST_EXTRA_STATUS";
	public static final String ACTION_STOP_SERVICE = "runners.errand.services.ACTION_STOP_SERVICE";

	private LocationService locationService;
	private GoogleApiClient locationClient;
	private LocationRequest locationRequest = new LocationRequest();
	private FusedLocationProviderClient fusedLocationProviderClient;
	private LocationCallback locationCallback = new LocationCallback() {
		@Override
		public void onLocationResult(LocationResult locationResult) {
			super.onLocationResult(locationResult);
			Log.e("SERVICE", "lat: " + locationResult.getLastLocation().getLatitude() + ", lng: " + locationResult.getLastLocation().getLongitude());
			updateLocation(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
		}

		@Override
		public void onLocationAvailability(LocationAvailability locationAvailability) {
			super.onLocationAvailability(locationAvailability);
			Log.e("SERVICE", "available: " + locationAvailability.isLocationAvailable());
			if (!locationAvailability.isLocationAvailable()) updateStatus(User.STATUS_NOT_RUNNING);
		}
	};
	private static int id = -1;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null && intent.getAction() != null && intent.getAction().equals(ACTION_STOP_SERVICE)) {
			updateStatus(User.STATUS_NOT_RUNNING);
			stopSelf();
		} else {
			locationService = this;
			id = PreferenceManager.getInt(this, PreferenceManager.GROUP_LOGIN, PreferenceManager.KEY_USER_ID);

			Log.e("SERVICE", "onStartCommand " + id);

			locationClient = new GoogleApiClient.Builder(this)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.addApi(LocationServices.API)
					.build();

			locationRequest.setInterval(3 * 60 * 1000);
			locationRequest.setFastestInterval(60 * 1000);
			locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
			locationClient.connect();
		}

		return START_STICKY;
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		Log.e("SERVICE", "onBind");
		return null;
	}

	@Override
	public void onDestroy() {
		Log.e("SERVICE", "onDestroy");
		if (fusedLocationProviderClient != null) fusedLocationProviderClient.removeLocationUpdates(locationCallback);
		if (locationClient != null) locationClient.disconnect();
		super.onDestroy();
	}

	private void updateStatus(final int status) {
		NetManager.setToken(PreferenceManager.getString(this, PreferenceManager.GROUP_LOGIN, PreferenceManager.KEY_TOKEN));
		NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_USER_STATUS_UPDATE, NetManager.PUT) {
			@Override
			public void success() {
				try {
					if (new JSONObject(getResult().getMsg()).optString("detail").equals("success")) {
						Intent intent = new Intent(ACTION_BROADCAST_STATUS_CHANGED);
						intent.putExtra(ACTION_BROADCAST_EXTRA_STATUS, status);
						LocalBroadcastManager.getInstance(locationService).sendBroadcast(intent);
						if (status == User.STATUS_NOT_RUNNING) stopSelf();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.success();
			}

			@Override
			public void error() {
				super.error();
			}
		};
		netRequest.putParam("created_by", id);
		netRequest.putParam("status", status);
		NetManager.add(netRequest);
	}

	private void updateLocation(double lat, double lng) {
		NetManager.setToken(PreferenceManager.getString(this, PreferenceManager.GROUP_LOGIN, PreferenceManager.KEY_TOKEN));
		NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_USER_LOCATION_UPDATE, NetManager.PUT) {
			@Override
			public void success() {
				super.success();
			}

			@Override
			public void error() {
				super.error();
			}
		};
		netRequest.putParam("created_by", id);
		netRequest.putParam("latitude", lat);
		netRequest.putParam("longitude", lng);
		Static.lat = lat;
		Static.lng = lng;
		NetManager.add(netRequest);
	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {
		Log.e("SERVICE", "onConnected");
		fusedLocationProviderClient = new FusedLocationProviderClient(this);
		fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
		updateStatus(User.STATUS_RUNNING);
	}

	@Override
	public void onConnectionSuspended(int i) {
		Log.e("SERVICE", "onConnectionSuspended");
		updateStatus(User.STATUS_NOT_RUNNING);
		stopSelf();
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		Log.e("SERVICE", "onConnectionFailed");
		updateStatus(User.STATUS_NOT_RUNNING);
		stopSelf();
	}
}
