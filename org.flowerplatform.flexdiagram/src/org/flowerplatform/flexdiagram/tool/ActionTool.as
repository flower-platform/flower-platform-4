package org.flowerplatform.flexdiagram.tool {
	import flash.events.MouseEvent;
	
	import org.flowerplatform.flexdiagram.DiagramShell;

	public class ActionTool extends Tool implements IWakeUpableTool {
		
		public function ActionTool(diagramShell:DiagramShell){
			super(diagramShell);
		}
		
		public function wakeUp(eventType:String, initialEvent:MouseEvent):Boolean {
			return false;
		}
		
		override public function activateAsMainTool():void {
			
		}
	}
}