package org.flowerplatform.freeplane.style.controller;

import static org.flowerplatform.freeplane.FreeplanePlugin.*;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class StyleChildrenProvider extends ChildrenProvider {

	@Override
	public List<Node> getChildren(Node node) {
		List<Node> list = new ArrayList<>(); 
		Node styleNode = new Node(STYLE_ROOT_NODE ,null, STYLE_ROOT_NODE, null);
		list.add(styleNode);
 		return list;
	}

	@Override
	public boolean hasChildren(Node node) {
		return true;
	}

}
