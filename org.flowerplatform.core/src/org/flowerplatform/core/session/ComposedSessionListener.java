/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
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
	
	/**
	 * @author see class
	 */
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