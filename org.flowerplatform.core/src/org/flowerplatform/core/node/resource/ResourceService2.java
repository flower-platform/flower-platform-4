package org.flowerplatform.core.node.resource;

import static org.flowerplatform.core.CoreConstants.EXECUTE_ONLY_FOR_UPDATER;
import static org.flowerplatform.core.CoreConstants.IS_DIRTY;
import static org.flowerplatform.core.CoreConstants.NODE_IS_RESOURCE_NODE;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
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
public abstract class ResourceService2 {

	private final static Logger logger = LoggerFactory.getLogger(ResourceService2.class);
	
	private Map<String, ResourceHandler> resourceHandlers = new HashMap<String, ResourceHandler>();
	
	private Map<String, IResourceSetProvider> resourceSetProviders = new HashMap<String, IResourceSetProvider>();
	
	public void addResourceHandler(String scheme, ResourceHandler resourceHandler) {
		resourceHandlers.put(scheme, resourceHandler);
	}
	
	public ResourceHandler getResourceHandler(String scheme) {
		return resourceHandlers.get(scheme);
	}
	
	/**
	 * Delegate to a {@link ResourceHandler} based on the scheme.
	 */
	public Node getNode(String nodeUriAsString) {
		logger.debug("Get node for URI: ", nodeUriAsString);
	
		URI nodeUri = Utils.getUri(nodeUriAsString);
		String scheme = nodeUri.getScheme();
		
		ResourceHandler resourceHandler = getResourceHandler(scheme);
		Node node;
		if (resourceHandler == null) {
			node = new Node(nodeUriAsString);
			node.setType(scheme);
		} else {
			node = getResourceHandler(scheme).getNode(nodeUri);
		}
		node.getOrPopulateProperties();
		
		return node;
	}
	
	public abstract Object getResourceInfo(URI resourceUri);
	
	/**
	 * Subscribes to the parent resource of the <code>node</code>.
	 * 
	 * @return a pair containing the resource URI and the resource set
	 * where the resource belongs
	 */
	public SubscriptionInfo subscribeToParentResource(String sessionId, String nodeUri, ServiceContext<ResourceService2> context) {
		logger.debug("Subscribe session {} to parent of {}", sessionId, nodeUri);
		
		// get resource uri from node uri by stripping the fragment
		URI resourceUri = CoreUtils.getResourceUri(nodeUri);
		
		// not a resource => return node
		if (!resourceHandlers.containsKey(resourceUri.getScheme())) {
			return new SubscriptionInfo(getNode(nodeUri));
		}
		
		// subscribe
		sessionSubscribedToResource(sessionId, resourceUri);
		CorePlugin.getInstance().getSessionService().sessionSubscribedToResource(sessionId, Utils.getString(resourceUri), null);
		
		// get resource node
		Node resourceNode = getNode(Utils.getString(resourceUri));
		
		// get resource set
		IResourceSetProvider resourceSetProvider = resourceSetProviders.get(resourceNode.getType());
		String resourceSet = resourceSetProvider != null ? resourceSetProvider.getResourceSet(resourceUri) :
			Utils.getString(resourceUri);
		CorePlugin.getInstance().getResourceSetService().addToResourceSet(resourceSet, resourceUri);
		
		return new SubscriptionInfo(getNode(nodeUri), resourceNode, resourceSet);
	}
	
	/**
	 * Subscribes the client with this <code>sessionId</code> to the <code>resourceUri</code>.
	 * Load the resource on first subscription.
	 * 
	 * <p>
	 * Paired with {@link SessionService#sessionSubscribedToResource(String, String, ServiceContext)}.
	 */
	public void sessionSubscribedToResource(String sessionId, URI resourceUri) {
		boolean firstSubscription = false;
		if (getSessionsSubscribedToResource(Utils.getString(resourceUri)).isEmpty()) {
			// first subscription
			String scheme = resourceUri.getScheme();
			ResourceHandler resourceHandler = getResourceHandler(scheme);
			resourceHandler.load(resourceUri);
		}
		doSessionSubscribedToResource(sessionId, resourceUri);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Subscribed session {} to resource {}, first subscription {}", new Object[] { sessionId, resourceUri, firstSubscription });
		}
	}
	
	protected abstract void doSessionSubscribedToResource(String sessionId, URI resourceUri);
	
	/**
	 * Unsubscribes the client with this <code>sessionId</code> from the <code>resourceUri</code>.
	 * Unload the resource on last unsubscription.
	 * 
	 * <p>
	 * Paired with {@link SessionService#sessionUnsubscribedFromResource(String, String, ServiceContext)}.
	 */
	public void sessionUnsubscribedFromResource(String sessionId, URI resourceUri) {
		doSessionUnsubscribedFromResource(sessionId, resourceUri);
		boolean lastUnsubscription = false;
		if (getSessionsSubscribedToResource(Utils.getString(resourceUri)).isEmpty()) {
			// last unsubscription
			lastUnsubscription = true;
			String scheme = resourceUri.getScheme();
			ResourceHandler resourceHandler = getResourceHandler(scheme);
			resourceHandler.unload(resourceUri);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Unsubscribed session {} from  resource {}, last unsubscription {}", new Object[] { sessionId, resourceUri, lastUnsubscription });
		}
	}
	
	protected abstract void doSessionUnsubscribedFromResource(String sessionId, URI resourceUri);
	
	public void save(String resourceUriAsString, ServiceContext<ResourceService2> context) {
		logger.debug("Save resource {}", resourceUriAsString);
		
		URI resourceUri = CoreUtils.getResourceUri(resourceUriAsString);
		String scheme = resourceUri.getScheme();
		ResourceHandler resourceHandler = getResourceHandler(scheme);
		resourceHandler.save(resourceUri);
		
		// update isDirty property
		Node resourceNode = getNode(resourceUriAsString);
		CorePlugin.getInstance().getNodeService().setProperty(
				resourceNode, 
				IS_DIRTY, 
				resourceHandler.isDirty(resourceUri),
				new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()).add(NODE_IS_RESOURCE_NODE, true).add(EXECUTE_ONLY_FOR_UPDATER, true));
	}

	public void reload(String resourceUriAsString, ServiceContext<ResourceService2> context) {
		logger.debug("Reload resource {}", resourceUriAsString);
		
		URI resourceUri = CoreUtils.getResourceUri(resourceUriAsString);
		String scheme = resourceUri.getScheme();
		ResourceHandler resourceHandler = getResourceHandler(scheme);
		resourceHandler.reload(resourceUri);
		
		// update isDirty property
		Node resourceNode = getNode(resourceUriAsString);
		CorePlugin.getInstance().getNodeService().setProperty(
				resourceNode, 
				IS_DIRTY, 
				resourceHandler.isDirty(resourceUri),
				new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()).add(NODE_IS_RESOURCE_NODE, true).add(EXECUTE_ONLY_FOR_UPDATER, true));
	}
	
	public boolean isDirty(String fullNodeId, ServiceContext<ResourceService2> serviceContext) {
		ResourceHandler resourceHandler = getResourceHandler(Utils.getUri(fullNodeId).getScheme());
		return resourceHandler == null ? false : resourceHandler.isDirty(Utils.getUri(fullNodeId));
	}

	public abstract List<String> getResources();
	
	public abstract List<String> getSessionsSubscribedToResource(String resourceNodeId);

	public abstract long getUpdateRequestedTimestamp(String resourceNodeId);
	
	public abstract void setUpdateRequestedTimestamp(String resourceUri, long timestamp);
	
}
