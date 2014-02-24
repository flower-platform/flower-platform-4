package org.flowerplatform.flex_client.properties.property_renderer {
	import flash.events.Event;
	import flash.events.FocusEvent;
	
	import mx.events.FlexEvent;
	
	import spark.components.DataRenderer;
	import spark.layouts.HorizontalLayout;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flex_client.properties.PropertiesPlugin;
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;

	/**
	 * @author Razvan Tache
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
		
		protected function sendChangedValuesToServer(event:Event):void {			
			if (!data.readOnly) {
				CorePlugin.getInstance().serviceLocator.invoke(
					"nodeService.setProperty", 
					[Node(PropertiesPlugin.getInstance().currentSelection.getItemAt(0)), data.name, getValue()]);				
			}	
		}

		/**
		 * @author Cristina Constantinescu
		 */ 
		protected function getValue():Object {
			return PropertyDescriptor(data).value;	
		}
		
		/**
		 * Registers the objectToListen to the Event event, and removes the listner when the parentRenderer is removed
		 * 
		 */
//		protected function handleListeningOnEvent(event:String, parentRenderer:BasicPropertyRenderer, objectToListen:Object):void {
//			objectToListen.addEventListener(event, sendChangedValuesToServer);		
//			parentRenderer.addEventListener(FlexEvent.REMOVE, function(flexEvent:FlexEvent):void {
//				objectToListen.removeEventListener(event, sendChangedValuesToServer);
//				trace("Listener removed");
//			});
//		}
		
	}
}