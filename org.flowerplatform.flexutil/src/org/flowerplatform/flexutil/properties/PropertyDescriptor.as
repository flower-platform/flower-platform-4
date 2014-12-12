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
	import mx.collections.IList;
	
	import org.flowerplatform.flexutil.controller.AbstractController;

	/**
	 * @author Cristina Constantinescu
	 * @author Cristian Spiescu
	 */
	[Bindable]
	public class PropertyDescriptor extends AbstractController {
		
		public var name:String;		
		public var label:String;
								
		public var type:String;		
		public var group:String;
		
		public var readOnly:Boolean;	
		
		public var writeableOnCreate:Boolean;
		
		// not yet used by properties2
		public var propertyLineRenderer:String;
		
		// not yet used by properties2
		public var defaultValue:Object;
		
		// not yet used by properties2
		public var possibleValues:IList;
		
		public function setName(value:String):PropertyDescriptor {		
			name = value;
			return this;
		}
		
		public function setLabel(value:String):PropertyDescriptor {
			label = value;
			return this;
		}
		
		public function setType(value:String):PropertyDescriptor {
			type = value;
			return this;
		}
		
		public function setGroup(value:String):PropertyDescriptor {
			group = value;
			return this;
		}
		
		public function setReadOnly(value:Boolean):PropertyDescriptor {
			readOnly = value;
			return this;
		}
		
		public function setWriteableOnCreate(value:Boolean):PropertyDescriptor {
			writeableOnCreate = value;
			return this;
		}
		
		public function setPropertyLineRenderer(value:String):PropertyDescriptor {
			propertyLineRenderer = value;
			return this;
		}
		
		public function setDefaultValue(value:Object):PropertyDescriptor {
			defaultValue = value;
			return this;
		}
		
		public function setPossibleValues(value:IList):PropertyDescriptor {
			possibleValues = value;
			return this;
		}
		
		public function isReadOnlyDependingOnMode(createMode:Boolean):Boolean {
			return createMode && readOnly && !writeableOnCreate || !createMode && readOnly;
		}
					
		override public function toString():String {
			return "PropertyDescriptor [name=" + name + ", title=" + label
				+ ", type=" + type + ", category=" + group
				+ ", propertyLineRenderer=" + propertyLineRenderer
				+ ", writeableOnCreate=" + writeableOnCreate
				+ ", readOnly=" + readOnly
				+ ", possibleValues=" + possibleValues + ", defaultValue="
				+ defaultValue + "]";
		}
		
	}
}
