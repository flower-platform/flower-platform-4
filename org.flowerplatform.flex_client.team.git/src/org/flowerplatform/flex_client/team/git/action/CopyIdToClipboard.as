package org.flowerplatform.flex_client.team.git.action {
	import flash.desktop.Clipboard;
	import flash.desktop.ClipboardFormats;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitHistoryConstants;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	public class CopyIdToClipboard extends ActionBase {
		
		public function CopyIdToClipboard() {
			super();
			label = Resources.getMessage("gitHistory.action.CopyId");
		}
		
		override public function  get visible():Boolean {
			if (selection != null && selection.length == 1 && selection.getItemAt(0) is Node) {
				return true;
			}
			return false;
		}
		
		override public function run():void {	
			var node:Node = Node(selection.getItemAt(0));
			Clipboard.generalClipboard.setData(ClipboardFormats.TEXT_FORMAT, node.getPropertyValue(GitHistoryConstants.ID));	
		}
		
	}
}