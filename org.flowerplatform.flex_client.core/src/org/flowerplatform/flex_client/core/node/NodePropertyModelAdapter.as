package org.flowerplatform.flex_client.core.node {
	import org.flowerplatform.flex_client.core.editor.remote.Node;
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
	}
}