package org.flowerplatform.flexutil.controller.operation {
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Cristian Spiescu
	 * @author Marius Iacob
	 */
	public class StringOperationResultCombiner extends OperationResultCombiner {
		
		public static const RESET:String = "StringOperationResultCombinerReset";
		
		public function StringOperationResultCombiner() {
			result = "";
		}
		/**
		 * @author Marius Iacob
		 */
		override public function combineResult(newResult:Object):void {
			if (newResult) {
				if (Utils.beginsWith(String(newResult), RESET)) {
					result = String(newResult).substr(RESET.length);
				} else {
					result += newResult;
				}
			}
		}
		
	}
}

