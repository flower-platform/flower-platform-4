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
package org.flowerplatform.flex_client.properties.action {
	
	import mx.collections.IList;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.AddChildDescriptor;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.properties.CreateNodeView;
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;
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
				label = CorePlugin.getInstance().getMessage("action.add");	
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
		
		/**
		 * @author Cristina Constantinescu
		 * @author Sebastian Solomon
		 */
		override public function run():void {
			var parent:Node = Node(selection.getItemAt(0));
			
			var properties:Object = new Object();
			properties.type = childType;
			
			var parentNode:Node = Node(selection.getItemAt(0));
			
			var propertyDescriptors:IList = CorePlugin.getInstance().nodeTypeDescriptorRegistry
				.getExpectedTypeDescriptor(childType).getAdditiveControllers("propertyDescriptor", null);
			
			var hasContributingDescriptors:Boolean = false;
			for ( var i:int = 0; i < propertyDescriptors.length; i++ ) {
				if (PropertyDescriptor(propertyDescriptors.getItemAt(i)).contributeToCreation) {
					hasContributingDescriptors = true;
					break;
				}
			}
			
			if (hasContributingDescriptors) {
				var createNodeView:CreateNodeView = new CreateNodeView();
				createNodeView.setParentNode(parentNode);
				createNodeView.nodeType = childType;
				// TODO take this value from a property.
				createNodeView.option = CreateNodeView.SHOW_CONTRIBUTING_TO_CREATION;
				FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
					.setTitle("New file/folder")
					.setViewContent(createNodeView)
					.show();
			} else {
				CorePlugin.getInstance().serviceLocator.invoke("nodeService.addChild", [parent.fullNodeId, properties, null]);
			}
		}
		
	}
}
