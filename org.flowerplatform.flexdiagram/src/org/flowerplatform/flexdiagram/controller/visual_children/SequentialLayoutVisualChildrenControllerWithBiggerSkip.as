package org.flowerplatform.flexdiagram.controller.visual_children {
	
	public class SequentialLayoutVisualChildrenControllerWithBiggerSkip extends SequentialLayoutVisualChildrenController {
		
		override protected function getVisualElementsToSkip(model:Object):int {
			return 1;
		}
	}
}