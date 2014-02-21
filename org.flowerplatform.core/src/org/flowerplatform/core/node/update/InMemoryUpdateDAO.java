package org.flowerplatform.core.node.update;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.node.remote.InMemoryRootNodeInfo;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.RootNodeInfo;
import org.flowerplatform.core.node.update.remote.Update;

/**
 * Stores informations for each subscribed root node in {@link RootNodeInfo}, in memory.
 * 
 * @author Cristina Constantinescu
 */
public class InMemoryUpdateDAO extends UpdateDAO {

	private Map<Node, RootNodeInfo> nodeToRootNodeInfo = new HashMap<Node, RootNodeInfo>();

	@Override
	protected void doAddUpdate(Node node, Update update) {
		if (!nodeToRootNodeInfo.containsKey(node)) {
			nodeToRootNodeInfo.put(node, new InMemoryRootNodeInfo());
		}
		nodeToRootNodeInfo.get(node).addUpdate(update);		
	}

	@Override
	public List<Update> getUpdates(Node rootNode, long timestampOfLastRequest) {	
		List<Update> updatesAddedAfterLastRequest = new ArrayList<Update>();
		List<Update> updates = null;
		if (nodeToRootNodeInfo.containsKey(rootNode)) {
			updates = nodeToRootNodeInfo.get(rootNode).getUpdates();
		}
		if (updates != null) {
			for (Update update : updates) {
				if (update.getTimestamp() > timestampOfLastRequest) {
					updatesAddedAfterLastRequest.add(update);
				}
			}
		}
		
		Collections.sort(updatesAddedAfterLastRequest);
		
		return updatesAddedAfterLastRequest;
	}

}
