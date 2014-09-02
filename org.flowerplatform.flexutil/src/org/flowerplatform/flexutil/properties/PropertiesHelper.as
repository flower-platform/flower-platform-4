package org.flowerplatform.flexutil.properties {
	import flash.utils.Dictionary;
	
	import mx.collections.IList;
	
	import org.flowerplatform.flexutil.FactoryWithInitialization;
	import org.flowerplatform.flexutil.controller.ComposedTypeProvider;
	import org.flowerplatform.flexutil.controller.IPropertyModelAdapter;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	import org.flowerplatform.flexutil.properties.controllers.PropertyDescriptorProvider;
	import org.flowerplatform.flexutil.properties.property_line_renderer.IPropertyLineRenderer;
	import org.flowerplatform.flexutil.properties.remote.IPropertyDescriptor;
	
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

		
		public static function getInstance():PropertiesHelper {
			return INSTANCE;
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
				propertyDescriptor = PropertyDescriptorProvider(providers.getItemAt(i)).getPropertyDescriptor(context, nodeObject, property);
				if (propertyDescriptor != null) {
					break;
				}
			}			
			return propertyDescriptor;			
		}
	}
}