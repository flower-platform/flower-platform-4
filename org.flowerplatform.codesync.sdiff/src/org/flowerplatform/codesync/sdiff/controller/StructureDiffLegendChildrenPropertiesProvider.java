package org.flowerplatform.codesync.sdiff.controller;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.MATCH_COLOR_ADDED;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.MATCH_COLOR_BODY_MODIFIED;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.MATCH_COLOR_CHILDREN_MODIFIED;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.MATCH_COLOR_PROP_MODIFIED;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.MATCH_COLOR_REMOVED;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.MATCH_COLOR_COMMENT;
import static org.flowerplatform.mindmap.MindMapConstants.COLOR_BACKGROUND;

import org.flowerplatform.core.CoreConstants;

/**
 * 
 * @author Tita Andreea
 *
 */

public class StructureDiffLegendChildrenPropertiesProvider  extends AbstractController implements IPropertiesProvider {

	public void populateWithProperties(Node node, ServiceContext<NodeService> context){
		setBackgroundColor(node);
	}
	
	public void setBackgroundColor(Node node){
		String color = null,name = null;
		String nodeUri = node.getNodeUri();
		char  lastCharNodeUri = nodeUri.charAt(nodeUri.length() - 1);
		switch (lastCharNodeUri) {
			case '0':
				color = MATCH_COLOR_ADDED;
				name = "Newly added element";
				break;
			case '1':
				color = MATCH_COLOR_REMOVED;
				name = "Deleted element";
				break;
			case '2':
				color = MATCH_COLOR_PROP_MODIFIED;
				name = "Modified element:only the structure";
				break;
			case '3':
				color = MATCH_COLOR_BODY_MODIFIED;
				name = "Modified element:the body";
				break;
			case '4':
				color = MATCH_COLOR_CHILDREN_MODIFIED;
				name = "Children are modified:the element is not modified";
				break;
			case '5':
				color = MATCH_COLOR_COMMENT;
				name = "Comment";
				break;
		}
		
		if(color != null){
			node.getProperties().put(COLOR_BACKGROUND, color);
		}
		
		if(name != null){
			node.getProperties().put(CoreConstants.NAME, name);
		}
				
	}
		
}
	
