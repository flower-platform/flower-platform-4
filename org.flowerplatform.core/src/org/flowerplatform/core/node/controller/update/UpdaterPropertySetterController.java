package org.flowerplatform.core.node.controller.update;

import java.util.Date;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeService;
import org.flowerplatform.core.node.update.remote.UpdaterService;

public class UpdaterPropertySetterController extends PropertySetter {

	@Override
	public void setProperty(Node node, String property, Object value) {
		if ("timestamp".equals(property)) {
			return;
		}
		UpdaterService service = (UpdaterService) CorePlugin.getInstance().getServiceRegistry().getService("updaterService");
		NodeService nodeService = (NodeService) CorePlugin.getInstance().getServiceRegistry().getService("nodeService");
		
		long timestamp = new Date().getTime();
		nodeService.setProperty(node, "timestamp", timestamp);
		
		service.addPropertyUpdate(node, timestamp, property, value);
	}

	@Override
	public void unsetProperty(Node node, String property) {
		setProperty(node, property, null);
	}

}
