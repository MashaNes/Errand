package runners.errand.utils.net;

import androidx.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class NetRequest {
	private String url = "", type = "";
	private Map<String, String> params = new HashMap<>();
	private boolean login = false;
	private NetResult result;

	public void success() {};
	public void error() {};

	public NetRequest(String url, String type) {
		this.url = url;
		this.type = type;
	}

	public boolean isLogin() {
		return login;
	}

	public String getUrl() {
		return url;
	}

	public String getType() {
		return type;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public NetResult getResult() {
		return result;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public void putParam(String key, String value) {
		this.params.put(key, value);
	}

	public void setResult(NetResult result) {
		this.result = result;
	}

	String getEncodedParams() {
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

	String getFullUrl() {
		if (type.equals(NetManager.GET)) return url += getEncodedParams();
		return url;
	}

	@Override
	@NonNull
	public String toString() {
		return "Request{" +
				"url='" + url + '\'' +
				", type='" + type + '\'' +
				", params=" + params.toString() +
				'}';
	}
}
