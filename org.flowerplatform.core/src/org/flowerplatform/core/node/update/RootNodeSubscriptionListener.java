package org.flowerplatform.core.node.update;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.RootNodeInfo;

/**
 * @author Mariana Gheorghe
 */
public interface RootNodeSubscriptionListener {

	public void firstClientSubscribed(Node rootNode, RootNodeInfo rootNodeInfo);
	
	public void lastClientUnubscribed(Node rootNode, RootNodeInfo rootNodeInfo);
	
}
