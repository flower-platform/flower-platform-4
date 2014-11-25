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
package org.flowerplatform.flexutil.samples.properties {
	import mx.utils.ObjectProxy;

	/**
	 * @author Cristian Spiescu
	 */
	[Bindable]
	public class SamplePropertiesModel {
		public var text:String;
		
		public var properties:ObjectProxy = new ObjectProxy({ 
			dynamicProperty1: "dynamicProperty1", 
			dynamicProperty2: "dynamicProperty2",
			hasGroupWithoutGroupDescriptor1: "value"
		});
		
		public function toString():String {
			return text;
		}
	}
}