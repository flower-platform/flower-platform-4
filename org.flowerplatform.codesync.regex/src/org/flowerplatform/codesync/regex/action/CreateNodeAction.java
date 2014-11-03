package org.flowerplatform.codesync.regex.action;

import java.util.HashMap;
import java.util.List;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * @author Elena Posea
 */
public class CreateNodeAction extends RegexAction {
	
	private String type;
	private List<String> properties;
	
	/**
	 * @param type the type of the action (attach info, clear info ...)
	 * @param properties
	 */
	public CreateNodeAction(String type, List<String> properties) {
		this.properties = properties;
		this.type = type;
	}

	@Override
	public void executeAction(RegexProcessingSession param) {
		Node c = new Node(null, type);
		// set type and properties accordingly
		HashMap<String, Object> listOfProperties = new HashMap<String, Object>();
		String []listOfValues = param.getCurrentSubMatchesForCurrentRegex();
		int i, n;
		if (listOfValues != null) {
			n = listOfValues.length; //because I can have more properties than actual capture groups;
			for (i = 0; i < n; i++) {
				if (listOfValues[i] == null) {
					continue;
				}
				listOfProperties.put(properties.get(i), listOfValues[i]);
			}
		}
		c.setProperties(listOfProperties);
		param.context.put("currentNode", c);
	}

}
