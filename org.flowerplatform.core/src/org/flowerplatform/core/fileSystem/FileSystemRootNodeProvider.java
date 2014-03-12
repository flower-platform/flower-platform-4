package org.flowerplatform.core.fileSystem;

import org.flowerplatform.core.node.controller.RootNodeProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class FileSystemRootNodeProvider extends RootNodeProvider {

	@Override
	public Node getRootNode(Node node) {
		return new Node("root2||2");
	}

}
