package org.flowerplatform.core.node.resource;

import java.util.Map;

import org.flowerplatform.util.controller.AbstractController;

/**
 * Responsible with loading a resource on the first client subscribed and unloading the resource
 * on the last client unsubscribed.
 * 
 * @author Mariana Gheorghe
 */
public abstract class ResourceSubscriptionListener extends AbstractController {

	public static final String RESOURCE_SUBSCRIPTION_LISTENER = "resourceSubscriptionListener";
	
	public abstract void firstClientSubscribed(String rootNodeId, Map<String, Object> options) throws Exception;
	
	public abstract void lastClientUnubscribed(String rootNodeId, Map<String, Object> options);
	
}
