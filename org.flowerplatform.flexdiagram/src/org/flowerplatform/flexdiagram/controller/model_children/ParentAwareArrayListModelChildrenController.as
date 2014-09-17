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
package org.flowerplatform.flexdiagram.controller.model_children {
	import mx.collections.IList;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.util.ParentAwareArrayList;
	
	/**
	 * <b>NOTE:</b> The handler for removed elements doesn't work for the follwing cases: a) <code>list.removeAll()</code> and
	 * b) <code>list.source = newValue</code> because a CollectionEvent of type RESET is sent which doen not contain information
	 * about the old elements. This may be solved by adding logic (i.e. event dispatch) to the list in these 2 methods or maybe even better
	 * in <code>stopTrackUpdates()</code>. 
	 * 
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */
	public class ParentAwareArrayListModelChildrenController extends ModelChildrenController {
		
		protected var shouldListenForRemovedElements:Boolean;
		
		public function ParentAwareArrayListModelChildrenController(shouldListenForRemovedElements:Boolean, orderIndex:int = 0) {
			super(orderIndex);
			this.shouldListenForRemovedElements = shouldListenForRemovedElements;
		}
		
		override public function getParent(context:DiagramShellContext, model:Object):Object {
			return getParentAwareArrayList(model).parent;
		}
		
		override public function getChildren(context:DiagramShellContext, model:Object):IList {
			return getParentAwareArrayList(model);
		}
		
		protected function getParentAwareArrayList(model:Object):ParentAwareArrayList {
			return ParentAwareArrayList(model);
		}
		
		override public function beginListeningForChanges(context:DiagramShellContext, model:Object):void {
			getParentAwareArrayList(model).addEventListener(CollectionEvent.COLLECTION_CHANGE, function (event:CollectionEvent):void {collectionChangeHandler(event, context)}); 	
		}
		
		override public function endListeningForChanges(context:DiagramShellContext, model:Object):void {
			getParentAwareArrayList(model).removeEventListener(CollectionEvent.COLLECTION_CHANGE, function (event:CollectionEvent):void {collectionChangeHandler(event, context)});
		}
		
		protected function collectionChangeHandler(event:CollectionEvent, context:DiagramShellContext):void {
			var parentModel:Object = ParentAwareArrayList(event.target).parent;
			
			if (event.kind != CollectionEventKind.UPDATE) {
				// we don't want "UPDATE" because it's actually a property change event
				// of a child
				context.diagramShell.shouldRefreshVisualChildren(context, parentModel);
			}
			
			if (shouldListenForRemovedElements) {
				var removedElement:Object = null;
				if (event.kind == CollectionEventKind.REMOVE) {
					removedElement = event.items[0];
				} else if (event.kind == CollectionEventKind.REPLACE) {
					removedElement = PropertyChangeEvent(event.items[0]).oldValue;
				}
				if (removedElement != null) {
					context.diagramShell.unassociateModelFromRenderer(context, removedElement, context.diagramShell.getRendererForModel(context, removedElement), true);
				}
			}
		}
		
	}
}