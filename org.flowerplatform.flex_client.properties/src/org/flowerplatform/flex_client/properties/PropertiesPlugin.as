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
package org.flowerplatform.flex_client.properties {
	import flash.utils.Dictionary;
	
	import mx.collections.IList;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.action.ActionDescriptor;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flex_client.properties.action.NewComposedAction;
	import org.flowerplatform.flex_client.properties.action.ShowPreferencesAction;
	import org.flowerplatform.flex_client.properties.action.ShowPropertiesAction;
	import org.flowerplatform.flex_client.properties.action.SiblingComposedAction;
	import org.flowerplatform.flex_client.properties.controllers.PreferencePropertyDescriptorProvider;
	import org.flowerplatform.flex_client.properties.controllers.PropertyDescriptorProvider;
	import org.flowerplatform.flex_client.properties.controllers.RawPropertyDescriptorProvider;
	import org.flowerplatform.flex_client.properties.preferences.PreferencesViewProvider;
	import org.flowerplatform.flex_client.properties.property_line_renderer.CategoryPropertyLineRenderer;
	import org.flowerplatform.flex_client.properties.property_line_renderer.IPropertyLineRenderer;
	import org.flowerplatform.flex_client.properties.property_line_renderer.PreferenceDefaultPropertyLineRenderer;
	import org.flowerplatform.flex_client.properties.property_line_renderer.PreferenceGlobalPropertyLineRenderer;
	import org.flowerplatform.flex_client.properties.property_line_renderer.PreferenceUserPropertyLineRenderer;
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
	import org.flowerplatform.flex_client.properties.remote.IPropertyDescriptor;
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;
	import org.flowerplatform.flexutil.ClassFactoryWithConstructor;
	import org.flowerplatform.flexutil.FlexUtilConstants;
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
		
			FlexUtilGlobals.getInstance().registerAction(ShowPropertiesAction);			
			FlexUtilGlobals.getInstance().registerAction(NewComposedAction);
			FlexUtilGlobals.getInstance().registerAction(SiblingComposedAction);
			
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateCategoryTypeDescriptor(FlexUtilConstants.CATEGORY_ALL)
				.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR, new ActionDescriptor(ShowPropertiesAction.ID));
			
			CorePlugin.getInstance().getEditorClassFactoryActionProvider().addActionClass(NewComposedAction);
			CorePlugin.getInstance().getEditorClassFactoryActionProvider().addActionClass(SiblingComposedAction);
			
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new PreferencesViewProvider());
			CorePlugin.getInstance().registerActionToGlobalMenu(new ShowPreferencesAction());
		}
		
		override public function start():void {
			super.start();
			registerPropertyProviders();
			
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateCategoryTypeDescriptor(FlexUtilConstants.CATEGORY_ALL)
				.addAdditiveController(PropertiesConstants.PROPERTY_DESCRIPTOR_PROVIDER, new PropertyDescriptorProvider())
				.addAdditiveController(PropertiesConstants.PROPERTY_DESCRIPTOR_PROVIDER, new RawPropertyDescriptorProvider(10000));
			
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateCategoryTypeDescriptor(PropertiesConstants.PREFERENCE_CATEGORY_TYPE)
				.addAdditiveController(PropertiesConstants.PROPERTY_DESCRIPTOR_PROVIDER, new PreferencePropertyDescriptorProvider(-1000)); // higher priority than one from category.all
		}
		
		override protected function registerMessageBundle():void {
			// messages come from .flex_client.resources
		}
		
		override protected function registerClassAliases():void {
			registerClassAliasFromAnnotation(PropertyDescriptor);
		}
				
		private function registerPropertyProviders():void {
			propertyDescriptorTypeToPropertyRendererFactory[PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_STRING] = new ClassFactoryWithConstructor(StringPropertyRenderer);
			propertyDescriptorTypeToPropertyRendererFactory[PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_BOOLEAN] = new ClassFactoryWithConstructor(BooleanPropertyRenderer);
			propertyDescriptorTypeToPropertyRendererFactory[PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_NUMBER] = new ClassFactoryWithConstructor(NumberPropertyRenderer);
			propertyDescriptorTypeToPropertyRendererFactory[PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_NUMBER_STEPPER] = new ClassFactoryWithConstructor(NumericStepperPropertyRenderer);
			propertyDescriptorTypeToPropertyRendererFactory[PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_DROP_DOWN_LIST] = new ClassFactoryWithConstructor(DropDownListPropertyRenderer);
			propertyDescriptorTypeToPropertyRendererFactory[PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_COLOR_PICKER] = new ClassFactoryWithConstructor(ColorPickerPropertyRenderer);
			propertyDescriptorTypeToPropertyRendererFactory[PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_DATE] = new ClassFactoryWithConstructor(DatePropertyRenderer);
			
			propertyDescriptorTypeToPropertyLineRendererFactory[PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_CATEGORY] = new ClassFactoryWithConstructor(CategoryPropertyLineRenderer);
			propertyDescriptorTypeToPropertyLineRendererFactory[PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_DEFAULT] = new ClassFactoryWithConstructor(PropertyLineRenderer);
			propertyDescriptorTypeToPropertyLineRendererFactory[PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_STYLABLE] = new ClassFactoryWithConstructor(StylablePropertyLineRenderer);
			propertyDescriptorTypeToPropertyLineRendererFactory[PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_PREFERENCE_DEFAULT] = new ClassFactoryWithConstructor(PreferenceDefaultPropertyLineRenderer);
			propertyDescriptorTypeToPropertyLineRendererFactory[PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_PREFERENCE_GLOBAL] = new ClassFactoryWithConstructor(PreferenceGlobalPropertyLineRenderer);
			propertyDescriptorTypeToPropertyLineRendererFactory[PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_PREFERENCE_USER] = new ClassFactoryWithConstructor(PreferenceUserPropertyLineRenderer);
			
			propertyValueClassToPropertyDescriptorType[Boolean] = PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_BOOLEAN;
			propertyValueClassToPropertyDescriptorType[Number] = PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_NUMBER;			
		}
		
		public function getNewPropertyLineRendererInstance(propertyLineRendererType:String):IPropertyLineRenderer {
			var propertyLineRendererFactory:ClassFactoryWithConstructor = propertyDescriptorTypeToPropertyLineRendererFactory[propertyLineRendererType];
			if (propertyLineRendererFactory == null) {
				propertyLineRendererFactory = propertyDescriptorTypeToPropertyLineRendererFactory[PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_DEFAULT];
			}
			if (propertyLineRendererFactory == null) {
				throw new Error();
			}
			return propertyLineRendererFactory.newInstance();
		}
		
		public function getNewPropertyRendererInstance(type:String):IPropertyRenderer {
			var propertyRendererFactory:ClassFactoryWithConstructor = propertyDescriptorTypeToPropertyRendererFactory[type];
			if (propertyRendererFactory == null) {
				propertyRendererFactory = propertyDescriptorTypeToPropertyRendererFactory[PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_STRING];
			}
			if (propertyRendererFactory == null) {
				throw new Error();
			}
			return propertyRendererFactory.newInstance();
		}
		
		/**
		 * @return the <code>PropertyDescriptor</code> associated to <code>property</code>. If no property descriptor found in registry, returns a dummy object.
		 */ 
		public function getPropertyDescriptor(node:Node, property:String, includeRawProperties:Boolean = false):IPropertyDescriptor {
			var context:Object = new Object();
			context[PropertiesConstants.INCLUDE_RAW_PROPERTY] = includeRawProperties;
			
			var providers:IList = CorePlugin.getInstance().nodeTypeDescriptorRegistry.getExpectedTypeDescriptor(node.type).getAdditiveControllers(PropertiesConstants.PROPERTY_DESCRIPTOR_PROVIDER, node);
			
			var propertyDescriptor:IPropertyDescriptor;
			for (var i:int = 0; i < providers.length; i++) {				
				propertyDescriptor = PropertyDescriptorProvider(providers.getItemAt(i)).getPropertyDescriptor(context, node, property);
				if (propertyDescriptor != null) {
					break;
				}
			}			
			return propertyDescriptor;			
		}
		
	}
}
