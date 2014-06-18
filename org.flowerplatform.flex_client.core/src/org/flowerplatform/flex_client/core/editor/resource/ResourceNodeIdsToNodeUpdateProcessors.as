/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flex_client.core.editor.resource {
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flex_client.core.editor.update.NodeUpdateProcessor;
	
	/**
	 * Convenience class to work with resourceNodeIds <-> node update processors.
	 * 
	 * @author Mariana Gheorghe
	 */
	public class ResourceNodeIdsToNodeUpdateProcessors {
		
		public var lastUpdateTimestampOfServer:Number = -1;
		public var lastUpdateTimestampOfClient:Number = -1;
		
		private var map:Dictionary = new Dictionary();
		
		public function getResourceNodeIds():ArrayCollection {
			var resourceNodeIds:ArrayCollection = new ArrayCollection();
			for (var resourceNodeId:String in map) {
				resourceNodeIds.addItem(resourceNodeId);
			}
			return resourceNodeIds;
		}
		
		public function getNodeUpdateProcessors(resourceNodeId:String):ArrayCollection {
			var nodeUpdateProcessors:ArrayCollection = map[resourceNodeId];
			if (nodeUpdateProcessors == null) {
				nodeUpdateProcessors = new ArrayCollection();
			}
			return nodeUpdateProcessors;
		}
		
		public function addNodeUpdateProcessor(resourceNodeId:String, nodeUpdateProcessor:NodeUpdateProcessor):void {
			var nodeUpdateProcessors:ArrayCollection = map[resourceNodeId];
			if (nodeUpdateProcessors == null) {
				nodeUpdateProcessors = new ArrayCollection();
				map[resourceNodeId] = nodeUpdateProcessors;
			}
			nodeUpdateProcessors.addItem(nodeUpdateProcessor);
		}
		
		public function removeNodeUpdateProcessor(resourceNodeId:String, nodeUpdateProcessor:NodeUpdateProcessor):void {
			var nodeUpdateProcessors:ArrayCollection = map[resourceNodeId];
			if (nodeUpdateProcessors != null) {
				nodeUpdateProcessors.removeItem(nodeUpdateProcessor);
				if (nodeUpdateProcessors.length == 0) {
					delete map[resourceNodeId];
				}
			}
		}
	}
}