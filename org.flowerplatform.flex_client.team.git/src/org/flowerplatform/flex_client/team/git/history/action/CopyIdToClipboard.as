package org.flowerplatform.flex_client.team.git.history.action {
	import flash.desktop.Clipboard;
	import flash.desktop.ClipboardFormats;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Andreea Tita
	 */	
	public class CopyIdToClipboard extends ActionBase {
		
		public function CopyIdToClipboard() {
			super();
			label = Resources.getMessage("gitHistory.action.CopyId");
			icon = Resources.copy;
		}
		
		override public function  get visible():Boolean {
			if (selection != null && selection.length == 1 && selection.getItemAt(0) is Node) {
				return true;
			}
			return false;
		}
		
		override public function run():void {	
			Clipboard.generalClipboard.setData(ClipboardFormats.TEXT_FORMAT, Node(selection.getItemAt(0)).getPropertyValue(GitConstants.ID));	
		}
	}
}