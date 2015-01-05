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
package org.flowerplatform.flex_client.core.editor.action {
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	public class UndoAction extends ActionBase {

		public function UndoAction() {
			super();
			label = "Undo";
			icon = Resources.undoIcon;
			orderIndex = 100;
			visible = true;
		}
		
		override public function run():void	{
			var node:Node = Node(selection.getItemAt(0));
			CorePlugin.getInstance().serviceLocator.invoke("resourceService.undo", [node.nodeUri]); 
		}
		
		override public function get visible():Boolean {
			if (selection != null && selection.length == 1 && selection.getItemAt(0) is Node) {
				var type:String = Node(selection.getItemAt(0)).type;
				if (type == CoreConstants.COMMAND_TYPE) return true;
			}
			return false;
		}

		
	}
}