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
package org.flowerplatform.flexutil.properties {
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexutil.flexdiagram.IRendererControllerAware;
	import org.flowerplatform.flexutil.flexdiagram.RendererController;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class PropertyEntryRendererController extends RendererController {
		
		public var propertyCommitController:PropertyCommitController;
		
		public var removePropertyIcon:Object = null;
		
		public function PropertyEntryRendererController(propertyCommitController:PropertyCommitController, removePropertyIcon:Object = null, orderIndex:int=0) {
			super(orderIndex);
			this.propertyCommitController = propertyCommitController;
			this.removePropertyIcon = removePropertyIcon;
		}
		
		override public function createRenderer(context:Object, model:Object):IVisualElement {
			var clazz:Class = Class(getUniqueKeyForRendererToRecycle(context, model));
			var renderer:IVisualElement = IVisualElement(new clazz());
			if (renderer is IPropertyCommitControllerAware) {
				IPropertyCommitControllerAware(renderer).propertyCommitController = propertyCommitController;
			}
			if (renderer is IRendererControllerAware) {
				IRendererControllerAware(renderer).rendererController = this;
			}
			return renderer;
		}
		
		override public function getUniqueKeyForRendererToRecycle(context:Object, model:Object):Object {
			if (PropertyEntry(model).isGroup) {
				return GroupPropertyEntryRenderer;
			} else {
				return PropertyEntryRenderer;
			}
		}
		
	}
}