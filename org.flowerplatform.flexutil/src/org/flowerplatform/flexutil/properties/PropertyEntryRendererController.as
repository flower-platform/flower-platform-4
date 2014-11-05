package org.flowerplatform.flexutil.properties {
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexutil.flexdiagram.RendererController;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class PropertyEntryRendererController extends RendererController {
		
		public var propertyCommitController:PropertyCommitController;
		
		public function PropertyEntryRendererController(propertyCommitController:PropertyCommitController, orderIndex:int=0) {
			super(orderIndex);
			this.propertyCommitController = propertyCommitController;
		}
		
		override public function createRenderer(context:Object, model:Object):IVisualElement {
			var clazz:Class = Class(getUniqueKeyForRendererToRecycle(context, model));
			var renderer:IVisualElement = IVisualElement(new clazz());
			if (renderer is IPropertyCommitControllerAware) {
				IPropertyCommitControllerAware(renderer).propertyCommitController = propertyCommitController;
			}
			return renderer;
		}
		
		override public function getUniqueKeyForRendererToRecycle(context:Object, model:Object):Object {
			if (PropertyEntry(model).isGroup) {
				return GroupPropertyEntryRenderer;
			} else {
				return PropertyEntryRenderer;
			}
		}
		
	}
}