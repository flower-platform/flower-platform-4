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

import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.IMG_TYPE_COMMENT;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.MATCH_COLOR_COMMENT;
import static org.flowerplatform.core.CoreConstants.ICONS;
import static org.flowerplatform.mindmap.MindMapConstants.COLOR_BACKGROUND;

import org.flowerplatform.codesync.sdiff.CodeSyncSdiffPlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana Gheorghe
 */
public class StructureDiffCommentController extends AbstractController implements IPropertiesProvider {
	
	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		node.getProperties().put(COLOR_BACKGROUND, MATCH_COLOR_COMMENT);
		node.getProperties().put(ICONS, CodeSyncSdiffPlugin.getInstance().getImagePath(IMG_TYPE_COMMENT));
	}

}
