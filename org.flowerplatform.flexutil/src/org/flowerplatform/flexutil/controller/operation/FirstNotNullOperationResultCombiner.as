package org.flowerplatform.flexutil.controller.operation {
	
	/**
	 * @author Cristian Spiescu
	 */
	public class FirstNotNullOperationResultCombiner extends OperationResultCombiner {
		
		override public function combineResult(newResult:Object):void {
			if (result == null) {
				result = newResult;
			}
		}
		
	}
}

