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
package org.flowerplatform.flex_client.properties.remote {
	import mx.collections.ArrayCollection;
	import mx.utils.StringUtil;
	
	import org.flowerplatform.flexutil.controller.AbstractController;

	/**
	 * @author Razvan Tache
	 * @author Cristina Constantinescu
	 * @author Sebastian Solomon
	 */
	[Bindable]
	[RemoteClass(alias="org.flowerplatform.core.node.remote.PropertyDescriptor")]	
	public class PropertyDescriptor extends AbstractController {
		
		public var name:String;
		
		public var title:String;
		
		public var value:Object;
		
		public var readOnly:Boolean;
		
		public var type:String;
		
		public var category:String;
		
		public var hasChangeCheckbox:Boolean;
		
		public var possibleValues:ArrayCollection;
		
		public var contributeToCreation:Boolean;
		
		public var isMandatory:Boolean;
		
		override public function toString():String {
			return StringUtil.substitute("PropertiesDescriptor [name = {0}, title = {1}, category = {2}, " +
				"\ntype = {3}, readOnly = {4}, possibleValues = {5}, " +
				"\ncontributeToCreation = {6}, isMandatory = {7}, orderIndex = {8}]", 
				name, title, category,
				type, readOnly, possibleValues,
				contributeToCreation, isMandatory, orderIndex);
		}
		
	}
}
