package org.flowerplatform.flex_client.properties.property_renderer {
	import flash.events.Event;
	import flash.events.FocusEvent;
	
	import mx.collections.IList;
	import mx.events.FlexEvent;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flex_client.properties.PropertiesPlugin;
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	import spark.components.DataRenderer;
	import spark.layouts.HorizontalLayout;

	/**
	 * @author Razvan Tache
	 * @author Cristina Constantinescu
	 */
	public class BasicPropertyRenderer extends DataRenderer {
						
		public function BasicPropertyRenderer() {
			super();
			layout = new HorizontalLayout;
			percentWidth = 50;
			percentHeight = 100;
		}
				
 		override protected function focusOutHandler(event:FocusEvent):void {
			super.focusOutHandler(event);	
		}
		
		protected function validPropertyValue():Boolean {
			return true;
		}
		
		protected function sendChangedValuesToServer(event:Event):void {			
			if (!data.readOnly) {
				if (!validPropertyValue()) {					
					return;
				}
				CorePlugin.getInstance().serviceLocator.invoke(
					"nodeService.setProperty", 
					[Node(PropertiesPlugin.getInstance().currentSelection.getItemAt(0)).fullNodeId, propertyDescriptor.name, getValue()]);				
			}
		}

		protected function getValue():Object {
			return propertyDescriptor.value;	
		}
		
		protected function get propertyDescriptor():PropertyDescriptor {
			return PropertyDescriptor(data);	
		}
		
		/**
		 * Registers the objectToListen to the Event event, and removes the listner when the parentRenderer is removed
		 * 
		 */
		protected function handleListeningOnEvent(event:String, parentRenderer:BasicPropertyRenderer, objectToListen:Object):void {
			objectToListen.addEventListener(event, sendChangedValuesToServer);		
			parentRenderer.addEventListener(FlexEvent.REMOVE, function(flexEvent:FlexEvent):void {
				objectToListen.removeEventListener(event, sendChangedValuesToServer);
				trace("Listener removed");
			});
		}
		
	}
}
