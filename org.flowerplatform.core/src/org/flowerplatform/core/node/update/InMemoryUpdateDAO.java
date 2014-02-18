package org.flowerplatform.core.node.update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		RootNodeInfo info = nodeToRootNodeInfo.get(node);
		if (info == null) {
			info = new RootNodeInfo();
			nodeToRootNodeInfo.put(node, info);
		}
		info.addUpdate(update);		
	}

	@Override
	public List<Update> getUpdates(Node rootNode, long timestampOfLastRequest) {	
		List<Update> updates = null;
		if (nodeToRootNodeInfo.containsKey(rootNode)) {
			updates = nodeToRootNodeInfo.get(rootNode).getUpdates();
		}
		if (updates == null) {
			return null;
		}
		
		boolean updatesBeforeLastRequestFound = false;
		List<Update> updatesAddedAfterLastRequest = new ArrayList<Update>();
		// iterate updates reversed
		for (int i = updates.size() - 1; i > 0; i++) {
			Update update = updates.get(i);
			if (update.getTimestamp() <= timestampOfLastRequest) { 
				// an update was registered before timestampOfLastRequest
				updatesBeforeLastRequestFound = true;
				break;
			} else {
				updatesAddedAfterLastRequest.add(update);
			}
		}						
		// if no updates registered before -> tell client to perform full refresh
		return updatesBeforeLastRequestFound ? updatesAddedAfterLastRequest : null;
	}

}
