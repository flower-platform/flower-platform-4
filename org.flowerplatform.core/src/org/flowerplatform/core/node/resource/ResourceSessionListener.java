package org.flowerplatform.core.node.resource;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.flowerplatform.core.CorePlugin;


/**
 * Notifies the {@link ResourceService} when a session is created or destroyed.
 * 
 * @author Mariana Gheorghe
 */
public class ResourceSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		CorePlugin.getInstance().getResourceService().sessionCreated(event.getSession().getId());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		CorePlugin.getInstance().getResourceService().sessionRemoved(event.getSession().getId());
	}

}
