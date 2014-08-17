package grouptabs.backend.dao;

import grouptabs.backend.representation.Tab;
import grouptabs.backend.representation.TabUser;
import grouptabs.backend.representation.Transaction;
import grouptabs.backend.representation.TransactionContribution;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.helpers.MapResultAsBean;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

public interface TabDAO extends Transactional<TabDAO> {
	
	@MapResultAsBean
	@SqlQuery("SELECT * FROM Tab")
	public List<Tab> getTabs();
	
	@MapResultAsBean
	@SqlQuery("SELECT * FROM Tab WHERE id = :id")
	public Tab getTab(@Bind("id") Integer id);
	
	@MapResultAsBean
	@SqlQuery("SELECT * FROM Tab WHERE key = :key")
	public Tab getTab(@Bind("key") String key);
	
	@GetGeneratedKeys
	@SqlUpdate("INSERT INTO Tab VALUES (NULL, :key, :name)")
	public Long insertTab(@Bind("key") String key, @Bind("name") String name);
	
	@SqlUpdate("DELETE FROM Tab WHERE id = :id")
	void deleteTab(@Bind("id") Integer id);
	
	@MapResultAsBean
	@SqlQuery("SELECT * FROM Tab_User WHERE tabId = :id")
	public List<TabUser> getTabUsers(@Bind("id") Integer id);
	
	@MapResultAsBean
	@SqlQuery("SELECT * FROM Transaction WHERE id = :id")
	public Transaction getTransaction(@Bind("id") Long id);
	
	@MapResultAsBean
	@SqlQuery("SELECT participant, amount FROM Contribution WHERE transactionId = :transactionId")
	public List<TransactionContribution> getContributionsForTransaction(@Bind("transactionId") Long transactionId);
	
	@MapResultAsBean
	@SqlQuery("SELECT * FROM Transaction WHERE tabKey = :tabKey ORDER BY DATE DESC, ID DESC LIMIT :limit")
	public List<Transaction> getLastTransactionsForTab(@Bind("tabKey") String tabKey, @Bind("limit") Integer limit);
	
	@MapResultAsBean
	@SqlQuery("SELECT * FROM Transaction WHERE tabKey = :tabKey AND date > :since ORDER BY DATE DESC, ID DESC LIMIT :limit")
	public List<Transaction> getLastTransactionsForTab(@Bind("tabKey") String tabKey, @Bind("since") String since, @Bind("limit") Integer limit);
	
	@SqlQuery("SELECT COUNT(*) FROM Transaction WHERE id = :id")
	public Boolean transactionExists(@Bind("id") Long id);
	
	@SqlUpdate("UPDATE Transaction SET date = :date, timestamp = :timestamp, description = :description, type = :type WHERE id = :id")
	void updateTransaction(@Bind("id") Long id, @Bind("date") Date date, @Bind("timestamp") Date timestamp, @Bind("description") String description, @Bind("type") String type);
	
	@SqlUpdate("DELETE FROM Contribution WHERE transactionId = :transactionId")
	void deleteContributionsForTransaction(@Bind("id") Long id);
	
	@SqlUpdate("DELETE FROM Transaction WHERE id = :id")
	void deleteTransaction(@Bind("id") Long id);

	@GetGeneratedKeys
	@SqlUpdate("INSERT INTO Transaction (id, tabId, date, timestamp, description, type) values (NULL, :tabId, :date, :timestamp, :description, :type)")
	public Long insertTransaction(
			@Bind("tabId") Integer tabId,
			@Bind("date") Date date,
			@Bind("timestamp") Date timestamp,
			@Bind("description") String description,
			@Bind("type") String type);
	
	@SqlUpdate("INSERT INTO Contribution (transactionId, participant, amount) values (:transactionId, :participant, :amount)")
	public void insertTransactionContribution(
			@Bind("transactionId") Long transactionId,
			@Bind("participant") String participant,
			@Bind("amount") BigDecimal amount);

}
