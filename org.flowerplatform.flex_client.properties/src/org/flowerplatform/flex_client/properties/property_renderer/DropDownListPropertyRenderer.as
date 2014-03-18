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
package org.flowerplatform.flex_client.properties.property_renderer {
	
	import flash.utils.getDefinitionByName;
	
	import mx.binding.utils.BindingUtils;
	import mx.collections.IList;
	import mx.events.FlexEvent;
	
	import spark.components.DropDownList;
	import spark.events.DropDownEvent;
	
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	/**
	 * @author Cristina Constantinescu
	 * @author Mariana Gheorghe
	 */ 
	public class DropDownListPropertyRenderer extends BasicPropertyRenderer {
		
		[Bindable]
		public var dropDownList:spark.components.DropDownList;
		
		public function DropDownListPropertyRenderer() {
			super();
		}
		
		private function creationCompleteHandler(event:FlexEvent):void {		
			BindingUtils.bindSetter(valueChanged, data, "value");
			dropDownList.addEventListener(DropDownEvent.CLOSE, dropDownEventHandler);
		}
		
		protected function valueChanged(value:Object = null):void {
			if (data != null && dropDownList.dataProvider != null) {
				dropDownList.selectedIndex = getItemIndexFromList(PropertyDescriptor(data).value, dropDownList.dataProvider);
			}
		}
		
		protected function dropDownEventHandler(e:DropDownEvent):void {
			saveProperty(null);
		}
		
		override protected function createChildren():void {			
			super.createChildren();
			
			if (FlexUtilGlobals.getInstance().isMobile) {
				var mobileClass:Class = Class(getDefinitionByName("com.flextras.mobile.dropDownList.DropDownList"));
				dropDownList = new mobileClass();
			} else {
				dropDownList = new spark.components.DropDownList();											
			}
						
			dropDownList.percentWidth = 100;
			dropDownList.percentHeight = 100;		

			addElement(dropDownList);			
		}
		
		override public function set data(value:Object):void {
			super.data = value;			
			dropDownList.enabled = !PropertyDescriptor(data).readOnly;
			
			requestDataProvider();
			
			addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
		}

		protected function requestDataProvider():void {
			if (data is PropertyDescriptor) {
				dropDownList.dataProvider = PropertyDescriptor(data).possibleValues;
			}
			valueChanged();
		}
				
		protected function getItemIndexFromList(item:Object, list:IList):int {
			if (item != null) {
				for (var i:int = 0; i < list.length; i++) {
					var listItem:Object = list.getItemAt(i);
					if (item == listItem) {
						return i;
					}
				}
			}
			return -1;
		}
		
		override protected function getValue():Object {
			return dropDownList.selectedItem;	
		}
		
	}
}