package runners.errand.model;

import android.media.Image;

// firstName, lastName and picture are part of the User model in the DB
// where createdBy represents that users id,
// however, they are in this class because the whole User object won't be needed most of the time
// and it would be better get them as part of the API call fetching the Rating data.
public class Rating {
	private int id, grade, ratingUser, ratedUser, request;
	private String comment, firstName, lastName;
	private Image picture;

	public Rating(int id, int grade, int ratingUser, int ratedUser, int request, String comment, String firstName, String lastName, Image picture) {
		this.id = id;
		this.grade = grade;
		this.ratingUser = ratingUser;
		this.ratedUser = ratedUser;
		this.request = request;
		this.comment = comment;
		this.firstName = firstName;
		this.lastName = lastName;
		this.picture = picture;
	}

	public int getId() {
		return id;
	}

	public int getGrade() {
		return grade;
	}

	public int getRatingUser() {
		return ratingUser;
	}

	public int getRatedUser() {
		return ratedUser;
	}

	public int getRequest() {
		return request;
	}

	public String getComment() {
		return comment;
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

	public Image getPicture() {
		return picture;
	}
}
