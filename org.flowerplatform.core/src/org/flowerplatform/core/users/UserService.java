package org.flowerplatform.core.users;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Mariana Gheorghe
 */
@Path("/users")
public class UserService {

	private List<Node> users = new ArrayList<Node>();
	
	private UserValidator userValidator = new UserValidator();
	
	public UserService() {
		users.add(newTestUser("john"));
		users.add(newTestUser("jane"));
		users.add(newTestUser("jim"));
	}
	
	private Node newTestUser(String login) {
		Node node = new Node("user:test|" + login, "user");
		node.getProperties().put("login", login);
		node.getProperties().put("name", login + " " + login + "son");
		node.getProperties().put("email", login + "@domain.com");
		node.getProperties().put("avatar", "http://localhost:8080/org.flowerplatform.host.web_app/servlet/public-resources/org.flowerplatform.resources/images/core/file.gif");
		
		//set an admin
		if (login.equals("Jim")) {
			node.getProperties().put("isAdmin", true);
		}
		return node;
	}
	
	/**
	 * @return full list of users
	 */
	@GET
	public List<Node> getUsers() {
		if (getCurrentUser() == null) {
			return users;
		}
		List<Node> test = new ArrayList<Node>();
		test.addAll(users);
		test.add(getCurrentUser());
		return test;
	}
	
	/**
	 * @return the user with <code>nodeUri</code>
	 */
	@GET @Path("/{nodeUri}")
	@Produces(MediaType.APPLICATION_JSON)
	public Node getUser(@PathParam("nodeUri") String nodeUri) {
		for (Node user : users) {
			if (user.getNodeUri().equals(nodeUri)) {
				return user;
			}
		}
		return getCurrentUser();
	}
	
	/**
	 * Update or create the user.
	 * 
	 * @return the updated user
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Node saveUser(Node user) {
		if (user.getNodeUri() == null) {
			// new user
			user.setNodeUri("user:test|" + user.getProperties().get("login"));
			users.add(user);
		}
		
		Node mem = getUser(user.getNodeUri());
		
		mem.getProperties().put("login", user.getProperties().get("login"));
		mem.getProperties().put("name", user.getProperties().get("name"));
		mem.getProperties().put("email", user.getProperties().get("email"));
		return mem;
	}
	
	/**
	 * Delete the user with <code>nodeUri</code>.
	 */
	@DELETE @Path("/{nodeUri}")
	public void deleteUser(@PathParam("nodeUri") String nodeUri) {
		for (Node user : users) {
			if (user.getNodeUri().equals(nodeUri)) {
				users.remove(user);
				break;
			}
		}
	}
	
	/**
	 * @return the current user (saved in the session)
	 */
	@GET @Path("/login")
	public Node getCurrentUser() {
		HttpServletRequest req = CorePlugin.getInstance().getRequestThreadLocal().get();
		if (req == null) {
			return null;
		}
		return getCurrentUser(req);
	}
	
	/**
	 * @param req
	 * @return the current user (saved in the session)
	 */
	public Node getCurrentUser(HttpServletRequest req) {
		Principal userPrincipal = userValidator.getCurrentUserPrincipal(req.getSession());
		System.out.println(req.getSession().getId());
		if (userPrincipal == null) {
			return null;
		}
//		for (Node user : users) {
//			if (user.getNodeUri().endsWith(userPrincipal.getName())) {
//				return user;
//			}
//		}
		
		return ((UserPrincipal) userPrincipal).getUser();
	}
	
	/**
	 * Perform login.
	 * 
	 * @return logged in user
	 */
	@POST @Path("/login")
	public Node login(Map<String, String> loginInfo) {
		String username = loginInfo.get("username");
		return login(username, null);
	}
	
	/**
	 * Perform login.
	 * 
	 * @return logged in user
	 */
	public Node login(String username, String password) {
		for (Node user : users) {
			if (user.getNodeUri().endsWith(username)) {
				if (CorePlugin.getInstance().getRequestThreadLocal().get() != null) {
					userValidator.setCurrentUserPrincipal(
							CorePlugin.getInstance().getRequestThreadLocal().get().getSession(), 
							new UserPrincipal(user));
				}
				return user;
			}
		}
		return null;
	}
	
	/**
	 * Perform logout.
	 */
	@POST @Path("/logout")
	public void logout() {
		userValidator.clearCurrentUserPrincipal(CorePlugin.getInstance().getRequestThreadLocal().get().getSession());
	}
	
}
