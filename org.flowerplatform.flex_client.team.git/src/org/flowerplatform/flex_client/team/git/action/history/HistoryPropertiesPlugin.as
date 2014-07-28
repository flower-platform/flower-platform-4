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
package org.flowerplatform.flex_client.team.git.action.history {
	import flash.utils.Dictionary;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;

	/**
	 * @author Cristina Constantinescu
	 */
	public class HistoryPropertiesPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:HistoryPropertiesPlugin;
		
		public var propertyDescriptorTypeToPropertyRendererFactory:Dictionary = new Dictionary();
		public var propertyDescriptorTypeToPropertyLineRendererFactory:Dictionary = new Dictionary();
		public var propertyValueClassToPropertyDescriptorType:Dictionary = new Dictionary();
		
		public var currentSelectedNode:Node;
		
		public static function getInstance():HistoryPropertiesPlugin {
			return INSTANCE;
		}
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
									
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new GitHistoryViewProvider());
			
			/*CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(ShowPropertiesAction);
			CorePlugin.getInstance().getEditorClassFactoryActionProvider().addActionClass(NewComposedAction);
			
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new PreferencesViewProvider());
			CorePlugin.getInstance().globalMenuActionProvider.addAction(new ShowPreferencesAction());*/
		}
		
		override public function start():void {
			super.start();
			registerPropertyProviders();
			
			/*CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateCategoryTypeDescriptor(FlexUtilConstants.CATEGORY_ALL)
				.addAdditiveController(PropertiesConstants.PROPERTY_DESCRIPTOR_PROVIDER, new PropertyDescriptorProvider())
				.addAdditiveController(PropertiesConstants.PROPERTY_DESCRIPTOR_PROVIDER, new RawPropertyDescriptorProvider(10000));
			
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateCategoryTypeDescriptor(PropertiesConstants.PREFERENCE_CATEGORY_TYPE)
				.addAdditiveController(PropertiesConstants.PROPERTY_DESCRIPTOR_PROVIDER, new PreferencePropertyDescriptorProvider(-1000)); // higher priority than one from category.all
			*/
		}
		
		override protected function registerMessageBundle():void {
			// messages come from .flex_client.resources
		}
		
		override protected function registerClassAliases():void {
			//registerClassAliasFromAnnotation(PropertyDescriptor);
		}
				
		private function registerPropertyProviders():void {
			/*propertyDescriptorTypeToPropertyRendererFactory[PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_STRING] = new FactoryWithInitialization(StringPropertyRenderer);
			propertyDescriptorTypeToPropertyRendererFactory[PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_BOOLEAN] = new FactoryWithInitialization(BooleanPropertyRenderer);
			propertyDescriptorTypeToPropertyRendererFactory[PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_NUMBER] = new FactoryWithInitialization(NumberPropertyRenderer);
			propertyDescriptorTypeToPropertyRendererFactory[PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_NUMBER_STEPPER] = new FactoryWithInitialization(NumericStepperPropertyRenderer);
			propertyDescriptorTypeToPropertyRendererFactory[PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_DROP_DOWN_LIST] = new FactoryWithInitialization(DropDownListPropertyRenderer);
			propertyDescriptorTypeToPropertyRendererFactory[PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_COLOR_PICKER] = new FactoryWithInitialization(ColorPickerPropertyRenderer);
			propertyDescriptorTypeToPropertyRendererFactory[PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_DATE] = new FactoryWithInitialization(DatePropertyRenderer);
			
			propertyDescriptorTypeToPropertyLineRendererFactory[PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_CATEGORY] = new FactoryWithInitialization(CategoryPropertyLineRenderer);
			propertyDescriptorTypeToPropertyLineRendererFactory[PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_DEFAULT] = new FactoryWithInitialization(PropertyLineRenderer);
			propertyDescriptorTypeToPropertyLineRendererFactory[PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_STYLABLE] = new FactoryWithInitialization(StylablePropertyLineRenderer);
			propertyDescriptorTypeToPropertyLineRendererFactory[PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_PREFERENCE_DEFAULT] = new FactoryWithInitialization(PreferenceDefaultPropertyLineRenderer);
			propertyDescriptorTypeToPropertyLineRendererFactory[PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_PREFERENCE_GLOBAL] = new FactoryWithInitialization(PreferenceGlobalPropertyLineRenderer);
			propertyDescriptorTypeToPropertyLineRendererFactory[PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_PREFERENCE_USER] = new FactoryWithInitialization(PreferenceUserPropertyLineRenderer);
			
			propertyValueClassToPropertyDescriptorType[Boolean] = PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_BOOLEAN;
			propertyValueClassToPropertyDescriptorType[Number] = PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_NUMBER;
			*/
		}		
	}
}
