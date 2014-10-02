package org.flowerplatform.flexutil.iframe {
	
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;

	/**
	 * @author Andreea Tita
	 */
	public class OpenUrlInIFrameAction extends ActionBase {
		
		public function OpenUrlInIFrameAction() {
			super();
		}
		
		override public function run():void {
			var view:OpenUrlInIFrameView = new OpenUrlInIFrameView();
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()			
				.setViewContent(view)
				.setTitle(Resources.getMessage("iframe.title"))
				.setIcon(Resources.openUrlIcon)
				.setWidth(350)
				.setHeight(150)
				.show();
		}
	}
}