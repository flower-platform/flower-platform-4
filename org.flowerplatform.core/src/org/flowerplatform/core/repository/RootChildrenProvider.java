package org.flowerplatform.core.repository;

import static org.flowerplatform.core.CoreConstants.REPOSITORY_TYPE;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Sebastian Solomon
 */
public class RootChildrenProvider extends AbstractController implements IChildrenProvider {

	public RootChildrenProvider() {
		setOrderIndex(200);
	}

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		List<Node> children = new ArrayList<Node>();
		
		// idWithinResource == null -> path to workspace location
		Node repository = new Node(Utils.getUri(REPOSITORY_TYPE, Utils.getSchemeSpecificPart(node.getNodeUri())), REPOSITORY_TYPE);
		children.add(repository);
		return children;
	}
	
	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return true;
	}
}
