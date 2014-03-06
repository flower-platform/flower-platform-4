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
	import mx.collections.ArrayCollection;
	import mx.events.FlexEvent;
	import mx.events.PropertyChangeEvent;
	
	import spark.components.CheckBox;
	import spark.components.DropDownList;
	import spark.components.Label;
	import spark.events.DropDownEvent;
	
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class DropDownListPropertyRenderer extends BasicPropertyRenderer {
		
		[Bindable]
		public var dropDownList:spark.components.DropDownList;
		
		[Bindable]
		public var changeCheckBox:CheckBox;
		
		[Bindable]
		public var changeLabel:Label;
		
		/**
		 * Signature: function getDataProviderHandler(callbackObject:Object, callbackFunction:Function):void		 
		 */ 
		public var requestDataProviderHandler:Function;
		
		/**
		 * Signature: function myLabelFunction(item:Object):String
		 */ 
		public var labelFunction:Function;
		
		/**
		 * Signature: function getItemIndexFromList(item:Object, list:ArrayCollection):int
		 */ 
		public var getItemIndexFromList:Function;
		
		public function DropDownListPropertyRenderer() {
			super();
		}
		
		private function creationCompleteHandler(event:FlexEvent):void {			
			dropDownList.addEventListener(DropDownEvent.CLOSE, dropDownEventHandler);
		}
		
		protected function propertyChangeHandler(e:PropertyChangeEvent):void {
			setSelectedIndex();
		}
		
		protected function dropDownEventHandler(e:DropDownEvent):void {
			saveProperty(null);
		}
		
		
		/**
		 * @author Cristina Constantinescu
		 * @author Sebastian Solomon
		 */
		override protected function createChildren():void {			
			super.createChildren();
			
			if (FlexUtilGlobals.getInstance().isMobile) {
//				dropDownList = new com.flextras.mobile.dropDownList.DropDownList();										
			} else {
				dropDownList = new spark.components.DropDownList();											
			}
						
			dropDownList.percentWidth = 100;
			dropDownList.percentHeight = 100;		
			dropDownList.labelFunction = labelFunction;

			//get data to fill dropDownList
			requestDataProviderHandler(this, requestDataProviderCallbackHandler);
			
			addElement(dropDownList);	
			changeCheckBox = new CheckBox();
			addElement(changeCheckBox);
			changeLabel = new Label();
			changeLabel.text = "Change";
			addElement(changeLabel);
		}
		
		override public function set data(value:Object):void {
			super.data = value;			
			dropDownList.enabled = !PropertyDescriptor(data).readOnly;
			
			setSelectedIndex();
			
			changeCheckBox.visible = PropertyDescriptor(data).hasChangeCheckbox;
			changeCheckBox.enabled = changeCheckBox.selected;
			
			addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
		}

		private function requestDataProviderCallbackHandler(result:ArrayCollection):void {
			dropDownList.dataProvider = result;		
			setSelectedIndex();
		}
		
		/**
		 * Called after the data provider for the dropDownList is set, and after the data is set.
		 * 
		 * @author Mariana Gheorghe
		 */
		private function setSelectedIndex():void {
			if (data != null && dropDownList.dataProvider != null) {
				dropDownList.selectedIndex = getItemIndexFromList(PropertyDescriptor(data).value, dropDownList.dataProvider);
			}
		}
		
		override protected function getValue():Object {
			return dropDownList.selectedItem;	
		}
		
	}
}