package org.flowerplatform.core.node.controller;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.resource.VirtualNodeResourceHandler;

/**
 * @author Mariana Gheorghe
 */
public class DebugControllers {

	protected VirtualNodeResourceHandler getVirtualNodeResourceHandler() {
		return CorePlugin.getInstance().getVirtualNodeResourceHandler();
	}
	
	protected Node createVirtualNode(String type, String typeSpecificPart) {
		String nodeUri = getVirtualNodeResourceHandler().createVirtualNodeUri(null, type, typeSpecificPart);
		return getVirtualNodeResourceHandler().createNodeFromRawNodeData(nodeUri, null);
	}
	
	protected void addVirtualDebugType(String type) {
		getVirtualNodeResourceHandler().addVirtualNodeType(type);
	}
	
}
