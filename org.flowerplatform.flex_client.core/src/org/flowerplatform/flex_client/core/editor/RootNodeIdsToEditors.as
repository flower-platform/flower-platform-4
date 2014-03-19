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
package org.flowerplatform.flex_client.core.editor {
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	
	/**
	 * Convenience class to work with rootNodeIds <-> editors.
	 * 
	 * @author Mariana Gheorghe
	 */
	public class RootNodeIdsToEditors {
		
		public var lastUpdateTimestamp:Number = -1;
		
		private var map:Dictionary = new Dictionary();
		
		public function getRootNodeIds():ArrayCollection {
			var rootNodeIds:ArrayCollection = new ArrayCollection();
			for (var rootNodeId:String in map) {
				rootNodeIds.addItem(rootNodeId);
			}
			return rootNodeIds;
		}
		
		public function getEditors(rootNodeId:String):ArrayCollection {
			var editors:ArrayCollection = map[rootNodeId];
			if (editors == null) {
				editors = new ArrayCollection();
			}
			return editors;
		}
		
		public function addEditor(rootNodeId:String, editor:EditorFrontend):void {
			var editors:ArrayCollection = map[rootNodeId];
			if (editors == null) {
				editors = new ArrayCollection();
				map[rootNodeId] = editors;
			}
			editors.addItem(editor);
		}
		
		public function removeEditor(rootNodeId:String, editor:EditorFrontend):void {
			var editors:ArrayCollection = map[rootNodeId];
			if (editors != null) {
				editors.removeItem(editor);
				if (editors.length == 0) {
					delete map[rootNodeId];
				}
			}
		}
		
	}
}