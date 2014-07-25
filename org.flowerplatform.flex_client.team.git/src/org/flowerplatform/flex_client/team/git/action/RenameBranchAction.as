package org.flowerplatform.flex_client.team.git.action {
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.team.git.ui.RenameBranchView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Tita Andreea
	 */
	
	public class RenameBranchAction extends ActionBase {
		
		public function RenameBranchAction() {
			super();
			label = ""; //TODO
		}
		
		
		override public function get visible():Boolean {
			return true;
		}
		
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var view:RenameBranchView = new RenameBranchView();
			// TODO: save the repo using node
			// ...
			
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(view)
				.setWidth(550)
				.setHeight(500)
				.setTitle(label)
				.setIcon(icon)
				.show();	
		}
	}
}