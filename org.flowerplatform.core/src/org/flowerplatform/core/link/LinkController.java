package org.flowerplatform.core.link;

import java.util.List;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.controller.IPropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

public class LinkController extends AbstractController implements IPropertySetter, IChildrenProvider {

	@Override
	public void setProperty(Node node, String property, Object value, ServiceContext<NodeService> context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unsetProperty(Node node, String property, ServiceContext<NodeService> context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		// TODO Auto-generated method stub
		return false;
	}

	protected Node getTargetNode(String targetNodeUri) {
		
		//TODO CM: Find node, subscribe to parent resource, create reverse links if necessary
	
		return null;
	}
	
}
