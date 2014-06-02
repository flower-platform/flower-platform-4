package org.flowerplatform.core.node.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.Pair;
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
	
	/**
	 * Delegate to a {@link ResourceHandler} based on the scheme.
	 */
	public Node getNode(URI nodeUri) {
		logger.debug("Get node for URI: ", nodeUri);
	
		String scheme = nodeUri.getScheme();
		ResourceHandler resourceHandler = resourceHandlers.get(scheme);
		if (resourceHandler == null) {
			throw new RuntimeException("No resource handler registered for the scheme: " + scheme);
		}
		return resourceHandler.getNode(nodeUri);
	}
	
	public abstract Object getResourceInfo(URI resourceUri);
	
	/**
	 * Subscribes to the parent resource of the <code>node</code>.
	 * 
	 * @return a pair containing the resource URI and the resource set
	 * where the resource belongs
	 */
	public Pair<String, String> subscribeToParentResource(String sessionId, String nodeUri) {
		// get resource uri from node uri by stripping the fragment
		URI resourceUri = getUriWithoutFragment(nodeUri);
		
		// subscribe
		doSessionSubscribedToResource(sessionId, resourceUri);
		CorePlugin.getInstance().getSessionService().sessionSubscribedToResource(sessionId, resourceUri);
		
		// get resource node
		Node resourceNode = getNode(resourceUri);
		
		// get resource set
		IResourceSetProvider resourceSetProvider = resourceSetProviders.get(resourceNode.getType());
		String resourceSet = resourceSetProvider != null ? resourceSetProvider.getResourceSet(resourceUri) :
			resourceUri.toString();
		CorePlugin.getInstance().getResourceSetService().addToResourceSet(resourceSet, resourceUri);
		
		return new Pair<String, String>(resourceUri.toString(), resourceSet);
	}
	
	protected abstract void doSessionSubscribedToResource(String sessionId, URI resourceUri);
	
	public void save(String resourceUri) {
		
	}
	
	private URI getUri(String uriAsString) {
		try {
			return new URI(uriAsString);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	private URI getUriWithoutFragment(String uriAsString) {
		URI uri = getUri(uriAsString);
		try {
			return new URI(uri.getScheme(), uri.getSchemeSpecificPart(), null);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
}
