package grouptabs.backend.dao;

import grouptabs.backend.representation.TabUser;
import grouptabs.backend.representation.User;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.helpers.MapResultAsBean;

public interface UserDAO {
	
	@MapResultAsBean
	@SqlQuery("SELECT * FROM User")
	public List<User> getUsers();

	@MapResultAsBean
	@SqlQuery("SELECT * FROM User WHERE id = :userId")
	public User getUser(@Bind("userId") Integer userId);
	
	@MapResultAsBean
	@SqlQuery("SELECT * FROM Tab_User WHERE userId = :id")
	public List<TabUser> getUserTabs(@Bind("id") Integer id);

	@MapResultAsBean
	@SqlQuery("SELECT * FROM Tab LEFT JOIN Tab_User ON userId WHERE id = userId AND tabId = :tabId")
	public List<User> getUsersForTab(@Bind("tabId") Integer tabId);
	
	// XXX implement hashing
	@MapResultAsBean
	@SqlQuery("SELECT * FROM User WHERE name = :name and password = :password")
	public User getUser(@Bind("name") String name, @Bind("password") String password);

}