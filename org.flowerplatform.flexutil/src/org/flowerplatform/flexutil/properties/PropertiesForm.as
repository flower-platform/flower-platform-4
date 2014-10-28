package org.flowerplatform.flexutil.properties {
	import spark.components.Form;
	import spark.layouts.FormLayout;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	import org.flowerplatform.flexutil.flexdiagram.StandAloneSequentialLayoutVisualChildrenController;
	
	/**
	 * Can be uses as a stand alone component; in this case some fields need to be provided.
	 * 
	 * <p>
	 * Can also be used in a diagram renderer. In this case, it doesn't do much, besides setting some styles.
	 * 
	 * @author Cristian Spiescu
	 */
	public class PropertiesForm extends Form {
		
		private var _shouldRefreshVisualChildren:Boolean;
		
		private var _data:Object;
		
		/**
		 * Needs to be provided if the component is used in stand alone mode. 
		 */
		public var visualChildrenController:StandAloneSequentialLayoutVisualChildrenController;

		/**
		 * Needs to be provided if the component is used in stand alone mode. 
		 */
		public var typeDescriptorRegistry:TypeDescriptorRegistry;
		
		/**
		 * Needs to be provided if the component is used in stand alone mode. 
		 */
		public var propertiesHelper:PropertiesHelper;
		
		public function get shouldRefreshVisualChildren():Boolean {
			return _shouldRefreshVisualChildren;
		}
		
		public function set shouldRefreshVisualChildren(value:Boolean):void {
			_shouldRefreshVisualChildren = value;
		}
		
		public function get data():Object {
			return _data;
		}
		
		public function set data(value:Object):void {
			_data = value;
			shouldRefreshVisualChildren = true;
			invalidateDisplayList();
		}
		
		override protected function createChildren():void {
			super.createChildren();
			var layout:FormLayout = new FormLayout();
			if (!FlexUtilGlobals.getInstance().isMobile) {
				layout.gap = -14;
			}
			layout.paddingLeft = 0;
			layout.paddingRight = 0;
			this.layout = layout;
			percentWidth = 100;
			contentGroup.setStyle("top", 0);
			contentGroup.setStyle("bottom", 0);	
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {			
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			if (visualChildrenController != null && shouldRefreshVisualChildren) {
				// i.e. used in stand alone mode
				visualChildrenController.refreshVisualChildrenDiagramOrStandAlone(null, typeDescriptorRegistry, this, data,
					propertiesHelper.getPropertyEntries(typeDescriptorRegistry, data, true));
				// this variable is managed by the controller only for the "normal" mode; i.e. not stand alone
				shouldRefreshVisualChildren = false;
			}
		}
		
	}
}