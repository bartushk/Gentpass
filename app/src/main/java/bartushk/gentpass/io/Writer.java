package bartushk.gentpass.io;

import android.os.Environment;
import android.util.Log;

import java.io.File;
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
            // Create a JSONArray from the passwords passed to
            // the function.
            File userpasswordFile = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "gentpass" + File.separator + user.getFileName());
			if (!userpasswordFile.exists()) {
                userpasswordFile.createNewFile();
			}
		} catch (Exception e){
            Log.d(e.getCause().toString(), e.getMessage());
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

        FileOutputStream fo = null;
        try {
            // Open a fileoutput stream and write the file.
            fo = new FileOutputStream(usersFile);
            fo.write(jray.toString().getBytes());

        } catch (IOException e) {
            Log.d(e.getCause().toString(), e.getMessage());
        } finally {
            if (fo != null) try { fo.close(); } catch (IOException logOrIgnore) {}
        }

    }

	/**
	 * Rewrites the user's PasswordInfo the their specific password file.
	 * 
	 * @param user
	 *            - The user whose PasswordInfo is being rewritten.
	 * @return void
	 */
	public void rewriteUserPasswords(User user) {
        FileOutputStream fo = null;
        try {
            // Create a JSONArray from the passwords passed to
            // the function.
            File userpasswordFile = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "gentpass" + File.separator + user.getFileName());
            JSONArray jray = new JSONArray();
            for (int i = 0; i < user.getPasswordInfos().size(); i++) {
                JSONObject jobj = new JSONObject();
                jobj.put("title", user.getPasswordInfos().get(i).getTitle());
                jobj.put("password", user.getPasswordInfos().get(i).getPassword());
                jobj.put("notes", user.getPasswordInfos().get(i).getNotes());
                jray.add(jobj);
            }
            // Open a fileOuputStream to the users specific file then write
            // the JSONArray to it after encrypting it.
            fo = new FileOutputStream(userpasswordFile);
            fo.write(AESUtils.encryptBytes(jray.toString().getBytes(),
                    user.getKey()));
            fo.close();
        } catch (Exception e) {
            Log.d(e.getCause().toString(), e.getMessage());
        } finally {
            if (fo != null) try { fo.close(); } catch (IOException logOrIgnore) {}
        }
    }

}
