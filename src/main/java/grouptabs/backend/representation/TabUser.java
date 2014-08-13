package grouptabs.backend.representation;

public class TabUser {
	
	private Integer tabId;
	private Integer userId;
	private String localName;
	
	public TabUser() {
	}

	public Integer getTabId() {
		return tabId;
	}

	public void setTabId(Integer tabId) {
		this.tabId = tabId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}
	
}
