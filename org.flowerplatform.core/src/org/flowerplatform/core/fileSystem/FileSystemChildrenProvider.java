package org.flowerplatform.core.fileSystem;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class FileSystemChildrenProvider extends ChildrenProvider {
	
	public FileSystemChildrenProvider() {
		setOrderIndex(200);
	}

	@Override
	public List<Node> getChildren(Node node) {
		List<Node> children = new ArrayList<Node>();
		children.add(getFileSystem(node));
		return children;	
	}
	
	public Node getFileSystem(Node parentnode) {
		Node node = new Node(CorePlugin.FILE_SYSTEM_NODE_TYPE, CorePlugin.FILE_SYSTEM_PATH, null, null);
		return node;
	}

}
