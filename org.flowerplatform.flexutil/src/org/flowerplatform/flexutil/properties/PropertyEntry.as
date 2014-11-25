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
package org.flowerplatform.flexutil.properties {
	import flash.events.IEventDispatcher;
	
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;

	/**
	 * Model for the lines in the properties form.
	 * 
	 * @author Cristian Spiescu
	 */
	public class PropertyEntry {
		public var typeDescriptorRegistry:TypeDescriptorRegistry;
		public var model:Object;
		public var eventDispatcher:IEventDispatcher;
		public var descriptor:PropertyDescriptor;
		public var value:Object;
		public var isGroup:Boolean;
		public var context:Object;
	}
}