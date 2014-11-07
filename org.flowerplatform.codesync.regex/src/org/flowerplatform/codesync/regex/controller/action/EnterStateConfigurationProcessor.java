package org.flowerplatform.codesync.regex.controller.action;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_PROPERTY_ENTER_STATE_IF_PROPERTY_SET;

import org.flowerplatform.codesync.regex.action.EnterStateAction;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.regex.RegexAction;

/**
 * @author Elena Posea
 */
public class EnterStateConfigurationProcessor extends RegexActionConfigurationProcessor {

	@Override
	protected RegexAction createRegexAction(Node node) {
		String property = (String) node.getPropertyValue(ACTION_PROPERTY_ENTER_STATE_IF_PROPERTY_SET);
		return new EnterStateAction(property);
	}
}
