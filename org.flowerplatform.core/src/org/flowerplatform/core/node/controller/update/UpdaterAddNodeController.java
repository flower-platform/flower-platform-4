package org.flowerplatform.core.node.controller.update;

import java.util.Date;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeService;

public class UpdaterAddNodeController extends AddNodeController {

	@Override
	public void addNode(Node node, Node child) {
		NodeService service = (NodeService) CorePlugin.getInstance().getServiceRegistry().getService("nodeService");
		
		long timestamp = new Date().getTime();
		service.setProperty(node, "timestamp", timestamp);
		
//		service.addChildrenListUpdate(node, timestamp, ChildrenListUpdate.ADDED, index, child);		
//		service.addPropertyUpdate(node, timestamp, "hasChildren", true);
	}

}
