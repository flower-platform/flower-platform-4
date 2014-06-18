package org.flowerplatform.flex_client.core.node_tree {
	import org.flowerplatform.flex_client.core.editor.action.DiagramShellAwareActionBase;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	/**
	 * @author Claudiu Matei
	 */
	public class NodeTreeAction extends DiagramShellAwareActionBase {
		
		public function NodeTreeAction() {
			super();
			orderIndex = 20;
			label = Resources.getMessage("editor.action.openNodeTree"); 
			icon = Resources.openIcon;
		}
		
		override public function get visible():Boolean {			
			return selection != null && selection.length == 1 && selection.getItemAt(0) is Node;
		}
		
		override public function run():void {	
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
				.setViewIdInWorkbench(GenericNodeTreeViewProvider.ID)
				.setWidth(500)
				.setHeight(350)
				.show();
		}
		
	}
}