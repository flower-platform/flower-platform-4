package org.flowerplatform.core.node.controller;

import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.IController;

/**
 * @author Sebastian Solomon
 */
public class DefaultPropertiesProvider extends AbstractController implements IPropertiesProvider {

	public DefaultPropertiesProvider() {
		super();
		setOrderIndex(10000);
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		List<AbstractController> propDescriptors =  context.getService().getPropertyDescriptors(node); 
		for (IController element : propDescriptors) {
			PropertyDescriptor propertyDescriptor = (PropertyDescriptor) element;			
			if (propertyDescriptor.getHasChangeCheckbox()) {
				Object nodeDefaultPropertyValue = context.getService().getDefaultPropertyValue(node, propertyDescriptor.getName(), new ServiceContext<NodeService>(context.getService()));
				node.getProperties().put(String.format(CoreConstants.PROPERTY_DEFAULT_FORMAT, propertyDescriptor.getName()), nodeDefaultPropertyValue);
			}			
		}
	}
}
