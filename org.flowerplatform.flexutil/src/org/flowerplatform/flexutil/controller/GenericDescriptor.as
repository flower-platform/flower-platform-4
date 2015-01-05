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
package org.flowerplatform.flexutil.controller {
	
	/**
	 * @author Cristian Spiescu
	 */
	[RemoteClass(alias="org.flowerplatform.util.controller.GenericDescriptor")]
	public class GenericDescriptor extends AbstractController {

		public var value:Object;
		
		/**
		 * An optional map.
		 */
		public var extraInfo:Object;
		
		public function GenericDescriptor(value:Object=null, extraInfo:Object=null, orderIndex:int=0) {
			super(orderIndex);
			this.value = value;
			this.extraInfo = extraInfo;
		}
		
		public function getExtraInfoProperty(property:String):Object {
			if (extraInfo == null) {
				return null;
			}
			return extraInfo[property];
		}
		
		override public function toString():String {
			return super.toString() + "[value = " + value + "]";
		}		
	
		public function addExtraInfoProperty(key:String, value:String):GenericDescriptor {
			if (extraInfo == null) {
				extraInfo = new Object();
			}
			extraInfo[key] = value;
			return this;
		}
	}
}