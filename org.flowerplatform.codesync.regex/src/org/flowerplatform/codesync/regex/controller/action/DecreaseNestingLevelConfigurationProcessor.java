package org.flowerplatform.codesync.regex.controller.action;

import org.flowerplatform.codesync.regex.action.DecreaseNestingLevelAction;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.regex.RegexAction;

/**
 * @author Elena Posea
 */
public class DecreaseNestingLevelConfigurationProcessor extends RegexActionConfigurationProcessor {

	@Override
	protected RegexAction createRegexAction(Node node) {
		return new DecreaseNestingLevelAction();
	}
}
