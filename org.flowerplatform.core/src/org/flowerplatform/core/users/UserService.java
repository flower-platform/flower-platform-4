/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.core.users;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.flowerplatform.core.node.remote.Node;

/**
 * @author Mariana Gheorghe
 */
@Path("/users")
public class UserService {

	private List<Node> users = new ArrayList<Node>();
	
	/**
	 * @author Mariana Gheorghe
	 */
	public UserService() {
		users.add(newTestUser("John"));
		users.add(newTestUser("Jane"));
		users.add(newTestUser("Jim"));
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
	 * @author Mariana Gheorghe
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
	 * @author Mariana Gheorghe
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
	 * @author Mariana Gheorghe
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
	
}
