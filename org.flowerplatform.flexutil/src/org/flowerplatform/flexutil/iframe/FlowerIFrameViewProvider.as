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