package org.flowerplatform.flexutil.properties {
	import flash.events.Event;
	import flash.events.FocusEvent;
	
	import mx.core.IDataRenderer;
	import mx.core.UIComponent;
	import mx.events.FlexEvent;
	import mx.events.PropertyChangeEvent;
	import mx.managers.IFocusManagerComponent;
	
	import spark.components.FormItem;
	
	import org.flowerplatform.flexutil.FlexUtilConstants;
	import org.flowerplatform.flexutil.controller.GenericDescriptor;
	import org.flowerplatform.flexutil.properties.editor.StringPropertyEditor;
	
	/**
	 * Renderer for a "normal" property (i.e. not a group), that has a label and an editor.
	 * 
	 * @author Cristian Spiescu
	 */
	public class PropertyEntryRenderer extends FormItem implements IDataRenderer, IPropertyCommitControllerAware {

		protected var _data:PropertyEntry;
		
		protected var editor:UIComponent = null;
		
		private var _propertyCommitController:PropertyCommitController;

		override protected function createChildren():void {
			super.createChildren();
			percentWidth = 100;
			UIComponent(labelDisplay).setStyle("fontWeight", "normal");
			UIComponent(labelDisplay).setStyle("fontSize", 9);
			UIComponent(labelDisplay).setStyle("fontFamily", "Arial");
			UIComponent(labelDisplay).setStyle("left", 0);
			UIComponent(contentGroup).setStyle("right", 0);	
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
			
			var clazz:Class = getPropertyEditorClass(_data.descriptor.type);
			if (editor == null || Object(editor).constructor != clazz) {
				// i.e. not same property editor
				if (editor != null) {
					editor.removeEventListener(FlexEvent.VALUE_COMMIT, commitValueHandler);
					if (editor is IFocusManagerComponent) {
						editor.removeEventListener(FocusEvent.FOCUS_OUT, commitValueHandler);
					}
					removeElement(editor);
				}
				editor = UIComponent(new clazz());
				editor.percentHeight = 100;
				editor.percentWidth = 100;
				editor.addEventListener(FlexEvent.VALUE_COMMIT, commitValueHandler);
				if (editor is IFocusManagerComponent) {
					editor.addEventListener(FocusEvent.FOCUS_OUT, commitValueHandler);
				}
				addElement(editor);
			}
			
			IDataRenderer(editor).data = _data.value;
		}
		
		protected function modelChangedHandler(event:PropertyChangeEvent):void {
			if (event.property == _data.descriptor.name) {
				_data.value = event.newValue;
				IDataRenderer(editor).data = _data.value;
			}
		}
		
		protected function commitValueHandler(event:Event):void {
			if (_data.value == IDataRenderer(editor).data) {
				return;
			}
			_data.value = IDataRenderer(editor).data;
			if (propertyCommitController != null) {
				propertyCommitController.commitProperty(_data);
			}
		}
		
		protected function getPropertyEditorClass(type:String):Class {
			var descriptor:GenericDescriptor = GenericDescriptor(_data.typeDescriptorRegistry.getExpectedTypeDescriptor(FlexUtilConstants.NOTYPE_PROPERTY_EDITORS)
				.getSingleController(_data.descriptor.type, null));
			if (descriptor != null) {
				return Class(descriptor.value);
			} else {
				return StringPropertyEditor;
			}
		}
	}
}