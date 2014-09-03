package org.flowerplatform.flexutil.properties.property_line_renderer {
	import mx.core.UIComponent;
	
	import spark.components.FormItem;
	import spark.components.HGroup;
	
	import org.flowerplatform.flexutil.properties.PropertiesHelper;
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
			// use title as label if set
			this.label = _propertyDescriptor.title != null ? _propertyDescriptor.title : _propertyDescriptor.name;			
			
			if (renderer != null) {
				renderer.propertyDescriptorChangedHandler();
			}
		}
		
		public function get nodeObject():Object {
			return _nodeObject;
		}
		
		public function set nodeObject(value:Object):void {			
			_nodeObject = value;				
			
			nodeObjectUpdated();
		}
		
		public function nodeObjectUpdated():void {
			if (renderer != null && nodeObject != null) {
				renderer.valueChangedHandler();
			}
		}
		
		override protected function createChildren():void {				
			super.createChildren();
			
			UIComponent(labelDisplay).setStyle("fontWeight", "normal");	
			
			rendererArea = new HGroup();
			rendererArea.percentHeight = 100;
			rendererArea.percentWidth = 100;
			rendererArea.gap = 15;
			rendererArea.verticalAlign = "middle";
			this.addElement(rendererArea);
			
			renderer = PropertiesHelper.getInstance().getNewPropertyRendererInstance(propertyDescriptor.type);
			UIComponent(renderer).percentWidth = 100;
			UIComponent(renderer).percentHeight = 100;
			renderer.propertyLineRenderer = this;	
			
			rendererArea.addElement(UIComponent(renderer));
			
			propertyDescriptorUpdated();
			nodeObjectUpdated();
		}		
		
		public function commit(callbackHandler:Function = null):void {			
			if (!savePropertyEnabled || propertyDescriptor.readOnly || !renderer.isValidValue()) {
				return;
			}
			
			var oldPropertyValue:Object = PropertiesHelper.getInstance().propertyModelAdapter
				.getPropertyValue(nodeObject,propertyDescriptor.name);
			var newPropertyValue:Object = renderer.valueToCommit;
			var propertyValueOrWrapper:Object = PropertiesHelper.getInstance().propertyModelAdapter.getPropertyValueOrWrapper(nodeObject,propertyDescriptor.name);
			
			if (oldPropertyValue != newPropertyValue) {
				PropertiesHelper.getInstance().propertyModelAdapter
					.commitPropertyValue(nodeObject, propertyValueOrWrapper, newPropertyValue, propertyDescriptor.name, callbackHandler);
			}
		}
		
		
		public function get value():Object {
			return renderer.valueToCommit;
		}
	}
}