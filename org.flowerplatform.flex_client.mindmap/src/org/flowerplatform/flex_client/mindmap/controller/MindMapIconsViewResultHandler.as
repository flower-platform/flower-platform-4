/**
 * @author Alina Bratu
 */

package org.flowerplatform.flex_client.mindmap.controller
{
	import org.flowerplatform.flexutil.dialog.IDialogResultHandler;

	public class MindMapIconsViewResultHandler implements IDialogResultHandler {
		
		private var handlerFunction:Function;
		
		public function MindMapIconsViewResultHandler(handlerFunction:Function) {
			this.handlerFunction = handlerFunction;
		}
		
		public function handleDialogResult(result:Object):void {
			handlerFunction(result);
		}
	}
}