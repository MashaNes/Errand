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
