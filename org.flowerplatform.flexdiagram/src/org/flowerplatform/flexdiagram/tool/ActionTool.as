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
package org.flowerplatform.flexdiagram.tool {
	import flash.events.MouseEvent;
	
	import mx.collections.IList;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.IAction;

	/**
	 * @author Diana Balutoiu
	 */
	public class ActionTool extends Tool implements IWakeUpableTool {
		
		public var action:IAction;
		
		private var _eventType:String;
		
		public function ActionTool(diagramShell:DiagramShell) {
			super(diagramShell);				
		}
		
		public function get eventType():String {
			return _eventType;
		}

		public function set eventType(value:String):void {
			_eventType = value;
			WakeUpTool.wakeMeUpIfEventOccurs(diagramShell, this, eventType);
		}
		
		public function wakeUp(eventType:String, initialEvent:MouseEvent):Boolean {
			return eventType == this.eventType;
		}
		
		override public function activateAsMainTool():void {
			FlexUtilGlobals.getInstance().actionHelper.runAction(
				action, 
				FlexUtilGlobals.getInstance().selectionManager.activeSelectionProvider.getSelection(), 
				diagramShell.getNewDiagramShellContext(),
				true);
			
			diagramShell.mainToolFinishedItsJob();
		}
	}
}