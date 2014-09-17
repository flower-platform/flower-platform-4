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
package org.flowerplatform.flex_client.properties.remote {
	import mx.collections.ArrayCollection;
	import mx.utils.StringUtil;
	
	import org.flowerplatform.flexutil.controller.AbstractController;

	/**
	 * @author Cristina Constantinescu
	 * @author Sebastian Solomon
	 */
	[Bindable]
	[RemoteClass(alias="org.flowerplatform.core.node.remote.PropertyDescriptor")]	
	public class PropertyDescriptor extends AbstractController implements IPropertyDescriptor {
		
		private var _name:String;		
		private var _label:String;
								
		private var _type:String;		
		private var _category:String;
				
		private var _propertyLineRenderer:String;
		
		private var _contributesToCreation:Boolean;		
		private var _mandatory:Boolean;
		
		private var _defaultValue:Object;
		
		private var _possibleValues:ArrayCollection;
		
		private var _readOnly:Boolean;	
					
		public function get readOnly():Boolean {
			return _readOnly;
		}

		public function set readOnly(value:Boolean):void {
			_readOnly = value;
		}

		public function get possibleValues():ArrayCollection {
			return _possibleValues;
		}

		public function set possibleValues(value:ArrayCollection):void {
			_possibleValues = value;
		}

		public function get defaultValue():Object {
			return _defaultValue;
		}

		public function set defaultValue(value:Object):void {
			_defaultValue = value;
		}

		public function get mandatory():Boolean {
			return _mandatory;
		}

		public function set mandatory(value:Boolean):void {
			_mandatory = value;
		}

		public function get contributesToCreation():Boolean {
			return _contributesToCreation;
		}

		public function set contributesToCreation(value:Boolean):void {
			_contributesToCreation = value;
		}

		public function get propertyLineRenderer():String {
			return _propertyLineRenderer;
		}

		public function set propertyLineRenderer(value:String):void {
			_propertyLineRenderer = value;
		}

		public function get category():String {
			return _category;
		}

		public function set category(value:String):void {
			_category = value;
		}

		public function get type():String {
			return _type;
		}

		public function set type(value:String):void {
			_type = value;
		}

		public function get label():String {
			return _label;
		}

		public function set label(value:String):void {
			_label = value;
		}

		public function get name():String {
			return _name;
		}

		public function set name(value:String):void {
			_name = value;
		}

		override public function toString():String {
			return "PropertyDescriptor [name=" + name + ", title=" + label
				+ ", type=" + type + ", category=" + category
				+ ", propertyLineRenderer=" + propertyLineRenderer
				+ ", contributesToCreation=" + contributesToCreation
				+ ", mandatory=" + mandatory + ", readOnly=" + readOnly
				+ ", possibleValues=" + possibleValues + ", defaultValue="
				+ defaultValue + "]";
		}
		
	}
}
