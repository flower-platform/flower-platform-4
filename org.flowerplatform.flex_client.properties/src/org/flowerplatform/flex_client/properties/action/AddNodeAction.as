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
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.AddChildDescriptor;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.properties.CreateNodeView;
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristina Constantinescu
	 * @author Mariana Gheorghe
	 */
	public class AddNodeAction extends ActionBase {
			
		public var childType:String;
		
		public function AddNodeAction(descriptor:AddChildDescriptor = null)	{
			super();
			
			childType = descriptor.childType;
				
			label = descriptor.label;
			icon = descriptor.icon;
			orderIndex = descriptor.orderIndex;
			parentId = NewComposedAction.ACTION_ID_NEW;
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
				.getExpectedTypeDescriptor(childType).getAdditiveControllers(CoreConstants.PROPERTY_DESCRIPTOR, null);
			
			var hasContributesToCreationDescriptors:Boolean = false;
			for ( var i:int = 0; i < propertyDescriptors.length; i++ ) {
				if (PropertyDescriptor(propertyDescriptors.getItemAt(i)).contributesToCreation) {
					hasContributesToCreationDescriptors = true;
					break;
				}
			}
			
			if (hasContributesToCreationDescriptors) {
				var createNodeView:CreateNodeView = new CreateNodeView();
				createNodeView.parentNode = parentNode;
				createNodeView.nodeType = childType;
				FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
					.setTitle(Resources.getMessage("new.file.folder"))
					.setViewContent(createNodeView)
					.show();
			} else {
				CorePlugin.getInstance().serviceLocator.invoke("nodeService.addChild", [parent.fullNodeId, properties, null]);
			}
		}
		
	}
}
