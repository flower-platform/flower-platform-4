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
package org.flowerplatform.flex_client.properties.action {
	
	import org.flowerplatform.flex_client.core.editor.action.DiagramShellAwareActionBase;
	import org.flowerplatform.flex_client.core.editor.remote.AddChildDescriptor;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.properties2.PropertiesView;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	/**
	 * @author Cristina Constantinescu
	 * @author Mariana Gheorghe
	 */
	public class AddNodeAction extends DiagramShellAwareActionBase {
			
		public static const ID:String = "org.flowerplatform.flex_client.properties.action.AddNodeAction";
		
		public var childType:String;
		
		public var dynamicChildType:String;
		
		public var siblingNodeUri:String;
		
		public var parentNode:Node;
		
		public function AddNodeAction(descriptor:AddChildDescriptor, siblingNodeUri:String = null) {
			super();
			
			if (descriptor != null) {
				childType = descriptor.childType;
				dynamicChildType = descriptor.dynamicChildType;
				label = descriptor.label;
				icon = descriptor.icon;
				orderIndex = descriptor.orderIndex;
			}
			
			this.siblingNodeUri = siblingNodeUri;
			this.parentId = siblingNodeUri != null ? NewSiblingComposedAction.ID : NewComposedAction.ID;
		}
		
		override public function get visible():Boolean {
			if (siblingNodeUri != null) {
				parentNode = Node(selection.getItemAt(0)).parent;
			} else {
				parentNode = Node(selection.getItemAt(0));
			}
			return selection != null && selection.length == 1 && selection.getItemAt(0) is Node;
		}
		
		/**
		 * @author Cristina Constantinescu
		 * @author Sebastian Solomon
		 */
		override public function run():void {
			var nodeToAdd:Node = new Node();
			nodeToAdd.type = childType;
			if (dynamicChildType != null) {
				nodeToAdd.properties.template = dynamicChildType; // TODO rename this property?
			}
			var propertiesView:PropertiesView = new PropertiesView();
			propertiesView.diagramShellContext = diagramShellContext;
			propertiesView.nodeToAdd = nodeToAdd;
			propertiesView.siblingNodeUri = siblingNodeUri;
			propertiesView.parentNode = parentNode;
			
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setTitle(Resources.getMessage("action.new.label", [label]))
				.setIcon(Resources.addIcon)
				.setWidth(500)
				.setHeight(350)
				.setViewContent(propertiesView)
				.show(false);
		}
	}
}
