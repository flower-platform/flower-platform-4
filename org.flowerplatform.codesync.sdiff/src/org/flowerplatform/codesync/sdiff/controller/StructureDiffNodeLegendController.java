package org.flowerplatform.codesync.sdiff.controller;

import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.STRUCTURE_DIFF_LEGEND;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.VirtualNodeResourceHandler;
import org.flowerplatform.util.controller.AbstractController;

/**
 * Creates the legend node.
 * 
 * @author Alexandra Topoloaga
 */
public class StructureDiffNodeLegendController extends AbstractController implements IChildrenProvider {

	public StructureDiffNodeLegendController() {
		setOrderIndex(-1);
	}

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		List<Node> children = new ArrayList<Node>();
		String repo = CoreUtils.getRepoFromNode(node);
		VirtualNodeResourceHandler virtualNodeHandler = CorePlugin.getInstance().getVirtualNodeResourceHandler();
		children.add(virtualNodeHandler.createNodeFromRawNodeData(virtualNodeHandler.createVirtualNodeUri(repo, STRUCTURE_DIFF_LEGEND, null), null));
		return children;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return true;
	}

}
