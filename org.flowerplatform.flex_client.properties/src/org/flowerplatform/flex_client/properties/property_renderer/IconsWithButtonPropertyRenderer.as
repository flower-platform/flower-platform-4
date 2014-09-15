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
	
	import flash.events.MouseEvent;
	
	import mx.controls.Spacer;
	
	import org.flowerplatform.flex_client.properties.property_line_renderer.PropertyLineRenderer;
	import org.flowerplatform.flexutil.FlowerArrayList;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.dialog.IDialogResultHandler;
	import org.flowerplatform.flexutil.renderer.IIconsComponentExtensionProvider;
	import org.flowerplatform.flexutil.renderer.IconsComponentExtension;
	
	import spark.components.Button;
	import spark.components.Group;
	import spark.layouts.HorizontalLayout;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class IconsWithButtonPropertyRenderer extends Group implements IIconsComponentExtensionProvider, IDialogResultHandler, IPropertyRenderer {

		protected var _propertyLineRenderer:PropertyLineRenderer;
		
		protected var iconsComponentExtension:IconsComponentExtension;
		
		/**
		 * Signature: function clickHandler(itemRendererHandler:IDialogResultHandler, propertyName:String, propertyValue:Object):void
		 */ 
		public var clickHandler:Function;
		
		/**
		 * Signature: function getNewIconsPropertyHandler(dialogResult:Object):String
		 */ 
		public var getNewIconsPropertyHandler:Function;
		
		private var currentValue:String;
		
		public function IconsWithButtonPropertyRenderer() {
			super();
			iconsComponentExtension = new IconsComponentExtension(this);
			
			var hLayout:HorizontalLayout = new HorizontalLayout();
			hLayout.gap = 5;
			hLayout.paddingBottom = 2;
			hLayout.paddingTop = 2;
			hLayout.paddingLeft = 2;
			hLayout.paddingRight = 2;
			hLayout.verticalAlign = "middle";
			
			this.layout = hLayout;	
		}
		
		public function getMainComponent():Group {
			return this;
		}
		
		public function get icons():FlowerArrayList {
			return iconsComponentExtension.icons;
		}
		
		public function set icons(value:FlowerArrayList):void {
			iconsComponentExtension.icons = value;
		}
		
		public function iconsChanged(value:Object):void {
			if (value != null) {
				icons = new FlowerArrayList(String(value).split(Utils.ICONS_SEPARATOR));
			} else {
				icons = null;
			}
			currentValue = value as String;
		}
		
		override protected function createChildren():void {
			super.createChildren();

			var spacer:Spacer = new Spacer();	
			spacer.percentWidth = 100;
			addElementAt(spacer, 0);
			
			var button:Button = new Button();			
			button.percentHeight = 100;
			button.label = "...";
			button.width = 32;
			button.addEventListener(MouseEvent.CLICK, clickHandlerInternal);
			
			addElementAt(button, 1);
		}
		
		private function clickHandlerInternal(event:MouseEvent):void {
			clickHandler(this, _propertyLineRenderer.propertyDescriptor.name, currentValue,this._propertyLineRenderer.node);
		}
		
		public function handleDialogResult(result:Object):void {			
			if (result == null) {
				return;
			}
			var newValue:String = Utils.computeStringTokens(currentValue, Utils.ICONS_SEPARATOR, result.type, result.iconUrl)			

			if (currentValue != newValue) {
				currentValue = newValue;
				_propertyLineRenderer.commit();
			}
		}
				
		public function newIconIndex():int {
			return numElements - 2;
		}
		
		override public function validateDisplayList():void {
			super.validateDisplayList();			
			iconsComponentExtension.validateDisplayList();
		}
		
		override public function validateProperties():void {
			super.validateProperties();
			iconsComponentExtension.validateProperties();
		}
		
		override public function validateSize(recursive:Boolean=false):void	{
			iconsComponentExtension.validateSize();
			super.validateSize(recursive);
		}
		
		public function isValidValue():Boolean {	
			return true;
		}
		
		public function set propertyLineRenderer(value:PropertyLineRenderer):void {
			_propertyLineRenderer = value;				
		}			
		
		public function get valueToCommit():Object {			
			return currentValue;
		}
		
		public function valueChangedHandler():void {
			iconsChanged(_propertyLineRenderer.node.getPropertyValue(_propertyLineRenderer.propertyDescriptor.name));			
		}
		
		public function propertyDescriptorChangedHandler():void {
			enabled = !_propertyLineRenderer.propertyDescriptor.readOnly;			
		}
		
	}
}
