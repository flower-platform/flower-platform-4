package org.flowerplatform.core.users;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
		
		//set an admin
		if (login.equals("Jim")) {
			node.getProperties().put("isAdmin", true);
		}
		return node;
	}
	
	@GET
	public List<Node> getUsers() {
		return users;
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
		return null;
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
		Principal userPrincipal = userValidator.getCurrentUserPrincipal(
				CorePlugin.getInstance().getRequestThreadLocal().get().getSession());
		if (userPrincipal == null) {
			return null;
		}
		for (Node user : users) {
			if (user.getNodeUri().endsWith(userPrincipal.getName())) {
				return user;
			}
		}
		throw new RuntimeException("User not found");
	}
	
	/**
	 * Perform login.
	 * 
	 * @return logged in user
	 */
	@POST @Path("/login")
	public Node login(Map<String, String> loginInfo) {
		String username = loginInfo.get("username");
		for (Node user : users) {
			if (user.getNodeUri().endsWith(username)) {
				userValidator.setCurrentUserPrincipal(
						CorePlugin.getInstance().getRequestThreadLocal().get().getSession(), 
						userValidator.validateUser(username, null));
				return user;
			}
		}
		throw new RuntimeException("User not found");
	}
	
	/**
	 * Perform logout.
	 */
	@POST @Path("/logout")
	public void logout() {
		userValidator.clearCurrentUserPrincipal(CorePlugin.getInstance().getRequestThreadLocal().get().getSession());
	}
	
}
