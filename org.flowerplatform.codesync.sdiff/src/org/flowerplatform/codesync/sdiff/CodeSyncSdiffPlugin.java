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
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.CATEGORY_CAN_CONTAIN_COMMENT;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.COMMENT;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.STRUCTURE_DIFF;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.STRUCTURE_DIFF_EXTENSION;
import static org.flowerplatform.core.CoreConstants.ADD_CHILD_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.CoreConstants.CHILDREN_PROVIDER;
import static org.flowerplatform.core.CoreConstants.FILE_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.CoreConstants.PROPERTY_SETTER;
import static org.flowerplatform.core.CoreConstants.REMOVE_NODE_CONTROLLER;

import org.flowerplatform.codesync.sdiff.controller.CanContainCommentAddNodeListener;
import org.flowerplatform.codesync.sdiff.controller.CanContainCommentPropertyProvider;
import org.flowerplatform.codesync.sdiff.controller.CanContainCommentRemoveNodeListener;
import org.flowerplatform.codesync.sdiff.controller.StructureDiffCommentController;
import org.flowerplatform.codesync.sdiff.controller.StructureDiffMatchChildrenProvider;
import org.flowerplatform.codesync.sdiff.controller.StructureDiffMatchPropertiesProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileSubscribableProvider;
import org.flowerplatform.core.node.remote.AddChildDescriptor;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;
import static org.flowerplatform.codesync.CodeSyncConstants.CATEGORY_CAN_HOLD_CUSTOM_ICON;

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

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(STRUCTURE_DIFF)
				.addCategory(CATEGORY_CAN_CONTAIN_COMMENT);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(FILE_NODE_TYPE)
				.addAdditiveController(PROPERTIES_PROVIDER, new FileSubscribableProvider(STRUCTURE_DIFF_EXTENSION, "fpp", "mindmap", true));

		StructureDiffMatchPropertiesProvider structureDiffMatchPropertiesController = new StructureDiffMatchPropertiesProvider();
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(MATCH)
				.addAdditiveController(PROPERTIES_PROVIDER, structureDiffMatchPropertiesController)
			.addAdditiveController(PROPERTY_SETTER, structureDiffMatchPropertiesController)
				.addAdditiveController(CHILDREN_PROVIDER, new StructureDiffMatchChildrenProvider())
				.addCategory(CATEGORY_CAN_CONTAIN_COMMENT);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(COMMENT)
				.addAdditiveController(PROPERTIES_PROVIDER, new StructureDiffCommentController().setOrderIndexAs(5000))
				.addAdditiveController(PROPERTY_SETTER, new StructureDiffCommentController().setOrderIndexAs(5000))
				.addCategory(CATEGORY_CAN_CONTAIN_COMMENT)
				.addCategory(CATEGORY_CAN_HOLD_CUSTOM_ICON);

		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(CATEGORY_CAN_CONTAIN_COMMENT)
				.addAdditiveController(PROPERTIES_PROVIDER, new CanContainCommentPropertyProvider())
				.addAdditiveController(PROPERTY_SETTER, new CanContainCommentPropertyProvider())
				.addAdditiveController(ADD_NODE_CONTROLLER, new CanContainCommentAddNodeListener().setOrderIndexAs(10000))
				.addAdditiveController(REMOVE_NODE_CONTROLLER, new CanContainCommentRemoveNodeListener().setOrderIndexAs(-10000))
				.addAdditiveController(ADD_CHILD_DESCRIPTOR,
						new AddChildDescriptor().setChildTypeAs(COMMENT).setLabelAs(ResourcesPlugin.getInstance().getMessage("codesync.sdiff.comment")));
	}

	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// nothing to do yet
	}

	public String getImagePath(String img) {
		return ResourcesPlugin.getInstance().getResourceUrl("/images/codesync.sdiff/comment-marker/" + img);
	}

}