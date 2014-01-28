package org.flowerplatform.flexdiagram.event {
	import flash.events.Event;
	
	/**
	 * Dispatched by <code>DiagramShell</code> to show creation options (e.g. a menu with actions).
	 * 
	 * @author Cristina Constantinescu
	 */ 
	public class ExecuteDragToCreateEvent extends Event	{
		
		public static const DRAG_TO_CREATE_EVENT:String = "dragToCreateEvent";
		
		public var context:Object;
		
		/**
		 * If true, the tool will not be deactivated after dispatching this event. 
		 */ 
		public var shouldFinishToolJobAfterExecution:Boolean;
		
		public function ExecuteDragToCreateEvent(context:Object = null, shouldFinishToolJobAfterExecution:Boolean = false)	{
			super(DRAG_TO_CREATE_EVENT);
			this.context = context;
			this.shouldFinishToolJobAfterExecution = shouldFinishToolJobAfterExecution;
		}
		
	}
}