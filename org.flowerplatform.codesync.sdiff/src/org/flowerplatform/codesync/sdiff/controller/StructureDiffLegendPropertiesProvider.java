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
import static org.flowerplatform.core.CoreConstants.NAME;


import java.util.HashMap;
import java.util.Map;

import org.flowerplatform.codesync.Match.MatchType;
import org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.controller.AbstractController;

import static org.flowerplatform.mindmap.MindMapConstants.NOTE;

public class StructureDiffLegendPropertiesProvider extends AbstractController implements IPropertiesProvider {
	Map<Integer,String> namesMap;
	Map<Integer,String> coloursMap;
	Integer key;
	
	public StructureDiffLegendPropertiesProvider() {
		// invoke after the persistence providers
		// so the properties are populate
		setOrderIndex(10000);
		namesMap = new HashMap<Integer, String>();
		coloursMap = new HashMap<Integer, String>();
		
		namesMap.put(0, CodeSyncSdiffConstants.EXAMPLE_ADDED);
		namesMap.put(1, CodeSyncSdiffConstants.EXAMPLE_REMOVED);
		namesMap.put(2, CodeSyncSdiffConstants.EXAMPLE_PROP_MODIFIED);
		namesMap.put(3, CodeSyncSdiffConstants.EXAMPLE_BODY_MODIFIED);
		namesMap.put(4, CodeSyncSdiffConstants.EXAMPLE_CHILDREN_MODIFIED);
		namesMap.put(5, CodeSyncSdiffConstants.EXAMPLE_COMMENT);
		namesMap.put(6, CodeSyncSdiffConstants.LEGEND_TEXT);
		
		coloursMap.put(0, CodeSyncSdiffConstants.MATCH_COLOR_ADDED);
		coloursMap.put(1, CodeSyncSdiffConstants.MATCH_COLOR_REMOVED);
		coloursMap.put(2, CodeSyncSdiffConstants.MATCH_COLOR_PROP_MODIFIED);
		coloursMap.put(3, CodeSyncSdiffConstants.MATCH_COLOR_BODY_MODIFIED);
		coloursMap.put(4, CodeSyncSdiffConstants.MATCH_COLOR_CHILDREN_MODIFIED);
		coloursMap.put(5, CodeSyncSdiffConstants.MATCH_COLOR_COMMENT);
		coloursMap.put(6, CodeSyncSdiffConstants.MATCH_COLOR_COMMENT);
		
	
		
	}
	
	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
//		setIcon(node);
		String uri = node.getNodeUri();
		
		String s = uri.substring(uri.length()-1);
		
		key = Integer.parseInt(s);
		if (key == 6) {
//			node.getProperties().put(HA);
		}
		setBackgroundColor(node);
		setText(node);
	}
	
	private void setText(Node node) {
		String text = (String) namesMap.get(key);
		if (text == null) {
			return;
		}
		node.getProperties().put(NAME, text);
	}
	
	
	
	private void setBackgroundColor(Node node) {	
		String colour = (String) coloursMap.get(key);
		if (colour == null) {
			return;
		}
		node.getProperties().put(COLOR_BACKGROUND, colour);
	}

	private boolean hasFlagTrue(Node node, String flag) {
		Boolean b = (Boolean) node.getProperties().get(flag);
		return b != null && b;
	}
}
