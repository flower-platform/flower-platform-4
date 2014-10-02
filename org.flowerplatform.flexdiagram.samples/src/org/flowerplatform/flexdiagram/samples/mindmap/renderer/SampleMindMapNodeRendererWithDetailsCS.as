package org.flowerplatform.flexdiagram.samples.mindmap.renderer {
	import mx.events.PropertyChangeEvent;
	
	import spark.components.Label;
	import spark.components.RichText;
	
	import flashx.textLayout.conversion.TextConverter;
	
	import org.flowerplatform.flexdiagram.mindmap.MindMapRenderer;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.controller.ValuesProvider;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SampleMindMapNodeRendererWithDetailsCS extends MindMapRenderer {
		
		protected var detailsLabel:RichText;
		
		protected static var instanceNumber:int = -1;
		
		public function set detailsText(value:String):void {
			if (value != null) {
				detailsLabel.textFlow = TextConverter.importToFlow(value , Utils.isHTMLText(value) ? TextConverter.TEXT_FIELD_HTML_FORMAT : TextConverter.PLAIN_TEXT_FORMAT);	
			} else {
				detailsLabel.textFlow = null;
			}			
		}
		
		public function SampleMindMapNodeRendererWithDetailsCS() {
			featureForValuesProvider = "mindMapValuesProvider";
			canHaveChildren = true;
		}
		
		override protected function createChildren():void {
			super.createChildren();
			var label:Label = new Label();
			label.text = "Details: (instance no = " + ++instanceNumber + ")";
			addElement(label);
			detailsLabel = new RichText();
			addElement(detailsLabel);
		}
		
		override protected function modelChangedHandler(event:PropertyChangeEvent):void {
			super.modelChangedHandler(event);
			var valuesProvider:ValuesProvider = getRequiredValuesProvider();
			setFieldIfNeeded(valuesProvider, typeDescriptorRegistry, event, "detailsText", "mindMapNodeRenderer.detailsText", "");
		}	
		
	}
}