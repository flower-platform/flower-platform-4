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
package org.flowerplatform.flex_client.mindmap {
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.action.OpenAction;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.mindmap.action.ExpandCollapseAction;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.FlexDiagramConstants;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDragTool;
	import org.flowerplatform.flexdiagram.mindmap.MindMapRootModelWrapper;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.tool.ActionTool;
	import org.flowerplatform.flexdiagram.tool.ScrollTool;
	import org.flowerplatform.flexdiagram.tool.SelectOnClickTool;
	import org.flowerplatform.flexdiagram.tool.WakeUpTool;
	import org.flowerplatform.flexdiagram.tool.ZoomTool;
	import org.flowerplatform.flexutil.ClassFactoryWithConstructor;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	import org.flowerplatform.flexutil.controller.ValuesProvider;
	import org.flowerplatform.js_client.common_js_as.node.INodeChangeListener;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapEditorDiagramShell extends MindMapDiagramShell implements INodeChangeListener {

		private var _nodeRegistry:*;
		
		public var editorFrontend:MindMapEditorFrontend;
				
		public function MindMapEditorDiagramShell() {
			super();
									
			registerTool(ScrollTool.ID, new ClassFactoryWithConstructor(ScrollTool));
			registerTool(ZoomTool.ID, new ClassFactoryWithConstructor(ZoomTool));
			registerTool(SelectOnClickTool.ID, new ClassFactoryWithConstructor(SelectOnClickTool));
			registerTool(MindMapDragTool.ID, new ClassFactoryWithConstructor(MindMapDragTool));
			registerTool(OpenAction.ID, new ClassFactoryWithConstructor(ActionTool, {"action": new OpenAction(null, null), "eventType": WakeUpTool.DOUBLE_CLICK}));		
			registerTool(ExpandCollapseAction.ID, new ClassFactoryWithConstructor(ActionTool, {"action": new ExpandCollapseAction(), "eventType": WakeUpTool.CLICK}));
		}
			
		public function get nodeRegistry():* {
			return _nodeRegistry;
		}

		public function set nodeRegistry(value:*):void {
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
			if (property == CoreConstants.IS_DIRTY) {
				CorePlugin.getInstance().resourceNodesManager.updateGlobalDirtyState(newValue);
			}
		}
				
		/**
		 * @author Cristian Spiescu
		 * @see NodeController.getSide()
		 */
		override public function getRootNodeX(context:DiagramShellContext, rootNode:Object):Number {
			var rootModel:Node = Node(MindMapRootModelWrapper(rootModel).model);
			var registry:TypeDescriptorRegistry = context.diagramShell.getRegistryForModel(rootModel);
			var valuesProvider:ValuesProvider = CorePlugin.getInstance().getNodeValuesProviderForMindMap(registry, rootModel);
			var sideProperty:String = valuesProvider.getPropertyName(registry, rootModel, FlexDiagramConstants.MIND_MAP_RENDERER_SIDE); 

			if (sideProperty != null) {
				return (DiagramRenderer(diagramRenderer).width - getPropertyValue(context, rootNode, "width")) / 2; // horizontal align = center
			}
			return (DiagramRenderer(diagramRenderer).width - getPropertyValue(context, rootNode, "width")) / 8; // horizontal align = left, but not 0
		}
		
		override public function getRootNodeY(context:DiagramShellContext, rootNode:Object):Number {
			return (DiagramRenderer(diagramRenderer).height - getPropertyValue(context, rootNode, "height"))/2; // vertical align = middle
		}
	
	}
}
