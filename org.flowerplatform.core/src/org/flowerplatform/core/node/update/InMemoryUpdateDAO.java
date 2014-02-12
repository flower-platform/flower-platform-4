package org.flowerplatform.core.node.update;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.RootNodeInfo;
import org.flowerplatform.core.node.update.remote.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stores informations for each subscribed root node in {@link RootNodeInfo}, in memory.
 * 
 * @author Cristina Constantinescu
 */
public class InMemoryUpdateDAO extends UpdateDAO {

	private final static Logger logger = LoggerFactory.getLogger(InMemoryUpdateDAO.class);

	private Map<Node, RootNodeInfo> nodeToRootNodeInfo = new HashMap<Node, RootNodeInfo>();

	@Override
	public void addUpdate(Node node, Update update) {
		if (!nodeToRootNodeInfo.containsKey(node)) {
			nodeToRootNodeInfo.put(node, new RootNodeInfo());
		}
		nodeToRootNodeInfo.get(node).addUpdate(update);
		logger.debug("UPDATE ADDED for {} : {}", node, update);
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
