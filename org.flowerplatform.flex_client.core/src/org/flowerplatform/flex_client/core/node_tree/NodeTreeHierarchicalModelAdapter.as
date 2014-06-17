package org.flowerplatform.flex_client.core.node_tree {
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flexutil.tree.IHierarchicalModelAdapter;

	/**
	 * @author Claudiu Matei
	 */
	public class NodeTreeHierarchicalModelAdapter implements IHierarchicalModelAdapter {
		
		public function getChildren(treeNode:Object):IList {
			var node:Node = Node(treeNode);
			if (node.children == null) {
				return new ArrayCollection();
			}
			return node.children;
		}
		
		public function hasChildren(treeNode:Object):Boolean {
			return Node(treeNode).properties[CoreConstants.HAS_CHILDREN];
		}
		
	}
}