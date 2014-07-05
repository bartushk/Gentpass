package bartushk.gentpass.crypto;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Name : Convert.java <br>
 * Author : Kyle Bartush<br>
 * Version : 1.0<br>
 * Description : Contains static functions used for converting data between
 * different formats.
 */
public class Convert {

	/**
	 * Converts an array of bytes into a hexadecimal string.
	 * 
	 * @param input
	 *            - The array of bytes to be converted.
	 * @return String with the hexadecimal value of the byte input.
	 */
	public static String toHexString(byte[] input) {
		BigInteger i = new BigInteger(1, input);
		return String.format("%1$032X", i);
	}

	/**
	 * Converts an array of bytes into a string representation containing only
	 * lower case letters.
	 * 
	 * @param input
	 *            - The array of bytes to be converted.
	 * @return String containing only lower case letters.
	 */
	public static String lowerString(byte[] input) {
		for (int i = 0; i < input.length; i++) {
			input[i] = (byte) ((input[i] + 128) % 26 + 97);
		}
		return new String(input);
	}

	/**
	 * Converts an array of bytes into a string representation containing lower
	 * case letters and numbers.
	 * 
	 * @param input
	 *            - The array of bytes to be converted.
	 * @return String containing lower case letters and numbers.
	 */
	public static String lowerNumberString(byte[] input) {
		int pos = 0;
		byte[] replace = HashUtils.md5Byte(input);
		for (int i = 0; i < input.length; i++) {
			int tmp = input[i] + 128;
			int maxValue = 255 - (256 % 62);
			while (tmp > maxValue) {
				if (pos > replace.length) {
					pos = 0;
					replace = HashUtils.md5Byte(replace);
				}
				tmp = replace[pos] + 128;
				pos++;
			}
			tmp %= 36;
			if (tmp < 10) {
				input[i] = (byte) (tmp + 48);
			} else {
				input[i] = (byte) (tmp + 87);
			}
		}
		return new String(input);
	}

	/**
	 * Converts an array of bytes into a string representation containing lower
	 * and upper case letters.
	 * 
	 * @param input
	 *            - The array of bytes to be converted.
	 * @return String containing lower and upper case letters.
	 */
	public static String lowerUpperString(byte[] input) {
		int pos = 0;
		byte[] replace = HashUtils.md5Byte(input);
		for (int i = 0; i < input.length; i++) {
			int tmp = input[i] + 128;
			int maxValue = 255 - (256 % 62);
			while (tmp > maxValue) {
				if (pos > replace.length) {
					pos = 0;
					replace = HashUtils.md5Byte(replace);
				}
				tmp = replace[pos] + 128;
				pos++;
			}
			tmp %= 52;
			if (tmp < 26) {
				input[i] = (byte) (tmp + 65);
			} else {
				input[i] = (byte) (tmp + 71);
			}
		}
		return new String(input);
	}

	/**
	 * Converts an array of bytes into a string representation containing lower
	 * and upper case letters as well as numbers.
	 * 
	 * @param input
	 *            - The array of bytes to be converted.
	 * @return String containing lower and upper case letters as well as
	 *         numbers.
	 */
	public static String lowerUpperNumberString(byte[] input) {
		int pos = 0;
		byte[] replace = HashUtils.md5Byte(input);
		for (int i = 0; i < input.length; i++) {
			int tmp = input[i] + 128;
			int maxValue = 255 - (256 % 62);
			while (tmp > maxValue) {
				if (pos > replace.length) {
					pos = 0;
					replace = HashUtils.md5Byte(replace);
				}
				tmp = replace[pos] + 128;
				pos++;
			}
			tmp %= 62;
			if (tmp < 10) {
				input[i] = (byte) (tmp + 48);
			} else if (tmp < 36) {
				input[i] = (byte) (tmp + 87);
			} else {
				input[i] = (byte) (tmp + 29);
			}
		}
		return new String(input);
	}
}
