package grouptabs.backend.representation;

import java.util.Map;
import java.util.Random;

public class Tab {
	
	private Integer id;
	private String key;
	private String name;
	private Map<String, Integer> users;
	
	public Tab() {
	}
	
	public Tab(String name) {
		super();
		this.name = name;
		
		// XXX check if key already exists
		this.key = Tab.generateKey(16);
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return a map containing the local names of the users of the tab and the user ID, if the user is registered
	 */
	public Map<String, Integer> getUsers() {
		return users;
	}

	public void setUsers(Map<String, Integer> participants) {
		this.users = participants;
	}
	
	
	// We exclude the vowels e, i, o and u from our code alphabet.
	// That way, we have a binary-friendly alphabet of 32 symbols
	// and exclude inappropriate words.
	private static String chars = "abcdfghjklmnpqrstvwxyz0123456789";
	private static Random rnd = new Random(System.currentTimeMillis());
	
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
