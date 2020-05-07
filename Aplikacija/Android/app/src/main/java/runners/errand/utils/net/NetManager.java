package runners.errand.utils.net;

import android.Manifest;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import runners.errand.BuildConfig;
import runners.errand.R;
import runners.errand.utils.PreferenceManager;

public class NetManager extends AsyncTask<NetRequest, String, NetRequest> {
	private static String API_SERVER_DEBUG = null;
	private static String API_SERVER_RELEASE = null;
	private static String TOKEN = null;

	private static final String API_BASE = "api/v1/";

	public static final String API_AUTH = API_BASE + "login/";
	public static final String API_USERS = API_BASE + "users/";
	public static final String API_USERS_INFO = API_BASE + "users_info/";
	public static final String API_USER_CREATE = API_BASE + "user_create/";
	public static final String API_USER_UPDATE = API_BASE + "user_update/";
	public static final String API_REQUESTS = API_BASE + "requests/";
	public static final String API_REQUESTS_INFO = API_BASE + "requests_info/";
	public static final String API_REQUEST_CREATE = API_BASE + "request_create/";
	public static final String API_ADDRESS_ADD = API_BASE + "address_add/";
	public static final String API_ADDRESS_UPDATE = API_BASE + "address_update/";
	public static final String API_ADDRESS_REMOVE = API_BASE + "address_remove/";

	public static final String POST = "POST";
	public static final String GET = "GET";
	public static final String PUT = "PUT";
	public static final String DELETE = "DELETE";

	private static boolean running = false;

	private static ArrayList<NetRequest> queue = new ArrayList<>();

	public static String getApiServer() {
		if (BuildConfig.DEBUG) {
			return API_SERVER_DEBUG;
		} else {
			return API_SERVER_RELEASE;
		}
	}

	public static void setApiServer(String ip) {
		if (BuildConfig.DEBUG) API_SERVER_DEBUG = "http://" + ip + ":8000/";
	}

	public static void clearToken(Context context) {
		TOKEN = null;
		PreferenceManager.putString(context, PreferenceManager.GROUP_LOGIN, PreferenceManager.KEY_TOKEN, null);
	}

	public static void setToken(String token) {
		TOKEN = token;
	}

	private static boolean isRunning() {
		return running;
	}

	private static void setRunning(boolean running) {
		NetManager.running = running;
		if (!running) next();
	}

	public static void add(NetRequest r) {
		queue.add(r);
		next();
	}

	private static void next() {
		if (!queue.isEmpty() && !isRunning()) {
			setRunning(true);
			new NetManager().execute(queue.get(0));
		}
	}

	private static NetRequest request(NetRequest r) {
		BufferedReader in;
		URL url;
		HttpURLConnection con;
		OutputStreamWriter out;

		try {
			url = new URL(r.getFullUrl());
		} catch (MalformedURLException e) {
			r.setResult(handle(e, null));
			return r;
		}

		try {
			 con = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			r.setResult(handle(e, null));
			return r;
		}

		if (!r.isLogin()) con.setRequestProperty("Authorization", TOKEN);
		con.setRequestProperty("Content-Type", "application/json; charset=utf-8");

		try {
			con.setRequestMethod(r.getType());
		} catch (ProtocolException e) {
			r.setResult(handle(e, con));
			return r;
		}

		if (r.getType().equals(POST) || r.getType().equals(PUT) || r.getType().equals(DELETE)) {
			con.setDoOutput(true);

			try {
				out = new OutputStreamWriter(con.getOutputStream());
				out.write(r.getJsonParams());
				out.flush();
				out.close();
			} catch (IOException e) {
				r.setResult(handle(e, con));
				return r;
			}
		}

		StringBuilder sb = new StringBuilder();
		boolean error = false;

		try {
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			error = true;
		}

		try {
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			in.close();
		} catch (IOException e) {
			r.setResult(handle(e, con));
			return r;
		}

		String response = sb.toString();

		if (error) {
			r.setResult(new NetResult(NetResult.TYPE_ERROR_REMOTE, response));
			return r;
		} else {
			if (r.isLogin()) {
				try {
					JSONObject o = new JSONObject(response);
					String tmp = o.optString("token", "");
					if (tmp.isEmpty()) {
						r.setResult(new NetResult(NetResult.TYPE_ERROR_LOCAL, response));
						return r;
					} else {
						TOKEN = "Token " + tmp;
						PreferenceManager.putString(r.getContext(), PreferenceManager.GROUP_LOGIN, PreferenceManager.KEY_TOKEN, TOKEN);
						response = o.optString("user");
					}
				} catch (JSONException e) {
					e.printStackTrace();
					r.setResult(handle(e, null));
					return r;
				}
			}

			r.setResult(new NetResult(NetResult.TYPE_SUCCESS, response));
			Log.e("NET", "done " + r.getResult().getType() + ": " + r.getResult().getMsg());
			return r;
		}
	}

	private static NetResult handle(Exception e, HttpURLConnection con) {
		if (con != null) con.disconnect();
		e.printStackTrace();
		return new NetResult(NetResult.TYPE_ERROR_LOCAL, e.getClass().getCanonicalName() + ": " + e.getMessage());
	}

	@Override
	protected NetRequest doInBackground(NetRequest... requests) {
		if (isRunning() && requests == null) return null;
		setRunning(true);
		Log.e("NET", "dib " + requests[0].getFullUrl() + "\n" + requests[0].getJsonParams());
		queue.remove(0);
		return request(requests[0]);
	}

	@Override
	protected void onPostExecute(NetRequest request) {
		setRunning(false);

		if (request == null || request.getResult() == null) return;

		if (request.getResult().getType() == NetResult.TYPE_SUCCESS)
			request.success();
		else
			request.error();
	}
}
