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
package org.flowerplatform.flexdiagram.samples.mindmap.controller {
	import mx.core.IVisualElementContainer;
	
	import org.flowerplatform.flexdiagram.controller.visual_children.SequentialLayoutVisualChildrenController;
	import org.flowerplatform.flexutil.flexdiagram.StandAloneSequentialLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.samples.mindmap.SampleMindMapDiagramShell;
	import org.flowerplatform.flexdiagram.samples.mindmap.model.SampleMindMapModel;
	import org.flowerplatform.flexutil.properties.PropertiesForm;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SampleSequentialLayoutVisualChildrenController extends SequentialLayoutVisualChildrenController {
		
		private var ds:SampleMindMapDiagramShell;
		
		public function SampleSequentialLayoutVisualChildrenController(ds:SampleMindMapDiagramShell, orderIndex:int=0) {
			super(1, PropertiesForm, orderIndex);
			this.ds = ds;
		}
		
		override protected function showChildren(context:Object, parentRenderer:IVisualElementContainer, parentModel:Object):int {
			var model:SampleMindMapModel = SampleMindMapModel(parentModel);
			return model.editProperties ? StandAloneSequentialLayoutVisualChildrenController.CHILDREN_SHOW : StandAloneSequentialLayoutVisualChildrenController.CHILDREN_DISABLED;
		}
		
	}
}