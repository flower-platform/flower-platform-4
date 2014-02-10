package org.flowerplatform.core.node.controller.update;

import java.util.Date;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeService;
import org.flowerplatform.core.node.update.remote.ChildrenListUpdate;
import org.flowerplatform.core.node.update.remote.UpdaterService;

public class UpdaterAddNodeController extends AddNodeController {

	@Override
	public void addNode(Node node, Node child) {
//		UpdaterService service = (UpdaterService) CorePlugin.getInstance().getServiceRegistry().getService("updaterService");
//		NodeService nodeService = (NodeService) CorePlugin.getInstance().getServiceRegistry().getService("nodeService");
//		
//		long timestamp = new Date().getTime();
//		nodeService.setProperty(node, "timestamp", timestamp);
//		
//		service.addChildrenListUpdate(node, timestamp, ChildrenListUpdate.ADDED, -1, child);		
//		service.addPropertyUpdate(node, timestamp, "hasChildren", true);
	}

}
