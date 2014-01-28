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
package org.flowerplatform.flexdiagram.mindmap {
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.IInvalidating;
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapControllerProvider;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapDiagramShell extends DiagramShell {
		
		public static const NONE:int = 0;
		public static const LEFT:int = -1;
		public static const RIGHT:int = 1;
		
		public static const HORIZONTAL_PADDING_DEFAULT:int = 20;
		public static const VERTICAL_PADDING_DEFAULT:int = 5;
		
		public var horizontalPadding:int = HORIZONTAL_PADDING_DEFAULT;
		public var verticalPadding:int = VERTICAL_PADDING_DEFAULT;
		
		public var diagramChildren:ArrayList = new ArrayList();
		
		public function MindMapDiagramShell() {
			super();			
		}
				
		override public function set rootModel(value:Object):void {
			super.rootModel = value;
					
			refreshDiagramChildren();
			shouldRefreshVisualChildren(rootModel)
		}
		
		public function refreshDiagramChildren():void {
			diagramChildren = new ArrayList();
			var mindMapRoot:Object = IMindMapControllerProvider(getControllerProvider(rootModel)).getMindMapRootController(rootModel).getMindMapRoot();
			if (mindMapRoot != null) {
				addChildren(mindMapRoot);
				shouldRefreshVisualChildren(rootModel);
			}			
		}
				
		public function addChildren(model:Object):void {			
			if (getModelController(model).getExpanded(model)) {
				var children:IList = getModelController(model).getChildren(model);
				for (var i:int = 0; i < children.length; i++) {
					addChildren(children.getItemAt(i));
				}				
			}
			diagramChildren.addItem(model);		
		}
		
		public function getModelController(model:Object):IMindMapModelController {
			return IMindMapControllerProvider(getControllerProvider(model)).getMindMapModelController(model);
		}
		
		private function getDynamicObject(model:Object):Object {
			return DynamicModelExtraInfoController(getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model);
		}
		
		private function getExpandedHeight(model:Object):Number {
			var expandedHeight:Number = getDynamicObject(model).expandedHeight;
			if (isNaN(expandedHeight)) {
				expandedHeight = getModelController(model).getHeight(model);
			}
			return expandedHeight;
		}
		
		private function getExpandedY(model:Object):Number {
			var expandedY:Number = getDynamicObject(model).expandedY;
			if (isNaN(expandedY)) {
				expandedY = model.y;
			}
			return expandedY;
		}
				
		public function refreshNodePositions(model:Object):void {			
			var oldExpandedHeight:Number = getExpandedHeight(model);
			var oldExpandedHeightLeft:Number = getDynamicObject(model).expandedHeightLeft;			
			var oldExpandedHeightRight:Number = getDynamicObject(model).expandedHeightRight;
			
			calculateRootNodeExpandedHeight(getModelController(model).getSide(model));
		
			var side:int = getModelController(model).getSide(model);
			if (side == NONE || side == LEFT) {
				if (side == NONE) {
					getDynamicObject(model).expandedHeight = getDynamicObject(model).expandedHeightLeft;
					oldExpandedHeight = oldExpandedHeightLeft;
				}			
				changeCoordinates(model, oldExpandedHeight, getExpandedHeight(model), side == NONE ? LEFT : side);
			}
			if (side == NONE || side == RIGHT) { 
				if (side == NONE) {
					getDynamicObject(model).expandedHeight = getDynamicObject(model).expandedHeightRight;
					oldExpandedHeight = oldExpandedHeightRight;
				}				
				changeCoordinates(model, oldExpandedHeight, getExpandedHeight(model), side == NONE ? RIGHT : side);
			}
		}
		
		private function calculateRootNodeExpandedHeight(side:int):void {
			var model:Object = IMindMapControllerProvider(getControllerProvider(rootModel)).getMindMapRootController(rootModel).getMindMapRoot();
			if (side == NONE || side == LEFT) { 
				calculateExpandedHeight(model, LEFT);
				getDynamicObject(model).expandedHeightLeft = getExpandedHeight(model);
			}
			if (side == NONE || side == RIGHT) { 
				calculateExpandedHeight(model, RIGHT);
				getDynamicObject(model).expandedHeightRight = getExpandedHeight(model);
			}
		}
		
		private function calculateExpandedHeight(model:Object, side:int):Number {			
			var expandedHeight:Number = 0;
			var children:IList = getModelController(model).getChildrenBasedOnSide(model, side);
			if (getModelController(model).getExpanded(model) && children.length > 0) {
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children.getItemAt(i);
					expandedHeight += calculateExpandedHeight(child, side);
					if (i < children.length - 1) { // add padding only between children (not after the last one)
						expandedHeight += verticalPadding;
					}
				}
			} else {
				expandedHeight = getModelController(model).getHeight(model);				
			}
			getDynamicObject(model).expandedHeight = expandedHeight;
			return expandedHeight;
		}
		
		private function changeCoordinates(model:Object, oldExpandedHeight:Number, newExpandedHeight:Number, side:int):void {			
			getDynamicObject(model).expandedY = getModelController(model).getY(model) - (newExpandedHeight - getModelController(model).getHeight(model))/2;
			
			changeChildrenCoordinates(model, side, true);				
			changeSiblingsCoordinates(model, (newExpandedHeight - oldExpandedHeight)/2, side);
			getDynamicObject(model).shouldRefreshPosition = false;
		}		
		
		private function changeChildrenCoordinates(model:Object, side:int, changeOnlyForChildren:Boolean = false):void {	
			if (!changeOnlyForChildren) {	
				getModelController(model).setY(model, getExpandedY(model) + (getExpandedHeight(model) - getModelController(model).getHeight(model))/2);		
				var parent:Object = getControllerProvider(model).getModelChildrenController(model).getParent(model);
				if (parent != null) {
					if (getModelController(model).getSide(model) == LEFT) {
						getModelController(model).setX(model, getModelController(parent).getX(parent) - getModelController(model).getWidth(model) - horizontalPadding);	
					} else {					
						getModelController(model).setX(model, getModelController(parent).getX(parent) + getModelController(parent).getWidth(parent) + horizontalPadding);				
					}
				}
			} else {
				getDynamicObject(model).expandedY = getModelController(model).getY(model) - (getExpandedHeight(model) - getModelController(model).getHeight(model))/2;		
			}
			if (getModelController(model).getExpanded(model)) {				
				var children:IList = getModelController(model).getChildrenBasedOnSide(model, side);				
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children.getItemAt(i);
					if (i == 0) {
						getDynamicObject(child).expandedY = getExpandedY(model);						
					} else {
						var previousChild:Object = children.getItemAt(i - 1);
						getDynamicObject(child).expandedY = getExpandedY(previousChild) + getExpandedHeight(previousChild) + verticalPadding;
					}					
					changeChildrenCoordinates(child, side);			
				}				
			}
		}
		
		private function changeSiblingsCoordinates(model:Object, diff:Number, side:int):void {
			var parent:Object = getControllerProvider(model).getModelChildrenController(model).getParent(model);
			if (parent != null) {
				var children:IList = getModelController(model).getChildrenBasedOnSide(parent, side);				
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children.getItemAt(i);
					if (children.getItemIndex(model) > children.getItemIndex(child)) {				
						getModelController(child).setY(child, getModelController(child).getY(child) - diff);					
						changeSiblingChildrenCoordinates(child, -diff, side);
					} else if (children.getItemIndex(model) < children.getItemIndex(child)) {
						getModelController(child).setY(child, getModelController(child).getY(child) + diff);					
						changeSiblingChildrenCoordinates(child, diff, side);
					}
				}
				changeSiblingsCoordinates(parent, diff, side);
			}
		}
		
		private function changeSiblingChildrenCoordinates(model:Object, diff:Number, side:int):void {
			var children:IList = getModelController(model).getChildrenBasedOnSide(model, side);				
			for (var i:int = 0; i < children.length; i++) {
				var child:Object = children.getItemAt(i);
				getModelController(child).setY(child, getModelController(child).getY(child) + diff);					
				changeSiblingChildrenCoordinates(child, diff, side);
			}
		}
		
	}
}