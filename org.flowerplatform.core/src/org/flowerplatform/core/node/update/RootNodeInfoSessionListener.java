package org.flowerplatform.core.node.update;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.flowerplatform.core.CorePlugin;

/**
 * @author Mariana Gheorghe
 */
public class RootNodeInfoSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		CorePlugin.getInstance().getNodeService().getRootNodeInfoDAO()
			.sessionCreated(event.getSession().getId());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		CorePlugin.getInstance().getNodeService().getRootNodeInfoDAO()
			.sessionRemoved(event.getSession().getId());
	}

}
