package bartushk.gentpass.data;

import java.io.File;

import android.os.Environment;
import bartushk.gentpass.crypto.HashUtils;

/**
 * Name : PasswordInfo.java <br>
 * Author : Kyle Bartush<br>
 * Version : 1.0<br>
 * Description : A class used to hold all the information pertaining to an
 * individual user.
 */
public class User {

	// Strings for holding the username, password, and challenge
	// for password verification. The challenge is a hash that is stored
	// in the users file to verify users.
	private String username, password, passChallenge;

	// The File pointer to the users encrypted password file.
	private File passwordFile;

	// The user's encryptoin key.
	private byte[] aesKey;
	
	//Master key used for identity. Is encrypted with username password locally.
	private byte[] masterKey = new byte[32];

	// Constructor where all important information is set as well.
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		setAdditionalInfo();
	}

	// Getters
	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public String getChallenge() {
		return this.passChallenge;
	}

	public byte[] getKey() {
		return this.aesKey;
	}

	public File getFile() {
		return this.passwordFile;
	}

	// Setters

	// also sets additional info when the username is changed.
	public void setUsername(String username) {
		this.username = username;
		setAdditionalInfo();
	}

	// also sets additional info when the password is changed.
	public void setPassword(String password) {
		this.password = password;
		setAdditionalInfo();
	}

	/**
	 * Name : setAdditionalInfo.java <br>
	 * Author : Kyle Bartush<br>
	 * Version : 1.0<br>
	 * Description : Sets additional user info. This is all information based
	 * off the username is password and is called when they are created or
	 * changed.
	 */
	private void setAdditionalInfo() {
		//The encryption key is the sha256 hash of the password.
		aesKey = HashUtils.sha256Byte(password);
		
		//The challenge is the md5 hash of the password plus
		//a 10 character salt composed of the sha1 hash of the password
		passChallenge = HashUtils.md5Hex(password
				+ HashUtils.shaHex(password).substring(0, 10));
		
		//The filename is the sha1 hash of the password salted
		//by the first 10 characters of the md5 hash of the password.
		String fileName = HashUtils.shaHex(password
				+ HashUtils.md5Hex(password).substring(0, 10));
		
		// Password file is stored individually for each user and the filename
		//is a hash making it indistinguishable from other users.
		passwordFile = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "gentpass" + File.separator + fileName);
	}
}
