package org.flowerplatform.flexutil.iframe {
	
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;

	public class IFrameOpenUrl extends ActionBase {
		
		public function IFrameOpenUrl() {
			super();
		}
		
		override public function run():void {
			var view:IFrameOpenUrlView = new IFrameOpenUrlView();
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()			
				.setViewContent(view)
				.setTitle(Resources.getMessage("iframe.title"))
				.setIcon(Resources.urlIcon)
				.setWidth(350)
				.setHeight(150)
				.show();
		}
	}
}