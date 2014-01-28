package org.flowerplatform.flexdiagram.event {
	import flash.events.Event;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class ZoomPerformedEvent extends Event {
		
		public static const ZOOM_PERFORMED:String = "zoom_performed";
		
		public var zoomPercent:Number;
		
		public function ZoomPerformedEvent(zoomPercent:Number = 1) {
			super(ZOOM_PERFORMED);
			this.zoomPercent = zoomPercent;
		}
	}
}