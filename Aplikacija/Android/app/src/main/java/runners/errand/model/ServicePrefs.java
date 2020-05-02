package runners.errand.model;

import androidx.annotation.NonNull;

import org.json.JSONObject;

public class ServicePrefs {
	public static final String
			PAYMENT_TYPE_FIXED = "fix";

	private Service service;
	private int id;
	private String paymentType;
	private double maxDistance, paymentAmount, minRating;

	public ServicePrefs(@NonNull JSONObject o) {
		this.id = o.optInt("id");

		JSONObject service = o.optJSONObject("service");
		if (service != null) this.service = new Service(service);

		this.maxDistance = o.optDouble("max_dist");
		this.paymentType = o.optString("payment_type");
		this.paymentAmount = o.optDouble("payment_ammount");
		this.minRating = o.optDouble("min_rating");
	}

	public Service getService() {
		return service;
	}

	public int getId() {
		return id;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public double getMaxDistance() {
		return maxDistance;
	}

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public double getMinRating() {
		return minRating;
	}
}
