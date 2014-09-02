package org.flowerplatform.flexutil.properties.controllers {
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.properties.PropertiesConstants;
	import org.flowerplatform.flexutil.properties.PropertiesHelper;
	import org.flowerplatform.flexutil.properties.remote.PropertyDescriptor;
	
	/**
	 * @author Balutoiu Diana
	 * @author Cristina Constantinescu
	 */
	public class RawPropertyDescriptorProvider extends PropertyDescriptorProvider {
		
		public function RawPropertyDescriptorProvider(orderIndex:int=0) {
			super(orderIndex);
		}
		
		/**
		 * @return A dummy <code>PropertyDescriptor</code> for raw properties ("raw" = property without descriptor). 
		 * 			<p>
		 * 			Gets type based on property value class registered in <Code>PropertiesPlugin.getInstance().propertyValueClassToPropertyDescriptorType</code>.
		 * 			If no type associated, string type is used.
		 */ 
		override public function getPropertyDescriptor(context:Object, nodeObject:Object, property:String):PropertyDescriptor {
			if (context.hasOwnProperty(PropertiesConstants.INCLUDE_RAW_PROPERTY) && !Boolean(context[PropertiesConstants.INCLUDE_RAW_PROPERTY]).valueOf()) {
				return null;
			}
			var type:String;
			
			var propertyValue:Object = nodeObject.getPropertyValue(property);
			if (propertyValue != null) {
				//type = PropertiesPlugin.getInstance().propertyValueClassToPropertyDescriptorType[Utils.getClass(propertyValue)];
				type = PropertiesHelper.getInstance().propertyValueClassToPropertyDescriptorType[Utils.getClass(propertyValue)];
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