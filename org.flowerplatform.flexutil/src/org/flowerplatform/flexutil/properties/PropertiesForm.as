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
	import mx.collections.IList;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.managers.IFocusManagerComponent;
	
	import spark.components.Form;
	import spark.components.Group;
	import spark.layouts.FormLayout;
	
	import org.flowerplatform.flexutil.FlexUtilConstants;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	import org.flowerplatform.flexutil.flexdiagram.StandAloneSequentialLayoutVisualChildrenController;
	import org.flowerplatform.flexutil.view_content_host.BasicViewContent;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	
	/**
	 * Can be used as a stand alone component; in this case some fields need to be provided.
	 * 
	 * <p>
	 * Can also be used in a diagram renderer. In this case, it doesn't do much, besides setting some styles.
	 * 
	 * @author Cristian Spiescu
	 */
	public class PropertiesForm extends Form {
		
		private var _shouldRefreshVisualChildren:Boolean;
		
		private var _data:Object;
		
		public var context:Object = null;
		
		public var shouldSetFocus:Boolean = true;
		
		/**
		 * Needs to be provided if the component is used in stand alone mode. 
		 */
		public var visualChildrenController:StandAloneSequentialLayoutVisualChildrenController;

		/**
		 * Needs to be provided if the component is used in stand alone mode. 
		 */
		public var typeDescriptorRegistry:TypeDescriptorRegistry;
		
		/**
		 * Needs to be provided if the component is used in stand alone mode. 
		 */
		public var propertiesHelper:PropertiesHelper;
		
		public function get shouldRefreshVisualChildren():Boolean {
			return _shouldRefreshVisualChildren;
		}
		
		public function set shouldRefreshVisualChildren(value:Boolean):void {
			_shouldRefreshVisualChildren = value;
			if (value) {
				invalidateDisplayList();
			}
		}
		
		public function get data():Object {
			return _data;
		}
		
		public function set data(value:Object):void {
			_data = value;
			shouldRefreshVisualChildren = true;
		}
		
		override protected function createChildren():void {
			super.createChildren();
			var layout:FormLayout = new FormLayout();
			if (!FlexUtilGlobals.getInstance().isMobile) {
				layout.gap = -14;
			}
			layout.paddingLeft = 0;
			layout.paddingRight = 0;
			this.layout = layout;
			percentWidth = 100;
		}
		
		override public function set contentGroup(value:Group):void {
			super.contentGroup = value;
			contentGroup.setStyle("top", 0);
			contentGroup.setStyle("bottom", 0);	
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {			
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			if (visualChildrenController != null && shouldRefreshVisualChildren) {
				// i.e. used in stand alone mode
				visualChildrenController.refreshVisualChildrenDiagramOrStandAlone(null, typeDescriptorRegistry, this, data,
					propertiesHelper.getPropertyEntries(context, typeDescriptorRegistry, data));
				// this variable is managed by the controller only for the "normal" mode; i.e. not stand alone
				_shouldRefreshVisualChildren = false;
				if (shouldSetFocus) {
					for (var i:int = 0; i < numElements; i++) {
						var child:IVisualElement = IVisualElement(getElementAt(i));
						if (child is PropertyEntryRenderer && !PropertyEntry(PropertyEntryRenderer(child).data).descriptor.isReadOnlyDependingOnMode(Utils.getPropertySafe(context, FlexUtilConstants.PROPERTIES_CONTEXT_IS_CREATE_MODE))) {
							focusManager.setFocus(IFocusManagerComponent(PropertyEntryRenderer(child).editor));
							break;
						}
					}
					
					shouldSetFocus = false;
				}
			}
		}
		
		public function createQuickPropertiesForm(okFunction:Function, typeDescriptorRegistry:TypeDescriptorRegistry, model:Object, propertyDescriptors:IList, 
				groupDescriptors:IList = null, wrapper:IViewContent = null, propertyCommitController:PropertyCommitController = null, 
				propertiesHelper:PropertiesHelper = null):IViewContent {
			if (propertyCommitController == null) {
				propertyCommitController = new PropertyCommitController();
			}
			if (propertiesHelper == null) {
				propertiesHelper = new PropertiesHelper();
			}
			propertiesHelper.propertyDescriptors = propertyDescriptors;
			propertiesHelper.groupDescriptors = groupDescriptors;
				
			visualChildrenController = new StandAloneSequentialLayoutVisualChildrenController(0, null, new PropertyEntryRendererController(propertyCommitController));
			this.typeDescriptorRegistry = typeDescriptorRegistry;
			this.propertiesHelper = propertiesHelper;

			if (wrapper == null) {
				wrapper = new BasicViewContent();
			}
			if (wrapper is BasicViewContent) {
				BasicViewContent(wrapper).okFunction = okFunction;
				BasicViewContent(wrapper).closeOnOk = true;
			}
			IVisualElementContainer(wrapper).addElement(this);
			data = model;
			return wrapper;
		}
		
	}
}