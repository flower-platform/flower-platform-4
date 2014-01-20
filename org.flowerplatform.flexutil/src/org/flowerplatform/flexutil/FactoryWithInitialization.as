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
package org.flowerplatform.flexutil {
	
	public class FactoryWithInitialization {
		
		public var generator:Class;
		public var properties:Object = null;
		
		public function FactoryWithInitialization(generator:Class, properties:Object = null) {
			this.generator = generator;
			this.properties = properties;
		}
		
		public function newInstance(constructorHasParameter:Boolean = false, construtorParameter:Object = null):* {
			var instance:Object;
			if (constructorHasParameter) {
				instance = new generator(construtorParameter);
			} else {
				instance = new generator();
			}
			
			if (properties != null)
			{
				for (var p:String in properties)
				{
					instance[p] = properties[p];
				}
			}
			
			return instance;
		}
	}
}