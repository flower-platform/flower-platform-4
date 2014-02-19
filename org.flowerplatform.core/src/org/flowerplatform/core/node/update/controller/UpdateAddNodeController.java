package org.flowerplatform.core.node.update.controller;

import static org.flowerplatform.core.NodePropertiesConstants.HAS_CHILDREN;
import static org.flowerplatform.core.node.update.remote.ChildrenUpdate.ADDED;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.update.remote.ChildrenUpdate;

public class UpdateAddNodeController extends AddNodeController {
		
	public UpdateAddNodeController() {
		// must be invoked last
		setOrderIndex(100000);
	}
	
	@Override
	public void addNode(Node node, Node child, Node insertBeforeNode) {		
		Node rootNode = CorePlugin.getInstance().getNodeService().getRootNode(node);
		if (rootNode != null) {
			CorePlugin.getInstance().getUpdateService().getUpdateDAO()
				.addUpdate(rootNode, 
						new ChildrenUpdate()
							.setTypeAs(ADDED)
							.setTargetNodeAs(child)
							.setFullTargetNodeAddedBeforeIdAs(insertBeforeNode != null ? insertBeforeNode.getFullNodeId() : null)
							.setFullNodeIdAs(node.getFullNodeId()));
			
			CorePlugin.getInstance().getNodeService().setProperty(node, HAS_CHILDREN, true);			
		}
	}

}
