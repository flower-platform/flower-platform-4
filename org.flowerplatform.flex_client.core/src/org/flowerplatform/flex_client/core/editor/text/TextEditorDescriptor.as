package org.flowerplatform.flex_client.core.editor.text {
	
	import org.flowerplatform.flex_client.core.editor.EditorDescriptor;
	import org.flowerplatform.flex_client.core.editor.EditorFrontend;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class TextEditorDescriptor extends EditorDescriptor {
		
		public static const ID:String = "text";
		
		override public function getEditorName():String {
			return "text";
		}
		
		override protected function createViewInstance():EditorFrontend	{
			return new TextEditorFrontend();
		}
		
		public override function getId():String {	
			return ID;
		}
		
		public override function getIcon(viewLayoutData:ViewLayoutData=null):Object {				
			return null;
		}
		
		public override function getTitle(viewLayoutData:ViewLayoutData=null):String {		
			return viewLayoutData.customData;
		}
	}
}