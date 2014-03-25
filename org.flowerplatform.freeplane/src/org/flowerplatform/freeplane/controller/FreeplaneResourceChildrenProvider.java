package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.core.ServiceContext.DONT_PROCESS_OTHER_CONTROLLERS;

import java.util.Collections;
import java.util.List;

import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.file.FileChildrenProvider;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.freeplane.features.map.NodeModel;

/**
 * Returns the root node from the freeplane file corresponding to the node.
 * It must be invoked <b>before</b> the {@link FileChildrenProvider}
 * and also set the {@link ServiceContext#DONT_PROCESS_OTHER_CONTROLLERS}
 * to make sure that other providers do not return children.
 * 
 * @author Mariana Gheorghe
 */
public class FreeplaneResourceChildrenProvider extends ChildrenProvider {

	public FreeplaneResourceChildrenProvider() {
		super();
		// children controller must be invoked before the file children provider
		setOrderIndex(-10000);
	}
	
	@Override
	public List<Node> getChildren(Node node, ServiceContext context) {
		if (!CoreUtils.isSubscribable(node.getOrPopulateProperties())) {
			return Collections.emptyList();
		}
		
		NodeModel nodeModel = (NodeModel) node.getOrRetrieveRawNodeData();
		List<Node> children = Collections.singletonList(FreeplanePlugin.getInstance().getFreeplaneUtils()
				.getStandardNode(nodeModel, node.getFullNodeId()));
		context.put(DONT_PROCESS_OTHER_CONTROLLERS, true);
		return children;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext context) {
		return CoreUtils.isSubscribable(node.getProperties());
	}
	
}
