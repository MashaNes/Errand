package runners.errand.model;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Notification {
	public static final int CATEGORY_REQUEST_DIRECT = 0; // Running
	public static final int CATEGORY_REQUEST_FAILED = 1; // Other
	public static final int CATEGORY_OFFER_CREATED = 2; // Requested
	public static final int CATEGORY_OFFER_ACCEPTED = 3; // Running
	public static final int CATEGORY_OFFER_CANCELED = 4; // Running
	public static final int CATEGORY_EDIT_CREATED = 5; // Requested
	public static final int CATEGORY_EDIT_ACCEPTED = 6; // Running
	public static final int CATEGORY_EDIT_CANCELED = 7; // Running
	public static final int CATEGORY_RATING = 8; // Other
	public static final int CATEGORY_ACHIEVEMENT = 9; // Other
	public static final int CATEGORY_REQUEST_SUCCESS = 10; // Other

	private int id = 0, type_id, category;
	private String title, body;
	private Date time;

	public Notification(@NonNull JSONObject o) {
		id = o.optInt("id");
		type_id = o.optInt("type_id");
		category = o.optInt("notification_type");
		if (Locale.getDefault().getLanguage().equals("sr")) {
			title = o.optString("title_sr");
			body = o.optString("body_sr");
		} else {
			title = o.optString("title_en");
			body = o.optString("body_en");
		}

		try {
			this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(o.optString("timestamp").replace("T", " ").substring(0, 19));
		} catch (ParseException e) {
			e.printStackTrace();
			this.time = null;
		}
	}

	public Notification(int id, int type_id, int category, String title, String body, String time) {
		this.id = id;
		this.type_id = type_id;
		this.category = category;
		this.title = title;
		this.body = body;
		try {
			this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			this.time = null;
		}
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

	public int getType_id() {
		return type_id;
	}
}
