package org.flowerplatform.flex_client.team.git.action
{
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.view.CloneRepoView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.action.MultipleSelectionActionBase;

	public class CloneRepoAction extends ActionBase {
		
		public function CloneRepoAction() {
			super();
			label = Resources.getMessage("git.cloneRepo.title");
			icon = Resources.uploadIcon;
//			orderIndex = 310;
		}
		
		override public function get visible():Boolean {
			
//			if (selection != null && selection.length == 1 && selection.getItemAt(0) is Node) {
//				var type:String = Node(selection.getItemAt(0)).type;
//				if (type == CoreConstants.GIT_TYPE) {
//					
//					return false;
//				}
//				return true;
//			}
//			return false;
			return true;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));	
			
			var view:CloneRepoView = new CloneRepoView();
			view.uploadLocationNode = node;
			
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
				.setViewContent(view)
				.setWidth(500)
				.setHeight(400)
				.setTitle(label)	
				.setIcon(icon)
				.show();			
		}
	}
}