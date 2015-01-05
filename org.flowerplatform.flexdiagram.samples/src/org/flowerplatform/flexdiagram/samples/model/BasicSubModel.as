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
package org.flowerplatform.flexdiagram.samples.model {
	
	[Bindable]
	/**
	 * @author Cristian Spiescu
	 */
	public class BasicSubModel {

		public var name:String = "Default Text";
		
		public var parent:BasicModel;
		
		public function BasicSubModel(name:String, parent:BasicModel) {
			this.name = name;
			this.parent = parent;
		}
		
		public function toString():String {
			return "BasicSubModel[" + name + "]";
		}
		
	}
}