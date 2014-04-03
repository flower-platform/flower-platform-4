package org.flowerplatform.core.node.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Sebastian Solomon
 */
public class SetPropertyDefaultPropertiesProvider extends PropertiesProvider {

	public SetPropertyDefaultPropertiesProvider() {
		super();
		setOrderIndex(Integer.MAX_VALUE);
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext context) {
		List<AbstractController> propDescriptors =  CorePlugin.getInstance().getNodeService().getPropertyDescriptors(node, null); 
		for (AbstractController element : propDescriptors) {
			PropertyDescriptor propertyDescriptor;
			if (element instanceof PropertyDescriptor) {
				propertyDescriptor = (PropertyDescriptor)element;
				propertyDescriptor.getName();
				if (propertyDescriptor.getHasChangeCheckbox()) {
					Object nodeDefaultPropertyValue = CorePlugin.getInstance().getNodeService().getDefaultPropertyValue(node, propertyDescriptor.getName());
					node.getProperties().put(propertyDescriptor.getName() + ".default", nodeDefaultPropertyValue);
				}
			}
		}
	}
}
