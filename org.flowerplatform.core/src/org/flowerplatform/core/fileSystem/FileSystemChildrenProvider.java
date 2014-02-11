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
	public List<Pair<Node, Object>> getChildren(Node node) {
		List<Pair<Node, Object>> children = new ArrayList<Pair<Node, Object>>();
		children.add(new Pair<Node, Object>(getFileSystem(node), new Object()));
		return children;	
	}
	
	public Node getFileSystem(Node parentode) {
		Node node = new Node();
		node.setId(parentode.getId() + ".fileSystem");
		node.setType("fileSystem");
		return node;
	}

}
