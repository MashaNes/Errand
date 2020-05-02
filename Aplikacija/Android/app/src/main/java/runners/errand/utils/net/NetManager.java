package runners.errand.utils.net;

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

public class NetManager extends AsyncTask<NetRequest, String, NetRequest> {
	private static String API_SERVER = "http://10.42.0.1:8000/", TOKEN = null;

	public static final String
			API_AUTH = API_SERVER + "api/auth/token/login",
			API_BASE = API_SERVER + "api/v1/",
			API_USERS = API_BASE + "users/";

	public static final String
			POST = "POST",
			GET = "GET";

	private static boolean running = false;

	private static ArrayList<NetRequest> queue = new ArrayList<>();

	public static void setApiServer(String ip) {
		API_SERVER = ip;
	}

	public static void clearToken() { TOKEN = null; }

	public static boolean isRunning() {
		return running;
	}

	public static void setRunning(boolean running) {
		NetManager.running = running;
		next();
	}

	public static void add(NetRequest r) {
		queue.add(r);
		next();
	}

	private static void next() {
		if (!queue.isEmpty() && !isRunning()) new NetManager().execute(queue.get(0));
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

		try {
			con.setRequestMethod(r.getType());
		} catch (ProtocolException e) {
			r.setResult(handle(e, con));
			return r;
		}

		if (r.getType().equals(POST)) {
			con.setDoOutput(true);

			try {
				out = new OutputStreamWriter(con.getOutputStream());
				out.write(r.getEncodedParams());
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

		Log.e("ASDASDASD", response);

		if (error) {
			r.setResult(new NetResult(NetResult.TYPE_ERROR_REMOTE, response));
			return r;
		} else {
			if (r.isLogin()) {
				try {
					JSONObject o = new JSONObject(response);
					String tmp = o.optString("auth_token", "");
					if (tmp.isEmpty()) {
						r.setResult(new NetResult(NetResult.TYPE_ERROR_LOCAL, "Response doesn't contain required token."));
						return r;
					} else {
						TOKEN = "Token " + tmp;
					}
				} catch (JSONException e) {
					e.printStackTrace();
					r.setResult(handle(e, null));
					return r;
				}
			}

			r.setResult(new NetResult(NetResult.TYPE_SUCCESS, response));
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
		setRunning(true);
		return request(requests[0]);
	}

	@Override
	protected void onPostExecute(NetRequest request) {
		queue.remove(request);
		setRunning(false);

		if (request.getResult().getType() == NetResult.TYPE_SUCCESS)
			request.success();
		else
			request.error();
	}
}
