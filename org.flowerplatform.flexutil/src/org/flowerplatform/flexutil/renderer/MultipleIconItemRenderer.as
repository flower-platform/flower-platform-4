package org.flowerplatform.flexutil.renderer {
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.events.SecurityErrorEvent;
	import flash.events.TimerEvent;
	import flash.net.URLRequest;
	import flash.utils.Timer;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.controls.listClasses.*;
	import mx.core.DPIClassification;
	import mx.core.FlexGlobals;
	import mx.core.INavigatorContent;
	import mx.core.mx_internal;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	import mx.events.PropertyChangeEvent;
	import mx.graphics.BitmapFillMode;
	import mx.graphics.BitmapScaleMode;
	import mx.styles.CSSStyleDeclaration;
	import mx.utils.DensityUtil;
	
	import org.flowerplatform.flexutil.FlowerArrayList;
	
	import spark.components.LabelItemRenderer;
	import spark.components.supportClasses.StyleableTextField;
	import spark.core.ContentCache;
	import spark.core.DisplayObjectSharingMode;
	import spark.core.IContentLoader;
	import spark.core.IGraphicElement;
	import spark.core.IGraphicElementContainer;
	import spark.core.ISharedDisplayObject;
	import spark.primitives.BitmapImage;
	import spark.utils.MultiDPIBitmapSource;
	
	use namespace mx_internal;
	
	[Style(name="iconsGap", type="Number", format="Number", inherit="no")]
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class MultipleIconItemRenderer extends LabelItemRenderer implements IGraphicElementContainer {
		
		mx_internal static var _imageCache:ContentCache;
		
		public function MultipleIconItemRenderer() {
			super();
			
			if (_imageCache == null) {
				_imageCache = new ContentCache();
				_imageCache.enableCaching = true;
				_imageCache.maxCacheEntries = 100;
			}
			
			_icons.addEventListener(CollectionEvent.COLLECTION_CHANGE, iconsChangedHandler);
			
			// set default messageDisplay width
			switch (applicationDPI)	{
				case DPIClassification.DPI_320:	{
					oldUnscaledWidth = 640;
					break;
				}
				case DPIClassification.DPI_240: {
					oldUnscaledWidth = 480;
					break;
				}
				default: {
					// default PPI160
					oldUnscaledWidth = 320;
					break;
				}
			}
			setStyle("iconsGap", 2);
		}
		
		//--------------------------------------------------------------------------
		//
		//  Variables
		//
		//--------------------------------------------------------------------------
		
		/**		
		 *  Stores the text of the label component.  This is calculated in 
		 *  commitProperties() based on labelFunction, labelField, and label.
		 * 
		 *  <p>We can't just use labelDisplay.text because it may contain 
		 *  a truncated value.</p>
		 */
		mx_internal var labelText:String = "";
				
		/**		
		 *  Since iconDisplay is a GraphicElement, we have to call its lifecycle methods 
		 *  directly.
		 */
		private var iconNeedsValidateProperties:Boolean = false;
		
		/**		
		 *  Since iconDisplay is a GraphicElement, we have to call its lifecycle methods 
		 *  directly.
		 */
		private var iconNeedsValidateSize:Boolean = false;
		
		/**
		 *  Since iconDisplay is a GraphicElement, we have to call help assign 
		 *  its display object
		 */
		private var iconNeedsDisplayObjectAssignment:Boolean = false;
				
		/**
		 *  The width of the component on the previous layout manager 
		 *  pass.  This gets set in updateDisplayList() and used in measure() on 
		 *  the next layout pass.  This is so our "guessed width" in measure() 
		 *  will be as accurate as possible since messageDisplay is multiline and 
		 *  the messageDisplay height is dependent on the width.
		 * 
		 *  In the constructor, this is actually set based on the DPI.
		 */
		mx_internal var oldUnscaledWidth:Number;
		
		override public function set data(value:Object):void {
			super.data = value;
			
			labelChanged = true;
			iconsChanged = true;
			
			invalidateProperties();
		}
		
		/**
		 *  <p>If <code>labelFunction</code> = <code>labelField</code> = null,
		 *  then use the <code>label</code> property that gets 
		 *  pushed in from the list control. 
		 *  However if <code>labelField</code> is explicitly set to 
		 *  <code>""</code> (the empty string), then no label appears.</p>
		 * 
		 *  @inheritDoc
		 * 
		 *  @see spark.components.IconItemRenderer#labelField
		 *  @see spark.components.IconItemRenderer#labelFunction
		 *  @see spark.components.IItemRenderer#label 
		 */
		override public function set label(value:String):void {
			if (value == label)
				return;
			
			super.label = value;
			
			labelChanged = true;
			invalidateProperties();
		}
	
		//----------------------------------
		//  labelField
		//----------------------------------
		
		private var _labelField:String = null;
		
		private var labelChanged:Boolean; 
		
		/**
		 *  The name of the field in the data provider items to display 
		 *  as the label. 
		 *  The <code>labelFunction</code> property overrides this property.
		 * 
		 *  <p>If <code>labelFunction</code> = <code>labelField</code> = null,
		 *  then use the <code>label</code> property that gets 
		 *  pushed in from the list-based control.  
		 *  However if <code>labelField</code> is explicitly set to 
		 *  <code>""</code> (the empty string),
		 *  then no label appears.</p>
		 * 
		 *  @see spark.components.IconItemRenderer#labelFunction
		 *  @see spark.components.IItemRenderer#label
		 *
		 *  @default null
		 */
		public function get labelField():String {
			return _labelField;
		}
		
		public function set labelField(value:String):void {
			if (value == _labelField)
				return;
			
			_labelField = value;			
			labelChanged = true;
			
			invalidateProperties();
		}
		
		//----------------------------------
		//  labelFunction
		//----------------------------------
		
		private var _labelFunction:Function; 
		
		/**
		 *  A user-supplied function to run on each item to determine its label.  
		 *  The <code>labelFunction</code> property overrides 
		 *  the <code>labelField</code> property.
		 *
		 *  <p>You can supply a <code>labelFunction</code> that finds the 
		 *  appropriate fields and returns a displayable string. The 
		 *  <code>labelFunction</code> is also good for handling formatting and 
		 *  localization.</p>
		 *
		 *  <p>The label function takes a single argument which is the item in 
		 *  the data provider and returns a String.</p>
		 *  <pre>
		 *  myLabelFunction(item:Object):String</pre>
		 * 
		 *  <p>If <code>labelFunction</code> = <code>labelField</code> = null,
		 *  then use the <code>label</code> property that gets 
		 *  pushed in from the list-based control.  
		 *  However if <code>labelField</code> is explicitly set to 
		 *  <code>""</code> (the empty string),
		 *  then no label appears.</p>
		 * 
		 *  @see spark.components.IconItemRenderer#labelFunction
		 *  @see spark.components.IItemRenderer#label
		 *
		 *  @default null
		 */
		public function get labelFunction():Function {
			return _labelFunction;
		}
		
		public function set labelFunction(value:Function):void {
			if (value == _labelFunction)
				return;
			
			_labelFunction = value;			
			labelChanged = true;
			
			invalidateProperties(); 
		}
		
		private var iconsChanged:Boolean;
		
		private var _iconsField:String = null;
				
		/**
		 *  The name of the field in the data provider items to display 
		 *  as the label. 
		 *  The <code>labelFunction</code> property overrides this property.
		 * 
		 *  <p>If <code>labelFunction</code> = <code>labelField</code> = null,
		 *  then use the <code>label</code> property that gets 
		 *  pushed in from the list-based control.  
		 *  However if <code>labelField</code> is explicitly set to 
		 *  <code>""</code> (the empty string),
		 *  then no label appears.</p>
		 * 
		 *  @see spark.components.IconItemRenderer#labelFunction
		 *  @see spark.components.IItemRenderer#label
		 *
		 *  @default null
		 */
		public function get iconsField():String {
			return _iconsField;
		}
		
		public function set iconsField(value:String):void {
			if (value == _iconsField)
				return;
			
			_iconsField = value;
			iconsChanged = true;
			
			invalidateProperties();
		}
		
		
		//----------------------------------
		//  iconsFunction
		//----------------------------------
		
		private var _iconsFunction:Function; 
		
		public function get iconsFunction():Function {
			return _iconsFunction;
		}
		
		public function set iconsFunction(value:Function):void {
			if (value == _iconsFunction)
				return;
			
			_iconsFunction = value;			
			iconsChanged = true;
			
			invalidateDisplayList();
		}
		
		//----------------------------------
		//  iconContentLoader
		//----------------------------------
		
		private var _iconContentLoader:IContentLoader = _imageCache;
				
		/**
		 *  Optional custom image loader, such as an image cache or queue, to
		 *  associate with content loader client.
		 * 
		 *  <p>The default value is a static content cache defined on IconItemRenderer
		 *  that allows up to 100 entries.</p>		
		 */
		public function get iconContentLoader():IContentLoader {
			return _iconContentLoader;
		}
		
		public function set iconContentLoader(value:IContentLoader):void {
			if (value == _iconContentLoader)
				return;
			
			_iconContentLoader = value;
			
			if (iconDisplays && iconDisplays.length > 0) {
				for (var i:int=0; i < iconDisplays.length; i++) {
					var iconDisplay:BitmapImage = BitmapImage(iconDisplays.getItemAt(i));
					iconDisplay.contentLoader = _iconContentLoader;
				}
			}
		}
								
		/**		
		 *  The class to use when instantiating the icon for IconItemRenderer.
		 *  This class must extend spark.primitives.BitmapImage.
		 *  This property was added for Design View so they can set this to a special
		 *  subclass of BitmapImage that knows how to load and resolve resources in Design View.
		 */
		mx_internal var iconDisplayClass:Class = BitmapImage;
		
		protected var iconDisplays:IList;
		
		private var _icons:FlowerArrayList = new FlowerArrayList();
		
		public function get icons():FlowerArrayList {
			return _icons;
		}
		
		public function set icons(value:FlowerArrayList):void {
			if (value == _icons)
				return;
			if (value == null) {
				_icons.removeAll();
			} else {
				var i:int;
				var j:int = 0;
				if (_icons.length > 0 && value.length > 0) {
					while (j < value.length && j < _icons.length) {
						BitmapImage(iconDisplays.getItemAt(j)).source = value.getItemAt(j);
						j++;
					}
				}
				if (j < _icons.length) {
					i = j;
					while (i < iconDisplays.length)  {
						removeIconDisplay(BitmapImage(iconDisplays.getItemAt(i)));
					}
				}
				if (j < value.length) {
					for (i = j; i < value.length; i++) {
						addIconDisplay(value.getItemAt(i));
					}
				}
				_icons = value;
			}
		}
		
		protected function iconsChangedHandler(event:CollectionEvent):void {
			if (event.kind == CollectionEventKind.RESET) {
				// we don't need to react to this, because our overriden list (FlowerArrayList) dispatches
				// a removed event for all children. I.e. this method will be called shortly after, for each
				// element that was previously there
				return;
			}
			var obj:Object = event.items[0];
			if (obj is PropertyChangeEvent) {
				obj = PropertyChangeEvent(obj).source;
			}	
			
			if (event.kind == CollectionEventKind.ADD) {
				addIconDisplay(obj);					
			} else if (event.kind == CollectionEventKind.REMOVE ||  event.kind == CollectionEventKind.REPLACE) {
				var index:Number;
				if (event.kind == CollectionEventKind.REPLACE) {
					obj = PropertyChangeEvent(event.items[0]).oldValue;	
					index = Number(PropertyChangeEvent(event.items[0]).property);
				} else {
					index = event.location;
				}
				removeIconDisplay(BitmapImage(iconDisplays.getItemAt(index)));
			}
			invalidateDisplayList();		
		}
				
		protected function addIconDisplay(icon:Object):void {
			var iconDisplay:BitmapImage = new iconDisplayClass();
			
			iconDisplay.contentLoader = iconContentLoader;
					
			if (!isNaN(iconWidth))
				iconDisplay.explicitWidth = iconWidth;
			if (!isNaN(iconHeight))
				iconDisplay.explicitHeight = iconHeight;
						
			iconDisplay.parentChanged(this);
			iconDisplay.source = icon;
			
			if (iconDisplays == null) {
				iconDisplays = new ArrayList();
			}
			iconDisplays.addItem(iconDisplay);
		}
		
		protected function removeIconDisplay(iconDisplay:BitmapImage):void {
			iconDisplay.parentChanged(null);
			iconDisplays.removeItemAt(iconDisplays.getItemIndex(iconDisplay));
			
			invalidateSize();
		}
	
		//----------------------------------
		//  iconHeight
		//----------------------------------
		
		private var _iconHeight:Number;
		
		public function get iconHeight():Number {
			return _iconHeight;
		}
		
		public function set iconHeight(value:Number):void {
			if (value == _iconHeight)
				return;
			
			_iconHeight = value;
			
			if (iconDisplays && iconDisplays.length > 0) {
				for (var i:int=0; i < iconDisplays.length; i++) {
					var iconDisplay:BitmapImage = BitmapImage(iconDisplays.getItemAt(i));
					iconDisplay.explicitHeight = _iconHeight;;
				}
			}
			
			invalidateSize();
			invalidateDisplayList();
		}
		
		//----------------------------------
		//  iconPlaceholder
		//----------------------------------
		
		private var _iconPlaceholder:Object;
		
		/**
		 *  The icon asset to use while an externally loaded asset is
		 *  being downloaded.
		 * 
		 *  <p>This asset should be an embedded image and not an externally 
		 *  loaded image.</p>		
		 */
		public function get iconPlaceholder():Object {
			return _iconPlaceholder;
		}
		
		public function set iconPlaceholder(value:Object):void {
			if (value == _iconPlaceholder)
				return;
			
			_iconPlaceholder = value;
			
			invalidateProperties();
			
			// clear clearOnLoad if necessary
			if (iconDisplays && iconDisplays.length > 0) {
				for (var i:int=0; i < iconDisplays.length; i++) {
					var iconDisplay:BitmapImage = BitmapImage(iconDisplays.getItemAt(i));
					iconDisplay.clearOnLoad = (iconPlaceholder == null);
				}
			}
		}
				
		//----------------------------------
		//  iconWidth
		//----------------------------------
		
		private var _iconWidth:Number;
		
		/**
		 *  The width of the icon.  If unspecified, the 
		 *  intrinsic width of the image is used.
		 *
		 */
		public function get iconWidth():Number {
			return _iconWidth;
		}
		
		public function set iconWidth(value:Number):void {
			if (value == _iconWidth)
				return;
			
			_iconWidth = value;
			
			if (iconDisplays && iconDisplays.length > 0) {
				for (var i:int=0; i < iconDisplays.length; i++) {
					var iconDisplay:BitmapImage = BitmapImage(iconDisplays.getItemAt(i));
					iconDisplay.explicitWidth = _iconWidth;
				}
			}
			
			invalidateSize();
			invalidateDisplayList();
		}
				
		//--------------------------------------------------------------------------
		//
		//  IGraphicElementContainer
		//
		//--------------------------------------------------------------------------
		
		/**
		 *  Notify the host that an element layer has changed.
		 *
		 *  The <code>IGraphicElementHost</code> must re-evaluates the sequences of 
		 *  graphic elements with shared DisplayObjects and may need to re-assign the 
		 *  DisplayObjects and redraw the sequences as a result. 
		 * 
		 *  Typically the host will perform this in its 
		 *  <code>validateProperties()</code> method.
		 *
		 *  @param element The element that has changed size. 
		
		 */
		public function invalidateGraphicElementSharing(element:IGraphicElement):void {
			// since the only graphic elements are hooked up to drawing with the background,
			// just invalidate display list
			if (element is BitmapImage)
				iconNeedsDisplayObjectAssignment = true;
			invalidateProperties();
		}
		
		/**
		 *  Notify the host component that an element changed and needs to validate properties.
		 * 
		 *  The <code>IGraphicElementHost</code> must call the <code>validateProperties()</code>
		 *  method on the IGraphicElement to give it a chance to commit its properties.
		 * 
		 *  Typically the host will validate the elements' properties in its
		 *  <code>validateProperties()</code> method.
		 *
		 *  @param element The element that has changed.
		 */
		public function invalidateGraphicElementProperties(element:IGraphicElement):void {
			if (element is BitmapImage)
				iconNeedsValidateProperties = true;
			invalidateProperties();
		}
		
		/**
		 *  Notify the host component that an element size has changed.
		 * 
		 *  The <code>IGraphicElementHost</code> must call the <code>validateSize()</code>
		 *  method on the IGraphicElement to give it a chance to validate its size.
		 * 
		 *  Typically the host will validate the elements' size in its
		 *  <code>validateSize()</code> method.
		 *
		 *  @param element The element that has changed size.
		 */
		public function invalidateGraphicElementSize(element:IGraphicElement):void {
			if (element is BitmapImage)
				iconNeedsValidateSize = true;
			invalidateSize();
		}
		
		/**
		 *  Notify the host component that an element has changed and needs to be redrawn.
		 * 
		 *  The <code>IGraphicElementHost</code> must call the <code>validateDisplayList()</code>
		 *  method on the IGraphicElement to give it a chance to redraw.
		 * 
		 *  Typically the host will validate the elements' display lists in its
		 *  <code>validateDisplayList()</code> method.
		 *
		 *  @param element The element that has changed.		
		 */
		public function invalidateGraphicElementDisplayList(element:IGraphicElement):void {
			invalidateDisplayList();
		}
				
		override protected function commitProperties():void {
			super.commitProperties();
						
			// label is created in super.createChildren()
			
			if (iconsChanged) {
				iconsChanged = false;
				if (iconsFunction != null) {
					icons = iconsFunction(data);
				} else if (iconsField) {
					if (iconsField in data && data[iconsField] != null) {
						icons = data[iconsField];
					}
				}
			}
			
			if (labelChanged)
			{
				labelChanged = false;
				
				// if label, try setting that
				if (labelFunction != null)
				{
					labelText = labelFunction(data);
					if (!labelDisplay)
						createLabelDisplay();
					labelDisplay.text = labelText;
				}
				else if (labelField) // if labelField is not null or "", then this is a user-set value
				{
					try
					{
						if (labelField in data && data[labelField] != null)
						{
							labelText = data[labelField];
							if (!labelDisplay)
								createLabelDisplay();
							labelDisplay.text = labelText;
						}
						else
						{
							labelText = "";
							if (!labelDisplay)
								createLabelDisplay();
							labelDisplay.text = labelText;
						}
					}
					catch(e:Error)
					{
						labelText = "";
						if (!labelDisplay)
							createLabelDisplay();
						labelDisplay.text = labelText;
					}
				}
				else if (label && labelField === null) // if there's a label and labelField === null, then show label
				{
					labelText = label;
					if (!labelDisplay)
						createLabelDisplay();
					labelDisplay.text = labelText;
				}
				else // if labelField === ""
				{
					// get rid of labelDisplay if present
					if (labelDisplay)
						destroyLabelDisplay();
				}
				
				invalidateSize();
				invalidateDisplayList();
			}
			
			if (iconNeedsDisplayObjectAssignment) {
				iconNeedsDisplayObjectAssignment = false;
				for (var i:int=0; i < iconDisplays.length; i++) {
					assignDisplayObject(BitmapImage(iconDisplays.getItemAt(i)));
				}				
			}
		}
		
		override public function validateProperties():void {
			super.validateProperties();
			
			// Since IGraphicElement is not ILayoutManagerClient, we need to make sure we
			// validate properties of the elements
			if (iconNeedsValidateProperties) {
				iconNeedsValidateProperties = false;
				if (iconDisplays && iconDisplays.length > 0) 
					for (var i:int=0; i < iconDisplays.length; i++) {
						var iconDisplay:BitmapImage = BitmapImage(iconDisplays.getItemAt(i));
						iconDisplay.validateProperties();
					}
			}
			
		}
		
		private function assignDisplayObject(bitmapImage:BitmapImage):void {
			if (bitmapImage) {
				// try using this display object first
				if (bitmapImage.setSharedDisplayObject(this))		{
					bitmapImage.displayObjectSharingMode = DisplayObjectSharingMode.USES_SHARED_OBJECT;
				} else {
					// if we can't use this as the display object, then let's see if 
					// the icon already has and owns a display object
					var ownsDisplayObject:Boolean = (bitmapImage.displayObjectSharingMode != DisplayObjectSharingMode.USES_SHARED_OBJECT);
					
					// If the element doesn't have a DisplayObject or it doesn't own
					// the DisplayObject it currently has, then create a new one
					var displayObject:DisplayObject = bitmapImage.displayObject;
					if (!ownsDisplayObject || !displayObject)
						displayObject = bitmapImage.createDisplayObject();
					
					// Add the display object as a child
					// Check displayObject for null, some graphic elements
					// may choose not to create a DisplayObject during this pass.
					if (displayObject)
						addChild(displayObject);
					
					bitmapImage.displayObjectSharingMode = DisplayObjectSharingMode.OWNS_UNSHARED_OBJECT;
				}
			}        
		}
		
		override public function validateSize(recursive:Boolean = false):void {
			// Since IGraphicElement is not ILayoutManagerClient, we need to make sure we
			// validate sizes of the elements, even in cases where recursive==false.
			
			// Validate element size
			if (iconNeedsValidateSize) {
				iconNeedsValidateSize = false;
				
				if (iconDisplays && iconDisplays.length > 0)
					for (var i:int=0; i < iconDisplays.length; i++) {
						var iconDisplay:BitmapImage = BitmapImage(iconDisplays.getItemAt(i));
						iconDisplay.validateSize();
					}
			}
			
			
			super.validateSize(recursive);
		}
		
		override protected function measure():void {
			// don't call super.measure() because there's no need to do the work that's
			// in there--we do it all in here.
			//super.measure();
			
			// start them at 0, then go through icon, label, and decorator
			// and add to these
			var myMeasuredWidth:Number = 0;
			var myMeasuredHeight:Number = 0;
			var myMeasuredMinWidth:Number = 0;
			var myMeasuredMinHeight:Number = 0;
			
			// calculate padding and horizontal gap
			// verticalGap is already handled above when there's a label
			// and a message since that's the only place verticalGap matters.
			// if we handled verticalGap here, it might add it to the icon if 
			// the icon was the tallest item.
			var numHorizontalSections:int = 0;
			var numHorizontalSectionBetweenIcons:int = 0;
			if (iconDisplays && iconDisplays.length > 0) {
				numHorizontalSections++;
				numHorizontalSectionBetweenIcons += iconDisplays.length;
			}
						
			if (labelDisplay) {
				numHorizontalSections++;
			}
			var paddingAndGapWidth:Number = Number(getStyle("paddingLeft")) + Number(getStyle("paddingRight"));
			if (numHorizontalSections > 0)
				paddingAndGapWidth += (getStyle("horizontalGap") * (numHorizontalSections - 1));
			if (numHorizontalSectionBetweenIcons > 0) {
				paddingAndGapWidth += (getStyle("iconsGap") * (numHorizontalSectionBetweenIcons - 1));
			}
				
			var hasLabel:Boolean = labelDisplay && labelDisplay.text != "";
			
			var verticalGap:Number = (hasLabel) ? getStyle("verticalGap") : 0;
			var paddingHeight:Number = getStyle("paddingTop") + getStyle("paddingBottom");
			
			// Icon is on left
			var myIconWidth:Number = 0;
			var myIconHeight:Number = 0;
			if (iconDisplays && iconDisplays.length > 0) {
				for (var i:int=0; i < iconDisplays.length; i++) {
					var iconDisplay:BitmapImage = BitmapImage(iconDisplays.getItemAt(i));
				
					myIconWidth += (isNaN(iconWidth) ? getElementPreferredWidth(iconDisplay) : iconWidth);
					myIconHeight = (isNaN(iconHeight) ? getElementPreferredHeight(iconDisplay) : iconHeight);
				}
				myMeasuredWidth += myIconWidth;
				myMeasuredMinWidth += myIconWidth;
				myMeasuredHeight = Math.max(myMeasuredHeight, myIconHeight);
				myMeasuredMinHeight = Math.max(myMeasuredMinHeight, myIconHeight);				
			}
									
			// Text is aligned next to icon
			var labelWidth:Number = 0;
			var labelHeight:Number = 0;
						
			if (hasLabel) {
				// reset text if it was truncated before.
				if (labelDisplay.isTruncated)
					labelDisplay.text = labelText;
				
				labelWidth = getElementPreferredWidth(labelDisplay);
				labelHeight = getElementPreferredHeight(labelDisplay);
			}
					
			myMeasuredWidth += labelWidth;
			myMeasuredHeight = Math.max(myMeasuredHeight, labelHeight + verticalGap);
			
			myMeasuredWidth += paddingAndGapWidth;
			myMeasuredMinWidth += paddingAndGapWidth;
			
			// verticalGap handled in label and message
			myMeasuredHeight += paddingHeight;
			myMeasuredMinHeight += paddingHeight;
			
			// now set the local variables to the member variables.
			measuredWidth = myMeasuredWidth
			measuredHeight = myMeasuredHeight;
			
			measuredMinWidth = myMeasuredMinWidth;
			measuredMinHeight = myMeasuredMinHeight;
		}
		
		override public function validateDisplayList():void {
			super.validateDisplayList();
			
			// Since IGraphicElement is not ILayoutManagerClient, we need to make sure we
			// validate properties of the elements
			
			// see if we have an icon that needs to be validated
			if (iconDisplays && iconDisplays.length > 0) {
				for (var i:int=0; i < iconDisplays.length; i++) {
					var iconDisplay:BitmapImage = BitmapImage(iconDisplays.getItemAt(i));
					iconDisplay.validateDisplayList();
				}
			}
		}
		
		override protected function layoutContents(unscaledWidth:Number, unscaledHeight:Number):void {
			// no need to call super.layoutContents() since we're changing how it happens here
			
			// start laying out our children now
			var iconWidth:Number = 0;
			var iconHeight:Number = 0;
						
			var hasLabel:Boolean = labelDisplay && labelDisplay.text != "";
			
			var paddingLeft:Number   = getStyle("paddingLeft");
			var paddingRight:Number  = getStyle("paddingRight");
			var paddingTop:Number    = getStyle("paddingTop");
			var paddingBottom:Number = getStyle("paddingBottom");
			var horizontalGap:Number = getStyle("horizontalGap");
			var iconsGap:Number = getStyle("iconsGap");
			var verticalAlign:String = getStyle("verticalAlign");
			var verticalGap:Number   = (hasLabel) ? getStyle("verticalGap") : 0;
			
			var vAlign:Number;
			if (verticalAlign == "top")
				vAlign = 0;
			else if (verticalAlign == "bottom")
				vAlign = 1;
			else // if (verticalAlign == "middle")
				vAlign = 0.5;
			// made "middle" last even though it's most likely so it is the default and if someone 
			// types "center", then it will still vertically center itself.
			
			var viewWidth:Number  = unscaledWidth  - paddingLeft - paddingRight;
			var viewHeight:Number = unscaledHeight - paddingTop  - paddingBottom;
			
			// icon is on the left
			if (iconDisplays && iconDisplays.length > 0) {
				var iconX:Number = paddingLeft;
				for (var i:int=0; i < iconDisplays.length; i++) {
					var iconDisplay:BitmapImage = BitmapImage(iconDisplays.getItemAt(i));
					// set the icon's position and size
					setElementSize(iconDisplay, this.iconWidth, this.iconHeight);
					
					iconWidth = iconDisplay.getLayoutBoundsWidth();
					iconHeight = iconDisplay.getLayoutBoundsHeight();
					
					// use vAlign to position the icon.
					var iconDisplayY:Number = Math.round(vAlign * (viewHeight - iconHeight)) + paddingTop;
					setElementPosition(iconDisplay, iconX, iconDisplayY);
					iconX += iconWidth + iconsGap;
				}
				iconWidth = iconX - iconsGap;
			}			
			
			// Figure out how much space we have for label and message as well as the 
			// starting left position
			var labelComponentsViewWidth:Number = viewWidth - iconWidth;
			
			// don't forget the extra gap padding if these elements exist
			if (iconDisplays && iconDisplays.length > 0) 
				labelComponentsViewWidth -= horizontalGap;
			
			var labelComponentsX:Number = paddingLeft;
			if (iconDisplays && iconDisplays.length > 0) 
				labelComponentsX += iconWidth + horizontalGap;
			
			// calculte the natural height for the label
			var labelTextHeight:Number = 0;
			
			if (hasLabel) {
			
				// commit styles to make sure it uses updated look
				labelDisplay.commitStyles();
				
				labelTextHeight = getElementPreferredHeight(labelDisplay);
			}
						
			// now size and position the elements, 3 different configurations we care about:
			// 1) label and message
			// 2) label only
			// 3) message only
			
			// label display goes on top
			// message display goes below
			
			var labelWidth:Number = 0;
			var labelHeight:Number = 0;
						
			if (hasLabel) {
				// handle labelDisplay.  it can only be 1 line
				
				// width of label takes up rest of space
				// height only takes up what it needs so we can properly place the message
				// and make sure verticalAlign is operating on a correct value.
				labelWidth = Math.max(labelComponentsViewWidth, 0);
				labelHeight = labelTextHeight;
				
				if (labelWidth == 0)
					setElementSize(labelDisplay, NaN, 0);
				else
					setElementSize(labelDisplay, labelWidth, labelHeight);				
			}
					
			// Position the text components now that we know all heights so we can respect verticalAlign style
			var totalHeight:Number = 0;
			var labelComponentsY:Number = 0; 
			
			// Heights used in our alignment calculations.  We only care about the "real" ascent 
			var labelAlignmentHeight:Number = 0; 
			
			if (hasLabel)
				labelAlignmentHeight = getElementPreferredHeight(labelDisplay);
			
			totalHeight = labelAlignmentHeight + verticalGap;          
			labelComponentsY = Math.round(vAlign * (viewHeight - totalHeight)) + paddingTop;
			
			if (labelDisplay)
				setElementPosition(labelDisplay, labelComponentsX, labelComponentsY);			
		}
		
	}
	
}