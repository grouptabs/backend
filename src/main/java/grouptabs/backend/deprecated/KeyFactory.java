package grouptabs.backend.deprecated;

import java.math.BigInteger;
import java.util.Random;


public class KeyFactory {
	
	// We exclude the vowels e, i, o and u from our code alphabet.
	// That way, we have a binary-friendly alphabet of 32 symbols
	// and exclude inappropriate words.
	private static String chars = "abcdfghjklmnpqrstvwxyz0123456789";
	
	private static Random rnd = new Random();
	
//	public static String getKeyForId(long id) {
//		StringBuffer key = new StringBuffer();
//		
//		for (int i = 12; i > 0; i--) {
//			// cut the long into 5-bit-chunks and encode
//			int charIndex = (int) ((id >>> i*5) & 31); 
//			key.append(chars.charAt(charIndex));
//		}
//		
//		return key.toString();
//	}
	
	/**
	 * Encodes a BigInteger value into a String
	 * @param id the BigInteger value to be encoded
	 * @return the String representation of the id
	 */
	public static String getKeyForId(BigInteger id) {
		StringBuffer key = new StringBuffer();
		BigInteger[] resultAndRemainder;
	    do {
	        resultAndRemainder = id.divideAndRemainder(BigInteger.valueOf(32));
	        int charIndex = resultAndRemainder[1].intValue(); 
			key.append(chars.charAt(charIndex));
	        System.out.println(Math.abs(resultAndRemainder[1].intValue()));
	        id = resultAndRemainder[0];
	    } while (id.compareTo(BigInteger.ZERO) != 0);
		
		return key.toString();
	}
	
	/**
	 * Decodes a String into a BigInteger value
	 * @param key the String to be decoded
	 * @return the BigInteger representation of the value
	 */
	public static BigInteger getIdForKey(String key) {
		BigInteger num32 = BigInteger.valueOf(32);
		BigInteger id = BigInteger.valueOf(0);
		for (int i = key.length()-1; i >= 0; i--) {
			int num = chars.indexOf(key.charAt(i));
			id = id.add(num32.pow(i).multiply(BigInteger.valueOf(num)));
		}
		return id;
	}
	
	/**
	 * Generates a random key String with n elements
	 * @param n the length of the String to be generated
	 * @return a random key value
	 */
	public static String generateKey(int n) {
		StringBuffer key = new StringBuffer(n);
		for (int i = 0; i < n; i++) {
			key.append(chars.charAt(rnd.nextInt(32)));
		}
		return key.toString();
	}
	
}
