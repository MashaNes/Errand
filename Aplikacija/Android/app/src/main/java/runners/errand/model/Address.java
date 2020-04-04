package runners.errand.model;

public class Address {
	private int id;
	private String name;
	private float lng, lat;
	private boolean home;

	public Address(int id, String name, float lng, float lat) {
		this.id = id;
		this.name = name;
		this.lng = lng;
		this.lat = lat;
	}

	public Address(int id, String name, float lng, float lat, boolean home) {
		this.id = id;
		this.name = name;
		this.lng = lng;
		this.lat = lat;
		this.home = home;
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
}
