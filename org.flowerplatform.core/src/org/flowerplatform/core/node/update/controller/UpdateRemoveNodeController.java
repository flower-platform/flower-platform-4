package org.flowerplatform.core.node.update.controller;

import static org.flowerplatform.core.CoreConstants.UPDATE_CHILD_REMOVED;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.controller.RemoveNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.update.remote.ChildrenUpdate;

/**
 * @author Cristina Constantinescu
 */
public class UpdateRemoveNodeController extends RemoveNodeController {

	public UpdateRemoveNodeController() {
		// must be invoked last; otherwise the modification may not be fully/correctly recorded
		setOrderIndex(100000);
	}
	
	@Override
	public void removeNode(Node node, Node child, ServiceContext context) {
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

}
