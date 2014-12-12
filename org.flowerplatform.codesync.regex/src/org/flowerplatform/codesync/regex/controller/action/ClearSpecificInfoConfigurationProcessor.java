package org.flowerplatform.codesync.regex.controller.action;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_CLEAR_SPECIFIC_INFO_KEY_PROPERTY;

import org.flowerplatform.codesync.regex.action.ClearSpecificInfoAction;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.regex.RegexAction;

/**
 * @author Elena Posea
 */
public class ClearSpecificInfoConfigurationProcessor extends RegexActionConfigurationProcessor  {

	@Override
	protected RegexAction createRegexAction(Node node) {
		return new ClearSpecificInfoAction((String) node.getPropertyValue(ACTION_TYPE_CLEAR_SPECIFIC_INFO_KEY_PROPERTY));
	}
}
