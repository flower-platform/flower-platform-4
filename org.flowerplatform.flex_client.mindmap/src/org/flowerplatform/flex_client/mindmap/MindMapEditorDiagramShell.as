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
package org.flowerplatform.flex_client.mindmap {
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.node.NodeRegistry;
	import org.flowerplatform.flex_client.core.node.controller.GenericValueProviderFromDescriptor;
	import org.flowerplatform.flex_client.core.node.controller.NodeControllerUtils;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDragTool;
	import org.flowerplatform.flexdiagram.mindmap.MindMapRootModelWrapper;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.tool.InplaceEditorTool;
	import org.flowerplatform.flexdiagram.tool.ScrollTool;
	import org.flowerplatform.flexdiagram.tool.SelectOnClickTool;
	import org.flowerplatform.flexdiagram.tool.ZoomTool;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapEditorDiagramShell extends MindMapDiagramShell {

		public var nodeRegistry:NodeRegistry;
				
		public function MindMapEditorDiagramShell() {
			super();
						
			var tools:Array = [ScrollTool, ZoomTool, SelectOnClickTool, MindMapDragTool];
			if (!FlexUtilGlobals.getInstance().isMobile) {
				tools.push(InplaceEditorTool);
			}
			registerTools(tools);				
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
