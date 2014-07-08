package bartushk.gentpass.io;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import bartushk.gentpass.crypto.AESUtils;
import bartushk.gentpass.data.PasswordInfo;
import bartushk.gentpass.data.User;

/**
 * Name : Reader.java <br>
 * Author : Kyle Bartush<br>
 * Version : 1.0<br>
 * Description : Extension of the FileInterface class containing functions
 * specific to reading files from the android SD card.
 */
public class Reader extends FileInterface {

	public Reader() throws IOException {
		super();
	}

	/**
	 * Reads from the usersFile returning a JSONArray of all the users
	 * registered.
	 * 
	 * @return JSONArray containing all users.
	 */
	public JSONArray getUsersJSON() {
        FileInputStream ifstream = null;
        try {
            // Read a string from the correct usersFile
            int chr;
            StringBuffer strBuf = new StringBuffer();
            ifstream = new FileInputStream(usersFile);
            while ((chr = ifstream.read()) != -1) {
                strBuf.append((char) chr);
            }
            // Parse and return a JSONArray.
            JSONParser jparse = new JSONParser();
            return (JSONArray) jparse.parse(strBuf.toString());
        } catch (Exception e) {
            Log.d(e.getCause().toString(), e.getMessage());
        } finally {
            if (ifstream != null) try { ifstream.close(); } catch (IOException logOrIgnore) {}
        }

        return null;
    }

	/**
	 * Returns an ArrayList of PasswordInfo for a particular user based on the
	 * username given.
	 * 
	 * @param user
	 *            - The user who's password info is to be returned.
	 * @return An ArrayList of all the PasswordInfo for the given user.
	 */
	public ArrayList<PasswordInfo> getUserPasswords(User user) {
        // Initialize the return list.
        ArrayList<PasswordInfo> retList = new ArrayList<PasswordInfo>();
        FileInputStream ifstream = null;
        try {
            // Open a filestream from the users file pointer.
            // Password file is stored individually for each user and the filename
            //is a hash making it indistinguishable from other users.
            File userpasswordFile = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "gentpass" + File.separator + user.getFileName());
            ifstream = new FileInputStream(userpasswordFile);
            // If the file is not empty then read from it.
            if (userpasswordFile.length() > 0) {
                // Get a byte array of the correct length to hold all the
                // info from the file.
                byte[] inputFileData = new byte[(int) userpasswordFile.length()];
                // Read the info from the input file.
                ifstream.read(inputFileData);
                JSONParser jparse = new JSONParser();
                // Get a decrypted string of the inputFileData using the users
                // key and the byte array read from the ifstream.
                String decrypted = new String(AESUtils.decryptBytes(
                        inputFileData, user.getKey()));
                // Parse the data from the decrpyted string into a json array.
                JSONArray readData = (JSONArray) jparse.parse(decrypted);
                // For each object in the JSONArray readData, create a new
                // PasswordInfo object and add it to the ArrayList retList.
                for (int i = 0; i < readData.size(); i++) {
                    String title = (String) ((JSONObject) readData.get(i))
                            .get("title");
                    String password = (String) ((JSONObject) readData.get(i))
                            .get("password");
                    String notes = (String) ((JSONObject) readData.get(i))
                            .get("notes");
                    if (title != null) {
                        retList.add(new PasswordInfo(title, password, notes));
                    }
                }
            }
        } catch (Exception e) {
            Log.d(e.getCause().toString(), e.getMessage());
        } finally {
            if (ifstream != null) try { ifstream.close(); } catch (IOException logOrIgnore) {}
        }
        return retList;
    }

}
