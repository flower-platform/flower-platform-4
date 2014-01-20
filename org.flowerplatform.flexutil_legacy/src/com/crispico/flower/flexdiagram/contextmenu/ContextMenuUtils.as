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
package com.crispico.flower.flexdiagram.contextmenu
{
	import flash.display.DisplayObject;
	import flash.display.DisplayObjectContainer;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	
	import mx.core.Container;
	import mx.core.UIComponent;
	import mx.core.mx_internal;
	
	use namespace mx_internal;
	
	/**
	 * Utility class intended to host various hacking methods
	 * for working with the Context Menu Framework and with Context Menu component.
	 * @author Sorin
	 */ 
	public class ContextMenuUtils {
		
		/**
		 * Utility method which for a given point in local coordinates of an <code>UIComponent</code>
		 * return the stage coordinates using <code>getStageLocationForContainer()</code> method for obtaining correct results.
		 */ 		
		public static function getStageLocationForContainerUsingLocal(figure:UIComponent, scrollableContainer:Container, localPointOfFigure:Point):Point {
			var globalPointOfFigure:Point = figure.localToGlobal(localPointOfFigure);
			var contentPointOfFigure:Point = scrollableContainer.globalToContent(globalPointOfFigure); 
			return getStageLocationForContainer(scrollableContainer, contentPointOfFigure);
		}
		
		/**
		 * Utility method which for a given point in content coordinates of the <pre>scrollableContainer</pre>
		 * is capable of returning the global/stage coordinates correctly.
		 * 
		 * <p/> This method takes in consideration the vertical and horizontal scroll position of the given container.
		 * The purpose of this method is to be used for computing stage coordinates by the <code>Container</code> extending components
		 * because there are not capable of providing a moment after the scrolling, for using the <code>localToGlobal</code> method on a figure and to return
		 * correct results (they return delayed, unshifted results). 
		 * 
		 * <p/> WHEN scrolling this method must be used ONLY after the receiving the update completed event.
		 * 
		 * <p/> It takes in consideration also the scale factor used when executing this method. 
		 */ 
		public static function getStageLocationForContainer(scrollableContainer:Container, contentLocation:Point, scaleFactor:Number = 1):Point {
			// Obtain the horizontal and vertical scrolling position in order to know the shifting needed from content coordinates of the scrollableContainer
			var horizontalScrollPosition:Number = 0;
			var verticalScrollPosition:Number = 0;
		
			var scrollRect:Rectangle = null;
			/*
			 Sometimes the contentPane from this scrollableContainer is null. 
			 It seems that some contentPanes aren't initialized from the begining when working with a diagram without scrollbars 
			 (RootFigure, DiagramContent).
			 They are set when user makes a move/creates an element/etc. or when a scrollbar is made visible.
			 If we don't make this actions and we try to select an element, a "null pointer exception" error appears 
			 because contentPane for scrollableContainer is null.
			 In the other case, when a diagram has scrollbars, the contentPane is never null from the begining. 
			 So, for the moment, we make this verification in order to run without errors.
			*/
			if (scrollableContainer != null && scrollableContainer.contentPane != null) {
				scrollRect = scrollableContainer.contentPane.scrollRect;
			}
			if (scrollRect != null) {
				horizontalScrollPosition = scrollRect.x;
				verticalScrollPosition = scrollRect.y;
			}
			
			// Obtain the coordinates of the top left of the scrollable container relative to the stage.
			var scrollableContainerUpperLeftGlobal:Point = scrollableContainer.localToGlobal(new Point(0,0));
			// Return the global location by adding the global location of the scrollable container and the content location relative to the scrollable container
			// but substracting the scrolled positions.
			return new Point(scrollableContainerUpperLeftGlobal.x + contentLocation.x * scaleFactor - horizontalScrollPosition,
			  				 scrollableContainerUpperLeftGlobal.y + contentLocation.y * scaleFactor - verticalScrollPosition);
		}		
			
		public static function flex4CompatibleContainer_addChild(parent:DisplayObjectContainer, child:DisplayObject):void {
			if (parent is mx.core.Container) {
				mx.core.Container(parent).addChild(child);
			} else {
				/* spark.components.supportClasses.SkinnableComponent */ Object(parent).addElement(child);	
			}
		}
		
		public static function flex4CompatibleContainer_removeChild(parent:DisplayObjectContainer, child:DisplayObject):void {
			if (parent is mx.core.Container) {
				mx.core.Container(parent).removeChild(child);
			} else {
				/* spark.components.supportClasses.SkinnableComponent */ Object(parent).removeElement(child);	
			}
		}
		
		public static function flex4CompatibleContainer_globalToContent(parent:DisplayObjectContainer, point:Point):Point {
			var relativeToContent:Point;
			if (parent is mx.core.Container) {
				relativeToContent = mx.core.Container(parent).globalToContent(point);
			} else { // like spark.components.SkinnableContainer
				relativeToContent = parent.globalToLocal(point);
			}
			return relativeToContent;
		}

		public static function flex4CompatibleContainer_contentToGlobal(parent:DisplayObjectContainer, point:Point):Point {
			var relativeToContent:Point;
			if (parent is mx.core.Container) {
				relativeToContent = mx.core.Container(parent).contentToGlobal(point);
			} else { // like spark.components.SkinnableContainer
				relativeToContent = parent.localToGlobal(point);
			}
			return relativeToContent;
		}
		
		public static function flex4CompatibleContainer_getCursorPosition(parent:DisplayObjectContainer):Point {
			var uiComponentParent:UIComponent = UIComponent(parent);
			var contentMouseCursor:Point = new Point(uiComponentParent.contentMouseX, uiComponentParent.contentMouseY);
			return flex4CompatibleContainer_contentToGlobal(parent, contentMouseCursor);
		}
//		/**
//		 * This is a generic implementation for finding if a given point is over the main selected object.
//		 * <p>
//		 * If the selected figure is a <code>ConnectionFigure</code>, the search is done by iterating througth all its
//		 * segments and it stops when the point is found over a segment.
//		 * Otherwise, the upper left and bottom right points of the rectangular area are found and if
//		 * the point is under this area then it returns <code>true</code>. 
//		 * 
//		 * @author Cristina
//		 * @author Cristi
//		 */ 
//		public static function diagramIsOverSelection(viewer:DiagramViewer, event:MouseEvent):Boolean {
//			var point:Point = new Point(event.stageX, event.stageY);			
//			var figure:UIComponent = viewer.getMainSelectedFigure();
//			if (figure == null) {
//				// nothing is selected, consider diagram as selection, so returns true
//				return true;
//			}
//			
//			if (figure is ConnectionFigure) {
//				var segmentFig:ConnectionFigure = figure as ConnectionFigure;
//				for (var i:int; i < segmentFig.getNumberOfSegments(); i++) {
//					var segment:ConnectionSegment = segmentFig.getSegmentAt(i);					
//					if (segment.hitTestPoint(point.x, point.y, true)) {
//						return true;
//					}	
//				}
//			} else {
//				var scrollableContainer:RootFigure = RootFigure(viewer.getRootEditPart().getFigure()); 
//				// These 2 corners are relative to the stage
//				var upperLeft:Point = ContextMenuUtils.getStageLocationForContainerUsingLocal(figure, scrollableContainer, new Point(0,0));
//				var downRight:Point = ContextMenuUtils.getStageLocationForContainerUsingLocal(figure, scrollableContainer, new Point(figure.width, figure.height));
//				
//				// TODO sorin : hack rapid pentru prezentare 16 nov, astfel incat atunci cand se move/resize un element selectat, CM sa il urmareasca
//				var mainSelectedEditPart:EditPart = EditPart(viewer.getSelection().getItemAt(viewer.getMainSelection()));
//				if (mainSelectedEditPart is IAbsolutePositionEditPart) {
//					var bounds:Array = IAbsolutePositionEditPart(mainSelectedEditPart).getBoundsRect();			
//					
//					upperLeft = ContextMenuUtils.getStageLocationForContainer(scrollableContainer, new Point(bounds[0], bounds[1]), viewer.getScaleForContextMenu());
//					downRight = ContextMenuUtils.getStageLocationForContainer(scrollableContainer, new Point(bounds[0] + bounds[2], bounds[1] + bounds[3]), viewer.getScaleForContextMenu());
//				}		
//				
//				if (point.x >= upperLeft.x 
//					&& point.y >= upperLeft.y 
//					&& point.x <= downRight.x
//					&& point.y <= downRight.y) {
//					return true;
//				}
//			}
//			return false;
//		}
//		
//		/**
//		 * This is a generic implementation for finding the figure's bounds relative to the stage,
//		 * figure associated to the main selected edit part.
//		 * <p/> If there is no main edit part selected or the figure associated is null
//		 * this method will return null, as in the selection has no visible area on the screen.
//		 * according to the CM framework convention.
//		 *   
//		 * @author Sorin
//		 * @author Cristi
//		 */
//		public static function diagramGetDisplayAreaOfSelection(viewer:DiagramViewer):Rectangle {
//			var figure:UIComponent = viewer.getMainSelectedFigure();
//			if (figure == null)
//				return null;
//			
//			var scrollableContainer:Container = RootFigure(viewer.getRootEditPart().getFigure()); 			
//			
//			if (figure is ConnectionFigure) {
//				// Coordonates relative to the diagram container
//				var middle:Array = ConnectionFigure(figure).getMiddlePointRect();
//				// Transform from coordonates relative to the content of the diagram to relative to the stage.
//				var middleGlobal:Point = ContextMenuUtils.getStageLocationForContainer(scrollableContainer, new Point(middle[0], middle[1]), viewer.getScaleForContextMenu());
//				// Width and height must be at least 1 in order for intersection of rectangles about the display area and the viewer area to work. 
//				return new Rectangle(middleGlobal.x, middleGlobal.y, DiagramViewer.MINIMUM_SIZE_AREA, DiagramViewer.MINIMUM_SIZE_AREA);  
//			} else {
//				// These 2 corners are relative to the stage
//				var upperLeft:Point = ContextMenuUtils.getStageLocationForContainerUsingLocal(figure, scrollableContainer, new Point(0,0));
//				var downRight:Point = ContextMenuUtils.getStageLocationForContainerUsingLocal(figure, scrollableContainer, new Point(figure.width, figure.height));
//				
//				// TODO sorin : hack rapid pentru prezentare 16 nov, astfel incat atunci cand se move/resize un element selectat, CM sa il urmareasca
//				var mainSelectedEditPart:EditPart = EditPart(viewer.getSelection().getItemAt(viewer.getMainSelection()));
//				if (mainSelectedEditPart is IAbsolutePositionEditPart) {
//					var bounds:Array = IAbsolutePositionEditPart(mainSelectedEditPart).getBoundsRect();				
//					
//					upperLeft = ContextMenuUtils.getStageLocationForContainer(scrollableContainer, new Point(bounds[0], bounds[1]), viewer.getScaleForContextMenu());
//					downRight = ContextMenuUtils.getStageLocationForContainer(scrollableContainer, new Point(bounds[0] + bounds[2], bounds[1] + bounds[3]), viewer.getScaleForContextMenu());
//				}
//				
//				// For having the width and the height of the rectangle we need to substract the x and y of the down right corner from the upper left corner.  
//				return new Rectangle(upperLeft.x, upperLeft.y, downRight.x - upperLeft.x, downRight.y - upperLeft.y);  
//			}
//		}
//		
	}
	
}