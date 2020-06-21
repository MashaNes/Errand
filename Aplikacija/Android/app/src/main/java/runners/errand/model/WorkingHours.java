package runners.errand.model;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.util.Locale;

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

	public WorkingHours(int day) {
		this.day = day;
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

	public void setId(int id) {
		this.id = id;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public void setFrom(int hours, int minutes) {
		from = String.format(Locale.getDefault(), "%02d", hours) + ":" + String.format(Locale.getDefault(), "%02d", minutes);
	}

	public void setUntil(int hours, int minutes) {
		until = String.format(Locale.getDefault(), "%02d", hours) + ":" + String.format(Locale.getDefault(), "%02d", minutes);
	}

	public int getFromHours() {
		return Integer.parseInt(from.split(":")[0]);
	}

	public int getFromMinutes() {
		return Integer.parseInt(from.split(":")[1]);
	}

	public int getUntilHours() {
		return Integer.parseInt(until.split(":")[0]);
	}

	public int getUntilMinutes() {
		return Integer.parseInt(until.split(":")[1]);
	}
}
