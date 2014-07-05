package bartushk.gentpass.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Name : AESUtils.java <br>
 * Author : Kyle Bartush<br>
 * Version : 1.0<br>
 * Description : Class containing static functions used for data encryption and
 * decryption.
 */
public class AESUtils {

	/**
	 * Encrypts data using the AES encryption cipher.
	 * 
	 * @param data
	 *            - Information to be encrypted.
	 * @param key
	 *            - Key to use for encryption.
	 * @return The encrypted data.
	 */
	public static byte[] encryptBytes(byte[] data, byte[] key) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			SecretKeySpec sKey = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, sKey);
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		return null;
	}

	
	/**
	 * Decrypts data using the AES encryption cipher.
	 * 
	 * @param data
	 *            - Information to be decrypted.
	 * @param key
	 *            - Key to use for decryption.
	 * @return The decrypted data.
	 */
	public static byte[] decryptBytes(byte[] data, byte[] key) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			SecretKeySpec sKey = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.DECRYPT_MODE, sKey);
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		return null;
	}

}
