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
	import org.flowerplatform.flexdiagram.samples.model.BasicConnection;
	import org.flowerplatform.flexdiagram.samples.model.BasicModel;
	import org.flowerplatform.flexdiagram.samples.model.BasicSubModel;
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
			rootModel.subModelsDict["key1"] = "value1";
			rootModel.subModelsDict["key2"] = "value2";
			rootModel.subModelsDict["key3"] = "value3";
			
			var child2:SampleMindMapModel = getMindMapModel(rootModel);	
			child2.side = MindMapDiagramShell.POSITION_LEFT;
			for (var i:int = 0; i < 10; i++) {
				child2.children.addItem(getMindMapModel(child2));
			}
			child2.children.addItem(getMindMapModel(child2));	
			child2.hasChildren = true;
			child2.subModelsDict["key1"] = "value1";
			child2.subModelsDict["key2"] = "value1";
			child2.subModelsDict["key3"] = "value1";
			
			
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
			child.subModelsDict["key1"] = "value1";
			child.subModelsDict["key2"] = "value1";
			child.subModelsDict["key3"] = "value1";
			child.subModelsDict["key4"] = "value1";
			child.subModelsDict["key5"] = "value1";
			
			rootModel.children.addItem(child);
			
			var child1:SampleMindMapModel = getMindMapModel(rootModel);	
			child1.side = MindMapDiagramShell.POSITION_RIGHT;
			child1.children.addItem(getMindMapModel(child1));
			child1.hasChildren = true;
			child1.parent = rootModel;	
			child1.subModelsDict["key1"] = "value11";
			child1.subModelsDict["key2"] = "value11";
			child1.subModelsDict["key3"] = "value11";
			child1.subModelsDict["key4"] = "value11";
			child1.subModelsDict["key5"] = "value11";
			child1.subModelsDict["key6"] = "value11";
			child1.subModelsDict["key7"] = "value11";


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
			child111.subModelsDict["key1"] = "value11";
			child111.subModelsDict["key2"] = "value11";
			child111.subModelsDict["key3"] = "value11";
			child111.subModelsDict["key4"] = "value11";
			child111.subModelsDict["key5"] = "value11";
			child11.hasChildren = true;
			child11.children.addItem(child111);
			
			modelHolder.rootModel = rootModel;
			
		}
		
		/**
		 * @author Alexandra Topoloaga
		 */
		private static var currentModel:int;
		
	 	/**
		 * @author Cristina Constantinescu
	     * @author Alexandra Topoloaga
		 */
		private static function getMindMapModel(parent:Object):SampleMindMapModel {
			var model:SampleMindMapModel;
			
			model = new SampleMindMapModel();
			model.text = "MindMap" + currentModel++;
//			model.width = 151;
//			model.height = 22;
			model.hasChildren = false;
			model.fontFamily = "Times New Roman";
			model.fontSize = 28;
			model.fontStyle = true;
			model.fontWeight = true;
			if (parent is SampleMindMapModel && parent != null && parent.side != 0) {
				model.side = parent.side;
			} else if (parent is SampleMindMapModel && parent != null) {
				model.side = MindMapDiagramShell.POSITION_LEFT;
			}
			if (!(parent is ParentAwareArrayList)) {
				model.parent = parent;
			}
			
			//model.subModels = new ParentAwareArrayList(model, [new BasicSubModel("1", model), new BasicSubModel("2", model)]);
//			model.subModels.addItem(new BasicSubModel("bsm1", model));
//			model.subModels.addItem(new BasicSubModel("bsm2", model));
			return model;
		}
	}
}
