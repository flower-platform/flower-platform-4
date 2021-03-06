package org.flowerplatform.codesync.regex.action;

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
	private String[] propertiesKeys;
	
	public CreateNodeAction(String type, String[] properties) {
		this.type = type;
		this.propertiesKeys = properties;
	}

	@Override
	public void executeAction(RegexProcessingSession param) {
		// set type and properties accordingly
		Node node = new Node(null, type);
		String[] listOfValues = param.getCurrentSubMatchesForCurrentRegex();
		int n = propertiesKeys.length;
		if (listOfValues.length > propertiesKeys.length) {
			n = propertiesKeys.length;
//			show warning to user ("More capture groups than properties keys");
		} else if (listOfValues.length < propertiesKeys.length) {
			n = propertiesKeys.length;
//			show warning to user ("More properties keys than capture groups");
		}
		for (int i = 0; i < n; i++) {
			node.getProperties().put(propertiesKeys[i], listOfValues[i]);
		}
		
		// keep the node
		param.context.put(CodeSyncRegexConstants.CURRENT_NODE, node);
	}

}
