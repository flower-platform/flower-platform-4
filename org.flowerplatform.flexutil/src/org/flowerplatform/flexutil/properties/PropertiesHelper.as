package org.flowerplatform.flexutil.properties {
	import flash.utils.Dictionary;
	
	import org.flowerplatform.flexutil.FactoryWithInitialization;
	import org.flowerplatform.flexutil.properties.property_line_renderer.IPropertyLineRenderer;
	
	/**
	 * @author Balutoiu Diana
	 */
	public class PropertiesHelper {
		
		protected static var INSTANCE:PropertiesHelper;
		public var propertyDescriptorTypeToPropertyLineRendererFactory:Dictionary = new Dictionary();
 		
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
	}
}