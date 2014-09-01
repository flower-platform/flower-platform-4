package org.flowerplatform.flexutil.properties.property_line_renderer {
	import spark.components.FormItem;
	import spark.components.HGroup;
	
	import org.flowerplatform.flexutil.properties.property_renderer.IPropertyRenderer;
	import org.flowerplatform.flexutil.properties.remote.PropertyDescriptor;
	
	/**
	 * @author Diana Balutoiu
	 * @author Cristina Constantinescu
	 */
	public class PropertyLineRenderer extends FormItem implements IPropertyLineRenderer {
		
		public var savePropertyEnabled:Boolean = true;
		
		protected var rendererArea:HGroup;
		
		protected var renderer:IPropertyRenderer;
		
		private var _propertyDescriptor:PropertyDescriptor;
		private var _nodeObject:Object;
		
		public function get propertyDescriptor():PropertyDescriptor {
			return _propertyDescriptor;
		}
		
		public function set propertyDescriptor(value:PropertyDescriptor):void {				
			if (_propertyDescriptor == value) {
				return;
			}
			_propertyDescriptor = value;
			
			propertyDescriptorUpdated();
		}
		
		protected function propertyDescriptorUpdated():void {
			//TODO:
		}
		
		public function get nodeObject():Object {
			return _nodeObject;
		}
		
		public function set nodeObject(value:Object):void {			
			_nodeObject = value;				
			
			nodeObjectUpdated();
		}
		
		public function nodeObjectUpdated():void {
			//TODO:
		}
		
		override protected function createChildren():void {				
			super.createChildren();
			
			//TODO:
		}		
		
		public function commit(callbackHandler:Function = null):void {			
			//TODO: needs node property;
		}
		
		protected function prepareCommit(propertyValueOrWrapper:Object, newPropertyValue:Object):Object {
			// TODO:
			return null;
		}
		
		public function get value():Object {
			return renderer.valueToCommit;
		}
	}
}