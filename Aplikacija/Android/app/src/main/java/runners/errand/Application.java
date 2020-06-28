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

//	Maybe
//	- Rating in request third fragment ? api doesn't support
//	- Show distance and search requests ?
//	- Achievements dialog to show condition numbers per level of achievement when you click on an achievement ?
//	- Remove picture
//	- Trim all strings from forms

// 	Do
//	- Check required fields on register ? first name, last name, email, repeat password

// 	Still API
//	- Geofence - On notification: edit accepted = remove and add a new one for given address

//	Crash: OOM because of pictures ?
//	API: search_requests returns your own requests; filtered in client so no need to fix

