package org.flowerplatform.flexutil.shortcut {
	import flash.events.Event;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class AssignShortcutForActionEvent extends Event {
		
		public static const ASSIGN_SHORTCUT_FOR_ACTION:String = "assignShortcutForActionEvent";
		
		public var actionId:String;
		
		public function AssignShortcutForActionEvent(actionId:String) {
			super(ASSIGN_SHORTCUT_FOR_ACTION, bubbles, cancelable);
			this.actionId = actionId;
		}
		
	}
}