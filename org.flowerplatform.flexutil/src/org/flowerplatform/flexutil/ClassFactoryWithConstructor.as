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
package org.flowerplatform.flexutil {
	import mx.core.IFactory;
	
	/**
	 * Similar to <code>ClassFactory</code>. But it allows the creation of classes that have a constructor with a parameter.
	 * 
	 * @author Cristian Spiescu
	 */
	public class ClassFactoryWithConstructor implements IFactory {
		
		public var generator:Class;
		public var properties:Object = null;
		
		public function ClassFactoryWithConstructor(generator:Class, properties:Object = null) {
			this.generator = generator;
			this.properties = properties;
		}
		
		public function newInstance():* {
			return newInstanceWithConstructorParameter(null, false);
		}
		
		public function newInstanceWithConstructorParameter(construtorParameter:Object = null, constructorHasParameter:Boolean = true):* {
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