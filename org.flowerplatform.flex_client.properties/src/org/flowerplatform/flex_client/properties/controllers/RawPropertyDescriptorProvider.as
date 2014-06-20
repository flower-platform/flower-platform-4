package org.flowerplatform.flex_client.properties.controllers {
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.properties.PropertiesConstants;
	import org.flowerplatform.flex_client.properties.PropertiesPlugin;
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.Utils;

	public class RawPropertyDescriptorProvider extends PropertyDescriptorProvider {
		
		public function RawPropertyDescriptorProvider(orderIndex:int=0) {
			super(orderIndex);
		}
		
		override public function getPropertyDescriptor(context:Object, node:Node, property:String):PropertyDescriptor {
			if (context.hasOwnProperty(PropertiesConstants.INCLUDE_RAW_PROPERTY) && !Boolean(context[PropertiesConstants.INCLUDE_RAW_PROPERTY]).valueOf()) {
				return null;
			}
			var type:String;
						
			var propertyValue:Object = node.getPropertyValue(property);
			if (propertyValue != null) {
				type = PropertiesPlugin.getInstance().propertyValueClassToPropertyDescriptorType[Utils.getClass(propertyValue)];
			}
			if (type == null) {
				type = PropertiesConstants.PROPERTY_DESCRIPTOR_TYPE_STRING;
			}						
			
			var pd:PropertyDescriptor = new PropertyDescriptor();
			pd.type = type;
			pd.name = property;		
			pd.category = Resources.getMessage("raw.properties");		
			pd.orderIndex = int.MAX_VALUE;
			return pd;			
		}		
		
	}
}