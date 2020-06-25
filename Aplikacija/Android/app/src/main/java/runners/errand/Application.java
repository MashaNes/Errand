package runners.errand;

import androidx.multidex.MultiDexApplication;

import runners.errand.utils.PreferenceManager;
import runners.errand.utils.net.NetManager;

public class Application extends MultiDexApplication {
	@Override
	public void onCreate() {
		NetManager.setApiServer(PreferenceManager.getString(getApplicationContext(), PreferenceManager.GROUP_API, PreferenceManager.KEY_SERVER_IP));
		super.onCreate();
	}
}

//	TODO:
//	- Register required fields ? name, date, 1 task (name & service; if name is not set, set it to service name; if service is not set, set it to other), broadcast enabled or direct user (both works, but at least one)
//	- Check required fields on create ? first name, last name, email, repeat password
//	- Achievements ? test
//	- Rating in request third fragment ? api doesn't support
//	- Display benefit discount as % ?
//	- Show "No users" text in new request when there is no users for direct
//	- Achievements, insert condition_number into description, also dialog to show condition numbers per level of achievement when you click on an achievement
//	- Show distance for direct users and search requests
//	- Wrong status layout when request is failed
//	- Notification update achievements, at the same broadcast receiver in MainActivity
//	- Refresh request fragment with changes and notifications
//	- Geofence - On notification: edit accepted = remove and add a new one for given address

// Make sure that find requests and direct users in new requests work

// API: search_requests, what do I send for no distance matrix?
