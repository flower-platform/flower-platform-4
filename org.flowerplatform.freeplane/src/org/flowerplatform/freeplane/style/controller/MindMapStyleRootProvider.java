package org.flowerplatform.freeplane.style.controller;

import org.flowerplatform.core.node.controller.RootNodeProvider;
import org.flowerplatform.core.node.remote.Node;

public class MindMapStyleRootProvider extends RootNodeProvider {

	@Override
	public Node getRootNode(Node node) {
		//TODO
		return new Node("Folder|mm://path_to_resource|ID_85319927");
	}
	
}
