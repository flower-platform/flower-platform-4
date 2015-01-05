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
package org.flowerplatform.flex_client.properties.property_renderer {
	
	import flash.utils.getDefinitionByName;
	
	import mx.collections.IList;
	import mx.events.FlexEvent;
	
	import org.flowerplatform.flex_client.properties.PropertiesPlugin;
	import org.flowerplatform.flex_client.properties.property_line_renderer.PropertyLineRenderer;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Pair;
	
	import spark.components.DropDownList;
	import spark.components.Group;
	import spark.events.IndexChangeEvent;
	
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
	public class DropDownListPropertyRenderer extends Group implements IPropertyRenderer {
		
		protected var _propertyLineRenderer:PropertyLineRenderer;
		
		[Bindable]
		public var dropDownList:spark.components.DropDownList;
		
		/**
		 * Signature: function requestDataProviderHandler(node:Node, callbackFunction:Function):void
		 */ 
		public var requestDataProviderHandler:Function;
		
		/**
		 *  Signature: function myLabelFunction(item:Object):String
		 */ 
		public var labelFunction:Function;
		
		public function DropDownListPropertyRenderer() {
			super();
			addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
		}
		
		private function creationCompleteHandler(event:FlexEvent):void {
			dropDownList.addEventListener(IndexChangeEvent.CHANGE, dropDownEventHandler);	
			dropDownList.labelFunction = labelFunction;
		}
		
		protected function dropDownEventHandler(e:IndexChangeEvent):void {
			_propertyLineRenderer.commit();
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
				
		protected function requestDataProvider():void {
			if (_propertyLineRenderer.propertyDescriptor.possibleValues == null) {
				requestDataProviderHandler(_propertyLineRenderer.node, requestDataProviderCallbackHandler);
				return;
			}
			dropDownList.dataProvider = _propertyLineRenderer.propertyDescriptor.possibleValues;
			// if list of Pairs, use item.b as label
			var listItem:Object = dropDownList.dataProvider.getItemAt(0);
			if (listItem is Pair) {
				dropDownList.labelField = "b";
			}			
			valueChangedHandler();
		}
		
		protected function requestDataProviderCallbackHandler(dataProvider:IList):void {
			dropDownList.dataProvider = dataProvider;
			valueChangedHandler();
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
				
		public function isValidValue():Boolean {	
			return true;
		}
		
		public function set propertyLineRenderer(value:PropertyLineRenderer):void {
			_propertyLineRenderer = value;			
		}			
		
		public function valueChangedHandler():void {
			if (dropDownList.dataProvider != null && _propertyLineRenderer.node != null) {
				dropDownList.selectedIndex = getItemIndexFromList(_propertyLineRenderer.node.getPropertyValue(_propertyLineRenderer.propertyDescriptor.name), dropDownList.dataProvider);
			}					
		}
		
		public function propertyDescriptorChangedHandler():void {
			enabled = !_propertyLineRenderer.propertyDescriptor.readOnly;
			requestDataProvider();
		}
		
		public function get valueToCommit():Object {
			if (dropDownList.selectedItem is Pair) {
				return Pair(dropDownList.selectedItem).a;
			}
			return dropDownList.selectedItem;	
		}	
		
	}
}
