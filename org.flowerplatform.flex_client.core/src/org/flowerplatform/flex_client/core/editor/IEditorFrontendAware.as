package org.flowerplatform.flex_client.core.editor {
	import org.flowerplatform.flex_client.core.editor.EditorFrontend;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public interface IEditorFrontendAware {

		function set editorFrontend(value:EditorFrontend):void;
		function get editorFrontend():EditorFrontend;
		
	}
}