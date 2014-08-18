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
package org.flowerplatform.codesync.sdiff.controller;

import static org.flowerplatform.mindmap.MindMapConstants.COLOR_BACKGROUND;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.ADDED;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.REMOVED;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.MODIFIED;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.MODIFIED_BODY;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.MODIFIED_CHILDREN;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.ADDED_COMMENT;

import org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.controller.AbstractController;

/**
 * Sets the the properties for legend's node children.
 * 
 * @author Alexandra Topoloaga
 */
public class StructureDiffLegendChildrenPropertiesProvider extends AbstractController implements IPropertiesProvider {
	
	@Override
		
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		switch(CorePlugin.getInstance().getVirtualNodeResourceHandler().getTypeSpecificPartFromNodeUri(node.getNodeUri())) {
		case ADDED: 
			node.getProperties().put(CoreConstants.NAME, ResourcesPlugin.getInstance().getMessage("codesync.sdiff.newlyAddedElement"));
			node.getProperties().put(COLOR_BACKGROUND, CodeSyncSdiffConstants.MATCH_COLOR_ADDED);
			break;
		case REMOVED: 
			node.getProperties().put(CoreConstants.NAME, ResourcesPlugin.getInstance().getMessage("codesync.sdiff.deletedElement"));
			node.getProperties().put(COLOR_BACKGROUND, CodeSyncSdiffConstants.MATCH_COLOR_REMOVED);
			break;
		case MODIFIED: 
			node.getProperties().put(CoreConstants.NAME, ResourcesPlugin.getInstance().getMessage("codesync.sdiff.modifiedElementStructure"));
			node.getProperties().put(COLOR_BACKGROUND, CodeSyncSdiffConstants.MATCH_COLOR_PROP_MODIFIED);
			break;
		case MODIFIED_BODY: 
			node.getProperties().put(CoreConstants.NAME, ResourcesPlugin.getInstance().getMessage("codesync.sdiff.modifiedElementBody"));
			node.getProperties().put(COLOR_BACKGROUND, CodeSyncSdiffConstants.MATCH_COLOR_BODY_MODIFIED);
			break;
		case MODIFIED_CHILDREN: 
			node.getProperties().put(CoreConstants.NAME, ResourcesPlugin.getInstance().getMessage("codesync.sdiff.modifiedChildren"));
			node.getProperties().put(COLOR_BACKGROUND, CodeSyncSdiffConstants.MATCH_COLOR_CHILDREN_MODIFIED);
			break;
		case ADDED_COMMENT: 
			node.getProperties().put(CoreConstants.NAME, ResourcesPlugin.getInstance().getMessage("codesync.sdiff.commentAdded"));
			node.getProperties().put(COLOR_BACKGROUND, CodeSyncSdiffConstants.MATCH_COLOR_COMMENT);
			break;
		default:
			break;
		}
	}
}
