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
package org.flowerplatform.codesync.sdiff;

import static org.flowerplatform.codesync.CodeSyncConstants.MATCH;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.COMMENT;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.STRUCTURE_DIFF;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.STRUCTURE_DIFF_EXTENSION;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.STRUCTURE_DIFF_LEGEND;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.STRUCTURE_DIFF_LEGEND_CHILD;
import static org.flowerplatform.core.CoreConstants.ADD_CHILD_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.CHILDREN_PROVIDER;
import static org.flowerplatform.core.CoreConstants.FILE_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.CoreConstants.PROPERTY_FOR_TITLE_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.PROPERTY_SETTER;
import static org.flowerplatform.mindmap.MindMapConstants.TEXT;

import org.flowerplatform.codesync.sdiff.controller.StructureDiffCommentController;
import org.flowerplatform.codesync.sdiff.controller.StructureDiffLegendChildrenPropertiesProvider;
import org.flowerplatform.codesync.sdiff.controller.StructureDiffLegendController;
import org.flowerplatform.codesync.sdiff.controller.StructureDiffMatchChildrenProvider;
import org.flowerplatform.codesync.sdiff.controller.StructureDiffMatchPropertiesProvider;
import org.flowerplatform.codesync.sdiff.controller.StructureDiffNodeLegendController;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileSubscribableProvider;
import org.flowerplatform.core.node.remote.AddChildDescriptor;
import org.flowerplatform.core.node.remote.GenericValueDescriptor;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncSdiffPlugin extends AbstractFlowerJavaPlugin {

	protected static CodeSyncSdiffPlugin instance;
	
	private StructureDiffService sDiffService = new StructureDiffService();
	
	public static CodeSyncSdiffPlugin getInstance() {
		return instance;
	}
	
	public StructureDiffService getSDiffService() {
		return sDiffService;
	}
	/**
	 *@author Mariana Gheorghe
	 **/
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		instance = this;
		
		CorePlugin.getInstance().getServiceRegistry().registerService("structureDiffService", sDiffService);
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(STRUCTURE_DIFF_LEGEND_CHILD)
			.addAdditiveController(PROPERTIES_PROVIDER, new StructureDiffLegendChildrenPropertiesProvider());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(STRUCTURE_DIFF_LEGEND)
			.addAdditiveController(PROPERTIES_PROVIDER, new StructureDiffLegendController())
			.addAdditiveController(CHILDREN_PROVIDER, new StructureDiffLegendController());
	
		CorePlugin.getInstance().getVirtualNodeResourceHandler().addVirtualNodeType(STRUCTURE_DIFF_LEGEND_CHILD);
		CorePlugin.getInstance().getVirtualNodeResourceHandler().addVirtualNodeType(STRUCTURE_DIFF_LEGEND);
	
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(STRUCTURE_DIFF)
			.addAdditiveController(CHILDREN_PROVIDER, new StructureDiffNodeLegendController());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(FILE_NODE_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new FileSubscribableProvider(STRUCTURE_DIFF_EXTENSION, "fpp", "mindmap", true));
	
		StructureDiffMatchPropertiesProvider structureDiffMatchPropertiesController = new StructureDiffMatchPropertiesProvider();
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(MATCH)
			.addAdditiveController(PROPERTIES_PROVIDER, structureDiffMatchPropertiesController)
			.addAdditiveController(PROPERTY_SETTER, structureDiffMatchPropertiesController)
			.addAdditiveController(CHILDREN_PROVIDER, new StructureDiffMatchChildrenProvider())
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(COMMENT))
			.addSingleController(PROPERTY_FOR_TITLE_DESCRIPTOR, new GenericValueDescriptor(TEXT).setOrderIndexAs(-10000));
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(COMMENT)
			.addAdditiveController(PROPERTIES_PROVIDER, new StructureDiffCommentController());
	}
	
	/**
	 *@author Mariana Gheorghe
	 **/
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		instance = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// nothing to do yet
	}
	
}