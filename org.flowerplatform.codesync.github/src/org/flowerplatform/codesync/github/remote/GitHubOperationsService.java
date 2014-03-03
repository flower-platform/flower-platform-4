package org.flowerplatform.codesync.github.remote;

import org.eclipse.egit.github.core.RepositoryId;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.Match;
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

	public void synchronize() {
		
		Node root = CodeSyncPlugin.getInstance().getCodeSyncMappingRoot(null);
		Node client = null;
		for (Node child : CorePlugin.getInstance().getNodeService().getChildren(root, true)) {
			if (GitHubConstants.REPOSITORY.equals(child.getType())) {
				client = child;
				break;
			}
		}
		
		if (client == null) {
			throw new RuntimeException("No GitHub client found as a child of root");
		}
		
		// START THE ALGORITHM
		
		// STEP 1: create a match
		Match match = new Match();
		
		// ancestor + left: model (Node structure)
		match.setAncestor(client);
		match.setLeft(client);
		
		// right: github
		match.setRight(new RepositoryId("flower-platform", "flower-platform-4"));
		
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
	
}
