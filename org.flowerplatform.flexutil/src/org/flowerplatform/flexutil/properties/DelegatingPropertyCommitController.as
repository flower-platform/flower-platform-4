package org.flowerplatform.flexutil.properties {
	import org.flowerplatform.flexutil.FlexUtilConstants;

	/**
	 * @author Cristian Spiescu
	 */
	public class DelegatingPropertyCommitController extends PropertyCommitController {
		public function DelegatingPropertyCommitController(orderIndex:int=0) {
			super(orderIndex);
		}
		
		override public function commitProperty(propertyEntry:PropertyEntry):void {
			var delegate:PropertyCommitController = PropertyCommitController(propertyEntry.typeDescriptorRegistry.getSingleController(FlexUtilConstants.FEATURE_PROPERTY_COMMIT_CONTROLLER, propertyEntry.model));
			if (delegate != null) {
				delegate.commitProperty(propertyEntry);
			}
		}
		
	}
}