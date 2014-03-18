package org.flowerplatform.flex_client.core.event {
	import flash.events.Event;
	
	/**
	 * Dispatched by <code>FlexGlobals.topLevelApplication</code> to notify listeners
	 * that <code>CorePlugin.globalMenuActionProvider</code> has been modified.
	 * 
	 * @author Cristina Constantinescu
	 */ 
	public class GlobalActionProviderChangedEvent extends Event {
		
		public static const ACTION_PROVIDER_CHANGED:String = "globalActionProviderChanged";
				
		public function GlobalActionProviderChangedEvent() {
			super(ACTION_PROVIDER_CHANGED);
		}
		
	}
}