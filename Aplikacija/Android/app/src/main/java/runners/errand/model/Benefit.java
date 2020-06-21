package runners.errand.model;

import androidx.annotation.NonNull;

import org.json.JSONObject;

public class Benefit {
	private int id;
	private User user;
	private double discount;

	public Benefit(@NonNull JSONObject o) {
		this.id = o.optInt("id");
		this.discount = o.optDouble("discount");
		JSONObject user = o.optJSONObject("benefit_user");
		if (user != null) this.user = new User(user);
	}

	public Benefit(int id, User user, double discount) {
		this.id = id;
		this.user = user;
		this.discount = discount;
	}

	public int getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public void setId(int id) {
		this.id = id;
	}
}
