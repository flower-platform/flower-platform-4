/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flex_client.mindmap {
	
	import org.flowerplatform.flex_client.core.editor.action.OpenAction;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.node.INodeChangeListener;
	import org.flowerplatform.flex_client.core.node.NodeRegistry;
	import org.flowerplatform.flex_client.core.node.controller.GenericValueProviderFromDescriptor;
	import org.flowerplatform.flex_client.core.node.controller.NodeControllerUtils;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDragTool;
	import org.flowerplatform.flexdiagram.mindmap.MindMapRootModelWrapper;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.tool.ActionTool;
	import org.flowerplatform.flexdiagram.tool.ScrollTool;
	import org.flowerplatform.flexdiagram.tool.SelectOnClickTool;
	import org.flowerplatform.flexdiagram.tool.WakeUpTool;
	import org.flowerplatform.flexdiagram.tool.ZoomTool;
	import org.flowerplatform.flexutil.FactoryWithInitialization;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapEditorDiagramShell extends MindMapDiagramShell implements INodeChangeListener {

		private var _nodeRegistry:NodeRegistry;
				
		public function MindMapEditorDiagramShell() {
			super();
									
			registerTool(ScrollTool.ID, new FactoryWithInitialization(ScrollTool));
			registerTool(ZoomTool.ID, new FactoryWithInitialization(ZoomTool));
			registerTool(SelectOnClickTool.ID, new FactoryWithInitialization(SelectOnClickTool));
			registerTool(MindMapDragTool.ID, new FactoryWithInitialization(MindMapDragTool));
			registerTool(ActionTool.ID, new FactoryWithInitialization(ActionTool, {"action": new OpenAction(), "eventType": WakeUpTool.DOUBLE_CLICK}));		
		}
			
		public function get nodeRegistry():NodeRegistry {
			return _nodeRegistry;
		}

		public function set nodeRegistry(value:NodeRegistry):void {
			_nodeRegistry = value;
			_nodeRegistry.addNodeChangeListener(this);
		}
		
		public function nodeAdded(node:Node):void {
			if (rootModel != null) {
				refreshRootModelChildren(getNewDiagramShellContext());
			}
		}
		
		public function nodeRemoved(node:Node):void {
			unassociateModelFromRenderer(getNewDiagramShellContext(), node, getRendererForModel(getNewDiagramShellContext(), node), true);	
			
			if (rootModel != null) {
				refreshRootModelChildren(getNewDiagramShellContext());				
			}
		}
		
		public function nodeUpdated(node:Node, property:String, oldValue:Object, newValue:Object):void {
			// do nothing
		}
						
		override public function getRootNodeX(context:DiagramShellContext, rootNode:Object):Number {
			var rootModel:Node = Node(MindMapRootModelWrapper(rootModel).model);
			var sideProvider:GenericValueProviderFromDescriptor = NodeControllerUtils.getValueProvider(
				MindMapEditorDiagramShell(context.diagramShell).registry, rootModel, MindMapConstants.NODE_SIDE_PROVIDER);
			if (sideProvider != null) {
				return (DiagramRenderer(diagramRenderer).width - getPropertyValue(context, rootNode, "width"))/2; // horizontal align = center
			}
			return (DiagramRenderer(diagramRenderer).width - getPropertyValue(context, rootNode, "width"))/8; // horizontal align = left, but not 0
		}
		
		override public function getRootNodeY(context:DiagramShellContext, rootNode:Object):Number {
			return (DiagramRenderer(diagramRenderer).height - getPropertyValue(context, rootNode, "height"))/2; // vertical align = middle
		}
	
	}
}
