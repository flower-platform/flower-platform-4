package org.flowerplatform.core.node.resource;

import static org.flowerplatform.core.CoreConstants.EXECUTE_ONLY_FOR_UPDATER;
import static org.flowerplatform.core.CoreConstants.IS_DIRTY;
import static org.flowerplatform.core.CoreConstants.NODE_IS_RESOURCE_NODE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.remote.SubscriptionInfo;
import org.flowerplatform.core.session.SessionService;
import org.flowerplatform.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages resources that are requested by the clients.
 * 
 * Note: there is no unsubscribe method, because if the user has two open
 * applications with the same session ID (e.g. the client app is open in two
 * browser tabs) and the same resource open in both applications, we do not
 * want to unsubscribe the client.
 * 
 * @author Mariana Gheorghe
 */
public abstract class ResourceService {

	private final static Logger logger = LoggerFactory.getLogger(ResourceService.class);
	
	private Map<String, ResourceHandler> resourceHandlers = new HashMap<String, ResourceHandler>();
	
	public void addResourceHandler(String scheme, ResourceHandler resourceHandler) {
		resourceHandlers.put(scheme, resourceHandler);
	}
	
	public ResourceHandler getResourceHandler(String scheme) {
		return resourceHandlers.get(scheme);
	}
	
	/**
	 * Delegate to a {@link ResourceHandler} based on the scheme.
	 */
	public Node getNode(String nodeUri) {
		logger.debug("Get node for URI: {}", nodeUri);
	
		String scheme = Utils.getScheme(nodeUri);
		
		ResourceHandler resourceHandler = getResourceHandler(scheme);
		Node node;
		if (resourceHandler == null) {
			node = new Node(nodeUri, scheme);
		} else {
			node = getResourceHandler(scheme).getNode(nodeUri);
		}
		node.getOrPopulateProperties();
		
		return node;
	}
	
	public abstract Object getResourceInfo(String resourceUri);
	
	/**
	 * Subscribes to the parent resource of the <code>node</code>.
	 * 
	 * @return a pair containing the root node, resource node and 
	 * resource set
	 */
	public SubscriptionInfo subscribeToParentResource(String sessionId, String nodeUri, ServiceContext<ResourceService> context) {
		logger.debug("Subscribe session {} to parent of {}", sessionId, nodeUri);
		
		String resourceUri = getResourceUri(nodeUri);
		if (resourceUri == null) {
			return new SubscriptionInfo(getNode(nodeUri));
		}
		
		// subscribe
		sessionSubscribedToResource(sessionId, resourceUri, context);
		CorePlugin.getInstance().getSessionService().sessionSubscribedToResource(sessionId, resourceUri, null);
		
		// get resource node
		Node resourceNode = getNode(resourceUri);
		
		// add to resource set
		String resourceSet = CorePlugin.getInstance().getResourceSetService().addToResourceSet(resourceUri);
		
		return new SubscriptionInfo(getNode(nodeUri), resourceNode, resourceSet);
	}
	
	/**
	 * Delegate to the registered {@link ResourceHandler}.
	 */
	public String getResourceUri(String nodeUri) {
		ResourceHandler resourceHandler = getResourceHandler(Utils.getScheme(nodeUri));
		// not in a resource => return null
		if (resourceHandler == null) {
			return null;
		}
		return resourceHandler.getResourceUri(nodeUri);
	}
	
	public Node getResourceNode(String nodeUri) {
		ResourceHandler resourceHandler = getResourceHandler(Utils.getScheme(nodeUri));
		// not in a resource => return null
		if (resourceHandler == null) {
			return null;
		}
		return resourceHandler.getNode(resourceHandler.getResourceUri(nodeUri));
	}
	
	/**
	 * Subscribes the client with this <code>sessionId</code> to the <code>resourceUri</code>.
	 * Load the resource on first subscription.
	 * 
	 * <p>
	 * Paired with {@link SessionService#sessionSubscribedToResource(String, String, ServiceContext)}.
	 */
	public void sessionSubscribedToResource(String sessionId, String resourceUri, ServiceContext<ResourceService> context) {
		boolean firstSubscription = false;
		if (getSessionsSubscribedToResource(resourceUri).isEmpty()) {
			// first subscription
			String scheme = Utils.getScheme(resourceUri);
			ResourceHandler resourceHandler = getResourceHandler(scheme);
			resourceHandler.load(resourceUri);
		}
		doSessionSubscribedToResource(sessionId, resourceUri);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Subscribed session {} to resource {}, first subscription {}", new Object[] { sessionId, resourceUri, firstSubscription });
		}
	}
	
	protected abstract void doSessionSubscribedToResource(String sessionId, String resourceUri);
	
	/**
	 * Unsubscribes the client with this <code>sessionId</code> from the <code>resourceUri</code>.
	 * Unload the resource on last unsubscription.
	 * 
	 * <p>
	 * Paired with {@link SessionService#sessionUnsubscribedFromResource(String, String, ServiceContext)}.
	 */
	public void sessionUnsubscribedFromResource(String sessionId, String resourceUri, ServiceContext<ResourceService> context) {
		doSessionUnsubscribedFromResource(sessionId, resourceUri);
		boolean lastUnsubscription = false;
		if (getSessionsSubscribedToResource(resourceUri).isEmpty()) {
			// last unsubscription
			lastUnsubscription = true;
			String scheme = Utils.getScheme(resourceUri);
			ResourceHandler resourceHandler = getResourceHandler(scheme);
			resourceHandler.unload(resourceUri);
			
			// remove from resource set as well
			CorePlugin.getInstance().getResourceSetService().removeFromResourceSet(resourceUri);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Unsubscribed session {} from  resource {}, last unsubscription {}", new Object[] { sessionId, resourceUri, lastUnsubscription });
		}
	}
	
	protected abstract void doSessionUnsubscribedFromResource(String sessionId, String resourceUri);
	
	public void save(String resourceUri, ServiceContext<ResourceService> context) {
		logger.debug("Save resource {}", resourceUri);
		
		String scheme = Utils.getScheme(resourceUri);
		ResourceHandler resourceHandler = getResourceHandler(scheme);
		resourceHandler.save(resourceUri);
		
		// update isDirty property
		Node resourceNode = getNode(resourceUri);
		CorePlugin.getInstance().getNodeService().setProperty(
				resourceNode, 
				IS_DIRTY, 
				resourceHandler.isDirty(resourceUri),
				new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()).add(NODE_IS_RESOURCE_NODE, true).add(EXECUTE_ONLY_FOR_UPDATER, true));
	}

	public void reload(String resourceUri, ServiceContext<ResourceService> context) {
		logger.debug("Reload resource {}", resourceUri);
		
		String scheme = Utils.getScheme(resourceUri);
		ResourceHandler resourceHandler = getResourceHandler(scheme);
		resourceHandler.reload(resourceUri);
		
		// update isDirty property
		Node resourceNode = getNode(resourceUri);
		CorePlugin.getInstance().getNodeService().setProperty(
				resourceNode, 
				IS_DIRTY, 
				resourceHandler.isDirty(resourceUri),
				new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()).add(NODE_IS_RESOURCE_NODE, true).add(EXECUTE_ONLY_FOR_UPDATER, true));
	}
	
	public boolean isDirty(String nodeUri, ServiceContext<ResourceService> serviceContext) {
		ResourceHandler resourceHandler = getResourceHandler(Utils.getScheme(nodeUri));
		return resourceHandler == null ? false : resourceHandler.isDirty(nodeUri);
	}

	public abstract List<String> getResources();
	
	public abstract List<String> getSessionsSubscribedToResource(String resourceNodeId);

	public abstract long getUpdateRequestedTimestamp(String resourceNodeId);
	
	public abstract void setUpdateRequestedTimestamp(String resourceUri, long timestamp);
	
}
