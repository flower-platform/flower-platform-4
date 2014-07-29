package org.flowerplatform.flexdiagram.tool {
	import flash.events.MouseEvent;
	
	import mx.collections.IList;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.IAction;

	public class ActionTool extends Tool implements IWakeUpableTool {
		
		public var eventType:String;
		public var action:IAction;
		
		public function ActionTool(diagramShell:DiagramShell, event:String, action:IAction){
			super(diagramShell);
			this.action = action;
			this.eventType = event;
			
			WakeUpTool.wakeMeUpIfEventOccurs(diagramShell, this, eventType);
		}
		
		public function wakeUp(eventType:String, initialEvent:MouseEvent):Boolean {
			context.shellContext = diagramShell.getNewDiagramShellContext();
			if(eventType == this.eventType){
				return true;
			}	
			return false;
		}
		
		override public function activateAsMainTool():void {
			action.selection = IList(FlexUtilGlobals.getInstance().selectionManager.activeSelectionProvider);
			if(action.visible){
				action.run();
			}
			deactivateAsMainTool();
			
		}
	}
}