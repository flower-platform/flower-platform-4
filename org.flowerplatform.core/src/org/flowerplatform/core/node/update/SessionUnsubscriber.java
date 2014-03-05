package org.flowerplatform.core.node.update;

import java.util.Collection;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

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
	
	private RootNodeInfoDAO rootNodeInfoDAO;
	
	public SessionUnsubscriber(RootNodeInfoDAO rootNodeInfoDAO) {
		this.rootNodeInfoDAO = rootNodeInfoDAO; 
	}
	
	@Override
	public void run() {
		long now = new Date().getTime();
		Collection<String> subscribedSessions = new CopyOnWriteArrayList<String>(rootNodeInfoDAO.getSubscribedSessions());
		for (String sessionId : subscribedSessions) {
			long lastPing = (long) rootNodeInfoDAO.getSessionProperty(sessionId, LAST_PING_TIMESTAMP);
			if (now - lastPing > SESSION_UNSUBSCRIBER_DELAY) {
				rootNodeInfoDAO.sessionRemoved(sessionId);
			}
		}
	}

	public void start() {
		new Timer().schedule(this, 0, SESSION_UNSUBSCRIBER_DELAY);
	}

}
