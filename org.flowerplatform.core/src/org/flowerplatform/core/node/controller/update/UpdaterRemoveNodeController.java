package org.flowerplatform.core.node.controller.update;

import java.util.Date;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.RemoveNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeService;

public class UpdaterRemoveNodeController extends RemoveNodeController {

	@Override
	public void removeNode(Node node, Node child) {
//		NodeService service = (NodeService) CorePlugin.getInstance().getServiceRegistry().getService("nodeService");
//		
//		long timestamp = new Date().getTime();
//		service.setProperty(node, "timestamp", timestamp);
//		
//		service.addChildrenListUpdate(node, timestamp, ChildrenListUpdate.REMOVED, index, node);
//		service.addPropertyUpdate(node, timestamp, "hasChildren", dao.getNodeChildCount(parentNode) != 0);		
	}

}
