package grouptabs.backend.representation;

import java.util.Map;

public class User {
	
	private Integer id;
	private String email;
	private String name;
	private String password;
	private Map<Integer, String> tabs;

	
	public User() {
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @return a map containing the IDs of the user's tabs and the respective local name of the user 
	 */
	public Map<Integer, String> getTabs() {
		return tabs;
	}
	
	public void setTabs(Map<Integer, String> tabs) {
		this.tabs = tabs;
	}

}
