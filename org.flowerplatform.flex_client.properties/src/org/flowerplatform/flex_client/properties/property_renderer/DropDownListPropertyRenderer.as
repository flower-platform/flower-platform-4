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
	import spark.events.IndexChangeEvent;
	
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Pair;
	
	/**
	 * Supported dataProvider item class:
	 * <ul>
	 * 	<li> String
	 * 	<li> Pair -> a = key, b = label
	 * </ul>
	 * 
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
			dropDownList.addEventListener(IndexChangeEvent.CHANGE, dropDownEventHandler);
		}
		
		protected function valueChanged(value:Object = null):void {
			if (data != null && dropDownList.dataProvider != null) {
				dropDownList.selectedIndex = getItemIndexFromList(PropertyDescriptor(data).value, dropDownList.dataProvider);
			}
		}
		
		protected function dropDownEventHandler(e:IndexChangeEvent):void {
			saveProperty();
		}
		
		override protected function createChildren():void {			
			if (FlexUtilGlobals.getInstance().isMobile) {
				// component for mobile
				// this class is in the MobileComponents lib from flexutil
				var mobileClass:Class = Class(getDefinitionByName("com.flextras.mobile.dropDownList.DropDownList"));
				dropDownList = new mobileClass();
			} else {
				dropDownList = new spark.components.DropDownList();											
			}
			
			dropDownList.percentWidth = 100;
			dropDownList.percentHeight = 100;		
			
			addElement(dropDownList);		
			super.createChildren();
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
				// if list of Pairs, use item.b as label
				var listItem:Object = dropDownList.dataProvider.getItemAt(0);
				if (listItem is Pair) {
					dropDownList.labelField = "b";
				}
			}
			valueChanged();
		}
		
		protected function getItemIndexFromList(itemKey:Object, list:IList):int {
			if (itemKey != null) {
				for (var i:int = 0; i < list.length; i++) {
					var listItem:Object = list.getItemAt(i);
					if (listItem is Pair) {
						if (listItem.a == itemKey) {
							return i;
						}
					} else if (itemKey == listItem) {
						return i;
					}
				}
			}
			return -1;
		}
		
		override protected function getValue():Object {
			if (dropDownList.selectedItem is Pair) {
				return Pair(dropDownList.selectedItem).a;
			}
			return dropDownList.selectedItem;	
		}
		
	}
}