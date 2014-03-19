package org.flowerplatform.core.node.resource;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.flowerplatform.core.CorePlugin;

/**
 * Scheduled task that periodically checks if the subscribed clients are still active.
 * If a resource was not pinged recently, then all the clients subscribed are forcefully 
 * unsubscribed from the resource; when all the clients are removed, the resource will
 * be unloaded.
 * 
 * @author Mariana Gheorghe
 */
public class ResourceUnsubscriber extends TimerTask {

	// get this from a property?
	public static long RESOURCE_UNSUBSCRIBER_DELAY = 600000; // ms
	
	@Override
	public void run() {
		long now = new Date().getTime();
		ResourceInfoService service = CorePlugin.getInstance().getResourceInfoService();
		List<String> rootNodeIds = service.getResources();
		for (String rootNodeId : rootNodeIds) {
			long lastPing = service.getUpdateRequestedTimestamp(rootNodeId);
			if (now - lastPing > RESOURCE_UNSUBSCRIBER_DELAY) {
				List<String> sessionIds = service.getSessionsSubscribedToResource(rootNodeId);
				for (int i = sessionIds.size() - 1; i >= 0; i--) {
					service.sessionUnsubscribedFromResource(rootNodeId, sessionIds.get(i));
				}
			}
		}
	}
	
	public void start() {
		new Timer().schedule(this, 0, RESOURCE_UNSUBSCRIBER_DELAY);
	}
	
}
