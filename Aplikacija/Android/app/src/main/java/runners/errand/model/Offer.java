package runners.errand.model;

public class Offer {
	private static final int
			PAYMENT_TYPE_FIXED = 0,
			PAYMENT_TYPE_PKM = 1,
			PAYMENT_TYPE_PH = 2,
			PAYMENT_TYPE_FIXED_PLUS = 3;

	private int id, paymentType, request;
	private float paymentAmount;

	public Offer(int id, int paymentType, int request, float paymentAmount) {
		this.id = id;
		this.paymentType = paymentType;
		this.request = request;
		this.paymentAmount = paymentAmount;
	}

	public int getId() {
		return id;
	}

	public int getPaymentType() {
		return paymentType;
	}

	public int getRequest() {
		return request;
	}

	public float getPaymentAmount() {
		return paymentAmount;
	}
}
