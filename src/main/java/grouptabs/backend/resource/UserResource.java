package grouptabs.backend.resource;

import grouptabs.backend.dao.UserDAO;
import grouptabs.backend.representation.TabUser;
import grouptabs.backend.representation.User;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.skife.jdbi.v2.DBI;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
	
	private final UserDAO userDao;

	public UserResource(DBI jdbi) {
		userDao = jdbi.onDemand(UserDAO.class);
	}
	
	@GET
	public Response getUsers() {
		List<User> users = userDao.getUsers();
		return Response.ok(users).build();
	}
	
	@GET
	@Path("/{userId}")
	public Response getUser(@PathParam("userId") Integer userId) {
		User userData = userDao.getUser(userId);
		
		List<TabUser> userTabs = userDao.getUserTabs(userId);
		Map<Integer, String> tabMap = new TreeMap<Integer, String>();
		for (TabUser tabUser : userTabs) {
			tabMap.put(tabUser.getTabId(), tabUser.getLocalName());
		}
		userData.setTabs(tabMap);

		return Response.ok(userData).build();
	}
	
}
