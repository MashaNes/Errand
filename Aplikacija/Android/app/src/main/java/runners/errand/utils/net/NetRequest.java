package runners.errand.utils.net;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NetRequest {
	private String url, type;
	private Map<String, String> params = new HashMap<>();
	private boolean login = false;
	private NetResult result;
	private JSONObject json = new JSONObject();
	private Context context;

	public void success() {}
	public void error() {}

	protected NetRequest(String url, String type) {
		this.url = url;
		this.type = type;
	}

	boolean isLogin() {
		return login;
	}

	String getType() {
		return type;
	}

	public NetResult getResult() {
		return result;
	}

	public void setLogin(boolean login, Context context) {
		this.login = login;
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	public void putParam(String key, String value) {
		try {
			this.json.put(key, value == null ? JSONObject.NULL : value);
		} catch (JSONException e) {
			e.printStackTrace();
			try {
				this.json.put(key, JSONObject.NULL);
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
		}

		this.params.put(key, value == null ? "null" : value);
	}

	public void putParam(String key, boolean value) {
		try {
			this.json.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
			try {
				this.json.put(key, JSONObject.NULL);
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
		}
		this.params.put(key, (value ? "true" : "false"));
	}

	public void putParam(String key, int value) {
		try {
			this.json.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
			try {
				this.json.put(key, JSONObject.NULL);
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
		}
		this.params.put(key, String.format(Locale.getDefault(), "%d", value));
	}

	public void putParam(String key, float value) {
		try {
			this.json.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
			try {
				this.json.put(key, JSONObject.NULL);
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
		}
		this.params.put(key, String.format(Locale.getDefault(), "%f", value));
	}

	public void putParam(String key, double value) {
		try {
			this.json.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
			try {
				this.json.put(key, JSONObject.NULL);
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
		}
		this.params.put(key, String.format(Locale.getDefault(), "%f", value));
	}

	public void putParam(String key, JSONObject value) {
		try {
			this.json.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
			try {
				this.json.put(key, JSONObject.NULL);
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void putParam(String key, JSONArray value) {
		try {
			this.json.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
			try {
				this.json.put(key, JSONObject.NULL);
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void putNull(String key) {
		try {
			this.json.put(key, JSONObject.NULL);
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	void setResult(NetResult result) {
		this.result = result;
	}

	private String getGETEncodedParams() {
		StringBuilder s = new StringBuilder();

		for (String key : params.keySet()) {
			String value = null;

			try {
				value = URLEncoder.encode(params.get(key), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if (s.length() > 0) s.append("&");

			s.append(key);
			s.append("=");
			s.append(value);
		}

		return s.toString();
	}

	String getJsonParams() {
		return json.toString();
	}

	String getFullUrl() {
		if (type.equals(NetManager.GET)) return url + getGETEncodedParams();
		return url;
	}
}
