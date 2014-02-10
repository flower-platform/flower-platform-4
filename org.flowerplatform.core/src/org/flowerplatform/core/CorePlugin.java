/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.core;

import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.core.file.PlainFileAccessController;
import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.controller.RemoveNodeController;
import org.flowerplatform.core.node.controller.update.InMemoryUpdaterProvider;
import org.flowerplatform.core.node.controller.update.UpdaterAddNodeController;
import org.flowerplatform.core.node.controller.update.UpdaterPropertySetterController;
import org.flowerplatform.core.node.controller.update.UpdaterRemoveNodeController;
import org.flowerplatform.core.node.remote.NodeService;
import org.flowerplatform.core.node.update.remote.UpdaterService;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Cristian Spiescu
 */
public class CorePlugin extends AbstractFlowerJavaPlugin {

	protected static CorePlugin INSTANCE;
	
	public static CorePlugin getInstance() {
		return INSTANCE;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
				
		getServiceRegistry().registerService("nodeService", new NodeService(nodeTypeDescriptorRegistry));
		getServiceRegistry().registerService("updaterService", new UpdaterService(new InMemoryUpdaterProvider()));
		
		TypeDescriptor updaterDescriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor("category.all");
		updaterDescriptor.addAdditiveController(AddNodeController.ADD_NODE_CONTROLLER, new UpdaterAddNodeController());
		updaterDescriptor.addAdditiveController(RemoveNodeController.REMOVE_NODE_CONTROLLER, new UpdaterRemoveNodeController());
		updaterDescriptor.addAdditiveController(PropertySetter.PROPERTY_SETTER, new UpdaterPropertySetterController());
		
		setFileAccessController(new PlainFileAccessController());
	}

	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}

	private ServiceRegistry serviceRegistry = new ServiceRegistry();

	public ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}
	
	private TypeDescriptorRegistry nodeTypeDescriptorRegistry = new TypeDescriptorRegistry();

	public TypeDescriptorRegistry getNodeTypeDescriptorRegistry() {
		return nodeTypeDescriptorRegistry;
	}

	protected IFileAccessController fileAccessController;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public IFileAccessController getFileAccessController() {
		return fileAccessController;
	}

	public void setFileAccessController(IFileAccessController fileAccessController) {
		this.fileAccessController = fileAccessController;
	}
	
	@Override
	public void registerMessageBundle() throws Exception {
		// no messages yet
	}

}
