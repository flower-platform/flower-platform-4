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

import static org.flowerplatform.core.node.controller.AddNodeController.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.node.controller.ChildrenProvider.CHILDREN_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertiesProvider.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertySetter.PROPERTY_SETTER;
import static org.flowerplatform.core.node.controller.RemoveNodeController.REMOVE_NODE_CONTROLLER;
import static org.flowerplatform.core.node.remote.PropertyDescriptor.PROPERTY_DESCRIPTOR;

import org.flowerplatform.core.file.FileAddNodeControlller;
import org.flowerplatform.core.file.FileChildrenProvider;
import org.flowerplatform.core.file.FilePropertiesProvider;
import org.flowerplatform.core.file.FilePropertySetter;
import org.flowerplatform.core.file.FileRemoveNodeController;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.core.file.PlainFileAccessController;
import org.flowerplatform.core.fileSystem.FileSystemPropertiesProvider;
import org.flowerplatform.core.node.remote.NodeService;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.util.type_descriptor.TypeDescriptor;
import org.flowerplatform.util.type_descriptor.TypeDescriptorRegistry;
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
				
		setFileAccessController(new PlainFileAccessController());
		
		TypeDescriptor fileSystemNodeTypeDescriptor = getNodeTypeDescriptorRegistry().getOrCreateNodeTypeDescriptor("fileSystem");
		fileSystemNodeTypeDescriptor.addControllerToList(CHILDREN_PROVIDER,	new FileChildrenProvider());
		fileSystemNodeTypeDescriptor.addControllerToList(PROPERTIES_PROVIDER, new FileSystemPropertiesProvider());
		fileSystemNodeTypeDescriptor.addControllerToList(REMOVE_NODE_CONTROLLER, new FileRemoveNodeController());
		fileSystemNodeTypeDescriptor.addControllerToList(ADD_NODE_CONTROLLER, new FileAddNodeControlller());

		TypeDescriptor fileNodeTypeDescriptor = getNodeTypeDescriptorRegistry().getOrCreateNodeTypeDescriptor("fileNode");
		fileNodeTypeDescriptor.addControllerToList(CHILDREN_PROVIDER, new FileChildrenProvider());
		fileNodeTypeDescriptor.addControllerToList(PROPERTIES_PROVIDER, new FilePropertiesProvider());
		fileNodeTypeDescriptor.addControllerToList(ADD_NODE_CONTROLLER, new FileAddNodeControlller());
		fileNodeTypeDescriptor.addControllerToList(REMOVE_NODE_CONTROLLER, new FileRemoveNodeController());
		fileNodeTypeDescriptor.addControllerToList(PROPERTY_SETTER, new FilePropertySetter());

		fileNodeTypeDescriptor.addControllerToList(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs("body").setReadOnlyAs(false));
		fileNodeTypeDescriptor.addControllerToList(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs("hasChildren"));
		fileNodeTypeDescriptor.addControllerToList(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs("size"));
		fileNodeTypeDescriptor.addControllerToList(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs("isDirectory").setReadOnlyAs(false));
		fileNodeTypeDescriptor.addControllerToList(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs("creationTime"));
		fileNodeTypeDescriptor.addControllerToList(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs("lastModifiedTime"));
		fileNodeTypeDescriptor.addControllerToList(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs("lastAccessTime"));
		
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
