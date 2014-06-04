package org.flowerplatform.flex_client.core.editor.action
{
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	public class RedoAction extends ActionBase
	{
		public function RedoAction()
		{
			super();
			label = "Redo";
			visible = true;
		}
		
		override public function run():void
		{
			var node:Node = Node(selection.getItemAt(0));
			CorePlugin.getInstance().serviceLocator.invoke("nodeService.redo", [node.resource, node.idWithinResource]); 
		}
		
	}
}