package org.flowerplatform.core.file;

import org.flowerplatform.core.node.controller.RootNodeProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class FileRootNodeProvider extends RootNodeProvider {

	@Override
	public Node getRootNode(Node node) {
		return new Node("Folder|mm://path_to_resource|ID_85319927");
	}
}
