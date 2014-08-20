package org.flowerplatform.flex_client.core.editor.action
{
	import org.flowerplatform.flexutil.controller.AbstractController;

	public class ActionDescriptor extends AbstractController {
		
		private var _actionId:String;
		
		public function ActionDescriptor(s:String):void {
			_actionId = s;
		}
		
		public function get actionId():String {
			return _actionId;
		}
		
	}
}