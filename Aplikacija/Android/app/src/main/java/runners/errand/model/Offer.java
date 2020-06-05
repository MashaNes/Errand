package runners.errand.model;

import androidx.annotation.NonNull;

import org.json.JSONObject;

public class Offer {
	private static final int
			PAYMENT_TYPE_FIXED = 0,
			PAYMENT_TYPE_PKM = 1,
			PAYMENT_TYPE_PH = 2,
			PAYMENT_TYPE_FIXED_PLUS = 3;

	private int id, createdBy, paymentType, request, paymentAmount;
	private Edit edit;

	public Offer(@NonNull JSONObject o) {
		this.id = o.optInt("id");
		this.createdBy = o.optInt("created_by");
		this.paymentType = o.optInt("payment_type");
		this.request = o.optInt("payment_ammount");
		this.paymentAmount = o.optInt("request");
		JSONObject edit = o.optJSONObject("edit");
		if (edit != null) this.edit = new Edit(edit);
	}

	public int getId() {
		return id;
	}

	public int getCreatedBy() {
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
