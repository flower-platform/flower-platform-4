package org.flowerplatform.users;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.flowerplatform.core.node.remote.Node;

@Path("/userService")
public class UserService {

	private List<Node> users = new ArrayList<Node>();
	
	public UserService() {
		users.add(newTestUser("John"));
		users.add(newTestUser("Jane"));
		users.add(newTestUser("Jim"));
	}
	
	private Node newTestUser(String login) {
		Node node = new Node("user", null, login, null);
		node.getProperties().put("login", login);
		node.getProperties().put("name", login + " " + login + "son");
		node.getProperties().put("email", login + "@domain.com");
		return node;
	}
	
	@GET @Path("/getUsers")
	public List<Node> getUsers() {
		return users;
	}
	
	@GET @Path("/getUser/{fullNodeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Node getUser(@PathParam("fullNodeId") String fullNodeId) {
		for (Node user : users) {
			if (user.getFullNodeId().equals(fullNodeId)) {
				return user;
			}
		}
		return null;
	}
	
	@PUT @Path("/saveUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public Node saveUser(Node user) {
		Node mem = getUser(user.getFullNodeId());
		mem.getProperties().put("name", user.getProperties().get("name"));
		mem.getProperties().put("email", user.getProperties().get("email"));
		return mem;
	}
	
	@POST @Path("/createUser")
	public Node createUser(Node user) {
		users.add(user);
		user.setIdWithinResource((String) user.getProperties().get("login"));
		return user;
	}
	
	@DELETE @Path("/deleteUser/{fullNodeId}")
	public void deleteUser(@PathParam("fullNodeId") String fullNodeId) {
		for (Node user : users) {
			if (user.getFullNodeId().equals(fullNodeId)) {
				users.remove(user);
				break;
			}
		}
	}
	
}
