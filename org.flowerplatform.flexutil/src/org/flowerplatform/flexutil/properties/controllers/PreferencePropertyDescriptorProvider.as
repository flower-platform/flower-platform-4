package org.flowerplatform.flexutil.properties.controllers {
	import mx.utils.ObjectUtil;
	
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.properties.PropertiesConstants;
	import org.flowerplatform.flexutil.properties.remote.PropertyDescriptor;
	
	/**
	 * @author Balutoiu Diana
	 * @author Cristina Constantinescu
	 */
	public class PreferencePropertyDescriptorProvider extends PropertyDescriptorProvider {
		public function PreferencePropertyDescriptorProvider(orderIndex:int=0) {
			super(orderIndex);
		}
		
		/**
		 * @return A custom <code>PropertyDescriptor</code> for default/global/user properties (custom orderIndex & renderer).
		 */ 
		override public function getPropertyDescriptor(context:Object, nodeObject:Object, property:String):PropertyDescriptor {
			var pd:PropertyDescriptor = super.getPropertyDescriptor(context, nodeObject, property);
			
			if (pd != null) {
				pd = PropertyDescriptor(ObjectUtil.copy(pd));
				pd.name = property;
				if (Utils.endsWith(property, ".default")) {				
					pd.propertyLineRenderer = PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_PREFERENCE_DEFAULT;
					pd.orderIndex = 1;					
				} else if (Utils.endsWith(property, ".global")) {	
					pd.propertyLineRenderer = PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_PREFERENCE_GLOBAL;
					pd.orderIndex = 2;
					pd.readOnly = false;
				} else if (Utils.endsWith(property, ".user")) { 
					pd.propertyLineRenderer = PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_PREFERENCE_USER;
					pd.orderIndex = 3;
					pd.readOnly = false;
				}	
			}
			return pd;
		}
	}
}