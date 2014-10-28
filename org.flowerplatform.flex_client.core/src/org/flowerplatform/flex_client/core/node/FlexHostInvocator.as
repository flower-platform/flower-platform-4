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
package org.flowerplatform.flex_client.core.node {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flex_client.core.editor.remote.FullNodeIdWithChildren;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.js_client.common_js_as.node.IHostInvocator;
	
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class FlexHostInvocator implements IHostInvocator {
		
		public function showMessageBox(titleKeyMessage:String, textKeyMessage:String, textParams:Array):void {			
			FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
				.setText(Resources.getMessage(textKeyMessage, textParams))
				.setTitle(Resources.getMessage(titleKeyMessage))
				.setWidth(300)
				.setHeight(200)
				.showMessageBox();
		}
				
		public function createFullNodeIdWithChildrenInstance():FullNodeIdWithChildren {			
			return new FullNodeIdWithChildren();
		}
		
		public function createListInstance():ArrayCollection {			
			return new ArrayCollection();
		}
		
		public function createMapInstance():Object {			
			return new Object();
		}
		
		public function addInMap(map:Object, key:String, value:Object):void {			
			map[key] = value;
		}
		
		public function addItem(list:ArrayCollection, item:Object):void {
			list.addItem(item);
		}
		
		public function addItemAt(list:ArrayCollection, item:Object, index:int):void {
			list.addItemAt(item, index);
		}
		
		public function contains(list:ArrayCollection, item:Object):Boolean {			
			return list.contains(item);
		}
		
		public function getItemAt(list:ArrayCollection, index:int):Object {			
			return list.getItemAt(index);
		}
		
		public function getItemIndex(list:ArrayCollection, item:Object):int {			
			return list.getItemIndex(item);
		}
		
		public function getLength(list:ArrayCollection):int {		
			return list.length;
		}
		
		public function removeItemAt(list:ArrayCollection, index:int):Object {			
			return list.removeItemAt(index);
		}
				
	}
}