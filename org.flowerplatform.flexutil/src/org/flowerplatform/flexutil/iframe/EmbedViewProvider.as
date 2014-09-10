package org.flowerplatform.flexutil.iframe {
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.layout.IViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class EmbedViewProvider implements IViewProvider {
		
		public static const ID:String = "embed";
		
		public function getId():String {
			return ID;
		}
		
		public function createView(viewLayoutData:ViewLayoutData):UIComponent {
			var view:EmbedView = new EmbedView();
			view.url = viewLayoutData.customData;
			return view;
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