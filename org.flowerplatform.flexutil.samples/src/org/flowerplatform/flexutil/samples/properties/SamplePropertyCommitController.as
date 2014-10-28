package org.flowerplatform.flexutil.samples.properties {
	import org.flowerplatform.flexutil.properties.PropertyCommitController;
	import org.flowerplatform.flexutil.properties.PropertyEntry;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SamplePropertyCommitController extends PropertyCommitController {
		public function SamplePropertyCommitController(orderIndex:int=0) {
			super(orderIndex);
		}
		
		override public function commitProperty(propertyEntry:PropertyEntry):void {
			propertyEntry.eventDispatcher[propertyEntry.descriptor.name] = propertyEntry.value;
		}
		
	}
}