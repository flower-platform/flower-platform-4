package org.flowerplatform.core.node.resource;

import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * Responsible with working with raw resource data (load on the first client subscribed and unload
 * on the last client unsubscribed, save and reload).
 * 
 * @author Mariana Gheorghe
 */
public abstract class ResourceAccessController extends AbstractController {

	public abstract void firstClientSubscribed(String resourceNodeId, ServiceContext<ResourceService> context) throws Exception;
	
	public abstract void lastClientUnubscribed(String resourceNodeId, ServiceContext<ResourceService> context);
	
	/**
	 * @author Cristina Constantinescu
	 */
	public abstract void save(String resourceNodeId, ServiceContext<ResourceService> context);
	
	public abstract void reload(String resourceNodeId, ServiceContext<ResourceService> context) throws Exception;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public abstract boolean isDirty(String resourceNodeId, ServiceContext<ResourceService> context);
	
}
