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
package org.flowerplatform.flex_client.core.mindmap.action {
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.remote.AddChildDescriptor;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flexutil.action.ComposedAction;
	
	/**
	 * @author Cristina Constantinescu
	 * @author Mariana Gheorghe
	 */
	public class AddNodeAction extends ComposedAction {
		
		public const ACTION_ID_NEW:String = "new";
		
		public var childType:String;
		
		public function AddNodeAction(descriptor:AddChildDescriptor = null)	{
			super();
			if (descriptor == null) {
				label = CorePlugin.getInstance().getMessage("mindmap.action.add");	
				icon = CorePlugin.getInstance().getResourceUrl("images/add.png");
				orderIndex = 10;
				id = ACTION_ID_NEW;
				
				actAsNormalAction = false;
			} else {
				childType = descriptor.childType;
				
				label = descriptor.label;
				icon = descriptor.icon;
				orderIndex = descriptor.orderIndex;
				parentId = ACTION_ID_NEW;
				
				actAsNormalAction = true;
			}
		}
		
		override public function get visible():Boolean {			
			return selection != null && selection.length == 1 && selection.getItemAt(0) is Node;
		}
		
		override public function run():void {
			var child:Node = new Node();
			child.type = childType;
			// TODO CC: temporary code
			child.resource = "mm://path_to_resource";
			
			CorePlugin.getInstance().serviceLocator.invoke("nodeService.addChild", [Node(selection.getItemAt(0)), child, null]);		
		}
		
	}
}
