package org.flowerplatform.flex_client.codesync.regex.action {
	import org.flowerplatform.flex_client.codesync.regex.CodeSyncRegexConstants;
	import org.flowerplatform.flex_client.codesync.regex.CodeSyncRegexPlugin;
	import org.flowerplatform.flex_client.core.editor.EditorFrontend;
	import org.flowerplatform.flex_client.core.editor.IEditorFrontendAware;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexdiagram.mindmap.MindMapRootModelWrapper;
	import org.flowerplatform.flexutil.action.MultipleSelectionActionBase;
		
	/**
	 * @author Cristina Constantinescu
	 */
	public class ShowTextEditorAction extends MultipleSelectionActionBase implements IEditorFrontendAware {
		
		private var _editorFrontend:EditorFrontend;
		
		public function ShowTextEditorAction() {
			super();
			preferShowOnActionBar = true;
			label = Resources.getMessage("regex.showTextEditor");
			icon = Resources.fileIcon;
		}	
				
		public function set editorFrontend(value:EditorFrontend):void {
			_editorFrontend = value;
		}
		
		public function get editorFrontend():EditorFrontend {			
			return _editorFrontend;
		}		
		
		override protected function isVisibleForSelectedElement(element:Object):Boolean {		
			if (element is MindMapRootModelWrapper) {
				return false;
			}
			return element is Node && 
				(Node(element).type == CodeSyncRegexConstants.REGEX_MATCHES_TYPE 
					|| Node(element).type == CodeSyncRegexConstants.REGEX_MATCH_TYPE 
					|| Node(element).type == CodeSyncRegexConstants.VIRTUAL_REGEX_TYPE);
		}		
		
		override public function run():void	{
			CodeSyncRegexPlugin.getInstance().getTextEditorFrontend(editorFrontend, true);
		}
		
	}
}