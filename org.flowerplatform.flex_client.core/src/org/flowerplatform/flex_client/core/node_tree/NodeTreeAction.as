package org.flowerplatform.flex_client.core.node_tree {
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Claudiu Matei
	 */
	public class NodeTreeAction extends ActionBase {
		
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