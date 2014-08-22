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