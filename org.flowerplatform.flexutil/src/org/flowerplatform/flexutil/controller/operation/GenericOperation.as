package org.flowerplatform.flexutil.controller.operation {
	import mx.collections.IList;
	import mx.core.FlexGlobals;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.controller.AbstractController;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class GenericOperation extends AbstractController {
		
		public var delegate:Function;
		
		public function GenericOperation(delegate:Function, orderIndex:int=0) {
			super(orderIndex);
			this.delegate = delegate;
		}
		
		public function run(... args):Object {
			return delegate.apply(null, args);
		}
		
		public static function runOperations(typeDescriptorRegistry:TypeDescriptorRegistry, feature:String, model:Object, operationResultCombiner:OperationResultCombiner, ... args):Object {
			try {
				if (operationResultCombiner == null) {
					operationResultCombiner = new FirstNotNullOperationResultCombiner();
				}
				var operations:IList = typeDescriptorRegistry.getAdditiveControllers(feature, model);
				for (var i:int = 0; i < operations.length; i++) {
					var current:GenericOperation = operations.getItemAt(i) as GenericOperation; 
					operationResultCombiner.combineResult(current.run.apply(null, args));
					if (operationResultCombiner is FirstNotNullOperationResultCombiner && operationResultCombiner.result != null) {
						break;
					}
				}
			} catch (error:Error) {
				var err:Error = error;
				UIComponent(FlexGlobals.topLevelApplication).callLater(function ():void {
					// we let the algorithm continue and throw at the end (so that we can
					// receive the exception on the server side
					throw new Error("Redispatching error with callLater; original:\n" + err.message + "\n" + err.getStackTrace());
				});
			}
			return operationResultCombiner.result; 
		}
		
	}
}