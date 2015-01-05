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
	
	/**
	 * @author Cristian Spiescu
	 */
	public class MindMapPopulator {
		
		protected const labels:Array = [0, "1. Short", "2. A longer label", "3. This label is very looong"];
		protected const icons:Array = [0, "bee", "family,penguin", "bookmark,freemind_butterfly,wizard,ksmiletris"];
		
		protected function getCurrent(array:Array):String {
			var current:int = int(array[0]);
			array[0] = (current + 1) % (array.length - 1);
			return array[current + 1];
		}
		
		protected function createNode(parent:SampleMindMapModel):SampleMindMapModel {
			var result:SampleMindMapModel = new SampleMindMapModel();
			result.text = getCurrent(labels);
			result.icons = getCurrent(icons);
			result.parent = parent;
			result.side = parent.side;
			parent.children.addItem(result);
			return result;
		}
		
		public function populate(modelHolder:IModelHolder, topLevelModels:int, secondLevelModels:int):void { 
			var instanceNo:int = 0;
			var l0:SampleMindMapModel = new SampleMindMapModel();
			l0.text = "Mind Map Root";
			l0.hasChildren = true;
			
			for (var i:int = 0; i < topLevelModels; i++) {
				var l1:SampleMindMapModel = createNode(l0);
				l1.text += " " + instanceNo++;
				l1.hasChildren = true;
				if (i < topLevelModels / 2) {
					l1.side = MindMapDiagramShell.POSITION_LEFT;
				} else {
					l1.side = MindMapDiagramShell.POSITION_RIGHT;
				}
				for (var j:int = 0; j < secondLevelModels; j++) {
					var l2:SampleMindMapModel = createNode(l1);
					l2.text += " " + instanceNo++;
				}
			}
			modelHolder.rootModel = l0;
		}
		
	}
}