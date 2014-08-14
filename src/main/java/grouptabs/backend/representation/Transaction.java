package grouptabs.backend.representation;

import java.util.Date;
import java.util.List;

public class Transaction {
	
	/**
	 * The possible types of a transaction
	 */
	public enum Type {
		SHARED, DIRECT;
	}
	
	private Long id;
	private Date date;
	private Date timestamp;
	private String description; 
	private List<TransactionContribution> participants;
	private Type type;

	private Integer tabId;
	

	public Transaction() {
	}

	public Transaction(Long id, Integer tabId, Date date, Date timestamp, String description, List<TransactionContribution> participants, Type type) {
		this.id = id;
		this.tabId = tabId;
		this.date = date;
		this.timestamp = timestamp;
		this.description = description;
		this.participants = participants;
		this.type = type;
	}

	/**
	 * @return the ID of the transaction
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the transaction ID (must be unique)
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the ID of tab in which the transaction took place
	 */
	public Integer getTabId() {
		return tabId;
	}

	/**
	 * @param tabId the ID of tab in which the transaction took place
	 */
	public void setTabId(Integer tabId) {
		this.tabId = tabId;
	}

	/**
	 * @return the (user-defined) date of the transaction
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date when the transaction took place
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * @return the timestamp of the last change on the transaction
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Only to be used by the object mapper.
	 * @param timestamp the timestamp of the last change on the transaction
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	/**
	 * @return the description the user has provided for the transaction
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @param description the description for the transaction
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return a list of the participants who are involved in the transaction
	 */
	public List<TransactionContribution> getParticipants() {
		return participants;
	}
	
	/**
	 * @param participants a list containing the participation data of all users involved in the transaction
	 */
	public void setParticipants(List<TransactionContribution> participants) {
		this.participants = participants;
	}
	
	/**
	 * @return the Transaction.Type of the transaction
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * @param type the Transaction.Type of the transaction
	 */
	public void setType(Type type) {
		this.type = type;
	}
	
}
