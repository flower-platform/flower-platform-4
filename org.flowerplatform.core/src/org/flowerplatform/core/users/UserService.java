package org.flowerplatform.core.users;

import static org.flowerplatform.core.CoreConstants.NAME;
import static org.flowerplatform.core.CoreConstants.SOCIAL_ACCOUNT;
import static org.flowerplatform.core.CoreConstants.SOCIAL_ACCOUNT_INFO;
import static org.flowerplatform.core.CoreConstants.USER;
import static org.flowerplatform.core.CoreConstants.USER_AVATAR;
import static org.flowerplatform.core.CoreConstants.USER_EMAIL;
import static org.flowerplatform.core.CoreConstants.USER_HASH_PASSWORD;
import static org.flowerplatform.core.CoreConstants.USER_LOGIN;
import static org.flowerplatform.core.CoreConstants.USER_PASSWORD;
import static org.flowerplatform.core.CoreConstants.USER_SALT_PASSWORD;
import static org.flowerplatform.core.CoreConstants.USER_SOCIAL_ACCOUNTS;
import static org.flowerplatform.core.CoreConstants.USER_WEBSITE;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mariana Gheorghe
 */
@Path("/users")
public class UserService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	
	private UserValidator userValidator = new UserValidator();
	
	// TODO persist
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
		new ResourceServiceRemote().reload(CoreConstants.USERS_PATH);
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
			String username = (String) properties.get(USER_LOGIN);
			user.setNodeUri(Utils.getUri("fpp", "|.users", username));
			user.setType(CoreConstants.USER);
			CorePlugin.getInstance().getNodeService().addChild(parent, user, new ServiceContext<NodeService>());
			
			String password = (String) properties.remove(USER_PASSWORD);
			if (password == null) {
				password = "a";
			}
			// set saltPassword + hashPassword for active user
			String saltPassword = DatatypeConverter.printBase64Binary(getSalt());
			String hashPassword = DatatypeConverter.printBase64Binary(createPasswordHash(password, saltPassword));
			CorePlugin.getInstance().getNodeService().setProperty(user, USER_SALT_PASSWORD, saltPassword, new ServiceContext<NodeService>());
			CorePlugin.getInstance().getNodeService().setProperty(user, USER_HASH_PASSWORD, hashPassword, new ServiceContext<NodeService>());
			
			LOGGER.debug("User created: {}", user.getNodeUri());
		} else {
			user = CorePlugin.getInstance().getResourceService().getNode(user.getNodeUri());
			
			LOGGER.debug("User updated: {}", user.getNodeUri());
		}
		
		properties.remove("nodeType");
		properties.remove(CoreConstants.IS_DIRTY);
		properties.remove(CoreConstants.HAS_CHILDREN);
		CorePlugin.getInstance().getNodeService().setProperties(user, properties, new ServiceContext<NodeService>());
		
		CorePlugin.getInstance().getResourceService().save(parent.getNodeUri(), new ServiceContext<ResourceService>());
		
		return stripPassword(user);
	}
	
	/**
	 * Delete the user with <code>nodeUri</code>.
	 */
	@DELETE @Path("/{nodeUri}")
	public void deleteUser(@PathParam("nodeUri") String nodeUri) {
		Node user = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		unlinkAllSocialAccounts(user);
		
		Node users = CorePlugin.getInstance().getResourceService().getNode(CoreConstants.USERS_PATH);
		CorePlugin.getInstance().getNodeService().removeChild(users, user, 
				new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
		CorePlugin.getInstance().getResourceService().save(users.getNodeUri(), new ServiceContext<ResourceService>());
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
		String username = loginInfo.get(USER_LOGIN);
		String password = loginInfo.get(USER_PASSWORD);
		Node user = login(username, password);
		if (user == null) {
			throw new RuntimeException("Invalid username or password");
		}
		
		// link social account
		String socialAccount = (String) loginInfo.get(SOCIAL_ACCOUNT);
		if (socialAccount != null) {
			linkSocialAccount(socialAccount, user);
			// TODO copy all props from session? (e.g. avatar, website)
			// what about token?
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
			return login(user);
		}
		
		return null;
	}
	
	/**
	 * Perform login.
	 * 
	 * @return logged in user
	 */
	public Node login(Node user) {
		if (CorePlugin.getInstance().getRequestThreadLocal().get() != null) {
			userValidator.setCurrentUserPrincipal(
					CorePlugin.getInstance().getRequestThreadLocal().get().getSession(), 
					new UserPrincipal(user));
		}
		LOGGER.debug("Login user: {}", user.getNodeUri());
		
		return user;
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
	public Node register(Map<String, Object> registerInfo) {
		// TODO validate unique login
		String username = (String) registerInfo.get(USER_LOGIN);
		String password = (String) registerInfo.get(USER_PASSWORD);
		
		if (password == null) {
			throw new RuntimeException("Invalid password.");
		}
		
		LOGGER.debug("Register user: {}", username);
		
		Node user = new Node(null, USER);
		Utils.copyProperties(registerInfo, user.getProperties(), USER_LOGIN, USER_PASSWORD, NAME, USER_EMAIL);
		
		// create
		saveUser(user);
		
		// link social account
		String socialAccount = (String) registerInfo.get(SOCIAL_ACCOUNT);
		if (socialAccount != null) {
			linkSocialAccount(socialAccount, user);
			// TODO copy all props from session? (e.g. avatar, website)
			// what about token?
			@SuppressWarnings("unchecked")
			Map<String, Object> socialAccountInfo = (Map<String, Object>) CorePlugin.getInstance().getRequestThreadLocal()
					.get().getSession().getAttribute(SOCIAL_ACCOUNT_INFO);
			Map<String, Object> properties = new HashMap<String, Object>();
			Utils.copyProperties(socialAccountInfo, properties, USER_AVATAR, USER_WEBSITE);
			CorePlugin.getInstance().getNodeService().setProperties(user, properties, new ServiceContext<NodeService>());
			new ResourceServiceRemote().save(CoreConstants.USERS_PATH);
		}
		
		// login
		login(username, password);
		
		return stripPassword(user);
	}
	
	/**
	 * @return social account info from session attributes
	 */
	@GET @Path("/socialAccountInfo")
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSocialAccountInfo() {
		Map<String, Object> socialAccountInfo = (Map<String, Object>) CorePlugin.getInstance().getRequestThreadLocal()
				.get().getSession().getAttribute(SOCIAL_ACCOUNT_INFO);
		Map<String, Object> result = new HashMap<String, Object>();
		Utils.copyProperties(socialAccountInfo, result, USER_LOGIN, NAME, USER_EMAIL, SOCIAL_ACCOUNT);
		return result;
	}
	
	/**
	 * <b>Note:</b> social account format: <tt>username@github</tt> (e.g. <tt>john-doe@github</tt>)
	 * 
	 * @return the user who has linked this social account
	 */
	public Node getUserForSocialAccount(String socialAccount) {
		String uri = socialAccounts.get(socialAccount);
		if (uri == null) {
			return null;
		}
		return getUser(uri);
	}
	
	/**
	 * 
	 * @param socialAccount
	 * @param username
	 */
	public void linkSocialAccount(String socialAccount, Node user) {
		LOGGER.debug("Link social account: {} for user account: {}", socialAccount, user.getNodeUri());
		
		socialAccounts.put(socialAccount, user.getNodeUri());
		ServiceContext<NodeService> context = new ServiceContext<NodeService>();
		String existing = (String) user.getPropertyValue(USER_SOCIAL_ACCOUNTS);
		String added = socialAccount;
		if (existing != null) {
			added = existing + ',' + socialAccount;
		}
		CorePlugin.getInstance().getNodeService().setProperty(user, USER_SOCIAL_ACCOUNTS, added, context);
		new ResourceServiceRemote().save(CoreConstants.USERS_PATH);
	}
	
	/**
	 * 
	 * @param socialAccount
	 * @param user
	 */
	public void unlinkSocialAccount(String socialAccount, Node user) {
		LOGGER.debug("Unlink social account: {} for user account: {}", socialAccount, user.getNodeUri());
		
		socialAccounts.remove(socialAccount);
		NodeService service = CorePlugin.getInstance().getNodeService();
		ServiceContext<NodeService> context = new ServiceContext<NodeService>(service);
		String existing = (String) user.getPropertyValue(USER_SOCIAL_ACCOUNTS);
		existing.replace(socialAccount, "");
		if (existing.length() == 0) {
			// this was the only linked social account
			service.unsetProperty(user, USER_SOCIAL_ACCOUNTS, context);
			return;
		}
		
		if (existing.startsWith(",")) {
			// this was the first linked social account
			existing.substring(1);
		} else if (existing.endsWith(",")) {
			// this was the last linked social account
			existing.substring(0, existing.length() - 1);
		} else {
			existing.replace(",,", ",");
		}
		
		service.setProperty(user, USER_SOCIAL_ACCOUNTS, existing, context);
	}
	
	/**
	 * 
	 * @param user
	 */
	public void unlinkAllSocialAccounts(Node user) {
		String existing = (String) user.getPropertyValue(USER_SOCIAL_ACCOUNTS);
		if (existing == null) {
			// no linked social accounts
			return;
		}
		
		for (String socialAccount : existing.split(",")) {
			LOGGER.debug("Unlink social account: {} for user account: {}", socialAccount, user.getNodeUri());
			socialAccounts.remove(socialAccount);
		}
		
		NodeService service = CorePlugin.getInstance().getNodeService();
		ServiceContext<NodeService> context = new ServiceContext<NodeService>(service);
		service.unsetProperty(user, USER_SOCIAL_ACCOUNTS, context);
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
			CorePlugin.getInstance().getNodeService().setProperty(currentUser, USER_SALT_PASSWORD, saltPassword, new ServiceContext<NodeService>());
			CorePlugin.getInstance().getNodeService().setProperty(currentUser, USER_HASH_PASSWORD, hashPassword, new ServiceContext<NodeService>());
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
		CorePlugin.getInstance().getNodeService().setProperty(user, USER_LOGIN, login, new ServiceContext<NodeService>());
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
        String storedPasswordHash = (String) user.getPropertyValue(USER_HASH_PASSWORD);
        String salt = (String) user.getPropertyValue(USER_SALT_PASSWORD);
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
		user.getProperties().remove(USER_SALT_PASSWORD);
		user.getProperties().remove(USER_HASH_PASSWORD);
		return user;
	}
	
}
