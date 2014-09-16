package org.flowerplatform.codesync.regex.controller;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_EXPECTED_RESULTS_FOLDER;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_RESULT_EXTENSION;

import org.flowerplatform.codesync.regex.CodeSyncRegexConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Elena Posea
 *
 */
public class RegexExpectedModelTreeController extends CodeSyncRegexSubscribableResourceProvider {
	@Override
	protected String getResourceUri(Node node) {
		// Node parent = CorePlugin.getInstance().getNodeService().getParent(node, new ServiceContext<NodeService>());
		String repo = CoreUtils.getRepoFromNode(node);
		String testFilePath = CorePlugin.getInstance().getVirtualNodeResourceHandler().getTypeSpecificPartFromNodeUri(node.getNodeUri());
		testFilePath = testFilePath.replace("$", "/" + REGEX_EXPECTED_RESULTS_FOLDER + "/") + REGEX_RESULT_EXTENSION;
		return CoreUtils.createNodeUriWithRepo("fpp", repo, CodeSyncRegexConstants.REGEX_CONFIGS_FOLDER + "/" + testFilePath);
		// fpp:elena/repo1|.regex-configs/ActionScript/match-files/....as.match
	}

}
