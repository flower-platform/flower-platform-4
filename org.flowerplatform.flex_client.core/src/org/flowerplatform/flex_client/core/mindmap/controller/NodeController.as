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
package org.flowerplatform.flex_client.core.mindmap.controller {
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.events.PropertyChangeEvent;
	import mx.rpc.events.ResultEvent;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.Diagram;
	import org.flowerplatform.flex_client.core.mindmap.MindMapEditorDiagramShell;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NodeController extends ControllerBase implements IMindMapModelController {
		
		public function NodeController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		private function get mindMapDiagramShell():MindMapEditorDiagramShell {
			return MindMapEditorDiagramShell(diagramShell);
		}
		
		public function getChildren(model:Object):IList {
			return Node(model).children;
		}
		
		public function getChildrenBasedOnSide(model:Object, side:int = 0):IList /* of Node */ {
			if (side == 0) {
				side = getSide(model);
			}
			
			var list:ArrayList = new ArrayList();	
			if (getChildren(model) != null) {
				for (var i:int = 0; i < getChildren(model).length; i++) {
					var child:Node = Node(getChildren(model).getItemAt(i));
					if (side == 0 || side == getSide(child)) {
						list.addItem(child);
					}
				}
			}
			return list;
		}
						
		public function getX(model:Object):Number {
			if (getDynamicObject(model).x == null) {
				getDynamicObject(model).x = 0;
			}
			return getDynamicObject(model).x;			
		}
		
		public function setX(model:Object, value:Number):void {	
			var oldValue:Number = getDynamicObject(model).x;
			
			getDynamicObject(model).x = value;
			
			model.dispatchEvent(PropertyChangeEvent.createUpdateEvent(this, "x", oldValue, value));
		}
		
		public function getY(model:Object):Number {
			if (getDynamicObject(model).y == null) {
				getDynamicObject(model).y = 0;
			}
			return getDynamicObject(model).y;			
		}
		
		public function setY(model:Object, value:Number):void {	
			var oldValue:Number = getDynamicObject(model).y;
			
			getDynamicObject(model).y = value;	
			
			model.dispatchEvent(PropertyChangeEvent.createUpdateEvent(this, "y", oldValue, value));
		}
		
		public function getWidth(model:Object):Number {	
			if (getDynamicObject(model).width == null) {
				getDynamicObject(model).width = 10;
			}
			return getDynamicObject(model).width;
		}
		
		public function setWidth(model:Object, value:Number):void {	
			var oldValue:Number = getDynamicObject(model).width;
			
			getDynamicObject(model).width = value;
			
			model.dispatchEvent(PropertyChangeEvent.createUpdateEvent(this, "width", oldValue, value));
		}
		
		public function getHeight(model:Object):Number {
			if (getDynamicObject(model).height == null) {
				getDynamicObject(model).height = 10;
			}
			return getDynamicObject(model).height;			
		}
		
		public function setHeight(model:Object, value:Number):void {
			var oldValue:Number = getDynamicObject(model).height;
			
			getDynamicObject(model).height = value;
			
			model.dispatchEvent(PropertyChangeEvent.createUpdateEvent(this, "height", oldValue, value));
		}
		
		public function getExpanded(model:Object):Boolean {
			return Node(model).children != null && Node(model).children.length > 0;
		}
		
		public function setExpanded(model:Object, value:Boolean):void {
			if (value) {
				MindMapEditorDiagramShell(diagramShell).updateProcessor.requestChildren(Node(model));
			} else {				
				MindMapEditorDiagramShell(diagramShell).updateProcessor.removeChildren(Node(model));
			}		
		}
		
		public function getSide(model:Object):int {		
			return Node(model).side;
		}
		
		public function setSide(model:Object, value:int):void {
			Node(model).side = value;
		}
		
		private function getDynamicObject(model:Object):Object {
			return DynamicModelExtraInfoController(diagramShell.getControllerProvider(model)
				.getModelExtraInfoController(model)).getDynamicObject(model);
		}
			
	}
}