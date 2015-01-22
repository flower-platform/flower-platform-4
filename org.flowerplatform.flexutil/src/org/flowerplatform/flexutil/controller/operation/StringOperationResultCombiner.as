package org.flowerplatform.flexutil.controller.operation {
	
	/**
	 * @author Cristian Spiescu
	 */
	public class StringOperationResultCombiner extends OperationResultCombiner {
		
		public function StringOperationResultCombiner() {
			result = "";
		}
		
		override public function combineResult(newResult:Object):void {
			if (newResult) {
				result += newResult;
			}
		}
		
	}
}

