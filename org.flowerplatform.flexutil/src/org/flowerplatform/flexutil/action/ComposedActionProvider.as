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
package org.flowerplatform.flexutil.action {
	
	import mx.collections.IList;
	
	/**
	 * Returns the actions from all registered <tt>IActionProviders</tt>, after they were processed by
	 * the <tt>IComposedActionProviderProcessor</tt>.
	 * 
	 * @author Mariana Gheorghe
	 */
	public class ComposedActionProvider implements IActionProvider {
		
		public var actionProviders:Vector.<IActionProvider> = new Vector.<IActionProvider>();
		
		public var composedActionProviderProcessors:Vector.<IComposedActionProviderProcessor> = new Vector.<IComposedActionProviderProcessor>();
		
		public function getActions(selection:IList):Vector.<IAction> {
			var result:Vector.<IAction> = new Vector.<IAction>();
			for each (var provider:IActionProvider in actionProviders) {
				var actions:Vector.<IAction> = provider.getActions(selection);
				if (actions == null) {
					continue;
				}
				for each (var action:IAction in actions) {
					processAction(action);
					result.push(action);
				}
			}
			return result;
		}
		
		protected function processAction(action:IAction):void {
			for each (var processor:IComposedActionProviderProcessor in composedActionProviderProcessors) {
				processor.processAction(action);
			}
		}
		
	}
}