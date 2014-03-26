package org.flowerplatform.flex_client.core.editor.action
{
	import org.flowerplatform.flex_client.core.editor.EditorFrontend;
	import org.flowerplatform.flex_client.core.editor.IEditorFrontendAware;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * Convenience class for actions that are <code>IEditorFrontendAware</code>.
	 * 
	 * @author Mariana Gheorghe
	 */
	public class EditorFrontendAwareAction extends ActionBase implements IEditorFrontendAware {
		
		private var _editorFrontend:EditorFrontend;
		
		public function set editorFrontend(value:EditorFrontend):void {
			_editorFrontend = value;
		}
		
		public function get editorFrontend():EditorFrontend {
			return _editorFrontend;
		}
	}
}