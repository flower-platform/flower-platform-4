package org.flowerplatform.core.node.update.controller;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.controller.PropertyValueWrapper;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.update.remote.PropertyUpdate;

/**
 * @author Cristina Constantinescu
 */
public class UpdatePropertySetterController extends PropertySetter {

	public UpdatePropertySetterController() {
		// must be invoked last; otherwise the modification may not be fully/correctly recorded
		setOrderIndex(100000);
	}
	
	@Override
	public void setProperty(Node node, String key, PropertyValueWrapper wrapper) {	
		setUnsetProperty(node, key, wrapper.getPropertyValue(), false);
	}

	@Override
	public void unsetProperty(Node node, String key) {
		setUnsetProperty(node, key, node.getOrPopulateProperties().get(key), false);
	}
	
	private void setUnsetProperty(Node node, String key, Object value, boolean isUnset) {
		Node rootNode = CorePlugin.getInstance().getNodeService().getRootNode(node);
		if (rootNode == null) {
			return;
		}
		CorePlugin.getInstance().getUpdateService().getUpdateDAO()
			.addUpdate(rootNode, new PropertyUpdate().setKeyAs(key).setValueAs(value).setUnsetAs(isUnset).setFullNodeIdAs(node.getFullNodeId()));		
	}

}
