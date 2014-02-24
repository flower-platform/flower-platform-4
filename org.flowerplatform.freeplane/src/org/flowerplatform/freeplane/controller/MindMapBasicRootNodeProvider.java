package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.controller.RootNodeProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.mindmap.MindMapPlugin;

/**
 * @author Cristina Constantinescu
 */
public class MindMapBasicRootNodeProvider extends RootNodeProvider {

	@Override
	public Node getRootNode(Node node) {
		return new Node(MindMapPlugin.MINDMAP_NODE_TYPE, node.getResource(), null, null);
	}

}
