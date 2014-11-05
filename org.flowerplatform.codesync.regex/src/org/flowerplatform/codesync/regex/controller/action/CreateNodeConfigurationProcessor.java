package org.flowerplatform.codesync.regex.controller.action;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_CREATE_NODE_NEW_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_CREATE_NODE_PROPERTIES;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.codesync.regex.action.CreateNodeAction;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.regex.RegexAction;

/**
 * @author Elena Posea
 */
public class CreateNodeConfigurationProcessor extends RegexActionConfigurationProcessor {

	@Override
	protected RegexAction createRegexAction(Node node) {
		String type = (String) node.getPropertyValue(ACTION_TYPE_CREATE_NODE_NEW_NODE_TYPE);
		String csvList = (String) node.getPropertyValue(ACTION_TYPE_CREATE_NODE_PROPERTIES);
		List<String> properties = new ArrayList<String>();
		if (!csvList.equals("")) {
			String[] prop = csvList.split(",");
			for (int x = 0; x < prop.length; x++) {
				properties.add(prop[x]);
			}
		}
		return new CreateNodeAction(type, properties);
	}
}
