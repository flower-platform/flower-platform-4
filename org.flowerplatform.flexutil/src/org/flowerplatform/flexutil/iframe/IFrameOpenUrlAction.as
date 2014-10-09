package org.flowerplatform.flexutil.iframe {
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;

	public class IFrameOpenUrlAction extends ActionBase {
		
		override public function run():void {
			var view:IFrameOpenUrlView = new IFrameOpenUrlView();
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()			
				.setViewContent(view)
				.setTitle(label)
				.setIcon(icon)
				.setWidth(350)
				.setHeight(150)
				.show();
		}
	}
}