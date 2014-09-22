package org.flowerplatform.core.repositories;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceService;
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
public class RepositoriesService {
	
	private ResourceService resourceService = CorePlugin.getInstance().getResourceService();
	private NodeService nodeService = CorePlugin.getInstance().getNodeService();

	/**
	 * @author see class
	 */
	public void createRepository(String login, String repoName, String description) throws IOException {
		
		// create user directory
		File userDir = new File(CoreConstants.FLOWER_PLATFORM_WORKSPACE + "/" + login);
		if (!userDir.exists()) {
			userDir.mkdirs();
		}
		
		// create repository directory
		File repoDir = new File(CoreConstants.FLOWER_PLATFORM_WORKSPACE + "/" + login + "/" + repoName, ".git");
		if (!repoDir.exists()) {
			repoDir.mkdirs();
			
			Repository repository = FileRepositoryBuilder.create(repoDir);
			repository.create();
		} else {
			throw new RuntimeException(String.format("Repository %s for user %s already exists", repoName, login));
		}
		
		// create repository node & populate with properties
		Node repositoriesNode = resourceService.getNode(CoreConstants.REPOSITORIES_URI);
		Node repositoryNode = new Node(getRepositoryNodeUri(login, repoName), CoreConstants.REPOSITORY);
		nodeService.addChild(
				repositoriesNode, 
				repositoryNode, 
				new ServiceContext<NodeService>(nodeService).add(CoreConstants.POPULATE_WITH_PROPERTIES, true));
		nodeService.setProperty(repositoryNode, CoreConstants.USER, login, new ServiceContext<NodeService>());
		nodeService.setProperty(repositoryNode, CoreConstants.NAME, repoName, new ServiceContext<NodeService>());
		nodeService.setProperty(repositoryNode, CoreConstants.DESCRIPTION, description, new ServiceContext<NodeService>());
		nodeService.setProperty(repositoryNode, CoreConstants.MEMBERS, new ArrayList<String>(), new ServiceContext<NodeService>());
		nodeService.setProperty(repositoryNode, CoreConstants.STARRED_BY, new ArrayList<String>(), new ServiceContext<NodeService>());
		nodeService.setProperty(repositoryNode, CoreConstants.EXTENSIONS, new ArrayList<String>(), new ServiceContext<NodeService>());
		
		// update user data
		Node userNode = resourceService.getNode(getUriFromFragment(login));
		
		// owned repos
		List<String> ownedRepos = (List<String>) userNode.getPropertyValue(CoreConstants.OWNED_REPOSITORIES);
		if (ownedRepos == null) {
			ownedRepos = new ArrayList<String>();
		}
		ownedRepos.add(getRepositoryName(login, repoName));
		nodeService.setProperty(userNode, CoreConstants.OWNED_REPOSITORIES, ownedRepos, new ServiceContext<NodeService>());
		
		// save file
		resourceService.save(CoreConstants.USERS_PATH, new ServiceContext<ResourceService>(resourceService));
	}
	
	/**
	 * @author see class
	 */
	public void deleteRepository(String login, String repoName) {
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
		nodeService.removeChild(
				resourceService.getNode(CoreConstants.REPOSITORIES_URI), 
				repositoryNode, 
				new ServiceContext<NodeService>());
		
		// save file
		resourceService.save(CoreConstants.USERS_PATH, new ServiceContext<ResourceService>(resourceService));
	}

	/**
	 * @author see class
	 */
	public void renameRepository(String login, String oldNameWithoutRepo, String newNameWithoutRepo) {
		String oldName = getRepositoryName(login, oldNameWithoutRepo);
		String newName = getRepositoryName(login, newNameWithoutRepo);
		
		Node repositoryNode = resourceService.getNode(getUriFromFragment(oldName));
		
		nodeService.setProperty(repositoryNode, CoreConstants.NAME, newNameWithoutRepo, new ServiceContext<NodeService>());
		//nodeService.setProperty(repositoryNode, "ID", newName, new ServiceContext<NodeService>());
		
		// update OWNED_REPOSITORIES
		Node ownerNode = resourceService.getNode(getUriFromFragment((String) repositoryNode.getPropertyValue(CoreConstants.USER)));
		List<String> ownedRepos = (ArrayList<String>) ownerNode.getPropertyValue(CoreConstants.OWNED_REPOSITORIES);
		ownedRepos.remove(oldName); ownedRepos.add(newName);
		nodeService.setProperty(ownerNode, CoreConstants.OWNED_REPOSITORIES, ownedRepos, new ServiceContext<NodeService>());
		
		List<String> members = (List<String>) repositoryNode.getPropertyValue(CoreConstants.MEMBERS);
		List<String> starredBy = (List<String>) repositoryNode.getPropertyValue(CoreConstants.STARRED_BY);
		Node memberNode;
		
		// update MEMBER_IN_REPOSITORIES
		for (String member : members) {
			memberNode = resourceService.getNode(getUriFromFragment(member));
			List<String> repositoriesWhereMember = (List<String>) memberNode.getPropertyValue(CoreConstants.MEMBER_IN_REPOSITORIES);
			repositoriesWhereMember.remove(oldName); repositoriesWhereMember.add(newName);
			
			nodeService.setProperty(memberNode, CoreConstants.MEMBER_IN_REPOSITORIES, repositoriesWhereMember, new ServiceContext<NodeService>());
		}
		
		// update STARRED_REPOSITORIES
		for (String memberWhoStarred : starredBy) {
			memberNode = resourceService.getNode(getUriFromFragment(memberWhoStarred));
			List<String> starredRepositories = (List<String>) memberNode.getPropertyValue(CoreConstants.STARRED_REPOSITORIES);
			starredRepositories.remove(oldName); starredRepositories.add(newName);
			
			nodeService.setProperty(memberNode, CoreConstants.STARRED_REPOSITORIES, starredRepositories, new ServiceContext<NodeService>());
		}
		
		// save file
		resourceService.save(CoreConstants.USERS_PATH, new ServiceContext<ResourceService>(resourceService));
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
			repositoriesWhereMember = new ArrayList<String>();
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
			starredRepositories = new ArrayList<String>();
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
//		for (Node repository : repositories) {
//			repository.setNodeUri(URLEncoder.encode(repository.getNodeUri()));
//		}
	
		return repositories;
	}
	
	/**
	 * @author see class
	 */
	public List<Node> getRepositoriesForUserAsNode(String login) {
		List<Node> repositories = new ArrayList<Node>();
		Node user = resourceService.getNode(getUriFromFragment(login));
		
		for (String ownedRepository : (List<String>) user.getPropertyValue(CoreConstants.OWNED_REPOSITORIES)) {
			repositories.add(resourceService.getNode(getRepositoryNodeUri(login, ownedRepository)));
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
	public void applyExtension(String login, String repoName, String extensionId) {
		Node repositoryNode = resourceService.getNode(getRepositoryNodeUri(login, repoName));
		//List<String> extensions = (List<String>) repositoryNode.getPropertyValue(CoreConstants.EXTENSIONS);
		List<String> extensionsString = (List<String>) repositoryNode.getPropertyValue(CoreConstants.EXTENSIONS);
		List<ExtensionInfoInFile> extensions = fromListStringToExtensionInfoInFile(extensionsString);
				
		if (contains(extensions, extensionId)) {
			throw new RuntimeException(String.format("Extension with ID '%s' already exists for repository '%s'", extensionId, repoName));
		}

		ExtensionInfoInFile newExtensionAdded = new ExtensionInfoInFile();
		newExtensionAdded.setId(extensionId);
		newExtensionAdded.setTransitive(false);
		
		extensions.add(newExtensionAdded);
			
		applyDependencies(extensionId, null, extensions);
		
		// save List<String> in file instead of List<ExtensionInfoInFile>
		nodeService.setProperty(repositoryNode, CoreConstants.EXTENSIONS, fromExtensionInfoInFileToListString(extensions), new ServiceContext<NodeService>());
			
		// save file
		resourceService.save(CoreConstants.USERS_PATH, new ServiceContext<ResourceService>(resourceService));
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
					if (!extensionInfo.contains(extensionDependency)) {
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
			
//			if (!extensions.contains(extensionDependency)) {
//				extensions.add(extensionDependency);
//				applyDependencies(extensionDependency, extensions);
//			}
		}
		
	}
	
	/**
	 * @author see class
	 */
	public void unapplyExtension(String login, String repoName, String extensionId) {
		Node repositoryNode = resourceService.getNode(getRepositoryNodeUri(login, repoName));
		List<ExtensionInfoInFile> extensions = (List<ExtensionInfoInFile>) repositoryNode.getPropertyValue(CoreConstants.EXTENSIONS);
		
		if (!contains(extensions, extensionId)) {
			throw new RuntimeException(String.format("The extension with ID '%s' doesn't exist for repository '%s'", extensionId, repoName));
		}
		
//		if (extensions.contains(extensionId)) {
//			extensions.remove(extensionId);
//		} else {
//			throw new RuntimeException(String.format("This extension doesn't exist for repository '%s'", repoName));
//		}
		
		// delete extension dependencies
//		ExtensionMetadata extensionMetadata = getExtensionMetadataForExtensionId(extensionId);
//		for (String extensionDependency : extensionMetadata.getDependencies()) {
//			extensions.remove(extensionDependency);
//		}
		
		
		
		nodeService.setProperty(repositoryNode, CoreConstants.EXTENSIONS, extensions, new ServiceContext<NodeService>());
		
		// save file
		resourceService.save(CoreConstants.USERS_PATH, new ServiceContext<ResourceService>(resourceService));
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
	public List<ExtensionMetadata> getAllExtensions() {
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
	private boolean contains(List<ExtensionInfoInFile> extensions, String extensionId) {
		for (ExtensionInfoInFile extensionInfoInFile : extensions) {
			if (extensionInfoInFile.getId().equals(extensionId)) {
				return true;
			}
		}
		return false;
	}
	
	private List<String> fromExtensionInfoInFileToListString(List<ExtensionInfoInFile> extensions) {
		List<String> result = new ArrayList<String>();
		for (ExtensionInfoInFile element : extensions) {
			result.add(element.toString());
		}
		return result;
	}
	
	private static List<ExtensionInfoInFile> fromListStringToExtensionInfoInFile(List<String> extensions) {
		List<ExtensionInfoInFile> result = new ArrayList<ExtensionInfoInFile>();
		for (String string : extensions) {
			String[] parts = string.split("$");
			ExtensionInfoInFile element = new ExtensionInfoInFile();
			element.setId(parts[0]);
			element.setTransitive(parts[1].equals("true") ? true : false);
			element.setExtensionsThatDependOnThis(fromStringToArrayList(parts[2]));
			result.add(element);
		}
		return result;
	}
	
	private static List<String> fromStringToArrayList(String string) {
		if (!string.equals("()")) {
			return new ArrayList<String>(Arrays.asList((string.substring(1, string.length() - 1)).split("#")));
		} else {
			return new ArrayList<String>();
		}
	}
}
