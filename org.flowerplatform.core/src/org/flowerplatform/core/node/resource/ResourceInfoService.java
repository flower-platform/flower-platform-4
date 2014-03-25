package org.flowerplatform.core.node.resource;

import static org.flowerplatform.core.NodePropertiesConstants.IS_DIRTY;
import static org.flowerplatform.core.ServiceContext.DONT_PROCESS_OTHER_CONTROLLERS;
import static org.flowerplatform.core.node.NodeService.NODE_IS_RESOURCE_NODE;
import static org.flowerplatform.core.node.resource.ResourceAccessController.RESOURCE_ACCESS_CONTROLLER;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.ServiceContext;
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
	}
	
	public Node subscribeToSelfOrParentResource(String nodeId, String sessionId, ServiceContext context) {
		logger.debug("Subscribe session {} to parent of {}", sessionId, nodeId);
		
		Node node = new Node(nodeId);
		Node subscribableNode = null;
		if (CoreUtils.isSubscribable(node.getOrPopulateProperties())) {
			subscribableNode = node;
		} else {
			Node resourceNode = CoreUtils.getRootNode(node);
			if (resourceNode != null && CoreUtils.isSubscribable(resourceNode.getOrPopulateProperties())) {
				subscribableNode = resourceNode;
			}
		}
		if (subscribableNode == null) {
			return null;
		}			
		sessionSubscribedToResource(subscribableNode.getFullNodeId(), sessionId, context);
		
		// populate resourceNode with isDirty			
		subscribableNode.getOrPopulateProperties().put(IS_DIRTY, isDirty(subscribableNode.getFullNodeId(), new ServiceContext()));
				
		return subscribableNode;
	}
	
	/**
	 * Subscribes the client with this <code>sessionId</code> to the <code>rootNode</code>. 
	 * Notifies all registered subscription listeners if this is the first client to subscribe
	 * to this node.
	 */
	public void sessionSubscribedToResource(String rootNodeId, String sessionId, ServiceContext context) {
		boolean firstSubscription = false;
		if (resourceInfoDao.getSessionsSubscribedToResource(rootNodeId).isEmpty()) {
			// first subscription
			firstSubscription = true;
			for (ResourceAccessController controller : getResourceAccessController(rootNodeId)) {
				try {
					controller.firstClientSubscribed(rootNodeId, context);
					if (context.getValue(DONT_PROCESS_OTHER_CONTROLLERS)) {
						break;
					}
				} catch (Exception e) {
					// there was an error loading the resource
					throw new RuntimeException(e);
				}
			}
		}
		resourceInfoDao.sessionSubscribedToResource(rootNodeId, sessionId);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Subscribed session {} to root node {}, first subscription {}", new Object[] { sessionId, rootNodeId, firstSubscription });
		}
	}
	
	/**
	 * Unsubscribes the client with this <code>sessionId</code> from the <code>rootNode</code>. 
	 * Notifies all registered subscription listeners if this is the last client to unsubscribe
	 * from this node.
	 */
	public void sessionUnsubscribedFromResource(String rootNodeId, String sessionId, ServiceContext context) {
		resourceInfoDao.sessionUnsubscribedFromResource(rootNodeId, sessionId);
		boolean lastUnsubscription = false;
		if (resourceInfoDao.getSessionsSubscribedToResource(rootNodeId).isEmpty()) {
			// last unsubscription
			lastUnsubscription = true;
			for (ResourceAccessController controller : getResourceAccessController(rootNodeId)) {
				controller.lastClientUnubscribed(rootNodeId, context);
				if (context.getValue(DONT_PROCESS_OTHER_CONTROLLERS)) {
					break;
				}
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Unsubscribed session {} from root node {}, last unsubscription {}", new Object[] { sessionId, rootNodeId, lastUnsubscription });
		}
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	public void save(String resourceNodeId, ServiceContext context) {
		for (ResourceAccessController controller : getResourceAccessController(resourceNodeId)) {
			controller.save(resourceNodeId, context);
			if (context.getValue(DONT_PROCESS_OTHER_CONTROLLERS)) {
				break;
			}
		}
		
		// update isDirty property
		CorePlugin.getInstance().getNodeService().setProperty(
				new Node(resourceNodeId), 
				IS_DIRTY, 
				CorePlugin.getInstance().getResourceInfoService().isDirty(resourceNodeId, new ServiceContext()), 
				new ServiceContext().add(NODE_IS_RESOURCE_NODE, true));
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	public boolean isDirty(String resourceNodeId, ServiceContext context) {
		boolean isDirty = false;
		for (ResourceAccessController controller : getResourceAccessController(resourceNodeId)) {
			isDirty = controller.isDirty(resourceNodeId, context);
			if (context.getValue(DONT_PROCESS_OTHER_CONTROLLERS)) {
				break;
			}
		}
		return isDirty;
	}
	
	public Object getRawResourceData(String resourceNodeId) {
		return resourceInfoDao.getRawResourceData(resourceNodeId);
	}
	
	public String getResourceCategory(String resourceNodeId) {
		return resourceInfoDao.getResourceCategory(resourceNodeId);
	}
	/**
	 * Delegates to {@link IResourceInfoDAO#setRawResourceData(String, Object)}.
	 */
	public void setRawResourceData(String resourceNodeId, Object rawResourceData, String resourceCategory) {
		resourceInfoDao.setRawResourceData(resourceNodeId, rawResourceData, resourceCategory);
	}
	
	public void unsetRawResourceData(String resourceNodeId) {
		resourceInfoDao.unsetRawResourceData(resourceNodeId);
	}
	
	public long getUpdateRequestedTimestamp(String resourceNodeId) {
		return resourceInfoDao.getUpdateRequestedTimestamp(resourceNodeId);
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
			sessionUnsubscribedFromResource(resources.get(i), sessionId, new ServiceContext());
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
	
	protected List<ResourceAccessController> getResourceAccessController(String rootNodeId) {
		Node rootNode = new Node(rootNodeId);
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(rootNode.getType());
		if (descriptor != null) {
			return descriptor.getAdditiveControllers(RESOURCE_ACCESS_CONTROLLER, rootNode);
		}
		return Collections.emptyList();
	}
	
}
