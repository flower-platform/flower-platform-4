package org.flowerplatform.core.node.update.controller;

import static org.flowerplatform.core.node.NodeService.NODE_IS_RESOURCE_NODE;

import java.util.Map;

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
	public void setProperty(Node node, String key, PropertyValueWrapper wrapper, Map<String, Object> options) {	
		setUnsetProperty(node, key, wrapper.getPropertyValue(), false, options);
	}

	@Override
	public void unsetProperty(Node node, String key, Map<String, Object> options) {
		setUnsetProperty(node, key, null, true, options);
	}
	
	private void setUnsetProperty(Node node, String key, Object value, boolean isUnset, Map<String, Object> options) {		
		Node rootNode;
		if ((boolean) options.get(NODE_IS_RESOURCE_NODE)) {
			rootNode = node;
		} else {
			rootNode = CorePlugin.getInstance().getNodeService().getRootNode(node);
			if (rootNode == null) {
				return;
			}
		}
		CorePlugin.getInstance().getResourceInfoService()
			.addUpdate(rootNode.getFullNodeId(), new PropertyUpdate().setKeyAs(key).setValueAs(value).setUnsetAs(isUnset).setFullNodeIdAs(node.getFullNodeId()));		
	}

}
