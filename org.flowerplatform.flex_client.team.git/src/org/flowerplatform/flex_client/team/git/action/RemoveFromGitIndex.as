package org.flowerplatform.flex_client.team.git.action {
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flex_client.team.git.GitStagingView;
	import org.flowerplatform.flexutil.action.ActionBase;

	/**
	 * @author Marius Iacob
	 */
	public class RemoveFromGitIndex extends ActionBase {
		
		private var gitStagingView:GitStagingView;
		
		public function RemoveFromGitIndex(gitStagingView:GitStagingView) {						
			super();				
			label = Resources.getMessage("team.git.action.RemoveFromGitIndex");
			this.gitStagingView = gitStagingView;			
		}
		
		override public function get visible():Boolean {			
			return selection != null && selection.length > 0;
		}
		
		override public function run():void {
			var list:ArrayCollection = new ArrayCollection();;
			for (var i:int = 0; i < selection.length; i++) {
				list.addItem(Node(selection.getItemAt(i)).getPropertyValue(GitConstants.FILE_PATH));
			}
			gitStagingView.deleteFromGitIndex(list);
		}
		
	}
}