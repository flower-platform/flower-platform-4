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
package org.flowerplatform.flex_client.properties {
	import flash.utils.Dictionary;
	
	import mx.collections.IList;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flex_client.properties.action.NewComposedAction;
	import org.flowerplatform.flex_client.properties.action.ShowPropertiesAction;
	import org.flowerplatform.flex_client.properties.property_line_renderer.CategoryPropertyLineRenderer;
	import org.flowerplatform.flex_client.properties.property_line_renderer.IPropertyLineRenderer;
	import org.flowerplatform.flex_client.properties.property_line_renderer.PropertyLineRenderer;
	import org.flowerplatform.flex_client.properties.property_line_renderer.StylablePropertyLineRenderer;
	import org.flowerplatform.flex_client.properties.property_renderer.BooleanPropertyRenderer;
	import org.flowerplatform.flex_client.properties.property_renderer.ColorPickerPropertyRenderer;
	import org.flowerplatform.flex_client.properties.property_renderer.DatePropertyRenderer;
	import org.flowerplatform.flex_client.properties.property_renderer.DropDownListPropertyRenderer;
	import org.flowerplatform.flex_client.properties.property_renderer.IPropertyRenderer;
	import org.flowerplatform.flex_client.properties.property_renderer.NumberPropertyRenderer;
	import org.flowerplatform.flex_client.properties.property_renderer.NumericStepperPropertyRenderer;
	import org.flowerplatform.flex_client.properties.property_renderer.StringPropertyRenderer;
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;
	import org.flowerplatform.flexutil.FactoryWithInitialization;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;

	/**
	 * @author Cristina Constantinescu
	 */
	public class PropertiesPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:PropertiesPlugin;
		
		public var propertyDescriptorTypeToPropertyRendererFactory:Dictionary = new Dictionary();
		public var propertyDescriptorTypeToPropertyLineRendererFactory:Dictionary = new Dictionary();
		public var propertyValueClassToPropertyDescriptorType:Dictionary = new Dictionary();
		
		public var currentSelectedNode:Node;
		
		public static function getInstance():PropertiesPlugin {
			return INSTANCE;
		}
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new PropertiesViewProvider());
			
			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(ShowPropertiesAction);
			CorePlugin.getInstance().getEditorClassFactoryActionProvider().addActionClass(NewComposedAction);
		}
		
		override public function start():void {
			super.start();
			registerPropertyProviders();
		}
		
		override protected function registerMessageBundle():void {
			// messages come from .flex_client.resources
		}
		
		override protected function registerClassAliases():void {
			registerClassAliasFromAnnotation(PropertyDescriptor);
		}
				
		private function registerPropertyProviders():void {
			propertyDescriptorTypeToPropertyRendererFactory[PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_STRING] = new FactoryWithInitialization(StringPropertyRenderer);
			propertyDescriptorTypeToPropertyRendererFactory[PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_BOOLEAN] = new FactoryWithInitialization(BooleanPropertyRenderer);
			propertyDescriptorTypeToPropertyRendererFactory[PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_NUMBER] = new FactoryWithInitialization(NumberPropertyRenderer);
			propertyDescriptorTypeToPropertyRendererFactory[PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_NUMBER_STEPPER] = new FactoryWithInitialization(NumericStepperPropertyRenderer);
			propertyDescriptorTypeToPropertyRendererFactory[PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_DROP_DOWN_LIST] = new FactoryWithInitialization(DropDownListPropertyRenderer);
			propertyDescriptorTypeToPropertyRendererFactory[PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_COLOR_PICKER] = new FactoryWithInitialization(ColorPickerPropertyRenderer);
			propertyDescriptorTypeToPropertyRendererFactory[PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_DATE] = new FactoryWithInitialization(DatePropertyRenderer);
			
			propertyDescriptorTypeToPropertyLineRendererFactory[PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_CATEGORY] = new FactoryWithInitialization(CategoryPropertyLineRenderer);
			propertyDescriptorTypeToPropertyLineRendererFactory[PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_DEFAULT] = new FactoryWithInitialization(PropertyLineRenderer);
			propertyDescriptorTypeToPropertyLineRendererFactory[PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_STYLABLE] = new FactoryWithInitialization(StylablePropertyLineRenderer);
			
			propertyValueClassToPropertyDescriptorType[Boolean] = PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_BOOLEAN;
			propertyValueClassToPropertyDescriptorType[Number] = PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_NUMBER;			
		}
		
		public function getNewPropertyLineRendererInstance(propertyLineRendererType:String):IPropertyLineRenderer {
			var propertyLineRendererFactory:FactoryWithInitialization = propertyDescriptorTypeToPropertyLineRendererFactory[propertyLineRendererType];
			if (propertyLineRendererFactory == null) {
				propertyLineRendererFactory = propertyDescriptorTypeToPropertyLineRendererFactory[PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_DEFAULT];
			}
			if (propertyLineRendererFactory == null) {
				throw new Error();
			}
			return propertyLineRendererFactory.newInstance(false);
		}
		
		public function getNewPropertyRendererInstance(type:String):IPropertyRenderer {
			var propertyRendererFactory:FactoryWithInitialization = propertyDescriptorTypeToPropertyRendererFactory[type];
			if (propertyRendererFactory == null) {
				propertyRendererFactory = propertyDescriptorTypeToPropertyRendererFactory[PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_STRING];
			}
			if (propertyRendererFactory == null) {
				throw new Error();
			}
			return propertyRendererFactory.newInstance(false);
		}
		
		/**
		 * @return the <code>PropertyDescriptor</code> associated to <code>property</code>. If no property descriptor found in registry, returns a dummy object.
		 */ 
		public function getPropertyDescriptor(node:Node, property:String, includeRawProperties:Boolean = false):PropertyDescriptor {
			var propertyDescriptor:PropertyDescriptor;
			var descriptors:IList = CorePlugin.getInstance().nodeTypeDescriptorRegistry.getExpectedTypeDescriptor(node.type).getAdditiveControllers(PropertiesConstants.PROPERTY_DESCRIPTOR, node);
			for (var i:int = 0; i < descriptors.length; i++) {
				propertyDescriptor = PropertyDescriptor(descriptors.getItemAt(i));
				if (propertyDescriptor.name == property) {
					return propertyDescriptor;
				}
			}
			if (includeRawProperties) {
				var type:String = propertyValueClassToPropertyDescriptorType[Object(node.getPropertyValue(property)).constructor];
				if (type == null) {
					var index:int = property.lastIndexOf(".");
					if (index != -1) {						
						var pd:PropertyDescriptor = getPropertyDescriptor(node, property.substring(0, index), includeRawProperties);
						if (pd != null) {
							type = pd.type;
						}
					} else {
						type = PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_STRING;
					}
				} 
				
				if (type != null) {
					propertyDescriptor = new PropertyDescriptor();
					propertyDescriptor.type = type;
					propertyDescriptor.name = property;				
					propertyDescriptor.orderIndex = int.MAX_VALUE;
					return propertyDescriptor;
				}
			}
			return null;
		}
		
	}
}
