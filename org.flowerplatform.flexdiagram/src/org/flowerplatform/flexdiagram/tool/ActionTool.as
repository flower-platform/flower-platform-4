package org.flowerplatform.flexdiagram.tool {
	import flash.events.MouseEvent;
	
	import mx.collections.IList;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.IAction;

	/**
	 * @author Diana Balutoiu
	 */
	public class ActionTool extends Tool implements IWakeUpableTool {
		
		public var action:IAction;
		private var _eventType:String;
		public static const ID:String = "ActionTool";
		
		public function ActionTool(diagramShell:DiagramShell){
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
			if(eventType == this.eventType){
				return true;
			}	
			return false;
		}
		
		override public function activateAsMainTool():void {
			var selection:IList = FlexUtilGlobals.getInstance().selectionManager.activeSelectionProvider.getSelection();
			action.selection = selection;
			if(action.visible){
				FlexUtilGlobals.getInstance().actionHelper.runAction(action, selection, diagramShell.getNewDiagramShellContext());
			}
			diagramShell.mainToolFinishedItsJob();
			deactivateAsMainTool();
		}
	}
}