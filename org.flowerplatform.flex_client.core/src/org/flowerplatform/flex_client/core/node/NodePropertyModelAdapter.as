package org.flowerplatform.flex_client.core.node {
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.remote.PropertyWrapper;
	import org.flowerplatform.flexutil.controller.IPropertyModelAdapter;
	
	/**
	 * @author Balutoiu Diana
	 */
	public class NodePropertyModelAdapter implements IPropertyModelAdapter {
		
		public function getProperties(model:Object):Object{
			return Node(model).properties;	
		}
		
		public function getPropertyValue(model:Object, property:String):Object{
			return Node(model).getPropertyValue(property);	
		}
		
		public function getPropertyValueOrWrapper(model:Object,property:String):* {
			return Node(model).properties[property];		
		}
		
		public function commitPropertyValue(model:Object,propertyValueOrWrapper:Object, value:Object,
											propertyDescriptorName:String, callbackHandler:Function = null):void{
			var newPropertyValueOrWrapper:Object = prepareCommit(propertyValueOrWrapper, value);
			
			CorePlugin.getInstance().serviceLocator.invoke(
				"nodeService.setProperty", 
				[Node(model).nodeUri, propertyDescriptorName, newPropertyValueOrWrapper], callbackHandler);
		}
		
		private function prepareCommit(propertyValueOrWrapper:Object, newPropertyValue:Object):Object {
			// set new value
			if (propertyValueOrWrapper is PropertyWrapper) {
				PropertyWrapper(propertyValueOrWrapper).value = newPropertyValue;
			} else {
				propertyValueOrWrapper = newPropertyValue;
			}
			return propertyValueOrWrapper;
		}
	}
}