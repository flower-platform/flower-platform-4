/**
 * 
 */
package org.flowerplatform.codesync.sdiff.controller;

import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.SKIP_MATCH_CHILDREN_PROVIDER;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.LEGEND_CHILD;
import static org.flowerplatform.core.CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS;
import static org.flowerplatform.core.CoreConstants.FILE_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.POPULATE_WITH_PROPERTIES;
import static org.flowerplatform.core.file.FileControllerUtils.createFileNodeUri;
import static org.flowerplatform.core.file.FileControllerUtils.getFileAccessController;
import static org.flowerplatform.mindmap.MindMapConstants.COLOR_BACKGROUND;
import static org.flowerplatform.mindmap.MindMapConstants.TEXT;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Alina Bratu
 *
 */
public class StructureDiffLegendChildrenProvider extends AbstractController implements IChildrenProvider {
	
	public StructureDiffLegendChildrenProvider() {
		// invoked before the persistence providers
//		setOrderIndex(-10001);
	}
	
	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		List<Node> children = new ArrayList<Node>();
		for (int i=0;i<6;i++) {
			Node example = new Node(Utils.getUri(node.getType(), i+""),LEGEND_CHILD);
			children.add(example);
		}
		context.getContext().put(DONT_PROCESS_OTHER_CONTROLLERS, true);
		return children;
	}
	
	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		context.getContext().put(DONT_PROCESS_OTHER_CONTROLLERS, true);
		return getChildren(node, context).size() > 0;
	}
}
