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
package org.flowerplatform.flexutil.tree {
	import flash.events.IEventDispatcher;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.controls.treeClasses.TreeItemRenderer;
	import mx.core.ClassFactory;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	
	import spark.components.List;
	
	public class TreeList extends List {
		public static const UPDATE_TREE_RENDERER_EVENT:String = "updateTreeRenderer";

		private static const EXPANDED:int = 0x0001;
		private static const SELECTED:int = 0x0002;
		
		protected var linearizedDataProviderRefreshRequested:Boolean;
		
		public var hierarchicalModelAdapter:IHierarchicalModelAdapter;
		
		private var _rootNode:HierarchicalModelWrapper;
		
		public function TreeList() {
			super();
			useVirtualLayout = true;
			setStyle("skinClass", TreeListSkin);
		}
		
		public function get rootNode():IEventDispatcher {
			return _rootNode.treeNode;
		}

		public function set rootNode(value:IEventDispatcher):void {
			if (_rootNode != null) {
				hierarchicalModelAdapter.getChildren(_rootNode.treeNode).removeEventListener(CollectionEvent.COLLECTION_CHANGE, hierarchicalModelChangedHandler);
			}
			if (value == null) {
				// to force the removal of the collection listener
				calculateMapWithInterestingNodes();
				_rootNode = null;
				dataProvider = null;
			} else {
				_rootNode = new HierarchicalModelWrapper();
				_rootNode.expanded = true;
				_rootNode.treeNode = value;
				dataProvider = new ArrayList();
				requestRefreshLinearizedDataProvider();
				hierarchicalModelAdapter.getChildren(_rootNode.treeNode).addEventListener(CollectionEvent.COLLECTION_CHANGE, hierarchicalModelChangedHandler);
			}
		}

		public function requestRefreshLinearizedDataProvider():void {
			linearizedDataProviderRefreshRequested = true;
			invalidateProperties();
		}
		
		/**
		 * @author Cristian Spiescu
		 * @author Cristina Constantinescu
		 */ 
		override protected function commitProperties():void {	
			// When working with selectedIndices (e.g. set it programatically), 
			// the selectedIndices sets its value on super.commitProperties()
			// so super.commitProperties() must be called first,
			// otherwise selectedIndices will be re-set in refreshLinearizedDataProvider with old current selectedIndices
			// and the one initially set is gone
			super.commitProperties();
			if (linearizedDataProviderRefreshRequested) {
				refreshLinearizedDataProvider();
				linearizedDataProviderRefreshRequested = false;
			}			
		}
		
		public function refreshLinearizedDataProvider():void {
			var newLinearizedArray:Array = new Array();
			var expandedNodesMap:Dictionary = calculateMapWithInterestingNodes();
			var newSelectedIndices:Vector.<int> = new Vector.<int>(); 
			addNodeToNewLinearizedArray(-1, _rootNode.treeNode, newLinearizedArray, expandedNodesMap, newSelectedIndices);
			ArrayList(dataProvider).source = newLinearizedArray;
			selectedIndices = newSelectedIndices;
		}
		
		protected function addNodeToNewLinearizedArray(nestingLevel:int, treeNode:IEventDispatcher, newLinearizedArray:Array, expandedNodesMap:Dictionary, newSelectionIndices:Vector.<int>):void {
			var modelWrapper:HierarchicalModelWrapper;
			
			if (nestingLevel == -1) {
				// i.e. root
				modelWrapper = _rootNode;
			} else {
				modelWrapper = new HierarchicalModelWrapper();
				modelWrapper.nestingLevel = nestingLevel;
				modelWrapper.treeNode = treeNode;
				newLinearizedArray.push(modelWrapper);
				if (hierarchicalModelAdapter.getChildren(modelWrapper.treeNode) != null) {
					hierarchicalModelAdapter.getChildren(modelWrapper.treeNode).addEventListener(CollectionEvent.COLLECTION_CHANGE, hierarchicalModelChangedHandler);
				}
			}
			
			var value:int = expandedNodesMap[treeNode];
			if ((value & SELECTED) == SELECTED) {
				newSelectionIndices.push(newLinearizedArray.length - 1);
			}
			if ((value & EXPANDED) == EXPANDED) {
				modelWrapper.expanded = true;
				var childrenList:IList = hierarchicalModelAdapter.getChildren(treeNode);
				if (childrenList != null) {
					for (var i:int = 0; i < childrenList.length; i++) {
						addNodeToNewLinearizedArray(nestingLevel + 1, IEventDispatcher(childrenList.getItemAt(i)), newLinearizedArray, expandedNodesMap, newSelectionIndices);					
					}
				}
			}
		}
		
		protected function calculateMapWithInterestingNodes():Dictionary {
			var result:Dictionary = new Dictionary();
			for (var i:int = 0; i < dataProvider.length; i++) {
				var current:HierarchicalModelWrapper = HierarchicalModelWrapper(IList(dataProvider).getItemAt(i));
				if (hierarchicalModelAdapter.getChildren(current.treeNode) != null) {
					hierarchicalModelAdapter.getChildren(current.treeNode).removeEventListener(CollectionEvent.COLLECTION_CHANGE, hierarchicalModelChangedHandler);
				}
				var value:int = 0;
				if (current.expanded) {
					value = value | EXPANDED;
				}
				if (selectedIndices.indexOf(i) >= 0) {
					// is selected
					value = value | SELECTED;
				}
				if (value != 0) {
					result[current.treeNode] = value;
				}
			}
			
			result[_rootNode.treeNode] = EXPANDED;
			
			return result;
		}
		
		protected function hierarchicalModelChangedHandler(event:CollectionEvent):void {
			if (event.kind != CollectionEventKind.UPDATE) {
				requestRefreshLinearizedDataProvider();
			}
		}
		
		public function expandCollapseNode(modelWrapper:HierarchicalModelWrapper):void {
			modelWrapper.expanded = !modelWrapper.expanded;
			requestRefreshLinearizedDataProvider();
		}
	}
}