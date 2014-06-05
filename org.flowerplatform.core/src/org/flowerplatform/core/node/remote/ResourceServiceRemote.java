package org.flowerplatform.core.node.remote;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.RemoteMethodInvocationListener;
import org.flowerplatform.core.node.resource.ResourceService2;

/**
 * @see ResourceService
 * 
 * @author Mariana Gheorghe
 */
public class ResourceServiceRemote {

	public SubscriptionInfo subscribeToParentResource(String nodeUri) {
		String sessionId = CorePlugin.getInstance().getRequestThreadLocal().get().getSession().getId();
		return CorePlugin.getInstance().getResourceService()
				.subscribeToParentResource(sessionId, nodeUri, new ServiceContext<ResourceService2>(CorePlugin.getInstance().getResourceService()));
	}
	
	/**
	 * This is simply a dummy method that is invoked periodically
	 * by the client. Node updates will be sent automatically
	 * via the {@link RemoteMethodInvocationListener} every time
	 * it is invoked.
	 */
	public void ping() {
		// nothing to do
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	public void save(String resourceNodeId) {
		CorePlugin.getInstance().getResourceService().save(resourceNodeId, new ServiceContext<ResourceService2>(CorePlugin.getInstance().getResourceService()));
	}
	
	public void reload(String resourceNodeId) {
		CorePlugin.getInstance().getResourceService().reload(resourceNodeId, new ServiceContext<ResourceService2>(CorePlugin.getInstance().getResourceService()));
	}
	
}
