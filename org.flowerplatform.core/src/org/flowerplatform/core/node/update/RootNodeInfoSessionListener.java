package org.flowerplatform.core.node.update;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Notifies the {@link RootNodeInfoDAO} when a session is created or destroyed.
 * 
 * @author Mariana Gheorghe
 */
public class RootNodeInfoSessionListener implements HttpSessionListener {

	private RootNodeInfoDAO rootNodeInfoDAO;
	
	public RootNodeInfoSessionListener(RootNodeInfoDAO rootNodeInfoDAO) {
		this.rootNodeInfoDAO = rootNodeInfoDAO;
	}

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		rootNodeInfoDAO.sessionCreated(event.getSession().getId());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		rootNodeInfoDAO.sessionRemoved(event.getSession().getId());
	}

}
