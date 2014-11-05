package org.flowerplatform.codesync.regex.controller.action;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_ATTACH_SPECIFIC_INFO_IS_CONTAINMENT_PROPERTY;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_ATTACH_SPECIFIC_INFO_KEY_PROPERTY;

import org.flowerplatform.codesync.regex.action.AttachSpecificInfoAction;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.regex.RegexAction;

/**
 * @author Elena Posea
 */
public class AttachSpecificInfoConfigurationProcessor extends RegexActionConfigurationProcessor  {

	@Override
	protected RegexAction createRegexAction(Node node) {
		String attachInfoKey = (String) node.getPropertyValue(ACTION_TYPE_ATTACH_SPECIFIC_INFO_KEY_PROPERTY);
		Boolean isContainment = (Boolean) node.getPropertyValue(ACTION_TYPE_ATTACH_SPECIFIC_INFO_IS_CONTAINMENT_PROPERTY);
		return new AttachSpecificInfoAction(attachInfoKey, isContainment);
	}
}
