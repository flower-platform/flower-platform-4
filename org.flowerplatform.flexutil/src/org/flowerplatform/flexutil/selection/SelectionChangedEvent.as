package org.flowerplatform.flexutil.selection {
	import flash.events.Event;
	
	import mx.collections.IList;
	
	
	/**
	 * @see SelectionManager
	 * @author Cristian Spiescu
	 */
	public class SelectionChangedEvent extends Event {
		
		public static const SELECTION_CHANGED:String = "selectionChanged";
		
		public var selectionProvider:ISelectionProvider;
		
		public var selection:IList;
		
		public var selectionForServer:IList;
		
		public function SelectionChangedEvent() {
			super(SELECTION_CHANGED);
		}
	}
}