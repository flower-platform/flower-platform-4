package org.flowerplatform.codesync.regex.controller.action;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_KEEP_SPECIFIC_INFO_IS_CONTAINMENT_PROPERTY;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_KEEP_SPECIFIC_INFO_IS_LIST_PROPERTY;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_KEEP_SPECIFIC_INFO_KEY_PROPERTY;

import org.flowerplatform.codesync.regex.action.KeepSpecificInfoAction;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.regex.RegexAction;

/**
 * @author Elena Posea
 */
public class KeepSpecificInfoConfigurationProcessor extends RegexActionConfigurationProcessor {

	@Override
	protected RegexAction createRegexAction(Node node) {
		String keepInfoKey = (String) node.getPropertyValue(ACTION_TYPE_KEEP_SPECIFIC_INFO_KEY_PROPERTY);
		Boolean isList = (Boolean) node.getPropertyValue(ACTION_TYPE_KEEP_SPECIFIC_INFO_IS_LIST_PROPERTY);
		Boolean isContainment = (Boolean) node.getPropertyValue(ACTION_TYPE_KEEP_SPECIFIC_INFO_IS_CONTAINMENT_PROPERTY);
		return new KeepSpecificInfoAction(keepInfoKey, isList, isContainment);
	}
}
