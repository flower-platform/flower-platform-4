package org.flowerplatform.flexutil.properties {
	import org.flowerplatform.flexutil.controller.AbstractController;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class PropertyCommitController extends AbstractController {
		public function PropertyCommitController(orderIndex:int=0) {
			super(orderIndex);
		}
		
		public function commitProperty(propertyEntry:PropertyEntry):void {
			throw new Error("This method should be implemented");
		}
	}
}