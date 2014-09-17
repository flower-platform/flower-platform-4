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
	
	import org.flowerplatform.flexdiagram.mindmap.MindMapRootModelWrapper;
	import org.flowerplatform.flexdiagram.samples.SampleConstants;
	import org.flowerplatform.flexdiagram.samples.mindmap.model.SampleMindMapModel;
	import org.flowerplatform.flexutil.Pair;
	import org.flowerplatform.flexutil.controller.ITypeProvider;
	
	public class SampleMindMapTypeProvider implements org.flowerplatform.flexutil.controller.ITypeProvider {
		
		public function getType(model:Object):String {
			if (model is MindMapRootModelWrapper) {
				return MindMapRootModelWrapper.ID;				
			}
			if (model is SampleMindMapModel) {
				return SampleConstants.SAMPLE_MINDMAP_NODE_TYPE;
			}
			if (model is Pair) {
				return SampleConstants.SAMPLE_MINDMAP_NODE_PROPERTY_TYPE;
			}
			return null;
		}
		
	}
	
}
