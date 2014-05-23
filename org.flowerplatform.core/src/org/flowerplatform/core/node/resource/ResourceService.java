package org.flowerplatform.core.node.resource;

import static org.flowerplatform.core.CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS;
import static org.flowerplatform.core.CoreConstants.EXECUTE_ONLY_FOR_UPDATER;
import static org.flowerplatform.core.CoreConstants.IS_DIRTY;
import static org.flowerplatform.core.CoreConstants.NODE_IS_RESOURCE_NODE;
import static org.flowerplatform.core.CoreConstants.RESOURCE_ACCESS_CONTROLLER;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.FlowerProperties;
import org.flowerplatform.core.RemoteMethodInvocationListener;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.update.Command;
import org.flowerplatform.core.node.update.remote.Update;
import org.flowerplatform.core.session.ISessionListener;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mariana Gheorghe
 */
public class ResourceService implements ISessionListener {
	
	private final static Logger logger = LoggerFactory.getLogger(ResourceService.class);
	
	private TypeDescriptorRegistry registry;
	
	private IResourceDAO resourceDao;
	
	public ResourceService(TypeDescriptorRegistry registry, IResourceDAO resourceDao) {
		this.registry = registry;
		this.resourceDao = resourceDao;	
		CorePlugin.getInstance().addSessionListener(this);
		
		CorePlugin.getInstance().getFlowerProperties().addProperty(new FlowerProperties.AddIntegerProperty(IResourceDAO.PROP_RESOURCE_UPDATES_MARGIN, IResourceDAO.PROP_DEFAULT_PROP_RESOURCE_UPDATES_MARGIN));
	}
	
	public Node subscribeToSelfOrParentResource(String nodeId, String sessionId, ServiceContext<ResourceService> context) {
		logger.debug("Subscribe session {} to parent of {}", sessionId, nodeId);
		
		Node node = new Node(nodeId);
		Node subscribableNode = null;
		if (CoreUtils.isSubscribable(node.getOrPopulateProperties())) {
			subscribableNode = node;
		} else {
			Node resourceNode = CoreUtils.getResourceNode(node);
			if (resourceNode != null && CoreUtils.isSubscribable(resourceNode.getOrPopulateProperties())) {
				subscribableNode = resourceNode;
			}
		}
		if (subscribableNode == null) {
			return null;
		}			
		sessionSubscribedToResource(subscribableNode.getFullNodeId(), sessionId, context);
		
		// populate resourceNode with isDirty			
		subscribableNode.getOrPopulateProperties().put(IS_DIRTY, isDirty(subscribableNode.getFullNodeId(), new ServiceContext<ResourceService>(context.getService())));
				
		return subscribableNode;
	}
	
	/**
	 * Subscribes the client with this <code>sessionId</code> to the <code>resourceNode</code>. 
	 * Notifies all registered subscription listeners if this is the first client to subscribe
	 * to this node.
	 */
	public void sessionSubscribedToResource(String resourceNodeId, String sessionId, ServiceContext<ResourceService> context) {
		boolean firstSubscription = false;
		if (resourceDao.getSessionsSubscribedToResource(resourceNodeId).isEmpty()) {
			// first subscription
			firstSubscription = true;
			for (ResourceAccessController controller : getResourceAccessControllers(resourceNodeId)) {
				try {
					controller.firstClientSubscribed(resourceNodeId, context);
					if (context.getBooleanValue(DONT_PROCESS_OTHER_CONTROLLERS)) {
						break;
					}
				} catch (Exception e) {
					// there was an error loading the resource
					throw new RuntimeException(e);
				}
			}
		}
		resourceDao.sessionSubscribedToResource(resourceNodeId, sessionId);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Subscribed session {} to root node {}, first subscription {}", new Object[] { sessionId, resourceNodeId, firstSubscription });
		}
	}
	
	/**
	 * Unsubscribes the client with this <code>sessionId</code> from the <code>resourceNode</code>. 
	 * Notifies all registered subscription listeners if this is the last client to unsubscribe
	 * from this node.
	 */
	public void sessionUnsubscribedFromResource(String resourceNodeId, String sessionId, ServiceContext<ResourceService> context) {
		resourceDao.sessionUnsubscribedFromResource(resourceNodeId, sessionId);
		boolean lastUnsubscription = false;
		if (resourceDao.getSessionsSubscribedToResource(resourceNodeId).isEmpty()) {
			// last unsubscription
			lastUnsubscription = true;
			for (ResourceAccessController controller : getResourceAccessControllers(resourceNodeId)) {
				controller.lastClientUnubscribed(resourceNodeId, context);
				if (context.getBooleanValue(DONT_PROCESS_OTHER_CONTROLLERS)) {
					break;
				}
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Unsubscribed session {} from root node {}, last unsubscription {}", new Object[] { sessionId, resourceNodeId, lastUnsubscription });
		}
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	public void save(String resourceNodeId, ServiceContext<ResourceService> context) {
		for (ResourceAccessController controller : getResourceAccessControllers(resourceNodeId)) {
			controller.save(resourceNodeId, context);
			if (context.getBooleanValue(DONT_PROCESS_OTHER_CONTROLLERS)) {
				break;
			}
		}
		
		// update isDirty property
		CorePlugin.getInstance().getNodeService().setProperty(
				new Node(resourceNodeId), 
				IS_DIRTY, 
				isDirty(resourceNodeId, new ServiceContext<ResourceService>(context.getService())), 
				new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()).add(NODE_IS_RESOURCE_NODE, true).add(EXECUTE_ONLY_FOR_UPDATER, true));
	}
	
	public void reload(String resourceNodeId, ServiceContext<ResourceService> context) {
		for (ResourceAccessController controller : getResourceAccessControllers(resourceNodeId)) {
			try {
				controller.reload(resourceNodeId, context);
				if (context.getBooleanValue(DONT_PROCESS_OTHER_CONTROLLERS)) {
					break;
				}
			} catch (Exception e) {
				// there was an error loading the resource
				// unsubscribe all other clients
				List<String> sessionIds = getSessionsSubscribedToResource(resourceNodeId);
				for (int i = sessionIds.size() - 1; i >= 0; i--) {
					sessionUnsubscribedFromResource(resourceNodeId, sessionIds.get(i), new ServiceContext<ResourceService>(context.getService()));
				}
				throw new RuntimeException(e);
			}
		}
		
		// update isDirty property
		CorePlugin.getInstance().getNodeService().setProperty(
				new Node(resourceNodeId), 
				CoreConstants.IS_DIRTY, 
				isDirty(resourceNodeId, new ServiceContext<ResourceService>(context.getService())), 
				new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()).add(NODE_IS_RESOURCE_NODE, true).add(EXECUTE_ONLY_FOR_UPDATER, true));
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	public boolean isDirty(String resourceNodeId, ServiceContext<ResourceService> context) {
		boolean isDirty = false;
		for (ResourceAccessController controller : getResourceAccessControllers(resourceNodeId)) {
			isDirty = controller.isDirty(resourceNodeId, context);
			if (context.getBooleanValue(DONT_PROCESS_OTHER_CONTROLLERS)) {
				break;
			}
		}
		return isDirty;
	}
	
	public Object getRawResourceData(String resourceNodeId) {
		return resourceDao.getRawResourceData(resourceNodeId);
	}
	
	public String getResourceCategory(String resourceNodeId) {
		return resourceDao.getResourceCategory(resourceNodeId);
	}
	/**
	 * Delegates to {@link IResourceDAO#setRawResourceData(String, Object)}.
	 */
	public void setRawResourceData(String resourceNodeId, Object rawResourceData, String resourceCategory) {
		resourceDao.setRawResourceData(resourceNodeId, rawResourceData, resourceCategory);
	}
	
	public void unsetRawResourceData(String resourceNodeId) {
		resourceDao.unsetRawResourceData(resourceNodeId);
	}
	
	public long getUpdateRequestedTimestamp(String resourceNodeId) {
		return resourceDao.getUpdateRequestedTimestamp(resourceNodeId);
	}
	
	/**
	 * Called by the registered {@link ResourceSessionListener} when a new session
	 * is created.
	 * 
	 * @author Mariana Gheorghe
	 * @author Cristina Constantinescu
	 */
	public void sessionCreated(String sessionId) {		
		HttpServletRequest request = CorePlugin.getInstance().getRequestThreadLocal().get();
		if (request == null) {
			// request doesn't come from FlowerMessageBrokerServlet, ignore it
			return;
		}
		logger.debug("Session created {}", sessionId);
		
		resourceDao.sessionCreated(sessionId);
		
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		resourceDao.updateSessionProperty(sessionId, "ip", ipAddress);
	}
	
	/**
	 * Called by the registered {@link ResourceSessionListener} when a session
	 * is removed.
	 */
	public void sessionRemoved(String sessionId) {
		logger.debug("Session removed {}", sessionId);
		
		List<String> resources = resourceDao.getResourcesSubscribedBySession(sessionId);
		for (int i = resources.size() - 1; i >= 0; i--) {
			sessionUnsubscribedFromResource(resources.get(i), sessionId, new ServiceContext<ResourceService>(this));
		}
		
		resourceDao.sessionRemoved(sessionId);
	}
	
	/**
	 * Delegates to {@link IResourceDAO#getSubscribedSessions()}.
	 */
	public List<String> getSubscribedSessions() {
		return resourceDao.getSubscribedSessions();
	}
	
	public Object getSessionProperty(String sessionId, String property) {
		return resourceDao.getSessionProperty(sessionId, property);
	}
	
	public void updateSessionProperty(String sessionId, String property, Object value) {
		resourceDao.updateSessionProperty(sessionId, property, value);
	}
	
	public List<String> getResourcesSubscribedBySession(String sessionId) {
		return resourceDao.getResourcesSubscribedBySession(sessionId);
	}
	
	public List<String> getSessionsSubscribedToResource(String resourceNodeId) {
		return resourceDao.getSessionsSubscribedToResource(resourceNodeId);
	}
	
	public List<String> getResources() {
		return resourceDao.getResources();
	}
	
	public void addUpdate(String resourceNodeId, Update update) {
		if (logger.isDebugEnabled()) {
			logger.debug("For resource = {} adding update = {}", resourceNodeId, update);
		}
		resourceDao.addUpdate(resourceNodeId, update);
	}
	
	public List<Update> getUpdates(String resourceNodeId, long timestampOfLastRequest, long timestampOfThisRequest) {
		return resourceDao.getUpdates(resourceNodeId, timestampOfLastRequest, timestampOfThisRequest);
	}

	/**
	 * @author Claudiu Matei 
	 */
	public void addCommand(String resourceNodeId, Command command) {
		if (logger.isDebugEnabled()) {
			logger.debug("For resource = {} adding command = {}", resourceNodeId, command);
		}
		resourceDao.addCommand(resourceNodeId, command);
		Node commandStackNode = new Node(CoreConstants.COMMAND_STACK_TYPE, "self", RemoteMethodInvocationListener.escapeFullNodeId(resourceNodeId), null);
		Node childNode = RemoteMethodInvocationListener.createCommandNode(command);
		CorePlugin.getInstance().getNodeService().addChild(commandStackNode, childNode, new ServiceContext<NodeService>());
	}

	/**
	 * @author Claudiu Matei 
	 */
	public List<Command> getCommands(String resourceNodeId) {
		return resourceDao.getCommands(resourceNodeId);
	}
	
	protected List<ResourceAccessController> getResourceAccessControllers(String resourceNodeId) {
		Node resourceNode = new Node(resourceNodeId);
		TypeDescriptor descriptor = registry.getExpectedTypeDescriptor(resourceNode.getType());
		if (descriptor != null) {
			return descriptor.getAdditiveControllers(RESOURCE_ACCESS_CONTROLLER, resourceNode);
		}
		return Collections.emptyList();
	}

}
