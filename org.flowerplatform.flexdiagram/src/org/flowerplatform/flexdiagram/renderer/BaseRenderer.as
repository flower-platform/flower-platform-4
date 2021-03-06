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
package org.flowerplatform.flexdiagram.renderer {
	import flash.display.GradientType;
	import flash.events.Event;
	import flash.events.IEventDispatcher;
	import flash.events.MouseEvent;
	import flash.geom.Matrix;
	import flash.system.Capabilities;
	
	import mx.collections.IList;
	import mx.core.DPIClassification;
	import mx.core.FlexGlobals;
	import mx.core.IVisualElement;
	import mx.core.UIComponent;
	import mx.events.CollectionEvent;
	import mx.events.PropertyChangeEvent;
	
	import spark.components.DataGroup;
	import spark.components.DataRenderer;
	import spark.components.Group;
	import spark.components.HGroup;
	import spark.components.IItemRenderer;
	import spark.components.RichText;
	import spark.components.supportClasses.InteractionState;
	import spark.components.supportClasses.InteractionStateDetector;
	import spark.layouts.HorizontalLayout;
	import spark.layouts.VerticalLayout;
	import spark.primitives.BitmapImage;
	import spark.primitives.Graphic;
	
	import flashx.textLayout.conversion.TextConverter;
	import flashx.textLayout.elements.BreakElement;
	import flashx.textLayout.elements.ParagraphElement;
	import flashx.textLayout.elements.SpanElement;
	import flashx.textLayout.elements.TextFlow;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.FlexDiagramConstants;
	import org.flowerplatform.flexdiagram.IDiagramShellContextAware;
	import org.flowerplatform.flexdiagram.mindmap.IAbstractMindMapModelRenderer;
	import org.flowerplatform.flexdiagram.ui.CalloutToolTip;
	import org.flowerplatform.flexutil.FlexUtilAssets;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	import org.flowerplatform.flexutil.controller.ValuesProvider;
	import org.flowerplatform.flexutil.flexdiagram.VisualChildrenController;
	import org.flowerplatform.flexutil.focusable_component.FocusableRichText;
	import org.flowerplatform.flexutil.list.EmptyList;
	import org.flowerplatform.flexutil.properties.PropertyEntry;

	/**
	 * Base renderer for diagram elements. It was initially used for mind map nodes, but it has
	 * been generalized. Now <code>MindMapNodeRenderer</code> is used for mind map nodes, and this class
	 * can be used, e.g. for 2nd level children of a class diagam (e.g. attributes).
	 * 
	 * <p>
	 * To see visual properties that are supported take a look at the constants around: 
	 * <code>FlexDiagramConstants.BASE_RENDERER_FONT_FAMILY</code>. Changing <code>canHaveChildren</code>
	 * does a layout modification, by adding an additional group, and the component behaves like a 
	 * 
	 * <p>
	 * This renderer can be used in a diagram, or stand alone (e.g. as a renderer in a list). For the
	 * second case, <code>typeNodeRegistry</code> should be provided at construction (which can be
	 * provided by the <code>IFactory</code> instance that creates the renderer).
	 * 
	 * <p>
	 * If the renderer is used within a diagram, it delegates to a <code>VisualChildrenController</code>
	 * if such a controller is registered.
	 * 
	 * <p>
	 * This class can be subclassed if needed. Although, for the most common use cases it can be used
	 * "as-is", because logic for property access is delegated to controllers, via a <code>ValuesProvider</code>
	 * controller, which is retrieved using <code>featureForValuesProvider</code>.
	 * 
	 * @author Cristian Spiescu
	 */
	public class BaseRenderer extends DataRenderer implements IDiagramShellContextAware, IItemRenderer, IAbstractMindMapModelRenderer, IVisualChildrenRefreshable {
		
		/**************************************************************************
		 * Constants.
		 *************************************************************************/
		protected static const CIRCLE_RADIUS:int = 3;
		
		protected static const BACKGROUND_COLOR_DEFAULT:uint = 0xFFFFFFFF;
		
		protected static const TEXT_COLOR_DEFAULT:uint = 0x000000;
		
		protected static const FONT_FAMILY_DEFAULT:String = null;
		
		protected static const FONT_SIZE_DEFAULT:Number = 9;
		
		protected static const PADDING:int = 2;
		
		/**************************************************************************
		 * Attributes.
		 *************************************************************************/
		
		// graphical components 
		protected var _label:FocusableRichText;
		
		protected var _noteIconContainer:Graphic;
		
		protected var detailsLabel:RichText;
		
		protected var propertiesGroup:Group;
		
		protected var propertiesNames:RichText;
		
		protected var propertiesValues:RichText;
		
		protected var iconsAndLabelArea:Group;
		
		protected var noteToolTip:CalloutToolTip;
		
		// other attributes
		/**
		 * Inspired from the Freeplane renderer, that increases the font a little bit.
		 */
		public var fontSizeCorrection:Number = (Capabilities.screenDPI == 72 ? 96 : Capabilities.screenDPI) / 72;
		
		/**
		 * @see Class doc.
		 */
		public var featureForValuesProvider:String;
		
		/**
		 * @see Class doc.
		 */
		public var typeDescriptorRegistry:TypeDescriptorRegistry;
		
		public var resetPropertiesOnDataNull:Boolean = false;
		
		protected var _backgroundColor:uint = BACKGROUND_COLOR_DEFAULT;
		
		protected var _context:DiagramShellContext;
		
		protected var _icons:IList;
		
		protected var _maxWidthAdvanced:Number;
		
		protected var _noteText:String;
		
		protected var _canHaveChildren:Boolean;
		
		protected var _shouldRefreshVisualChildren:Boolean;
		
		protected var visualChildrenController:VisualChildrenController;
		
		/**************************************************************************
		 * Graphic properties supported by this renderer.
		 *************************************************************************/
		public function set text(value:String):void {
			if (value != null) {
				_label.textFlow = Utils.importTextFlowFromHtmlOrPlainText(value);
			} else {
				_label.textFlow = null;
			}
		}
		
		public function set fontFamily(value:String):void {
			_label.setStyle("fontFamily", Utils.getSupportedFontFamily(value));
		}
		
		public function set fontSize(value:Number):void {
			_label.setStyle("fontSize", (fontSizeCorrection * value));
		}
		
		public function set fontBold(value:Boolean):void {
			_label.setStyle("fontWeight", value == true ? "bold" : "normal");
		}
		
		public function set fontItalic(value:Boolean):void {
			_label.setStyle("fontStyle", value == true ? "italic" : "normal");
		}
		
		public function set textColor(value:uint):void {
			_label.setStyle("color", value);
		}
		
		public function set backgroundColor(value:uint):void {
			_backgroundColor = value;
			invalidateDisplayList();
		}
		
		public function set icons(value:IList):void {		
			if (value == _icons) {
				return;
			}
			if (_icons != null && _icons != EmptyList.INSTANCE) {
				_icons.removeEventListener(CollectionEvent.COLLECTION_CHANGE, handleIconsChanged);
			}
			_icons = value;
			if (_icons != null) {
				_icons.addEventListener(CollectionEvent.COLLECTION_CHANGE, handleIconsChanged);				
			} else {
				_icons = EmptyList.INSTANCE;
			}
			handleIconsChanged(null);
		}
		
		public function set maxWidthAdvanced(value:Number):void {
			_maxWidthAdvanced = value;
			if (isNaN(_maxWidthAdvanced)) {
				// e.g. recycling a renderer that had maxW towards a model that doesn't have maxW
				_label.maxWidth = NaN;
			}
			invalidateSize();
		}

		protected override function measure():void {
			super.measure();
			if (isNaN(_maxWidthAdvanced) || iconsAndLabelArea == null || _label == null) {
				return;
			}
			// inspired from the way this seems to work in Freeplane: maxWidth is for icons + label;
			// but if there are a lot of icons, then they have priority. In this case, the maxWidth condition
			// won't be met any more. The label is being shrank as much as possible to try to meet the condition
			// as close as possible. However, we impose a threshold of 20 px, under which the label cannot be shrank
			// any more
			var widthWithoutLabel:Number = iconsAndLabelArea.measuredWidth - _label.measuredWidth;
			_label.maxWidth = Math.max(_maxWidthAdvanced - widthWithoutLabel, 20);
		}
		
		public function set noteText(value:String):void {	
			var indexOfNoteIcon:int, _noteIcon:BitmapImage;
			
			if (value != null) {
				_noteText = value;
			} else {
				_noteText = null;
			}
			
			if (_noteText != null && _noteText != "") {
				// should have an icon ...
				if (_noteIconContainer == null) {
					// ... but it doesn't => create it
					_noteIcon = new BitmapImage();
					_noteIcon.source = FlexUtilAssets.noteIcon;
				
					_noteIconContainer = new Graphic();
					_noteIconContainer.addElement(_noteIcon);
					
					iconsAndLabelArea.addElementAt(_noteIconContainer, 0);
					
					_noteIconContainer.addEventListener(MouseEvent.MOUSE_OVER, noteIconRollOverHandler);
					// useful for tablet, where we don't have mouse
					_noteIconContainer.addEventListener(MouseEvent.CLICK, noteIconRollOverHandler);
					_noteIconContainer.addEventListener(MouseEvent.MOUSE_OUT, noteIconOutHandler);
				}
				// else: it has an icon => nothing to do
			} else {
				// shouldn't have an icon ...
				if (_noteIconContainer != null) {
					// ... and it has => remove it
					iconsAndLabelArea.removeElement(_noteIconContainer);
					_noteIconContainer.removeEventListener(MouseEvent.MOUSE_OVER, noteIconRollOverHandler);
					_noteIconContainer.removeEventListener(MouseEvent.CLICK, noteIconRollOverHandler);
					_noteIconContainer.removeEventListener(MouseEvent.MOUSE_OUT, noteIconOutHandler);
					_noteIconContainer = null;
				}
				// else: it hasn't got an icon => nothing to do
			}
			
		}
		
		public function set detailsText(value:String):void {
			if (value != null && value != "") {
				if (detailsLabel == null) {
					canHaveChildren = true;
					detailsLabel = new RichText();
					insertNewChild(detailsLabel);
				}
				detailsLabel.textFlow = TextConverter.importToFlow(value , Utils.isHTMLText(value) ? TextConverter.TEXT_FIELD_HTML_FORMAT : TextConverter.PLAIN_TEXT_FORMAT);	
			} else {
				if (detailsLabel != null) {
					removeElement(detailsLabel);
					detailsLabel = null;
					if (!hasChildren()) {
						canHaveChildren = false;
					}
				}
			}			
		}
		
		public function set propertyEntries(entries:IList):void {
			if (entries != null) {
				if (propertiesGroup == null) {
					canHaveChildren = true;
					propertiesGroup = new HGroup();
					propertiesGroup.percentWidth = 100;
					insertNewChild(propertiesGroup);
					propertiesNames = new RichText();
					propertiesGroup.addElement(propertiesNames);
					propertiesValues = new RichText();
					propertiesValues.percentWidth = 100;
					propertiesGroup.addElement(propertiesValues);
				}
				var namesTextFlow:TextFlow = new TextFlow();
				var names:ParagraphElement = new ParagraphElement();
				namesTextFlow.addChild(names);
				
				var valuesTextFlow:TextFlow = new TextFlow();
				var values:ParagraphElement = new ParagraphElement();
				valuesTextFlow.addChild(values);
				
				for (var i:int = 0; i < entries.length; i++) {
					var entry:PropertyEntry = PropertyEntry(entries.getItemAt(i));
					var name:SpanElement = new SpanElement();
					var value:SpanElement = new SpanElement();
					if (entry.isGroup) {
						name.fontWeight = "bold";
						name.text = " " + entry.descriptor.name;
					} else {
						value.text = String(entry.value);
						name.text = "  " + entry.descriptor.name;
					}
					names.addChild(name);
					values.addChild(value);
					
					
					if (i < entries.length - 1) {
						names.addChild(new BreakElement());
						values.addChild(new BreakElement());
					}
				}
				propertiesNames.textFlow = namesTextFlow;
				propertiesValues.textFlow = valuesTextFlow;
			} else {
				if (propertiesGroup != null) {
					removeElement(propertiesGroup);
					propertiesGroup = null;
					propertiesNames = null;
					propertiesValues = null;
					if (!hasChildren()) {
						canHaveChildren = false;
					}
				}
			}
		}
		
		protected function getLogicalIndexForChild(element:IVisualElement):int {
			if (detailsLabel == element) {
				return 0;
			} else if (propertiesGroup == element) {
				return 1;
			} else {
				return -1;
			}
		}
		
		protected function hasChildren():Boolean {
			return detailsLabel != null || propertiesGroup != null;
		}
		
		protected function insertNewChild(newlyAddedElement:IVisualElement):void {
			var elementIndex:int = getLogicalIndexForChild(newlyAddedElement);
			for (var i:int = 0; i < numElements; i++) {
				if (elementIndex <= getLogicalIndexForChild(getElementAt(i))) {
					// e.g. look for element 3; currentResult = 4
					// equality shouldn't happen, as it's newly added element
					break;
				}
			}
			// not found, i.e. all elements have lower index; e.g. look for element 3, found 0 and 2
			// i.e. numElements; i.e. insert at the end
			addElementAt(newlyAddedElement, i);
		}

		/**************************************************************************
		 * Other functions.
		 *************************************************************************/
		
		public function BaseRenderer() {
			mouseEnabledWhereTransparent = true;
			
			if (!FlexUtilGlobals.getInstance().isMobile) {
				minHeight = 22;
				minWidth = 10;
			} else {
				switch (FlexGlobals.topLevelApplication.applicationDPI) {
					case DPIClassification.DPI_320:	{
						minHeight = 88;
						break;
					}
					case DPIClassification.DPI_240:	{
						minHeight = 66;
						break;
					}
					default: {
						// default PPI160
						minHeight = 44;
						break;
					}
				}
			}
			interactionStateDetector = new InteractionStateDetector(this);
			interactionStateDetector.addEventListener(Event.CHANGE, interactionStateDetector_changeHandler);
		}
		
		public function getLabelDisplay():RichText {
			return _label;
		}
		
		public function get diagramShellContext():DiagramShellContext {			
			return _context;
		}
		
		public function set diagramShellContext(value:DiagramShellContext):void {
			this._context = value;
			if (typeDescriptorRegistry == null) {
				typeDescriptorRegistry = value.diagramShell.registry;
			}
		}
		
		public function get shouldRefreshVisualChildren():Boolean {
			return _shouldRefreshVisualChildren;
		}
		
		public function set shouldRefreshVisualChildren(value:Boolean):void {
			_shouldRefreshVisualChildren = value;
		}
		
		override public function set data(value:Object):void {
			if (data != null) {
				endModelListen();
				visualChildrenController = null;
			}
			super.data = value;
			if (data != null) {
				beginModelListen();
				if (diagramShellContext != null) {
					visualChildrenController = VisualChildrenController(diagramShellContext.diagramShell.registry.getExpectedTypeDescriptor(					
						diagramShellContext.diagramShell.registry.typeProvider.getType(value)).getSingleController(FlexDiagramConstants.VISUAL_CHILDREN_CONTROLLER, value));
					// if not null => this element has children
				}
			} else if (resetPropertiesOnDataNull) {
				modelChangedHandler(null);
			}
		}
		
		protected function beginModelListen():void {
			data.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
			var actualObject:IEventDispatcher = getRequiredValuesProvider().getActualObject(IEventDispatcher(data));
			if (actualObject != data) {
				actualObject.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
			}
			modelChangedHandler(null);
		}
		
		protected function endModelListen():void {
			data.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
			var actualObject:IEventDispatcher = getRequiredValuesProvider().getActualObject(IEventDispatcher(data));
			if (actualObject != data) {
				actualObject.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
			}
		}
		
		protected function getRequiredValuesProvider():ValuesProvider {
			if (featureForValuesProvider == null) {
				throw new Error("'featureForValuesProvider' should be specified, so that we can get the corresponding 'ValuesProvider'");
			}
			if (typeDescriptorRegistry == null) {
				throw new Error("'typeDescriptorRegistry' should be not null, so that we can get the corresponding 'ValuesProvider'");
			}
			var valuesProvider:ValuesProvider = ValuesProvider(typeDescriptorRegistry.getExpectedTypeDescriptor(typeDescriptorRegistry.typeProvider.getType(data)).getSingleController(featureForValuesProvider, data));
			if (valuesProvider == null) {
				throw new Error("'ValuesProvider' was not found for featureForValuesProvider = " + featureForValuesProvider);
			}
			return valuesProvider;
		}
		
		/**
		 * Invoked when <code>data</code> is changed. If the 'actual' object (cf. <code>ValuesProvider</code>) is different than <code>data</code>,
		 * then that object is listened for changes too; i.e. this method is invoked also when it changes.
		 */
		protected function modelChangedHandler(event:PropertyChangeEvent):void {
			var valuesProvider:ValuesProvider = null;
			if (data != null) {
				valuesProvider = getRequiredValuesProvider();
			}
			setFieldIfNeeded(valuesProvider, typeDescriptorRegistry, event, "fontFamily", FlexDiagramConstants.BASE_RENDERER_FONT_FAMILY, FONT_FAMILY_DEFAULT);
			setFieldIfNeeded(valuesProvider, typeDescriptorRegistry, event, "fontSize", FlexDiagramConstants.BASE_RENDERER_FONT_SIZE, FONT_SIZE_DEFAULT);
			setFieldIfNeeded(valuesProvider, typeDescriptorRegistry, event, "fontBold", FlexDiagramConstants.BASE_RENDERER_FONT_BOLD, false);
			setFieldIfNeeded(valuesProvider, typeDescriptorRegistry, event, "fontItalic", FlexDiagramConstants.BASE_RENDERER_FONT_ITALIC, false);
			setFieldIfNeeded(valuesProvider, typeDescriptorRegistry, event, "text", FlexDiagramConstants.BASE_RENDERER_TEXT, "");
			setFieldIfNeeded(valuesProvider, typeDescriptorRegistry, event, "textColor", FlexDiagramConstants.BASE_RENDERER_TEXT_COLOR, TEXT_COLOR_DEFAULT);
			setFieldIfNeeded(valuesProvider, typeDescriptorRegistry, event, "backgroundColor", FlexDiagramConstants.BASE_RENDERER_BACKGROUND_COLOR, BACKGROUND_COLOR_DEFAULT);
			setFieldIfNeeded(valuesProvider, typeDescriptorRegistry, event, "icons", FlexDiagramConstants.BASE_RENDERER_ICONS, null);
			setFieldIfNeeded(valuesProvider, typeDescriptorRegistry, event, "minWidth", FlexDiagramConstants.BASE_RENDERER_MIN_WIDTH, NaN);
			setFieldIfNeeded(valuesProvider, typeDescriptorRegistry, event, "maxWidthAdvanced", FlexDiagramConstants.BASE_RENDERER_MAX_WIDTH, 600);
			setFieldIfNeeded(valuesProvider, typeDescriptorRegistry, event, "noteText", FlexDiagramConstants.BASE_RENDERER_NOTE, "");
			setFieldIfNeeded(valuesProvider, typeDescriptorRegistry, event, "detailsText", FlexDiagramConstants.BASE_RENDERER_DETAILS_TEXT, null);
		}
		
		protected function setFieldIfNeeded(valuesProvider:ValuesProvider, registry:TypeDescriptorRegistry, event:PropertyChangeEvent, field:String, featureForField:String, defaultValue:Object):void {
			if (data == null) {
				this[field] = defaultValue;
			} else if (event == null || event.property == valuesProvider.getPropertyName(registry, IEventDispatcher(data), featureForField)) {
				var value:Object = valuesProvider.getValue(registry, IEventDispatcher(data), featureForField);
				if (value != null) {
					this[field] = value;
				} else {
					// probably happens only if event == null
					this[field] = defaultValue;
				}
			} 			
		}
		
		protected function handleIconsChanged(event:CollectionEvent):void {			
			var iconDisplay:BitmapImage;
			var offsetBecauseOfNoteIcon:int = 0;
			
			if (_noteIconContainer != null) {
				offsetBecauseOfNoteIcon = getElementIndex(_noteIconContainer) + 1;
			}
			
			// loop over the icons list; and compare with the actual image components; the purpose: try to reuse the components
			// there are 3 cases: the size theoretical list == the size of the actual list; or < or > 
			
			for (var i:int = 0; i < _icons.length; i++) {
				var candidate:IVisualElement = iconsAndLabelArea.getElementAt(i + offsetBecauseOfNoteIcon);
				if (candidate is BitmapImage) {
					// a BitmapImage that will be reused
					iconDisplay = BitmapImage(candidate);
				} else {
					// we encountered something else; probably the Label; so we need to create a new BitmapImage
					iconDisplay = new BitmapImage();
					iconDisplay.contentLoader = FlexUtilGlobals.getInstance().imageContentCache;
					iconDisplay.verticalAlign = "middle";
					iconDisplay.depth = UIComponent(this).depth;
					iconsAndLabelArea.addElementAt(iconDisplay, i + offsetBecauseOfNoteIcon);
				}
				iconDisplay.source = FlexUtilGlobals.getInstance().adjustImageBeforeDisplaying(_icons.getItemAt(i));
			}
			
			// now delete all images that exist visually, but that are not needed; e.g. if we have 4 visual icons, but the list contains only 3 => we would remove(3)
	
			while (iconsAndLabelArea.getElementAt(_icons.length + offsetBecauseOfNoteIcon) is BitmapImage) {
				iconsAndLabelArea.removeElementAt(_icons.length + offsetBecauseOfNoteIcon);
			}
		}
		
		private function noteIconRollOverHandler(event:MouseEvent):void {
			noteToolTip = new CalloutToolTip();
			noteToolTip.text = _noteText;
			noteToolTip.open(_noteIconContainer, false);
		}
		
		private function noteIconOutHandler(event:MouseEvent):void {
			noteToolTip.close();
		}
		
		public function get canHaveChildren():Boolean {
			return _canHaveChildren;
		}
		
		public function set canHaveChildren(value:Boolean):void {
			if (_canHaveChildren == value) {
				return;
			}
			_canHaveChildren = value;
			if (_label == null) {
				// i.e. createChildren() not invoked yet
				return;
			}
			
			var elementsToMove:Array = new Array();
			for (var i:int = 0; i < iconsAndLabelArea.numElements; i++) {
				elementsToMove.push(iconsAndLabelArea.getElementAt(i));
			}
			createIconsAndLabelArea();
			for each (var element:IVisualElement in elementsToMove) {
				iconsAndLabelArea.addElement(element);
			}
		}
		
		protected function createIconsAndLabelArea():void {
			if (canHaveChildren) {
				this.layout = new VerticalLayout();
				VerticalLayout(this.layout).gap = 1;
				iconsAndLabelArea = new Group();
				addElement(iconsAndLabelArea);
			} else {
				if (iconsAndLabelArea != null && iconsAndLabelArea != this) {
					// i.e. had children
					removeAllElements();
				}
				iconsAndLabelArea = this;
			}
			
			var hLayout:HorizontalLayout = new HorizontalLayout();
			hLayout.gap = PADDING;
			hLayout.paddingBottom = PADDING;
			hLayout.paddingTop = PADDING;
			hLayout.paddingLeft = PADDING;
			hLayout.paddingRight = PADDING;
			hLayout.verticalAlign = "middle";
			iconsAndLabelArea.layout = hLayout;				
		}
		
		override protected function createChildren():void {
			super.createChildren();
			
			createIconsAndLabelArea();
			
			_label = new FocusableRichText();
			// no need for setting width/height percents. This way, the label "pushes" the container
			// with w=h=100%, there are issues during figure recycling; and it's not correct any way	
			_label.setStyle("verticalAlign" , "middle");
			// I think that the text is vertically aligned; but it seems to be offsetted towards the top;
			// probably because of the "p,g,y". That's why we offset it towards the bottom a little bit
			_label.setStyle("paddingTop", 3);
			_label.right = 0;
			iconsAndLabelArea.addElement(_label);
			
		}
		
		protected function drawCloud(unscaledWidth:Number, unscaledHeight:Number):void {
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {			
			graphics.clear();
			super.updateDisplayList(unscaledWidth, unscaledHeight);

			drawCloud(unscaledWidth, unscaledHeight);
			
			graphics.lineStyle(1, 0x808080);
			graphics.beginFill(_backgroundColor, 1);
			if (diagramShellContext != null) {
				graphics.drawRoundRect(0, 0, unscaledWidth, unscaledHeight, 10, 10);
			} else {
				// if not in diagram, i.e. in a list. Rounded rect is ugly.
				graphics.drawRect(0, 0, unscaledWidth, unscaledHeight);
			}
			graphics.endFill();
			
			drawBackground(unscaledWidth, unscaledHeight);
			
			if (visualChildrenController != null) {
				// i.e. has children
				visualChildrenController.refreshVisualChildren(diagramShellContext, this, data);
			}
		}
		
		/**************************************************************************
		 * Functions used for list selection. Shoud be refactored; e.g. this class should extend ItemRenderer.
		 *************************************************************************/
		
		public var showSelected:Boolean = false;
		
		private var interactionStateDetector:InteractionStateDetector;
		
		private var _dragging:Boolean = false;
		
		public function get dragging():Boolean {
			return _dragging;
		}
		
		public function set dragging(value:Boolean):void {
			_dragging = value;
			
		}
		
		private var _itemIndex:int;
		protected var isLastItem:Boolean = false;
		
		public function get itemIndex():int {
			return _itemIndex;
		}
		
		public function set itemIndex(value:int):void {
			var wasLastItem:Boolean = isLastItem;       
			var dataGroup:DataGroup = parent as DataGroup;
			isLastItem = (dataGroup && (value == dataGroup.numElements - 1));
			
			// if whether or not we are the last item in the last has changed then
			// invalidate our display. note:  even if our new index has not changed,
			// whether or not we're the last item may have so we perform this check 
			// before the value == _itemIndex check below
			if (wasLastItem != isLastItem) 
				invalidateDisplayList();
			
			if (value == _itemIndex)
				return;
			
			_itemIndex = value;
			
			// only invalidateDisplayList() if this causes use to redraw which
			// is only if alternatingItemColors are defined (and technically also
			// only if we are not selected or down, etc..., but we'll ignore those
			// as this will shortcut 95% of the time anyways)
			if (getStyle("alternatingItemColors") !== undefined)
				invalidateDisplayList();
		}
		
		public function get label():String {
			return null;
		}
		
		public function set label(value:String):void {
			
		}
		
		private var _selected:Boolean = false;
		
		public function get selected():Boolean {
			return _selected;
		}
		
		public function set selected(value:Boolean):void {
			if (value == _selected)
				return;
			
			_selected = value; 
			invalidateDisplayList();
		}
		
		private var _showsCaret:Boolean = false;
		
		public function get showsCaret():Boolean {
			return _showsCaret;
		}
		
		public function set showsCaret(value:Boolean):void {
			if (value == _showsCaret)
				return;
			
			_showsCaret = value;
			invalidateDisplayList();
		}
		
		private var _down:Boolean = false;
		
		protected function get down():Boolean {
			return _down;
		}
		
		protected function set down(value:Boolean):void {
			if (value == _down)
				return;
			
			_down = value; 
			invalidateDisplayList();
		}
		
		private var _hovered:Boolean = false;
		
		protected function get hovered():Boolean {
			return _hovered;
		}
		
		protected function set hovered(value:Boolean):void {
			if (value == _hovered)
				return;
			
			_hovered = value; 
			invalidateDisplayList();
		}
		
		protected function drawBackground(unscaledWidth:Number, unscaledHeight:Number):void {
			// figure out backgroundColor
			var backgroundColor:*;
			var downColor:* = getStyle("downColor");
			var drawBackground:Boolean = true;
			var opaqueBackgroundColor:* = undefined;
			
			if (down && downColor !== undefined) {
				backgroundColor = downColor;
			} else if (selected) {
				backgroundColor = getStyle("selectionColor");
			} else if (hovered) {
				backgroundColor = getStyle("rollOverColor");
			} else if (showsCaret) {
				backgroundColor = getStyle("selectionColor");
			} else {
				var alternatingColors:Array;
				var alternatingColorsStyle:Object = getStyle("alternatingItemColors");
				
				if (alternatingColorsStyle)
					alternatingColors = (alternatingColorsStyle is Array) ? (alternatingColorsStyle as Array) : [alternatingColorsStyle];
				
				if (alternatingColors && alternatingColors.length > 0) {
					// translate these colors into uints
					styleManager.getColorNames(alternatingColors);
					
					backgroundColor = alternatingColors[itemIndex % alternatingColors.length];
				} else {
					// don't draw background if it is the contentBackgroundColor. The
					// list skin handles the background drawing for us. 
					drawBackground = false;
				}
				
			} 
			
			// draw backgroundColor
			// the reason why we draw it in the case of drawBackground == 0 is for
			// mouse hit testing purposes
			if (showSelected) {
				graphics.beginFill(backgroundColor, drawBackground ? 1 : 0);
				graphics.lineStyle();
				graphics.drawRect(0, 0, unscaledWidth, unscaledHeight);
				graphics.endFill();
			}
			// Selected and down states have a gradient overlay as well
			// as different separators colors/alphas
			if (selected || down) {
				var colors:Array = [0x000000, 0x000000 ];
				var alphas:Array = [.2, .1];
				var ratios:Array = [0, 255];
				var matrix:Matrix = new Matrix();
				
				// gradient overlay
				matrix.createGradientBox(unscaledWidth, unscaledHeight, Math.PI / 2, 0, 0 );
				graphics.beginGradientFill(GradientType.LINEAR, colors, alphas, ratios, matrix);
				graphics.drawRect(0, 0, unscaledWidth, unscaledHeight);
				graphics.endFill();
			}
		}
		
		private function interactionStateDetector_changeHandler(event:Event):void {
			if (showSelected) {
				down = (interactionStateDetector.state == InteractionState.DOWN);
				hovered = (interactionStateDetector.state == InteractionState.OVER);
			}
		}
		
	}
}