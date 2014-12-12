package org.flowerplatform.codesync.regex.controller.action;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_PROPERTY_INFO_IS_CONTAINMENT;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_PROPERTY_INFO_KEY;

import org.flowerplatform.codesync.regex.action.AttachSpecificInfoAction;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.regex.RegexAction;

/**
 * @author Elena Posea
 */
public class AttachSpecificInfoConfigurationProcessor extends RegexActionConfigurationProcessor  {

	@Override
	protected RegexAction createRegexAction(Node node) {
		String attachInfoKey = (String) node.getPropertyValue(ACTION_PROPERTY_INFO_KEY);
		Boolean isContainment = (Boolean) node.getPropertyValue(ACTION_PROPERTY_INFO_IS_CONTAINMENT);
		return new AttachSpecificInfoAction(attachInfoKey, isContainment);
	}
}
