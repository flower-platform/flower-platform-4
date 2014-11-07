package org.flowerplatform.flexutil.samples.properties {
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayList;
	import mx.utils.ObjectUtil;
	
	import org.flowerplatform.flexutil.FlexUtilConstants;
	import org.flowerplatform.flexutil.properties.PropertiesHelper;
	import org.flowerplatform.flexutil.properties.PropertyDescriptor;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SamplePropertiesHelper extends PropertiesHelper {
		
		public function SamplePropertiesHelper() {
			super();
			propertyDescriptors = new ArrayList();
			groupDescriptors = new ArrayList();
			
			var descriptor:PropertyDescriptor;
			
//			for (var i:int = 0; i < 10; i++) {
//				descriptor = new PropertyDescriptor();
//				descriptor.name = "withoutGroup" + i;
//				propertyDescriptors.addItem(descriptor);
//			}
			
			descriptor = new PropertyDescriptor();
			descriptor.name = "withoutGroup1";
			propertyDescriptors.addItem(descriptor);
			
			descriptor = new PropertyDescriptor();
			descriptor.name = "withoutGroup2";
			propertyDescriptors.addItem(descriptor);
			
			descriptor = new PropertyDescriptor();
			descriptor.name = "hasGroupWithoutGroupDescriptor2";
			descriptor.group = "groupWithNoDescriptor1";
			propertyDescriptors.addItem(descriptor);
			
			descriptor = new PropertyDescriptor();
			descriptor.name = "hasGroupWithoutGroupDescriptor1";
			descriptor.group = "groupWithNoDescriptor2";
			propertyDescriptors.addItem(descriptor);
			
			descriptor = new PropertyDescriptor();
			descriptor.name = "hasGroupWithoutGroupDescriptor3";
			descriptor.group = "groupWithNoDescriptor2";
			propertyDescriptors.addItem(descriptor);
			
			descriptor = new PropertyDescriptor();
			descriptor.name = "booleanProperty";
			descriptor.group = "groupWithDescriptor1";
			descriptor.type = FlexUtilConstants.PROPERTY_EDITOR_TYPE_BOOLEAN;
			propertyDescriptors.addItem(descriptor);
			
			descriptor = new PropertyDescriptor();
			descriptor.name = "hasGroupWithDescriptor2";
			descriptor.group = "groupWithDescriptor2";
			propertyDescriptors.addItem(descriptor);
			
			descriptor = new PropertyDescriptor();
			descriptor.name = "hasGroupWithDescriptor3";
			descriptor.group = "groupWithDescriptor2";
			propertyDescriptors.addItem(descriptor);
			
			// groups
			descriptor = new PropertyDescriptor();
			descriptor.name = "groupWithDescriptor1";
			groupDescriptors.addItem(descriptor);
			
			descriptor = new PropertyDescriptor();
			descriptor.name = "groupWithDescriptor2";
			groupDescriptors.addItem(descriptor);
		}
		
		override protected function copyAllModelProperties(context:Object, model:Object):Object {
			return ObjectUtil.copy(model.properties);
		}
		
		override protected function getEventDispatcher(context:Object, model:Object):IEventDispatcher {
			return model.properties;
		}
	
	}
}