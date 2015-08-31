package org.flowerplatform.flexutil.popup.manager {
	import mx.core.Singleton;
	import mx.preloaders.DownloadProgressBar;
	
	/**
	 * Needed to register a custom popup manager.
	 * @author Cristina Constantinescu
	 */
	public class CustomPreloader extends DownloadProgressBar {
		
		override public function initialize():void {
			super.initialize();
		
			Singleton.registerClass("mx.managers::IPopUpManager", CustomPopupManager);
		};
	}
}