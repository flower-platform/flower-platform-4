package org.flowerplatform.core.node.resource;

import static org.flowerplatform.core.CoreConstants.EXECUTE_ONLY_FOR_UPDATER;
import static org.flowerplatform.core.CoreConstants.IS_DIRTY;
import static org.flowerplatform.core.CoreConstants.NODE_IS_RESOURCE_NODE;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.remote.SubscriptionInfo;
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
		if (resourceHandler == null) {
			Node node = new Node(nodeUriAsString);
			node.setType(scheme);
			return node;
		}
		return getResourceHandler(scheme).getNode(nodeUri);
	}
	
	public abstract Object getResourceInfo(URI resourceUri);
	
	/**
	 * Subscribes to the parent resource of the <code>node</code>.
	 * 
	 * @return a pair containing the resource URI and the resource set
	 * where the resource belongs
	 */
	public SubscriptionInfo subscribeToParentResource(String sessionId, String nodeUri, ServiceContext<ResourceService2> context) {
		// get resource uri from node uri by stripping the fragment
		URI resourceUri = Utils.getUriWithoutFragment(nodeUri);
		
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
	
	protected abstract void sessionSubscribedToResource(String sessionId, URI resourceUri);
	
	public void save(String resourceUriAsString, ServiceContext<ResourceService2> context) {
		URI resourceUri = Utils.getUriWithoutFragment(resourceUriAsString);
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
		URI resourceUri = Utils.getUriWithoutFragment(resourceUriAsString);
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

	public abstract long getUpdateRequestedTimestamp(String resourceNodeId);
	
	public abstract void setUpdateRequestedTimestamp(String resourceUri, long timestamp);

	public abstract List<String> getSessionsSubscribedToResource(String resourceNodeId);
	
}
