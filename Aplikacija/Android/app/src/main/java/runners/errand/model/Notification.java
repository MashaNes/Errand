package runners.errand.model;

import java.sql.Date;

public class Notification {
	public static final int
		CATEGORY_REQUEST = 0,
		CATEGORY_RUNNING = 1,
		CATEGORY_RATING = 2,
		CATEGORY_ACHIEVEMENT = 3;

	private int id = 0, category;
	private String title, body;
	private Date time;

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
