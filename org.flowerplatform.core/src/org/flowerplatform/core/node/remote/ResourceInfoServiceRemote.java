package org.flowerplatform.core.node.remote;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.RemoteMethodInvocationListener;
import org.flowerplatform.core.node.resource.ResourceInfoService;

/**
 * @see ResourceInfoService
 * 
 * @author Mariana Gheorghe
 */
public class ResourceInfoServiceRemote {

	public Node subscribeToSelfOrParentResource(String nodeId) {
		String sessionId = CorePlugin.getInstance().getRequestThreadLocal().get().getSession().getId();
		return CorePlugin.getInstance().getResourceInfoService()
				.subscribeToSelfOrParentResource(nodeId, sessionId);
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
	
	public void save(String resourceNodeId) {
		CorePlugin.getInstance().getResourceInfoService().save(resourceNodeId, CorePlugin.getInstance().getNodeService().getControllerInvocationOptions());
	}
	
}
