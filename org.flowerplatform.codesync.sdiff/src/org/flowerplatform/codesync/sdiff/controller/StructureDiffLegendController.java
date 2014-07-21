package org.flowerplatform.codesync.sdiff.controller;

import static org.flowerplatform.codesync.CodeSyncConstants.CODESYNC;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.SKIP_MATCH_CHILDREN_PROVIDER;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.LEGEND;
import static org.flowerplatform.core.CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS;
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
import org.flowerplatform.core.node.resource.BaseResourceHandler;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

public class StructureDiffLegendController extends AbstractController implements IChildrenProvider {
	public StructureDiffLegendController () {
		// invoked before the persistence providers
//		setOrderIndex(-10002);
	}
	
	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		List<Node> children = new ArrayList<Node>();
		
		Node example = new Node(Utils.getUri(LEGEND, "legend6"),LEGEND);
		children.add(example);
		
		return children;
	}
	
	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		context.getContext().put(DONT_PROCESS_OTHER_CONTROLLERS, true);
		return getChildren(node, context).size() > 0;
	}
}
