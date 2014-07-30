package org.flowerplatform.flex_client.codesync.regex.action {
	import org.flowerplatform.flex_client.codesync.regex.CodeSyncRegexConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.action.DiagramShellAwareActionBase;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.mindmap.MindMapEditorDiagramShell;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexdiagram.mindmap.MindMapRootModelWrapper;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class ShowOrderedMatchesAction extends DiagramShellAwareActionBase {
		
		public static const ID:String = "org.flowerplatform.flex_client.codesync.regex.action.ShowOrderedMatchesAction";
		
		public function ShowOrderedMatchesAction() {
			super();
			label = Resources.getMessage("regex.action.ordered");
			icon = Resources.brickIcon;
		}
		
		override public function get visible():Boolean {			
			if (selection == null || selection.length != 1) {
				return false;
			}
			var obj:Object = selection.getItemAt(0);
			if (obj is MindMapRootModelWrapper) {
				obj = MindMapRootModelWrapper(obj).model;
			}
			return obj is Node && Node(obj).type == CodeSyncRegexConstants.REGEX_MATCHES_TYPE;
		}
		
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var ds:MindMapEditorDiagramShell = MindMapEditorDiagramShell(diagramShell);
			if (ds.getModelController(diagramShellContext, node).getExpanded(diagramShellContext, node)) {
				CorePlugin.getInstance().nodeRegistryManager.collapse(ds.nodeRegistry, node);
			}			
			CorePlugin.getInstance().nodeRegistryManager.expand(ds.nodeRegistry, node, getExpandContext());
		}
			
		protected function getExpandContext():Object {
			return new Object();
		}
		
	}
}