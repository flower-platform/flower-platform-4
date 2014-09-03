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
package org.flowerplatform.flexdiagram.samples.mindmap.controller {
	
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.collections.IList;
	
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.model_children.ModelChildrenController;
	import org.flowerplatform.flexdiagram.event.UpdateConnectionEndsEvent;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelRendererController;
	import org.flowerplatform.flexdiagram.samples.mindmap.model.SampleMindMapModel;
	import org.flowerplatform.flexutil.Pair;
	import org.flowerplatform.flexutil.properties.PropertiesHelper;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class SampleMindMapModelChildrenController extends ModelChildrenController {
		
		private static const EMPTY_LIST:ArrayList = new ArrayList();
		
				
		override public function getParent(context:DiagramShellContext, model:Object):Object {
			return SampleMindMapModel(model).parent;
		}
		
		override public function getChildren(context:DiagramShellContext, model:Object):IList	{
			// no children; this controller is used only to dispatch events
			//return EMPTY_LIST;
			var list:IList = new ArrayCollection();
			
			var subModels:Dictionary = Dictionary(PropertiesHelper.getInstance().propertyModelAdapter.getProperties(model));
			for(var key:Object in subModels) {
				var dictionaryEntry:Pair = new Pair();
				dictionaryEntry.a = key;
				dictionaryEntry.b = PropertiesHelper.getInstance().propertyModelAdapter.getPropertyValue(model,String(key));
				list.addItem(dictionaryEntry);
			}
			return list;
		}
		
		
		override public function beginListeningForChanges(context:DiagramShellContext, model:Object):void {			
			SampleMindMapModel(model).addEventListener(UpdateConnectionEndsEvent.UPDATE_CONNECTION_ENDS, function (event:UpdateConnectionEndsEvent):void {updateConnectionEndsHandler(event, context);});
		}
		
		override public function endListeningForChanges(context:DiagramShellContext, model:Object):void {			
			SampleMindMapModel(model).removeEventListener(UpdateConnectionEndsEvent.UPDATE_CONNECTION_ENDS, function (event:UpdateConnectionEndsEvent):void {updateConnectionEndsHandler(event, context);});
		}
				
		protected function updateConnectionEndsHandler(event:UpdateConnectionEndsEvent, context:DiagramShellContext):void {
			MindMapModelRendererController(ControllerUtils.getRendererController(context, event.target)).updateConnectors(context, event.target);
		}

	}
}