package org.flowerplatform.flex_client.team.git.history.action
{
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flexutil.action.ActionBase;

	/**
	 * @author Vlad Bogdan Manica
	 */
	public class HistoryReset extends ActionBase
	{
		public function HistoryReset()
		{
			super();
			label = Resources.getMessage("gitHistory.action.Reset");
			icon = Resources.resetIcon;
			setOrderIndex(int(Resources.getMessage('flex_client.team.git.action.history.sortIndex.HistoryReset')));
		}
		
		override public function  get visible():Boolean {
			return (selection != null && selection.length == 1 && selection.getItemAt(0) is Node);
		}
		
		override public function run():void {	
			var node:Node = Node(selection.getItemAt(0));
			CorePlugin.getInstance().serviceLocator.invoke("GitService.reset", [node.nodeUri, "HARD", String(node.properties[GitConstants.ID])]);
		}
	}
}