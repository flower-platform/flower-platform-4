package org.flowerplatform.codesync.regex.controller.action;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_PROPERTY_VALID_STATES_PROPERTY;

import org.flowerplatform.codesync.regex.action.CheckStateAction;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.regex.RegexAction;

/**
 * @author Elena Posea
 */
public class CheckStateNodeConfigurationProcessor extends RegexActionConfigurationProcessor {

	@Override
	protected RegexAction createRegexAction(Node node) {
		return new CheckStateAction((String) node.getPropertyValue(ACTION_PROPERTY_VALID_STATES_PROPERTY));
	}
}
