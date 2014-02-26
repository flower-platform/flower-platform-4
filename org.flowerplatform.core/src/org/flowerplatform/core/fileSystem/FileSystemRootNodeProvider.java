package org.flowerplatform.core.fileSystem;

import org.flowerplatform.core.node.controller.RootNodeProvider;
import org.flowerplatform.core.node.remote.Node;

public class FileSystemRootNodeProvider extends RootNodeProvider {

	@Override
	public Node getRootNode(Node node) {
		return new Node("Folder|mm://path_to_resource|ID_85319927");
	}

}
