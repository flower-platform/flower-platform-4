package org.flowerplatform.codesync.regex.controller.action;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_PROPERTY_INFO_IS_CONTAINMENT;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_PROPERTY_INFO_IS_LIST;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_PROPERTY_INFO_KEY;

import org.flowerplatform.codesync.regex.action.KeepSpecificInfoAction;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.regex.RegexAction;

/**
 * @author Elena Posea
 */
public class KeepSpecificInfoConfigurationProcessor extends RegexActionConfigurationProcessor {

	@Override
	protected RegexAction createRegexAction(Node node) {
		String keepInfoKey = (String) node.getPropertyValue(ACTION_PROPERTY_INFO_KEY);
		Boolean isList = (Boolean) node.getPropertyValue(ACTION_PROPERTY_INFO_IS_LIST);
		Boolean isContainment = (Boolean) node.getPropertyValue(ACTION_PROPERTY_INFO_IS_CONTAINMENT);
		return new KeepSpecificInfoAction(keepInfoKey, isList, isContainment);
	}
}
