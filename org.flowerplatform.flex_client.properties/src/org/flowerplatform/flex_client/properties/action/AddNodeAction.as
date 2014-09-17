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
	
	import mx.collections.IList;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.action.DiagramShellAwareActionBase;
	import org.flowerplatform.flex_client.core.editor.remote.AddChildDescriptor;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.node.remote.ServiceContext;
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;
	import org.flowerplatform.flex_client.properties.ui.CreateNodeView;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	/**
	 * @author Cristina Constantinescu
	 * @author Mariana Gheorghe
	 */
	public class AddNodeAction extends DiagramShellAwareActionBase {
			
		public static const ID:String = "org.flowerplatform.flex_client.properties.action.AddNodeAction";
		
		public var childType:String;
		
		public var asSibling:Boolean;
		
		public var siblingNodeUri:String;
		
		public function AddNodeAction(descriptor:AddChildDescriptor = null, asSibling:Boolean = false, childNodeUri:String = null) {
			super();
			
			childType = descriptor.childType;
					
			label = descriptor.label;
			icon = descriptor.icon;
			orderIndex = descriptor.orderIndex;
			
			this.asSibling = asSibling;
			this.siblingNodeUri = childNodeUri;
			this.parentId = asSibling ? NewSiblingComposedAction.ID : NewComposedAction.ID; 
		}
		
		override public function get visible():Boolean {			
			return selection != null && selection.length == 1 && selection.getItemAt(0) is Node;
		}
		
		/**
		 * @author Cristina Constantinescu
		 * @author Sebastian Solomon
		 */
		override public function run():void {
			var context:ServiceContext = new ServiceContext();
			context.add("type", childType);
			
			var parentNode:Node;
			if (asSibling == true) {
				context.add(CoreConstants.INSERT_BEFORE_FULL_NODE_ID, siblingNodeUri);
				parentNode = Node(selection.getItemAt(0)).parent;
			} else {
				parentNode = Node(selection.getItemAt(0));
			}
			
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
				createNodeView.diagramShellContext = diagramShellContext;
				createNodeView.siblingNodeUri = siblingNodeUri;
				
				FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
					.setTitle(Resources.getMessage("action.new.label", [label]))
					.setIcon(Resources.addIcon)
					.setViewContent(createNodeView)
					.setHeight(200)
					.setWidth(400)
					.show();
			} else {
				CorePlugin.getInstance().serviceLocator.invoke("nodeService.addChild", [parentNode.nodeUri, context], 
					function(childFullNodeId:String):void {
						// expand parentNode, select the added child.
						if (!ControllerUtils.getMindMapModelController(diagramShellContext, parentNode).getExpanded(diagramShellContext, parentNode)) {
							diagramShellContext[CoreConstants.HANDLER] = function():void {CorePlugin.getInstance().selectNode(diagramShellContext, childFullNodeId);};
							MindMapDiagramShell(diagramShellContext.diagramShell).getModelController(diagramShellContext, parentNode).setExpanded(diagramShellContext, parentNode, true);
						}else {
							CorePlugin.getInstance().selectNode(diagramShellContext, childFullNodeId);
						}
					});
			}
		}
	}
}
