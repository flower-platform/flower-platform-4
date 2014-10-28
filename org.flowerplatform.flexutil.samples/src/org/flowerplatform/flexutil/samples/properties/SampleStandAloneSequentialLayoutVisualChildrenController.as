package org.flowerplatform.flexutil.samples.properties {
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	import org.flowerplatform.flexutil.flexdiagram.RendererController;
	import org.flowerplatform.flexutil.flexdiagram.StandAloneSequentialLayoutVisualChildrenController;
	import org.flowerplatform.flexutil.properties.PropertyEntryRendererController;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SampleStandAloneSequentialLayoutVisualChildrenController extends StandAloneSequentialLayoutVisualChildrenController {
		
		protected var rendererController:PropertyEntryRendererController = new PropertyEntryRendererController(new SamplePropertyCommitController());
		
		public function SampleStandAloneSequentialLayoutVisualChildrenController(visualElementsToSkip:int=0, requiredContainerClass:Class=null, orderIndex:int=0) {
			super(visualElementsToSkip, requiredContainerClass, orderIndex);
		}
		
		override protected function getRendererController(typeDescriptorRegistry:TypeDescriptorRegistry, childModel:Object):RendererController {
			return rendererController;
		}
		
	}
}