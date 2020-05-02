package runners.errand.model;

import androidx.annotation.NonNull;

import org.json.JSONObject;

public class Address {
	private int id;
	private String name;
	private float lng, lat;
	private boolean home, arrived;

	public Address(@NonNull JSONObject o) {
		this.id = o.optInt("id");
		this.name = o.optString("name");
		this.lng = (float) o.optDouble("longitude");
		this.lat = (float) o.optDouble("latitude");
		this.home = o.optBoolean("home");
		this.arrived = o.optBoolean("arrived");
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public float getLng() {
		return lng;
	}

	public float getLat() {
		return lat;
	}

	public boolean isHome() {
		return home;
	}

	public boolean isArrived() {
		return arrived;
	}
}
