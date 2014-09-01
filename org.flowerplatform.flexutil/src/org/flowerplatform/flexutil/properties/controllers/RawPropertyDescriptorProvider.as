package org.flowerplatform.flexutil.properties.controllers {
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
			//TODO: use node property
			return null;
		}		
		
	}
}