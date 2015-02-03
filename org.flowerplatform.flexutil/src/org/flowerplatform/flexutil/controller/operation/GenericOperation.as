/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.flexutil.controller.operation {
	import mx.collections.IList;
	
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
			if (operationResultCombiner == null) {
				operationResultCombiner = new FirstNotNullOperationResultCombiner();
			}
			var operations:IList = typeDescriptorRegistry.getAdditiveControllers(feature, model);
			for (var i:int = 0; i < operations.length; i++) {
				var current:GenericOperation = operations.getItemAt(i) as GenericOperation; 
				operationResultCombiner.combineResult(current.run.apply(null, args));
			}
			return operationResultCombiner.result; 
		}
		
	}
}