package org.flowerplatform.tests.core.repositories;
//CHECKSTYLE:OFF
import static org.flowerplatform.core.CoreConstants.FLOWER_PLATFORM_WORKSPACE;
import static org.flowerplatform.core.repositories.RepositoriesService.fromStringListToExtensionInfoInFile;
import static org.flowerplatform.core.repositories.RepositoriesService.getExtensionInfoInFile;
import static org.flowerplatform.core.repositories.RepositoriesService.getExtensionMetadataForExtensionId;
import static org.flowerplatform.tests.EclipseIndependentTestSuite.startPlugin;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.core.repositories.ExtensionInfoInFile;
import org.flowerplatform.core.repositories.ExtensionMetadata;
import org.flowerplatform.core.repositories.RepositoriesService;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.flowerplatform.tests.EclipseIndependentTestSuite;
import org.flowerplatform.util.StringList;
import org.flowerplatform.util.Utils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Cristina Brinza
 *
 */

public class RepositoriesServiceTest {

	public static final String REPOSITORIES = "repositories";
	public static final String USERS_FILE = ".users";
	public static final String USERS_FILE_URI = "fpp:repositories|.users";
	public static final String USERS_URI = USERS_FILE_URI + "#" + CoreConstants.USERS;
	public static final String REPOSITORIES_URI = USERS_FILE_URI + "#" + CoreConstants.REPOSITORIES;
	public static ResourceService resourceService;
	public RepositoriesService repositoriesService; 
	public ServiceContext<ResourceService> context; 
	public static Node users;
	public static Node repos;

	/**
	 * @author see class
	 */
	@BeforeClass
	public static void beforeClass() throws Exception {
		startPlugin(new FreeplanePlugin());

		// create test file
		File repositories = new File(FLOWER_PLATFORM_WORKSPACE + "/repositories");
		if (!repositories.exists()) {
			repositories.mkdirs();
		}

		if (!(new File(FLOWER_PLATFORM_WORKSPACE + "/" + REPOSITORIES + "/" + USERS_FILE)).exists()) {
			String repositoriesUri = Utils.getUri(CoreConstants.FILE_SCHEME, "repositories|");
			Node parent = CorePlugin.getInstance().getResourceService().getNode(repositoriesUri);
			Node child = new Node(USERS_FILE_URI, CoreConstants.FILE_NODE_TYPE);

			ServiceContext<NodeService> context = new ServiceContext<NodeService>();
			context.getContext().put(CoreConstants.NAME, USERS_FILE);
			context.getContext().put(CoreConstants.FILE_IS_DIRECTORY, false);

			CorePlugin.getInstance().getNodeService().addChild(parent, child, context);

			// add users and repositories nodes
			resourceService = CorePlugin.getInstance().getResourceService();
			CorePlugin.getInstance().getResourceService().subscribeToParentResource("dummy-session", USERS_FILE_URI, new ServiceContext<ResourceService>(resourceService));
			Node root = resourceService.getNode(USERS_FILE_URI);

			// users node
			users = new Node(USERS_URI, CoreConstants.USERS);
			CorePlugin.getInstance().getNodeService().addChild(
					root, 
					users,
					new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));

			// repositories node
			repos = new Node(REPOSITORIES_URI, CoreConstants.REPOSITORIES);
			CorePlugin.getInstance().getNodeService().addChild(
					root, 
					repos, 
					new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));

			// save file
			resourceService.save(USERS_FILE_URI, new ServiceContext<ResourceService>(resourceService));

			// user1
			Node user = new Node(USERS_FILE_URI + "#user1-random1", CoreConstants.USER);
			CorePlugin.getInstance().getNodeService().addChild(
					users, 
					user, 
					new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
			user = CorePlugin.getInstance().getResourceService().getNode(user.getNodeUri());
			CorePlugin.getInstance().getNodeService().setProperty(user, "firstName", "user1", new ServiceContext<NodeService>());
			CorePlugin.getInstance().getNodeService().setProperty(user, "login", "user1-random1", new ServiceContext<NodeService>());
			CorePlugin.getInstance().getNodeService().setProperty(user, "lastName", "user1", new ServiceContext<NodeService>());
			CorePlugin.getInstance().getNodeService().setProperty(user, "email", "user@1", new ServiceContext<NodeService>());
			CorePlugin.getInstance().getNodeService().setProperty(user, "name", "user1", new ServiceContext<NodeService>());
			CorePlugin.getInstance().getNodeService().setProperty(user, "isAdmin", "false", new ServiceContext<NodeService>());
			
			// user2
			user = new Node(USERS_FILE_URI + "#user2-random2", CoreConstants.USER);
			CorePlugin.getInstance().getNodeService().addChild(
					users,
					user,
					new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
			user = CorePlugin.getInstance().getResourceService().getNode(user.getNodeUri());
			CorePlugin.getInstance().getNodeService().setProperty(user, "firstName", "user2", new ServiceContext<NodeService>());
			CorePlugin.getInstance().getNodeService().setProperty(user, "login", "user2-random2", new ServiceContext<NodeService>());
			CorePlugin.getInstance().getNodeService().setProperty(user, "lastName", "user2", new ServiceContext<NodeService>());
			CorePlugin.getInstance().getNodeService().setProperty(user, "email", "user@2", new ServiceContext<NodeService>());
			CorePlugin.getInstance().getNodeService().setProperty(user, "name", "user2", new ServiceContext<NodeService>());
			CorePlugin.getInstance().getNodeService().setProperty(user, "isAdmin", "true", new ServiceContext<NodeService>());
			
			// user3
			user = new Node(USERS_FILE_URI + "#user3-random3", CoreConstants.USER);
			CorePlugin.getInstance().getNodeService().addChild(
					users,
					user,
					new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
			user = CorePlugin.getInstance().getResourceService().getNode(user.getNodeUri());
			CorePlugin.getInstance().getNodeService().setProperty(user, "firstName", "user3", new ServiceContext<NodeService>());
			CorePlugin.getInstance().getNodeService().setProperty(user, "login", "user3-random3", new ServiceContext<NodeService>());
			CorePlugin.getInstance().getNodeService().setProperty(user, "lastName", "user3", new ServiceContext<NodeService>());
			CorePlugin.getInstance().getNodeService().setProperty(user, "email", "user@3", new ServiceContext<NodeService>());
			CorePlugin.getInstance().getNodeService().setProperty(user, "name", "user3", new ServiceContext<NodeService>());
			CorePlugin.getInstance().getNodeService().setProperty(user, "isAdmin", "false", new ServiceContext<NodeService>());
			
			// save file
			resourceService.save(USERS_FILE_URI, new ServiceContext<ResourceService>(resourceService));
		}
		FileUtils.copyFileToDirectory(new File("workspace/repositories/.users"), new File("workspace/"));
		CorePlugin.getInstance().getResourceService().subscribeToParentResource("dummy-session", CoreConstants.USERS_PATH, new ServiceContext<ResourceService>(resourceService));
	}
	
	/**
	 * @author see class
	 */
	@Before
	public void before() {
		context = new ServiceContext<ResourceService>();
		context.add(CoreConstants.POPULATE_WITH_PROPERTIES, true);
		
		repositoriesService = new RepositoriesService();
	}

	/**
	 * @author see class
	 */

	@Test
	public void testCreateRepository() throws IOException {
		String testLogin = "user1-random1";
		String testRepoName = "firstRepo";
		Node testRepository = new Node();
		
		testRepository.getProperties().put(CoreConstants.USER, testLogin);
		testRepository.getProperties().put(CoreConstants.NAME, testRepoName);
		testRepository.getProperties().put(CoreConstants.DESCRIPTION, "descriere");
		
		repositoriesService.saveRepository(testRepository);
		
		// check if repository dir is created
		assertTrue("Repository directory not created", new File(FLOWER_PLATFORM_WORKSPACE + "/" + testLogin + "/" + testRepoName).exists());
		
		// check if Repository node is created
		assertTrue("Repository Node not created", resourceService.getNode(CoreUtils.getRepositoryNodeUri(testLogin, testRepoName)) != null);
		
		String repositoryName = CoreUtils.getRepositoryName(testLogin, testRepoName);		
		Node user = resourceService.getNode(CoreConstants.USERS_PATH + "#" + testLogin);
		
		// check if repo is in owned_repositories
		@SuppressWarnings("unchecked")
		List<String> ownedRepositories = (List<String>) user.getPropertyValue(CoreConstants.OWNED_REPOSITORIES);
		assertTrue("Repo not in owned repositories", ownedRepositories.contains(repositoryName));
			
		// create second repo for same user
		String testRepoName2 = "secondRepo";
		testRepository = new Node();
		
		// test create Repository		
		testRepository.getProperties().put(CoreConstants.USER, testLogin);
		testRepository.getProperties().put(CoreConstants.NAME, testRepoName2);
		testRepository.getProperties().put(CoreConstants.DESCRIPTION, "descriere2");
		
		repositoriesService.saveRepository(testRepository);
		
		// check if repository dir is created
		assertTrue("Repository directory not created", new File(FLOWER_PLATFORM_WORKSPACE + "/" + testLogin + "/" + testRepoName2).exists());
		
		// check if Repository node is created
		assertTrue("Repository Node not created", resourceService.getNode(CoreUtils.getRepositoryNodeUri(testLogin, testRepoName2)) != null);
		
		// create repo with same name as firstRepo for another user
		String testLogin2 = "user2-random2";
		testRepository = new Node();
		testRepository.getProperties().put(CoreConstants.USER, testLogin2);
		testRepository.getProperties().put(CoreConstants.NAME, testRepoName);
		testRepository.getProperties().put(CoreConstants.DESCRIPTION, "descriere2");
		
		repositoriesService.saveRepository(testRepository);
		
		// check if repository dir is created
		assertTrue("Repository directory not created", new File(FLOWER_PLATFORM_WORKSPACE + "/" + testLogin2 + "/" + testRepoName).exists());
		
		// check if Repository node is created
		assertTrue("Repository Node not created", resourceService.getNode(CoreUtils.getRepositoryNodeUri(testLogin2, testRepoName)) != null);
	}

	/**
	 * @author see class
	 */
	@Test(expected = RuntimeException.class)
	public void testCreateRepositoryException() throws IOException {
		String testLogin = "user3-random3";
		String testRepoName = "repo";
		Node testRepository = new Node();
		
		// test create Repository		
		testRepository.getProperties().put(CoreConstants.USER, testLogin);
		testRepository.getProperties().put(CoreConstants.NAME, testRepoName);
		testRepository.getProperties().put(CoreConstants.DESCRIPTION, "descriere");
		
		testRepository = repositoriesService.saveRepository(testRepository);
		
		// trying to create repo with same name for same user throws Runtime Exception
		repositoriesService.saveRepository(testRepository);
	}

	/**
	 * @author see clas
 	 */
//	@Test
//	public void test() throws Exception {
//		String login = "user2-random2";
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("repoName", "firstRepo");
//		map.put("login", "user2-random2");
//		map.put("description", "description-firstRepo");
//		repositoriesService.createRepository(login, "firstRepo", "description-firstRepo");
//		repositoriesService.applyExtension(map);
//			
//		map.put("repoName", "add-member");
//		map.put("login", "user2-random2");
//		map.put("description", "description-add-member");
//		repositoriesService.createRepository(login, "add-member", "description-add-member");
//		repositoriesService.applyExtension(map);
//		
//		map.put("repoName", "add-starred-by");
//		map.put("login", "user2-random2");
//		map.put("description", "description-add-starred-by");
//		repositoriesService.createRepository(login, "add-starred-by", "description-add-starred-by");
//		repositoriesService.applyExtension(map);
//		
//		map.put("repoName", "apply-extension");
//		map.put("login", "user2-random2");
//		map.put("description", "description-apply-extension");
//		repositoriesService.createRepository(login, "apply-extension", "description-apply-extension");
//		repositoriesService.applyExtension(map);
//	}
	
	/**
	 * @author see class
	 */
	@Test
	public void testDeleteRepository() throws IOException {
		String testLogin = "user1-random1";
		String testRepoName = "delete-repo";
		Node testDeleteRepository = new Node();
		
		testDeleteRepository.getProperties().put(CoreConstants.USER, testLogin);
		testDeleteRepository.getProperties().put(CoreConstants.NAME, testRepoName);
		testDeleteRepository.getProperties().put(CoreConstants.DESCRIPTION, "descriere");
		
		// create then delete repo
		testDeleteRepository = repositoriesService.saveRepository(testDeleteRepository);
		repositoriesService.deleteRepository(testDeleteRepository.getNodeUri());
		
		// check if directory still exists
		assertTrue("Repository dir still exists!", !new File(FLOWER_PLATFORM_WORKSPACE + "/" + testLogin + "/" + testRepoName).exists());
		
		// check if repository node was deleted from file
		//CHECKSTYLE:OFF
		try {
			resourceService.getNode(CoreUtils.getRepositoryNodeUri(testLogin, testRepoName));
			fail("NullPointerException should have been thrown");
		} catch (NullPointerException e) {
			// ignore this
		}
		//CHECKSTYLE:ON
		
		String repositoryName = CoreUtils.getRepositoryName(testLogin, testRepoName);		
		Node user = resourceService.getNode(CoreUtils.getUriFromFragment(testLogin));
		
		// check in OWNED_REPOSITORIES for owner
		@SuppressWarnings("unchecked")
		List<String> ownedRepositories = (List<String>) user.getPropertyValue(CoreConstants.OWNED_REPOSITORIES);
		assertTrue("Repo still in owned repositories!", !ownedRepositories.contains(repositoryName));
				
		// check every member in MEMBERS
		@SuppressWarnings("unchecked")
		List<String> memberInRepositories = (List<String>) user.getPropertyValue(CoreConstants.MEMBER_IN_REPOSITORIES);
		if (memberInRepositories != null) {
			assertTrue("Repo still in member_in_repositories!", !memberInRepositories.contains(repositoryName));
		}
	}

	/**
	 * @author see class
	 */
	@Test(expected = RuntimeException.class)
	public void testDeleteNoRepository() throws IOException {
		// trying to delete a repository that doesn't exist
		String testLogin = "user1-random1";
		String testRepoName = "null-repo";
		Node testDeleteRepository = new Node();
		testDeleteRepository.getProperties().put(CoreConstants.USER, testLogin);
		testDeleteRepository.getProperties().put(CoreConstants.NAME, testRepoName);
		testDeleteRepository.getProperties().put(CoreConstants.DESCRIPTION, "descriere");
		
		repositoriesService.deleteRepository(testDeleteRepository.getNodeUri());		
	}
	
	/**
	 * @author see class
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testRenameRepository() throws IOException {
		String testLogin = "user1-random1";
		String testRepoName = "repo-to-rename";
		String testRepoNameRenamed = "renamed-repo";
		Node testRenameRepository = new Node();
		
		testRenameRepository.getProperties().put(CoreConstants.USER, testLogin);
		testRenameRepository.getProperties().put(CoreConstants.NAME, testRepoName);
		testRenameRepository.getProperties().put(CoreConstants.DESCRIPTION, "descriere");
		
		// first create repo
		repositoriesService.saveRepository(testRenameRepository);
		testRenameRepository = resourceService.getNode(CoreUtils.getRepositoryNodeUri(testLogin, testRepoName), context);
		testRenameRepository.getProperties().put(CoreConstants.NAME, testRepoNameRenamed);
		
		repositoriesService.saveRepository(testRenameRepository);
		
		// take the repository
		String repositoryNameChanged = CoreUtils.getRepositoryName(testLogin, testRepoNameRenamed);
		Node repository = null;
		try {
			repository = resourceService.getNode(CoreUtils.getRepositoryNodeUri(testLogin, testRepoNameRenamed));
		} catch (NullPointerException e) {
			fail("Repository ID not changed!");
		}
		assertTrue("Repository not renamed!", repository.getPropertyValue(CoreConstants.NAME).equals(testRepoNameRenamed));
		
		assertTrue("Old repository still on file System!", !new File(FLOWER_PLATFORM_WORKSPACE + "/" + testLogin + "/" + testRepoName).exists()); 
		assertTrue("Repository not renamed on file System!", new File(FLOWER_PLATFORM_WORKSPACE + "/" + testLogin + "/" + testRepoNameRenamed).exists());
		
		// check every member has repository renamed
		for (String member : (List<String>) repository.getPropertyValue(CoreConstants.MEMBERS)) {
			Node memberNode = resourceService.getNode(CoreUtils.getUriFromFragment(member));
			assertTrue("Each member should have the repository renamed!", 
					((List<String>) memberNode.getPropertyValue(CoreConstants.MEMBER_IN_REPOSITORIES)).contains(repositoryNameChanged));
		}
		
		// check every user who starred has repository renamed
		for (String memberWhoStarred : (List<String>) repository.getPropertyValue(CoreConstants.STARRED_BY)) {
			Node memberNode = resourceService.getNode(CoreUtils.getUriFromFragment(memberWhoStarred));
			assertTrue("Each member should have the repository renamed!", 
					((List<String>) memberNode.getPropertyValue(CoreConstants.STARRED_REPOSITORIES)).contains(repositoryNameChanged));
		}
	}
	
	/**
	 * @author see class
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testAddMember() throws IOException {
		String owner = "user1-random1";
		String owner2 = "user2-random2";
		String repoName = "add-member";
		String newMember = "user3-random3";
		Node testDeleteRepository = new Node();
		
		testDeleteRepository.getProperties().put(CoreConstants.USER, owner);
		testDeleteRepository.getProperties().put(CoreConstants.NAME, repoName);
		testDeleteRepository.getProperties().put(CoreConstants.DESCRIPTION, "descriere");
	
		repositoriesService.saveRepository(testDeleteRepository);
		repositoriesService.addMember(owner, repoName, newMember);
		
		// check for member in repository 
		Node repository = resourceService.getNode(CoreUtils.getRepositoryNodeUri(owner, repoName));
		assertTrue("Repository doesn't contain new member!", ((List<String>) repository.getPropertyValue(CoreConstants.MEMBERS)).contains(newMember));
		
		// check in MEMBER_IN_REPOSITORIES for member
		Node newMemberNode = resourceService.getNode(CoreUtils.getUriFromFragment(newMember));
		assertTrue("Member doesn't have repository in MEMBER_IN_REPOSITORIES!", 
				((List<String>) newMemberNode.getPropertyValue(CoreConstants.MEMBER_IN_REPOSITORIES)).contains(CoreUtils.getRepositoryName(owner, repoName)));
		
		// check member in two repos with same name - diff owners 
		testDeleteRepository = new Node();
		
		testDeleteRepository.getProperties().put(CoreConstants.USER, owner2);
		testDeleteRepository.getProperties().put(CoreConstants.NAME, repoName);
		testDeleteRepository.getProperties().put(CoreConstants.DESCRIPTION, "descriere");
		repositoriesService.saveRepository(testDeleteRepository);
		repositoriesService.addMember(owner2, repoName, newMember);
		assertTrue("Member in two repos with same name failed!", 
				((List<String>) newMemberNode.getPropertyValue(CoreConstants.MEMBER_IN_REPOSITORIES)).contains(CoreUtils.getRepositoryName(owner2, repoName)));
	}
	
	/**
	 * @author see class
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testAddStarredBy() throws IOException {
		String owner = "user1-random1";
		String owner2 = "user2-random2";
		String repoName = "add-starred-by";
		String userWhoStarred = "user3-random3";
		
		Node testAddStarredBy = new Node();
		testAddStarredBy.getProperties().put(CoreConstants.USER, owner);
		testAddStarredBy.getProperties().put(CoreConstants.NAME, repoName);
		testAddStarredBy.getProperties().put(CoreConstants.DESCRIPTION, "descriere");
		
		repositoriesService.saveRepository(testAddStarredBy);
		repositoriesService.addStarredBy(owner, repoName, userWhoStarred);
		
		// check for member in repository 
		Node repository = resourceService.getNode(CoreUtils.getRepositoryNodeUri(owner, repoName));
		assertTrue("Repository doesn't contain user who starred!", ((List<String>) repository.getPropertyValue(CoreConstants.STARRED_BY)).contains(userWhoStarred));
		
		// check in STARRED_REPOSITORIES for member
		Node newMemberNode = resourceService.getNode(CoreUtils.getUriFromFragment(userWhoStarred));
		assertTrue("Member doesn't have repository in STARRED_REPOSITORIES!", 
				((List<String>) newMemberNode.getPropertyValue(CoreConstants.STARRED_REPOSITORIES)).contains(CoreUtils.getRepositoryName(owner, repoName)));
		
		// check user who starred in two repos with same name - diff owners 
		testAddStarredBy = new Node();
		testAddStarredBy.getProperties().put(CoreConstants.USER, owner2);
		testAddStarredBy.getProperties().put(CoreConstants.NAME, repoName);
		testAddStarredBy.getProperties().put(CoreConstants.DESCRIPTION, "descriere");
		
		repositoriesService.saveRepository(testAddStarredBy);
		repositoriesService.addStarredBy(owner2, repoName, userWhoStarred);
		assertTrue("Starred two repos with same name failed!", 
				((List<String>) newMemberNode.getPropertyValue(CoreConstants.STARRED_REPOSITORIES)).contains(CoreUtils.getRepositoryName(owner2, repoName)));
	}
	
	/**
	 * @author see class
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testRemoveMember() throws IOException {
		String owner = "user1-random1";
		String repoName = "remove-member";
		String member = "user2-random2";
		
		Node testRemoveMember = new Node();
		testRemoveMember.getProperties().put(CoreConstants.USER, owner);
		testRemoveMember.getProperties().put(CoreConstants.NAME, repoName);
		testRemoveMember.getProperties().put(CoreConstants.DESCRIPTION, "descriere");
		
		repositoriesService.saveRepository(testRemoveMember);
		repositoriesService.addMember(owner, repoName, member);
		repositoriesService.removeMember(owner, repoName, member);
		
		// member should be removed
		Node repository = resourceService.getNode(CoreUtils.getRepositoryNodeUri(owner, repoName));
		assertFalse("Repository does contain removed member!", ((List<String>) repository.getPropertyValue(CoreConstants.MEMBERS)).contains(member));
	}
	
	/**
	 * @author see class
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testRemoveStarredBy() throws IOException {
		String owner = "user1-random1";
		String repoName = "remove-starred-by";
		String userWhoStarredToBeRemoved = "user2-random2";
		Node testRemoveStarredBy = new Node();
		
		testRemoveStarredBy.getProperties().put(CoreConstants.USER, owner);
		testRemoveStarredBy.getProperties().put(CoreConstants.NAME, repoName);
		testRemoveStarredBy.getProperties().put(CoreConstants.DESCRIPTION, "description");
		
		repositoriesService.saveRepository(testRemoveStarredBy);
		repositoriesService.addStarredBy(owner, repoName, userWhoStarredToBeRemoved);
		repositoriesService.removeStarredBy(owner, repoName, userWhoStarredToBeRemoved);
		
		// remove starred by
		Node repository = resourceService.getNode(CoreUtils.getRepositoryNodeUri(owner, repoName));
		assertFalse("Repository does contain removed member who starred!", 
				((List<String>) repository.getPropertyValue(CoreConstants.STARRED_BY)).contains(userWhoStarredToBeRemoved));
	}
	
	/**
	 * @author see class
	 */
	@SuppressWarnings("serial")
	@Test
	public void testApplyExtension() throws IOException {
		final String login = "user1-random1";
		final String repoName = "apply-extension";
		Map<String, String> parameters = new HashMap<String, String>() { {
			put("login", login); put("repositoryName", repoName); put("extensionId", "git");
			} };
		
		Node testApplyExtension = new Node();
		testApplyExtension.getProperties().put(CoreConstants.USER, login);
		testApplyExtension.getProperties().put(CoreConstants.NAME, repoName);
		testApplyExtension.getProperties().put(CoreConstants.DESCRIPTION, "");
				
		repositoriesService.saveRepository(testApplyExtension);
		repositoriesService.applyExtension(parameters);
				
		Node repository = resourceService.getNode(CoreUtils.getRepositoryNodeUri(login, repoName));
		StringList extensionsString = (StringList) repository.getPropertyValue(CoreConstants.EXTENSIONS);
		List<ExtensionInfoInFile> extensions = fromStringListToExtensionInfoInFile(extensionsString);
		
		int index;
		for (index = 0; index < extensions.size(); index++) {
			if (extensions.get(index).getId().equals("git")) {
				break;
			}
		}
		
		if (index == extensions.size()) {
			fail(String.format("Extension 'git' not applied!"));
		}
		
		testApplyDependencies("git", extensions);
	}
	
	/**
	 * @author see class
	 */
	private void testApplyDependencies(String extensionId, List<ExtensionInfoInFile> extensions) {
		ExtensionMetadata extensionMetadata = getExtensionMetadataForExtensionId(extensionId);
		for (String extensionDependency : extensionMetadata.getDependencies()) {
			int i;
			for (i = 0; i < extensions.size(); i++) {
				if (extensions.get(i).getId().equals(extensionDependency)) {
					break;
				}
			}
			
			if (i == extensions.size()) {
				fail(String.format("Dependency '%s' not found in dependencies, although '%s' depends on it!",
						extensionDependency, extensionId));
			}
			
			testApplyDependencies(extensionDependency, extensions);
		}
	}
	
	/**
	 * @author see class
	 */
	@SuppressWarnings("serial")
	@Test
	public void testUnapplyExtension() throws IOException {
		final String login = "user1-random1";
		final String repoName = "unapply-extension";
		Map<String, String> parameters = new HashMap<String, String>() { {
			put("login", login); put("repositoryName", repoName); put("extensionId", "git");
			} };
		
		Node testUnapplyExtension = new Node();
		testUnapplyExtension.getProperties().put(CoreConstants.USER, login);
		testUnapplyExtension.getProperties().put(CoreConstants.NAME, repoName);
		testUnapplyExtension.getProperties().put(CoreConstants.DESCRIPTION, "");
		
		repositoriesService.saveRepository(testUnapplyExtension);
		repositoriesService.applyExtension(parameters);
		repositoriesService.unapplyExtension(parameters);
		
		// git and all it's dependencies must be gone	
		Node repository = resourceService.getNode(CoreUtils.getRepositoryNodeUri(login, repoName));
		StringList extensionsString = (StringList) repository.getPropertyValue(CoreConstants.EXTENSIONS);
		List<ExtensionInfoInFile> extensions = fromStringListToExtensionInfoInFile(extensionsString);
		ExtensionInfoInFile extensionInfoInFile = getExtensionInfoInFile(extensions, "git");
				
		if (extensionInfoInFile != null) {
			fail(String.format("Extension 'git' still there though it shouldn't be!"));
		}
		
		testUnapplyDependencies("git", extensions, new ArrayList<String>(Arrays.asList("git")));
		
		parameters.put("extensionId", "codeSync");
		repositoriesService.applyExtension(parameters);
		parameters.put("extensionId", "git");
		repositoriesService.applyExtension(parameters);
		repositoriesService.unapplyExtension(parameters);
		
		repository = resourceService.getNode(CoreUtils.getRepositoryNodeUri(login, repoName));
		extensionsString = (StringList) repository.getPropertyValue(CoreConstants.EXTENSIONS);
		extensions = fromStringListToExtensionInfoInFile(extensionsString);
		extensionInfoInFile = getExtensionInfoInFile(extensions, "codeSync");
		
		if (extensionInfoInFile == null) {
			fail(String.format("Extension 'codeSync' should still be there!"));
		}
		
		testUnapplyDependencies("git", extensions, new ArrayList<String>(Arrays.asList("git")));
	}
	
	private void testUnapplyDependencies(String extensionId, List<ExtensionInfoInFile> extensions, List<String> removedExtensions) {
		ExtensionMetadata extensionMetadata = getExtensionMetadataForExtensionId(extensionId);
		ExtensionInfoInFile extensionInfo;
		
		for (String extensionDependency : extensionMetadata.getDependencies()) {
			extensionInfo = getExtensionInfoInFile(extensions, extensionDependency);
			if (extensionInfo != null) {
				for (String removedExtension : removedExtensions) {
					if (extensionInfo.getExtensionsThatDependOnThis() != null && extensionInfo.getExtensionsThatDependOnThis().contains(removedExtension)) {
						fail(String.format("'%s' shouldn't be in the list for '%s' anymore. '%s' has just been deleted!", 
								removedExtension, extensionInfo.getId(), extensionInfo.getId()));
					}
				}
				
				if ((!extensionInfo.isTransitive() || extensionInfo.getExtensionsThatDependOnThis().size() != 0) && !extensions.contains(extensionInfo)) {
					fail(String.format("'%s' was deleted although it isn't transitive or there are still extensions that depend on it!", extensionInfo.getId()));
				}
			}
			
			testUnapplyDependencies(extensionDependency, extensions, removedExtensions);
		}
	}
	
	/**
	 * @author see class
	 */
	//@Test
	public void test() throws Exception {
		Node testRepository = new Node();
		
		// test create Repository		
		testRepository.getProperties().put(CoreConstants.USER, "user1-random1");
		testRepository.getProperties().put(CoreConstants.NAME, "firstRepo");
		testRepository.getProperties().put(CoreConstants.DESCRIPTION, "descriere");

		repositoriesService.saveRepository(testRepository);
		
		// test rename repository
		Node renameRepository = resourceService.getNode(CoreUtils.getRepositoryNodeUri("user1-random1", "firstRepo"), context);
		renameRepository.getProperties().put(CoreConstants.NAME, "renamed-repo");
		repositoriesService.saveRepository(renameRepository);
	}
	
	/**
	 * @author see class
	 */

	@AfterClass
	public static void afterClass() throws Exception {
		EclipseIndependentTestSuite.deleteFiles(REPOSITORIES);
		//new File("workspace/.users").delete();
		EclipseIndependentTestSuite.deleteFiles("user1-random1");
		EclipseIndependentTestSuite.deleteFiles("user2-random2");
		EclipseIndependentTestSuite.deleteFiles("user3-random3");
	}

}
