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
import static org.flowerplatform.core.CoreConstants.ICONS;
import static org.flowerplatform.mindmap.MindMapConstants.COLOR_BACKGROUND;

import org.flowerplatform.codesync.Match.MatchType;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana Gheorghe
 */
public class StructureDiffMatchPropertiesProvider extends AbstractController implements IPropertiesProvider {

	public StructureDiffMatchPropertiesProvider() {
		// invoke after the persistence providers
		// so the properties are populate
		setOrderIndex(10000);
	}
	
	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		setIcon(node);
		setBackgroundColor(node);
	}
	
	private void setIcon(Node node) {
		String elementType = (String) node.getProperties().get(MATCH_MODEL_ELEMENT_TYPE);
		if (elementType == null) {
			return;
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
		if (icon != null) {
			node.getProperties().put(ICONS, icon);
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
			if (hasFlagTrue(node, MATCH_BODY_MODIFIED)) {
				color = MATCH_COLOR_BODY_MODIFIED;
			} else if (hasFlagTrue(node, MATCH_CHILDREN_MODIFIED_RIGHT)) {
				color = MATCH_COLOR_CHILDREN_MODIFIED;
			} else if (hasFlagTrue(node, MATCH_DIFFS_MODIFIED_RIGHT)) {
				color = MATCH_COLOR_PROP_MODIFIED;
			}
		}
		
		// set color
		if (color != null) {
			node.getProperties().put(COLOR_BACKGROUND, color);
		}
	}

	private boolean hasFlagTrue(Node node, String flag) {
		Boolean b = (Boolean) node.getProperties().get(flag);
		return b != null && b;
	}
	
}
