package org.flowerplatform.core.session;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


/**
 * Notifies {@link ISessionListener}s when a session is created or destroyed.
 * 
 * @author Mariana Gheorghe
 * @author Cristina Constantinescu
 */
public class ComposedSessionListener implements HttpSessionListener {
	
	private Set<ISessionListener> sessionListeners = new HashSet<>();
	
	public void add(ISessionListener listener) {
		sessionListeners.add(listener);
	}
	
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		for (ISessionListener listener : sessionListeners) {
			listener.sessionCreated(event.getSession().getId());
		}
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		for (ISessionListener listener : sessionListeners) {
			listener.sessionRemoved(event.getSession().getId());
		}
	}

}
