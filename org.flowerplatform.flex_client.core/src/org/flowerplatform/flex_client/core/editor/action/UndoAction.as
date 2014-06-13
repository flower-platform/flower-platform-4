package org.flowerplatform.flex_client.core.editor.action
{
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	public class UndoAction extends ActionBase
	{
		public function UndoAction()
		{
			super();
			label = "Undo";
			visible = true;
		}
		
		override public function run():void
		{
			var node:Node = Node(selection.getItemAt(0));
			CorePlugin.getInstance().serviceLocator.invoke("nodeService.undo", [node.resource, node.idWithinResource]); 
		}
		
	}
}