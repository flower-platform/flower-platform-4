package org.flowerplatform.flex_client.core.editor.text {
	
	import org.flowerplatform.flex_client.core.editor.EditorFrontend;
	
	import spark.components.Label;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class TextEditorFrontend extends EditorFrontend {
		
		override protected function createChildren():void {
			super.createChildren();
			
			var label:Label = new Label();
			label.text = "This is a text editor";
			addElement(label);
		}
		
	}
}