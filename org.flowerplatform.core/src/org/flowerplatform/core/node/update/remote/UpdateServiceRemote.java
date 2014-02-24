package org.flowerplatform.core.node.update.remote;

import java.util.List;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Cristina Constantinescu
 */
public class UpdateServiceRemote {

	public List<Update> getUpdates(String rootFullNodeId, long timestampOfLastRequest) {
		return CorePlugin.getInstance().getUpdateService().getUpdates(new Node(rootFullNodeId), timestampOfLastRequest);
	}
	
	public void subscribe(Node rootNode) {	
		// TODO CC: to be implemented
	}

	public void unsubscribe(Node rootNode) {	
		// TODO CC: to be implemented
	}
	
}
