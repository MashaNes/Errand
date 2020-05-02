package runners.errand.model;

import androidx.annotation.NonNull;

import org.json.JSONObject;

public class Benefit {
	private int id, benefitUser;
	private float discount;

	public Benefit(@NonNull JSONObject o) {

	}

	public Benefit(int id, int benefitUser, float discount) {
		this.id = id;
		this.benefitUser = benefitUser;
		this.discount = discount;
	}

	public int getId() {
		return id;
	}

	public int getBenefitUser() {
		return benefitUser;
	}

	public float getDiscount() {
		return discount;
	}
}
