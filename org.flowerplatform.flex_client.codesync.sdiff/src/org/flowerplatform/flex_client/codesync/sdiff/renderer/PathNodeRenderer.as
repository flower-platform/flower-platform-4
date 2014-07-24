
package  org.flowerplatform.flex_client.codesync.sdiff.renderer {
	
	import mx.events.PropertyChangeEvent;
	
	import spark.components.Label;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.mindmap.renderer.MindMapNodeWithDetailsRenderer;
	
	public class PathNodeRenderer extends MindMapNodeWithDetailsRenderer{
		
		protected var label:Label;
		
		public function PathNodeRenderer(){
			super();
		}
		
		override protected function createChildren():void{
			super.createChildren();
			createLabel();
			addElementAt(label,2);
		}
		
		private function createLabel():void{
			label = new Label();
			//label.setStyle("fontFamily","Courier");
			label.setStyle("fontStyle","italic");
			label.width = 250;
		}
		
		override protected function modelChangedHandler(event:PropertyChangeEvent):void {
			super.modelChangedHandler(event);
			label.text = "org.flowerplatform." + node.properties[CoreConstants.NAME] as String ;
		}
	}
}