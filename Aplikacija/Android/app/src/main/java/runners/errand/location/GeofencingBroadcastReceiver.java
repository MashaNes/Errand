package runners.errand.location;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import runners.errand.model.Request;
import runners.errand.model.Task;
import runners.errand.utils.PreferenceManager;
import runners.errand.utils.net.NetManager;
import runners.errand.utils.net.NetRequest;

public class GeofencingBroadcastReceiver extends BroadcastReceiver {
	private static final String GEOFENCE_REQUEST_ID_PREFIX = "runners.errand.geofence-";

	public GeofencingBroadcastReceiver() { }

	@Override
	public void onReceive(final Context context, Intent intent) {
		Log.e("GEOFENCE", "Received broadcast");

		GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
		if (geofencingEvent.hasError()) {
			Log.e("GEOFENCE", GeofenceStatusCodes.getStatusCodeString(geofencingEvent.getErrorCode()));
		}

		int geofenceTransition = geofencingEvent.getGeofenceTransition();

		if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
			for (Geofence geofence : geofencingEvent.getTriggeringGeofences()) {
				Log.e("GEOFENCE", geofence.getRequestId());
				String[] s = geofence.getRequestId().split("-");
				if (s.length != 3) {
					Log.e("GEOFENCE", "RequestID is wrong length.");
					return;
				}
				final int requestId = Integer.parseInt(s[1]);
				final int addressId = Integer.parseInt(s[2]);
				NetManager.setToken(PreferenceManager.getString(context, PreferenceManager.GROUP_LOGIN, PreferenceManager.KEY_TOKEN));
				NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_ARRIVED, NetManager.PUT) {
					@Override
					public void success() {
						super.success();
						removeGeofence(context, requestId, addressId);
					}
				};
				netRequest.putParam("request", requestId);
				netRequest.putParam("address", addressId);
				NetManager.add(netRequest);
			}
		} else {
			Log.e("GEOFENCE", "Geofence type invalid. " + geofenceTransition);
		}
	}

	public static void addGeofence(Context context, int requestId, int addressId, double lat, double lng) {
		GeofencingClient geofencingClient = LocationServices.getGeofencingClient(context);
		List<Geofence> geofenceList = new ArrayList<>();
		geofenceList.add(new Geofence.Builder()
				.setRequestId(GEOFENCE_REQUEST_ID_PREFIX + requestId + "-" + addressId)
				.setCircularRegion(
						lat,
						lng,
						300
				)
				.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
				.setExpirationDuration(Geofence.NEVER_EXPIRE)
				.setNotificationResponsiveness(6 * 60 * 1000)
				.build()
		);
		GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
				.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
				.addGeofences(geofenceList)
				.build();
		PendingIntent geofencingPendingIntent;
		Intent intent = new Intent(context, GeofencingBroadcastReceiver.class);
		geofencingPendingIntent = PendingIntent.getBroadcast(context, addressId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		geofencingClient.addGeofences(geofencingRequest, geofencingPendingIntent).addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
				Log.e("GEOFENCE", "Added.");
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Log.e("GEOFENCE", "Failed to add.");
			}
		});
	}

	public static void removeGeofence(Context context, int requestId, int addressId) {
		GeofencingClient geofencingClient = LocationServices.getGeofencingClient(context);
		List<String> ids = new ArrayList<>();
		ids.add(GEOFENCE_REQUEST_ID_PREFIX + requestId + "-" + addressId);
		geofencingClient.removeGeofences(ids).addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
				Log.e("GEOFENCE", "Removed.");
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Log.e("GEOFENCE", "Failed to remove.");
			}
		});
	}

	public static void removeGeofence(final Context context, final int requestId) {
		NetManager.setToken(PreferenceManager.getString(context, PreferenceManager.GROUP_LOGIN, PreferenceManager.KEY_TOKEN));
		NetRequest netRequest = new NetRequest(NetManager.getApiServer() + NetManager.API_REQUESTS + requestId + "/", NetManager.GET) {
			@Override
			public void success() {
				super.success();
				Request request;
				try {
					JSONObject o = new JSONObject(getResult().getMsg());
					o = o.optJSONObject("request");
					if (o != null)
						request = new Request(o);
					else return;
				} catch (JSONException e) {
					e.printStackTrace();
					return;
				}
				if (request.getDestination() != null) {
					removeGeofence(context, requestId, request.getDestination().getId());
				}
				for (Task task : request.getTasks()) {
					if (task.getAddress() != null) removeGeofence(context, requestId, task.getAddress().getId());
				}
			}
		};
		NetManager.add(netRequest);
	}
}
