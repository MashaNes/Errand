package runners.errand.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import runners.errand.LoginActivity;
import runners.errand.R;
import runners.errand.geofencing.GeofencingBroadcastReceiver;
import runners.errand.model.Notification;
import runners.errand.utils.PreferenceManager;

public class MessagingService extends FirebaseMessagingService {
	public final static String ACTION_BROADCAST_NOTIFICATION = "runners.errand.firebase.ACTION_BROADCAST_NOTIFICATION";

	public final static String NOTIFICATION_EXTRA_ID = "runners.errand.firebase.NOTIFICATION_EXTRA_ID";
	public final static String NOTIFICATION_EXTRA_TYPE_ID = "runners.errand.firebase.NOTIFICATION_EXTRA_TYPE_ID";
	public final static String NOTIFICATION_EXTRA_CATEGORY = "runners.errand.firebase.NOTIFICATION_EXTRA_CATEGORY";
	public final static String NOTIFICATION_EXTRA_TITLE_EN = "runners.errand.firebase.NOTIFICATION_EXTRA_TITLE_EN";
	public final static String NOTIFICATION_EXTRA_BODY_EN = "runners.errand.firebase.NOTIFICATION_EXTRA_BODY_EN";
	public final static String NOTIFICATION_EXTRA_TITLE_SR = "runners.errand.firebase.NOTIFICATION_EXTRA_TITLE_SR";
	public final static String NOTIFICATION_EXTRA_BODY_SR = "runners.errand.firebase.NOTIFICATION_EXTRA_BODY_SR";
	public final static String NOTIFICATION_EXTRA_TIME = "runners.errand.firebase.NOTIFICATION_EXTRA_TIME";

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);

		Log.e("FCM", remoteMessage.getData().toString());

		int id = Integer.parseInt(remoteMessage.getData().get("id"));
		int typeId = Integer.parseInt(remoteMessage.getData().get("type_id"));
		int category = Integer.parseInt(remoteMessage.getData().get("notification_type"));

		String title_en = remoteMessage.getData().get("title_en");
		String body_en = remoteMessage.getData().get("body_en");
		String title_sr = remoteMessage.getData().get("title_sr");
		String body_sr = remoteMessage.getData().get("body_sr");

		if (category == Notification.CATEGORY_EDIT_ACCEPTED) {
			// TODO: Api, list of addresses changed with the edit
//			GeofencingBroadcastReceiver.removeGeofence(this, typeId, addressId);
//			GeofencingBroadcastReceiver.addGeofence(this, typeId, addressId, lat, lng);
		} else if (category == Notification.CATEGORY_REQUEST_FAILED || category == Notification.CATEGORY_REQUEST_SUCCESS) {
			GeofencingBroadcastReceiver.removeGeofence(this, typeId);
		}

		Intent intent = new Intent(ACTION_BROADCAST_NOTIFICATION);
		intent.putExtra(NOTIFICATION_EXTRA_ID, id);
		intent.putExtra(NOTIFICATION_EXTRA_TYPE_ID, typeId);
		intent.putExtra(NOTIFICATION_EXTRA_CATEGORY, category);
		intent.putExtra(NOTIFICATION_EXTRA_TITLE_EN, title_en);
		intent.putExtra(NOTIFICATION_EXTRA_BODY_EN, body_en);
		intent.putExtra(NOTIFICATION_EXTRA_TITLE_SR, title_sr);
		intent.putExtra(NOTIFICATION_EXTRA_BODY_SR, body_sr);
		intent.putExtra(NOTIFICATION_EXTRA_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())));
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

		if (Locale.getDefault().getLanguage().equals("sr")) {
			sendNotification(title_sr, body_sr, typeId, id, category);
		} else {
			sendNotification(title_en, body_en, typeId, id, category);
		}
	}

	private void sendNotification(String title, String body, int typeId, int id, int category) {
		if (!pushNotificationEnabled(category)) return;

		Intent intent = new Intent(this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(NOTIFICATION_EXTRA_ID, id);
		intent.putExtra(NOTIFICATION_EXTRA_TYPE_ID, typeId);
		intent.putExtra(NOTIFICATION_EXTRA_CATEGORY, typeId);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 10000 + id, intent, PendingIntent.FLAG_ONE_SHOT);

		String channelId = getString(R.string.default_notification_channel_id);
		Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		long[] vibration = {250, 700, 250, 100, 100, 700};
		NotificationCompat.Builder notificationBuilder =
				new NotificationCompat.Builder(this, channelId)
						.setSmallIcon(R.drawable.ic_run)
						.setContentTitle(title)
						.setContentText(body)
						.setAutoCancel(true)
						.setContentIntent(pendingIntent)
						.setStyle(new NotificationCompat.BigTextStyle().bigText(body))
						.setPriority(NotificationCompat.PRIORITY_MAX)
						.setVibrate(vibration)
						.setLights(Color.RED, 800, 200)
						.setSound(defaultSoundUri);;

		NotificationManager notificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel(channelId,
					getString(R.string.default_notification_channel_name),
					NotificationManager.IMPORTANCE_HIGH);
			assert notificationManager != null;
			notificationManager.createNotificationChannel(channel);
		}

		assert notificationManager != null;
		notificationManager.notify(10000 + id, notificationBuilder.build());
	}

	private boolean pushNotificationEnabled(int category) {
		switch (category) {
			case Notification.CATEGORY_REQUEST_DIRECT:
				return PreferenceManager.getBoolean(this, PreferenceManager.GROUP_SETTINGS, PreferenceManager.KEY_REQUEST_DIRECT);
			case Notification.CATEGORY_REQUEST_FAILED:
				return PreferenceManager.getBoolean(this, PreferenceManager.GROUP_SETTINGS, PreferenceManager.KEY_REQUEST_FAILED);
			case Notification.CATEGORY_OFFER_CREATED:
				return PreferenceManager.getBoolean(this, PreferenceManager.GROUP_SETTINGS, PreferenceManager.KEY_OFFER_CREATED);
			case Notification.CATEGORY_OFFER_ACCEPTED:
				return PreferenceManager.getBoolean(this, PreferenceManager.GROUP_SETTINGS, PreferenceManager.KEY_OFFER_ACCEPTED);
			case Notification.CATEGORY_OFFER_CANCELED:
				return PreferenceManager.getBoolean(this, PreferenceManager.GROUP_SETTINGS, PreferenceManager.KEY_OFFER_CANCELED);
			case Notification.CATEGORY_EDIT_CREATED:
				return PreferenceManager.getBoolean(this, PreferenceManager.GROUP_SETTINGS, PreferenceManager.KEY_EDIT_CREATED);
			case Notification.CATEGORY_EDIT_ACCEPTED:
				return PreferenceManager.getBoolean(this, PreferenceManager.GROUP_SETTINGS, PreferenceManager.KEY_EDIT_ACCEPTED);
			case Notification.CATEGORY_EDIT_CANCELED:
				return PreferenceManager.getBoolean(this, PreferenceManager.GROUP_SETTINGS, PreferenceManager.KEY_EDIT_CANCELED);
			case Notification.CATEGORY_RATING:
				return PreferenceManager.getBoolean(this, PreferenceManager.GROUP_SETTINGS, PreferenceManager.KEY_RATING);
			case Notification.CATEGORY_ACHIEVEMENT:
				return PreferenceManager.getBoolean(this, PreferenceManager.GROUP_SETTINGS, PreferenceManager.KEY_ACHIEVEMENT);
			case Notification.CATEGORY_REQUEST_SUCCESS:
				return PreferenceManager.getBoolean(this, PreferenceManager.GROUP_SETTINGS, PreferenceManager.KEY_REQUEST_SUCCESS);
			default:
				return false;
		}
	}
}
