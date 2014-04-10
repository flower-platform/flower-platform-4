package org.flowerplatform.users;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/userService")
public class UserService {

	public UserService() {
		System.out.println("created");
	}
	
	@GET
	@Path("/hello/{name}")
	@Produces(MediaType.TEXT_PLAIN)
	public String hello(@PathParam("name") String name) {
		return "Welcome " + name;
	}
	
	@GET
	@Path("/get/{login}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@PathParam("login") String login) {
		User user = new User();
		user.setLogin(login);
		user.setName(login + " " + login + "son");
		return user;
	}
}
