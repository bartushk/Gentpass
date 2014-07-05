package bartushk.gentpass.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Name : HashUtils.java <br>
 * Author : Kyle Bartush<br>
 * Version : 1.0<br>
 * Description : Contains static functions used for hashing information to and
 * from different forms.
 */
public class HashUtils {

	/**
	 * Returns a digest, in the form of an array of bytes, using various hashing
	 * algorithms. This is a private function used by many of the public
	 * functions within this class.
	 * 
	 * @param algorithm
	 *            - The hashing algorithm to be used.
	 * @param input
	 *            - The array of bytes to be hashed.
	 * @return An array of bytes representing the hash result.
	 */
	private static byte[] digestByte(String algorithm, byte[] input) {
		MessageDigest m;
		try {
			m = MessageDigest.getInstance(algorithm);
			m.update(input, 0, input.length);
			return m.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns a digest, in the form of a string, using various hashing algorithms. This
	 * is a private function used by many of the public functions within this
	 * class.
	 * 
	 * @param algorithm
	 *            - The hashing algorithm to be used.
	 * @param input
	 *            - The array of bytes to be hashed.
	 * @return An array of bytes representing the hash result.
	 */
	private static String digestString(String algorithm, byte[] input) {
		MessageDigest m;
		try {
			m = MessageDigest.getInstance(algorithm);
			m.update(input, 0, input.length);
			return Convert.toHexString(m.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	// //////////////////////////////////////////////

	/**
	 * Returns a hexadecimal string representation of the hash 
	 * of a string input using the MD5 hashing algorithm.
	 * 
	 * @param in
	 *            - The array of bytes to be hashed.
	 * @return A Hex string representation of the hashed input.
	 */
	public static String md5Hex(String in) {
		return digestString("MD5", in.getBytes());
	}

	/**
	 * Returns a hexadecimal string representation of the hash 
	 * of a byte array input using the MD5 hashing algorithm.
	 * 
	 * @param in
	 *            - The array of bytes to be hashed.
	 * @return A Hex string representation of the hashed input.
	 */
	public static String md5Hex(byte[] in) {
		return digestString("MD5", in);
	}

	/**
	 * Returns a byte array representation of the hash 
	 * of a string input using the MD5 hashing algorithm.
	 * 
	 * @param in
	 *            - The String to be hashed.
	 * @return A byte array of the hashed input.
	 */
	public static byte[] md5Byte(String in) {
		return digestByte("MD5", in.getBytes());
	}

	/**
	 * Returns a byte array representation of the hash 
	 * of a byte array input using the MD5 hashing algorithm.
	 * 
	 * @param in
	 *            - The array of bytes to be hashed.
	 * @return A byte array representation of the input.
	 */
	public static byte[] md5Byte(byte[] in) {
		return digestByte("MD5", in);
	}

	// ///////////////////////////////////////////////////////

	/**
	 * Returns a hexadecimal string representation of the hash 
	 * of a string input using the SHA1 hashing algorithm.
	 * 
	 * @param in
	 *            - The array of bytes to be hashed.
	 * @return A Hex string representation of the hashed input.
	 */
	public static String shaHex(String in) {
		return digestString("SHA", in.getBytes());
	}

	/**
	 * Returns a hexadecimal string representation of the hash 
	 * of a byte array input using the SHA1 hashing algorithm.
	 * 
	 * @param in
	 *            - The array of bytes to be hashed.
	 * @return A Hex string representation of the hashed input.
	 */
	public static String shaHex(byte[] in) {
		return digestString("SHA", in);
	}

	/**
	 * Returns a byte array representation of the hash 
	 * of a string input using the SHA1 hashing algorithm.
	 * 
	 * @param in
	 *            - The String to be hashed.
	 * @return A byte array of the hashed input.
	 */
	public static byte[] shaByte(String in) {
		return digestByte("SHA", in.getBytes());
	}

	/**
	 * Returns a byte array representation of the hash 
	 * of a byte array input using the SHA1 hashing algorithm.
	 * 
	 * @param in
	 *            - The array of bytes to be hashed.
	 * @return A byte array representation of the input.
	 */
	public static byte[] shaByte(byte[] in) {
		return digestByte("SHA", in);
	}

	// ///////////////////////////////////////////////////////
	
	/**
	 * Returns a hexadecimal string representation of the hash 
	 * of a string input using the SHA256 hashing algorithm.
	 * 
	 * @param in
	 *            - The array of bytes to be hashed.
	 * @return A Hex string representation of the hashed input.
	 */
	public static String sha256Hex(String in) {
		return digestString("SHA256", in.getBytes());
	}

	/**
	 * Returns a hexadecimal string representation of the hash 
	 * of a byte array input using the SHA256 hashing algorithm.
	 * 
	 * @param in
	 *            - The array of bytes to be hashed.
	 * @return A Hex string representation of the hashed input.
	 */
	public static String sha256Hex(byte[] in) {
		return digestString("SHA256", in);
	}

	/**
	 * Returns a byte array representation of the hash 
	 * of a string input using the SHA256 hashing algorithm.
	 * 
	 * @param in
	 *            - The String to be hashed.
	 * @return A byte array of the hashed input.
	 */
	public static byte[] sha256Byte(String in) {
		return digestByte("SHA256", in.getBytes());
	}

	/**
	 * Returns a byte array representation of the hash 
	 * of a byte array input using the SHA256 hashing algorithm.
	 * 
	 * @param in
	 *            - The array of bytes to be hashed.
	 * @return A byte array representation of the input.
	 */
	public static byte[] sha256Byte(byte[] in) {
		return digestByte("SHA256", in);
	}

}
