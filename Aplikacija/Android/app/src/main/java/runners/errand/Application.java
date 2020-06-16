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
//	- Profile dialog
//		- Show info - DONE (Needs more customisation for buttons)
//		- Block user
//		- Report user
//	- Requests
//		- Refresh on pull down - DONE
//		- Refresh on load if bundle "refresh" is true - DONE?
//	- Create request
//		- On address long click, show full address name in a toast (same as in Profile) - DONE
//		- Add destination address (optional?) - DONE
//		- Broadcast - DONE
//		- Add direct request - DONE
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
//		- Don't repeat services if multiple tasks have same service - DONE

// TODO Location Service:

//	- Location service on active ??? Maybe not needed, since we're using geofencing for request locations, maybe for distance matrix server can request location through Firebase??

// TODO Links:

//	- Showing notifications https://developer.android.com/training/notify-user/build-notification
//	- Firebase https://firebase.google.com/docs/android/setup
//	- Doze https://developer.android.com/training/monitoring-device-state/doze-standby

// TODO API:

// 	- Filter request by distance & rating
//	- Distance matrix
//	- Update just benefits requirement & discount from user info
//	- Notifications endpoints????
