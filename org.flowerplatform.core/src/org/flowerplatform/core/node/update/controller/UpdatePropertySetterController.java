package org.flowerplatform.core.node.update.controller;

import static org.flowerplatform.core.CoreConstants.NODE_IS_RESOURCE_NODE;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.ServiceContext;
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
	public void setProperty(Node node, String key, PropertyValueWrapper wrapper, ServiceContext context) {	
		setUnsetProperty(node, key, wrapper.getPropertyValue(), false, context);
	}

	@Override
	public void unsetProperty(Node node, String key, ServiceContext context) {
		setUnsetProperty(node, key, null, true, context);
	}
	
	private void setUnsetProperty(Node node, String key, Object value, boolean isUnset, ServiceContext context) {		
		Node resourceNode;
		if (context.getValue(NODE_IS_RESOURCE_NODE)) {
			resourceNode = node;
		} else {
			resourceNode = CoreUtils.getResourceNode(node);
			if (resourceNode == null) {
				return;
			}
		}

		CorePlugin.getInstance().getResourceService()
			.addUpdate(resourceNode.getFullNodeId(), new PropertyUpdate().setKeyAs(key).setValueAs(value).setUnsetAs(isUnset).setFullNodeIdAs(node.getFullNodeId()));		
	}

}
