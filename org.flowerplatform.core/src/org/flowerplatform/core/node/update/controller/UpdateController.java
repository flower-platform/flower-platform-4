package org.flowerplatform.core.node.update.controller;

import static org.flowerplatform.core.CoreConstants.NODE_IS_RESOURCE_NODE;
import static org.flowerplatform.core.CoreConstants.UPDATE_CHILD_ADDED;
import static org.flowerplatform.core.CoreConstants.UPDATE_CHILD_REMOVED;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IAddNodeController;
import org.flowerplatform.core.node.controller.IPropertySetter;
import org.flowerplatform.core.node.controller.IRemoveNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.update.remote.ChildrenUpdate;
import org.flowerplatform.core.node.update.remote.PropertyUpdate;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cristina Constantinescu
 */
public class UpdateController extends AbstractController
		implements IAddNodeController, IRemoveNodeController, IPropertySetter {
	
	public UpdateController() {
		// must be invoked last; otherwise the modification may not be fully/correctly recorded
		setOrderIndex(100000);
	}
	
	@Override
	public void addNode(Node node, Node child, ServiceContext<NodeService> context) {		
		Node resourceNode = CoreUtils.getResourceNode(node);
		String insertBeforeFullNodeId = (String) context.get(CoreConstants.INSERT_BEFORE_FULL_NODE_ID);
		if (resourceNode != null) {
			CorePlugin.getInstance().getResourceService()
				.addUpdate(resourceNode.getFullNodeId(), 
						new ChildrenUpdate()
							.setTypeAs(UPDATE_CHILD_ADDED)
							.setTargetNodeAs(child)
							.setFullTargetNodeAddedBeforeIdAs(insertBeforeFullNodeId)
							.setFullNodeIdAs(node.getFullNodeId()));
			
		}
	}
	
	@Override
	public void removeNode(Node node, Node child, ServiceContext<NodeService> context) {
		Node resourceNode = CoreUtils.getResourceNode(node);
		if (resourceNode != null) {
			CorePlugin.getInstance().getResourceService()
				.addUpdate(resourceNode.getFullNodeId(), 
						new ChildrenUpdate()
							.setTypeAs(UPDATE_CHILD_REMOVED)
							.setTargetNodeAs(child)
							.setFullNodeIdAs(node.getFullNodeId()));		
		}
	}
	
	@Override
	public void setProperty(Node node, String key, Object value, ServiceContext<NodeService> context) {	
		setUnsetProperty(node, key, value, false, context);
	}

	@Override
	public void unsetProperty(Node node, String key, ServiceContext<NodeService> context) {
		setUnsetProperty(node, key, node.getOrPopulateProperties().get(key), false, context);
	}
	
	private void setUnsetProperty(Node node, String key, Object value, boolean isUnset, ServiceContext<NodeService> context) {		
		Node resourceNode;
		if (context.getBooleanValue(NODE_IS_RESOURCE_NODE)) {
			resourceNode = node;
		} else {
			resourceNode = CoreUtils.getResourceNode(node);
			if (resourceNode == null) {
				return;
			}
		}

		CorePlugin.getInstance().getResourceService()
			.addUpdate(resourceNode.getFullNodeId(), new PropertyUpdate().setKeyAs(key).setValueAs(value).setUnsetAs(isUnset).setFullNodeIdAs(node.getFullNodeId()));		
	}

}
