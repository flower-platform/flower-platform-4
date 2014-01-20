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
package com.crispico.flower.util.layout.persistence {
	import org.flowerplatform.flexutil.layout.LayoutData;
	
	/**
	 * Interface that needs to be implemented in order to provide serialization/deserialization support for windows layout.
	 * Objects that implements this interface must provide functionality for the following methods:
	 * <ul>
	 * 	<li> serialize() - serializes a given <code>LayoutData</code> and returns corresponding string.
	 * 	<li> deserialize() - deserialize a given string and returns corresponding <code>LayoutData</codE>
	 * </ul>
	 */
	public interface ILayoutSerializer {
		
		   function serialize(layoutData:LayoutData):String;
		
		   function deserialize(data:String):LayoutData;
	}
	
}