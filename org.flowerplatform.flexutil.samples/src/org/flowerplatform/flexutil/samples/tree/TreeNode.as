/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.flexutil.samples.tree {
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	import mx.utils.UIDUtil;
	
	import org.flowerplatform.flexutil.tree.TreeList;
	
	[Bindable]
	public class TreeNode {
		
		[Embed(source="/defaultIcon.gif")]			
		public static const INFO:Class;
		
		public var id:String;
		
		private var _name:String;
		
		public var image:Object;
		
		public var parent:TreeNode;
		public var children:ArrayCollection;
		
		private var _hasChildren:Boolean;
				
		public function TreeNode(name:String = '') {
			id = name;
			this.name = name;
			this.children = new ArrayCollection();
			this.image = INFO;
		}
		
		public function get name():String
		{
			return _name;
		}

		public function set name(value:String):void
		{
			if( _name !== value)
			{
				_name = value;
				dispatchEvent(new Event(TreeList.UPDATE_TREE_RENDERER_EVENT));
			}
		}

		public function get hasChildren():Boolean
		{
			return _hasChildren;
		}

		public function set hasChildren(value:Boolean):void
		{
			if( _hasChildren !== value)
			{
				_hasChildren = value;
				dispatchEvent(new Event(TreeList.UPDATE_TREE_RENDERER_EVENT));
			}
		}

		public static function getNodeByPath(path:ArrayCollection, parent:TreeNode = null):TreeNode {							
			if (path == null) {
				return parent;
			}			
			var node:TreeNode = parent;			
			for each (var id:String in path) {				
				parent = node;
				node = null;
				for each (var child:TreeNode in parent.children) {
					if (child.id == id) {
						node = child;
						break;
					}
				}
				if (node == null) {
					return null;
				}
			}
			return node;			
		}	
		
		public function getPathForNode():ArrayCollection {
			var node:TreeNode = this;
			if (node == null || node.parent == null) {
				return null;
			}
			var path:ArrayCollection = new ArrayCollection();
			while (node.parent != null) {        		
				path.addItemAt(node.id, 0);
				node = node.parent;
			}			
			return path;
		}
	}
}