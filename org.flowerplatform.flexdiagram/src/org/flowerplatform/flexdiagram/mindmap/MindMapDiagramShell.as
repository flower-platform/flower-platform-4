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
	import mx.core.ILayoutDirectionElement;
	import mx.core.IVisualElement;
	import mx.core.UIComponent;
	import mx.events.PropertyChangeEvent;
	import mx.olap.aggregators.MaxAggregator;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapControllerProvider;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
	
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
			
			shouldRefreshVisualChildren(rootModel);
		}
		
		public function getRoot():Object {
			var children:IList = getControllerProvider(rootModel).getModelChildrenController(rootModel).getChildren(context, rootModel);
			if (children == null || children.length == 0) {
				throw new Error("No root provided!");
			}
			return children.getItemAt(0);
		}
		
		public function refreshRootModelChildren():void {			
			var root:Object = getRoot();
			// clear old children
			getDynamicObject(rootModel).children = new ArrayList();
			// add new children
			addModelInRootModelChildrenListRecursive(root, true);	
			// refresh rootModel's visual children
			shouldRefreshVisualChildren(rootModel);
		}
		
		public function addModelInRootModelChildrenList(model:Object, asRoot:Boolean = false):void {
			if (getDynamicObject(rootModel).children == null) {
				getDynamicObject(rootModel).children = new ArrayList();
			}
			if (asRoot) {
				getDynamicObject(rootModel).children.addItemAt(model, 0);
			} else {
				getDynamicObject(rootModel).children.addItem(model);
			}
		}
		
		protected function addModelInRootModelChildrenListRecursive(model:Object, asRoot:Boolean = false):void {			
			if (getModelController(model).getExpanded(context, model)) {
				var children:IList = getModelController(model).getChildren(context, model);
				for (var i:int = 0; i < children.length; i++) {
					addModelInRootModelChildrenListRecursive(children.getItemAt(i));
				}
			}
			addModelInRootModelChildrenList(model, asRoot);				
		}
		
		public function getModelController(model:Object):IMindMapModelController {
			return IMindMapControllerProvider(getControllerProvider(model)).getMindMapModelController(model);
		}
		
		public function getDynamicObject(model:Object):Object {
			return DynamicModelExtraInfoController(getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(context, model);
		}
		
		private function getInitialPropertyValue(model:Object, property:String):Object {
			switch (property) {								
				case "width":
					return 10;	// minWidth
				case "height":
				case "oldHeight":
					return 22;	// minHeight							
				case "expandedHeight":
					return getPropertyValue(model, "height");
				default:
					return 0;
			}
		}
		
		public function getPropertyValue(model:Object, property:String):Number {
			if (isNaN(getDynamicObject(model)[property])) {				
				getDynamicObject(model)[property] = getInitialPropertyValue(model, property);
			}
			return getDynamicObject(model)[property];			
		}
		
		public function setPropertyValue(model:Object, property:String, value:Object):void {	
			var oldValue:Number = getDynamicObject(model)[property];
			
			getDynamicObject(model)[property] = value;
			
			model.dispatchEvent(PropertyChangeEvent.createUpdateEvent(model, property, oldValue, value));
		}
		
		protected function getChildrenBasedOnSide(model:Object, side:int = 0):IList {
			if (side == 0) {
				side = getModelController(model).getSide(context, model);
			}
			
			var list:ArrayList = new ArrayList();	
			var children:IList = getModelController(model).getChildren(context, model);
			if (children != null) {
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children.getItemAt(i);
					if (side == 0 || side == getModelController(child).getSide(context, child)) {
						list.addItem(child);
					}
				}
			}
			return list;
		}
		
		public function refreshModelPositions(model:Object):void {			
			var oldExpandedHeight:Number = getPropertyValue(model, "expandedHeight");
			var oldExpandedHeightLeft:Number = getPropertyValue(model, "expandedHeightLeft");		
			var oldExpandedHeightRight:Number = getPropertyValue(model, "expandedHeightRight");
			
			var side:int = getModelController(model).getSide(context, model);
			var isRoot:Boolean = getModelController(model).isRoot(context, model);			
			
			calculateRootExpandedHeight(side);
			
			if (isRoot || side == POSITION_LEFT) {						
				if (isRoot) {										
					oldExpandedHeight = oldExpandedHeightLeft;
				}			
				changeCoordinates(model, oldExpandedHeight, getPropertyValue(model, "expandedHeight"), POSITION_LEFT);
			}
			if (isRoot || side == POSITION_RIGHT) {
				if (isRoot) {								
					oldExpandedHeight = oldExpandedHeightRight;
				}				
				changeCoordinates(model, oldExpandedHeight, getPropertyValue(model, "expandedHeight"), POSITION_RIGHT);
			}
		}
		
		/**
		 * Recalculates all <code>expandedHeight</code> starting from root model.
		 * This is done only for a specific side (left/right).
		 * <p>
		 * Used when getting delta to re-arrange model siblings.
		 * @see changeCoordinates
		 */ 
		private function calculateRootExpandedHeight(side:int):void {
			var model:Object = getRoot();
			var isRoot:Boolean = getModelController(model).isRoot(context, model);			
			
			if (isRoot || side == POSITION_LEFT) {
				calculateExpandedHeight(model, POSITION_LEFT);
				setPropertyValue(model, "expandedHeightLeft", getPropertyValue(model, "expandedHeight"));				
			}
			if (isRoot || side == POSITION_RIGHT) {
				calculateExpandedHeight(model, POSITION_RIGHT);
				setPropertyValue(model, "expandedHeightRight", getPropertyValue(model, "expandedHeight"));				
			}
		}
		
		private function calculateExpandedHeight(model:Object, side:int):Number {			
			var expandedHeight:Number = 0;
			var children:IList = getChildrenBasedOnSide(model, side);
			if (getModelController(model).getExpanded(context, model)) {				
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children.getItemAt(i);
					var childExpandedHeight:Number = calculateExpandedHeight(child, side);
					expandedHeight += childExpandedHeight;					
					if (i < children.length - 1) { // add padding only between children (not after the last one)
						expandedHeight += verticalPadding;						
					}
				}
				setPropertyValue(model, "expandedHeight", expandedHeight);				
				expandedHeight = Math.max(expandedHeight, getPropertyValue(model, "height"));			
			} else {
				expandedHeight = getPropertyValue(model, "height");
				// for collapse models, the expandedHeight must be 0
				setPropertyValue(model, "expandedHeight", 0);
			}			
			return expandedHeight;
		}
		
		private function changeCoordinates(model:Object, oldExpandedHeight:Number, newExpandedHeight:Number, side:int):void {			
			changeChildrenCoordinates(model, side);	
			
			if (newExpandedHeight != 0 && newExpandedHeight < getPropertyValue(model, "height")) {
				
			} else {
				var delta:Number = 0;
				if (newExpandedHeight == 0) {
					if (oldExpandedHeight > getPropertyValue(model, "height")) {						
						delta = (getPropertyValue(model, "height") - oldExpandedHeight)/2;
					}					
				} else if (oldExpandedHeight == 0) {
					delta = (newExpandedHeight - getPropertyValue(model, "height"))/2;
				} else {
					delta = (newExpandedHeight - oldExpandedHeight)/2;
				}
				if (delta != 0) {
					changeSiblingsCoordinates(model, delta, side);
				}
			}					
			getDynamicObject(model).shouldRefreshPosition = false;
		}		
		
		private function changeChildrenCoordinates(model:Object, side:int):void {					
			if (getModelController(model).getExpanded(context, model)) {
				var children:IList = getChildrenBasedOnSide(model, side);				
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children.getItemAt(i);
					if (i == 0) {						
						setPropertyValue(child, "y", getPropertyValue(model, "y") + getDeltaBetweenExpandedHeightAndHeight(child)/2 - getDeltaBetweenExpandedHeightAndHeight(model)/2);										
					} else {
						var previousChild:Object = children.getItemAt(i - 1);
						setPropertyValue(child, "y", getPropertyValue(previousChild, "y") + getModelBottomHeight(previousChild) + verticalPadding + getDeltaBetweenExpandedHeightAndHeight(child, true)/2);
					}								
					if (getModelController(child).getSide(context, child) == POSITION_LEFT) {
						setPropertyValue(child, "x", getPropertyValue(model, "x") - getPropertyValue(child, "width") - horizontalPadding);							
					} else {	
						setPropertyValue(child, "x", getPropertyValue(model, "x") + getPropertyValue(model, "width") + horizontalPadding);															
					}						
					changeChildrenCoordinates(child, side);			
				}				
			}
		}
		
		private function changeSiblingsCoordinates(model:Object, delta:Number, side:int, onlyBottomSiblings:Boolean = false):void {			
			var parent:Object = getControllerProvider(model).getModelChildrenController(model).getParent(context, model);
			if (parent != null) {				
				var children:IList = getChildrenBasedOnSide(parent, side);				
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children.getItemAt(i);
					if (!onlyBottomSiblings && children.getItemIndex(model) > children.getItemIndex(child)) {		
						setPropertyValue(child, "y", getPropertyValue(child, "y") - delta);										
						changeSiblingChildrenCoordinates(child, - delta, side);
					} else if (children.getItemIndex(model) < children.getItemIndex(child)) {
						setPropertyValue(child, "y", getPropertyValue(child, "y") + delta);								
						changeSiblingChildrenCoordinates(child, delta, side);						
					}					
				}
				changeSiblingsCoordinates(parent, delta, side, onlyBottomSiblings);
			}			
		}
		
		private function changeSiblingChildrenCoordinates(model:Object, delta:Number, side:int):void {
			var children:IList = getChildrenBasedOnSide(model, side);				
			for (var i:int = 0; i < children.length; i++) {
				var child:Object = children.getItemAt(i);
				setPropertyValue(child, "y", getPropertyValue(child, "y") + delta);					
				changeSiblingChildrenCoordinates(child, delta, side);
			}
		}
		
		private function getDeltaBetweenExpandedHeightAndHeight(model:Object, preventNegativeValues:Boolean = false):Number {
			if (preventNegativeValues && getPropertyValue(model, "expandedHeight") < getPropertyValue(model, "height")) {
				return 0;
			}
			if (getPropertyValue(model, "expandedHeight") != 0) {
				return getPropertyValue(model, "expandedHeight") - getPropertyValue(model, "height");
			}
			return 0;		
		}
		
		private function getModelBottomHeight(model:Object):Number {			
			return Math.max(getPropertyValue(model, "expandedHeight"), getPropertyValue(model, "height"))/2 + getPropertyValue(model, "height")/2;
		}
	}
}