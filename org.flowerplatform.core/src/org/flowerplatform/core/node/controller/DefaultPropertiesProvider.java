package org.flowerplatform.core.node.controller;

import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Sebastian Solomon
 */
public class DefaultPropertiesProvider extends PropertiesProvider {

	public DefaultPropertiesProvider() {
		super();
		setOrderIndex(10000);
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext context) {
		List<AbstractController> propDescriptors =  CorePlugin.getInstance().getNodeService().getPropertyDescriptors(node); 
		for (AbstractController element : propDescriptors) {
			PropertyDescriptor propertyDescriptor = (PropertyDescriptor) element;			
			if (propertyDescriptor.getHasChangeCheckbox()) {
				Object nodeDefaultPropertyValue = CorePlugin.getInstance().getNodeService().getDefaultPropertyValue(node, propertyDescriptor.getName(), new ServiceContext());
				node.getProperties().put(String.format(CoreConstants.PROPERTY_DEFAULT_FORMAT, propertyDescriptor.getName()), nodeDefaultPropertyValue);
			}			
		}
	}
}
