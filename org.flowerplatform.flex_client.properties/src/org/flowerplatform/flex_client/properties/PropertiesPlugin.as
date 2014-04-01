package org.flowerplatform.flex_client.properties {
	import flash.utils.Dictionary;
	
	import mx.collections.IList;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flex_client.properties.action.AddNodeAction;
	import org.flowerplatform.flex_client.properties.action.NewComposedAction;
	import org.flowerplatform.flex_client.properties.action.ShowPropertiesAction;
	import org.flowerplatform.flex_client.properties.property_renderer.BooleanPropertyRenderer;
	import org.flowerplatform.flex_client.properties.property_renderer.ColorPickerPropertyRenderer;
	import org.flowerplatform.flex_client.properties.property_renderer.DatePropertyRenderer;
	import org.flowerplatform.flex_client.properties.property_renderer.DropDownListPropertyRenderer;
	import org.flowerplatform.flex_client.properties.property_renderer.FileSizePropertyRenderer;
	import org.flowerplatform.flex_client.properties.property_renderer.NumberPropertyRenderer;
	import org.flowerplatform.flex_client.properties.property_renderer.NumericStepperPropertyRenderer;
	import org.flowerplatform.flex_client.properties.property_renderer.StringPropertyRenderer;
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FactoryWithInitialization;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;

	/**
	 * @author Razvan Tache
	 */
	public class PropertiesPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:PropertiesPlugin;
		
		public var propertyRendererClasses:Dictionary = new Dictionary();
		
		public var currentSelection:IList;
		
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
			propertyRendererClasses[null] = new FactoryWithInitialization(StringPropertyRenderer);
			propertyRendererClasses["String"] = new FactoryWithInitialization(StringPropertyRenderer);
			propertyRendererClasses["Boolean"] = new FactoryWithInitialization(BooleanPropertyRenderer);
			propertyRendererClasses["Number"] = new FactoryWithInitialization(NumberPropertyRenderer);
			propertyRendererClasses["NumberStepper"] = new FactoryWithInitialization(NumericStepperPropertyRenderer);
			propertyRendererClasses["DropDownList"] = new FactoryWithInitialization(DropDownListPropertyRenderer);
			propertyRendererClasses["ColorPicker"] = new FactoryWithInitialization(ColorPickerPropertyRenderer);
			propertyRendererClasses["FileSize"] = new FactoryWithInitialization(FileSizePropertyRenderer);
			propertyRendererClasses["Date"] = new FactoryWithInitialization(DatePropertyRenderer);
		}
		
	}
}
