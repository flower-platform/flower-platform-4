package org.flowerplatform.codesync.github.remote;

import java.io.IOException;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.github.CodeSyncGitHubPlugin;
import org.flowerplatform.codesync.github.GitHubConstants;
import org.flowerplatform.codesync.type_provider.ComposedTypeProvider;
import org.flowerplatform.codesync.type_provider.ITypeProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;

/**
 * @author Mariana Gheorghe
 */
public class GitHubOperationsService {

	public void synchronize(String fullNodeId) {
		
		Node root = new Node(fullNodeId);
		
		// START THE ALGORITHM
		
		// STEP 1: create a match
		Match match = new Match();
		
		// ancestor + left: model (Node structure)
		match.setAncestor(root);
		match.setLeft(root);
		
		// right: github
		Object right = null;
		
		if (GitHubConstants.REPOSITORY.equals(root.getType())) {
			right = getRepository(root);
		} else if (GitHubConstants.PULL_REQUEST.equals(root.getType())) {
			int number = (int) root.getOrPopulateProperties().get(GitHubConstants.PULL_REQUEST_NUMBER);
			try {
				Node parent = CorePlugin.getInstance().getNodeService().getParent(root);
				right = new PullRequestService(CodeSyncGitHubPlugin.getInstance().getClient())
						.getPullRequest(getRepository(parent), number);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		match.setRight(right);
		
		// initialize the algorithm
		ITypeProvider typeProvider = new ComposedTypeProvider()
		.addTypeProvider(CodeSyncPlugin.getInstance().getTypeProvider("node"))
		.addTypeProvider(CodeSyncPlugin.getInstance().getTypeProvider(GitHubConstants.GITHUB));
		TypeDescriptorRegistry typeDescriptorRegistry = CorePlugin.getInstance().getNodeTypeDescriptorRegistry();
		
		CodeSyncAlgorithm algorithm = new CodeSyncAlgorithm(typeDescriptorRegistry, typeProvider);
		match.setCodeSyncAlgorithm(algorithm);
		
		// STEP 2: generate the diff, i.e. 3-way compare
		algorithm.generateDiff(match, true);
	}
	
	public IRepositoryIdProvider getRepository(Node node) {
		String repositoryId = (String) node.getOrPopulateProperties().get("text");
		return RepositoryId.createFromId(repositoryId);
	}
	
}
