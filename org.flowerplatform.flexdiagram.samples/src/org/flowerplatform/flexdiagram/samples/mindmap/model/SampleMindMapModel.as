/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayList;
	
	/**
	 * @author Cristina Constantinescu
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
		public var fontSize:Number;
		public var fontWeight:Boolean;
		public var fontStyle:Boolean;
		
		
		public var subModelsDict:Dictionary  = new Dictionary();
		
	}	
}