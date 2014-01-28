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
package org.flowerplatform.flexdiagram.renderer {
	import flash.events.FocusEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	
	import mx.core.IVisualElement;
	import mx.managers.IFocusManagerComponent;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.visual_children.IVisualChildrenController;
	import org.flowerplatform.flexdiagram.util.RectangularGrid;
	import org.flowerplatform.flexdiagram.util.infinitegroup.InfiniteDataRenderer;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class DiagramRenderer extends InfiniteDataRenderer implements IDiagramShellAware, IVisualChildrenRefreshable, IAbsoluteLayoutRenderer, IFocusManagerComponent {

		private var _diagramShell:DiagramShell;
		protected var visualChildrenController:IVisualChildrenController;
		private var _shouldRefreshVisualChildren:Boolean;
		private var _noNeedToRefreshRect:Rectangle;
		
		public var viewPortRectOffsetTowardOutside:int = 0;
		
		/**
		 * @author Mircea Negreanu
		 * 
		 * The background grid
		 */
		private var _grid:IVisualElement;
		
		/**
		 * If we want grid or not
		 * 
		 * @author Mircea Negreanu
		 */
		public var useGrid:Boolean = true;
		
		public function get diagramShell():DiagramShell {
			return _diagramShell;
		}
		
		public function set diagramShell(value:DiagramShell):void {
			_diagramShell = value;
		}
		
		public function get shouldRefreshVisualChildren():Boolean {
			return _shouldRefreshVisualChildren;
		}
		
		public function set shouldRefreshVisualChildren(value:Boolean):void {
			_shouldRefreshVisualChildren = value;
		}
		
		public function get noNeedToRefreshRect():Rectangle {
			return _noNeedToRefreshRect;
		}
		
		public function set noNeedToRefreshRect(value:Rectangle):void {
			_noNeedToRefreshRect = value; 
		}

		/**
		 * Get the grid used by this component.
		 * 
		 * @author Mircea Negreanu
		 */
		public function get grid():IVisualElement {
			if (useGrid && _grid == null) {
				_grid = new RectangularGrid();
			}
			return _grid;
		}
		
		override public function set data(value:Object):void {
			super.data = value;
			if (data == null) {
				visualChildrenController = null;
			} else {
				visualChildrenController = diagramShell.getControllerProvider(data).getVisualChildrenController(data);
			}
		}
		
		public function getViewportRect():Rectangle {
			return new Rectangle(horizontalScrollPosition - viewPortRectOffsetTowardOutside, verticalScrollPosition - viewPortRectOffsetTowardOutside, width + 2 * viewPortRectOffsetTowardOutside, height + 2 * viewPortRectOffsetTowardOutside);
		}
		
		public function setContentRect(rect:Rectangle):void {
			contentRect = rect;
		}
		
		/**
		 * @author Cristian Spiescu
		 * @author Mircea Negreanu
		 */
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			if (visualChildrenController != null) {
				visualChildrenController.refreshVisualChildren(data);
			}
			
			// resize/move the grid (depending on the viewport dimensions)
			sizeGrid();
			
			super.updateDisplayList(unscaledWidth, unscaledHeight);

			graphics.clear();
			graphics.lineStyle(1);
			graphics.beginFill(0xCCCCCC, 0);
			graphics.drawRect(horizontalScrollPosition, verticalScrollPosition, width, height);
		}
		
		/**
		 * @author Cristina Constantinescu
		 */ 
		override protected function focusInHandler(event:FocusEvent):void {
			super.focusInHandler(event);		
			if (diagramShell != null && stage != null) { 
				// stage == null -> save dialog closes, the focusManager tries to put focus on diagram,
				// but it will be removed shortly, so don't take this in consideration
				diagramShell.activateTools();
			}
		}
		
		override protected function focusOutHandler(event:FocusEvent):void {
			super.focusOutHandler(event);
			
			if (stage == null) {			
				return;
			}
			var point:Point = globalToContent(new Point(stage.mouseX, stage.mouseY));			
			if (!getViewportRect().containsPoint(point)) { // if outside diagram area
				diagramShell.deactivateTools();	
			}							
		}		
		
		/**
		 * In case we want grid, add it to the container
		 * 
		 * @author Mircea Negreanu
		 */
		override protected function createChildren():void {
			super.createChildren();
			
			if (useGrid) {
				grid.x = grid.y = 0;
				// we want dash
				RectangularGrid(grid).dashSize = 1;
			
				// add it
				addElement(grid);
			}
		}
		
		/**
		 * Size the grid based on scroll position and width/height.
		 * <p>
		 * 	Make the grid invisible when the scale is lower the 0.75
		 * </p>
		 * 
		 * @author Mircea Negreanu
		 */
		protected function sizeGrid():void {
			if (useGrid) {
				if (this.scaleX < 0.75) {
					grid.visible = false;
				} else {
					grid.visible = true;
				}
				
				// resize/move the grid, only if its dimensions or position were changed
				if (grid.visible &&
					(grid.x != horizontalScrollPosition
						|| grid.y != verticalScrollPosition
						|| grid.width != width
						|| grid.height != height)) {
					grid.x = horizontalScrollPosition;
					grid.y = verticalScrollPosition;
					grid.width = width;
					grid.height = height;
				}
			}
		}
	}
}