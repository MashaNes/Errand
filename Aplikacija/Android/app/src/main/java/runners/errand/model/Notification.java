package runners.errand.model;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Notification {
	public static final int CATEGORY_REQUEST_DIRECT = 0; // Running, request
	public static final int CATEGORY_REQUEST_FAILED = 1; // Other, request
	public static final int CATEGORY_OFFER_CREATED = 2; // Requested, request
	public static final int CATEGORY_OFFER_ACCEPTED = 3; // Running, request
	public static final int CATEGORY_OFFER_CANCELED = 4; // Running, request
	public static final int CATEGORY_EDIT_CREATED = 5; // Requested, request
	public static final int CATEGORY_EDIT_ACCEPTED = 6; // Running, request
	public static final int CATEGORY_EDIT_CANCELED = 7; // Running, request
	public static final int CATEGORY_RATING = 8; // Other, profile > ratings
	public static final int CATEGORY_ACHIEVEMENT = 9; // Other, profile > achievements
	public static final int CATEGORY_REQUEST_SUCCESS = 10; // Other, request

	private int id, type_id, category;
	private String title_en, body_en, title_sr, body_sr;
	private Date time;
	private boolean seen, opened;

	Notification(@NonNull JSONObject o) {
		id = o.optInt("id");
		type_id = o.optInt("type_id");
		category = o.optInt("notification_type");
		title_sr = o.optString("title_sr");
		body_sr = o.optString("body_sr");
		title_en = o.optString("title_en");
		body_en = o.optString("body_en");
		seen = o.optBoolean("seen");
		opened = o.optBoolean("opened");
		try {
			this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(o.optString("timestamp").replace("T", " ").substring(0, 19));
		} catch (ParseException e) {
			e.printStackTrace();
			this.time = null;
		}
	}

	public Notification(int id, int type_id, int category, String title_en, String body_en, String title_sr, String body_sr, String time) {
		this.id = id;
		this.type_id = type_id;
		this.category = category;
		this.title_en = title_en;
		this.body_en = body_en;
		this.title_sr = title_sr;
		this.body_sr = body_sr;
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

	public String getTitle_en() {
		return title_en;
	}

	public void setTitle_en(String title_en) {
		this.title_en = title_en;
	}

	public String getBody_en() {
		return body_en;
	}

	public void setBody_en(String body_en) {
		this.body_en = body_en;
	}

	public String getTitle_sr() {
		return title_sr;
	}

	public void setTitle_sr(String title_sr) {
		this.title_sr = title_sr;
	}

	public String getBody_sr() {
		return body_sr;
	}

	public void setBody_sr(String body_sr) {
		this.body_sr = body_sr;
	}

	public String getTitle() {
		if (Locale.getDefault().getLanguage().equals("sr")) {
			return title_sr;
		} else {
			return title_en;
		}
	}

	public String getBody() {
		if (Locale.getDefault().getLanguage().equals("sr")) {
			return body_sr;
		} else {
			return body_en;
		}
	}

	public Date getTime() {
		return time;
	}

	public int getType_id() {
		return type_id;
	}

	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}
}
