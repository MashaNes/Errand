package runners.errand.model;

import androidx.annotation.NonNull;

import org.json.JSONObject;

public class Offer {
	private static final int
			PAYMENT_TYPE_FIXED = 0,
			PAYMENT_TYPE_PKM = 1,
			PAYMENT_TYPE_PH = 2,
			PAYMENT_TYPE_FIXED_PLUS = 3;

	private int id, paymentType, request, paymentAmount;
	private User createdBy;
	private Edit edit;

	public Offer(@NonNull JSONObject o) {
		this.id = o.optInt("id");
		JSONObject createdBy = o.optJSONObject("created_by");
		if (createdBy != null) this.createdBy = new User(createdBy);
		this.paymentType = o.optInt("payment_type");
		this.paymentAmount = o.optInt("payment_ammount");
		this.request = o.optInt("request");
		JSONObject edit = o.optJSONObject("edit");
		if (edit != null) this.edit = new Edit(edit);
	}

	public int getId() {
		return id;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public int getPaymentType() {
		return paymentType;
	}

	public int getRequest() {
		return request;
	}

	public int getPaymentAmount() {
		return paymentAmount;
	}
}
