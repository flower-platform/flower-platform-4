/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flex_client.server.blazeds;

import javax.servlet.ServletContext;

import org.flowerplatform.util.RunnableWithParam;

import flex.messaging.HttpFlexSession;

/**
 * Adds {@link HttpFlexSession} as a listener. Needed to unregister a flex client when its session expires.
 * 
 * @author Mariana Gheorghe
 */
public class AddHttpFlexSessionListenerRunnable implements RunnableWithParam<Void, ServletContext> {

	@Override
	public Void run(ServletContext context) {
		context.addListener(new HttpFlexSession());
		return null;
	}

}