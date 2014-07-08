package bartushk.gentpass.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import android.os.Environment;

import bartushk.gentpass.core.Utils;
import bartushk.gentpass.crypto.HashUtils;
import bartushk.gentpass.io.Reader;

/**
 * Name : User.java <br>
 * Author : Kyle Bartush<br>
 * Version : 1.0<br>
 * Description : A class used to hold all the information pertaining to an
 * individual user.
 */
public class User {

    private String cryptoVersion;

	// Strings for holding the username, password, and challenge
	// for password verification. The challenge is a hash that is stored
	// in the users file to verify users.
	private String username, password, passChallenge;

    // The filename for the password info to be saved under.
    private String fileName;

    // Date time, last updated.
    private Date lastUpdated;

    // User currently saved passwords.
    private ArrayList<PasswordInfo> passwordInfos;
	
	//Master key used for identity. Is encrypted with username password locally.
	private byte[] masterKey = new byte[32];

    //Master key used for identity. Is encrypted with username password locally.
    private byte[] syncKey = new byte[32];

    // The user's encryption key.
    private byte[] aesKey;

	// Constructor where all important information is set as well.
	public User(String username, String password, String cryptoVersion) {
		this.username = username;
		this.password = password;
        this.cryptoVersion = cryptoVersion;
        this.passwordInfos = new ArrayList<PasswordInfo>();
		setAdditionalInfo();
	}

	// Getters
	public String getUsername() {
		return this.username;
	}

    public Date getLastUpdated(){ return this.lastUpdated;}

	public String getPassword() {
		return this.password;
	}

	public String getChallenge() {
		return this.passChallenge;
	}

    public ArrayList<PasswordInfo> getPasswordInfos(){
        return this.passwordInfos;
    }

	public byte[] getKey() {
		return this.aesKey;
	}

    public String getFileName(){
        return this.fileName;
    }

    public String getCryptoVersion(){ return this.cryptoVersion; }

	// Setters


    public void setLastUpdated(Date newUpdated){ this.lastUpdated = newUpdated;}

    public void setPasswordInfos(ArrayList<PasswordInfo> passInfos){
        this.passwordInfos = passInfos;
    }

    public void setCryptoVersion(String ver){ this.cryptoVersion = ver;}

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

        if(this.cryptoVersion == "Orig"){
                //The encryption key is the sha256 hash of the password.
                aesKey = HashUtils.sha256Byte(password);

                //The challenge is the md5 hash of the password plus
                //a 10 character salt composed of the sha1 hash of the password
                passChallenge = HashUtils.md5Hex(password
                        + HashUtils.shaHex(password).substring(0, 10));

                //The filename is the sha1 hash of the password salted
                //by the first 10 characters of the md5 hash of the password.
                this.fileName = HashUtils.shaHex(password
                        + HashUtils.md5Hex(password).substring(0, 10));

                this.syncKey = HashUtils.sha256Byte(this.masterKey);
        }


        if(this.cryptoVersion == "2.0"){

        }
	}
}
