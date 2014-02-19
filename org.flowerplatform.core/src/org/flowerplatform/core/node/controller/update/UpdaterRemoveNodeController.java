package org.flowerplatform.core.node.controller.update;

import static org.flowerplatform.core.node.update.remote.ChildrenUpdate.REMOVED;

import java.util.Date;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.RemoveNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.update.remote.ChildrenUpdate;

/**
 * @author Cristina Constantinescu
 */
public class UpdaterRemoveNodeController extends RemoveNodeController {

	public UpdaterRemoveNodeController() {
		// must be invoked after persistence controller 
		setOrderIndex(100000);
	}
	
	@Override
	public void removeNode(Node node, Node child) {
		Node rootNode = CorePlugin.getInstance().getNodeService().getRootNode(node);
		if (rootNode != null) {
			CorePlugin.getInstance().getUpdateService().getUpdateDAO()
				.addUpdate(rootNode, new ChildrenUpdate().setTypeAs(REMOVED).setTargetNodeAs(child).setNodeAs(node).setTimestampAs(new Date().getTime()));			
		}
	}

}
