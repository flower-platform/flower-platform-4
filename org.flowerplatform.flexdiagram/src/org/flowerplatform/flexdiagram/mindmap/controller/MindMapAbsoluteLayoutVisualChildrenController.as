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
package org.flowerplatform.flexdiagram.mindmap.controller {
	
	import flash.geom.Rectangle;
	
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.visual_children.AbsoluteLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramRenderer;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.renderer.IAbsoluteLayoutRenderer;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapAbsoluteLayoutVisualChildrenController extends AbsoluteLayoutVisualChildrenController {
		
		public function MindMapAbsoluteLayoutVisualChildrenController(orderIndex:int=0) {
			super(orderIndex);
		}
		
		override public function refreshVisualChildren(context:DiagramShellContext, parentModel:Object):void {
			var parentRenderer:MindMapDiagramRenderer = MindMapDiagramRenderer(ControllerUtils.getModelExtraInfoController(context, parentModel).getRenderer(context, context.diagramShell.modelToExtraInfoMap[parentModel]));
			
			var scrollRect:Rectangle = IAbsoluteLayoutRenderer(parentRenderer).getViewportRect();
			var noNeedToRefreshRect:Rectangle = IAbsoluteLayoutRenderer(parentRenderer).noNeedToRefreshRect;
			
			if (parentRenderer.shouldRefreshModelPositions) {				
				parentRenderer.shouldRefreshModelPositions = false;
				
				var logTsStart:Number = new Date().time;
				refreshModelPositions(context, MindMapDiagramShell(context.diagramShell).getRoot(context));
				
//				trace("MMAbsLayout.refrMP(): " + (new Date().time - logTsStart) + "ms");				
			}
			
			super.refreshVisualChildren(context, parentModel);
		}		
		
		public function refreshModelPositions(context:DiagramShellContext, model:Object):void {		
			var ds:MindMapDiagramShell = MindMapDiagramShell(context.diagramShell);
			var dynamicObject:Object = ds.getDynamicObject(context, model);
			var oldExpandedHeight:Number = ds.getPropertyValue(context, model, "expandedHeight", dynamicObject);
			var oldExpandedHeightLeft:Number = ds.getPropertyValue(context, model, "expandedHeightLeft", dynamicObject);		
			var oldExpandedHeightRight:Number = ds.getPropertyValue(context, model, "expandedHeightRight", dynamicObject);
			
			var side:int = ds.getModelController(context, model).getSide(context, model);
			var isRoot:Boolean = ds.getModelController(context, model).isRoot(context, model);			
			
			if (model == ds.getRoot(context)) {
				ds.setPropertyValue(context, model, "x", ds.getRootNodeX(context, model), dynamicObject);	
				ds.setPropertyValue(context, model, "y", ds.getRootNodeY(context, model), dynamicObject);	
			}
			
			calculateRootExpandedWidthAndHeight(context, side);	
			
			if (isRoot || side == MindMapDiagramShell.POSITION_LEFT) {						
				if (isRoot) {										
					oldExpandedHeight = oldExpandedHeightLeft;
					ds.setPropertyValue(context, model, "expandedHeight", ds.getPropertyValue(context, model, "expandedHeightLeft", dynamicObject), dynamicObject);	
				}				
				changeCoordinates(context, model, oldExpandedHeight, ds.getPropertyValue(context, model, "expandedHeight", dynamicObject), MindMapDiagramShell.POSITION_LEFT);								
			}
			if (isRoot || side == MindMapDiagramShell.POSITION_RIGHT) {
				if (isRoot) {								
					oldExpandedHeight = oldExpandedHeightRight;
					ds.setPropertyValue(context, model, "expandedHeight", ds.getPropertyValue(context, model, "expandedHeightRight", dynamicObject), dynamicObject);
				}
				changeCoordinates(context, model, oldExpandedHeight, ds.getPropertyValue(context, model, "expandedHeight", dynamicObject), MindMapDiagramShell.POSITION_RIGHT);							
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
			var ds:MindMapDiagramShell = MindMapDiagramShell(context.diagramShell);
			var model:Object = ds.getRoot(context);
			var dynamicObject:Object = ds.getDynamicObject(context, model);
			var isRoot:Boolean = ds.getModelController(context, model).isRoot(context, model);			
			
			if (isRoot || side == MindMapDiagramShell.POSITION_LEFT) {
				calculateExpandedWidthAndHeight(context, model, MindMapDiagramShell.POSITION_LEFT);
				ds.setPropertyValue(context, model, "expandedHeightLeft", ds.getPropertyValue(context, model, "expandedHeight", dynamicObject), dynamicObject);				
				ds.setPropertyValue(context, model, "expandedWidthLeft", ds.getPropertyValue(context, model, "expandedWidth", dynamicObject), dynamicObject);
			}
			if (isRoot || side == MindMapDiagramShell.POSITION_RIGHT) {
				calculateExpandedWidthAndHeight(context, model, MindMapDiagramShell.POSITION_RIGHT);
				ds.setPropertyValue(context, model, "expandedHeightRight", ds.getPropertyValue(context, model, "expandedHeight", dynamicObject), dynamicObject);
				ds.setPropertyValue(context, model, "expandedWidthRight", ds.getPropertyValue(context, model, "expandedWidth", dynamicObject), dynamicObject);
			}
		}
		
		private function calculateExpandedWidthAndHeight(context:DiagramShellContext, model:Object, side:int):Array {
			var expandedWidth:Number = 0;
			var expandedHeight:Number = 0;
			
			var ds:MindMapDiagramShell = MindMapDiagramShell(context.diagramShell);
			var dynamicObject:Object = ds.getDynamicObject(context, model);			
			var additionalPadding:Number = ds.getPropertyValue(context, model, "additionalPadding", dynamicObject);
			
			if (ds.getModelController(context, model).getExpanded(context, model)) {
				var children:Array = ds.getChildrenBasedOnSide(context, model, side);
				var logTsStart:Number = new Date().time;	
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children[i];
					
					var arr:Array = calculateExpandedWidthAndHeight(context, child, side);
					
					var childExpandedWidth:Number = arr[0];
					expandedWidth = Math.max(expandedWidth, childExpandedWidth);
					
					var childExpandedHeight:Number = arr[1];
					expandedHeight += childExpandedHeight;					
					if (i < children.length - 1) { // add padding only between children (not after the last one)
						expandedHeight += ds.verticalPadding;						
					}					
				}				
				expandedWidth += ds.getPropertyValue(context, model, "width", dynamicObject) + ds.horizontalPadding;
				
				ds.setPropertyValue(context, model, "expandedWidth", expandedWidth, dynamicObject);
				// add additional padding here -> used only to calculate parent's expandedWidth
				expandedWidth += additionalPadding/2;
				
				expandedHeight += additionalPadding;
				ds.setPropertyValue(context, model, "expandedHeight", expandedHeight, dynamicObject);				
				expandedHeight = Math.max(expandedHeight, ds.getPropertyValue(context, model, "height", dynamicObject));				
			} else {				
				expandedWidth = ds.getPropertyValue(context, model, "width", dynamicObject);				
				ds.setPropertyValue(context, model, "expandedWidth", expandedWidth, dynamicObject);
				// add additional padding here -> used only for return result to calculate parent's expandedWidth
				expandedWidth += additionalPadding/2;
				
				expandedHeight = ds.getPropertyValue(context, model, "height", dynamicObject) + additionalPadding;
				
				// for collapse models, the expandedHeight must be 0
				ds.setPropertyValue(context, model, "expandedHeight", 0, dynamicObject);				
			}
			return [expandedWidth, expandedHeight];
		}
		
		private function changeCoordinates(context:DiagramShellContext, model:Object, oldExpandedHeight:Number, newExpandedHeight:Number, side:int):void {			
			changeChildrenCoordinates(context, model, side);	
			
			var ds:MindMapDiagramShell = MindMapDiagramShell(context.diagramShell);
			var dynamicObject:Object = ds.getDynamicObject(context, model);			
			var additionalPadding:Number = ds.getPropertyValue(context, model, "additionalPadding", dynamicObject);
			var height:Number = ds.getPropertyValue(context, model, "height", dynamicObject);
			
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
			var ds:MindMapDiagramShell = MindMapDiagramShell(context.diagramShell);
			if (ds.getModelController(context, model).getExpanded(context, model)) {
				var modelDynamicObject:Object = ds.getDynamicObject(context, model);
				var modelX:Number = ds.getPropertyValue(context, model, "x", modelDynamicObject);
				var modelY:Number = ds.getPropertyValue(context, model, "y", modelDynamicObject);
				var modelWidth:Number = ds.getPropertyValue(context, model, "width", modelDynamicObject);
				var delta:Number = ds.getDeltaBetweenExpandedHeightAndHeight(context, model, false, false);
				
				var children:Array = ds.getChildrenBasedOnSide(context, model, side);				
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children[i];
					var dynamicObject:Object = ds.getDynamicObject(context, child);
					if (i == 0) {						
						ds.setPropertyValue(context, child, "y", modelY + ds.getDeltaBetweenExpandedHeightAndHeight(context, child)/2 - delta/2, dynamicObject);										
					} else {
						var previousChild:Object = children[i - 1];
						ds.setPropertyValue(context, child, "y", ds.getPropertyValue(context, previousChild, "y") + getModelBottomHeight(context, previousChild) + ds.verticalPadding + ds.getDeltaBetweenExpandedHeightAndHeight(context, child, true)/2, dynamicObject);
					}								
					if (ds.getModelController(context, child).getSide(context, child) == MindMapDiagramShell.POSITION_LEFT) {
						ds.setPropertyValue(context, child, "x", modelX - ds.getPropertyValue(context, child, "width", dynamicObject) - ds.horizontalPadding, dynamicObject);							
					} else {	
						ds.setPropertyValue(context, child, "x", modelX + modelWidth + ds.horizontalPadding, dynamicObject);															
					}						
					changeChildrenCoordinates(context, child, side);			
				}				
			}
		}
		
		private function changeSiblingsCoordinates(context:DiagramShellContext, model:Object, delta:Number, side:int, onlyBottomSiblings:Boolean = false):void {
			var ds:MindMapDiagramShell = MindMapDiagramShell(context.diagramShell);
			var parent:Object = ControllerUtils.getModelChildrenController(context, model).getParent(context, model);
			if (parent != null) {				
				var children:Array = ds.getChildrenBasedOnSide(context, parent, side);				
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children[i];
					var dynamicObject:Object = ds.getDynamicObject(context, child);
					if (!onlyBottomSiblings && children.getItemIndex(model) > children.getItemIndex(child)) {		
						ds.setPropertyValue(context, child, "y", ds.getPropertyValue(context, child, "y", dynamicObject) - delta, dynamicObject);										
						changeSiblingChildrenCoordinates(context, child, - delta, side);
					} else if (children.getItemIndex(model) < children.getItemIndex(child)) {
						ds.setPropertyValue(context, child, "y", ds.getPropertyValue(context, child, "y", dynamicObject) + delta, dynamicObject);								
						changeSiblingChildrenCoordinates(context, child, delta, side);						
					}					
				}
				changeSiblingsCoordinates(context, parent, delta, side, onlyBottomSiblings);
			}			
		}
		
		private function changeSiblingChildrenCoordinates(context:DiagramShellContext, model:Object, delta:Number, side:int):void {
			var ds:MindMapDiagramShell = MindMapDiagramShell(context.diagramShell);
			var children:Array = ds.getChildrenBasedOnSide(context, model, side);				
			for (var i:int = 0; i < children.length; i++) {
				var child:Object = children[i];
				ds.setPropertyValue(context, child, "y", ds.getPropertyValue(context, child, "y") + delta);					
				changeSiblingChildrenCoordinates(context, child, delta, side);
			}
		}
		
		private function getModelBottomHeight(context:DiagramShellContext, model:Object):Number {
			var ds:MindMapDiagramShell = MindMapDiagramShell(context.diagramShell);
			var dynamicObject:Object = ds.getDynamicObject(context, model);
			
			var additionalPadding:Number = ds.getPropertyValue(context, model, "additionalPadding", dynamicObject);			
			var expandedHeight:Number = ds.getPropertyValue(context, model, "expandedHeight", dynamicObject);
			var height:Number = ds.getPropertyValue(context, model, "height", dynamicObject);
			
			return (Math.max(expandedHeight, height + additionalPadding) + height)/2 ;
		}
		
	}
}
