package org.flowerplatform.codesync.regex.controller;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.controller.IPropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.IController;

public class RegexActionController  extends AbstractController implements IPropertiesProvider, IPropertySetter {

	public RegexActionController() {
		super();
		// invoked after the persistence providers
		setOrderIndex(10000);
	}
	
	@Override
	public void setProperty(Node node, String property, Object value, ServiceContext<NodeService> context) {
//		if(property.equals(arg0)){
//			
//		}
	}

	@Override
	public void unsetProperty(Node node, String property, ServiceContext<NodeService> context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		// TODO Auto-generated method stub
		
	}

}
