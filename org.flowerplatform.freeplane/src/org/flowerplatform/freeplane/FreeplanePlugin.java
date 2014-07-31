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
package org.flowerplatform.freeplane;

import static org.flowerplatform.core.CoreConstants.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.CoreConstants.CATEGORY_RESOURCE_PREFIX;
import static org.flowerplatform.core.CoreConstants.CHILDREN_PROVIDER;
import static org.flowerplatform.core.CoreConstants.FILE_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.FILE_SYSTEM_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.PARENT_PROVIDER;
import static org.flowerplatform.core.CoreConstants.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.CoreConstants.PROPERTY_SETTER;
import static org.flowerplatform.core.CoreConstants.REMOVE_NODE_CONTROLLER;
import static org.flowerplatform.mindmap.MindMapConstants.FREEPLANE_MINDMAP_CATEGORY;
import static org.flowerplatform.mindmap.MindMapConstants.FREEPLANE_MINDMAP_RESOURCE_KEY;
import static org.flowerplatform.mindmap.MindMapConstants.FREEPLANE_PERSISTENCE_CATEGORY;
import static org.flowerplatform.mindmap.MindMapConstants.FREEPLANE_PERSISTENCE_RESOURCE_KEY;
import static org.flowerplatform.mindmap.MindMapConstants.MINDMAP_CONTENT_TYPE;
import static org.freeplane.features.url.UrlManager.FREEPLANE_FILE_EXTENSION;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileSubscribableProvider;
import org.flowerplatform.core.node.controller.DefaultPropertiesProvider;
import org.flowerplatform.freeplane.controller.MindMapAddNodeController;
import org.flowerplatform.freeplane.controller.MindMapChildrenProvider;
import org.flowerplatform.freeplane.controller.MindMapFileAddNodeController;
import org.flowerplatform.freeplane.controller.MindMapParentProvider;
import org.flowerplatform.freeplane.controller.MindMapPropertiesProvider;
import org.flowerplatform.freeplane.controller.MindMapPropertySetter;
import org.flowerplatform.freeplane.controller.MindMapRemoveNodeController;
import org.flowerplatform.freeplane.controller.PersistenceAddNodeProvider;
import org.flowerplatform.freeplane.controller.PersistencePropertiesProvider;
import org.flowerplatform.freeplane.controller.PersistencePropertySetter;
import org.flowerplatform.freeplane.remote.MindMapServiceRemote;
import org.flowerplatform.freeplane.resource.FreeplaneMindmapResourceHandler;
import org.flowerplatform.freeplane.resource.FreeplanePersistenceResourceHandler;
import org.flowerplatform.freeplane.style.controller.MindMapStyleChildrenProvider;
import org.flowerplatform.freeplane.style.controller.MindMapStyleResourceHandler;
import org.flowerplatform.freeplane.style.controller.StyleRootChildrenProvider;
import org.flowerplatform.freeplane.style.controller.StyleRootPropertiesProvider;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.freeplane.features.url.UrlManager;
import org.freeplane.main.headlessmode.HeadlessMModeControllerFactory;
import org.osgi.framework.BundleContext;

/**
 * @author Cristina Constantinescu
 */
public class FreeplanePlugin extends AbstractFlowerJavaPlugin {

	protected static FreeplanePlugin INSTANCE;
	
	public static final String STYLE_ROOT_NODE = "styleRootNode";
	
	public static final String MIND_MAP_STYLE = "mindMapStyle";
	
	static {
		// configure Freeplane starter
		new FreeplaneHeadlessStarter().createController().setMapViewManager(new HeadlessMapViewController());		
		HeadlessMModeControllerFactory.createModeController();	
	}
	
	public static FreeplanePlugin getInstance() {
		return INSTANCE;
	}
		
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
	
		FreeplanePersistenceResourceHandler fppResourceHandler = new FreeplanePersistenceResourceHandler();
		FreeplaneMindmapResourceHandler fpmResourceHandler = new FreeplaneMindmapResourceHandler();
		
		MindMapFileAddNodeController mindMapFileAddNodeController = new MindMapFileAddNodeController(UrlManager.FREEPLANE_FILE_EXTENSION);
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(FILE_SYSTEM_NODE_TYPE)
			.addAdditiveController(ADD_NODE_CONTROLLER, mindMapFileAddNodeController);
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(FILE_NODE_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new FileSubscribableProvider(FREEPLANE_FILE_EXTENSION, 
					FREEPLANE_MINDMAP_RESOURCE_KEY, MINDMAP_CONTENT_TYPE, true))
			.addAdditiveController(ADD_NODE_CONTROLLER, mindMapFileAddNodeController)
			.addAdditiveController(ADD_NODE_CONTROLLER, new MindMapFileAddNodeController(".regexMatches", "regexMatches"))  // TODO delete
			.addAdditiveController(ADD_NODE_CONTROLLER, new MindMapFileAddNodeController(".regex", "regexConfig"))  // TODO delete
			.addAdditiveController(ADD_NODE_CONTROLLER, new MindMapFileAddNodeController(".sdiff", "structureDiff")); // TODO delete
		
		CorePlugin.getInstance().getResourceService().addResourceHandler(FREEPLANE_PERSISTENCE_RESOURCE_KEY, fppResourceHandler);
		CorePlugin.getInstance().getResourceService().addResourceHandler(FREEPLANE_MINDMAP_RESOURCE_KEY, fpmResourceHandler);
		CorePlugin.getInstance().getResourceService().addResourceHandler(MIND_MAP_STYLE, new MindMapStyleResourceHandler());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(FREEPLANE_MINDMAP_CATEGORY)
			.addAdditiveController(PROPERTIES_PROVIDER, new MindMapPropertiesProvider())		
			.addAdditiveController(PROPERTY_SETTER, new MindMapPropertySetter())
			.addSingleController(PARENT_PROVIDER, new MindMapParentProvider())
			.addAdditiveController(CHILDREN_PROVIDER, new MindMapChildrenProvider())
			.addAdditiveController(ADD_NODE_CONTROLLER, new MindMapAddNodeController())
			.addAdditiveController(REMOVE_NODE_CONTROLLER, new MindMapRemoveNodeController())
			.addAdditiveController(CHILDREN_PROVIDER, new StyleRootChildrenProvider())
			.addAdditiveController(PROPERTIES_PROVIDER, new DefaultPropertiesProvider());

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(FREEPLANE_PERSISTENCE_CATEGORY)
			.addAdditiveController(PROPERTIES_PROVIDER, new PersistencePropertiesProvider())		
			.addAdditiveController(PROPERTY_SETTER, new PersistencePropertySetter())
			.addSingleController(PARENT_PROVIDER, new MindMapParentProvider())
			.addAdditiveController(CHILDREN_PROVIDER, new MindMapChildrenProvider())
			.addAdditiveController(ADD_NODE_CONTROLLER, new PersistenceAddNodeProvider())
			.addAdditiveController(REMOVE_NODE_CONTROLLER, new MindMapRemoveNodeController());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(STYLE_ROOT_NODE)
			.addAdditiveController(PROPERTIES_PROVIDER, new StyleRootPropertiesProvider())
			.addAdditiveController(CHILDREN_PROVIDER, new MindMapStyleChildrenProvider());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(CATEGORY_RESOURCE_PREFIX + MIND_MAP_STYLE)
			.addAdditiveController(PROPERTIES_PROVIDER, new MindMapPropertiesProvider());
		
		CorePlugin.getInstance().getServiceRegistry().registerService("mindmapService", new MindMapServiceRemote());	
	}

	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// not used
	}	
	
}
