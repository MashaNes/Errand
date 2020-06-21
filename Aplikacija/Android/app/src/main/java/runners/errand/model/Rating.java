package runners.errand.model;

import android.media.Image;

import androidx.annotation.NonNull;

import org.json.JSONObject;

public class Rating {
	private int id, grade, ratedUser, request;
	private String comment;
	private User user;

	public Rating(@NonNull JSONObject o) {
		this.id = o.optInt("id");
		this.grade = (int) o.optDouble("grade");
		JSONObject user = o.optJSONObject("created_by");
		if (user != null) this.user = new User(user);
		this.comment = o.optString("comment");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getRatedUser() {
		return ratedUser;
	}

	public void setRatedUser(int ratedUser) {
		this.ratedUser = ratedUser;
	}

	public int getRequest() {
		return request;
	}

	public void setRequest(int request) {
		this.request = request;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
