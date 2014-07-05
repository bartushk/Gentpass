package bartushk.gentpass.data;


/**
 * Name : PasswordInfo.java <br>
 * Author : Kyle Bartush<br>
 * Version : 1.0<br>
 * Description : A class used to hold all the information pertaining to a single
 * password entry.
 */
public class PasswordInfo {
	// Strings to hold the password, title, and any notes the
	// user may want to store.
	private String password, title, notes;

	// Constructor that sets the notes blank if none are given.
	public PasswordInfo(String title, String password) {
		this.title = title;
		this.password = password;
		this.notes = "";
	}

	// COnstructor for each field.
	public PasswordInfo(String title, String password, String notes) {
		this.title = title;
		this.password = password;
		this.notes = notes;
	}

	// Getters
	public String getTitle() {
		return this.title;
	}

	public String getPassword() {
		return this.password;
	}

	public String getNotes() {
		return this.notes;
	}

	// Setters

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

}
