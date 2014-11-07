package org.flowerplatform.codesync.regex.controller.action;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_PROPERTY_CREATE_NODE_NEW_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_PROPERTY_CREATE_NODE_PROPERTIES;

import org.flowerplatform.codesync.regex.action.CreateNodeAction;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.regex.RegexAction;

/**
 * @author Elena Posea
 */
public class CreateNodeConfigurationProcessor extends RegexActionConfigurationProcessor {

	@Override
	protected RegexAction createRegexAction(Node node) {
		String type = (String) node.getPropertyValue(ACTION_PROPERTY_CREATE_NODE_NEW_NODE_TYPE);
		String properties = (String) node.getPropertyValue(ACTION_PROPERTY_CREATE_NODE_PROPERTIES);
		return new CreateNodeAction(type, properties.split(","));
	}
}
