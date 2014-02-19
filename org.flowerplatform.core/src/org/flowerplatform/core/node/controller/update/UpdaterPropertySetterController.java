package org.flowerplatform.core.node.controller.update;

import java.util.Date;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.controller.PropertyValueWrapper;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.update.remote.PropertyUpdate;

/**
 * @author Cristina Constantinescu
 */
public class UpdaterPropertySetterController extends PropertySetter {

	public UpdaterPropertySetterController() {
		// must be invoked last
		setOrderIndex(100000);
	}
	
	@Override
	public void setProperty(Node node, String key, PropertyValueWrapper wrapper) {	
		Node rootNode = CorePlugin.getInstance().getNodeService().getRootNode(node);
		if (rootNode != null) {
			CorePlugin.getInstance().getUpdateService().getUpdateDAO()
				.addUpdate(rootNode, new PropertyUpdate().setKeyAs(key).setValueAs(wrapper.getPropertyValue()).setNodeAs(node).setTimestampAs(new Date().getTime()));
		}
	}

	@Override
	public void unsetProperty(Node node, String key) {
		Node rootNode = CorePlugin.getInstance().getNodeService().getRootNode(node);
		if (rootNode != null) {
			CorePlugin.getInstance().getUpdateService().getUpdateDAO()
				.addUpdate(rootNode, new PropertyUpdate().setKeyAs(key).setUnsetAs(true).setNodeAs(node).setTimestampAs(new Date().getTime()));
		}
	}

}
