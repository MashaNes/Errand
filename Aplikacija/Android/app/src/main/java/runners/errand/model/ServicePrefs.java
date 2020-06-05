package runners.errand.model;

import android.content.Context;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import runners.errand.R;

public class ServicePrefs {
	private static final int PAYMENT_TYPE_FIXED = 0;
	private static final int PAYMENT_TYPE_PER_HOUR = 1;
	private static final int PAYMENT_TYPE_PER_KM = 2;

	private int id, service, paymentType;
	private double maxDistance, paymentAmount, minRating;

	public ServicePrefs(int service) {
		this.id = -1;
		this.service = service;
		this.paymentType = 0;
	}

	ServicePrefs(@NonNull JSONObject o) {
		this.id = o.optInt("id");

		JSONObject service = o.optJSONObject("service");
		if (service != null) this.service = service.optInt("id");

		this.paymentType = o.optInt("payment_type");
		this.maxDistance = o.optDouble("max_dist");
		this.paymentAmount = o.optDouble("payment_ammount");
		this.minRating = o.optDouble("min_rating");
	}

	public ServicePrefs(int id, int service, int paymentType, double maxDistance, double paymentAmount, double minRating) {
		this.id = id;
		this.service = service;
		this.paymentType = paymentType;
		this.maxDistance = maxDistance;
		this.paymentAmount = paymentAmount;
		this.minRating = minRating;
	}

	public int getId() {
		return id;
	}

	public int getService() {
		return service;
	}

	public int getPaymentType() {
		return paymentType;
	}

	public static String getPaymentTypeString(Context context, int paymentType) {
		switch (paymentType) {
			case PAYMENT_TYPE_FIXED:
				return context.getString(R.string.payment_type_fixed);
			case PAYMENT_TYPE_PER_HOUR:
				return context.getString(R.string.payment_type_per_hour);
			case PAYMENT_TYPE_PER_KM:
				return context.getString(R.string.payment_type_per_kilometer);
			default:
				return context.getString(R.string.payment_type_unknown);
		}
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

	public void setId(int id) {
		this.id = id;
	}

	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}

	public void setMaxDistance(double maxDistance) {
		this.maxDistance = maxDistance;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public void setMinRating(double minRating) {
		this.minRating = minRating;
	}

	public ServicePrefs copy() {
		return new ServicePrefs(id, service, paymentType, maxDistance, paymentAmount, minRating);
	}
}
