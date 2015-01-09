package org.flowerplatform.jsutil.service {
	
	
	/**
	 * @author Claudiu Matei
	 * 
	 */
	[RemoteClass(alias="org.flowerplatform.util.diff_update.DiffUpdatesRequest")]
	public class DiffUpdatesRequest {

		private var _notificationChannelsData:Array;
		
		public function DiffUpdatesRequest(notificationChannelsData:Array) {
			_notificationChannelsData = notificationChannelsData;
		}
		
		
		public function get notificationChannelsData():Array {
			return _notificationChannelsData;
		}

		public function set notificationChannelsData(value:Array):void {
			_notificationChannelsData = value;
		}

	}
	
}