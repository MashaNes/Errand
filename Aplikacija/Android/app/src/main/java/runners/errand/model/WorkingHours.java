package runners.errand.model;

import androidx.annotation.NonNull;

import org.json.JSONObject;

public class WorkingHours {
	private int id, day;
	private String from, until;

	WorkingHours(@NonNull JSONObject o) {
		this.id = o.optInt("id");
		this.day = o.optInt("day");
		this.from = o.optString("work_from");
		if (!from.isEmpty()) from = from.substring(0, 5);
		this.until = o.optString("work_until");
		if (!until.isEmpty()) until = until.substring(0, 5);
	}

	public int getId() {
		return id;
	}

	public int getDay() {
		return day;
	}

	public String getFrom() {
		return from;
	}

	public String getUntil() {
		return until;
	}
}
