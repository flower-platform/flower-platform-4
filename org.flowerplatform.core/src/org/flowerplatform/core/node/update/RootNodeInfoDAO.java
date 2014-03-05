package org.flowerplatform.core.node.update;

import static org.flowerplatform.core.node.update.SessionUnsubscriber.LAST_PING_TIMESTAMP;
import static org.flowerplatform.core.node.update.UpdateService.TIMESTAMP_OF_LAST_REQUEST;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.RootNodeInfo;
import org.flowerplatform.util.controller.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mariana Gheorghe
 */
public abstract class RootNodeInfoDAO extends AbstractController {

	private final static Logger logger = LoggerFactory.getLogger(RootNodeInfoDAO.class);
	
	protected List<RootNodeSubscriptionListener> subscriptionListeners = new ArrayList<RootNodeSubscriptionListener>();
	
	public RootNodeInfoDAO() {
		new SessionUnsubscriber(this).start();
	}
	
	/**
	 * Subscribes the client with this <code>sessionId</code> to the <code>rootNode</code>. 
	 * Notifies all registered subscription listeners if this is the first client to subscribe
	 * to this node.
	 */
	public void subscribe(String sessionId, Node rootNode) {
		logger.debug("Subscribe session {} to root node {}", sessionId, rootNode);
		
		RootNodeInfo info = getRootNodeInfoForNode(rootNode);
		if (info.getSessions().size() == 0) {
			// first subscription
			for (RootNodeSubscriptionListener listener : subscriptionListeners) {
				listener.firstClientSubscribed(rootNode, info);
			}
		}
		info.addSession(sessionId);
		
		updateSessionProperty(sessionId, TIMESTAMP_OF_LAST_REQUEST, new Date().getTime());
	}
	
	/**
	 * Unsubscribes the client with this <code>sessionId</code> from the <code>rootNode</code>. 
	 * Notifies all registered subscription listeners if this is the last client to unsubscribe
	 * from this node.
	 */
	public void unsubscribe(String sessionId, Node rootNode) {
		logger.debug("Unsubscribe session {} from root node {}", sessionId, rootNode);
	
		RootNodeInfo info = getRootNodeInfoForNode(rootNode);
		info.removeSession(sessionId);
		if (info.getSessions().size() == 0) {
			// last unsubscription
			for (RootNodeSubscriptionListener listener : subscriptionListeners) {
				listener.lastClientUnubscribed(rootNode, info);
			}
		}
	}
	
	public void stillSubscribedPing(String sessionid, Node rootNode) {
		updateSessionProperty(sessionid, LAST_PING_TIMESTAMP, new Date().getTime());
	}
	
	public void sessionCreated(String sessionId) {
		HttpServletRequest request = CorePlugin.getInstance().getRequest();
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		updateSessionProperty(sessionId, "ip", ipAddress);
	}
	
	public void sessionRemoved(String sessionId) {
		Collection<Node> rootNodes = new CopyOnWriteArrayList<Node>(getRootNodesForSession(sessionId));
		for (Node rootNode : rootNodes) {
			unsubscribe(sessionId, rootNode);
		}
	}
	
	public abstract void updateSessionProperty(String sessionId, String property, Object value);
	
	public abstract Object getSessionProperty(String sessionId, String property);
	
	public void addRootNodeInfoSubscriptionListener(RootNodeSubscriptionListener listener) {
		subscriptionListeners.add(listener);
	}
	
	public abstract Collection<String> getSubscribedSessions();
	
	public abstract Collection<Node> getRootNodesForSession(String sessionId);

	public abstract RootNodeInfo getRootNodeInfoForNode(Node rootNode);
	
}
