package org.flowerplatform.flexdiagram.samples.properties {
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.renderer.RendererController;
	import org.flowerplatform.flexutil.properties.GroupPropertyEntryRenderer;
	import org.flowerplatform.flexutil.properties.IPropertyCommitControllerAware;
	import org.flowerplatform.flexutil.properties.PropertyCommitController;
	import org.flowerplatform.flexutil.properties.PropertyEntry;
	import org.flowerplatform.flexutil.properties.PropertyEntryRenderer;
	
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
			var clazz:Class = Class(geUniqueKeyForRendererToRecycle(context, model));
			var renderer:IVisualElement = IVisualElement(new clazz());
			if (renderer is IPropertyCommitControllerAware) {
				IPropertyCommitControllerAware(renderer).propertyCommitController = propertyCommitController;
			}
			return renderer;
		}
		
		override public function geUniqueKeyForRendererToRecycle(context:Object, model:Object):Object {
			if (PropertyEntry(model).isGroup) {
				return GroupPropertyEntryRenderer;
			} else {
				return PropertyEntryRenderer;
			}
		}
		
	}
}