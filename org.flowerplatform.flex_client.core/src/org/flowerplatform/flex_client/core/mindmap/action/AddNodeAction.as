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
	import org.flowerplatform.flex_client.core.mindmap.CreateFileDialogView;
	import org.flowerplatform.flex_client.core.mindmap.remote.AddChildDescriptor;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
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
			var properties:Object = new Object();
			properties.type = childType;
			// TODO CC: temporary code
			properties.resource = "mm://path_to_resource";
			
			var child:Node = new Node();
			child.type = "javaParameter";
			if (Node(selection.getItemAt(0)).type != "fileNode" && 
				Node(selection.getItemAt(0)).type != "fileSystem") { 
				CorePlugin.getInstance().serviceLocator.invoke("nodeService.addChild", [Node(selection.getItemAt(0)).fullNodeId, properties, null]);
			} else {
				var view:CreateFileDialogView = new CreateFileDialogView();
				view.setParentNode(Node(selection.getItemAt(0)));
				FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
					.setTitle("New file/directory")
					.setWidth(300)
					.setHeight(150)
					.setViewContent(view)
					.show();
			}
		}
		
	}
}
