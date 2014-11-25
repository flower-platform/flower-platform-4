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
package org.flowerplatform.flex_client.core.editor.remote {
	import mx.utils.StringUtil;
	
	import org.flowerplatform.flexutil.controller.AbstractController;
	
	/**
	 * @author Mariana Gheorghe
	 */
	[RemoteClass(alias="org.flowerplatform.core.node.remote.AddChildDescriptor")]
	public class AddChildDescriptor extends AbstractController {
		
		public var childType:String;
		
		public var dynamicChildType:String;
		
		public var label:String;
		
		public var icon:String;
		
		override public function toString():String {
			return StringUtil.substitute("AddChildDescriptor [childType = {0}, label = {1}, orderIndex = {2}]", 
				childType, label, orderIndex)
		}
	}
}