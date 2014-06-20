package org.flowerplatform.flex_client.properties.controllers {
	
	import mx.utils.ObjectUtil;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.properties.PropertiesConstants;
	import org.flowerplatform.flex_client.properties.controllers.PropertyDescriptorProvider;
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;
	import org.flowerplatform.flexutil.Utils;
	
	public class PreferencePropertyDescriptorProvider extends PropertyDescriptorProvider {
		
		public function PreferencePropertyDescriptorProvider(orderIndex:int=0) {
			super(orderIndex);
		}
		
		override public function getPropertyDescriptor(context:Object, node:Node, property:String):PropertyDescriptor {
			var pd:PropertyDescriptor = super.getPropertyDescriptor(context, node, property);
			
			if (pd != null) {
				pd = PropertyDescriptor(ObjectUtil.copy(pd));
				pd.name = property;
				if (Utils.endsWith(property, ".default")) {				
					pd.propertyLineRenderer = PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_PREFERENCE_DEFAULT;
					pd.orderIndex = 1;
				} else if (Utils.endsWith(property, ".global")) {	
					pd.propertyLineRenderer = PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_PREFERENCE_GLOBAL;
					pd.orderIndex = 2;
				} else if (Utils.endsWith(property, ".user")) { 
					pd.propertyLineRenderer = PropertiesConstants.PROPERTY_LINE_RENDERER_TYPE_PREFERENCE_USER;
					pd.orderIndex = 3;
				}	
			}
			return pd;
		}
				
	}
}