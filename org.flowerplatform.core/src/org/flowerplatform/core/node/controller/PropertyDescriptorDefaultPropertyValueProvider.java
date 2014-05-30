package org.flowerplatform.core.node.controller;

import java.util.List;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.IController;

public class PropertyDescriptorDefaultPropertyValueProvider extends AbstractController implements IDefaultPropertyValueProvider {

	@Override
	public Object getDefaultValue(Node node, String property, ServiceContext<NodeService> context) {
		List<AbstractController> propertyDescriptorList = context.getService().getPropertyDescriptors(node);
		
		for (IController element : propertyDescriptorList) {
			PropertyDescriptor propertyDescriptor = (PropertyDescriptor) element;
			if (propertyDescriptor.getName().equals(property)) {
				return propertyDescriptor.getDefaultValue();
			}
		}
		return null;
	}

}
