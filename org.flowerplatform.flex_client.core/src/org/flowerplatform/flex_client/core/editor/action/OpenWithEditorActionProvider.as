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
package org.flowerplatform.flex_client.core.editor.action {
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flexutil.Pair;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.IActionProvider;
	
	/**	
	 * @author Cristina Constantinescu
	 */
	public class OpenWithEditorActionProvider implements IActionProvider {
		
		public function getActions(selection:IList):Vector.<IAction> {
			if (selection == null || selection.length != 1 || !(selection.getItemAt(0) is Node)) {
				return null;
			}
			
			var result:Vector.<IAction> = new Vector.<IAction>();			
			var node:Node = Node(selection.getItemAt(0));
			
			var subscribableResources:ArrayCollection = ArrayCollection(node.properties[CoreConstants.SUBSCRIBABLE_RESOURCES]);
			if (subscribableResources != null && subscribableResources.length > 1) {
				for each (var pair:Pair in subscribableResources) {
					result.push(new OpenAction(pair.b as String));				
				}
			}			
			return result;
		}
	}
}