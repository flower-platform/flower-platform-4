package org.flowerplatform.flex_client.core.editor {
	
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.IComposedActionProviderProcessor;
	

	public class EditorFrontendAwareProcessor implements IComposedActionProviderProcessor {
		
		public var editorFrontend:EditorFrontend;
		
		public function EditorFrontendAwareProcessor(editorFrontend:EditorFrontend) {
			this.editorFrontend = editorFrontend;
		}
		
		public function processAction(action:IAction):void {
			if (action is IEditorFrontendAware) {
				IEditorFrontendAware(action).editorFrontend = editorFrontend;
			}
		}
		
	}
}