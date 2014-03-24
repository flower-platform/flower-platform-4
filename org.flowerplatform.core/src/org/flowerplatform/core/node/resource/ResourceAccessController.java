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
	
	public abstract void firstClientSubscribed(String rootNodeId, ServiceContext context) throws Exception;
	
	public abstract void lastClientUnubscribed(String rootNodeId, ServiceContext context);
	
	/**
	 * @author Cristina Constantinescu
	 */
	public abstract void save(String rootNodeId, ServiceContext context);
	
	/**
	 * @author Cristina Constantinescu
	 */
	public abstract boolean isDirty(String rootNodeId, ServiceContext context);
	
}
