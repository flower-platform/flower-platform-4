package org.flowerplatform.core.node.resource;

import static org.flowerplatform.core.node.NodeService.STOP_CONTROLLER_INVOCATION;
import static org.flowerplatform.core.node.resource.ResourceSubscriptionListener.RESOURCE_SUBSCRIPTION_LISTENER;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.update.remote.Update;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mariana Gheorghe
 */
public class ResourceInfoService {

	private final static Logger logger = LoggerFactory.getLogger(ResourceInfoService.class);
	
	private TypeDescriptorRegistry registry;
	
	private IResourceInfoDAO resourceInfoDao;
	
	public ResourceInfoService(TypeDescriptorRegistry registry, IResourceInfoDAO resourceInfoDao) {
		this.registry = registry;
		this.resourceInfoDao = resourceInfoDao;
		
		new ResourceUnsubscriber().start();
	}
	
	public Node subscribeToParentResource(String nodeId, String sessionId) {
		logger.debug("Subscribe session {} to parent of {}", sessionId, nodeId);
		
		Node rootNode = CorePlugin.getInstance().getNodeService().getRootNode(new Node(nodeId));
		sessionSubscribedToResource(rootNode.getFullNodeId(), sessionId);
		return rootNode;
	}
	
	/**
	 * Subscribes the client with this <code>sessionId</code> to the <code>rootNode</code>. 
	 * Notifies all registered subscription listeners if this is the first client to subscribe
	 * to this node.
	 */
	public void sessionSubscribedToResource(String rootNodeId, String sessionId) {
		if (resourceInfoDao.getSessionsSubscribedToResource(rootNodeId).isEmpty()) {
			// first subscription
			Map<String, Object> options = CorePlugin.getInstance().getNodeService().getControllerInvocationOptions();
			for (ResourceSubscriptionListener listener : getResourceSubscriptionListeners(rootNodeId)) {
				try {
					listener.firstClientSubscribed(rootNodeId, options);
					if ((boolean) options.get(STOP_CONTROLLER_INVOCATION)) {
						break;
					}
				} catch (Exception e) {
					// there was an error loading the resource
					throw new RuntimeException(e);
				}
			}
		}
		resourceInfoDao.sessionSubscribedToResource(rootNodeId, sessionId);
		
		logger.debug("Subscribed session {} to root node {}", sessionId, rootNodeId);
	}
	
	/**
	 * Unsubscribes the client with this <code>sessionId</code> from the <code>rootNode</code>. 
	 * Notifies all registered subscription listeners if this is the last client to unsubscribe
	 * from this node.
	 */
	public void sessionUnsubscribedFromResource(String rootNodeId, String sessionId) {
		resourceInfoDao.sessionUnsubscribedFromResource(rootNodeId, sessionId);
		if (resourceInfoDao.getSessionsSubscribedToResource(rootNodeId).isEmpty()) {
			// last unsubscription
			Map<String, Object> options = CorePlugin.getInstance().getNodeService().getControllerInvocationOptions();
			for (ResourceSubscriptionListener listener : getResourceSubscriptionListeners(rootNodeId)) {
				listener.lastClientUnubscribed(rootNodeId, options);
				if ((boolean) options.get(STOP_CONTROLLER_INVOCATION)) {
					break;
				}
			}
		}
		
		logger.debug("Unsubscribed session {} from root node {}", sessionId, rootNodeId);
	}
	
	/**
	 * Delegates to {@link IResourceInfoDAO#getRawResourceData(String)} for the root node
	 * of the node with this <code>nodeId</code>.
	 */
	public Object getRawResourceData(String nodeId) {
		Node rootNode = CorePlugin.getInstance().getNodeService().getRootNode(new Node(nodeId));
		return resourceInfoDao.getRawResourceData(rootNode.getFullNodeId());
	}
	
	/**
	 * Delegates to {@link IResourceInfoDAO#setRawResourceData(String, Object)}.
	 */
	public void setRawResourceData(String rootNodeId, Object rawResourceData) {
		resourceInfoDao.setRawResourceData(rootNodeId, rawResourceData);
	}
	
	public long getUpdateRequestedTimestamp(String rootNodeId) {
		return resourceInfoDao.getUpdateRequestedTimestamp(rootNodeId);
	}
	
	/**
	 * Called by the registered {@link ResourceInfoSessionListener} when a new session
	 * is created.
	 */
	public void sessionCreated(String sessionId) {
		logger.debug("Session created {}", sessionId);
		
		resourceInfoDao.sessionCreated(sessionId);
		
		HttpServletRequest request = CorePlugin.getInstance().getRequestThreadLocal().get();
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		resourceInfoDao.updateSessionProperty(sessionId, "ip", ipAddress);
	}
	
	/**
	 * Called by the registered {@link ResourceInfoSessionListener} when a session
	 * is removed.
	 */
	public void sessionRemoved(String sessionId) {
		logger.debug("Session removed {}", sessionId);
		
		List<String> resources = resourceInfoDao.getResourcesSubscribedBySession(sessionId);
		for (int i = resources.size() - 1; i >= 0; i--) {
			sessionUnsubscribedFromResource(resources.get(i), sessionId);
		}
		
		resourceInfoDao.sessionRemoved(sessionId);
	}
	
	/**
	 * Delegates to {@link IResourceInfoDAO#getSubscribedSessions()}.
	 */
	public List<String> getSubscribedSessions() {
		return resourceInfoDao.getSubscribedSessions();
	}
	
	public Object getSessionProperty(String sessionId, String property) {
		return resourceInfoDao.getSessionProperty(sessionId, property);
	}
	
	public void updateSessionProperty(String sessionId, String property, Object value) {
		resourceInfoDao.updateSessionProperty(sessionId, property, value);
	}
	
	public List<String> getResourcesSubscribedBySession(String sessionId) {
		return resourceInfoDao.getResourcesSubscribedBySession(sessionId);
	}
	
	public List<String> getSessionsSubscribedToResource(String rootNodeId) {
		return resourceInfoDao.getSessionsSubscribedToResource(rootNodeId);
	}
	
	public List<String> getResources() {
		return resourceInfoDao.getResources();
	}
	
	public void addUpdate(String rootNodeId, Update update) {
		resourceInfoDao.addUpdate(rootNodeId, update);
	}
	
	public List<Update> getUpdates(String rootNodeId, long timestampOfLastRequest, long timestampOfThisRequest) {
		return resourceInfoDao.getUpdates(rootNodeId, timestampOfLastRequest, timestampOfThisRequest);
	}
	
	protected List<ResourceSubscriptionListener> getResourceSubscriptionListeners(String rootNodeId) {
		Node rootNode = new Node(rootNodeId);
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(rootNode.getType());
		if (descriptor != null) {
			return descriptor.getAdditiveControllers(RESOURCE_SUBSCRIPTION_LISTENER, rootNode);
		}
		return Collections.emptyList();
	}
	
}
