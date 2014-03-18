package org.flowerplatform.core.fileSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class RepoChildrenProvider extends ChildrenProvider {
	
	public RepoChildrenProvider() {
		setOrderIndex(200);
	}

	@Override
	public List<Node> getChildren(Node node, Map<String, Object> options) {
		List<Node> children = new ArrayList<Node>();
		children.add(getFileSystem(node));
		return children;	
	}
	
	public Node getFileSystem(Node parentnode) {
		Node node = new Node(CorePlugin.FILE_SYSTEM_NODE_TYPE, CorePlugin.SELF_RESOURCE, parentnode.getIdWithinResource(), null);
		return node;
	}

	@Override
	public boolean hasChildren(Node node, Map<String, Object> options) {
		return true;
	}

}
