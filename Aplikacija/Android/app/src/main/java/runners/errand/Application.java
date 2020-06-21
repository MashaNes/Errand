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

// 	- Request:
//		- Upload pictures
//	- Register required fields ?
//	- Check required fields on create ?
// 	- Unregister firebase token on logout ?
//	- Achievements ?
//	- Calculate payment based on kilometers traveled and time spent
// 	- Make notifications open all the stuff, send nav id and tab index in bundle extra thing

// TODO API:

//	- Search requests:
//		- No route by distance matrix in api = don't return it
//		- Should not return ones that user has already sent an offer to or have status > 0
//		- Not getting any broadcast requests, maybe distance calc problem, direct request has max int distance even tho the lat and lng values seem correct
//	- Filter users:
//		- Return distance from requests addresses
//	- Notifications:
//		- Direct request has request_id instead of request_name.
//	- Request:
//		- Start request (Runner has to manually start request changing status to active because of distance and time measuring for per payment types), accepting an offer still sets working_with and accepted_offer, also starting location needs to be set with this endpoint
//		- Change location_status to int array for tracking order of tasks with location completed for per km payment type (for per hour user existing time field that isn't used anymore)
// 	- Add estimated time to complete to offers?
