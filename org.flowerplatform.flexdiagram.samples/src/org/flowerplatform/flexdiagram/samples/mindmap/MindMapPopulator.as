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
package org.flowerplatform.flexdiagram.samples.mindmap {
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.samples.IModelHolder;
	import org.flowerplatform.flexdiagram.samples.mindmap.model.SampleMindMapModel;
	import org.flowerplatform.flexdiagram.util.ParentAwareArrayList;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapPopulator {
		
		public static function populateRootModel(modelHolder:IModelHolder):void {
			var rootModel:ParentAwareArrayList = modelHolder.rootModel;
			if (rootModel == null) {
				rootModel = new ParentAwareArrayList(null);
				rootModel.parent = rootModel;
			}
			
			var model:SampleMindMapModel = getMindMapModel(rootModel);
			model.text = "Root";
			model.children.addItem(getMindMapModel(model));
			model.children.addItem(getMindMapModel(model));
			model.children.addItem(getMindMapModel(model));
			model.expanded = true;
			rootModel.addItem(model);
			
			var child2:SampleMindMapModel = getMindMapModel(model);	
			child2.side = MindMapDiagramShell.LEFT;
			for (var i:int = 0; i < 10; i++) {
				child2.children.addItem(getMindMapModel(child2));
			}
			child2.children.addItem(getMindMapModel(child2));	
			child2.hasChildren = true;
			
			model.children.addItem(child2);
			model.hasChildren = true;
			
			var child:SampleMindMapModel = getMindMapModel(model);
			child.side = MindMapDiagramShell.RIGHT;
			for (var i:int = 0; i < 10; i++) {
				child.children.addItem(getMindMapModel(child));
			}
			child.children.addItem(getMindMapModel(child));
			child.children.addItem(getMindMapModel(child));
			child.children.addItem(getMindMapModel(child));
			child.hasChildren = true;
			child.parent = model;
			
			model.children.addItem(child);
			
			var child1:SampleMindMapModel = getMindMapModel(model);	
			child1.side = MindMapDiagramShell.RIGHT;
			child1.children.addItem(getMindMapModel(child1));
			child1.hasChildren = true;
			child1.parent = model;				
			model.children.addItem(child1);	
			
			var child11:SampleMindMapModel = getMindMapModel(child1);
			for (var i:int = 0; i < 10; i++) {
				child11.children.addItem(getMindMapModel(child11));
			}
			child11.children.addItem(getMindMapModel(child11));
			child11.hasChildren = true;
			child1.children.addItem(child11);			
			
			var child111:SampleMindMapModel = getMindMapModel(child11);				
			child111.children.addItem(getMindMapModel(child111));
			child111.children.addItem(getMindMapModel(child111));
			child111.hasChildren = true;
			child11.children.addItem(child111);	
			
			modelHolder.rootModel = rootModel;
		}
		
		private static function getMindMapModel(parent:Object):SampleMindMapModel {
			var model:SampleMindMapModel;
			
			model = new SampleMindMapModel();
			model.text = "MindMap" + (new Date()).time;
//			model.width = 151;
//			model.height = 22;
			model.hasChildren = false;
			if (parent is SampleMindMapModel && parent != null && parent.side != 0) {
				model.side = parent.side;
			} else if (parent is SampleMindMapModel && parent != null) {
				model.side = MindMapDiagramShell.LEFT;
			}
			model.parent = parent;
			
			return model;
		}
	}
}