package runners.errand.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import runners.errand.utils.BitmapUtils;

public class User {
	public static final int
		STATUS_NOT_RUNNING = 0,
		STATUS_RUNNING = 1;

	private int id, status, benefitRequirements;
	private String firstName, lastName, email, phone;
	private String picture_b64;
	private Bitmap picture_bmp;
	private float rating, minRating, maxDistance, benefitDiscount;
	private ArrayList<Achievement> achievements = new ArrayList<>();
	private ArrayList<Address> addresses = new ArrayList<>();
	private ArrayList<Benefit> benefits = new ArrayList<>();
	private ArrayList<Rating> ratings = new ArrayList<>();
	private ArrayList<Request> requests = new ArrayList<>();
	private ArrayList<Offer> offers = new ArrayList<>();
	private ArrayList<Request> history = new ArrayList<>();
	private ArrayList<Notification> notifications = new ArrayList<>();
	private ArrayList<ServicePrefs> servicePrefs = new ArrayList<>();
	private ArrayList<WorkingHours> workingHours = new ArrayList<>();
	private ArrayList<User> blockedUsers = new ArrayList<>();

	public User(@NonNull JSONObject o) {
		JSONObject user = o.optJSONObject("user");
		if (user != null) {
			this.id = user.optInt("id");
			this.email = user.optString("email");
			this.firstName = user.optString("first_name");
			this.lastName = user.optString("last_name");
			this.phone = user.optString("phone");
			this.rating = (float) user.optDouble("avg_rating");
			this.picture_b64 = user.optString("picture");
			if (picture_b64.contains("data:image/png;base64,")) {
				picture_b64 = picture_b64.substring(22);
			} else {
				picture_bmp = BitmapUtils.decode(picture_b64);
			}
		}

		JSONArray achievements = o.optJSONArray("achievements");
		if (achievements != null) {
			for (int i = 0; i < achievements.length(); i++) {
				JSONObject achievement = achievements.optJSONObject(i);
				if (achievement != null) this.achievements.add(new Achievement(achievement));
			}
		}

		JSONArray addresses = o.optJSONArray("addresses");
		if (addresses != null) {
			for (int i = 0; i < addresses.length(); i++) {
				JSONObject address = addresses.optJSONObject(i);
				if (address != null) this.addresses.add(new Address(address));
			}
		}

		JSONArray benefits = o.optJSONArray("benefitlist");
		if (benefits != null) {
			for (int i = 0; i < benefits.length(); i++) {
				JSONObject benefit = benefits.optJSONObject(i);
				if (benefit != null) this.benefits.add(new Benefit(benefit));
			}
		}

		JSONArray ratings = o.optJSONArray("ratings");
		if (ratings != null) {
			for (int i = 0; i < ratings.length(); i++) {
				JSONObject rating = ratings.optJSONObject(i);
				if (rating != null) this.ratings.add(new Rating(rating));
			}
		}

		JSONArray requests = o.optJSONArray("requests");
		if (requests != null) {
			for (int i = 0; i < requests.length(); i++) {
				JSONObject request = requests.optJSONObject(i);
				if (request != null) this.requests.add(new Request(request));
			}
		}

		JSONArray notifications = o.optJSONArray("notifications");
		if (notifications != null) {
			for (int i = 0; i < notifications.length(); i++) {
				JSONObject notification = notifications.optJSONObject(i);
				if (notification != null) this.notifications.add(new Notification(notification));
			}
		}

		JSONArray services = o.optJSONArray("services");
		if (services != null) {
			for (int i = 0; i < services.length(); i++) {
				JSONObject service = services.optJSONObject(i);
				if (service != null) this.servicePrefs.add(new ServicePrefs(service));
			}
		}

		JSONArray workingHours = o.optJSONArray("working_hours");
		if (workingHours != null) {
			for (int i = 0; i < workingHours.length(); i++) {
				JSONObject workingHour = workingHours.optJSONObject(i);
				if (workingHour != null) this.workingHours.add(new WorkingHours(workingHour));
			}
		}

		JSONArray blockedUsers = o.optJSONArray("blocked");
		if (blockedUsers != null) {
			for (int i = 0; i < blockedUsers.length(); i++) {
				JSONObject blockedUser = blockedUsers.optJSONObject(i);
				if (blockedUser != null) {
					JSONObject tmp = new JSONObject();
					try {
						tmp.put("user", blockedUser);
						this.blockedUsers.add(new User(tmp));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}

		this.benefitDiscount = (float) o.optDouble("benefit_discount");
		this.benefitRequirements = o.optInt("benefit_requirement");
	}

	public int getId() {
		return id;
	}

	public int getStatus() {
		return status;
	}

	public float getRating() {
		return rating;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getName() {
		return firstName + " " + lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public ArrayList<Address> getAddresses() {
		return addresses;
	}

	public ArrayList<Achievement> getAchievements() {
		return achievements;
	}

	public ArrayList<Rating> getRatings() {
		return ratings;
	}

	public ArrayList<Request> getRequests() {
		return requests;
	}

	public Request getRequest(int id) {
		for (Request r : requests)
			if (r.getId() == id)
				return r;

		return null;
	}

	public ArrayList<Notification> getNotifications() {
		return notifications;
	}

	public String getPicture_b64() {
		return picture_b64;
	}

	public Bitmap getPicture_bmp() {
		return picture_bmp;
	}

	public float getMinRating() {
		return minRating;
	}

	public float getMaxDistance() {
		return maxDistance;
	}

	public int getBenefitRequirements() {
		return benefitRequirements;
	}

	public float getBenefitDiscount() {
		return benefitDiscount;
	}

	public ArrayList<ServicePrefs> getServicePrefs() {
		return servicePrefs;
	}

	public ArrayList<WorkingHours> getWorkingHours() {
		return workingHours;
	}

	public ArrayList<User> getBlockedUsers() {
		return blockedUsers;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPicture_b64(String picture_b64) {
		this.picture_b64 = picture_b64;
	}

	public void setPicture_bmp(Bitmap picture_bmp) {
		this.picture_bmp = picture_bmp;
	}

	public void setAddresses(ArrayList<Address> addresses) {
		this.addresses = addresses;
	}
}
