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
	import mx.collections.IList;
	import mx.events.CollectionEvent;
	
	import spark.components.DataGroup;
	
	public class TreeListDataGroup extends DataGroup {
		
//		private var _dataProviderChanged:Boolean;
//		
//		private var _lastScrollPosition:Number = 0;
//		
//		public function TreeListDataGroup()	{
//			super();
//		}
//		
//		override public function set dataProvider(value:IList):void {
//			if (dataProvider != null && value != dataProvider) {
//				dataProvider.removeEventListener(CollectionEvent.COLLECTION_CHANGE, onDataProviderChanged);
//			}
//			super.dataProvider = value;
//			
//			if (value != null) {
//				value.addEventListener(CollectionEvent.COLLECTION_CHANGE, onDataProviderChanged);
//			}
//		}
		
		override protected function commitProperties():void {
			// sometimes, the original mehtod, sets the scroll to 0, 0
			// we prevent this here
			var lastVerticalScrollPosition:Number = verticalScrollPosition;
			var lastHorizontalScrollPosition:Number = horizontalScrollPosition;
			
			super.commitProperties();
			
//			if (_dataProviderChanged) {
				verticalScrollPosition = lastVerticalScrollPosition;
				horizontalScrollPosition = lastHorizontalScrollPosition;
//			}
		}
		
//		private function onDataProviderChanged(e:CollectionEvent):void {
//			_dataProviderChanged = true;
//			invalidateProperties();
//		}
//		
//		
//		override public function set verticalScrollPosition(value:Number):void {
//			super.verticalScrollPosition = value;
//			_lastScrollPosition = value;
//		}
	}

}