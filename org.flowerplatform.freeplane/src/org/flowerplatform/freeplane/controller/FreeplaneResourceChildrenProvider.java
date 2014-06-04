package org.flowerplatform.freeplane.controller;

import java.util.Collections;
import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.flowerplatform.util.controller.AbstractController;
import org.freeplane.features.map.NodeModel;

/**
 * Returns the root node from the freeplane file corresponding to the node.
 * It must be invoked <b>before</b> the {@link FileChildrenProvider}
 * and also set the {@link CoreConstants#DONT_PROCESS_OTHER_CONTROLLERS}
 * to make sure that other providers do not return children.
 * 
 * @author Mariana Gheorghe
 */
public class FreeplaneResourceChildrenProvider extends AbstractController implements IChildrenProvider {

	public FreeplaneResourceChildrenProvider() {
		super();
		// children controller must be invoked before the file children provider
		setOrderIndex(-10000);
	}
	
	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		if (!CoreUtils.isSubscribable(node.getOrPopulateProperties())) {
			return Collections.emptyList();
		}
		
		NodeModel nodeModel = (NodeModel) node.getRawNodeData();
		List<Node> children = Collections.singletonList(FreeplanePlugin.getInstance().getStandardNode(nodeModel, node.getNodeUri()));
		context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
		return children;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return CoreUtils.isSubscribable(node.getProperties());
	}
	
}
