package org.flowerplatform.core.node.resource;

import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * Responsible with loading a resource on the first client subscribed and unloading the resource
 * on the last client unsubscribed.
 * 
 * @author Mariana Gheorghe
 */
public abstract class ResourceAccessController extends AbstractController {

	public static final String RESOURCE_ACCESS_CONTROLLER = "resourceAccessController";
	
	public abstract void firstClientSubscribed(String resourceNodeId, ServiceContext context) throws Exception;
	
	public abstract void lastClientUnubscribed(String resourceNodeId, ServiceContext context);
	
	/**
	 * @author Cristina Constantinescu
	 */
	public abstract void save(String resourceNodeId, ServiceContext context);
	
	/**
	 * @author Cristina Constantinescu
	 */
	public abstract boolean isDirty(String resourceNodeId, ServiceContext context);
	
}
