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

// TODO:

// 	- Request
//		- Show destination - DONE
//	- Settings
//		- Service defaults - DONE
//		- Working hours
//		- Benefits
//	- Profile for other users
//		- Block user
//		- Report user
//	- Requests
//		- Refresh on pull down - DONE
//		- Refresh on load if bundle "refresh" is true - DONE?
//	- Create request
//		- On address long click, show full address name in a toast (same as in Profile) - DONE
//		- Add destination address (optional?) - DONE
//		- Broadcast - DONE
//		- Add direct request
//			- Choose weather to show only active users or not
//		- Check required fields on create
//		- Create request - DONE
//	- Find request
//		- Tab 1: Filter
//		- Tab 2: Broadcast
//		- Tab 3: Direct
//	- Firebase
//	- Redesign request item
//		- Request item for Find requests
//		- Don't repeat services if multiple tasks have same service

// TODO Service:

//	- Location service on active
//	- Bind permanent notification to location service to
//		keep user notified that they are still active and stop
//		Doze from killing off the service
//	- Start service and become active based on working hours using alarms

// TODO Links:

//	- Showing notifications https://developer.android.com/training/notify-user/build-notification
//	- Firebase https://firebase.google.com/docs/android/setup
//	- Doze https://developer.android.com/training/monitoring-device-state/doze-standby

// TODO API:

//	- Request broadcast param
// 	- Direct request in request_create
// 	- Filter request by distance & rating
//	- Filter users by active, distance & rating
//	- Update user status
//	- Update user location
//	- Notifications endpoints
// 	- Firebase for notifications
//	- Report user (needs a timeout so people can't spam report)
//	- Update just benefits requirement & discount from user info