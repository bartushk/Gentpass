package bartushk.gentpass.io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import bartushk.gentpass.crypto.AESUtils;
import bartushk.gentpass.data.PasswordInfo;
import bartushk.gentpass.data.User;

/**
 * Name : Writer.java <br>
 * Author : Kyle Bartush<br>
 * Version : 1.0<br>
 * Description : Extension of the FileInterface class containing functions
 * specific to writing files to the android SD card.
 */
public class Writer extends FileInterface {

	public Writer() throws IOException {
		super();
	}

	/**
	 * Adds a user to the usersFile.
	 * 
	 * @param user
	 *            - The user to be added to the usersFile.
	 * @return void
	 */
	public void addUser(User user) {
		try {
			Reader read = new Reader();
			// Create a JSONObject with the username and challenge.
			JSONObject job = new JSONObject();
			job.put("user", user.getUsername());
			job.put("challenge", user.getChallenge());
			// Get the JSONArray of all users.
			JSONArray jray = read.getUsersJSON();
			// Add the new JSONObject.
			jray.add(job);
			// Rewrite the usersFile.
			rewriteUsers(jray);
			// Create a new file for the new users PasswordInfo.
			if (!user.getFile().exists()) {
				user.getFile().createNewFile();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Private function used to rewrite the usersFile (The file where all
	 * registered users are stored on the android SD card.)
	 * 
	 * @param jray
	 *            - The JSONArray holding all user info to be rewritten.
	 * @return void
	 */
	private void rewriteUsers(JSONArray jray) {

		try {
			// Open a fileoutput stream and write the file.
			FileOutputStream fo = new FileOutputStream(usersFile);
			fo.write(jray.toString().getBytes());
			fo.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Rewrites the user's PasswordInfo the their specific password file.
	 * 
	 * @param user
	 *            - The user whose PasswordInfo is being rewritten.
	 * @param passwords
	 *            -ArrayList of the PasswordInfo that is to be rewritten for the
	 *            user.
	 * @return void
	 */
	public void rewriteUserPasswords(User user,
			ArrayList<PasswordInfo> passwords) {
		try {
			// Create a JSONArray from the passwords passed to
			// the function.
			JSONArray jray = new JSONArray();
			for (int i = 0; i < passwords.size(); i++) {
				JSONObject jobj = new JSONObject();
				jobj.put("title", passwords.get(i).getTitle());
				jobj.put("password", passwords.get(i).getPassword());
				jobj.put("notes", passwords.get(i).getNotes());
				jray.add(jobj);
			}
			// Open a fileOuputStream to the users specific file then write
			// the JSONArray to it after encrypting it.
			FileOutputStream fo = new FileOutputStream(user.getFile());
			fo.write(AESUtils.encryptBytes(jray.toString().getBytes(),
					user.getKey()));
			fo.close();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
