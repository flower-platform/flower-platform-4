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
package org.flowerplatform.flexdiagram.tool {
	import flash.display.DisplayObject;
	import flash.display.Stage;
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	import flash.events.KeyboardEvent;
	import flash.geom.Point;
	import flash.ui.Keyboard;
	
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	
	import spark.components.Scroller;
	
	[Bindable]
	
	/**
	 * @author Cristina Constantinescu
	 */	
	public class Tool extends EventDispatcher {
		
		public static const ACTIVATED_AS_MAIN_TOOL:String = "activatedAsMainTool";
		public static const DEACTIVATED_AS_MAIN_TOOL:String = "deactivatedAsMainTool";
		
		protected var diagramShell:DiagramShell;
		
		public var context:Object = new Object();
				
		public function Tool(diagramShell:DiagramShell) {
			this.diagramShell = diagramShell;
		}		
					
		public function activateDozingMode():void {				
		}
		
		public function deactivateDozingMode():void { 				
		}
		
		public function activateAsMainTool():void {		
			dispatchEvent(new Event(ACTIVATED_AS_MAIN_TOOL));
		}
		
		public function deactivateAsMainTool():void {
			dispatchEvent(new Event(DEACTIVATED_AS_MAIN_TOOL));		
		}
		
		public function get diagramRenderer():DiagramRenderer {			
			return DiagramRenderer(diagramShell.diagramRenderer);
		}
			
		protected function getRendererFromDisplayCoordinates(ignoreDiagramRenderer:Boolean = false):IVisualElement {
			var stage:Stage = diagramRenderer.stage;
			var arr:Array = stage.getObjectsUnderPoint(new Point(stage.mouseX, stage.mouseY));
						
			var renderer:IVisualElement;
			var i:int;
			for (i = arr.length - 1; i >= 0;  i--) {
				renderer = getRendererFromDisplay(arr[i]);
				if (renderer != null) {
					if (!(ignoreDiagramRenderer && renderer is DiagramRenderer)) {
						return renderer;
					}					
				}
			}
			return null;
		}
		
		protected function getRendererFromDisplay(obj:Object):IVisualElement {			
			// in order for us to traverse its hierrarchy
			// it has to be a DisplayObject
			if (!(obj is DisplayObject)) {
				return null;
			}
			
			// traverse all the obj's hierarchy	
			while (obj != null) {
				if (obj is DiagramRenderer) {
					return IVisualElement(obj);
				}
				if (obj is IDataRenderer && diagramShell.modelToExtraInfoMap[IDataRenderer(obj).data] != null) {
					// found it
					return IVisualElement(obj);					
				}
				obj = DisplayObject(obj).parent;
			}
			
			// no found on the obj's hierarchy
			return null;
		}
		
		protected function globalToDiagram(x:Number, y:Number):Point { 
			var localPoint:Point = diagramRenderer.globalToLocal(new Point(x, y));
			localPoint = diagramRenderer.localToContent(localPoint);
			return localPoint;
		}
		
		public function reset():void {			
		}
	}	
	
}