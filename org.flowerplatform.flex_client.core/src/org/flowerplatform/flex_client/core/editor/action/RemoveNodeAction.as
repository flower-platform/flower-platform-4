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
package org.flowerplatform.flex_client.core.editor.action {
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class RemoveNodeAction extends ActionBase {
		
		public function RemoveNodeAction() {
			super();
			label = Resources.getMessage("action.remove");	
			icon = Resources.deleteIcon;
			orderIndex = 50;
		}
		
		override public function get visible():Boolean {
			if (selection != null && selection.length == 1 && selection.getItemAt(0) is Node) {
				var type:String = Node(selection.getItemAt(0)).type;
				if (type == CoreConstants.ROOT_TYPE ||
					type == CoreConstants.REPOSITORY_TYPE ||
					type == CoreConstants.FILE_SYSTEM_NODE_TYPE ||
					type == CoreConstants.CODE_TYPE) {
					
					return false;
				}
				return true;
			}
			return false;
		}
		
		override public function run():void {
			CorePlugin.getInstance().serviceLocator.invoke("nodeService.removeChild", [Node(selection.getItemAt(0)).parent.fullNodeId, Node(selection.getItemAt(0)).fullNodeId]);
		}
	}
}