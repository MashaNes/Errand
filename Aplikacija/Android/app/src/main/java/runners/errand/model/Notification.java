package runners.errand.model;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.sql.Date;

public class Notification {
	public static final int
			CATEGORY_NEW_RATING = 0,
			CATEGORY_ACHIEVEMENT = 1,
			CATEGORY_OFFER_ACCEPTED = 2,
			CATEGORY_OFFER_DECLINED = 3,
			CATEGORY_OFFER_REQUEST_CANCELED = 4,
			CATEGORY_OFFER_DIRECT_REQUEST = 5,
			CATEGORY_REQUEST_SUCCESS = 6,
			CATEGORY_REQUEST_FAILURE = 7,
			CATEGORY_REQUEST_DIRECT_REJECTED = 8;

	private int id = 0, category;
	private String title, body;
	private Date time;

	public Notification(@NonNull JSONObject o) {

	}

	public Notification(int category, String title, String body, Date time) {
		this.category = category;
		this.title = title;
		this.body = body;
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public int getCategory() {
		return category;
	}

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

	public Date getTime() {
		return time;
	}
}
