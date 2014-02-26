package org.flowerplatform.core.node.update;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.flowerplatform.core.node.remote.InMemoryRootNodeInfo;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.RootNodeInfo;

/**
 * Keeps in-memory maps of {@link RootNodeInfo}s and information about subscribed clients (sessions).
 * 
 * @author Mariana Gheorghe
 */
public class InMemoryRootNodeInfoDAO extends RootNodeInfoDAO {

	private Map<Node, InMemoryRootNodeInfo> nodeToRootNodeInfo = new HashMap<Node, InMemoryRootNodeInfo>();

	private Map<String, Set<Node>> sessionIdToRootNodes = new HashMap<String, Set<Node>>();

	private Map<String, Map<String, Object>> sessionProperties = new HashMap<String, Map<String, Object>>();

	public InMemoryRootNodeInfoDAO() {
		addRootNodeInfoSubscriptionListener(new InMemoryRootNodeSubscriptionListener());
	}
	
	@Override
	public void subscribe(String sessionId, Node rootNode) {
		super.subscribe(sessionId, rootNode);
		
		getRootNodesForSession(sessionId).add(rootNode);
	}

	@Override
	public void unsubscribe(String sessionId, Node rootNode) {
		super.unsubscribe(sessionId, rootNode);
		
		getRootNodesForSession(sessionId).remove(rootNode);
		if (getRootNodesForSession(sessionId).size() == 0) {
			// remove this key from the maps
			sessionIdToRootNodes.remove(sessionId);
			sessionProperties.remove(sessionId);
		}
	}
	
	public void updateSessionProperty(String sessionId, String property, Object value) {
		getSessionProperties(sessionId).put(property, value);
	}
	
	@Override
	public Object getSessionProperty(String sessionId, String property) {
		return getSessionProperties(sessionId).get(property);
	}
	
	@Override
	public Collection<String> getSubscribedSessions() {
		return sessionIdToRootNodes.keySet();
	}
	
	@Override
	public Set<Node> getRootNodesForSession(String sessionId) {
		Set<Node> rootNodes = sessionIdToRootNodes.get(sessionId);
		if (rootNodes == null) {
			rootNodes = new HashSet<Node>();
			sessionIdToRootNodes.put(sessionId, rootNodes);
		}
		return rootNodes;
	}
	
	@Override
	public InMemoryRootNodeInfo getRootNodeInfoForNode(Node rootNode) {
		InMemoryRootNodeInfo info = nodeToRootNodeInfo.get(rootNode);
		if (info == null) {
			info = new InMemoryRootNodeInfo();
			nodeToRootNodeInfo.put(rootNode, info);
		}
		return info;
	}
	
	public Map<String, Object> getSessionProperties(String sessionId) {
		Map<String, Object> properties = sessionProperties.get(sessionId);
		if (properties == null) {
			properties = new HashMap<String, Object>();
			sessionProperties.put(sessionId, properties);
		}
		return properties;
	}

}
