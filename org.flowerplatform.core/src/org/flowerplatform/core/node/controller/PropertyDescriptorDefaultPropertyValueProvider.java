package org.flowerplatform.core.node.controller;

import java.util.List;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.util.controller.AbstractController;

public class PropertyDescriptorDefaultPropertyValueProvider extends DefaultPropertyValueProvider {

	@Override
	public Object getDefaultValue(Node node, String property, ServiceContext context) {
		List<AbstractController> propertyDescriptorList = CorePlugin.getInstance().getNodeService().getPropertyDescriptors(node);
		
		for (AbstractController element : propertyDescriptorList) {
			if (element instanceof PropertyDescriptor) {
				PropertyDescriptor propertyDescriptor = (PropertyDescriptor)element;
				if (propertyDescriptor.getName().equals(property)) {
					return propertyDescriptor.getDefaultValue();
				}
			}
		}
		return null;
	}

}
