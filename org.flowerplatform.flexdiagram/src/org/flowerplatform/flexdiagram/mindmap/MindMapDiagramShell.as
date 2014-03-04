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
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelController;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapDiagramShell extends DiagramShell {
		
		public static const POSITION_LEFT:int = -1;
		public static const POSITION_RIGHT:int = 1;
		
		public static const HORIZONTAL_PADDING_DEFAULT:int = 20;
		public static const VERTICAL_PADDING_DEFAULT:int = 5;
		
		public var horizontalPadding:int = HORIZONTAL_PADDING_DEFAULT;
		public var verticalPadding:int = VERTICAL_PADDING_DEFAULT;
		
		/**
		 * Structure:
		 * - rootModel -> keeps in its dynamic object the list of model children added directly on diagram renderer
		 * - root (model from where the mindmap structure begins) -> first child in getDynamicObject(rootModel).children, it has no parent
		 */ 
		override public function set rootModel(value:Object):void {
			super.rootModel = value;
			
			shouldRefreshVisualChildren(getNewDiagramShellContext(), rootModel);
		}
		
		public function getRoot(context:DiagramShellContext):Object {
			var children:IList = ControllerUtils.getModelChildrenController(context, rootModel).getChildren(context, rootModel);
			if (children == null || children.length == 0) {
				throw new Error("No root provided!");
			}
			return children.getItemAt(0);
		}
		
		public function refreshRootModelChildren(context:DiagramShellContext):void {			
			var root:Object = getRoot(context);
			// clear old children
			getDynamicObject(context, rootModel).children = new ArrayList();
			// add new children
			addModelInRootModelChildrenListRecursive(context, root, true);	
			// refresh rootModel's visual children
			shouldRefreshVisualChildren(context, rootModel);
		}
		
		public function addModelInRootModelChildrenList(context:DiagramShellContext, model:Object, asRoot:Boolean = false):void {
			var dynamicObject:Object = getDynamicObject(context, rootModel);
			if (dynamicObject.children == null) {
				dynamicObject.children = new ArrayList();
			}
			if (asRoot) {
				dynamicObject.children.addItemAt(model, 0);
			} else {
				dynamicObject.children.addItem(model);
			}
		}
		
		protected function addModelInRootModelChildrenListRecursive(context:DiagramShellContext, model:Object, asRoot:Boolean = false):void {			
			if (getModelController(context, model).getExpanded(context, model)) {
				var children:IList = getModelController(context, model).getChildren(context, model);
				for (var i:int = 0; i < children.length; i++) {
					addModelInRootModelChildrenListRecursive(context, children.getItemAt(i));
				}
			}
			addModelInRootModelChildrenList(context, model, asRoot);				
		}
		
		public function getModelController(context:DiagramShellContext, model:Object):MindMapModelController {
			return ControllerUtils.getMindMapModelController(context, model);
		}
			
		private function getInitialPropertyValue(context:DiagramShellContext, model:Object, property:String):Object {
			switch (property) {								
				case "width":
					return 10;	// minWidth
				case "height":
				case "oldHeight":
					return 22;	// minHeight							
				case "expandedHeight":
					return getPropertyValue(context, model, "height");
				default:
					return 0;
			}
		}
		
		public function getPropertyValue(context:DiagramShellContext, model:Object, property:String):Number {
			var dynamicObject:Object = getDynamicObject(context, model);
			if (isNaN(dynamicObject[property])) {				
				dynamicObject[property] = getInitialPropertyValue(context, model, property);
			}
			return dynamicObject[property];			
		}
		
		public function setPropertyValue(context:DiagramShellContext, model:Object, property:String, value:Object):void {
			var dynamicObject:Object = getDynamicObject(context, model);
			var oldValue:Number = dynamicObject[property];
			
			dynamicObject[property] = value;
			
			model.dispatchEvent(PropertyChangeEvent.createUpdateEvent(model, property, oldValue, value));
		}
		
		protected function getChildrenBasedOnSide(context:DiagramShellContext, model:Object, side:int = 0):IList {
			if (side == 0) {
				side = getModelController(context, model).getSide(context, model);
			}
			
			var list:ArrayList = new ArrayList();	
			var children:IList = getModelController(context, model).getChildren(context, model);
			if (children != null) {
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children.getItemAt(i);
					if (side == 0 || side == getModelController(context, child).getSide(context, child)) {
						list.addItem(child);
					}
				}
			}
			return list;
		}
		
		public function refreshModelPositions(context:DiagramShellContext, model:Object):void {			
			var oldExpandedHeight:Number = getPropertyValue(context, model, "expandedHeight");
			var oldExpandedHeightLeft:Number = getPropertyValue(context, model, "expandedHeightLeft");		
			var oldExpandedHeightRight:Number = getPropertyValue(context, model, "expandedHeightRight");
			
			var side:int = getModelController(context, model).getSide(context, model);
			var isRoot:Boolean = getModelController(context, model).isRoot(context, model);			
			
			calculateRootExpandedHeight(context, side);
			
			if (isRoot || side == POSITION_LEFT) {						
				if (isRoot) {										
					oldExpandedHeight = oldExpandedHeightLeft;
				}			
				changeCoordinates(context, model, oldExpandedHeight, getPropertyValue(context, model, "expandedHeight"), POSITION_LEFT);
			}
			if (isRoot || side == POSITION_RIGHT) {
				if (isRoot) {								
					oldExpandedHeight = oldExpandedHeightRight;
				}				
				changeCoordinates(context, model, oldExpandedHeight, getPropertyValue(context, model, "expandedHeight"), POSITION_RIGHT);
			}
		}
		
		/**
		 * Recalculates all <code>expandedHeight</code> starting from root model.
		 * This is done only for a specific side (left/right).
		 * <p>
		 * Used when getting delta to re-arrange model siblings.
		 * @see changeCoordinates
		 */ 
		private function calculateRootExpandedHeight(context:DiagramShellContext, side:int):void {
			var model:Object = getRoot(context);
			var isRoot:Boolean = getModelController(context, model).isRoot(context, model);			
			
			if (isRoot || side == POSITION_LEFT) {
				calculateExpandedHeight(context, model, POSITION_LEFT);
				setPropertyValue(context, model, "expandedHeightLeft", getPropertyValue(context, model, "expandedHeight"));				
			}
			if (isRoot || side == POSITION_RIGHT) {
				calculateExpandedHeight(context, model, POSITION_RIGHT);
				setPropertyValue(context, model, "expandedHeightRight", getPropertyValue(context, model, "expandedHeight"));				
			}
		}
		
		private function calculateExpandedHeight(context:DiagramShellContext, model:Object, side:int):Number {			
			var expandedHeight:Number = 0;
			var children:IList = getChildrenBasedOnSide(context, model, side);
			if (getModelController(context, model).getExpanded(context, model)) {				
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children.getItemAt(i);
					var childExpandedHeight:Number = calculateExpandedHeight(context, child, side);
					expandedHeight += childExpandedHeight;					
					if (i < children.length - 1) { // add padding only between children (not after the last one)
						expandedHeight += verticalPadding;						
					}
				}
				setPropertyValue(context, model, "expandedHeight", expandedHeight);				
				expandedHeight = Math.max(expandedHeight, getPropertyValue(context, model, "height"));			
			} else {
				expandedHeight = getPropertyValue(context, model, "height");
				// for collapse models, the expandedHeight must be 0
				setPropertyValue(context, model, "expandedHeight", 0);
			}			
			return expandedHeight;
		}
		
		private function changeCoordinates(context:DiagramShellContext, model:Object, oldExpandedHeight:Number, newExpandedHeight:Number, side:int):void {			
			changeChildrenCoordinates(context, model, side);	
			
			if (newExpandedHeight != 0 && newExpandedHeight < getPropertyValue(context, model, "height")) {
				
			} else {
				var delta:Number = 0;
				if (newExpandedHeight == 0) {
					if (oldExpandedHeight > getPropertyValue(context, model, "height")) {						
						delta = (getPropertyValue(context, model, "height") - oldExpandedHeight)/2;
					}					
				} else if (oldExpandedHeight == 0) {
					delta = (newExpandedHeight - getPropertyValue(context, model, "height"))/2;
				} else {
					delta = (newExpandedHeight - oldExpandedHeight)/2;
				}
				if (delta != 0) {
					changeSiblingsCoordinates(context, model, delta, side);
				}
			}					
			getDynamicObject(context, model).shouldRefreshPosition = false;
		}		
		
		private function changeChildrenCoordinates(context:DiagramShellContext, model:Object, side:int):void {					
			if (getModelController(context, model).getExpanded(context, model)) {
				var children:IList = getChildrenBasedOnSide(context, model, side);				
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children.getItemAt(i);
					if (i == 0) {						
						setPropertyValue(context, child, "y", getPropertyValue(context, model, "y") + getDeltaBetweenExpandedHeightAndHeight(context, child)/2 - getDeltaBetweenExpandedHeightAndHeight(context, model)/2);										
					} else {
						var previousChild:Object = children.getItemAt(i - 1);
						setPropertyValue(context, child, "y", getPropertyValue(context, previousChild, "y") + getModelBottomHeight(context, previousChild) + verticalPadding + getDeltaBetweenExpandedHeightAndHeight(context, child, true)/2);
					}								
					if (getModelController(context, child).getSide(context, child) == POSITION_LEFT) {
						setPropertyValue(context, child, "x", getPropertyValue(context, model, "x") - getPropertyValue(context, child, "width") - horizontalPadding);							
					} else {	
						setPropertyValue(context, child, "x", getPropertyValue(context, model, "x") + getPropertyValue(context, model, "width") + horizontalPadding);															
					}						
					changeChildrenCoordinates(context, child, side);			
				}				
			}
		}
		
		private function changeSiblingsCoordinates(context:DiagramShellContext, model:Object, delta:Number, side:int, onlyBottomSiblings:Boolean = false):void {			
			var parent:Object = ControllerUtils.getModelChildrenController(context, model).getParent(context, model);
			if (parent != null) {				
				var children:IList = getChildrenBasedOnSide(context, parent, side);				
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children.getItemAt(i);
					if (!onlyBottomSiblings && children.getItemIndex(model) > children.getItemIndex(child)) {		
						setPropertyValue(context, child, "y", getPropertyValue(context, child, "y") - delta);										
						changeSiblingChildrenCoordinates(context, child, - delta, side);
					} else if (children.getItemIndex(model) < children.getItemIndex(child)) {
						setPropertyValue(context, child, "y", getPropertyValue(context, child, "y") + delta);								
						changeSiblingChildrenCoordinates(context, child, delta, side);						
					}					
				}
				changeSiblingsCoordinates(context, parent, delta, side, onlyBottomSiblings);
			}			
		}
		
		private function changeSiblingChildrenCoordinates(context:DiagramShellContext, model:Object, delta:Number, side:int):void {
			var children:IList = getChildrenBasedOnSide(context, model, side);				
			for (var i:int = 0; i < children.length; i++) {
				var child:Object = children.getItemAt(i);
				setPropertyValue(context, child, "y", getPropertyValue(context, child, "y") + delta);					
				changeSiblingChildrenCoordinates(context, child, delta, side);
			}
		}
		
		private function getDeltaBetweenExpandedHeightAndHeight(context:DiagramShellContext, model:Object, preventNegativeValues:Boolean = false):Number {
			if (preventNegativeValues && getPropertyValue(context, model, "expandedHeight") < getPropertyValue(context, model, "height")) {
				return 0;
			}
			if (getPropertyValue(context, model, "expandedHeight") != 0) {
				return getPropertyValue(context, model, "expandedHeight") - getPropertyValue(context, model, "height");
			}
			return 0;		
		}
		
		private function getModelBottomHeight(context:DiagramShellContext, model:Object):Number {			
			return Math.max(getPropertyValue(context, model, "expandedHeight"), getPropertyValue(context, model, "height"))/2 + getPropertyValue(context, model, "height")/2;
		}
	}
}