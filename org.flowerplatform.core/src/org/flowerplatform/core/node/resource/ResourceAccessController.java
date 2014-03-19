package org.flowerplatform.core.node.resource;

import java.util.Map;

import org.flowerplatform.util.controller.AbstractController;

/**
 * Responsible with loading a resource on the first client subscribed and unloading the resource
 * on the last client unsubscribed.
 * 
 * @author Mariana Gheorghe
 */
public abstract class ResourceAccessController extends AbstractController {

	public static final String RESOURCE_ACCESS_CONTROLLER = "resourceAccessController";
	
	public abstract void firstClientSubscribed(String rootNodeId, Map<String, Object> options) throws Exception;
	
	public abstract void lastClientUnubscribed(String rootNodeId, Map<String, Object> options);
	
	public abstract void save(String rootNodeId, Map<String, Object> options);
	
	public abstract boolean isDirty(String rootNodeId, Map<String, Object> options);
	
}
