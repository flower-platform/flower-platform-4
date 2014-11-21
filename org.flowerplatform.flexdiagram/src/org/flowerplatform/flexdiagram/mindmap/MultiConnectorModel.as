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
package org.flowerplatform.flexdiagram.mindmap {
	import flash.events.Event;
	import flash.events.IEventDispatcher;
	
	import mx.collections.IList;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelController;

	/**
	 * @author Cristian Spiescu
	 */
	[Bindable]
	public class MultiConnectorModel {
		
		public static const TYPE:String = "multiConnectorModel";
		
		private var _source:IEventDispatcher;
		
		/**
		 * Needed for root, to check the side of the children (within the renderer).
		 */
		public var isForRoot:Boolean;
		
		/**
		 * Needed for root, that has 2 MultiConnectorModels
		 */
		public var isRight:Boolean = true;
		
		public var x:Number;
		
		public var y:Number;
		
		public var width:Number;
		
		public var height:Number;
		
		public var diagramShellContext:DiagramShellContext;
	
		public function get source():Object {
			return _source;
		}

		public function set source(value:Object):void {
			if (_source != null) {
				_source.removeEventListener(MindMapDiagramShell.COORDINATES_CHANGED_EVENT, sourceModelChangedHandler);	
			}
			_source = IEventDispatcher(value);
			if (_source != null) {
				_source.addEventListener(MindMapDiagramShell.COORDINATES_CHANGED_EVENT, sourceModelChangedHandler);
				sourceModelChangedHandler(null);
			}
		}
		
		protected function sourceModelChangedHandler(e:Event):void {
			var mindMapDiagramShell:MindMapDiagramShell = MindMapDiagramShell(diagramShellContext.diagramShell);

			if (isRight) {
				x = mindMapDiagramShell.getPropertyValue(diagramShellContext, source, "x") + mindMapDiagramShell.getPropertyValue(diagramShellContext, source, "width") ;
			} else {
				x = mindMapDiagramShell.getPropertyValue(diagramShellContext, source, "x") - width;
			}
			
			width = MindMapDiagramShell(diagramShellContext.diagramShell).horizontalPadding;

			var mindMapModelController:MindMapModelController = mindMapDiagramShell.getModelController(diagramShellContext, source);
			var children:IList = mindMapModelController.getChildren(diagramShellContext, source);
			
			var firstChild:Object, lastChild:Object;
			if (!isForRoot) {
				if (children != null && children.length > 0) {
					// i.e. all children on same side; pick first and last
					firstChild = children.getItemAt(0);
					lastChild = children.getItemAt(children.length - 1);
				}
			} else {
				// children are on both sides; search for the first and last
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children.getItemAt(i);
					var childIsRight:Boolean = mindMapDiagramShell.getModelController(diagramShellContext, child).getSide(diagramShellContext, child) == MindMapDiagramShell.POSITION_RIGHT;
					if (isRight == childIsRight) {
						lastChild = child;
						if (firstChild == null) {
							firstChild = child;
						}
					}
				}
			}
			
			if (firstChild == null || lastChild == null) {
				// both conditions above can happen (simoultaneously), for root, where all children are on one side
				y = mindMapDiagramShell.getPropertyValue(diagramShellContext, source, "y");
				height = 0;
			} else {
				y = mindMapDiagramShell.getPropertyValue(diagramShellContext, firstChild, "y");
				height = mindMapDiagramShell.getPropertyValue(diagramShellContext, lastChild, "y") + mindMapDiagramShell.getPropertyValue(diagramShellContext, lastChild, "height") - y;
			}

		}

		public function toString():String {
			return "MultiConnectorModel for " + source;
		}
	}
}