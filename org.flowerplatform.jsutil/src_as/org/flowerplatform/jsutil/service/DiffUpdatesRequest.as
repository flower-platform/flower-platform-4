/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
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