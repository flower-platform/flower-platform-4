package org.flowerplatform.core.fileSystem;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.Pair;

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
	
	public Node getFileSystem(Node parentode) {
		Node node = new Node();
		node.setIdWithinResource(parentode.getIdWithinResource() + ".fileSystem");
		node.setType("fileSystem");
		return node;
	}

}
