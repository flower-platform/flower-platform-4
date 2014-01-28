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
package org.flowerplatform.flexdiagram.tool
{
	import flash.events.GesturePhase;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.events.TouchEvent;
	import flash.events.TransformGestureEvent;
	import flash.geom.Matrix;
	import flash.geom.Point;
	import flash.ui.Multitouch;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class ZoomTool extends Tool {
		
		public static const ID:String = "ZoomTool";
		
		private const MIN_SCALE_DEFAULT:Number = 0.15; 
		
		private const MAX_SCALE_DEFAULT:Number = 4; 
		
		private var _minScale:Number = MIN_SCALE_DEFAULT;
		
		private var _maxScale:Number = MAX_SCALE_DEFAULT;
		
		public function get minScale():Number {
			return _minScale;
		}

		public function set minScale(value:Number):void {
			_minScale = value;
		}

		public function get maxScale():Number {
			return _maxScale;
		}
		
		public function set maxScale(value:Number):void {
			_maxScale = value;
		}
		
		public function ZoomTool(diagramShell:DiagramShell)	{
			super(diagramShell);
		}
		
		override public function activateDozingMode():void {
			diagramRenderer.addEventListener(MouseEvent.MOUSE_WHEEL, mouseWheelHandler, false, int.MAX_VALUE);
			if(Multitouch.supportsGestureEvents) {
				diagramRenderer.addEventListener(TransformGestureEvent.GESTURE_ZOOM, gestureZoomHandler);
			}
		}
		
		override public function deactivateDozingMode():void {
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_WHEEL, mouseWheelHandler);
			if(Multitouch.supportsGestureEvents) {
				diagramRenderer.removeEventListener(TransformGestureEvent.GESTURE_ZOOM, gestureZoomHandler);
			}
		}
		
		private function mouseWheelHandler(event:MouseEvent):void {			
			if (event.ctrlKey && event.type == MouseEvent.MOUSE_WHEEL) {
				diagramShell.mainTool = this;
										
				// scrolling the mouse with ctrl pressed
				// The delta parameter represents the number of lines to be scrolled, when using the mouse wheel. This 
				// parameter is given by the operating system. We have enforced that the delta should be different from 0
				// because on Mac, when scrolling the wheel, with the most small step, the delta returned is 0. 
				// On Mac this means that the mouse wheel must do bigger steps (scroll faster) in order for the zoom to work.
				// TODO sorin : on Mac OS , native listeners should be added to fix the problem.
				if (event.delta != 0) {
					var scale:Number = 0.1;
					if (event.delta < 0) {
						scale *= -1;
					}
					scale = 1 + scale;
					
					diagramRenderer.scaleX *= getScaleFactor(scale, true);
					diagramRenderer.scaleY *= getScaleFactor(scale, false);
					
				}
				// We stop the propagation of the event even if the delta is 0 because on Mac, sometimes when scrolling
				// with CMD pressed at a speed not big enougth to be caught with delta !=0, it seams that it scrolls the diagram,
				// instead to perform a zooming.
				event.preventDefault();
				
				// this will trigger a refresh for renderers to display truncated label if too long
				diagramRenderer.shouldRefreshVisualChildren = true; 
				diagramShell.mainToolFinishedItsJob();
			}
		}
		
		private function gestureZoomHandler(event:TransformGestureEvent):void {	
			if (event.phase == GesturePhase.BEGIN) {				
				diagramShell.mainTool = this;
			}
			
			diagramRenderer.scaleX *= getScaleFactor((event.scaleX + event.scaleY)/2, true);
			diagramRenderer.scaleY *= getScaleFactor((event.scaleX + event.scaleY)/2, false);
			
			if (event.phase == GesturePhase.END) {	
				// this will trigger a refresh for renderers to display truncated label if too long
				diagramRenderer.shouldRefreshVisualChildren = true; 
				diagramShell.mainToolFinishedItsJob();
			}			
		}
			
		private function getScaleFactor(scaleFactor:Number, isX:Boolean):Number {
			if ((isX ? diagramRenderer.scaleX : diagramRenderer.scaleY) * scaleFactor < _minScale) {
				return 1;
			} else if ((isX ? diagramRenderer.scaleX : diagramRenderer.scaleY) * scaleFactor > _maxScale) {
				return 1;
			}
			return scaleFactor;
		}
	}
	
}