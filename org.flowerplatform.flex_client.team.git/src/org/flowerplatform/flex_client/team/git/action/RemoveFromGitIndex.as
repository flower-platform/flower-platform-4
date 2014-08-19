package org.flowerplatform.flex_client.team.git.action
{
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitStagingView;
	import org.flowerplatform.flexutil.action.ActionBase;

	public class RemoveFromGitIndex extends ActionBase{
		
		private var gitStagingView:GitStagingView;
		private var selectedNodes:Vector.<Object>;
		
		public function RemoveFromGitIndex(gitStagingView:GitStagingView, selectedNodes:Vector.<Object>) {						
			super();				
			label = Resources.getMessage("team.git.action.RemoveFromGitIndex");
			this.gitStagingView = gitStagingView;
			this.selectedNodes = selectedNodes;
		}
		
		override public function run():void
		{
			gitStagingView.deleteFromGitIndex(selectedNodes);
		}
		
	}
}