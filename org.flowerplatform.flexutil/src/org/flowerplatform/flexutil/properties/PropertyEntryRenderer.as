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
	import flash.events.Event;
	import flash.events.FocusEvent;
	import flash.events.MouseEvent;
	
	import mx.core.IDataRenderer;
	import mx.core.UIComponent;
	import mx.events.PropertyChangeEvent;
	import mx.managers.IFocusManagerComponent;
	
	import spark.components.Button;
	import spark.components.FormItem;
	import spark.layouts.HorizontalLayout;
	
	import org.flowerplatform.flexutil.FlexUtilAssets;
	import org.flowerplatform.flexutil.FlexUtilConstants;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.controller.GenericDescriptor;
	import org.flowerplatform.flexutil.flexdiagram.IRendererControllerAware;
	import org.flowerplatform.flexutil.flexdiagram.RendererController;
	import org.flowerplatform.flexutil.properties.editor.IPropertyEditor;
	import org.flowerplatform.flexutil.properties.editor.StringPropertyEditor;
	
	/**
	 * Renderer for a "normal" property (i.e. not a group), that has a label and an editor.
	 * 
	 * @author Cristian Spiescu
	 */
	public class PropertyEntryRenderer extends FormItem implements IDataRenderer, IPropertyCommitControllerAware, IRendererControllerAware {

		protected var _data:PropertyEntry;
		
		public var editor:UIComponent = null;
		
		protected var removePropertyButton:Button = null;
		
		private var _propertyCommitController:PropertyCommitController;
		
		protected  var _rendererController:PropertyEntryRendererController;

		override protected function createChildren():void {
			super.createChildren();
			percentWidth = 100;
			UIComponent(labelDisplay).setStyle("fontWeight", "normal");
			UIComponent(labelDisplay).setStyle("left", 0);
			contentGroup.setStyle("right", 0);	
			contentGroup.layout = new HorizontalLayout();
		}
		
		public function get propertyCommitController():PropertyCommitController {
			return _propertyCommitController;
		}

		public function set propertyCommitController(value:PropertyCommitController):void {
			_propertyCommitController = value;
		}

		public function get data():Object {
			return _data;
		}
		
		public function set rendererController(value:RendererController):void {
			_rendererController = PropertyEntryRendererController(value);			
		}
		
		public function set data(value:Object):void {
			if (_data == value) {
				return;
			}
			if (_data != null && _data.eventDispatcher != null) {
				_data.eventDispatcher.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
			}
			
			_data = PropertyEntry(value);
			if (value == null) {
				// don't change label and editor, because this may be recycled; so don't waste time
				return;
			}

			if (_data.eventDispatcher != null) {
				_data.eventDispatcher.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
			}
			
			label = _data.descriptor.label != null ? _data.descriptor.label : _data.descriptor.name;
			var clazz:Class = getPropertyEditorClass(_data.descriptor.type, Utils.getClassNameForObject(_data.value, true));
			if (editor == null || Object(editor).constructor != clazz) {
				// i.e. not same property editor
				if (editor != null) {
					editor.removeEventListener(FlexUtilConstants.EVENT_COMMIT_PROPERTY, commitValueHandler);
					if (editor is IFocusManagerComponent) {
						editor.removeEventListener(FocusEvent.FOCUS_OUT, commitValueHandler);
					}
					removeComponents();
				}
				editor = UIComponent(new clazz());
				editor.percentHeight = 100;
				editor.percentWidth = 100;
				editor.addEventListener(FlexUtilConstants.EVENT_COMMIT_PROPERTY, commitValueHandler);
				if (editor is IFocusManagerComponent) {
					editor.addEventListener(FocusEvent.FOCUS_OUT, commitValueHandler);
				}
				var insertAt:int = numElements;
				if (removePropertyButton != null) {
					insertAt--;
				}
				addComponents(insertAt);
			}
			
			if (Utils.getPropertySafe(_data.context, FlexUtilConstants.PROPERTIES_CONTEXT_SHOW_REMOVE_PROPERTY)) {
				if (removePropertyButton == null) {
					removePropertyButton = new Button();
					removePropertyButton.focusEnabled = false;
					removePropertyButton.setStyle("icon", _rendererController.removePropertyIcon);
					if (_rendererController.removePropertyIcon == null) {
						removePropertyButton.label = FlexUtilAssets.INSTANCE.getMessage("properties.removeProperty");
					} else {
						// wierd behavior: minWidth not working if < about 70 px; so we set here the actual size
						removePropertyButton.width = 20;
						removePropertyButton.toolTip = FlexUtilAssets.INSTANCE.getMessage("properties.removeProperty");
					}
					removePropertyButton.addEventListener(MouseEvent.CLICK, removePropertyClickHandler);
					addElement(removePropertyButton);
				}
			} else {
				if (removePropertyButton != null) {
					removeElement(removePropertyButton);
					removePropertyButton.removeEventListener(MouseEvent.CLICK, removePropertyClickHandler);
					removePropertyButton = null;
				}
			}
			
			IPropertyEditor(editor).propertyEntry = _data;
		}
		
		protected function removeComponents():void {
			removeElement(editor);
		}
		
		/**
		 * Should return the index at which on overridding method may add additional children.
		 */
		protected function addComponents(insertIndex:int):int {
			addElementAt(editor, insertIndex);
			return insertIndex++;
		}
		
		protected function removePropertyClickHandler(event:Event):void {
			if (propertyCommitController != null) {
				propertyCommitController.unsetProperty(_data);
			}			
		}
		
		protected function modelChangedHandler(event:PropertyChangeEvent):void {
			if (event.property == _data.descriptor.name) {
				_data.value = event.newValue;
				IPropertyEditor(editor).propertyEntry = _data;
			}
		}
		
		protected function commitValueHandler(event:Event):void {
			if (_data == null || _data.descriptor.isReadOnlyDependingOnMode(Utils.getPropertySafe(_data.context, FlexUtilConstants.PROPERTIES_CONTEXT_IS_CREATE_MODE)) || _data.value == IPropertyEditor(editor).valueToCommit) {
				// in theory, _data cannot be null here; but I discovered a NPE in the line below; not reproductible
				// and I think maybe the event is somehow delayed, and meanwhile the component has been disposed? 
				return;
			}
			_data.value = IPropertyEditor(editor).valueToCommit;
			if (propertyCommitController != null) {
				propertyCommitController.commitProperty(_data);
			}
		}
		
		protected function getPropertyEditorClass(type:String, dataType:String):Class {
			var descriptor:GenericDescriptor = GenericDescriptor(_data.typeDescriptorRegistry.getExpectedTypeDescriptor(FlexUtilConstants.NOTYPE_PROPERTY_EDITORS)
				.getSingleController(_data.descriptor.type, null));
			if (descriptor == null) {
				// none found; try by looking by data type (e.g. in the case of properties without descriptor)
				descriptor = GenericDescriptor(_data.typeDescriptorRegistry.getExpectedTypeDescriptor(FlexUtilConstants.NOTYPE_CLASSES_TO_PROPERTY_EDITORS)
					.getSingleController(dataType, null));
			}
			if (descriptor != null) {
				return Class(descriptor.value);
			} else {
				return StringPropertyEditor;
			}
		}
	}
}