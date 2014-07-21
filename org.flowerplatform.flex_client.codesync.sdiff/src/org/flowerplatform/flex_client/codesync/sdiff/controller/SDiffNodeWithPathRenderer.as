package org.flowerplatform.flex_client.codesync.sdiff.controller {
	import mx.controls.Label;
	import mx.events.PropertyChangeEvent;
	
	import spark.components.Group;
	import spark.layouts.VerticalLayout;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.mindmap.renderer.MindMapNodeWithDetailsRenderer;
	import org.flowerplatform.flex_client.core.editor.remote.Node;

	public class SDiffNodeWithPathRenderer extends MindMapNodeWithDetailsRenderer {
		
		protected var pathGroup:Group;
		protected var pathLabel:Label;
		
		override protected function createChildren():void {
			super.createChildren();
			createPathGroup();
			createPathLabel();
			
			addElementAt(pathGroup,2);
		}
		
		private function createPathGroup():void {
			pathGroup = new Group();
			pathGroup.percentWidth = 100;
			
			var hLayout:VerticalLayout = new VerticalLayout();
			hLayout.gap = 2;
			hLayout.paddingBottom = 2;
			hLayout.paddingTop = 2;
			hLayout.paddingLeft = 2;
			hLayout.paddingRight = 2;
			hLayout.horizontalAlign = "middle";
			hLayout.verticalAlign = "middle";
			
			pathGroup.layout = hLayout; 
		}
	
		private function createPathLabel():void {
			
			pathLabel = new Label();
			var nameFontSize:int = nodeGroup.getStyle("fontSize");
			pathLabel.setStyle("fontSize",nameFontSize-2);
			pathLabel.percentWidth = 100;
			pathLabel.setStyle("textAlign","center");
			pathGroup.addElement(pathLabel);
		}
		
		override protected function modelChangedHandler(event:PropertyChangeEvent):void {
			super.modelChangedHandler(event);
//			var node:Node = Node(event.source);
			if (event.property == CoreConstants.NAME) {
				pathLabel.text = "org.flowerplatform.codesync.sdiff." + event.newValue;
			}
		}
		
	}
}