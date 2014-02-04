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
import org.flowerplatform.core.mindmap.FreeplaneAddNodeController;
import org.flowerplatform.core.mindmap.FreeplaneChildrenProvider;
import org.flowerplatform.core.mindmap.FreeplanePropertiesProvider;
import org.flowerplatform.core.mindmap.FreeplanePropertySetter;
import org.flowerplatform.core.mindmap.FreeplaneRemoveNodeController;
import org.flowerplatform.core.mindmap.FreeplaneUtils;
import org.flowerplatform.core.node.NodeTypeDescriptor;
import org.flowerplatform.core.node.NodeTypeDescriptorRegistry;
import org.flowerplatform.core.node.remote.NodeService;
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
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
		
		NodeTypeDescriptor nodeTypeDescriptor = nodeTypeDescriptorRegistry.getOrCreateNodeTypeDescriptor("freeplaneNode");
		nodeTypeDescriptor.addChildrenProvider(new FreeplaneChildrenProvider());
		nodeTypeDescriptor.addPropertiesProvider(new FreeplanePropertiesProvider());
		nodeTypeDescriptor.addAddNodeController(new FreeplaneAddNodeController());
		nodeTypeDescriptor.addRemoveNodeController(new FreeplaneRemoveNodeController());
		nodeTypeDescriptor.addPropertySetter(new FreeplanePropertySetter());
		
		getServiceRegistry().registerService("nodeService", new NodeService(nodeTypeDescriptorRegistry));
		
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
	
	private NodeTypeDescriptorRegistry nodeTypeDescriptorRegistry = new NodeTypeDescriptorRegistry();

	public NodeTypeDescriptorRegistry getNodeTypeDescriptorRegistry() {
		return nodeTypeDescriptorRegistry;
	}
	
	/**
	 * TODO CC: move to Freeplane plugin
	 * @author Cristina Constantinescu
	 */
	private FreeplaneUtils freeplaneUtils = new FreeplaneUtils();

	/**
	 * @author Cristina Constantinescu
	 */
	public FreeplaneUtils getFreeplaneUtils() {
		return freeplaneUtils;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// no messages yet
	}

}
