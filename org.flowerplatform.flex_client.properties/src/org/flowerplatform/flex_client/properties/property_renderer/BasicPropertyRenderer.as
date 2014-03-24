package org.flowerplatform.flex_client.properties.property_renderer {
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.ui.Keyboard;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.properties.PropertiesPlugin;
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;
	
	import spark.components.DataRenderer;
	import spark.layouts.HorizontalLayout;

	/**
	 * @author Razvan Tache
	 * @author Cristina Constantinescu
	 * @author Sebastian Solomon
	 */
	public class BasicPropertyRenderer extends DataRenderer {
		
		public var oldValue:Object;
		
		public function BasicPropertyRenderer() {
			super();
			layout = new HorizontalLayout;
		}
				
		protected function validPropertyValue():Boolean {
			return true;
		}
		
		protected function saveProperty(event:Event):void {			
			if (!data.readOnly) {
				if (!validPropertyValue()) {					
					return;
				}
				if (oldValue != getValue()) {
					oldValue = getValue();
					CorePlugin.getInstance().serviceLocator.invoke(
						"nodeService.setProperty", 
						[Node(PropertiesPlugin.getInstance().currentSelection.getItemAt(0)).fullNodeId, propertyDescriptor.name, getValue()]);
				}
			}
		}

		protected function getValue():Object {
			return propertyDescriptor.value;	
		}
		
		protected function get propertyDescriptor():PropertyDescriptor {
			return PropertyDescriptor(data);	
		}
		
		protected function keyHandler(event:KeyboardEvent):void {
			if (event.keyCode == Keyboard.ENTER) {
				saveProperty(null);
			}
		}
		
	}
}
