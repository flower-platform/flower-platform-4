package org.flowerplatform.flexutil.samples.properties {
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.utils.ObjectUtil;
	
	import org.flowerplatform.flexutil.FlexUtilConstants;
	import org.flowerplatform.flexutil.properties.PropertiesHelper;
	import org.flowerplatform.flexutil.properties.PropertyDescriptor;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SamplePropertiesHelper extends PropertiesHelper {
		
		protected var propertyDescriptors:IList = new ArrayList();
		
		protected var groupDescriptors:IList = new ArrayList();
		
		public function SamplePropertiesHelper() {
			super();
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
			descriptor.category = "groupWithNoDescriptor1";
			propertyDescriptors.addItem(descriptor);
			
			descriptor = new PropertyDescriptor();
			descriptor.name = "hasGroupWithoutGroupDescriptor1";
			descriptor.category = "groupWithNoDescriptor2";
			propertyDescriptors.addItem(descriptor);
			
			descriptor = new PropertyDescriptor();
			descriptor.name = "hasGroupWithoutGroupDescriptor3";
			descriptor.category = "groupWithNoDescriptor2";
			propertyDescriptors.addItem(descriptor);
			
			descriptor = new PropertyDescriptor();
			descriptor.name = "booleanProperty";
			descriptor.category = "groupWithDescriptor1";
			descriptor.type = FlexUtilConstants.PROPERTY_EDITOR_TYPE_BOOLEAN;
			propertyDescriptors.addItem(descriptor);
			
			descriptor = new PropertyDescriptor();
			descriptor.name = "hasGroupWithDescriptor2";
			descriptor.category = "groupWithDescriptor2";
			propertyDescriptors.addItem(descriptor);
			
			descriptor = new PropertyDescriptor();
			descriptor.name = "hasGroupWithDescriptor3";
			descriptor.category = "groupWithDescriptor2";
			propertyDescriptors.addItem(descriptor);
			
			// groups
			descriptor = new PropertyDescriptor();
			descriptor.name = "groupWithDescriptor1";
			groupDescriptors.addItem(descriptor);
			
			descriptor = new PropertyDescriptor();
			descriptor.name = "groupWithDescriptor2";
			groupDescriptors.addItem(descriptor);
		}
		
		override protected function copyAllModelProperties(model:Object):Object {
			return ObjectUtil.copy(model.properties);
		}
		
		override protected function getDescriptorsForGroups(model:Object):IList {
			return groupDescriptors;
		}
		
		override protected function getDescriptorsForProperties(model:Object):IList {
			return propertyDescriptors;
		}
		
		override protected function getEventDispatcher(model:Object):IEventDispatcher {
			return model.properties;
		}
	
	}
}