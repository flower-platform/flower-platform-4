package org.flowerplatform.tests.core.repositories;

import static org.flowerplatform.tests.EclipseIndependentTestSuite.startPlugin;
import static org.flowerplatform.core.CoreConstants.FLOWER_PLATFORM_WORKSPACE;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.core.repositories.RepositoriesService;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.flowerplatform.tests.EclipseIndependentTestSuite;
import org.flowerplatform.util.Utils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
	public static Node users;
	public static Node repos;

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
					new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()).add(CoreConstants.POPULATE_WITH_PROPERTIES, true));
			user = CorePlugin.getInstance().getResourceService().getNode(user.getNodeUri());
			CorePlugin.getInstance().getNodeService().setProperty(user, "firstName", "user1", new ServiceContext<NodeService>());
			CorePlugin.getInstance().getNodeService().setProperty(user, "login", "user1-random1", new ServiceContext<NodeService>());

			// user2
			user = new Node(USERS_FILE_URI + "#user2-random2", CoreConstants.USER);
			CorePlugin.getInstance().getNodeService().addChild(
					users,
					user,
					new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()).add(CoreConstants.POPULATE_WITH_PROPERTIES, true));
			user = CorePlugin.getInstance().getResourceService().getNode(user.getNodeUri());
			CorePlugin.getInstance().getNodeService().setProperty(user, "firstName", "user2", new ServiceContext<NodeService>());
			CorePlugin.getInstance().getNodeService().setProperty(user, "login", "user2-random2", new ServiceContext<NodeService>());
			
			// user2
			user = new Node(USERS_FILE_URI + "#user3-random3", CoreConstants.USER);
			CorePlugin.getInstance().getNodeService().addChild(
					users,
					user,
					new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()).add(CoreConstants.POPULATE_WITH_PROPERTIES, true));
			user = CorePlugin.getInstance().getResourceService().getNode(user.getNodeUri());
			CorePlugin.getInstance().getNodeService().setProperty(user, "firstName", "user3", new ServiceContext<NodeService>());
			CorePlugin.getInstance().getNodeService().setProperty(user, "login", "user3-random3", new ServiceContext<NodeService>());

			// save file
			resourceService.save(USERS_FILE_URI, new ServiceContext<ResourceService>(resourceService));
		}
		
		FileUtils.copyFileToDirectory(new File("workspace/repositories/.users"), new File("workspace/"));
		CorePlugin.getInstance().getResourceService().subscribeToParentResource("dummy-session", CoreConstants.USERS_PATH, new ServiceContext<ResourceService>(resourceService));
	}
	
	@Before
	public void before() {
		repositoriesService = new RepositoriesService();
	}

	@Test
	public void testCreateRepository() throws IOException {
		String testLogin = "user1-random1";
		String testRepoName = "firstRepo";
		
		repositoriesService.createRepository(testLogin, testRepoName, "firstRepo description");
		
		// check if repository dir is created
		assertTrue("Repository directory not created", new File(FLOWER_PLATFORM_WORKSPACE + "/" + testLogin + "/" + testRepoName).exists());
		
		// check if Repository node is created
		assertTrue("Repository Node not created", resourceService.getNode(repositoriesService.createRepositoryNodeUri(testLogin, testRepoName)) != null);
		
		String repositoryName = repositoriesService.createRepositoryName(testLogin, testRepoName);		
		Node user = resourceService.getNode(CoreConstants.USERS_PATH + "#" + testLogin);
		
		// check if repo is in owned_repositories
		@SuppressWarnings("unchecked")
		List<String> ownedRepositories = (List<String>)user.getPropertyValue(CoreConstants.OWNED_REPOSITORIES);
		assertTrue("Repo not in owned repositories", ownedRepositories.contains(repositoryName));
		
		// check if repo is in member_in_repositories
		@SuppressWarnings("unchecked")
		List<String> memberInRepositories = (List<String>)user.getPropertyValue(CoreConstants.MEMBER_IN_REPOSITORIES);
		assertTrue("Repo not in member_in_repositories", memberInRepositories.contains(repositoryName));
			
		// create second repo for same user
		String testRepoName2 = "secondRepo";
		
		repositoriesService.createRepository(testLogin, testRepoName2, "secondRepo description");
		
		// check if repository dir is created
		assertTrue("Repository directory not created", new File(FLOWER_PLATFORM_WORKSPACE + "/" + testLogin + "/" + testRepoName2).exists());
		
		// check if Repository node is created
		assertTrue("Repository Node not created", resourceService.getNode(repositoriesService.createRepositoryNodeUri(testLogin, testRepoName2)) != null);
		
		// create repo with same name as firstRepo for another user
		String testLogin2 = "user2-random2";
		
		repositoriesService.createRepository(testLogin2, testRepoName, "repo for second user with same name as first repo");
		
		// check if repository dir is created
		assertTrue("Repository directory not created", new File(FLOWER_PLATFORM_WORKSPACE + "/" + testLogin2 + "/" + testRepoName).exists());
		
		// check if Repository node is created
		assertTrue("Repository Node not created", resourceService.getNode(repositoriesService.createRepositoryNodeUri(testLogin2, testRepoName)) != null);
	}

	@Test(expected = RuntimeException.class)
	public void testCreateRepositoryException() throws IOException {
		String testLogin = "user3-random3";
		String testRepoName = "repo";
		
		repositoriesService.createRepository(testLogin, testRepoName, "repository description");
		
		// trying to create repo with same name for same user throws Runtime Exception
		repositoriesService.createRepository(testLogin, testRepoName, "repository description");
	}
		
	@Test
	public void testDeleteRepository() throws IOException{
		String testLogin = "user1-random1";
		String testRepoName = "delete-repo";
		
		// create repo first
		repositoriesService.createRepository(testLogin, testRepoName, "");
		
		// delete repo
		repositoriesService.deleteRepository(testLogin, testRepoName);
		
		// check if directory still exists
		assertTrue("Repository dir still exists!", !new File(FLOWER_PLATFORM_WORKSPACE + "/" + testLogin + "/" + testRepoName).exists());
		
		// check if repository node was deleted from file
		try {
			resourceService.getNode(repositoriesService.createRepositoryNodeUri(testLogin, testRepoName));
			fail("NullPointerException should have been thrown");
		} catch (NullPointerException e) {
			// ignore this
		}
		
		String repositoryName = repositoriesService.createRepositoryName(testLogin, testRepoName);		
		Node user = resourceService.getNode(repositoriesService.getUriFromFragment(testLogin));
		
		// check in OWNED_REPOSITORIES for owner
		@SuppressWarnings("unchecked")
		List<String> ownedRepositories = (List<String>)user.getPropertyValue(CoreConstants.OWNED_REPOSITORIES);
		assertTrue("Repo still in owned repositories!", !ownedRepositories.contains(repositoryName));
				
		// check every member in MEMBERS
		@SuppressWarnings("unchecked")
		List<String> memberInRepositories = (List<String>)user.getPropertyValue(CoreConstants.MEMBER_IN_REPOSITORIES);
		assertTrue("Repo still in member_in_repositories!", !memberInRepositories.contains(repositoryName));
	}
	
	@Test(expected = RuntimeException.class)
	public void testDeleteNoRepository() throws IOException {
		// trying to delete a repository that doesn't exist
		String testLogin = "user1-random1";
		String testRepoName = "null-repo";
		
		repositoriesService.deleteRepository(testLogin, testRepoName);		
	}
	
	@Test
	public void testRenameRepository() throws IOException {
		String testLogin = "user1-random1";
		String testRepoName = "repo";
		
		// first create repo
		repositoriesService.createRepository(testLogin, testRepoName, "");
		
		repositoriesService.renameRepository("user1-random1", "repo", "renamed-repo");
	}
	
	@Test
	public void testAddMember() throws IOException {
		String owner = "user1-random1";
		String repoName = "add-member";
		String newMember = "user2-random2";
	
		repositoriesService.createRepository(owner, repoName, "");
		repositoriesService.addMember(owner, repoName, newMember);
		
		// check member in two repos with same name - diff owner's 
	}
	
	@Test
	public void testAddStarredBy() throws IOException {
		String owner = "user1-random1";
		String repoName = "add-starred-by";
		String userWhoStarred = "user3-random3";
		
		repositoriesService.createRepository(owner, repoName, "");
		repositoriesService.addStarredBy(owner, repoName, userWhoStarred);
		
		// check same as above
	}
	
	@Test
	public void testRemoveMember() throws IOException {
		String owner = "user1-random1";
		String repoName = "remove-member";
		String member = "user2-random2";
		
		repositoriesService.createRepository(owner, repoName, "");
		repositoriesService.addMember(owner, repoName, member);
		repositoriesService.removeMember(owner, repoName, member);
	}
	
	@Test
	public void testRemoveStarredBy() throws IOException {
		String owner = "user1-random1";
		String repoName = "remove-starred-by";
		String userWhoStarredToBeRemoved = "user2-random2";
		
		repositoriesService.createRepository(owner, repoName, "");
		repositoriesService.addStarredBy(owner, repoName, userWhoStarredToBeRemoved);
		repositoriesService.removeStarredBy(owner, repoName, userWhoStarredToBeRemoved);
	}
	
	@AfterClass
	public static void afterClass() throws Exception {
//		EclipseIndependentTestSuite.deleteFiles(REPOSITORIES);
//		new File("workspace/.users").delete();
//		EclipseIndependentTestSuite.deleteFiles("user1-random1");
//		EclipseIndependentTestSuite.deleteFiles("user2-random2");
//		EclipseIndependentTestSuite.deleteFiles("user3-random3");
	}
}
