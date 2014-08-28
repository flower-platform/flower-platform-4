/**
 * @author Alina Bratu
 */
package org.flowerplatform.flex_client.team.git.action {
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Alina Bratu
	 */
	public class RevertCommitAction extends ActionBase {
		
		public function RevertCommitAction() {
			super();
			label = Resources.getMessage("flex_client.team.git.history.action.revertCommit");
			icon = Resources.gitRevertCommitIcon;
		}
		
		override public function  get visible():Boolean {
			if (selection != null && selection.length == 1 && selection.getItemAt(0) is Node) {
				return true;
			}
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var idCommit:String = node.getPropertyValue(GitConstants.ID);
			CorePlugin.getInstance().serviceLocator.invoke("GitService.revertCommit", [node.nodeUri, idCommit],
				function(result:String):void {
					if (result != null) {
						FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
						.setText(result)
						.setTitle(Resources.getMessage('info'))
						.setWidth(300)
						.setHeight(200)
						.showMessageBox();
					}
			});
		}
		
	}
}