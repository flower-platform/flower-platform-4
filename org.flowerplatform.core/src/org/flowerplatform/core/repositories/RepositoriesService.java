package org.flowerplatform.core.repositories;

import static org.flowerplatform.core.file.FileControllerUtils.getFilePathWithRepo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resources;
import javax.rmi.CORBA.Util;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.util.Utils;

/**
 * @author Cristina Brinza
 *
 */

@SuppressWarnings("unchecked")
public class RepositoriesService {
	
	private ResourceService resourceService = CorePlugin.getInstance().getResourceService();
	private NodeService nodeService = CorePlugin.getInstance().getNodeService();

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
		Node repositoryNode = new Node(createRepositoryNodeUri(login, repoName), CoreConstants.REPOSITORY);
		nodeService.addChild(
				repositoriesNode, 
				repositoryNode, 
				new ServiceContext<NodeService>(nodeService).add(CoreConstants.POPULATE_WITH_PROPERTIES, true));
		nodeService.setProperty(repositoryNode, CoreConstants.USER, login, new ServiceContext<NodeService>());
		nodeService.setProperty(repositoryNode, CoreConstants.NAME, repoName, new ServiceContext<NodeService>());
		nodeService.setProperty(repositoryNode, CoreConstants.DESCRIPTION, description, new ServiceContext<NodeService>());
		List<String> members = new ArrayList<String>(); members.add(login);
		nodeService.setProperty(repositoryNode, CoreConstants.MEMBERS, members, new ServiceContext<NodeService>());
		nodeService.setProperty(repositoryNode, CoreConstants.STARRED_BY, new ArrayList<String>(), new ServiceContext<NodeService>());
		nodeService.setProperty(repositoryNode, CoreConstants.EXTENSIONS, new ArrayList<String>(), new ServiceContext<NodeService>());
		
		// update user data
		Node userNode = resourceService.getNode(getUriFromFragment(login));
		
		// owned repos
		List<String> ownedRepos = (List<String>)userNode.getPropertyValue(CoreConstants.OWNED_REPOSITORIES);
		if (ownedRepos == null) {
			ownedRepos = new ArrayList<String>();
		}
		ownedRepos.add(createRepositoryName(login, repoName));
		nodeService.setProperty(userNode, CoreConstants.OWNED_REPOSITORIES, ownedRepos, new ServiceContext<NodeService>());
		
		// member_in_repos
		List<String> memberInRepos = (List<String>)userNode.getPropertyValue(CoreConstants.MEMBER_IN_REPOSITORIES);
		if (memberInRepos == null) {
			memberInRepos = new ArrayList<String>();
		}
		memberInRepos.add(createRepositoryName(login, repoName));
		nodeService.setProperty(userNode, CoreConstants.MEMBER_IN_REPOSITORIES, memberInRepos, new ServiceContext<NodeService>());
		
		// save file
		resourceService.save(CoreConstants.USERS_PATH, new ServiceContext<ResourceService>(resourceService));
	}
	
	public void deleteRepository(String login, String repoName) {
		Object repository;
		String repositoryName = createRepositoryName(login, repoName);
		
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
		List<String> ownedRepos;
		(ownedRepos = (List<String>)(userNode.getPropertyValue(CoreConstants.OWNED_REPOSITORIES))).remove(repositoryName);
		nodeService.setProperty(userNode, CoreConstants.OWNED_REPOSITORIES, ownedRepos, new ServiceContext<NodeService>());
		
		Node repositoryNode = resourceService.getNode(getUriFromFragment(repositoryName));
		List<String> members = (List<String>) repositoryNode.getPropertyValue(CoreConstants.MEMBERS);
		List<String> starredBy = (List<String>) repositoryNode.getPropertyValue(CoreConstants.STARRED_BY);
		Node memberNode;
		
		// update MEMBER_IN_REPOSITORIES
		for (String member : members) {
			memberNode = resourceService.getNode(getUriFromFragment(member));
			List<String> repositoriesWhereMember;
			(repositoriesWhereMember = (List<String>)memberNode.getPropertyValue(CoreConstants.MEMBER_IN_REPOSITORIES)).remove(repositoryName);
			nodeService.setProperty(memberNode, CoreConstants.MEMBER_IN_REPOSITORIES, repositoriesWhereMember, new ServiceContext<NodeService>());
		}
		
		// update STARRED_REPOSITORIES
		for (String memberWhoStarred : starredBy) {
			memberNode = resourceService.getNode(getUriFromFragment(memberWhoStarred));
			List<String> starredRepositories;
			(starredRepositories = (List<String>)memberNode.getPropertyValue(CoreConstants.STARRED_REPOSITORIES)).remove(repositoryName);
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

	public void renameRepository(String login, String oldNameWithoutRepo, String newNameWithoutRepo) {
		String oldName = createRepositoryName(login, oldNameWithoutRepo);
		String newName = createRepositoryName(login, newNameWithoutRepo);
		
		Node repositoryNode = resourceService.getNode(getUriFromFragment(oldName));
		
		nodeService.setProperty(repositoryNode, CoreConstants.NAME, newNameWithoutRepo, new ServiceContext<NodeService>());
		
		// update OWNED_REPOSITORIES
		Node ownerNode = resourceService.getNode(getUriFromFragment((String)repositoryNode.getPropertyValue(CoreConstants.USER)));
		List<String> ownedRepos = (ArrayList<String>)ownerNode.getPropertyValue(CoreConstants.OWNED_REPOSITORIES);
		ownedRepos.remove(oldName); ownedRepos.add(newName);
		nodeService.setProperty(ownerNode, CoreConstants.OWNED_REPOSITORIES, ownedRepos, new ServiceContext<NodeService>());
		
		List<String> members = (List<String>) repositoryNode.getPropertyValue(CoreConstants.MEMBERS);
		List<String> starredBy = (List<String>) repositoryNode.getPropertyValue(CoreConstants.STARRED_BY);
		Node memberNode;
		
		// update MEMBER_IN_REPOSITORIES
		for (String member : members) {
			memberNode = resourceService.getNode(getUriFromFragment(member));
			List<String> repositoriesWhereMember = (List<String>)memberNode.getPropertyValue(CoreConstants.MEMBER_IN_REPOSITORIES);
			repositoriesWhereMember.remove(oldName); repositoriesWhereMember.add(newName);
			
			nodeService.setProperty(memberNode, CoreConstants.MEMBER_IN_REPOSITORIES, repositoriesWhereMember, new ServiceContext<NodeService>());
		}
		
		// update STARRED_REPOSITORIES
		for (String memberWhoStarred : starredBy) {
			memberNode = resourceService.getNode(getUriFromFragment(memberWhoStarred));
			List<String> starredRepositories = (List<String>)memberNode.getPropertyValue(CoreConstants.STARRED_REPOSITORIES);
			starredRepositories.remove(oldName); starredRepositories.add(newName);
			
			nodeService.setProperty(memberNode, CoreConstants.STARRED_REPOSITORIES, starredRepositories, new ServiceContext<NodeService>());
		}
		
		// save file
		resourceService.save(CoreConstants.USERS_PATH, new ServiceContext<ResourceService>(resourceService));
	}
	

	public void addMember(String login, String repoName, String newMember) {
		Node repositoryNode = resourceService.getNode(createRepositoryNodeUri(login, repoName));
		Node newMemberNode;
		try {
			newMemberNode = resourceService.getNode(getUriFromFragment(newMember));
		} catch (NullPointerException e) {
			throw new RuntimeException(String.format("User with login '%s' doesn't exist!", newMember));
		}
		
		// update MEMBERS
		List<String> members = (List<String>)repositoryNode.getPropertyValue(CoreConstants.MEMBERS);
		if (members.contains(newMember)) {
			throw new RuntimeException(String.format("User with login '%s' is already a member of this repo", login));
		}
		members.add(newMember);
		nodeService.setProperty(repositoryNode, CoreConstants.MEMBERS, members, new ServiceContext<NodeService>());
		
		// update user info - MEMBER_IN_REPOSITORIES
		List<String> repositoriesWhereMember = (List<String>)newMemberNode.getPropertyValue(CoreConstants.MEMBER_IN_REPOSITORIES);
		if (repositoriesWhereMember == null) {
			repositoriesWhereMember = new ArrayList<String>();
		}
		repositoriesWhereMember.add(createRepositoryName(login, repoName));
		nodeService.setProperty(newMemberNode, CoreConstants.MEMBER_IN_REPOSITORIES, repositoriesWhereMember, new ServiceContext<NodeService>());
		
		// save file
		resourceService.save(CoreConstants.USERS_PATH, new ServiceContext<ResourceService>(resourceService));
	}
	
	public void addStarredBy(String login, String repoName, String userWhoStarred) {
		Node repositoryNode = resourceService.getNode(createRepositoryNodeUri(login, repoName));
		Node userWhoStarredNode = resourceService.getNode(getUriFromFragment(userWhoStarred));
		
		// update STARRED_BY
		List<String> starredBy = (List<String>)repositoryNode.getPropertyValue(CoreConstants.STARRED_BY);
		if (starredBy.contains(userWhoStarred)) {
			throw new RuntimeException(String.format("User with login '%s' already starred this repo", userWhoStarred));
		}
		starredBy.add(userWhoStarred);
		nodeService.setProperty(repositoryNode, CoreConstants.STARRED_BY, starredBy, new ServiceContext<NodeService>());
		
		// update user info - STARRED_REPOSITORIES
		List<String> starredRepositories = (List<String>)userWhoStarredNode.getPropertyValue(CoreConstants.STARRED_REPOSITORIES);
		if (starredRepositories == null) {
			starredRepositories = new ArrayList<String>();
		}
		starredRepositories.add(createRepositoryName(login, repoName));
		nodeService.setProperty(userWhoStarredNode, CoreConstants.STARRED_REPOSITORIES, starredRepositories, new ServiceContext<NodeService>());
		
		// save file
		resourceService.save(CoreConstants.USERS_PATH, new ServiceContext<ResourceService>(resourceService));
	}
	
	public void removeMember(String login, String repoName, String removedMember) {
		Node repositoryNode = resourceService.getNode(createRepositoryNodeUri(login, repoName));
		Node removedMemberNode;
		try {
			removedMemberNode = resourceService.getNode(getUriFromFragment(removedMember));
		} catch (NullPointerException e) {
			throw new RuntimeException(String.format("User with login '%s' doesn't exist!", removedMember));
		}
		
		// update MEMBERS
		List<String> members = (List<String>)repositoryNode.getPropertyValue(CoreConstants.MEMBERS);
		if (!members.contains(removedMember)) {
			throw new RuntimeException(String.format("User with login '%s' is not a member of this repo", removedMember));
		}
		members.remove(removedMember);
		nodeService.setProperty(repositoryNode, CoreConstants.MEMBERS, members, new ServiceContext<NodeService>());
		
		// update user info - MEMBER_IN_REPOSITORIES
		List<String> repositoriesWhereMember = (List<String>)removedMemberNode.getPropertyValue(CoreConstants.MEMBER_IN_REPOSITORIES);
		repositoriesWhereMember.remove(createRepositoryName(login, repoName));
		nodeService.setProperty(removedMemberNode, CoreConstants.MEMBER_IN_REPOSITORIES, repositoriesWhereMember, new ServiceContext<NodeService>());
		
		// save file
		resourceService.save(CoreConstants.USERS_PATH, new ServiceContext<ResourceService>(resourceService));
	}
	
	public void removeStarredBy(String login, String repoName, String userWhoStarredToBeRemoved) {
		Node repositoryNode = resourceService.getNode(createRepositoryNodeUri(login, repoName));
		Node userWhoStarredToBeRemovedNode = resourceService.getNode(getUriFromFragment(userWhoStarredToBeRemoved));
		
		// update STARRED_BY
		List<String> starredBy = (List<String>)repositoryNode.getPropertyValue(CoreConstants.STARRED_BY);
		if (!starredBy.contains(userWhoStarredToBeRemoved)) {
			throw new RuntimeException(String.format("User with login '%s' didn't starred this repo!", userWhoStarredToBeRemoved));
		}
		starredBy.remove(userWhoStarredToBeRemoved);
		nodeService.setProperty(repositoryNode, CoreConstants.STARRED_BY, starredBy, new ServiceContext<NodeService>());
		
		// update user info - STARRED_REPOSITORIES
		List<String> starredRepositories = (List<String>)userWhoStarredToBeRemovedNode.getPropertyValue(CoreConstants.STARRED_REPOSITORIES);
		starredRepositories.remove(createRepositoryName(login, repoName));
		nodeService.setProperty(userWhoStarredToBeRemovedNode, CoreConstants.STARRED_REPOSITORIES, starredRepositories, new ServiceContext<NodeService>());
		
		// save file
		resourceService.save(CoreConstants.USERS_PATH, new ServiceContext<ResourceService>(resourceService));
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	public String createRepositoryName(String login, String repo) {
		return login + "/" + repo;
	}
	
	public String createRepositoryNodeUri(String login, String repo) {
		return CoreConstants.USERS_PATH + "#" + createRepositoryName(login, repo);
	}
	
	public String getUriFromFragment(String fragment) {
		return CoreConstants.USERS_PATH + "#" + fragment;
	}
		
}
