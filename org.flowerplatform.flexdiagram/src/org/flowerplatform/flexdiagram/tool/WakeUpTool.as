/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flexdiagram.tool {
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.events.TimerEvent;
	import flash.ui.Keyboard;
	import flash.utils.Timer;
	
	import mx.collections.ArrayList;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class WakeUpTool extends Tool {
				
		public static const ID:String = "WakeUpTool";
		
		public static const NO_EVENT:String = "none";		
		public static const MOUSE_DRAG:String = "mouseDrag";
		public static const MOUSE_DOWN:String = "mouseDown";
		public static const MOUSE_UP:String = "mouseUp";
		public static const MOUSE_RIGHT_CLICK:String = "mouseRightClick";
		public static const DOUBLE_CLICK:String ="doubleClick";
		public static const CLICK:String ="click";
		
		public var listeners:ArrayList = new ArrayList();
			
		public var myEventType:String; 
		
		private var secondClick:Boolean = false;
		
		public function WakeUpTool(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public static function wakeMeUpIfEventOccurs(diagramShell:DiagramShell, tool:Tool, event:String, priority:int = 0):void {
			var listener:Object = new Object();
			listener.tool = tool;
			listener.event = event;
			listener.priority = priority;
			
			diagramShell.tools[WakeUpTool].listeners.addItem(listener);
		}
		
		override public function activateAsMainTool():void {
			diagramRenderer.doubleClickEnabled = true;
			diagramRenderer.addEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler);
			diagramRenderer.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);			
			diagramRenderer.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			diagramRenderer.addEventListener(MouseEvent.RIGHT_CLICK, rightClickHandler);
			diagramRenderer.addEventListener(MouseEvent.DOUBLE_CLICK, mouseDoubleClickHandler);
			diagramRenderer.addEventListener(MouseEvent.CLICK, clickHandler);
			super.activateAsMainTool();
		}
		
		override public function deactivateAsMainTool():void {		
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler);
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);			
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			diagramRenderer.removeEventListener(MouseEvent.RIGHT_CLICK, rightClickHandler);
			diagramRenderer.removeEventListener(MouseEvent.DOUBLE_CLICK, mouseDoubleClickHandler);
			diagramRenderer.removeEventListener(MouseEvent.CLICK, clickHandler);

			super.deactivateAsMainTool();
		}
				
		override public function activateDozingMode():void {
			diagramRenderer.addEventListener(KeyboardEvent.KEY_DOWN, keyDownHandler);
		}
		
		override public function deactivateDozingMode():void { 	
			diagramRenderer.removeEventListener(KeyboardEvent.KEY_DOWN, keyDownHandler);
		}
		
		// added here because we only need one event for all tools
		// this seamed to be the right place
		protected function keyDownHandler(event:KeyboardEvent):void {
			if (event.keyCode == Keyboard.ESCAPE || event.keyCode == Keyboard.BACK) {
				// if ESC or back button pressed, deactivates current tool
				diagramShell.mainToolFinishedItsJob();
			}
		}
		
		private function rightClickHandler(event:MouseEvent):void {
			myEventType = MOUSE_RIGHT_CLICK;			
			dispatchMyEvent(myEventType, event);			
		}
		
		private function mouseDownHandler(event:MouseEvent):void {
			myEventType = MOUSE_DOWN;			
			dispatchMyEvent(myEventType, event);	
		}
		
		private function mouseMoveHandler(event:MouseEvent):void {
			if (event.buttonDown) {			
				myEventType = MOUSE_DRAG;
				dispatchMyEvent(myEventType, event);
				reset();
			}					
		}
				
		private function mouseUpHandler(event:MouseEvent):void {
			if (myEventType != MOUSE_DRAG) {
				myEventType = MOUSE_UP;
				dispatchMyEvent(myEventType, event);
				reset();
			}
		}
		
		private function mouseDoubleClickHandler(event:MouseEvent):void {
			myEventType = DOUBLE_CLICK;
			dispatchMyEvent(myEventType, event);
			secondClick = true;
		}
		
		private function clickHandler(event:MouseEvent):void {
			/*A timer is needed so that the clickEvent won`t be dispatched along with the doubleClickEvent */
			secondClick = false;
			var timer:Timer = new Timer(250,1);
			timer.addEventListener(TimerEvent.TIMER, onTimer);
			timer.start();		
		}
		
		private function onTimer(event:TimerEvent):void {
			if(secondClick == false && myEventType != MOUSE_DRAG){
				myEventType = CLICK;
				dispatchMyEvent(myEventType, new MouseEvent(CLICK));
			}
			secondClick = false;
		}
		
		private function dispatchMyEvent(eventType:String, initialEvent:MouseEvent):void {			
			var array:Array = filterAndSortListeners(eventType);
			
			while (array.length != 0) {				
				var tool:IWakeUpableTool = array.pop().tool;
				if (tool.wakeUp(eventType, initialEvent)) {
					diagramShell.mainTool = Tool(tool);
					break;
				}
			}
		}
		
		private function filterAndSortListeners(eventType:String):Array {
			// filter by eventType
			var array:Array = listeners.source.filter(
				function callback(item:*, index:int, array:Array):Boolean {
					return (item.event == eventType);
				}			
			);			
			// sort descending by priority
			return array.sortOn("priority", Array.DESCENDING & Array.NUMERIC);
		}
		
		override public function reset():void {
			super.reset();
			for (var i:int = 0; i < listeners.length; i++) {
				Tool(listeners.getItemAt(i).tool).reset();
			}
		}
	}
}