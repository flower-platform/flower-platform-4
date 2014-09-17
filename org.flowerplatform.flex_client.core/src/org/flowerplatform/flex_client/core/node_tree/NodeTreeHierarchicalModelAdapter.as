/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
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