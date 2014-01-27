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
package org.flowerplatform.flexdiagram.util.infinitegroup {
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import mx.core.InteractionMode;
	import mx.core.mx_internal;
	import mx.events.PropertyChangeEvent;
	import mx.events.ResizeEvent;
	
	import spark.components.VScrollBar;
	import spark.events.TrackBaseEvent;
	
	use namespace mx_internal;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class InfiniteVScrollBar extends VScrollBar {
		
		private var clickOffset:Point;   
				
		override public function set minimum(value:Number):void {	
			var myValue:Number = getMyMinimumValue(value);		
			if (myValue== super.minimum) {
				return;
			}
			super.minimum = myValue;
			invalidateDisplayList();
		}
		
		override public function set maximum(value:Number):void {			
			var myValue:Number = getMyMaximumValue(value);
			if (myValue == super.maximum) {
				return;
			}
			super.maximum = myValue;
			invalidateSkinState();
		}

		private function getMyMinimumValue(flexValue:Number = NaN):Number {
			var myValue:Number = flexValue;
			if (viewport != null) {
				myValue = Math.min(0, viewport.verticalScrollPosition);
			}
			return myValue;
		}
		
		private function getMyMaximumValue(flexValue:Number = NaN):Number {		
			var myValue:Number = flexValue;
			var infiniteViewport:InfiniteDataRenderer = InfiniteDataRenderer(viewport);
			if (infiniteViewport != null && infiniteViewport.contentRect != null) {
				myValue = Math.max(infiniteViewport.getMaxVerticalScrollPosition(), viewport.verticalScrollPosition);
			}
			return myValue;
		}
		
		override mx_internal function viewportVerticalScrollPositionChangeHandler(event:PropertyChangeEvent):void {
			super.viewportVerticalScrollPositionChangeHandler(event);
			if (viewport) {					
				minimum = getMyMinimumValue();
				maximum = getMyMaximumValue();
			}
		}
		
		override protected function thumb_mouseDownHandler(event:MouseEvent) : void {
			super.thumb_mouseDownHandler(event);
			clickOffset = thumb.globalToLocal(new Point(event.stageX, event.stageY));        
		}
		
		override protected function system_mouseMoveHandler(event:MouseEvent):void {     
			if (!track)
				return;
			
			var p:Point = track.globalToLocal(new Point(event.stageX, event.stageY));
			var newValue:Number = pointToValue(p.x - clickOffset.x, p.y - clickOffset.y);
			newValue = nearestValidValue(newValue, snapInterval);
					
			if (newValue != pendingValue) {
				dispatchEvent(new TrackBaseEvent(TrackBaseEvent.THUMB_DRAG));
				if (getStyle("liveDragging") === true) {
					setValue(newValue);
					dispatchEvent(new Event(Event.CHANGE));
				} else {
					pendingValue = newValue;
				}
			}			
			event.updateAfterEvent();
		}
		
		override protected function nearestValidValue(value:Number, interval:Number):Number	{					
			var scale:Number = 1;
			
			if (interval != Math.round(interval)) {
				// calculate scale and compute new scaled values.
				const parts:Array = (new String(1 + interval)).split("."); 
				scale = Math.pow(10, parts[1].length);				
				interval = Math.round(interval * scale);
				value = Math.round((value * scale));
			}
			
			var lower:Number = Math.max(minimum - 1, Math.floor(value / interval) * interval);
			var upper:Number = Math.min(maximum + 1, Math.floor((value + interval) / interval) * interval);
			var validValue:Number = ((value - lower) >= ((upper - lower) / 2)) ? upper : lower;
			
			return validValue / scale;
		}		
	}
}