package org.flowerplatform.core.users;

import static org.flowerplatform.core.CoreConstants.USER;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
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
import org.flowerplatform.util.Utils;

/**
 * @author Mariana Gheorghe
 */
@Path("/users")
public class UserService {
	
	private UserValidator userValidator = new UserValidator();
	
	private Map<String, String> socialAccounts = new HashMap<String, String>();
	
	public UserService() {
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////
	// CRUD operations for user
	//////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * @return users list
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

		List<Node> stripped = new ArrayList<Node>();
		for (Node user : users) {
			stripped.add(stripPassword(user));
		}
		return stripped;
	}
	
	/**
	 * @return the user with <code>nodeUri</code>
	 */
	@GET @Path("/{nodeUri}")
	@Produces(MediaType.APPLICATION_JSON)
	public Node getUser(@PathParam("nodeUri") String nodeUri) {
		ServiceContext<ResourceService> context = new ServiceContext<ResourceService>();
		context.add(CoreConstants.POPULATE_WITH_PROPERTIES, true);
		Node user = CorePlugin.getInstance().getResourceService().getNode(nodeUri, context);
		
		if (user != null) {
			return stripPassword(user);
		}

		return null;
	}
	
	/**
	 * @return newly created or updated user
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Node saveUser(Node user) {
		Node parent = CorePlugin.getInstance().getResourceService().getNode(CoreConstants.USERS_PATH);
		
		Map<String, Object> properties = user.getProperties();
		user.setProperties(null);
		if (user.getNodeUri() == null) {
			user.setNodeUri(Utils.getUri("fpp", "|.users", (String) properties.get("login")));
			user.setType(CoreConstants.USER);
			CorePlugin.getInstance().getNodeService().addChild(parent, user, new ServiceContext<NodeService>());
			
			String password = "a"; // TODO remove; password should come from the UI
			// set saltPassword + hashPassword for active user
			String saltPassword = DatatypeConverter.printBase64Binary(getSalt());
			String hashPassword = DatatypeConverter.printBase64Binary(createPasswordHash(password, saltPassword));
			CorePlugin.getInstance().getNodeService().setProperty(user, "saltPassword", saltPassword, new ServiceContext<NodeService>());
			CorePlugin.getInstance().getNodeService().setProperty(user, "hashPassword", hashPassword, new ServiceContext<NodeService>());
		} else {
			user = CorePlugin.getInstance().getResourceService().getNode(user.getNodeUri());
		}
		
		// TODO the password set from the client should not be saved in DB
		properties.remove("nodeType");
		properties.remove("isDirty");
		properties.remove("hasChildren");
		CorePlugin.getInstance().getNodeService().setProperties(user, properties, new ServiceContext<NodeService>());
		
		CorePlugin.getInstance().getResourceService().save(parent.getNodeUri(), new ServiceContext<ResourceService>());
		
		return stripPassword(user);
	}
	
	/**
	 * Delete the user with <code>nodeUri</code>.
	 */
	@DELETE @Path("/{nodeUri}")
	public void deleteUser(@PathParam("nodeUri") String nodeUri) {
		CorePlugin.getInstance().getNodeService().removeChild(
				CorePlugin.getInstance().getResourceService().getNode(CoreConstants.USERS_PATH), 
				CorePlugin.getInstance().getResourceService().getNode(nodeUri), 
				new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
		String resourceUri = CorePlugin.getInstance().getResourceService().getResourceNode(nodeUri).getNodeUri();
		CorePlugin.getInstance().getResourceService().save(resourceUri, new ServiceContext<ResourceService>());
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////
	// Login + Logout + Register
	//////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * @return the current user (saved in the session)
	 */
	@GET @Path("/login")
	public Node getCurrentUser() {
		HttpServletRequest req = CorePlugin.getInstance().getRequestThreadLocal().get();
		if (req == null) {
			return null;
		}
		return stripPassword(getCurrentUser(req));
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
		String password = loginInfo.get("password");
		Node user = login(username, password);
		if (user == null) {
			throw new RuntimeException("Invalid username or password");
		}
		return stripPassword(user);
	}
	
	/**
	 * Perform login.
	 * 
	 * @return logged in user
	 */
	public Node login(String username, String password) {
		// get user
		String nodeUri = Utils.getUri("fpp", "|.users", username);
		Node user = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		if (user == null) {
			return null;
		}
		
		// validate password
		boolean validPass = checkPassword(user, password);
		if (validPass) {
			if (CorePlugin.getInstance().getRequestThreadLocal().get() != null) {
				userValidator.setCurrentUserPrincipal(
						CorePlugin.getInstance().getRequestThreadLocal().get().getSession(), 
						new UserPrincipal(user));
			}
			return user;
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
	
	/**
	 * Perform register.
	 * 
	 * return registered user
	 */
	@POST @Path("/register")
	public Node register(Map<String, String> registerInfo) {
		// TODO validate unique login
		String username = registerInfo.get("username");
		String password = registerInfo.get("password");
		
		if (password == null) {
			throw new RuntimeException("Invalid password.");
		}
		
		Node user = new Node(null, USER);
		user.getProperties().put("login", username);
		user.getProperties().put("email", registerInfo.get("email"));
		// TODO copy all props
		
		// create
		saveUser(user);
		
		// login
		login(username, password);
		
		// link social account
		String socialAccount = registerInfo.get("socialAccounts");
		linkSocialAccount(socialAccount, user);
		
		return stripPassword(user);
	}
	
	/**
	 * @return the user who has linked this social account
	 */
	public String getUserForSocialAccount(String socialAccount) {
		return socialAccounts.get(socialAccount);
	}
	
	/**
	 * 
	 * @param socialAccount
	 * @param username
	 */
	public void linkSocialAccount(String socialAccount, Node user) {
		socialAccounts.put(socialAccount, user.getNodeUri());
		ServiceContext<NodeService> context = new ServiceContext<NodeService>();
		String existing = (String) user.getPropertyValue("socialAccounts");
		String added = socialAccount;
		if (existing != null) {
			added = existing + ',' + socialAccount;
		}
		CorePlugin.getInstance().getNodeService().setProperty(user, "socialAccounts", added, context);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////
	// Set/validate passwords
	//////////////////////////////////////////////////////////////////////////////////////////
	
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
		Node user = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		CorePlugin.getInstance().getNodeService().setProperty(user, "login", login, new ServiceContext<NodeService>());
		return stripPassword(user);
	}
	
	/**
	 * Generate a salt for password.
	 */
	private byte[] getSalt() {
	    byte[] salt = new byte[16];
	    new Random().nextBytes(salt);
	    return salt;
	}
	
	/**
	 * Create a hashed password using salt.
	 */
	private byte[] createPasswordHash(String password, String salt) {
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
	private boolean checkPassword(Node user, String password) {
        String storedPasswordHash = (String) user.getPropertyValue("hashPassword");
        String salt = (String) user.getPropertyValue("saltPassword");
        byte[] checkPasswordHashBytes = createPasswordHash(password, salt);
        String checkPasswordHash = DatatypeConverter.printBase64Binary(checkPasswordHashBytes);
 
        if (checkPasswordHash != null && storedPasswordHash != null && checkPasswordHash.equals(storedPasswordHash)) {
        	return true;
        }
        
       return false;
    }
	
	private Node stripPassword(Node user) {
		if (user == null) {
			return null;
		}
		user.getProperties().remove("saltPassword");
		user.getProperties().remove("hashPassword");
		return user;
	}
	
}
