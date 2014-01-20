package org.flowerplatform.flexutil.action {
	import flash.events.Event;
	
	/**
	 * Dispatched when the menu is closed, 
	 * by the menu system specific to the platform (web: ContextMenu; mobile: MobileHostBase). 
	 * 
	 * The event is dispatched on the top level app.
	 * 
	 * @author Cristina Constantinescu
	 */ 
	public class MenuClosedEvent extends Event {
		
		public static const MENU_CLOSED:String = "MENU_CLOSED";
				
		public function MenuClosedEvent() {
			super(MENU_CLOSED);
		}
	}
}