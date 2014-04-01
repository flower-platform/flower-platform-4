package org.flowerplatform.core.node.controller;

import java.util.HashMap;
import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class IsPropertyDefaultPropertiesProvider extends PropertiesProvider {

	public IsPropertyDefaultPropertiesProvider() {
		super();
		setOrderIndex(Integer.MAX_VALUE);
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext context) {
		Map<String, Object> isDefaultProperties = new HashMap<String, Object>();
		for (Map.Entry<String, Object> entry : node.getProperties().entrySet())	{
			if (entry.getValue() != null) { 
				if (!entry.getValue().equals(CorePlugin.getInstance().getNodeService().getDefaultPropertyValue(node, entry.getKey()))) {
					isDefaultProperties.put(entry.getKey() + ".isDefault", false);
				}
			}
		}
		node.getProperties().putAll(isDefaultProperties);
	}

}
