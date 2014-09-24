package org.flowerplatform.flexutil.properties {
	import flash.utils.Dictionary;
	
	import mx.collections.IList;
	
	import org.flowerplatform.flexutil.FactoryWithInitialization;
	import org.flowerplatform.flexutil.controller.ComposedTypeProvider;
	import org.flowerplatform.flexutil.controller.IPropertyModelAdapter;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	import org.flowerplatform.flexutil.properties.property_line_renderer.CategoryPropertyLineRenderer;
	import org.flowerplatform.flexutil.properties.property_line_renderer.IPropertyLineRenderer;
	import org.flowerplatform.flexutil.properties.property_line_renderer.PropertyLineRenderer;
	import org.flowerplatform.flexutil.properties.property_renderer.BooleanPropertyRenderer;
	import org.flowerplatform.flexutil.properties.property_renderer.ColorPickerPropertyRenderer;
	import org.flowerplatform.flexutil.properties.property_renderer.DatePropertyRenderer;
	import org.flowerplatform.flexutil.properties.property_renderer.DropDownListPropertyRenderer;
	import org.flowerplatform.flexutil.properties.property_renderer.IPropertyRenderer;
	import org.flowerplatform.flexutil.properties.property_renderer.NumberPropertyRenderer;
	import org.flowerplatform.flexutil.properties.property_renderer.NumericStepperPropertyRenderer;
	import org.flowerplatform.flexutil.properties.property_renderer.StringPropertyRenderer;
	import org.flowerplatform.flexutil.properties.remote.IPropertyDescriptor;
	import org.flowerplatform.flexutil.properties.remote.PropertyDescriptor;
	
	/**
	 * @author Balutoiu Diana
	 */
	public class PropertiesHelper {
		
		
		protected static var INSTANCE:PropertiesHelper;
		
		public var propertyModelAdapter:IPropertyModelAdapter;
		public var nodeTypeDescriptorRegistry:TypeDescriptorRegistry = new TypeDescriptorRegistry();
		public var composedTypeProvider:ComposedTypeProvider  = new ComposedTypeProvider();
		public var propertyDescriptorTypeToPropertyLineRendererFactory:Dictionary = new Dictionary();
		public var propertyValueClassToPropertyDescriptorType:Dictionary = new Dictionary();
		public var propertyDescriptorTypeToPropertyRendererFactory:Dictionary = new Dictionary();
		public var currentSelectedNodeObject:Object;

		
		public static function getInstance():PropertiesHelper {
			return INSTANCE;
		}
		
		public function PropertiesHelper() {
			if (INSTANCE != null) {
				throw new Error("An instance of PropertiesHelper already exists; it should be a singleton!");
			}
			INSTANCE = this;
			registerPropertyProviders();
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
		
		public function getPropertyDescriptor(nodeObject:Object, property:String, includeRawProperties:Boolean = false):IPropertyDescriptor {
			var context:Object = new Object();
			context[PropertiesConstants.INCLUDE_RAW_PROPERTY] = includeRawProperties;
			
			var providers:IList = PropertiesHelper.getInstance().nodeTypeDescriptorRegistry
				.getExpectedTypeDescriptor(PropertiesHelper.getInstance()
				.composedTypeProvider.getType(nodeObject)).getAdditiveControllers(PropertiesConstants.PROPERTY_DESCRIPTOR_PROVIDER, nodeObject);
			
			var propertyDescriptor:IPropertyDescriptor;
			for (var i:int = 0; i < providers.length; i++) {				
				propertyDescriptor = PropertyDescriptor(providers.getItemAt(i));
				if (propertyDescriptor != null) {
					break;
				}
			}			
			return propertyDescriptor;				
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
			
			propertyValueClassToPropertyDescriptorType[Boolean] = PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_BOOLEAN;
			propertyValueClassToPropertyDescriptorType[Number] = PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_NUMBER;			
		}
		
		
	}
}