package org.flowerplatform.core.mindmap;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.RemoveNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class FreeplaneRemoveNodeController extends RemoveNodeController {

	@Override
	public void removeNode(Node node, Node child) {
		NodeModel childModel = CorePlugin.getInstance().getFreeplaneUtils().getNodeModel(child.getId());
		childModel.removeFromParent();
	}

}
