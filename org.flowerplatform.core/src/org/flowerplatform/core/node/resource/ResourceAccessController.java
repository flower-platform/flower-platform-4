package org.flowerplatform.core.node.resource;

import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * Responsible with working with raw resource data (load on the first client subscribed and unload
 * on the last client unsubscribed, save and reload).
 * 
 * @author Mariana Gheorghe
 */
public abstract class ResourceAccessController extends AbstractController {

	public abstract void firstClientSubscribed(String resourceNodeId, ServiceContext context) throws Exception;
	
	public abstract void lastClientUnubscribed(String resourceNodeId, ServiceContext context);
	
	/**
	 * @author Cristina Constantinescu
	 */
	public abstract void save(String resourceNodeId, ServiceContext context);
	
	public abstract void reload(String resourceNodeId, ServiceContext context) throws Exception;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public abstract boolean isDirty(String resourceNodeId, ServiceContext context);
	
}
