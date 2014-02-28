package org.flowerplatform.codesync.github.remote;

import static org.flowerplatform.codesync.github.CodeSyncGitHubPlugin.GITHUB;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.Match;
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
		root = CorePlugin.getInstance().getNodeService().getChildren(root, true).get(0);
		
		// START THE ALGORITHM
		
		// STEP 1: create a match
		Match match = new Match();
		
		// ancestor + left: model (Node structure)
		match.setAncestor(root);
		match.setLeft(root);
		
		// right: github
		match.setRight(new GitHubClient());
		
		// initialize the algorithm
		ITypeProvider typeProvider = new ComposedTypeProvider()
		.addTypeProvider(CodeSyncPlugin.getInstance().getTypeProvider("node"))
		.addTypeProvider(CodeSyncPlugin.getInstance().getTypeProvider(GITHUB));
		TypeDescriptorRegistry typeDescriptorRegistry = CorePlugin.getInstance().getNodeTypeDescriptorRegistry();
		
		CodeSyncAlgorithm algorithm = new CodeSyncAlgorithm(typeDescriptorRegistry, typeProvider);
		match.setCodeSyncAlgorithm(algorithm);
		
		// STEP 2: generate the diff, i.e. 3-way compare
		algorithm.generateDiff(match, true);
	}
	
}
