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

import static org.flowerplatform.core.NodePropertiesConstants.CREATION_TIME;
import static org.flowerplatform.core.NodePropertiesConstants.HAS_CHILDREN;
import static org.flowerplatform.core.NodePropertiesConstants.IS_DIRECTORY;
import static org.flowerplatform.core.NodePropertiesConstants.LAST_ACCESS_TIME;
import static org.flowerplatform.core.NodePropertiesConstants.LAST_MODIFIED_TIME;
import static org.flowerplatform.core.NodePropertiesConstants.SIZE;
import static org.flowerplatform.core.NodePropertiesConstants.TEXT;
import static org.flowerplatform.core.node.controller.AddNodeController.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.node.controller.ChildrenProvider.CHILDREN_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertiesProvider.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertySetter.PROPERTY_SETTER;
import static org.flowerplatform.core.node.controller.RemoveNodeController.REMOVE_NODE_CONTROLLER;
import static org.flowerplatform.core.node.controller.RootNodeProvider.ROOT_NODE_PROVIDER;
import static org.flowerplatform.core.node.remote.AddChildDescriptor.ADD_CHILD_DESCRIPTOR;
import static org.flowerplatform.core.node.remote.PropertyDescriptor.PROPERTY_DESCRIPTOR;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.core.file.FileAddNodeController;
import org.flowerplatform.core.file.FileChildrenProvider;
import org.flowerplatform.core.file.FilePropertiesProvider;
import org.flowerplatform.core.file.FilePropertySetter;
import org.flowerplatform.core.file.FileRemoveNodeController;
import org.flowerplatform.core.file.FileRootNodeProvider;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.core.file.PlainFileAccessController;
import org.flowerplatform.core.fileSystem.FileSystemChildrenProvider;
import org.flowerplatform.core.fileSystem.FileSystemPropertiesProvider;
import org.flowerplatform.core.fileSystem.FileSystemRootNodeProvider;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.controller.RemoveNodeController;
import org.flowerplatform.core.node.controller.ResourceTypeDynamicCategoryProvider;
import org.flowerplatform.core.node.remote.AddChildDescriptor;
import org.flowerplatform.core.node.remote.NodeServiceRemote;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.core.node.update.InMemoryUpdateDAO;
import org.flowerplatform.core.node.update.UpdateService;
import org.flowerplatform.core.node.update.controller.UpdateAddNodeController;
import org.flowerplatform.core.node.update.controller.UpdatePropertySetterController;
import org.flowerplatform.core.node.update.controller.UpdateRemoveNodeController;
import org.flowerplatform.core.node.update.remote.UpdateServiceRemote;
import org.flowerplatform.util.controller.AllDynamicCategoryProvider;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.util.servlet.ResourcesServlet;
import org.osgi.framework.BundleContext;

/**
 * @author Cristian Spiescu
 * @author Cristina Constantinescu
 */
public class CorePlugin extends AbstractFlowerJavaPlugin {

	public static final String RESOURCE_KEY = "resource";
	public static final String TYPE_KEY = "type";
	public static final String FILE_SYSTEM_NODE_TYPE = "fileSystem";
	public static final String FILE_NODE_TYPE = "fileNode";
	public static final String FILE_SYSTEM_PATH = "d:/temp/fileSystemNode";
	
	
	protected static CorePlugin INSTANCE;

	protected IFileAccessController fileAccessController = new PlainFileAccessController();

	/**
	 * @author Sebastian Solomon
	 */
	protected RemoteMethodInvocationListener remoteMethodInvocationListener = new RemoteMethodInvocationListener();

	protected ServiceRegistry serviceRegistry = new ServiceRegistry();
	protected TypeDescriptorRegistry nodeTypeDescriptorRegistry = new TypeDescriptorRegistry();
	protected NodeService nodeService = new NodeService(nodeTypeDescriptorRegistry);
	protected UpdateService updateService = new UpdateService(new InMemoryUpdateDAO());

	public static CorePlugin getInstance() {
		return INSTANCE;
	}
	
	/**
	 * @author Sebastian Solomon
	 */
	public RemoteMethodInvocationListener getRemoteMethodInvocationListener() {
		return remoteMethodInvocationListener;
	}
	
	/**
	 * @author Mariana Gheorghe
	 */
	public IFileAccessController getFileAccessController() {
		return fileAccessController;
	}
	
	public void setFileAccessController(IFileAccessController fileAccessController) {
		this.fileAccessController = fileAccessController;
	}

	public ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}

	public TypeDescriptorRegistry getNodeTypeDescriptorRegistry() {
		return nodeTypeDescriptorRegistry;
	}

	public NodeService getNodeService() {
		return nodeService;
	}

	public UpdateService getUpdateService() {
		return updateService;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
		
		
		
		
getServiceRegistry().registerService("nodeService", new NodeServiceRemote());
		getServiceRegistry().registerService("updateService", new UpdateServiceRemote());
				
		setFileAccessController(new PlainFileAccessController());
		
		TypeDescriptor dummyTypeDescriptor = getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor("dummyNode");
		dummyTypeDescriptor.addAdditiveController(CHILDREN_PROVIDER, new FileSystemChildrenProvider());
		
		TypeDescriptor fileSystemNodeTypeDescriptor = getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(FILE_SYSTEM_NODE_TYPE);
		fileSystemNodeTypeDescriptor.addSingleController(ROOT_NODE_PROVIDER, new FileSystemRootNodeProvider());
		fileSystemNodeTypeDescriptor.addAdditiveController(PROPERTIES_PROVIDER, new FileSystemPropertiesProvider());
		fileSystemNodeTypeDescriptor.addAdditiveController(CHILDREN_PROVIDER, new FileChildrenProvider());
		fileSystemNodeTypeDescriptor.addAdditiveController(REMOVE_NODE_CONTROLLER, new FileRemoveNodeController());
		fileSystemNodeTypeDescriptor.addAdditiveController(ADD_NODE_CONTROLLER, new FileAddNodeController());
		
		addChildDescriptors(fileSystemNodeTypeDescriptor);

		TypeDescriptor fileNodeTypeDescriptor = getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(FILE_NODE_TYPE);
		fileNodeTypeDescriptor.addSingleController(ROOT_NODE_PROVIDER, new FileRootNodeProvider());
		fileNodeTypeDescriptor.addAdditiveController(CHILDREN_PROVIDER, new FileChildrenProvider());
		fileNodeTypeDescriptor.addAdditiveController(PROPERTIES_PROVIDER, new FilePropertiesProvider());
		fileNodeTypeDescriptor.addAdditiveController(ADD_NODE_CONTROLLER, new FileAddNodeController());
		fileNodeTypeDescriptor.addAdditiveController(REMOVE_NODE_CONTROLLER, new FileRemoveNodeController());
		fileNodeTypeDescriptor.addAdditiveController(PROPERTY_SETTER, new FilePropertySetter());
		
		addChildDescriptors(fileNodeTypeDescriptor);
		
		fileNodeTypeDescriptor.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(TEXT).setReadOnlyAs(false));
		fileNodeTypeDescriptor.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(HAS_CHILDREN));
		fileNodeTypeDescriptor.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(SIZE));
		fileNodeTypeDescriptor.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(IS_DIRECTORY));
		fileNodeTypeDescriptor.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(CREATION_TIME));
		fileNodeTypeDescriptor.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(LAST_MODIFIED_TIME));
		fileNodeTypeDescriptor.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(LAST_ACCESS_TIME));
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().addDynamicCategoryProvider(new ResourceTypeDynamicCategoryProvider());
				
		TypeDescriptor updaterDescriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(AllDynamicCategoryProvider.CATEGORY_ALL);
		updaterDescriptor.addAdditiveController(AddNodeController.ADD_NODE_CONTROLLER, new UpdateAddNodeController());
		updaterDescriptor.addAdditiveController(RemoveNodeController.REMOVE_NODE_CONTROLLER, new UpdateRemoveNodeController());
		updaterDescriptor.addAdditiveController(PropertySetter.PROPERTY_SETTER, new UpdatePropertySetterController());
			
		//TODO use Flower property
		boolean isDeleteTempFolderAtStartProperty = true;
		if (isDeleteTempFolderAtStartProperty) {
			FileUtils.deleteDirectory(ResourcesServlet.TEMP_FOLDER);
		}
	}

	private void addChildDescriptors(TypeDescriptor typeDescriptor) {
		Map<String, Object> filePropertyMap = new HashMap<String, Object>();
		Map<String, Object> folderPropertyMap = new HashMap<String, Object>();
		folderPropertyMap.put(IS_DIRECTORY, true);
		filePropertyMap.put(IS_DIRECTORY, false);
		
		typeDescriptor
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(FILE_NODE_TYPE).setLabelAs("File")
				.setPropertiesAs(filePropertyMap)
				.setIconAs(getResourceUrl("images/file.gif"))
				.setOrderIndexAs(10))
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(FILE_NODE_TYPE).setLabelAs("Folder")
				.setPropertiesAs(folderPropertyMap)
				.setIconAs(getResourceUrl("images/folder.gif"))
				.setOrderIndexAs(20));
	}

	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// no messages yet
	}
	
}
