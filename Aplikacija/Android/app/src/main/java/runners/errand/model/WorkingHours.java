package runners.errand.model;

import androidx.annotation.NonNull;

import org.json.JSONObject;

public class WorkingHours {
	private int id;
	private boolean day;
	private String from, until;

	public WorkingHours(@NonNull JSONObject o) {
		this.id = o.optInt("id");
		this.day = o.optBoolean("day");
		this.from = o.optString("work_from");
		this.until = o.optString("work_until");
	}

	public int getId() {
		return id;
	}

	public boolean isDay() {
		return day;
	}

	public String getFrom() {
		return from;
	}

	public String getUntil() {
		return until;
	}
}
