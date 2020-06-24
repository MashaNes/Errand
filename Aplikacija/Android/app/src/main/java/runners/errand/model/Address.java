package runners.errand.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class Address {
	private int id;
	private String name;
	private float lng, lat;
	private boolean home = false, arrived = false;

	public Address() {}

	public Address(String name, float lat, float lng) {
		this.name = name;
		this.lat = lat;
		this.lng = lng;
	}

	public Address(Address address) {
		this.id = address.getId();
		this.name = address.getName();
		this.lng = address.getLng();
		this.lat = address.getLat();
		this.home = address.isHome();
		this.arrived = address.isArrived();
	}

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

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setHome(boolean home) {
		this.home = home;
	}

	public void copy(Address address) {
		this.name = address.getName();
		this.lat = address.getLat();
		this.lng = address.getLng();
	}

	@NonNull
	public Address clone() {
		return new Address(this);
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		Address comp = ((Address) obj);
		return
				comp != null &&
				comp.getLat() == lat &&
				comp.getLng() == lng &&
				comp.getName().equals(name) &&
				comp.isHome() == home &&
				comp.isArrived() == arrived;
	}

	public JSONObject toJSON() {
		JSONObject o = new JSONObject();
		try {
			o.put("name", name);
			o.put("latitude", lat);
			o.put("longitude", lng);
			o.put("home", home);
			o.put("arrived", arrived);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return o;
	}
}
