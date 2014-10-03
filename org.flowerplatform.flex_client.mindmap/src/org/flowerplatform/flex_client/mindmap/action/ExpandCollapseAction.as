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
package org.flowerplatform.flex_client.mindmap.action {
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.editor.action.DiagramShellAwareActionBase;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	
	/**
	 * @author Diana Balutoiu
	 */
	public class ExpandCollapseAction extends DiagramShellAwareActionBase {
		
		public static const ID:String = "ExpandCollapseAction";
		
		public function ExpandCollapseAction() {
			super();
		}
		
		override public function get visible():Boolean{
			if (selection == null || selection.length == 0){
				return false;
			}
			var obj:Object = selection.getItemAt(0);
			if (!(obj is Node) || Node(obj).parent == null) {
				return false;
			}
			return Node(obj).getPropertyValue(CoreConstants.HAS_CHILDREN);
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			context.diagramShell.getModelController(context, node).setExpanded(
				context, 
				node, 
				!context.diagramShell.getModelController(context, node).getExpanded(context, node));
		}	
		
	}
}