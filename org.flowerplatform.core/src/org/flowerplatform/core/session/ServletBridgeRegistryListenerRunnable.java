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

import javax.servlet.ServletContext;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.util.RunnableWithParam;
import org.flowerplatform.util.servlet.ServletUtils;

/**
 * Registers a {@link ComposedSessionListener} as a session listener.
 * 
 * @author Mariana Gheorghe
 * @author Cristina Constantinescu
 */
public class ServletBridgeRegistryListenerRunnable implements RunnableWithParam<Void, ServletContext> {

	@Override
	public Void run(ServletContext context) {
//		context.addListener(CorePlugin.getInstance().getComposedSessionListener());
				
		ServletUtils.addServletContextAdditionalAttributes(ServletUtils.PROP_USE_FILES_FROM_TEMPORARY_DIRECTORY, CorePlugin.getInstance()
				.getFlowerProperties().getProperty(ServletUtils.PROP_USE_FILES_FROM_TEMPORARY_DIRECTORY));
		
		return null;
	}

}
