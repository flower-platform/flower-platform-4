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
		
		public static const ADDITIONAL_PADDING_DEFAULT:int = 15;
		
		public var horizontalPadding:int = HORIZONTAL_PADDING_DEFAULT;
		public var verticalPadding:int = VERTICAL_PADDING_DEFAULT;
		
		/**
		 * Additional padding value (left/right/top/bottom) for nodes.
		 * 
		 * <p>
		 * When set in model's dynamic object, it is used to calculate model children & siblings coordinates based on it.
		 * (e.g. mindmap cloud shape)
		 */ 
		public var additionalPadding:int = ADDITIONAL_PADDING_DEFAULT;
		
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
		
		public function addModelInRootModelChildrenList(context:DiagramShellContext, model:Object, asRoot:Boolean = false, depth:int = 0):void {
			// set depth in model's dynamic object -> it will be set further, in renderer
			var modelDynamicObject:Object = getDynamicObject(context, model);
			modelDynamicObject.depth = depth;
			
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
		
		protected function addModelInRootModelChildrenListRecursive(context:DiagramShellContext, model:Object, asRoot:Boolean = false, depth:int = 0):void {			
			if (getModelController(context, model).getExpanded(context, model)) {
				var children:IList = getModelController(context, model).getChildren(context, model);
				for (var i:int = 0; i < children.length; i++) {
					addModelInRootModelChildrenListRecursive(context, children.getItemAt(i), false, depth + 1);
				}
			}
			addModelInRootModelChildrenList(context, model, asRoot, depth);				
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
				case "expandedWidth":
					return getPropertyValue(context, model, "width");
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
			
			calculateRootExpandedWidthAndHeight(context, side);
			
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
		private function calculateRootExpandedWidthAndHeight(context:DiagramShellContext, side:int):void {
			var model:Object = getRoot(context);
			var isRoot:Boolean = getModelController(context, model).isRoot(context, model);			
			
			if (isRoot || side == POSITION_LEFT) {
				calculateExpandedWidthAndHeight(context, model, POSITION_LEFT);
				setPropertyValue(context, model, "expandedHeightLeft", getPropertyValue(context, model, "expandedHeight"));				
			}
			if (isRoot || side == POSITION_RIGHT) {
				calculateExpandedWidthAndHeight(context, model, POSITION_RIGHT);
				setPropertyValue(context, model, "expandedHeightRight", getPropertyValue(context, model, "expandedHeight"));				
			}
		}
				
		private function calculateExpandedWidthAndHeight(context:DiagramShellContext, model:Object, side:int):Array {
			var expandedWidth:Number = 0;
			var expandedHeight:Number = 0;
			var children:IList = getChildrenBasedOnSide(context, model, side);
			if (getModelController(context, model).getExpanded(context, model)) {				
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children.getItemAt(i);
					
					var arr:Array = calculateExpandedWidthAndHeight(context, child, side);
					
					var childExpandedWidth:Number = arr[0];
					expandedWidth = Math.max(expandedWidth, childExpandedWidth);
					
					var childExpandedHeight:Number = arr[1];
					expandedHeight += childExpandedHeight;					
					if (i < children.length - 1) { // add padding only between children (not after the last one)
						expandedHeight += verticalPadding;						
					}					
				}
				expandedWidth += getPropertyValue(context, model, "width") + horizontalPadding;
				setPropertyValue(context, model, "expandedWidth", expandedWidth);
				// add additional padding here -> used only to calculate parent's expandedWidth
				expandedWidth += getPropertyValue(context, model, "additionalPadding")/2;
				
				expandedHeight += getPropertyValue(context, model, "additionalPadding");
				setPropertyValue(context, model, "expandedHeight", expandedHeight);				
				expandedHeight = Math.max(expandedHeight, getPropertyValue(context, model, "height"));				
			} else {
				expandedWidth = getPropertyValue(context, model, "width");				
				setPropertyValue(context, model, "expandedWidth", expandedWidth);
				// add additional padding here -> used only for return result to calculate parent's expandedWidth
				expandedWidth += getPropertyValue(context, model, "additionalPadding")/2;
				
				expandedHeight = getPropertyValue(context, model, "height") + getPropertyValue(context, model, "additionalPadding");
				
				// for collapse models, the expandedHeight must be 0
				setPropertyValue(context, model, "expandedHeight", 0);
			}
			return [expandedWidth, expandedHeight];
		}
		
		private function changeCoordinates(context:DiagramShellContext, model:Object, oldExpandedHeight:Number, newExpandedHeight:Number, side:int):void {			
			changeChildrenCoordinates(context, model, side);	
			
			var additionalPadding:Number = getPropertyValue(context, model, "additionalPadding");
			var height:Number = getPropertyValue(context, model, "height");
			
			var oldExpandedHeightWithoutAdditionalPadding:Number = oldExpandedHeight - additionalPadding;
			var newExpandedHeightWithoutAdditionalPadding:Number = newExpandedHeight - additionalPadding;
			
			var delta:Number = 0;
			if (newExpandedHeight != 0 && newExpandedHeightWithoutAdditionalPadding < height) {
				// do nothing
			} else {				
				if (newExpandedHeight == 0) { // model collapsed
					if (oldExpandedHeightWithoutAdditionalPadding > height) { 
						// old expandedHeight was greater than height -> get delta between those two (value is negative)						
						delta = (height - oldExpandedHeightWithoutAdditionalPadding)/2 ;
					} else if (oldExpandedHeightWithoutAdditionalPadding < height) { 
						// old expandedHeight was smaller than height -> use additional padding to calculate delta	
						delta = additionalPadding/2;
					} else { // oldExpandedHeightWithoutAdditionalPadding == height
						// do nothing
					}
				} else if (oldExpandedHeight == 0) { 
					// model expanded -> get delta between expandedHeight (without additional padding because it must propagate to its height) and height (value is positive)
					delta = (newExpandedHeightWithoutAdditionalPadding - height)/2;
				} else { 
					// model changed its expandedHeight -> get delta between old and new expandedHeights
					delta = (newExpandedHeight - oldExpandedHeight)/2;
				}
			}	
		
			if (delta != 0) { // delta changed, update siblings
				changeSiblingsCoordinates(context, model, delta, side);
			}
		}		
		
		private function changeChildrenCoordinates(context:DiagramShellContext, model:Object, side:int):void {					
			if (getModelController(context, model).getExpanded(context, model)) {
				var children:IList = getChildrenBasedOnSide(context, model, side);				
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children.getItemAt(i);
					if (i == 0) {						
						setPropertyValue(context, child, "y", getPropertyValue(context, model, "y") + getDeltaBetweenExpandedHeightAndHeight(context, child)/2 - getDeltaBetweenExpandedHeightAndHeight(context, model, false, false)/2);										
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
		
		public function getDeltaBetweenExpandedHeightAndHeight(context:DiagramShellContext, model:Object, preventNegativeValues:Boolean = false, addAdditionalPaddingIfNecessary:Boolean = true):Number {
			var additionalPadding:Number = getPropertyValue(context, model, "additionalPadding");			
			var expandedHeight:Number = getPropertyValue(context, model, "expandedHeight");
			var height:Number = getPropertyValue(context, model, "height");
			
			if ((preventNegativeValues && expandedHeight < height) || expandedHeight == 0) {
				// expandedHeight is smaller than height and we don't want negative values OR expandedHeght is 0 (model isn't expanded) -> return only the additional delta
				return addAdditionalPaddingIfNecessary ? additionalPadding : 0;
			}
			
			// expandedHeight exists (model is expanded) -> return delta between expandedHeight and height and, based on addAdditionalPaddingIfNecessary, remove additional padding
			// (used when we want to calculate the first child position)
			return expandedHeight - height - (!addAdditionalPaddingIfNecessary ? additionalPadding : 0);			
		}
		
		private function getModelBottomHeight(context:DiagramShellContext, model:Object):Number {
			var additionalPadding:Number = getPropertyValue(context, model, "additionalPadding");			
			var expandedHeight:Number = getPropertyValue(context, model, "expandedHeight");
			var height:Number = getPropertyValue(context, model, "height");
			
			return (Math.max(expandedHeight, height + additionalPadding) + height)/2 ;
		}
	}
}