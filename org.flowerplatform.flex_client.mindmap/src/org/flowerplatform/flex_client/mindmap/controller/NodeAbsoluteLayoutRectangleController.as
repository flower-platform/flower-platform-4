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

package org.flowerplatform.flex_client.mindmap.controller  {
	import flash.geom.Rectangle;
	
	import org.flowerplatform.flex_client.mindmap.renderer.MindMapNodeWithDetailsRenderer;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapAbsoluteLayoutRectangleController;
	
	/**
	 * @author Sebastian Solomon
	 */
	public class NodeAbsoluteLayoutRectangleController extends MindMapAbsoluteLayoutRectangleController {
		
		public function NodeAbsoluteLayoutRectangleController() {
			super(orderIndex);
		}
		
		override public function getBounds(context:DiagramShellContext, model:Object):Rectangle {
			var diagramShell:MindMapDiagramShell = MindMapDiagramShell(context.diagramShell);
			var dynamicObject:Object = diagramShell.getDynamicObject(context, model);
			var nodeRenderer:Object = dynamicObject.renderer;
			
			if (nodeRenderer != null && nodeRenderer is MindMapNodeWithDetailsRenderer && MindMapNodeWithDetailsRenderer(nodeRenderer).horizontalLine.visible) {
				return new Rectangle(
					diagramShell.getPropertyValue(context, model, "x"), 
					diagramShell.getPropertyValue(context, model, "y"), 
					diagramShell.getPropertyValue(context, model, "width"), 
					nodeRenderer.horizontalLine.y);
			} else {
				return super.getBounds(context, model);
			}
		}
	}
}