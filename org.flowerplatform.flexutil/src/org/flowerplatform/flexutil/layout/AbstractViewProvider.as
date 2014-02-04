package org.flowerplatform.flexutil.layout {
	
	import mx.core.UIComponent;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class AbstractViewProvider implements IViewProvider {
				
		public function getId():String {
			throw new Error("This method needs to be implemented.");
		}
		
		public function createView(viewLayoutData:ViewLayoutData):UIComponent {
			throw new Error("This method needs to be implemented.");
		}
		
		public function getTitle(viewLayoutData:ViewLayoutData=null):String {
			throw new Error("This method needs to be implemented.");
		}
		
		public function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			throw new Error("This method needs to be implemented.");
		}
		
		public function getTabCustomizer(viewLayoutData:ViewLayoutData):Object {
			return null;
		}
		
		public function getViewPopupWindow(viewLayoutData:ViewLayoutData):UIComponent {
			return null;
		}
		
	}
}