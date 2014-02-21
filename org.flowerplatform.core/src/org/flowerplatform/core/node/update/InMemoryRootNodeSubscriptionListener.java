package org.flowerplatform.core.node.update;

import org.flowerplatform.core.node.remote.InMemoryRootNodeInfo;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.RootNodeInfo;

/**
 * Responsible with loading a resource on the first client subscribed and unloading the resource
 * on the last client unsubscribed.
 * 
 * @author Mariana Gheorghe
 */
public class InMemoryRootNodeSubscriptionListener implements RootNodeSubscriptionListener {

	@Override
	public void firstClientSubscribed(Node rootNode, RootNodeInfo rootNodeInfo) {
		getInMemoryRootNodeInfo(rootNodeInfo).getResourceIdToResource().put(rootNode.getResource(), rootNode.getOrRetrieveRawNodeData());
	}

	@Override
	public void lastClientUnubscribed(Node rootNode, RootNodeInfo rootNodeInfo) {
		getInMemoryRootNodeInfo(rootNodeInfo).getResourceIdToResource().remove(rootNode.getResource());
	}

	protected InMemoryRootNodeInfo getInMemoryRootNodeInfo(RootNodeInfo rootNodeInfo) {
		return (InMemoryRootNodeInfo) rootNodeInfo;
	}
}
