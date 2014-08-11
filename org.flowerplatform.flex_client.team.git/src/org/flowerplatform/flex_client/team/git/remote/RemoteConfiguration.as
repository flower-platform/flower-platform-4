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
package org.flowerplatform.flex_client.team.git.remote {
	import mx.collections.ArrayCollection;
	
	/**
	 *	@author Cristina Constantinescu
	 */ 
	[RemoteClass(alias="org.flowerplatform.team.git.remote.RemoteConfiguration")]
	[Bindable]
	public class RemoteConfiguration {
		
		public var name:String;
				
		public var uri:String;
		
		public var fetchMappings:ArrayCollection;
		
		public var pushMappings:ArrayCollection;		
		
	}
}