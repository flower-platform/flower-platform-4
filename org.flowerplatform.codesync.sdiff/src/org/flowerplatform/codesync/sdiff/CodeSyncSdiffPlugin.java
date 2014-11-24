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
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.CATEGORY_CAN_CONTAIN_COMMENT;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.COMMENT;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.STRUCTURE_DIFF;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.STRUCTURE_DIFF_EXTENSION;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.STRUCTURE_DIFF_LEGEND;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.STRUCTURE_DIFF_LEGEND_CHILD;
import static org.flowerplatform.core.CoreConstants.ADD_CHILD_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.CoreConstants.BASE_RENDERER_ICONS;
import static org.flowerplatform.core.CoreConstants.CHILDREN_PROVIDER;
import static org.flowerplatform.core.CoreConstants.FILE_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.ICONS;
import static org.flowerplatform.core.CoreConstants.MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX;
import static org.flowerplatform.core.CoreConstants.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.CoreConstants.PROPERTY_SETTER;
import static org.flowerplatform.core.CoreConstants.REMOVE_NODE_CONTROLLER;
import static org.flowerplatform.mindmap.MindMapConstants.MINDMAP_ICONS_WITH_BUTTON_DESCRIPTOR_TYPE;
import static org.flowerplatform.util.UtilConstants.EXTRA_INFO_VALUE_CONVERTER;
import static org.flowerplatform.util.UtilConstants.FEATURE_PROPERTY_DESCRIPTORS;
import static org.flowerplatform.util.UtilConstants.VALUE_CONVERTER_CSV_TO_LIST;

import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.sdiff.controller.CanContainCommentAddNodeListener;
import org.flowerplatform.codesync.sdiff.controller.CanContainCommentPropertyController;
import org.flowerplatform.codesync.sdiff.controller.CanContainCommentRemoveNodeListener;
import org.flowerplatform.codesync.sdiff.controller.StructureDiffCommentController;
import org.flowerplatform.codesync.sdiff.controller.StructureDiffLegendChildrenPropertiesProvider;
import org.flowerplatform.codesync.sdiff.controller.StructureDiffLegendController;
import org.flowerplatform.codesync.sdiff.controller.StructureDiffMatchChildrenProvider;
import org.flowerplatform.codesync.sdiff.controller.StructureDiffMatchPropertiesController;
import org.flowerplatform.codesync.sdiff.controller.StructureDiffNodeLegendController;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileSubscribableProvider;
import org.flowerplatform.core.node.remote.AddChildDescriptor;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.controller.GenericDescriptor;
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
			.addAdditiveController(CHILDREN_PROVIDER, new StructureDiffNodeLegendController())
			.addCategory(CATEGORY_CAN_CONTAIN_COMMENT);
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(FILE_NODE_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, new FileSubscribableProvider(STRUCTURE_DIFF_EXTENSION, "fpp", "mindmap", true));

		StructureDiffMatchPropertiesController structureDiffMatchPropertiesController = new StructureDiffMatchPropertiesController();
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(MATCH)
			.addAdditiveController(PROPERTIES_PROVIDER, structureDiffMatchPropertiesController)
			.addAdditiveController(PROPERTY_SETTER, structureDiffMatchPropertiesController)
			.addAdditiveController(CHILDREN_PROVIDER, new StructureDiffMatchChildrenProvider())
			.addCategory(CATEGORY_CAN_CONTAIN_COMMENT)
			.addSingleController(CoreConstants.MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX + CoreConstants.BASE_RENDERER_TEXT,
					new GenericDescriptor(CodeSyncSdiffConstants.PROPERTY_NAME_WITH_PATH).setOrderIndexAs(-10000));
		
		StructureDiffCommentController commentController = new StructureDiffCommentController();
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(COMMENT)
			.addAdditiveController(PROPERTIES_PROVIDER, commentController)
			.addAdditiveController(PROPERTY_SETTER, commentController)
			.addCategory(CATEGORY_CAN_CONTAIN_COMMENT);

		CanContainCommentPropertyController commentPropertyProvider = new CanContainCommentPropertyController(); 
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(CATEGORY_CAN_CONTAIN_COMMENT)
			.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(ICONS).setTypeAs(MINDMAP_ICONS_WITH_BUTTON_DESCRIPTOR_TYPE))	
			.addSingleController(MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX + BASE_RENDERER_ICONS, new GenericDescriptor(CodeSyncConstants.CODE_SYNC_ICONS)
					.addExtraInfoProperty(EXTRA_INFO_VALUE_CONVERTER, VALUE_CONVERTER_CSV_TO_LIST).setOrderIndexAs(-1000))
			.addAdditiveController(PROPERTIES_PROVIDER, commentPropertyProvider)
			.addAdditiveController(PROPERTY_SETTER, commentPropertyProvider)
			.addAdditiveController(ADD_NODE_CONTROLLER, new CanContainCommentAddNodeListener().setOrderIndexAs(10000))
			.addAdditiveController(REMOVE_NODE_CONTROLLER, new CanContainCommentRemoveNodeListener().setOrderIndexAs(-10000))
			.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(COMMENT)
					.setIconAs(ResourcesPlugin.getInstance().getResourceUrl("/images/codesync.sdiff/comment-marker/comment.png")));
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
