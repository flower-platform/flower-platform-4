package org.flowerplatform.flex_client.core.editor.action {
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	public class UndoAction extends ActionBase {

		public function UndoAction() {
			super();
			label = "Undo";
			icon = Resources.undoIcon;
			orderIndex = 100;
			visible = true;
		}
		
		override public function run():void	{
			var node:Node = Node(selection.getItemAt(0));
			CorePlugin.getInstance().serviceLocator.invoke("resourceService.undo", [node.nodeUri]); 
		}
		
		override public function get visible():Boolean {
			if (selection != null && selection.length == 1 && selection.getItemAt(0) is Node) {
				var type:String = Node(selection.getItemAt(0)).type;
				if (type == CoreConstants.COMMAND_TYPE) return true;
			}
			return false;
		}

		
	}
}