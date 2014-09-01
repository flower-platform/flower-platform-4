package org.flowerplatform.flexutil.properties.controllers
{
	import org.flowerplatform.flexutil.controller.AbstractController;
	import org.flowerplatform.flexutil.properties.remote.PropertyDescriptor;

	/**
	 * @author Balutoiu Diana
	 * @author Cristina Constantinescu
	 */ 
	public class PropertyDescriptorProvider extends AbstractController {
		
		public function PropertyDescriptorProvider(orderIndex:int=0) {
			super(orderIndex);
		}
		
		/**
		 * @return The <code>PropertyDescriptor</code> found in registry for given node and property. <p>
		 * 
		 * 			If no descriptor found, verifies if property has "x.y" format and tries to get descriptor from "x" property. 
		 * 			(e.g. preferences have value.default, value.global, value.user, but only value has a property descriptor registered).<p>
		 * 
		 * 			Otherwise returns <code>null</code>.
		 */ 
		public function getPropertyDescriptor(context:Object, nodeObject:Object, property:String):PropertyDescriptor {
			//TODO: use node property
			return null;
		}
	}
}