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
package org.flowerplatform.codesync.sdiff.controller;

import static org.flowerplatform.codesync.CodeSyncConstants.CODESYNC_ICONS;
import static org.flowerplatform.core.CoreConstants.EXECUTE_ONLY_FOR_UPDATER;
import static org.flowerplatform.core.CoreConstants.ICONS;

import java.util.Map;

import org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.controller.IPropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana Gheorghe
 */
public class StructureDiffCommentController extends AbstractController implements IPropertiesProvider, IPropertySetter {

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		node.getProperties().put(CorePlugin.getInstance().getPropertyNameForVisualFeatureSupportedByMindMapRenderer(
				CoreConstants.BASE_RENDERER_BACKGROUND_COLOR), CodeSyncSdiffConstants.MATCH_COLOR_COMMENT);
		Object obj = node.getProperties().get(ICONS);
		String icons = "";
		if (obj != null) {
			icons = (String) obj;
		}
		node.getProperties().put(ICONS, icons);
		node.getProperties().put(CODESYNC_ICONS, icons + (icons.isEmpty() ? "" : CoreConstants.ICONS_SEPARATOR) 
				+ ResourcesPlugin.getInstance().getResourceUrl("/images/codesync.sdiff/comment-marker/comment.png"));
	}

	@Override
	public void setProperties(Node node, Map<String, Object> properties, ServiceContext<NodeService> context) {
		ServiceContext<NodeService> newContext = new ServiceContext<NodeService>(context.getService());
		newContext.getContext().put(EXECUTE_ONLY_FOR_UPDATER, true);
		for (String property : properties.keySet()) {
			if (property.equals(ICONS)) {
				String icons = (String) properties.get(ICONS);
				context.getService().setProperty(node, CODESYNC_ICONS, icons + (icons.isEmpty() ? "" : CoreConstants.ICONS_SEPARATOR) 
						+ ResourcesPlugin.getInstance().getResourceUrl("/images/codesync.sdiff/comment-marker/comments.png"), newContext);
			}
		}
	}

	@Override
	public void unsetProperty(Node node, String property, ServiceContext<NodeService> context) {
		// nothing to do
	}

}
