package org.flowerplatform.core.users;

import static org.flowerplatform.core.CoreConstants.EXECUTE_ONLY_FOR_UPDATER;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ResourceServiceRemote;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceService;

/**
 * @author Mariana Gheorghe
 */
@Path("/users")
public class UserService {

	private List<Node> users = new ArrayList<Node>();
	
	public UserService() {
	}
	
//	private Node newTestUser(String login) {
//		Node node = new Node("user:test|" + login, "user");
//		node.getProperties().put("login", login);
//		node.getProperties().put("name", login + " " + login + "son");
//		node.getProperties().put("email", login + "@domain.com");
//		
//		//set an admin
//		if (login.equals("Jim")) {
//			node.getProperties().put("isAdmin", true);
//		}
//		return node;
//	}
	
	@SuppressWarnings("deprecation")
	@GET
	public List<Node> getUsers() {
		new ResourceServiceRemote().subscribeToParentResource(CoreConstants.USERS_PATH);
		Node node =  CorePlugin.getInstance().getResourceService().getNode(CoreConstants.USERS_PATH);
		
		ServiceContext<NodeService> context = new ServiceContext<NodeService>();
		context.add(CoreConstants.POPULATE_WITH_PROPERTIES, true);
		users = CorePlugin.getInstance().getNodeService().getChildren(node,context);
			for (Node user : users){
				user.setNodeUri(URLEncoder.encode(user.getNodeUri()));
			}
	
		return users;
	}
	
	@GET @Path("/{nodeUri}")	
	@Produces(MediaType.APPLICATION_JSON)
	public Node getUser(@PathParam("nodeUri") String nodeUri) {
		
		ServiceContext<ResourceService> context = new ServiceContext<ResourceService>();
		context.add(CoreConstants.POPULATE_WITH_PROPERTIES, true);
		Node user = CorePlugin.getInstance().getResourceService().getNode(nodeUri, context);
		
		if (user != null) {
			return user;
		}

		return null;
	}
	
	@SuppressWarnings("deprecation")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Node saveUser(Node user) throws UnsupportedEncodingException {

	 Node parent = CorePlugin.getInstance().getResourceService().getNode(CoreConstants.USERS_PATH);
		if (user.getType() == null) {
			user.setType(CoreConstants.USER);
			CorePlugin.getInstance().getNodeService().addChild(
					parent, 
					user, 
					new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()).add(CoreConstants.POPULATE_WITH_PROPERTIES, true));
			
		}
		
		//CorePlugin.getInstance().getNodeService().setProperty(user, "name", user.getPropertyValue("name"),  new ServiceContext<NodeService>());
		//CorePlugin.getInstance().getNodeService().setProperty(user, "email", user.getPropertyValue("email"), new ServiceContext<NodeService>());
		//CorePlugin.getInstance().getNodeService().setProperty(user, "login", user.getPropertyValue("login"),  new ServiceContext<NodeService>());
		
		user.getProperties().put("login", user.getPropertyValue("login"));
		
		return user;
	}
	
	@DELETE @Path("/{nodeUri}")
	public void deleteUser(@PathParam("nodeUri") String nodeUri) {
		
		CorePlugin.getInstance().getNodeService().removeChild(
				CorePlugin.getInstance().getResourceService().getNode(CoreConstants.USERS_PATH), 
				CorePlugin.getInstance().getResourceService().getNode(nodeUri), 
				new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
	}
	
}
