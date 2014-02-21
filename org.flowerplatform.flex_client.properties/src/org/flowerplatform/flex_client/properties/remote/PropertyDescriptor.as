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
* Contributors:
*   Crispico - Initial API and implementation
*
* license-end
*/
package org.flowerplatform.flex_client.properties.remote {
	import mx.collections.ArrayCollection;

	/**
	 * @author Razvan Tache
	 * @author Cristina Constantinescu
	 */
	[Bindable]
	[RemoteClass(alias="org.flowerplatform.core.node.remote.PropertyDescriptor")]	
	public class PropertyDescriptor {

		public var name:String;
		
		public var value:Object;
		
		public var readOnly:Boolean;
		
		public var type:String;
		
		public var possibleValues:ArrayCollection;
		
	}
}