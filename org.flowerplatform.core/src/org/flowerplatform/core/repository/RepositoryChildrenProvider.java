package org.flowerplatform.core.repository;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.Utils;
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
		String scheme = CoreConstants.FILE_NODE_TYPE;
		String ssp = Utils.getUri(parentNode.getNodeUri()).getSchemeSpecificPart();
		Node node = new Node(Utils.getString(Utils.getUri(scheme, ssp, null)));
		node.setType(scheme);
		return node;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return true;
	}

}
