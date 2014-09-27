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
			//			var rootModel:ParentAwareArrayList = modelHolder.rootModel;
			//			if (rootModel == null) {
			//				rootModel = new ParentAwareArrayList(null);
			//			}
			//			
			var rootModel:SampleMindMapModel = getMindMapModel(rootModel);
			rootModel.text = "Root";
			rootModel.children.addItem(getMindMapModel(rootModel));
			rootModel.children.addItem(getMindMapModel(rootModel));
			rootModel.children.addItem(getMindMapModel(rootModel));
			rootModel.expanded = true;
			rootModel.note = "rootModelNote";
			rootModel.details = "rootModelDetails";
			
			var child2:SampleMindMapModel = getMindMapModel(rootModel);	
			child2.side = MindMapDiagramShell.POSITION_LEFT; 
			for (var i:int = 0; i < 40; i++) {
				child2.children.addItem(getMindMapModel(child2));
			}
			child2.children.addItem(getMindMapModel(child2));	
			child2.hasChildren = true;
			
			rootModel.children.addItem(child2);
			rootModel.hasChildren = true;
			
			var child:SampleMindMapModel = getMindMapModel(rootModel);
			child.side = MindMapDiagramShell.POSITION_RIGHT; 
			for (var i:int = 0; i < 10; i++) {
				child.children.addItem(getMindMapModel(child));
			}
			child.children.addItem(getMindMapModel(child));
			child.children.addItem(getMindMapModel(child));
			child.children.addItem(getMindMapModel(child));
			child.hasChildren = true;
			child.parent = rootModel;
			
			rootModel.children.addItem(child);
			
			var child1:SampleMindMapModel = getMindMapModel(rootModel);	
			child1.side = MindMapDiagramShell.POSITION_RIGHT;
			child1.children.addItem(getMindMapModel(child1));
			child1.hasChildren = true;
			child1.parent = rootModel;				
			rootModel.children.addItem(child1);	
			
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
			
			var child3:SampleMindMapModel = getMindMapModel(rootModel);	
			child3.side = MindMapDiagramShell.POSITION_RIGHT;
			child3.children.addItem(getMindMapModel(child3));
			child3.hasChildren = true;
			child3.parent = rootModel;				
			rootModel.children.addItem(child3);	
			
			modelHolder.rootModel = rootModel;
		}
		
		/**
		 * @author Alexandra Topoloaga
		 */
		private static var currentModel:int;
		private static var text:String = "Mm";
		
		/**
		 * @author Cristina Constantinescu
		 * @author Alexandra Topoloaga
		 */
		protected static function getMindMapModel(parent:Object):SampleMindMapModel {
			text = "mm";
			var model:SampleMindMapModel = new SampleMindMapModel();
			var number:Number = Math.random() * 50;
			for (var i:int = 0; i < number; i++) {
				text += "**";	
			}
			model.text = text + currentModel++;
			//			model.width = 151;
			//			model.height = 22;
			model.hasChildren = false;
			model.fontFamily = "SansSerif";
			model.fontSize = 9;
			model.fontItalic = true;
			model.fontBold = false;
			model.textColor = 0x000000;
			model.backgroundColor = 0xFFFFFF;
			if (parent is SampleMindMapModel && parent != null && parent.side != 0) {
				model.side = parent.side;
			} else if (parent is SampleMindMapModel && parent != null) {
				model.side = MindMapDiagramShell.POSITION_LEFT;
			}
			if (!(parent is ParentAwareArrayList)) {
				model.parent = parent;
			}
			return model;
		}
	}
}