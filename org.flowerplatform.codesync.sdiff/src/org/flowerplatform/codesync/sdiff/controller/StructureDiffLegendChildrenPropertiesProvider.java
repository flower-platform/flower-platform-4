package org.flowerplatform.codesync.sdiff.controller;

import static org.flowerplatform.mindmap.MindMapConstants.COLOR_BACKGROUND;

import org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants;
import org.flowerplatform.core.CoreConstants;
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
	
	public static int number = 0;
	@Override
		
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		if (number == 6) {
			number = 0;
		}
		number++;
		switch(number){
		case 1: 
			node.getProperties().put(CoreConstants.NAME, ResourcesPlugin.getInstance().getMessage("codesync.sdiff.newlyAddedElement"));
			node.getProperties().put(COLOR_BACKGROUND, CodeSyncSdiffConstants.MATCH_COLOR_ADDED);
			break;
		case 2: 
			node.getProperties().put(CoreConstants.NAME, ResourcesPlugin.getInstance().getMessage("codesync.sdiff.deletedElement"));
			node.getProperties().put(COLOR_BACKGROUND, CodeSyncSdiffConstants.MATCH_COLOR_REMOVED);
			break;
		case 3: 
			node.getProperties().put(CoreConstants.NAME, ResourcesPlugin.getInstance().getMessage("codesync.sdiff.modifiedElementStructure"));
			node.getProperties().put(COLOR_BACKGROUND, CodeSyncSdiffConstants.MATCH_COLOR_PROP_MODIFIED);
			break;
		case 4: 
			node.getProperties().put(CoreConstants.NAME, ResourcesPlugin.getInstance().getMessage("codesync.sdiff.modifiedElementBody"));
			node.getProperties().put(COLOR_BACKGROUND, CodeSyncSdiffConstants.MATCH_COLOR_BODY_MODIFIED);
			break;
		case 5: 
			node.getProperties().put(CoreConstants.NAME, ResourcesPlugin.getInstance().getMessage("codesync.sdiff.modifiedChildren"));
			node.getProperties().put(COLOR_BACKGROUND, CodeSyncSdiffConstants.MATCH_COLOR_CHILDREN_MODIFIED);
			break;
		case 6: 
			node.getProperties().put(CoreConstants.NAME, ResourcesPlugin.getInstance().getMessage("codesync.sdiff.commentAdded"));
			node.getProperties().put(COLOR_BACKGROUND, CodeSyncSdiffConstants.MATCH_COLOR_COMMENT);
			break;
		}
	}
}