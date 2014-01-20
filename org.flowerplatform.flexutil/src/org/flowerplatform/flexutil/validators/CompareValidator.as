/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.flexutil.validators
{
	import mx.validators.ValidationResult;
	import mx.validators.Validator;
	
	/**
	 * Validator used to compare 2 objects.
	 * 
	 * @author Cristina
	 */ 
	public class CompareValidator extends Validator {
	
		public var valueToCompare:Object;
   		
   		public var errorMessage:String = "Value does not match.";
		
     	override protected function doValidation(value:Object):Array {
  	 		var results:Array = [];
     		var srcVal:Object = this.getValueFromSource();
 
     		if (srcVal != valueToCompare) {
        		results.push(new ValidationResult(true, null, "Match", errorMessage));
       		}
     		return results;
		}
	}
}