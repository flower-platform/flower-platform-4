package org.flowerplatform.flex_client.text {
	import org.flowerplatform.flex_client.core.editor.EditorDescriptor;
	import org.flowerplatform.flex_client.core.editor.EditorFrontend;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
		
	/**
	 * @author Cristina Constantinescu
	 */
	public class TextEditorDescriptor extends EditorDescriptor	{
		
		override public function getEditorName():String {
			return TextConstants.TEXT_EDITOR_NAME;
		}
		
		override protected function createViewInstance():EditorFrontend	{
			return new TextEditorFrontend();
		}
		
		public override function getId():String {	
			return TextConstants.TEXT_CONTENT_TYPE;
		}
		
		public override function getIcon(viewLayoutData:ViewLayoutData=null):Object {	
			return Resources.fileIcon;
		}
		
		public override function getTitle(viewLayoutData:ViewLayoutData=null):String {	
			if (viewLayoutData != null) {
				return viewLayoutData.customData;
			} else {
				return TextConstants.TEXT_EDITOR_NAME;
			}
		}
		
	}
}