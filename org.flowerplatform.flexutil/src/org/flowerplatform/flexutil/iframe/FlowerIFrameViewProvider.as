package org.flowerplatform.flexutil.iframe {
	
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.layout.IViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class FlowerIFrameViewProvider implements IViewProvider {
		
		public static const ID:String = "flowerIFrame";
		
		public function getId():String {
			return ID;
		}
		
		public function createView(viewLayoutData:ViewLayoutData):UIComponent {
			var view:IFlowerIFrame = createFlowerIFrame();
			view.url = viewLayoutData.customData;
			view.addCallback("syncCookies", syncCookies);
			return UIComponent(view);
		}
		
		/**
		 * Platform-dependent.
		 */
		protected function createFlowerIFrame():IFlowerIFrame {
			if (FlexUtilGlobals.getInstance().isMobile) {
				return new StageWebViewUIComponent();
			} else {
				return new FlowerIFrame();
			}
		}
		
		protected function syncCookies():Object {
			var cookies:Object = FlexUtilGlobals.getInstance().cookiesForJs;
			// clear; sync only on first open
			FlexUtilGlobals.getInstance().cookiesForJs = {};
			return cookies;
		}
		
		public function getTitle(viewLayoutData:ViewLayoutData=null):String {
			return viewLayoutData.customData;
		}
		
		public function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			return null;
		}
		
		public function getTabCustomizer(viewLayoutData:ViewLayoutData):Object {
			return null;
		}
		
		public function getViewPopupWindow(viewLayoutData:ViewLayoutData):UIComponent {
			return null;
		}
	}
}