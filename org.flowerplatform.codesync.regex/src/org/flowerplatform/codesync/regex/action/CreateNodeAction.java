package org.flowerplatform.codesync.regex.action;

import java.util.HashMap;
import java.util.List;

import org.flowerplatform.codesync.regex.CodeSyncRegexConstants;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * Create a new node and keep in the context under {@link CodeSyncRegexConstants#CURRENT_NODE}.
 * The node will have the given {@link #type} and properties, mapped under {@link #propertiesKeys}.
 * 
 * @author Elena Posea
 */
public class CreateNodeAction extends RegexAction {
	
	private String type;
	private List<String> propertiesKeys;
	
	public CreateNodeAction(String type, List<String> properties) {
		this.type = type;
		this.propertiesKeys = properties;
	}

	@Override
	public void executeAction(RegexProcessingSession param) {
		// set type and properties accordingly
		Node node = new Node(null, type);
		HashMap<String, Object> parsedProperties = new HashMap<String, Object>();
		String[] listOfValues = param.getCurrentSubMatchesForCurrentRegex();
		int i, n;
		if (listOfValues != null) {
			n = listOfValues.length;
			for (i = 0; i < n; i++) {
				if (listOfValues[i] == null) {
					break; // just in case there are more keys than capture groups
				}
				parsedProperties.put(propertiesKeys.get(i), listOfValues[i]);
			}
		}
		node.setProperties(parsedProperties);
		
		// keep the node
		param.context.put(CodeSyncRegexConstants.CURRENT_NODE, node);
	}

}
