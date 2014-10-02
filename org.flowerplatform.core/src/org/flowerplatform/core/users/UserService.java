package org.flowerplatform.core.users;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;

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
	
	public UserService() {
	}
	
	/**
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@GET
	public List<Node> getUsers() {
		new ResourceServiceRemote().subscribeToParentResource(CoreConstants.USERS_PATH);
		Node node =  CorePlugin.getInstance().getResourceService().getNode(CoreConstants.USERS_PATH);
		
		ServiceContext<NodeService> context = new ServiceContext<NodeService>();
		context.add(CoreConstants.POPULATE_WITH_PROPERTIES, true);
		List<Node> users = CorePlugin.getInstance().getNodeService().getChildren(node, context);
			for (Node user : users) {
				user.setNodeUri(URLEncoder.encode(user.getNodeUri()));
			}
	
		return users;
	}
	
	/**
	 * @param nodeUri
	 * @return
	 */
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
	
	/**
	 * @param user
	 * @return
	 * @throws UnsupportedEncodingException
	 */
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
		
		Node currentUser = CorePlugin.getInstance().getResourceService().getNode(user.getNodeUri());
		
		// set saltPassword + hashPassword for active user
		/* String saltPassword = DatatypeConverter.printBase64Binary(getSalt());
		String hashPassword = DatatypeConverter.printBase64Binary(createPasswordHash((String)user.getProperties().get("password"), saltPassword));
		CorePlugin.getInstance().getNodeService().setProperty(currentUser, "saltPassword", saltPassword, new ServiceContext<NodeService>());
		CorePlugin.getInstance().getNodeService().setProperty(currentUser, "hashPassword", hashPassword, new ServiceContext<NodeService>()); */
		
		CorePlugin.getInstance().getNodeService().setProperty(currentUser, "firstName", user.getProperties().get("firstName"),  new ServiceContext<NodeService>());
		CorePlugin.getInstance().getNodeService().setProperty(currentUser, "lastName", user.getProperties().get("lastName"), new ServiceContext<NodeService>());
		CorePlugin.getInstance().getNodeService().setProperty(currentUser, "email", user.getProperties().get("email"), new ServiceContext<NodeService>());
		CorePlugin.getInstance().getNodeService().setProperty(currentUser, "login", user.getProperties().get("login"),  new ServiceContext<NodeService>());
		
		return currentUser;
	}
	
	/**
	 * @param nodeUri
	 */
	@DELETE @Path("/{nodeUri}")
	public void deleteUser(@PathParam("nodeUri") String nodeUri) {
		
		CorePlugin.getInstance().getNodeService().removeChild(
				CorePlugin.getInstance().getResourceService().getNode(CoreConstants.USERS_PATH), 
				CorePlugin.getInstance().getResourceService().getNode(nodeUri), 
				new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
	}
	
	/**
	 * @param nodeUri
	 * @param map
	 * @return
	 */
	@POST @Path("/{nodeUri}/password")
	public String changePassword(@PathParam("nodeUri") String nodeUri, Map<String, String> map) {
		Node currentUser = CorePlugin.getInstance().getResourceService().getNode(nodeUri);

		if (map.get("oldPassword").equals(currentUser.getPropertyValue("password"))) {
			CorePlugin.getInstance().getNodeService().setProperty(currentUser, "password", map.get("newPassword"), new ServiceContext<NodeService>());
			return CoreConstants.PASS_CHANGED;
		}
		
		//check oldPassword hash + set the newPassword hash
		/* if (checkPassword(currentUser, map.get("oldPassword"))) {
			String saltPassword = DatatypeConverter.printBase64Binary(getSalt());
			String hashPassword = DatatypeConverter.printBase64Binary(createPasswordHash(map.get("newPassword"), saltPassword));
			CorePlugin.getInstance().getNodeService().setProperty(currentUser, "saltPassword", saltPassword, new ServiceContext<NodeService>());
			CorePlugin.getInstance().getNodeService().setProperty(currentUser, "hashPassword", hashPassword, new ServiceContext<NodeService>());
		}*/
		
		return CoreConstants.PASS_NOT_CHANGED;
	}
	
	/**
	 * @param nodeUri
	 * @param login
	 * @return
	 */
	@POST @Path("/{nodeUri}/login") 
	public Node changeLogin(@PathParam("nodeUri") String nodeUri, String login) {
		Node currentUser = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		
		CorePlugin.getInstance().getNodeService().setProperty(currentUser, "login", login, new ServiceContext<NodeService>());

		return currentUser;
	}
	
	/**
	 * Generate a salt for password.
	 */
	public byte[] getSalt() {
	    byte[] salt = new byte[16];
	    new Random().nextBytes(salt);
	    return salt;
	}
	
	/**
	 * Create a hashed password using salt.
	 */
	public byte[] createPasswordHash(String password, String salt) {
		final Charset charset = Charset.forName("UTF-8");
		byte[] result = null;

		try {
			MessageDigest digest = MessageDigest.getInstance(CoreConstants.HASH_ALGORITHM);
			digest.update(salt.getBytes(charset));
			digest.update(password.getBytes(charset));
			result = digest.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	/**
	 * Check if the entered password is the same with the hashed password.
	 */
	public boolean checkPassword(Node user, String password) {
        boolean result = false;
        String storedPasswordHash = (String) user.getProperties().get("hashPassword");
        String salt = (String) user.getProperties().get("saltPassword");
        byte[] checkPasswordHashBytes = createPasswordHash(password, salt);
        String checkPasswordHash = DatatypeConverter.printBase64Binary(checkPasswordHashBytes);
 
        if (checkPasswordHash != null && storedPasswordHash != null && checkPasswordHash.equals(storedPasswordHash)) {
        	result = true;
        }
        
       return result;
    }
	
}
