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

import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_BODY_MODIFIED;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_CHILDREN_MODIFIED_RIGHT;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_DIFFS_MODIFIED_RIGHT;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_MODEL_ELEMENT_TYPE;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_TYPE;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.MATCH_COLOR_ADDED;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.MATCH_COLOR_BODY_MODIFIED;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.MATCH_COLOR_CHILDREN_MODIFIED;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.MATCH_COLOR_PROP_MODIFIED;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.MATCH_COLOR_REMOVED;
import static org.flowerplatform.core.CoreConstants.CODESYNC_ICONS;
import static org.flowerplatform.core.CoreConstants.ICONS;
import static org.flowerplatform.mindmap.MindMapConstants.COLOR_BACKGROUND;

import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.Match.MatchType;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.controller.IPropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.mindmap.MindMapConstants;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana Gheorghe
 */
public class StructureDiffMatchPropertiesProvider extends AbstractController implements IPropertiesProvider,
		IPropertySetter {

	public StructureDiffMatchPropertiesProvider() {
		// invoke after the persistence providers
		// so the properties are populate
		setOrderIndex(10000);
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		String icons = (String) node.getProperties().get(ICONS);
		if (icons == null) {
			node.getProperties().put(ICONS, "");
		}

		String codeSyncIcons = getCodeSyncIcons(node);
		if (codeSyncIcons != null) {
			node.getProperties().put(CODESYNC_ICONS, codeSyncIcons);
		}

		setBackgroundColor(node); 
		setText(node); 
	}

	private String getCodeSyncIcons(Node node) {
		String icons = (String) node.getProperties().get(ICONS);
		
		String elementType = (String) node.getProperties().get(MATCH_MODEL_ELEMENT_TYPE);
		if (icons == null) {
			icons = "";
		}
		
		if (elementType == null) {
			return icons;
		}
		String icon = null;
		if (elementType.endsWith("File")) {
			icon = getImagePath("jcu_obj.gif");
		} else if (elementType.endsWith("Class")) {
			icon = getImagePath("class_obj.gif");
		} else if (elementType.endsWith("Interface")) {
			icon = getImagePath("interface_obj.gif");
		} else if (elementType.endsWith("Enum")) {
			icon = getImagePath("enum_obj.gif");
		} else if (elementType.endsWith("AnnotationType")) {
			icon = getImagePath("annotation_obj.gif");
		} else if (elementType.endsWith("Attribute") || elementType.endsWith("EnumConstant")) {
			icon = getImagePath("field_obj.gif");
		} else if (elementType.endsWith("Operation")) {
			icon = getImagePath("method_obj.gif");
		}

		if (icon == null) {
			return icons;
		} else {
			return (icons.isEmpty()) ? icon : (icons + "," + icon);
		}
	}

	private String getImagePath(String img) {
		return ResourcesPlugin.getInstance().getResourceUrl("/images/codesync.java/" + img);
	}

	private void setBackgroundColor(Node node) {
		String matchType = (String) node.getProperties().get(MATCH_TYPE);
		if (matchType == null) {
			return;
		}
		String color = null;
		switch (MatchType.valueOf(matchType)) {
		case _1MATCH_RIGHT:
			color = MATCH_COLOR_ADDED;
			break;
		case _2MATCH_ANCESTOR_LEFT:
			color = MATCH_COLOR_REMOVED;
			break;
		case _3MATCH:
			if (hasFlagTrue(node, MATCH_CHILDREN_MODIFIED_RIGHT)) {
				color = MATCH_COLOR_CHILDREN_MODIFIED;
			} else if (hasFlagTrue(node, MATCH_DIFFS_MODIFIED_RIGHT)) {
				color = MATCH_COLOR_PROP_MODIFIED;
			} else if (hasFlagTrue(node, MATCH_BODY_MODIFIED)) {
				color = MATCH_COLOR_BODY_MODIFIED;
			}
			break;
		}

		// set color
		if (color != null) {
			node.getProperties().put(COLOR_BACKGROUND, color);
		}
	}
	
	/**
	*  Adds the property "TEXT" which contains file's name and file's path (if it has one)
	* 
	* @author Alexandra Topoloaga
	*/
	
	private void setText(Node node) {
		String name = (String) node.getProperties().get(CoreConstants.NAME);
		String textPath = (String) node.getProperties().get(CodeSyncConstants.MATCH_PATH);
		if (textPath != null ) {
			node.getProperties().put(MindMapConstants.TEXT, "<html><head>" + name + "</head><br><body><font size=9>"+ textPath + "</font></body></html>");
		} else {
			node.getProperties().put(MindMapConstants.TEXT, name);
		}
	} 

	private boolean hasFlagTrue(Node node, String flag) {
		Boolean b = (Boolean) node.getProperties().get(flag);
		return b != null && b;
	}

	@Override
	public void setProperty(Node node, String property, Object value, ServiceContext<NodeService> context) {
		if (ICONS.equals(property)) {
			node.getOrPopulateProperties(context);
			
			String codeSyncIcons = getCodeSyncIcons(node);
			if (codeSyncIcons == null) {
				codeSyncIcons = "";
			}
			context.getService().setProperty(node, CODESYNC_ICONS, codeSyncIcons, context);
		}
	}

	@Override
	public void unsetProperty(Node node, String property, ServiceContext<NodeService> context) {
		// At this moment, this method it is not necessary because the
		// "remove..." actions for
		// the icons are treated by the setProperty(...) method
	}

}