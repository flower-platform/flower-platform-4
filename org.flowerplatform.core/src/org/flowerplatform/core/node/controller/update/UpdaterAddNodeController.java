package org.flowerplatform.core.node.controller.update;

import static org.flowerplatform.core.node.update.remote.ChildrenUpdate.ADDED;

import java.util.Date;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.update.remote.ChildrenUpdate;
import org.flowerplatform.core.node.update.remote.PropertyUpdate;

public class UpdaterAddNodeController extends AddNodeController {
		
	public UpdaterAddNodeController() {
		// must be invoked last
		setOrderIndex(100000);
	}
	
	@Override
	public void addNode(Node node, Node child, Node currentChildAtInsertionPoint) {
		Node rootNode = CorePlugin.getInstance().getNodeService().getRootNode(node);
		if (rootNode != null) {
			CorePlugin.getInstance().getUpdateService().getUpdateDAO()
				.addUpdate(rootNode, new ChildrenUpdate().setTypeAs(ADDED).setTargetNodeAs(child).setTargetNodeAddedBeforeAs(currentChildAtInsertionPoint).setNodeAs(node).setTimestampAs(new Date().getTime()));
			
			// TODO CC: temporary code
			CorePlugin.getInstance().getUpdateService().getUpdateDAO()
				.addUpdate(rootNode, new PropertyUpdate().setKeyAs("hasChildren").setValueAs(true).setNodeAs(node).setTimestampAs(new Date().getTime()));
		}
	}

}
