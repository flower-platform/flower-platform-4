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
package org.flowerplatform.codesync.sdiff;

import static org.flowerplatform.codesync.CodeSyncConstants.MATCH;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.COMMENT;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.STRUCTURE_DIFF;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.STRUCTURE_DIFF_EXTENSION;
import static org.flowerplatform.core.CoreConstants.ADD_CHILD_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.CHILDREN_PROVIDER;
import static org.flowerplatform.core.CoreConstants.FILE_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.PROPERTIES_PROVIDER;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.LEGEND;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.LEGEND_CHILD;

import org.flowerplatform.codesync.sdiff.controller.StructureDiffCommentController;
import org.flowerplatform.codesync.sdiff.controller.StructureDiffLegendChildrenPropertiesProvider;
import org.flowerplatform.codesync.sdiff.controller.StructureDiffLegendChildrenProvider;
import org.flowerplatform.codesync.sdiff.controller.StructureDiffLegendPropertiesProvider;
import org.flowerplatform.codesync.sdiff.controller.StructureDiffMatchChildrenProvider;
import org.flowerplatform.codesync.sdiff.controller.StructureDiffMatchPropertiesProvider;
import org.flowerplatform.codesync.sdiff.controller.StructureDiffLegendProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileSubscribableProvider;
import org.flowerplatform.core.node.remote.AddChildDescriptor;
import org.flowerplatform.core.node.resource.BaseResourceHandler;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncSdiffPlugin extends AbstractFlowerJavaPlugin {

	protected static CodeSyncSdiffPlugin INSTANCE;
	
	private StructureDiffService sDiffService = new StructureDiffService();
	
	public static CodeSyncSdiffPlugin getInstance() {
		return INSTANCE;
	}
	
	public StructureDiffService getSDiffService() {
		return sDiffService;
	}
	
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
		
		CorePlugin.getInstance().getServiceRegistry().registerService("structureDiffService", sDiffService);
		
		/* virtual node -> path:legend/legend */
		CorePlugin.getInstance().getResourceService().addResourceHandler(LEGEND, new BaseResourceHandler(LEGEND));
		CorePlugin.getInstance().getResourceService().addResourceHandler(LEGEND_CHILD, new BaseResourceHandler(LEGEND_CHILD));
		
		/* create Legend_Child node + add properties*/
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(LEGEND_CHILD)
			.addAdditiveController(PROPERTIES_PROVIDER, new StructureDiffLegendChildrenPropertiesProvider());
		
		/* create Legend node + add properties + children  */
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(LEGEND)
			.addAdditiveController(PROPERTIES_PROVIDER, new StructureDiffLegendPropertiesProvider())
			.addAdditiveController(CHILDREN_PROVIDER, new StructureDiffLegendChildrenProvider());
		
		/* add Legend node to root */
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(STRUCTURE_DIFF)
			.addAdditiveController(CHILDREN_PROVIDER, new StructureDiffLegendProvider());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(FILE_NODE_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new FileSubscribableProvider(STRUCTURE_DIFF_EXTENSION, "fpp", "mindmap", true));
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(MATCH)
			.addAdditiveController(PROPERTIES_PROVIDER, new StructureDiffMatchPropertiesProvider())
			.addAdditiveController(CHILDREN_PROVIDER, new StructureDiffMatchChildrenProvider())
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(COMMENT).setLabelAs(ResourcesPlugin.getInstance().getMessage("codesync.sdiff.comment")));
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(COMMENT)
			.addAdditiveController(PROPERTIES_PROVIDER, new StructureDiffCommentController());
	}
	
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// nothing to do yet
	}
	
}
