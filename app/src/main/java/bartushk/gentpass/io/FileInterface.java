package bartushk.gentpass.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;

/**
 * Name : FileInterface.java <br>
 * Author : Kyle Bartush<br>
 * Version : 1.0<br>
 * Description : A super class to the file reader and writer class written for
 * this application.
 */
public class FileInterface {
	// File pointer to the file that stores
	// all the information on the registered users.
	File usersFile;

	// Constructor sets up the users file. For now this is put in
	// a default position, though this could be edited by the
	// user in an options screen.
	public FileInterface() throws IOException {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		// Sets booleans testing whether the external storage device
		// is available and/or writeable.
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}

		// Throw exceptions based on the availability of the external
		// storage.
		if (!mExternalStorageWriteable) {
			throw new IOException("Cannot write to external storage.");
		}
		if (!mExternalStorageAvailable) {
			throw new IOException("External storage not available");
		}
		// Check if the gentpass directory exists, if not, create it.
		File file = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "gentpass");
		if (!file.isDirectory()) {
			file.mkdirs();
		}

		// Set the usersFile pointer to the default file. If the file
		// does not exist, initialize it.
		usersFile = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "gentpass" + File.separator + "users");
		if (!usersFile.exists()) {
			usersFile.createNewFile();
			FileOutputStream fo = new FileOutputStream(usersFile);
			String tmp = "[]";
			fo.write(tmp.getBytes());
		}
	}

}
