package org.flowerplatform.flexutil.properties {
	import org.flowerplatform.flexutil.controller.AbstractController;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class PropertyCommitController extends AbstractController {
		public function PropertyCommitController(orderIndex:int=0) {
			super(orderIndex);
		}
		
		protected function getTarget(propertyEntry:PropertyEntry):Object {
			var target:Object;
			if (propertyEntry.eventDispatcher != null) {
				target = propertyEntry.eventDispatcher;
			} else {
				target = propertyEntry.model;
			}
			return target;
		}
		
		public function commitProperty(propertyEntry:PropertyEntry):void {
			var target:Object = getTarget(propertyEntry);
			target[propertyEntry.descriptor.name] = propertyEntry.value;
		}
		
		public function unsetProperty(propertyEntry:PropertyEntry):void {
			var target:Object = getTarget(propertyEntry);
			delete target[propertyEntry.descriptor.name];
		}
	}
}