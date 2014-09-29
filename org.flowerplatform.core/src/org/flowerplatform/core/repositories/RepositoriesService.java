package org.flowerplatform.core.repositories;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ResourceServiceRemote;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.util.StringList;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.IController;
import org.flowerplatform.util.controller.TypeDescriptor;

import static org.flowerplatform.core.CoreUtils.getUriFromFragment;
import static org.flowerplatform.core.CoreUtils.getRepositoryName;
import static org.flowerplatform.core.CoreUtils.getRepositoryNodeUri;

/**
 * @author Cristina Brinza
 *
 */
@SuppressWarnings("unchecked")
@Path("/repository")
public class RepositoriesService {
	
	private ResourceService resourceService = CorePlugin.getInstance().getResourceService();
	private NodeService nodeService = CorePlugin.getInstance().getNodeService();
	
	/**
	 * @author see class
	 */
	@POST @Path("//save")
	public Node saveRepository(Node repository) throws IOException {		
		String login = (String) repository.getProperties().get(CoreConstants.USER);
		String newName = (String) repository.getProperties().get(CoreConstants.NAME);
		String newDescription = (String) repository.getProperties().get(CoreConstants.DESCRIPTION);
		String newNameRepo = getRepositoryName(login, newName);
		ServiceContext<ResourceService> context = new ServiceContext<ResourceService>();
		context.add(CoreConstants.POPULATE_WITH_PROPERTIES, true);
		Node memberNode;
		Map<String, Object> properties = new HashMap<String, Object>();

		if (repository.getNodeUri().equals("")) {
			// create repository
			repository.setType(CoreConstants.REPOSITORY);
			repository.setNodeUri(getRepositoryNodeUri(login, newName));

			Node repositories = resourceService.getNode(CoreConstants.REPOSITORIES_URI, context);
			nodeService.addChild(repositories, repository, new ServiceContext<NodeService>(nodeService));
		} else {
			// rename repo
			repository.setRawNodeData(resourceService.getNode(repository.getNodeUri(), context).getRawNodeData());
			repository.setNodeUri(getRepositoryNodeUri(login, newName));
			// set login so that changeID in PersistencePropertySetter is
			// called.
			// we do not set NAME because the oldName is needed later.
			nodeService.setProperty(repository, CoreConstants.USER, login, new ServiceContext<NodeService>().add(CoreConstants.POPULATE_WITH_PROPERTIES, true));
		}

		Node repositoryOnServer = resourceService.getNode(repository.getNodeUri(), context);
		String oldName = (String) repositoryOnServer.getProperties().get(CoreConstants.NAME);
		String oldNameRepo = getRepositoryName(login, oldName);

		// create user directory
		File userDir = new File(CoreConstants.FLOWER_PLATFORM_WORKSPACE + "/" + login);
		if (!userDir.exists()) {
			userDir.mkdirs();
		}

		// create repository directory
		File repoDir = new File(CoreConstants.FLOWER_PLATFORM_WORKSPACE + "/" + login + "/" + newName + "/");
		File oldRepoDir = new File(CoreConstants.FLOWER_PLATFORM_WORKSPACE + "/" + login + "/" + oldName + "/");

		if (!repoDir.exists() && !oldRepoDir.exists()) {
			repoDir.mkdirs();
		} else if (oldRepoDir.exists() && !oldName.equals(newName)) {
			oldRepoDir.renameTo(repoDir);
		} else {
			throw new RuntimeException(String.format("Repository %s for user %s already exists", newName, login));
		}

		// update OWNED_REPOSITORIES
		Node ownerNode = resourceService.getNode(getUriFromFragment((String) repository.getPropertyValue(CoreConstants.USER)));
		StringList ownedRepos = (StringList) ownerNode.getPropertyValue(CoreConstants.OWNED_REPOSITORIES);
		if (ownedRepos == null) {
			ownedRepos = new StringList();
		}
		ownedRepos.remove(oldNameRepo);
		ownedRepos.add(newNameRepo);
		nodeService.setProperty(ownerNode, CoreConstants.OWNED_REPOSITORIES, ownedRepos, new ServiceContext<NodeService>());

		// update MEMBER_IN_REPOSITORIES
		List<String> members = (List<String>) repositoryOnServer.getPropertyValue(CoreConstants.MEMBERS);
		if (members == null) {
			members = new StringList();
		}
		for (String member : members) {
			memberNode = resourceService.getNode(getUriFromFragment(member));
			List<String> repositoriesWhereMember = (List<String>) memberNode.getPropertyValue(CoreConstants.MEMBER_IN_REPOSITORIES);
			repositoriesWhereMember.remove(oldNameRepo);
			repositoriesWhereMember.add(newNameRepo);

			nodeService.setProperty(memberNode, CoreConstants.MEMBER_IN_REPOSITORIES, repositoriesWhereMember, new ServiceContext<NodeService>());
		}

		// update STARRED_REPOSITORIES
		List<String> starredBy = (List<String>) repositoryOnServer.getPropertyValue(CoreConstants.STARRED_BY);
		if (starredBy == null) {
			starredBy = new StringList();
		}
		for (String memberWhoStarred : starredBy) {
			memberNode = resourceService.getNode(getUriFromFragment(memberWhoStarred));
			List<String> starredRepositories = (List<String>) memberNode.getPropertyValue(CoreConstants.STARRED_REPOSITORIES);
			starredRepositories.remove(oldNameRepo);
			starredRepositories.add(newNameRepo);

			nodeService.setProperty(memberNode, CoreConstants.STARRED_REPOSITORIES, starredRepositories, new ServiceContext<NodeService>());
		}

		// update properties
		properties.put(CoreConstants.USER, login);
		properties.put(CoreConstants.NAME, newName);
		properties.put(CoreConstants.DESCRIPTION, newDescription);
		properties.put(CoreConstants.MEMBERS, members);
		properties.put(CoreConstants.STARRED_BY, starredBy);
		List<String> extensions = (List<String>) repositoryOnServer.getProperties().get(CoreConstants.EXTENSIONS);
		if (extensions == null) {
			extensions = new StringList();
		}
		properties.put(CoreConstants.EXTENSIONS, extensions);
		nodeService.setProperties(repository, properties, new ServiceContext<NodeService>());

		// save file
		resourceService.save(CoreConstants.USERS_PATH, new ServiceContext<ResourceService>(resourceService));

		return repository;
	}
	
	/**
	 * @author see class
	 */
	@POST @Path("//deleteRepository")
	public List<Node> deleteRepository(String nodeUri) {
		Node repositoryFromClient = resourceService.getNode(nodeUri, new ServiceContext<ResourceService>().add(CoreConstants.POPULATE_WITH_PROPERTIES, true));
		String login = (String) repositoryFromClient.getProperties().get(CoreConstants.USER);
		String repoName = (String) repositoryFromClient.getProperties().get(CoreConstants.NAME);
		Object repository;
		String repositoryName = getRepositoryName(login, repoName);

		try {
			repository = CorePlugin.getInstance().getFileAccessController().getFile(repositoryName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		if (!CorePlugin.getInstance().getFileAccessController().exists(repository)) {
			throw new RuntimeException(String.format("Repository '%s' for user '%s' doesn't exist!", repoName, login));
		}

		// delete repository
		try {
			FileUtils.deleteDirectory(CorePlugin.getInstance().getFileAccessController().getFileAsFile(repository));
		} catch (Exception e) {
			throw new RuntimeException(String.format("Repository dir '%s' cannot be deleted", repoName), e);
		}

		// update user data
		Node userNode = resourceService.getNode(getUriFromFragment(login));

		// update OWNED_REPOSITORIES
		List<String> ownedRepos = (List<String>) (userNode.getPropertyValue(CoreConstants.OWNED_REPOSITORIES));
		ownedRepos.remove(repositoryName);
		nodeService.setProperty(userNode, CoreConstants.OWNED_REPOSITORIES, ownedRepos, new ServiceContext<NodeService>());

		Node repositoryNode = resourceService.getNode(getUriFromFragment(repositoryName));
		List<String> members = (List<String>) repositoryNode.getPropertyValue(CoreConstants.MEMBERS);
		List<String> starredBy = (List<String>) repositoryNode.getPropertyValue(CoreConstants.STARRED_BY);
		Node memberNode;

		// update MEMBER_IN_REPOSITORIES
		for (String member : members) {
			memberNode = resourceService.getNode(getUriFromFragment(member));
			List<String> repositoriesWhereMember = (List<String>) memberNode.getPropertyValue(CoreConstants.MEMBER_IN_REPOSITORIES);
			repositoriesWhereMember.remove(repositoryName);
			nodeService.setProperty(memberNode, CoreConstants.MEMBER_IN_REPOSITORIES, repositoriesWhereMember, new ServiceContext<NodeService>());
		}

		// update STARRED_REPOSITORIES
		for (String memberWhoStarred : starredBy) {
			memberNode = resourceService.getNode(getUriFromFragment(memberWhoStarred));
			List<String> starredRepositories = (List<String>) memberNode.getPropertyValue(CoreConstants.STARRED_REPOSITORIES);
			starredRepositories.remove(repositoryName);
			nodeService.setProperty(memberNode, CoreConstants.STARRED_REPOSITORIES, starredRepositories, new ServiceContext<NodeService>());
		}

		// delete repository node
		nodeService.removeChild(resourceService.getNode(CoreConstants.REPOSITORIES_URI), repositoryNode, new ServiceContext<NodeService>());

		// save file
		resourceService.save(CoreConstants.USERS_PATH, new ServiceContext<ResourceService>(resourceService));

		return getRepositoriesForUserAsNode(getUriFromFragment(login));
	}

	

	/**
	 * @author see class
	 */
	public void addMember(String login, String repoName, String newMember) {
		Node repositoryNode = resourceService.getNode(getRepositoryNodeUri(login, repoName));
		Node newMemberNode;
		try {
			newMemberNode = resourceService.getNode(getUriFromFragment(newMember));
		} catch (NullPointerException e) {
			throw new RuntimeException(String.format("User with login '%s' doesn't exist!", newMember));
		}
		
		// update MEMBERS
		List<String> members = (List<String>) repositoryNode.getPropertyValue(CoreConstants.MEMBERS);
		if (members.contains(newMember)) {
			throw new RuntimeException(String.format("User with login '%s' is already a member of this repo", login));
		}
		members.add(newMember);
		nodeService.setProperty(repositoryNode, CoreConstants.MEMBERS, members, new ServiceContext<NodeService>());
		
		// update user info - MEMBER_IN_REPOSITORIES
		List<String> repositoriesWhereMember = (List<String>) newMemberNode.getPropertyValue(CoreConstants.MEMBER_IN_REPOSITORIES);
		if (repositoriesWhereMember == null) {
			repositoriesWhereMember = new StringList();
		}
		repositoriesWhereMember.add(getRepositoryName(login, repoName));
		nodeService.setProperty(newMemberNode, CoreConstants.MEMBER_IN_REPOSITORIES, repositoriesWhereMember, new ServiceContext<NodeService>());
		
		// save file
		resourceService.save(CoreConstants.USERS_PATH, new ServiceContext<ResourceService>(resourceService));
	}
	
	/**
	 * @author see class
	 */
	public void addStarredBy(String login, String repoName, String userWhoStarred) {
		Node repositoryNode = resourceService.getNode(getRepositoryNodeUri(login, repoName));
		Node userWhoStarredNode = resourceService.getNode(getUriFromFragment(userWhoStarred));
		
		// update STARRED_BY
		List<String> starredBy = (List<String>) repositoryNode.getPropertyValue(CoreConstants.STARRED_BY);
		if (starredBy.contains(userWhoStarred)) {
			throw new RuntimeException(String.format("User with login '%s' already starred this repo", userWhoStarred));
		}
		starredBy.add(userWhoStarred);
		nodeService.setProperty(repositoryNode, CoreConstants.STARRED_BY, starredBy, new ServiceContext<NodeService>());
		
		// update user info - STARRED_REPOSITORIES
		List<String> starredRepositories = (List<String>) userWhoStarredNode.getPropertyValue(CoreConstants.STARRED_REPOSITORIES);
		if (starredRepositories == null) {
			starredRepositories = new StringList();
		}
		starredRepositories.add(getRepositoryName(login, repoName));
		nodeService.setProperty(userWhoStarredNode, CoreConstants.STARRED_REPOSITORIES, starredRepositories, new ServiceContext<NodeService>());
		
		// save file
		resourceService.save(CoreConstants.USERS_PATH, new ServiceContext<ResourceService>(resourceService));
	}
	
	/**
	 * @author see class
	 */
	public void removeMember(String login, String repoName, String removedMember) {
		Node repositoryNode = resourceService.getNode(getRepositoryNodeUri(login, repoName));
		Node removedMemberNode;
		try {
			removedMemberNode = resourceService.getNode(getUriFromFragment(removedMember));
		} catch (NullPointerException e) {
			throw new RuntimeException(String.format("User with login '%s' doesn't exist!", removedMember));
		}
		
		// update MEMBERS
		List<String> members = (List<String>) repositoryNode.getPropertyValue(CoreConstants.MEMBERS);
		if (!members.contains(removedMember)) {
			throw new RuntimeException(String.format("User with login '%s' is not a member of this repo", removedMember));
		}
		members.remove(removedMember);
		nodeService.setProperty(repositoryNode, CoreConstants.MEMBERS, members, new ServiceContext<NodeService>());
		
		// update user info - MEMBER_IN_REPOSITORIES
		List<String> repositoriesWhereMember = (List<String>) removedMemberNode.getPropertyValue(CoreConstants.MEMBER_IN_REPOSITORIES);
		repositoriesWhereMember.remove(getRepositoryName(login, repoName));
		nodeService.setProperty(removedMemberNode, CoreConstants.MEMBER_IN_REPOSITORIES, repositoriesWhereMember, new ServiceContext<NodeService>());
		
		// save file
		resourceService.save(CoreConstants.USERS_PATH, new ServiceContext<ResourceService>(resourceService));
	}
	
	/**
	 * @author see class
	 */
	public void removeStarredBy(String login, String repoName, String userWhoStarredToBeRemoved) {
		Node repositoryNode = resourceService.getNode(getRepositoryNodeUri(login, repoName));
		Node userWhoStarredToBeRemovedNode = resourceService.getNode(getUriFromFragment(userWhoStarredToBeRemoved));
		
		// update STARRED_BY
		List<String> starredBy = (List<String>) repositoryNode.getPropertyValue(CoreConstants.STARRED_BY);
		if (!starredBy.contains(userWhoStarredToBeRemoved)) {
			throw new RuntimeException(String.format("User with login '%s' didn't starred this repo!", userWhoStarredToBeRemoved));
		}
		starredBy.remove(userWhoStarredToBeRemoved);
		nodeService.setProperty(repositoryNode, CoreConstants.STARRED_BY, starredBy, new ServiceContext<NodeService>());
		
		// update user info - STARRED_REPOSITORIES
		List<String> starredRepositories = (List<String>) userWhoStarredToBeRemovedNode.getPropertyValue(CoreConstants.STARRED_REPOSITORIES);
		starredRepositories.remove(getRepositoryName(login, repoName));
		nodeService.setProperty(userWhoStarredToBeRemovedNode, CoreConstants.STARRED_REPOSITORIES, starredRepositories, new ServiceContext<NodeService>());
		
		// save file
		resourceService.save(CoreConstants.USERS_PATH, new ServiceContext<ResourceService>(resourceService));
	}
	
	/**
	 * @author see class
	 */
	public List<Node> getAllRepositories() {
		Node repositoriesNode =  resourceService.getNode(CoreConstants.REPOSITORIES_URI);
		
		ServiceContext<NodeService> context = new ServiceContext<NodeService>();
		context.add(CoreConstants.POPULATE_WITH_PROPERTIES, true);
		List<Node> repositories = CorePlugin.getInstance().getNodeService().getChildren(repositoriesNode, context);
	
		return repositories;
	}
	
	/**
	 * @author see class
	 */
	@GET @Path("/{nodeUri}")	
	@Produces(MediaType.APPLICATION_JSON)
	public List<Node> getRepositoriesForUserAsNode(@PathParam("nodeUri") String nodeUri) {
		//new ResourceServiceRemote().subscribeToParentResource(CoreConstants.USERS_PATH);
		List<Node> repositories = new ArrayList<Node>();
		
		ServiceContext<ResourceService> context = new ServiceContext<ResourceService>();
		context.add(CoreConstants.POPULATE_WITH_PROPERTIES, true);
		Node user = resourceService.getNode(nodeUri, context);
		
		for (String ownedRepository : (List<String>) user.getPropertyValue(CoreConstants.OWNED_REPOSITORIES)) {
			repositories.add(resourceService.getNode(getUriFromFragment(ownedRepository), context));
		}
		
		return repositories;
	}
	
	/**
	 * @author see class
	 */
	public List<String> getRepositoriesForUserAsString(String login) {
		return (List<String>) resourceService.getNode(getUriFromFragment(login)).getPropertyValue(CoreConstants.OWNED_REPOSITORIES);
	}

	/**
	 * @author see class
	 */
	@POST @Path("//applyExtension")
	public List<ExtensionMetadata> applyExtension(Map<String, String> map) {
		String login = (String) map.get("login");
		String repoName = (String) map.get("repositoryName");
		String extensionId = (String) map.get("extensionId");
		Node repositoryNode = resourceService.getNode(getRepositoryNodeUri(login, repoName));
		StringList extensionsString = (StringList) repositoryNode.getPropertyValue(CoreConstants.EXTENSIONS);
		List<ExtensionInfoInFile> extensions = fromStringListToExtensionInfoInFile(extensionsString);
				
		if (getExtensionInfoInFile(extensions, extensionId) != null) {
			throw new RuntimeException(String.format("Extension with ID '%s' already exists for repository '%s'", extensionId, repoName));
		}

		ExtensionInfoInFile newExtensionAdded = new ExtensionInfoInFile();
		newExtensionAdded.setId(extensionId);
		newExtensionAdded.setTransitive(false);
		
		extensions.add(newExtensionAdded);
			
		applyDependencies(extensionId, null, extensions);
		
		// save List<String> in file instead of List<ExtensionInfoInFile>
		nodeService.setProperty(repositoryNode, CoreConstants.EXTENSIONS, fromExtensionInfoInFileToStringList(extensions), new ServiceContext<NodeService>());
			
		// save file
		resourceService.save(CoreConstants.USERS_PATH, new ServiceContext<ResourceService>(resourceService));
		
		return fromExtensionInfoInFileToExtensionMetadata(extensions);
	}
	
	/**
	 * @author see class
	 */
	public List<ExtensionMetadata> fromExtensionInfoInFileToExtensionMetadata(List<ExtensionInfoInFile> extensions) {
		  List<ExtensionMetadata> result = new ArrayList<ExtensionMetadata>();
		  
		  for (ExtensionInfoInFile extensionInfoInFile : extensions) {
		   result.add(getExtensionMetadataForExtensionId(extensionInfoInFile.getId()));
		  }
		  
		 return result;
	}
	
	/**
	 * @author see class
	 */
	private void applyDependencies(String extensionId, String parentExtensionId, List<ExtensionInfoInFile> extensions) {
		ExtensionMetadata extensionMetadata = getExtensionMetadataForExtensionId(extensionId);
		ExtensionInfoInFile extensionInfo;
		
		for (String extensionDependency : extensionMetadata.getDependencies()) {
			
			int i;
			for (i = 0; i < extensions.size(); i++) {
				extensionInfo = extensions.get(i);
				if (extensionInfo.getId().equals(extensionDependency)) {
					if (!extensionInfo.contains(extensionId)) {
						extensionInfo.addDependency(extensionId);
					}
					break;
				}
			}
			
			if (i == extensions.size()) {
				extensionInfo = new ExtensionInfoInFile();
				extensionInfo.setId(extensionDependency);
				extensionInfo.setTransitive(true);
				if (!extensionInfo.contains(extensionDependency)) {
					extensionInfo.addDependency(extensionId);
				}
				extensions.add(extensionInfo);
			}
			
			applyDependencies(extensionDependency, extensionId, extensions);
		}
		
	}
	
	/**
	 * @author see class
	 */
//	public void unapplyExtension(String login, String repoName, String extensionId) {
//		Node repositoryNode = resourceService.getNode(getRepositoryNodeUri(login, repoName));
//		StringList extensionsString = (StringList) repositoryNode.getPropertyValue(CoreConstants.EXTENSIONS);
//		List<ExtensionInfoInFile> extensions = fromStringListToExtensionInfoInFile(extensionsString);
//		ExtensionInfoInFile extension = getExtensionInfoInFile(extensions, extensionId);
//
//		if (extension == null) {
//			throw new RuntimeException(String.format("The extension with ID '%s' doesn't exist for repository '%s'", extensionId, repoName));
//		}
//		
//		if (extension.getExtensionsThatDependOnThis().size() != 0) {
//			throw new RuntimeException(String.format("Cannot delete extension with ID '%s'. Another extensions (e.g. '%s') depend on it", 
//					extensionId, 
//					extension.getExtensionsThatDependOnThis().get(0)));
//		}
//		
//		List<String> removedExtensions = new ArrayList<String>(Arrays.asList(extension.getId()));
//		extensions.remove(extension);
//		
//		// remove it's dependencies
//		unapplyDependencies(extensionId, extensions, removedExtensions);
//		
//		nodeService.setProperty(repositoryNode, CoreConstants.EXTENSIONS, fromExtensionInfoInFileToStringList(extensions), new ServiceContext<NodeService>());
//		
//		// save file
//		resourceService.save(CoreConstants.USERS_PATH, new ServiceContext<ResourceService>(resourceService));
//	}
	
	@POST @Path("//unapplyExtension")
	public List<ExtensionMetadata> unapplyExtension(Map<String, String> map) {
		String login = (String) map.get("login");
		String repoName = (String) map.get("repositoryName");
		String extensionId = (String) map.get("extensionId");
		Node repositoryNode = resourceService.getNode(getRepositoryNodeUri(login, repoName));
		StringList extensionsString = (StringList) repositoryNode.getPropertyValue(CoreConstants.EXTENSIONS);
		List<ExtensionInfoInFile> extensions = fromStringListToExtensionInfoInFile(extensionsString);
		ExtensionInfoInFile extension = getExtensionInfoInFile(extensions, extensionId);

		if (extension == null) {
			throw new RuntimeException(String.format("The extension with ID '%s' doesn't exist for repository '%s'", extensionId, repoName));
		}

		if (extension.getExtensionsThatDependOnThis().size() != 0) {
			throw new RuntimeException(String.format("Cannot delete extension with ID '%s'. Another extensions (e.g. '%s') depend on it", extensionId, extension
					.getExtensionsThatDependOnThis().get(0)));
		}

		List<String> removedExtensions = new ArrayList<String>(Arrays.asList(extension.getId()));
		extensions.remove(extension);

		// remove it's dependencies
		unapplyDependencies(extensionId, extensions, removedExtensions);

		nodeService.setProperty(repositoryNode, CoreConstants.EXTENSIONS, fromExtensionInfoInFileToStringList(extensions), new ServiceContext<NodeService>());

		// save file
		resourceService.save(CoreConstants.USERS_PATH, new ServiceContext<ResourceService>(resourceService));

		return fromExtensionInfoInFileToExtensionMetadata(extensions);
	}
	
	/**
	 * @author see class
	 */
	private void unapplyDependencies(String extensionId, List<ExtensionInfoInFile> extensions, List<String> removedExtensions) {
		ExtensionMetadata extensionMetadata = getExtensionMetadataForExtensionId(extensionId);
		ExtensionInfoInFile extensionInfo;
		
		for (String extensionDependency : extensionMetadata.getDependencies()) {
			extensionInfo = getExtensionInfoInFile(extensions, extensionDependency);
			for (String removedExtension : removedExtensions) {
				if (extensionInfo.getExtensionsThatDependOnThis().contains(removedExtension)) {
					extensionInfo.removeDependency(removedExtension);
				}
			}
			
			if (extensionInfo.isTransitive() && extensionInfo.getExtensionsThatDependOnThis().size() == 0) {
				extensions.remove(extensionInfo);
				removedExtensions.add(extensionInfo.getId());	
			}
			
			unapplyDependencies(extensionDependency, extensions, removedExtensions);
		}
	}
	
	/**
	 * @author see class
	 */
	protected List<AbstractController> getExtensionDescriptors() {
		TypeDescriptor descriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(CoreConstants.GENERAL_PURPOSE);
		return descriptor.getAdditiveControllers(CoreConstants.EXTENSION_DESCRIPTOR, null);
	}
	
	/**
	 * @author see class
	 */
	@GET @Path("//allExtensions")	
	@Produces(MediaType.APPLICATION_JSON)
	public List<ExtensionMetadata> getAllExtensions() {
		new ResourceServiceRemote().subscribeToParentResource(CoreConstants.USERS_PATH);
		List<AbstractController> extensionDescriptors = getExtensionDescriptors();
		List<ExtensionMetadata> extensions = new ArrayList<ExtensionMetadata>();
		
		for (IController element : extensionDescriptors) {
			extensions.add((ExtensionMetadata) element);
		}
		
		return extensions;
	}
	
	/**
	 * @author see class
	 */
	protected List<String> getAllExtensionsNames() {
		List<ExtensionMetadata> extensions = getAllExtensions();
		List<String> extensionsIds = new ArrayList<String>();
		
		for (ExtensionMetadata extensionMetadata : extensions) {
			extensionsIds.add(extensionMetadata.getId());
		}
		
		return extensionsIds;
	}
	
	/**
	 * @author see class
	 * 
	 * Extensions that are not applied to current repository 
	 */
	public List<ExtensionMetadata> getAvailableExtensions(String login, String repoName) {
		List<ExtensionMetadata> availableExtensions = new ArrayList<ExtensionMetadata>();
		Node repository = resourceService.getNode(getRepositoryNodeUri(login, repoName));
		List<ExtensionMetadata> allExtensions = getAllExtensions();
		List<String> appliedExtensions = (List<String>) repository.getPropertyValue(CoreConstants.EXTENSIONS);
		
		for (int i = 0; i < allExtensions.size(); i++) {
			ExtensionMetadata extension = allExtensions.get(i);
			if (!appliedExtensions.contains(extension.getId())) {
				availableExtensions.add(extension);
			}
		}
		
		return availableExtensions;
	}
	
	/**
	 * @author see class
	 */
	public ExtensionMetadata getExtensionMetadataForExtensionId(String extensionId) {
		List<AbstractController> extensionDescriptors = getExtensionDescriptors();
		for (IController element : extensionDescriptors) {
			ExtensionMetadata extensionDescriptor = (ExtensionMetadata) element;
			if (extensionDescriptor.getId().equals(extensionId)) {
				return extensionDescriptor;
			}
		}

		throw new RuntimeException(String.format("No metadata found for extension '%s'", extensionId));
	}
		
	/**
	 * @author see class
	 */
	public static ExtensionInfoInFile getExtensionInfoInFile(List<ExtensionInfoInFile> extensions, String extensionId) {
		for (ExtensionInfoInFile extensionInfoInFile : extensions) {
			if (extensionInfoInFile.getId().equals(extensionId)) {
				return extensionInfoInFile;
			}
		}
		return null;
	}
	
	/**
	 * @author see class
	 */
	public StringList fromExtensionInfoInFileToStringList(List<ExtensionInfoInFile> extensions) {
		StringList result = new StringList();
		for (ExtensionInfoInFile element : extensions) {
			result.add(element.toString());
		}
		return result;
	}
	
	/**
	 * @author see class
	 */
	public static List<ExtensionInfoInFile> fromStringListToExtensionInfoInFile(StringList extensions) {
		List<ExtensionInfoInFile> result = new ArrayList<ExtensionInfoInFile>();
		for (String string : extensions) {
			String[] parts = string.split(":");
			ExtensionInfoInFile element = new ExtensionInfoInFile();
			element.setId(parts[0]);
			element.setTransitive(parts[1].equals("true") ? true : false);
			element.setExtensionsThatDependOnThis(fromStringToArrayList(parts[2], "()", "#"));
			result.add(element);
		}
		return result;
	}
	
	/**
	 * @author see class
	 */
	public static List<String> fromStringToArrayList(String string, String emptyValue, String separator) {
		if (!string.equals(emptyValue)) {
			return new ArrayList<String>(Arrays.asList((string.substring(1, string.length() - 1)).split(separator)));
		} else {
			return new ArrayList<String>();
		}
	}
	
	/**
	 * @author see class
	 */
	@GET @Path("/{nodeUri}/extensions")	
	@Produces(MediaType.APPLICATION_JSON)
	public List<ExtensionMetadata> getExtensionsForRepository(@PathParam("nodeUri") String nodeUri) throws UnsupportedEncodingException {
		nodeUri = URLDecoder.decode(nodeUri, "UTF-8");
		List<ExtensionMetadata> extensions = new ArrayList<ExtensionMetadata>();
	  
		ServiceContext<ResourceService> context = new ServiceContext<ResourceService>();
		context.add(CoreConstants.POPULATE_WITH_PROPERTIES, true);
		Node repository = resourceService.getNode(nodeUri, context);
		
		StringList extensionsString = (StringList) repository.getPropertyValue(CoreConstants.EXTENSIONS);
		List<ExtensionInfoInFile> extensionsInfoInFile = fromStringListToExtensionInfoInFile(extensionsString);
		  
		for (ExtensionInfoInFile extensionInfoInFile : extensionsInfoInFile) {
			ExtensionMetadata extensionMetadata = getExtensionMetadataForExtensionId(extensionInfoInFile.getId());
			extensions.add(extensionMetadata);
		}
  
		return extensions;
	 }

}
