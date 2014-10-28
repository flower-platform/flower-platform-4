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
package org.flowerplatform.flexdiagram.samples.mindmap.model {
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.utils.ObjectProxy;
	
	/**
	 * @author Cristina Constantinescu
	 * @author Alexandra Topoloaga
	 */
	[Bindable]
	public class SampleMindMapModel {
		
		public var parent:Object;
		public var children:ArrayList = new ArrayList();
		public var hasChildren:Boolean;
		
		public var text:String;
		public var side:int;
		
		public var expanded:Boolean;
		
		public var fontFamily:String;
		public var fontSize:Number = 9;
		public var fontBold:Boolean;
		public var fontItalic:Boolean;
		
		public var textColor:uint;
		public var backgroundColor:uint = 0xDDDDDD;
		
		public var cloudColor:uint;
		public var cloudType:String;
		
		public var icons:String;
		
		public var note:String;
		public var details:String;
		
		public var showProperties:Boolean;
		
		public var editProperties:Boolean;
		
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