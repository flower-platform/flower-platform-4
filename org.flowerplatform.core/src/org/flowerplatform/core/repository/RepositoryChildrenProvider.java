package org.flowerplatform.core.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class RepositoryChildrenProvider extends ChildrenProvider {
	
	public RepositoryChildrenProvider() {
		setOrderIndex(200);
	}

	@Override
	public List<Node> getChildren(Node node, ServiceContext context) {
		List<Node> children = new ArrayList<Node>();
		children.add(getFileSystem(node));
		return children;	
	}
	
	public Node getFileSystem(Node parentnode) {
		Node node = new Node("fileSystem", CorePlugin.SELF_RESOURCE, parentnode.getIdWithinResource(), null);
		return node;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext context) {
		return true;
	}

}
