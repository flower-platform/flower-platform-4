package org.flowerplatform.freeplane.client;

import org.flowerplatform.js_client.java.node.ClientNode;
import org.freeplane.core.extension.IExtension;

/**
 * @author Valentina Bojan
 */
public class ClientNodeModel implements IExtension {

	private ClientNode node;
	
	public ClientNode getNode() {
		return node;
	}
	
	public void setNode(ClientNode node) {
		this.node = node;
	}
	
}
