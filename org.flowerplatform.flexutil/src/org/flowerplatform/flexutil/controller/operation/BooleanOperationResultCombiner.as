package org.flowerplatform.flexutil.controller.operation {
	
	
	/**
	 * @author Iordanescu Vlad
	 */
	public class BooleanOperationResultCombiner extends OperationResultCombiner {
		
		public function BooleanOperationResultCombiner() {
			result = true;
		}
		
		override public function combineResult(newResult:Object):void {
			result = result && newResult;
		}
		
	}
}

