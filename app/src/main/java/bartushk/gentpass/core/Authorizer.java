package bartushk.gentpass.core;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import bartushk.gentpass.data.User;
import bartushk.gentpass.io.Reader;

/**
 * Name : ForumMenu.java <br>
 * Author : Kyle Bartush<br>
 * Version : 1.0<br>
 * Description : Contains static functions used to authorize certain user
 * information such as whether a user is valid or if a user already exists.
 */
public class Authorizer {

	/**
	 * Validates a user login attempt by reading from the user file saved on the
	 * android SD card.
	 * 
	 * @param user
	 *            - The user object to be tested for validation.
	 * @return Boolean of validation pass/fail.
	 */
	public static boolean validateUser(User user) {
		try {
			Reader read = new Reader();

			// Uses the reader class to return a
			// json array of all users.
			JSONArray jray = read.getUsersJSON();

			// Iterates through each Json object
			// Each object contains the username and
			// it's corresponding channel.
			for (int i = 0; i < jray.size(); i++) {
				// If the username and challenge from the json array
				// match that from the passed user object, return true.
				if (((JSONObject) jray.get(i)).get("user").equals(
						user.getUsername())
						&& ((JSONObject) jray.get(i)).get("challenge").equals(
								user.getChallenge())) {
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Checks if a user already exists within the user file saved on the android
	 * SD card.
	 * 
	 * @param user
	 *            - The user object to be tested for duplicates.
	 * @return Boolean of whether the user previously existed.
	 */
	public static boolean userAlreadyExists(User user) {
		try {
			Reader read = new Reader();
			// Uses the reader class to return a
			// json array of all users.
			JSONArray jray = read.getUsersJSON();

			// Iterate through all the users returned.
			for (int i = 0; i < jray.size(); i++) {
				// If a username match occurs return true.
				if (((JSONObject) jray.get(i)).get("user").equals(
						user.getUsername())) {
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

}
