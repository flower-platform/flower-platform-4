package org.flowerplatform.codesync.sdiff.controller;

import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.STRUCTURE_DIFF_LEGEND_CHILDREN;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Alexandra Topoloaga
 */
public class StructureDiffLegendController extends AbstractController implements IChildrenProvider, IPropertiesProvider {

	public StructureDiffLegendController(){
		setOrderIndex(-1);
	}

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		List<Node> children = new ArrayList<Node>();
		for (int i = 1; i<= 6; i++){
			children.add(new Node(STRUCTURE_DIFF_LEGEND_CHILDREN + ":" + STRUCTURE_DIFF_LEGEND_CHILDREN, STRUCTURE_DIFF_LEGEND_CHILDREN));
		}
		return children;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return true;
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		node.getProperties().put(CoreConstants.NAME, "Legend");
		}
}
