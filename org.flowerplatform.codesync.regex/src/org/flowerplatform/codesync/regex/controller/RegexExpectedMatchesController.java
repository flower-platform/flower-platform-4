package org.flowerplatform.codesync.regex.controller;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_CONFIGS_FOLDER;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_EXPECTED_MATCHES_FOLDER;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MATCH_EXTENSION;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Elena Posea
 *
 */
public class RegexExpectedMatchesController extends CodeSyncRegexSubscribableResourceProvider {
	
	@Override
	protected String getResourceUri(Node node) {
		String repo = CoreUtils.getRepoFromNode(node);
		String testFilePath = CorePlugin.getInstance().getVirtualNodeResourceHandler().getTypeSpecificPartFromNodeUri(node.getNodeUri());
		testFilePath = testFilePath.replace("$", "/" + REGEX_EXPECTED_MATCHES_FOLDER + "/") + REGEX_MATCH_EXTENSION;
		return CoreUtils.createNodeUriWithRepo("fpp", repo, REGEX_CONFIGS_FOLDER + "/" + testFilePath);
	}

}
