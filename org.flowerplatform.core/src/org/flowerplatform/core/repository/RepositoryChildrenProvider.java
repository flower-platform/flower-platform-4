package org.flowerplatform.core.repository;

import static org.flowerplatform.core.CoreConstants.FILE_SCHEME;
import static org.flowerplatform.core.CoreConstants.FILE_SYSTEM_NODE_TYPE;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Sebastian Solomon
 */
public class RepositoryChildrenProvider extends AbstractController implements IChildrenProvider {
	
	public RepositoryChildrenProvider() {
		setOrderIndex(200);
	}

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		List<Node> children = new ArrayList<Node>();
		children.add(getFileSystem(node));
		return children;	
	}
	
	public Node getFileSystem(Node parentNode) {
		Node node = new Node(FILE_SCHEME, parentNode.getSchemeSpecificPart(), null, FILE_SYSTEM_NODE_TYPE);
		return node;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return true;
	}

}
