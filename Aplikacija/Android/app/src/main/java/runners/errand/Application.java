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
//	- Rating in request third fragment ? api doesn't support
//	- Register required fields ? name, date, 1 task (name & service; if name is not set, set it to service name; if service is not set, set it to other), broadcast enabled or direct user (both works, but at least one)
//	- Check required fields on create ? first name, last name, email, repeat password
//	- Show distance and search requests ?
//	- Achievements, insert condition_number into description, also dialog to show condition numbers per level of achievement when you click on an achievement ?

// 	- Buttons to call and send email from profile dialog
//	- Show "No users" text in new request when there is no users for direct
//	- Notification update achievements, at the same broadcast receiver in MainActivity
//	- Refresh request fragment with changes and notifications
//	- Geofence - On notification: edit accepted = remove and add a new one for given address

// 	Make sure that find requests and direct users in new requests work ?
//	OOM because of pictures ?

// API: search_requests, what do I send for no distance matrix ?

// ValueError: Cannot assign "<FullUser: FullUser object (1)>": "Benefit.benefit_user" must be a "User" instance.
//[26/Jun/2020 09:32:24] "PUT /api/v1/request_finish/ HTTP/1.1" 500 16648
//	Test request cancel???
