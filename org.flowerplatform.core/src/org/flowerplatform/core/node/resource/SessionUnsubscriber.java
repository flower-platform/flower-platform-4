package org.flowerplatform.core.node.resource;

import java.util.Collection;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import org.flowerplatform.core.CorePlugin;


/**
 * Scheduled task that periodically checks if the subscribed clients are still active.
 * If a client did not send a ping recently, the the client is forcefully unsubscribed
 * from all nodes.
 * 
 * @author Mariana Gheorghe
 */
public class SessionUnsubscriber extends TimerTask {

	public static String LAST_PING_TIMESTAMP = "lastPing";
	
	// get this from a property?
	public static long SESSION_UNSUBSCRIBER_DELAY = 600000; // ms
	
	@Override
	public void run() {
		long now = new Date().getTime();
		ResourceInfoService service = CorePlugin.getInstance().getResourceInfoService();
		Collection<String> subscribedSessions = null;
		for (String sessionId : subscribedSessions) {
			long lastPing = (long) service.getSessionProperty(sessionId, LAST_PING_TIMESTAMP);
			if (now - lastPing > SESSION_UNSUBSCRIBER_DELAY) {
				service.sessionRemoved(sessionId);
			}
		}
	}

	public void start() {
		new Timer().schedule(this, 0, SESSION_UNSUBSCRIBER_DELAY);
	}

}
