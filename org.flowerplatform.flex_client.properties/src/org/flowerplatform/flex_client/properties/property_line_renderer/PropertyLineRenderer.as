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
package org.flowerplatform.flex_client.properties.property_line_renderer {
	import mx.core.UIComponent;
	
	import spark.components.FormItem;
	import spark.components.HGroup;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.remote.PropertyWrapper;
	import org.flowerplatform.flex_client.properties.PropertiesPlugin;
	import org.flowerplatform.flex_client.properties.property_renderer.IPropertyRenderer;
	import org.flowerplatform.flexutil.properties.PropertyDescriptor;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class PropertyLineRenderer extends FormItem implements IPropertyLineRenderer {
						
		public var savePropertyEnabled:Boolean = true;
		
		protected var rendererArea:HGroup;
		
		protected var renderer:IPropertyRenderer;
		
		private var _propertyDescriptor:PropertyDescriptor;
		private var _node:Node;
				
		public function get propertyDescriptor():PropertyDescriptor {
			return _propertyDescriptor;
		}
				
		public function set propertyDescriptor(value:PropertyDescriptor):void {				
			if (_propertyDescriptor == value) {
				return;
			}
			_propertyDescriptor = value;
			
			propertyDescriptorUpdated();
		}
		
		protected function propertyDescriptorUpdated():void {
			// use title as label if set
			this.label = _propertyDescriptor.label != null ? _propertyDescriptor.label : _propertyDescriptor.name;			
			
			if (renderer != null) {
				renderer.propertyDescriptorChangedHandler();
			}
		}
		
		public function get node():Node {
			return _node;
		}
		
		public function set node(value:Node):void {			
			_node = value;				
						
			nodeUpdated();
		}
		
		public function nodeUpdated():void {
			if (renderer != null && node != null) {
				renderer.valueChangedHandler();
			}
		}
		
		override protected function createChildren():void {				
			super.createChildren();
			styleName = "propertyFormItemOld";
			UIComponent(labelDisplay).setStyle("fontWeight", "normal");	
			
			rendererArea = new HGroup();
			rendererArea.percentHeight = 100;
			rendererArea.percentWidth = 100;
			rendererArea.gap = 15;
			rendererArea.verticalAlign = "middle";
			this.addElement(rendererArea);
			
			renderer = PropertiesPlugin.getInstance().getNewPropertyRendererInstance(propertyDescriptor.type);
			UIComponent(renderer).percentWidth = 100;
			UIComponent(renderer).percentHeight = 100;
			renderer.propertyLineRenderer = this;	
						
			rendererArea.addElement(UIComponent(renderer));
						
			propertyDescriptorUpdated();
			nodeUpdated();
		}		
		
		public function commit(callbackHandler:Function = null):void {			
			if (!savePropertyEnabled || propertyDescriptor.readOnly || !renderer.isValidValue()) {
				return;
			}
			
			var oldPropertyValue:Object = node.getPropertyValue(propertyDescriptor.name);
			var newPropertyValue:Object = renderer.valueToCommit;
			var propertyValueOrWrapper:Object = node.getPropertyValueOrWrapper(propertyDescriptor.name);
						
			if (oldPropertyValue != newPropertyValue) {
				var newPropertyValueOrWrapper:Object = prepareCommit(propertyValueOrWrapper, newPropertyValue);
				
				CorePlugin.getInstance().serviceLocator.invoke(
					"nodeService.setProperty", 
					[node.nodeUri, propertyDescriptor.name, newPropertyValueOrWrapper], callbackHandler);
			}	
		}
		
		protected function prepareCommit(propertyValueOrWrapper:Object, newPropertyValue:Object):Object {
			// set new value
			if (propertyValueOrWrapper is PropertyWrapper) {
				PropertyWrapper(propertyValueOrWrapper).value = newPropertyValue;
			} else {
				propertyValueOrWrapper = newPropertyValue;
			}
			return propertyValueOrWrapper;
		}
		
		public function get value():Object {
			return renderer.valueToCommit;
		}
		
	}
}