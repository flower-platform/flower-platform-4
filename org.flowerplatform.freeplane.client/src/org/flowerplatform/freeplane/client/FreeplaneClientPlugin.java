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
package org.flowerplatform.freeplane.client;

import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.freeplane.core.util.LogUtils;
import org.freeplane.features.mode.Controller;
import org.freeplane.main.osgi.IControllerExtensionProvider;
import org.osgi.framework.BundleContext;

/**
 * @author Valentina Bojan
 */
public class FreeplaneClientPlugin extends AbstractFlowerJavaPlugin {

	protected static FreeplaneClientPlugin instance;
	
	public static FreeplaneClientPlugin getInstance() {
		return instance;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;
		
		context.registerService(IControllerExtensionProvider.class.getName(), new IControllerExtensionProvider() {
			public void installExtension(Controller controller) {
				FlowerPlatformManager.install();
				LogUtils.info("Workspace controller installed.");
			}
		}, null);
	}
	
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		instance = null;
	}
	
	@Override
	public void registerMessageBundle() throws Exception {
		// nothing to do yet
	}
}
