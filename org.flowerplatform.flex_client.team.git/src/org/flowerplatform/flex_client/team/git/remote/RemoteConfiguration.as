/* license-start
* 
* Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
package  org.flowerplatform.flex_client.team.git.remote {
	import mx.collections.ArrayCollection;
	
	/**
	 *	@author Cristina Constantinescu
	 */ 
	[Bindable]
	[RemoteClass(alias="org.flowerplatform.team.git.remote.RemoteConfiguration")]
	public class RemoteConfiguration {
		
		private var _name:String;
		
		private var _uri:String;
		
		private var _fetchMappings:ArrayCollection;
		
		private var _pushMappings:ArrayCollection;
		
		private var _type:int;
		
		public function set name(value:String):void {
			_name = value;
		}
		
		public function set uri(value:String):void {
			_uri = value;
		}
		
		public function set fetchMappings(value:ArrayCollection):void {
			_fetchMappings = value;
		}
		
		public function set pushMappings(value:ArrayCollection):void {
			_pushMappings = value;
		}
		
		public function get name():String {
			return _name;
		}
		
		public function get uri():String {
			return _uri;
		}
		
		public function get fetchMappings():ArrayCollection {
			return _fetchMappings;
		}
		
		public function get pushMappings():ArrayCollection {
			return _pushMappings;
		}
	}
}


