/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package com.crispico.flower.util.text {
	
	import com.crispico.flower.util.UtilAssets;
	
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.FocusEvent;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.events.TextEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.text.TextLineMetrics;
	import flash.ui.Keyboard;
	
	import mx.controls.DateChooser;
	import mx.controls.LinkButton;
	import mx.controls.PopUpButton;
	import mx.controls.TextInput;
	import mx.controls.dataGridClasses.DataGridListData;
	import mx.controls.listClasses.ListData;
	import mx.core.EdgeMetrics;
	import mx.core.EventPriority;
	import mx.core.FlexVersion;
	import mx.core.IRectangularBorder;
	import mx.core.UIComponentGlobals;
	import mx.core.UITextField;
	import mx.core.mx_internal;
	import mx.events.CalendarLayoutChangeEvent;
	import mx.events.FlexEvent;
	import mx.events.FlexMouseEvent;
	import mx.events.InterManagerRequest;
	import mx.events.SandboxMouseEvent;
	import mx.formatters.DateFormatter;
	import mx.managers.ISystemManager;
	import mx.managers.PopUpManager;

	use namespace mx_internal;
	
	/**
	 * TextInput specialized to display/edit dates.
	 */
	public class DateInputText extends TextInput {
		/**
		 * Default date format.
		 */	
		public static const DATETIME_FORMAT:String = "DD/MM/YYYY JJ:NN";
		
		// hour formats
		public static const HOUR_23:int = 23; // 0-23, JJ
		
		public static const HOUR_24:int = 24; // 1-24, HH
		
		private var _currentDate:Date;
		
		private var _maxDate:Date;
		
		private var _minDate:Date;
		
		private var _formatString:String;
		
		private var _displayMask:String;
		
		private var _chooseButtonEnabled:Boolean = false;
		
		private var _allowNull:Boolean = true;
		
		/**
		 * @private
		 * marks if the DateChooser is on screen
		 */ 
		private var showingChooser:Boolean = false;
		
		/**
		 * @private
		 */ 
		private var dateChanged:Boolean = false;
		
		private var lastDate:Date = null;
		
		private var firstTimeSet:Boolean = true;
		
		protected var chooseButton:LinkButton;
		
		protected var popupButton:PopUpButton;
		
		protected var dateChooser:DateChooser;		
		
		/**
	     *  @private
	     */
	    private var openPos:Number = 0;
	    
	    /**
	     * @private
	     * used to capture data when this component is used as ItemRenderer
	     */ 	 
	    private var _data:Object;
			
		//////////////////////////////////////////////////////////////////////////////////////////////
		// Positions inside text where each date component starts.                    				//
		// If the provided _formatString will not include the component then its value should be -1 //
		//////////////////////////////////////////////////////////////////////////////////////////////
		private var _dayPosition:int = 0;
		
		private var _monthPosition:int = 3;
		
		private var _yearPosition:int = 6;
		
		private var _hourPosition:int = 11;
		
		private var _minutePosition:int = 14;
		
		// by default no seconds are displayed
		private var _secondPosition:int = -1; 
				
		private var _activeDateComponent:String;
		
		private var _hourType:int = HOUR_23;
		
		private const _maxDaysPerMonth:Array = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
		
		private var monthError:String = "";
		
		private var dayError:String = "";
		
		private var hourError:String = "";
		
		private var minuteError:String = "";
		
		private var secondError:String = "";  
		
		public function DateInputText() {
			super();
			//setStyle("fontFamily", "Arial");
			//setStyle("fontSize", 12);
			//setStyle("paddingBottom", 0);
			// set DATETIME format by default
			formatString = DATETIME_FORMAT;
			this.text = _displayMask;
			this.addEventListener(Event.ADDED_TO_STAGE, addedToStage);
		}
		
		/**
		 * The <code>Date</code> currently displayed by this component. 
		 */ 
		[Bindable(event="currentDate")]
		public function get currentDate():Date {
			return _currentDate;
		}
		
		public function set currentDate(value:Date):void {
			
			if (value != null) {
				clampDate(value);
			}
			
			this._currentDate = value;
			createTextFromDate();
			
			if (firstTimeSet) {
				lastDate = value;
				firstTimeSet = false;	
			}
			dispatchEvent(new Event("currentDate"));
		}
		
		/**
		 * When <code>true</code> this component shows a button, allowing the user to choose date from a
		 * popup </code>DateCooser</code>. User can disable the button by setting this property on <code>false</code>.
		 * This property won't take effect if changed at runtime.
		 * 
		 * @default <code>false</code>
		 */ 
		public function get chooseButtonEnabled():Boolean {
			return _chooseButtonEnabled;
		}
		
		public function set chooseButtonEnabled(value:Boolean):void {
			this._chooseButtonEnabled = value;
		}
		
		/**
		 * The maximum <code>Date</code> allowed by this component. If a bigger <code>Date</code> is inserted the it is clamped
		 * to <code>maxDate</code> and an error message is displayed.
		 * 
		 * @default <code>null</code> - not provided
		 */ 
		public function get maxDate():Date {
			return _maxDate;
		}
		
		public function set maxDate(value:Date):void {
			_maxDate = value;
			if (_maxDate != null && _currentDate != null) {
				if (_currentDate.getTime() > _maxDate.getTime()) {
					currentDate = _maxDate;
				}
			}
		}
		
		/**
		 * The minimum <code>Date</code> allowed by this component. If a smaller <code>Date</code> is inserted the it is clamped
		 * to <code>minDate</code> and an error message is displayed.
		 * 
		 * @default <code>null</code> - not provided
		 */ 
		public function get minDate():Date {
			return _minDate;
		}
		
		public function set minDate(value:Date):void {
			_minDate = value;
			if (_minDate != null && _currentDate != null) {
				if (_currentDate.getTime() < _minDate.getTime()) {
					currentDate = _minDate;
				}
			}
		}
	
		/**
		 * The format used to parse and display the <code>Date</code>.
		 *  
		 * <p>
		 * Formats allowed:
		 * <ul>
		 * 	<li>for year: YYYY
		 * 	<li>for month: MM
		 * 	<li>for date: DD
		 * 	<li>for hour: HH (24 hour format 1 - 24) or JJ (23 hour format 0 - 23)
		 * 	<li>for minute: NN
		 * 	<li>for second: SS
		 * </ul>
		 *
		 * A valid formatString can contain one, some or all the above patterns. Any combination is allowed.
		 * The order is not important.
		 * <br>Patterns must be separated by delimiters respecting the following rules:
		 * <ul>
		 * 	<li>digits are not allowed
		 * 	<li>the length of a separator is one single character
		 * 	<li>characters contained by the patterns are not allowed
		 * </ul>
		 * 
		 * <p>Example:
		 * <br>"DD/MM/YYYY" or "MM-DD-YYYY HH:NN" or "JJ:NN:SS"
		 * 
		 * @default "DD/MM/YYYY JJ:NN"
		 * 
		 * @see #DATETIME_FORMAT
		 */ 
		public function get formatString():String {
			return _formatString;
		}
		
		/** 
		 * @param str Format String
		 * YYYY - Year
		 * MM - Month
		 * DD - Date
		 * HH - Hour, 24 hour format 1-24
		 * JJ - Hour, 24 hour format 0-23
		 * NN - Minute
		 * SS - Second
		 */		
		public function set formatString(str:String):void {
			var error:String = resetFormat(str);
			
			if (error) {
				// reset to default so this component can continue normally
				formatString = DATETIME_FORMAT;
				throw new Error(error);	
			}
			
			_formatString = str;
			_displayMask = str.replace(/[MYDHJNS]/g,"_");
			this.maxChars = _displayMask.length;	
				
			createTextFromDate();	
		}
		
		override public function get data():Object {			
			return _data;
		}
		
		override public function set data(value:Object):void {
			var newDate:Date;
			
			this._data = value;
			
			if (listData != null && listData is DataGridListData) {
				newDate = _data[DataGridListData(listData).dataField];
				 	
			} else if (listData is ListData && ListData(listData).labelField in _data) {
				newDate = _data[ListData(listData).labelField]; 
					
			} else if (_data is String) {
				if (matchFormat(_data as String) && isValidDate(_data as String)) {
					// create date from the incomming text
					newDate = createDateFromText(_data as String);
				} else {
					throw new Error("Invalid date String: " + value);
				}
			} else {
				newDate = _data as Date;
			}
			
			// stop recording changes
			// start from 0
			firstTimeSet = true;
			currentDate = newDate;
			dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
		}
		
		/**
		 * When <code>true</code> this component allows <code>null</code>, otherwise a validation error is triggered
		 * when the component looses focus and no value is assigend.
		 * 
		 * By default this is <code>true</code>
		 */ 
		public function get allowNull():Boolean {
			return _allowNull;
		}
		
		public function set allowNull(value:Boolean):void {
			this._allowNull = value;
		}
		
		override mx_internal function createTextField(childIndex:int):void {
			super.createTextField(childIndex);
			textField.addEventListener(Event.CHANGE, textFieldChangeHandler, false, EventPriority.DEFAULT + 1);
			textField.addEventListener(TextEvent.TEXT_INPUT, textFieldInputChangedHandler,  false, EventPriority.DEFAULT + 1);
		}	
		
		override mx_internal function removeTextField():void {
		 	textField.removeEventListener(Event.CHANGE, textFieldChangeHandler);
			textField.removeEventListener(TextEvent.TEXT_INPUT, textFieldInputChangedHandler);
			super.removeTextField();
		}	
		 
		private function resetFormat(str:String):String {
			var delimiterWithDuplicates:String = str.replace(/[MYDHJNS]/g, "");
			var delimitesWithoutDuplicates:String = delimiterWithDuplicates.split('').sort().join('').replace(/(.)\1+/gi,'$1');
			
			if (delimitesWithoutDuplicates.match("[MYDHJNS0-9_]")) {
				return "formatString contains invalid delimiters";
			}
			
			var delimiterRegExp:RegExp = new RegExp("[" + delimitesWithoutDuplicates + "]");
			var tokens:Array = str.split(delimiterRegExp);
			
			_dayPosition = -1;
			_monthPosition = -1;
			_yearPosition = -1;
			_hourPosition = -1;
			_minutePosition = -1;
			_secondPosition = -1;
			
			for (var i:String in tokens) {
				switch (tokens[i]) {
					case "DD" :
						_dayPosition = str.indexOf(tokens[i]);
						break;
					case "MM" :
						_monthPosition = str.indexOf(tokens[i]);
						break;
					case "YYYY" :
						_yearPosition = str.indexOf(tokens[i]);
						break;
					case "JJ" :
						_hourPosition = str.indexOf(tokens[i]);
						_hourType = HOUR_23;
						break;
					case "HH" :
						_hourPosition = str.indexOf(tokens[i]);
						_hourType = HOUR_24;
						break;
					case "NN" :
						_minutePosition = str.indexOf(tokens[i]);
						break;
					case "SS" :
						_secondPosition = str.indexOf(tokens[i]);
						break;
					default:
						return "Invalid format String. Unexpected token " + tokens[i];
				}	
			}
			
			return null;
		}
		
		protected function createTextFromDate():void {
			if (currentDate == null) {
				text = _displayMask;
				return;
			}
			
			var df:DateFormatter = new DateFormatter();
			
			var currentText:String = text;
			
			// if current format includes day
			if (_dayPosition >= 0) {
				df.formatString = "DD";
				currentText = currentText.substring(0, _dayPosition) + df.format(currentDate) + currentText.substring(_dayPosition + 2, currentText.length);
			}
			
			// if current format includes month
			if (_monthPosition >= 0) {
				df.formatString = "MM";
				currentText = currentText.substring(0, _monthPosition) + df.format(currentDate) + currentText.substring(_monthPosition + 2, currentText.length)
			}
			
			// if current format includes year
			if (_yearPosition >= 0) {
				df.formatString = "YYYY";
				currentText = currentText.substring(0, _yearPosition) + df.format(currentDate) + currentText.substring(_yearPosition + 4, currentText.length);
			}
			
			// if current format includes hour
			if (_hourPosition >= 0) {
				df.formatString = _hourType == HOUR_23 ? "JJ" : "HH";
				currentText = currentText.substring(0, _hourPosition) + df.format(currentDate) + currentText.substring(_hourPosition + 2, currentText.length);
			} 
			
			// if current format includes minute
			if (_minutePosition >= 0) {
				df.formatString = "NN";
				currentText = currentText.substring(0, _minutePosition) + df.format(currentDate) + currentText.substring(_minutePosition + 2, currentText.length);
			}
			
			// if current format includes seconds
			if (_secondPosition >= 0) {
				df.formatString = "SS";
				currentText = currentText.substring(0, _secondPosition) + df.format(currentDate) + currentText.substring(_secondPosition + 2, currentText.length);
			}
			
			text = currentText;
		} 
		
		/**
		 * Creates a new Date from the given text assuming that it represents a valid Date in the format given by <code>formatString</code>.
		 * The created date will snap to maxDate/minDate if any provided.
		 */ 
		protected function createDateFromText(text:String):Date {			
			
			var date:Date = new Date();
			
			if (_yearPosition >= 0) {
				date.setFullYear(getValidNumberFromString(text.substr(_yearPosition, 4)), 0, 1);
			}
			
			if (_monthPosition >= 0) {
				date.setMonth(getValidNumberFromString(text.substr(_monthPosition, 2)) - 1, 1);	
			} else {
				date.setMonth(0);
			}
			
			if (_dayPosition >= 0) {
				date.setDate(getValidNumberFromString(text.substr(_dayPosition, 2)));
			} else {
				date.setDate(1);
			}
			
			if (_hourPosition >= 0) {
				var hour:Number = getValidNumberFromString(text.substr(_hourPosition, 2));
				if (_hourType == HOUR_24 && hour == 0) {
					hour = 24;
				}
				date.setHours(hour, 0, 0, 0);
			} else {
				date.setHours(0, 0, 0, 0);
			}
			
			if (_minutePosition >= 0) {
				date.setMinutes(getValidNumberFromString(text.substr(_minutePosition, 2)), 0, 0);	
			} else {
				date.setMinutes(0, 0, 0);
			}
			
			if (_secondPosition >= 0) {
				date.setSeconds(getValidNumberFromString(text.substr(_secondPosition, 2)), 0);
			} else {
				date.setSeconds(0, 0);
			}
			
			return date;
		}
		
		override protected function createChildren():void {
			super.createChildren();
			
			if (chooseButtonEnabled) {
				chooseButton = new LinkButton();
				chooseButton.tabEnabled = false;
				chooseButton.setStyle("icon", UtilAssets.INSTANCE._calendar);  		 
	       		chooseButton.width = 18;
	       		chooseButton.height = 18;
	       		
				addChild(chooseButton);
				dateChooser = new DateChooser();
				dateChooser.yearNavigationEnabled = true;
				
				// add listeners to the chooser button
				if (chooseButton != null) {
					chooseButton.addEventListener(FlexEvent.BUTTON_DOWN, chooseButtonClickedHandler);
				}
				
				// add listeners to the date chooser
				if (dateChooser != null) {
					dateChooser.addEventListener(CalendarLayoutChangeEvent.CHANGE, dateChooserChangeHandler);
		        	dateChooser.addEventListener(FlexMouseEvent.MOUSE_DOWN_OUTSIDE, dropdown_mouseDownOutsideHandler);
		        	dateChooser.addEventListener(FlexMouseEvent.MOUSE_WHEEL_OUTSIDE, dropdown_mouseDownOutsideHandler);
		        	dateChooser.addEventListener(SandboxMouseEvent.MOUSE_DOWN_SOMEWHERE, dropdown_mouseDownOutsideHandler);
		        	dateChooser.addEventListener(SandboxMouseEvent.MOUSE_WHEEL_SOMEWHERE, dropdown_mouseDownOutsideHandler);
		  		}
			}
		}
		
		// copied and updated from TextInput
		override protected function measure():void {
			measuredMinWidth = 0;
       		measuredMinHeight = 0;
        	measuredWidth = 0;
        	measuredHeight = 0;
	    	
	    	var bm:EdgeMetrics = border && border is IRectangularBorder ?
                             IRectangularBorder(border).borderMetrics :
                             EdgeMetrics.EMPTY;

	        var w:Number;
	        var h:Number;
	
	        // Start with a width of 160. This may change.
	        measuredWidth = DEFAULT_MEASURED_WIDTH;
	        
	        if (maxChars) {
	            // Use the width of "W" and multiply by the maxChars
	            measuredWidth = Math.min(measuredWidth,
	                measureText("W").width * maxChars + bm.left + bm.right + 8);
	        }
	        
	        if (!text || text == "") {
	            w = DEFAULT_MEASURED_MIN_WIDTH;
	            h = measureText(" ").height +
	                bm.top + bm.bottom + UITextField.TEXT_HEIGHT_PADDING;
	            if (FlexVersion.compatibilityVersion >= FlexVersion.VERSION_3_0)  
	                h += getStyle("paddingTop") + getStyle("paddingBottom");
	        
	        } else {
	            var lineMetrics:TextLineMetrics;
	            lineMetrics = measureText(text);
	
	            w = lineMetrics.width + bm.left + bm.right + 8; 
	            h = lineMetrics.height + bm.top + bm.bottom + UITextField.TEXT_HEIGHT_PADDING; 
	                            
	            if (FlexVersion.compatibilityVersion >= FlexVersion.VERSION_3_0)
	            {
	                w += getStyle("paddingLeft") + getStyle("paddingRight");
	                h += getStyle("paddingTop") + getStyle("paddingBottom");
	            }
	        }

	       	// measuredWidth = Math.max(w, measuredWidth);
	       	measuredHeight = Math.max(h, DEFAULT_MEASURED_HEIGHT);
	        
	        measuredMinWidth = DEFAULT_MEASURED_MIN_WIDTH;
	        measuredMinHeight = DEFAULT_MEASURED_MIN_HEIGHT;
	        
			var offset:int = 0;
			if (chooseButtonEnabled) {
				offset = 20;
			}
			this.minWidth = this.measuredWidth = this.measuredMinWidth = w + offset;
		}
		
		// coppied and updated from TextInput
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			var bm:EdgeMetrics = EdgeMetrics.EMPTY;
	
	        if (border) {
	            border.setActualSize(unscaledWidth, unscaledHeight);
	            bm = border is IRectangularBorder ?
	                    IRectangularBorder(border).borderMetrics : EdgeMetrics.EMPTY;
	        }
	        
	        var paddingLeft:Number = getStyle("paddingLeft");
	        var paddingRight:Number = getStyle("paddingRight");
	        var paddingTop:Number = getStyle("paddingTop");
	        var paddingBottom:Number = getStyle("paddingBottom");
	        var widthPad:Number = bm.left + bm.right;
	        var heightPad:Number = bm.top + bm.bottom + 1;
	        var rightPad:Number = bm.right;
	        
	        if (FlexVersion.compatibilityVersion >= FlexVersion.VERSION_3_0) {
	            textField.x = bm.left + paddingLeft;
	            textField.y = bm.top +  paddingTop;
	            widthPad += paddingLeft + paddingRight; 
	            heightPad += paddingTop + paddingBottom;
	            rightPad += paddingRight;
	        } else {
	        	textField.x = bm.left;
	       		textField.y = bm.top;
	        }
	        
	        textField.height = Math.max(0, unscaledHeight - heightPad);
			// center chooseButton on height and place it after the textField on width
			if (chooseButtonEnabled) {
				chooseButton.y = unscaledHeight / 2 - 10;
				chooseButton.x = unscaledWidth - rightPad - chooseButton.width;
				textField.width = Math.max(0, unscaledWidth - widthPad - chooseButton.width - 2);
			} else {
				textField.width = Math.max(0, unscaledWidth - widthPad);
			}
		}
		
		/**
		 * @private
		 * Add all the necessary listeners on this component.
		 */ 
		private function addedToStage(event:Event):void {
			addEventListener(Event.REMOVED_FROM_STAGE, removedFromStage);
			addEventListener(MouseEvent.MOUSE_UP, onMouseUp);
			
			addEventListener("currentDate", currentDateChanged, false, EventPriority.DEFAULT + 1);
			
			if (chooseButtonEnabled) {
				// add weak reference to stage
				stage.addEventListener(KeyboardEvent.KEY_DOWN, hideChooser, false, 0, true);
          		stage.addEventListener(Event.RESIZE, hideChooser, false, 0, true);
   			}		
		}
		
		private function captureChange(event:Event):void {
			event.stopImmediatePropagation();
			removeEventListener(Event.CHANGE, captureChange);
		}
		
		private function currentDateChanged(event:Event):void {			
			
			if (!areEqualDates(lastDate, currentDate)) {
				// update last date
				if (currentDate != null) {
					lastDate = new Date(currentDate.getTime());
				} else {
					lastDate = null;
				}				
				dateChanged = true;	
			}
		}
		
		private function areEqualDates(date1:Date, date2:Date):Boolean {
			if (date1 == date2) {
				return true;
			}
			
			if (date1 == null || date2 == null) {
				return false;
			}
			
			if (date1.getTime() == date2.getTime()) {
				return true;
			}
			
			return false;	
		}
		
		/**
		 * @private 
		 * Remove all the listeners except addedToStage.
		 */ 
		private function removedFromStage(event:Event):void {
			removeEventListener(Event.REMOVED_FROM_STAGE, removedFromStage);
			removeEventListener(MouseEvent.MOUSE_UP, onMouseUp);
			addEventListener("currentDate", currentDateChanged);
			if (stage) {
				stage.removeEventListener(KeyboardEvent.KEY_DOWN, hideChooser);
				stage.removeEventListener(Event.RESIZE, hideChooser);
			}
		}
		
		private function dropdown_mouseDownOutsideHandler(event:Event):void {
	        if (event is MouseEvent) {
	            var mouseEvent:MouseEvent = MouseEvent(event);
            
	            if (!hitTestPoint(mouseEvent.stageX, mouseEvent.stageY, true))
	                hideChooser();
	        } else if (event is SandboxMouseEvent) {
	          	hideChooser();
	        }   
    	}
    	
    	private function dateChooserChangeHandler(event:CalendarLayoutChangeEvent):void {
    		if (event.triggerEvent is MouseEvent) { 
    			hideChooser();	
    		}
    		
    		if (currentDate != null && dateChooser.selectedDate.getTime() == currentDate.getTime()) {	
    			return;
    		}
    		
    		var selectedDate:Date = dateChooser.selectedDate;
    		if (_currentDate) {
    			selectedDate.setHours(_currentDate.getHours(), _currentDate.getMinutes(), currentDate.getSeconds());
    		}
    		
    		currentDate = selectedDate;
    		notifyDateChanged();
    	}
    	
    	/**
    	 * Dispatch change event when loose focus and 
    	 */ 
    	private function notifyDateChanged():void {
    		if (dateChanged) {
    			dateChanged = false;
    			dispatchEvent(new Event(Event.CHANGE));
    		}
    	}
	
		protected function chooseButtonClickedHandler(event:FlexEvent):void {
       		callLater(showChooser);
		}
		
		use namespace mx.core.mx_internal;
				
		private function hideChooser(event:Event = null):void {
			if (showingChooser) {
				PopUpManager.removePopUp(dateChooser);
				showingChooser = false;
			}
		}
		
		private function showChooser():void {
			if (showingChooser || !editable || !enabled) {
				return;
			}
			showingChooser = true;
			
			dateChooser.scaleX = scaleX;
            dateChooser.scaleY = scaleY;
            var range:Object = null;
        		
    		if (minDate != null && maxDate != null) {
    			range = {rangeStart: minDate,  rangeEnd: maxDate};
    		} else if (minDate != null) {
    			range = {rangeStart: minDate};
    		} else if (maxDate != null) {
    			range = {rangeEnd: maxDate};
    		}
    		dateChooser.selectableRange = range;
    		
        	if (currentDate) {
        		dateChooser.selectedDate = currentDate;
        	} else {
        		var month:Number = getCurrentMonth();
        		if (!isNaN(month)) {
        			dateChooser.displayedMonth = month;
        		}
        		
        		var year:Number = getCurrentYear();
        		if (!isNaN(year)) {
        			dateChooser.displayedYear = year;
        		}        		
        	}
        	        	
			PopUpManager.addPopUp(dateChooser, this, false);
            PopUpManager.bringToFront(dateChooser);

	        // point x will exactly appear on the icon.
	        // Leaving 1 pixel for the border to appear.
	        var point:Point = new Point(unscaledWidth - chooseButton.width,0);
	        point = localToGlobal(point);
	        
	        var xVal:Number = point.x;
            var yVal:Number = point.y;
        	
            //handling of dropdown position
            // A. Bottom Left Placment
            // B. Bottom Right Placement
            // C. Top Right Placement
            var sm:ISystemManager = systemManager.topLevelSystemManager;
            var sbRoot:DisplayObject = sm.getSandboxRoot();
            var screen:Rectangle;

            if (sm != sbRoot) {
                var request:InterManagerRequest = new InterManagerRequest(InterManagerRequest.SYSTEM_MANAGER_REQUEST, 
                                        false, false,
                                        "getVisibleApplicationRect"); 
                sbRoot.dispatchEvent(request);
                screen = Rectangle(request.value);
            
            } else {
                screen = sm.getVisibleApplicationRect();
            }

            if (screen.right > dateChooser.getExplicitOrMeasuredWidth() + point.x &&
                screen.bottom < dateChooser.getExplicitOrMeasuredHeight() + point.y) {
                xVal = point.x
                yVal = point.y - dateChooser.getExplicitOrMeasuredHeight();
                openPos = 1;
            }
            else if (screen.right < dateChooser.getExplicitOrMeasuredWidth() + point.x &&
                     screen.bottom < dateChooser.getExplicitOrMeasuredHeight() + point.y) {
                xVal = point.x - dateChooser.getExplicitOrMeasuredWidth() + chooseButton.width;
                yVal = point.y - dateChooser.getExplicitOrMeasuredHeight();
                openPos = 2;
            }
            else if (screen.right < dateChooser.getExplicitOrMeasuredWidth() + point.x &&
                     screen.bottom > dateChooser.getExplicitOrMeasuredHeight() + point.y) {
                xVal = point.x - dateChooser.getExplicitOrMeasuredWidth() + chooseButton.width;
                yVal = point.y + unscaledHeight;
                openPos = 3;
            }
            else {
                openPos = 0;
            }

            point.x = xVal;
            point.y = yVal;
            point = dateChooser.parent.globalToLocal(point);
            UIComponentGlobals.layoutManager.validateClient(dateChooser, true);
            dateChooser.move(point.x, point.y);
            dateChooser.setActualSize(dateChooser.getExplicitOrMeasuredWidth(), dateChooser.getExplicitOrMeasuredHeight());
		}
    	
		/**
		 * Checks if a position is editable
		 */ 
		private function isPositionEditable(pos:int):Boolean {
			if (pos >= _displayMask.length || pos < 0) {
				return false;
			}
			
			if (_displayMask.charAt(pos) != "_") {
				return false;
			}
			return true;
		}
		
		/**
		 * @private
		 * catch changes on the textField component and cancel them when necessary.
		 */ 
		private function textFieldChangeHandler(event:Event):void {
			if (textField.text != text) {
				textField.text = this.text;
			}
			// change event will be thrown each time the text inside changes but
			// we want to be thrown only when the component looses focus or 
			addEventListener(Event.CHANGE, captureChange, false, EventPriority.DEFAULT + 1);
		}
		
		private function matchFormat(text:String):Boolean {
			if (text.length != _displayMask.length) {
				return false;
			}
			
			for (var i:int = 0; i < _displayMask.length; i++) {
				if (_displayMask.charAt(i) == "_" && (text.charCodeAt(i) < 47 || text.charCodeAt(i) > 58)) {
					return false
				} 
			} 
			
			return true;
		}
		
		private function textFieldInputChangedHandler(event:TextEvent):void {
			// paste behavior
			// accept text only if it matches this formatString and can be transformed to valid date
			if (matchFormat(event.text) && isValidDate(event.text)) {
				// create date from the incomming text
				currentDate = createDateFromText(event.text);
			// normal behavior
			} else {
				createDate();
			}
		}
		
		private function createDate():void {
			validateComponents();
			// if errors found or the test not yet completed
			if (this.errorString.length > 0 || text.indexOf("_") != -1) {
				// do not use setter because that will cause the text to change
				_currentDate = null;
				dispatchEvent(new Event("currentDate"));
			} else {
				var newDate:Date = createDateFromText(this.text);
				
				if (clampDate(newDate)) {
					// use setter because text must be updated too in this case
					currentDate = newDate;
				} else {
					// just use the created date when it is valid
					_currentDate = newDate;
					dispatchEvent(new Event("currentDate"));
				}
			}			
		}
		
		/**
		 * Clamp given Date between min and max Date if they are provided.
		 * Returns <code>true</code> sets and error message if the date was clamped. 
		 */ 
		private function clampDate(date:Date):Boolean {
			
			if (maxDate != null && date.getTime() > maxDate.getTime()) {
				date.setTime(maxDate.getTime());
				this.errorString = UtilAssets.INSTANCE.getMessage('text.maxDateError', [maxDate]);
				return true;
			}
			
			if (minDate != null && date.getTime() < minDate.getTime()) {
				date.setTime(minDate.getTime());
				this.errorString = UtilAssets.INSTANCE.getMessage('text.minDateError', [minDate]);
				return true;
			}
			
			return false;
		}
		
		override protected function keyDownHandler(event:KeyboardEvent):void {
			onKeyDown(event);
			super.keyDownHandler(event);
		}
		
		private function findNextDateComponentPosition(position:int):int {
			var closestFound:int = text.length;
			
			if (_yearPosition >= 0 && _yearPosition > position && _yearPosition < closestFound) {
				closestFound = _yearPosition;
			}
			if (_monthPosition >= 0 && _monthPosition > position && _monthPosition < closestFound) {
				closestFound = _monthPosition;
			}
			
			if (_dayPosition >= 0 && _dayPosition > position && _dayPosition < closestFound) {
				closestFound = _dayPosition;
			}
			
			if (_hourPosition >= 0 && _hourPosition > position && _hourPosition < closestFound) {
				closestFound = _hourPosition;
			}
			
			if (_minutePosition >= 0 && _minutePosition > position && _minutePosition < closestFound) {
				closestFound = _minutePosition;
			}
			
			if (_secondPosition >= 0 && _secondPosition > position && _secondPosition < closestFound) {
				closestFound = _secondPosition;
			}
			return closestFound;
		}
		
		private function findPreviousDateComponentPosition(position:int):int {
			var closestFound:int = 0;
			
			if (_yearPosition >= 0 && _yearPosition < position && _yearPosition > closestFound) {
				closestFound = _yearPosition;
			}
			if (_monthPosition >= 0 && _monthPosition < position && _monthPosition > closestFound) {
				closestFound = _monthPosition;
			}
			
			if (_dayPosition >= 0 && _dayPosition < position && _dayPosition > closestFound) {
				closestFound = _dayPosition;
			}
			
			if (_hourPosition >= 0 && _hourPosition < position && _hourPosition > closestFound) {
				closestFound = _hourPosition;
			}
			
			if (_minutePosition >= 0 && _minutePosition < position && _minutePosition > closestFound) {
				closestFound = _minutePosition;
			}
			
			if (_secondPosition >= 0 && _secondPosition < position && _secondPosition > closestFound) {
				closestFound = _secondPosition;
			}
			return closestFound;
		}
		
		private var onShiftSelectionEnd:int = -1;
		
		private function onKeyDown(event:KeyboardEvent):void {
			// stop event propagation and treat it here
        	event.preventDefault();
        	event.stopImmediatePropagation();
           	
           	if (!editable) {
           		return;
           	}
           	
        	if (showingChooser) {
        		if (event.keyCode == Keyboard.ESCAPE) {
        			hideChooser();
        		}
        		return;
        	}
        	        	
        	if (event.shiftKey && onShiftSelectionEnd == -1) {
        		onShiftSelectionEnd = selectionEndIndex;
        	} else if (!event.shiftKey) {
        		onShiftSelectionEnd = -1;
        	}
        	var newSelectionBegin:int = selectionBeginIndex;
        	var newSelectionEnd:int = selectionEndIndex;
			
			if (event.keyCode == Keyboard.LEFT) {
				
				if (event.ctrlKey && event.shiftKey) {
					newSelectionBegin = findPreviousDateComponentPosition(selectionBeginIndex);
					
				} else if (event.ctrlKey) {
					newSelectionBegin = newSelectionEnd = findPreviousDateComponentPosition(selectionBeginIndex);
				
				} else if (event.shiftKey) {
					newSelectionBegin -= 1;
					newSelectionEnd = onShiftSelectionEnd;	
				
				} else {
					// go to previos editable position
					while (newSelectionBegin > 0 && !isPositionEditable(newSelectionBegin -1)) {
						newSelectionBegin --;
					}
					
					newSelectionBegin = newSelectionEnd = newSelectionBegin - 1;
				}
				
				setSelection(newSelectionBegin, newSelectionEnd);
				
				var oldActiveDateComponent:String = _activeDateComponent;
				// also pass the new begin and end selection indexes because the super bahavior takes this parameters 
				// from the child UITextField and they might not be yet update => active component will not be properly set
				resetActiveDateComponent(newSelectionBegin, newSelectionEnd);
				
				// change the date component in focus => filled blanks => create and validate Date 
				if (oldActiveDateComponent != _activeDateComponent) {
					createDate();
				}
        	}
        	
        	if (event.keyCode == Keyboard.RIGHT) {
        		if (event.ctrlKey && event.shiftKey) {
        			newSelectionEnd = findNextDateComponentPosition(selectionEndIndex);
        		
        		} else if (event.ctrlKey) {
        			newSelectionBegin = newSelectionEnd = findNextDateComponentPosition(selectionEndIndex);
        			
        		} else if (event.shiftKey) {
        			newSelectionEnd += 1;
        		
        		} else {
        			
        			// go to next editable position
        			// do in while considering that the separators might be multiple character strings
        			while (newSelectionEnd < text.length && !isPositionEditable(newSelectionEnd + 1)) {
        				newSelectionEnd ++;
        			}
        			
        			newSelectionBegin = newSelectionEnd = newSelectionEnd + 1;
        		}
        		
        		setSelection(newSelectionBegin, newSelectionEnd);
        		
        		oldActiveDateComponent = _activeDateComponent;
				// also pass the new begin and end selection indexes because the super bahavior takes this parameters 
				// from the child UITextField and they might not be yet update => active component will not be properly set
				resetActiveDateComponent(newSelectionBegin, newSelectionEnd);
				
				// change the date component in focus => filled blanks => create and validate Date 
				if (oldActiveDateComponent != _activeDateComponent) {
					createDate();
				}
        	}
        	
        	if (event.keyCode == Keyboard.BACKSPACE) {
        		if (newSelectionBegin == 0) {
        			return; // already on first position= > unable to remove any characters
        		}
        		if (isPositionEditable(newSelectionBegin - 1)) {
        			this.text = text.substring(0, newSelectionBegin - 1) + "_" + text.substring(newSelectionBegin, text.length);
				
				} else { // on separator the jump one position backwards
					newSelectionBegin --;
					this.text = text.substring(0, newSelectionBegin - 1) + "_" + text.substring(newSelectionBegin, text.length);
				}
				
				newSelectionBegin = newSelectionEnd = newSelectionBegin - 1;
				
				setSelection(newSelectionBegin, newSelectionEnd);
				
				resetActiveDateComponent(newSelectionBegin, newSelectionEnd);
				createDate();
        	}
        	
        	// on delete clear this date
        	if (event.keyCode == Keyboard.DELETE) {
        		var selectionLen:int = newSelectionEnd - newSelectionBegin;
        		// if all the content is selected then clear
    			if (selectionLen == _displayMask.length) {
    				validateComponents();
        			currentDate = null;
        			
        		} else if (isPositionEditable(newSelectionBegin)) {
        			
        			// cursor is on one editable character
        			if (selectionLen == 0) {
	        			var end:int = getActiveComponentEndPosition();
	        			
	        			// find first non empty character
	        			for (var i:int = selectionBeginIndex; i < end; i++) {
	        				if (text.charAt(i) != "_") {
	        					break;
	        				}
	        			}
	        			// still have characters to remove in the active date component
	        			if (i != end) {
	        				text = text.substring(0, i) + "_" + text.substring(i + 1, text.length);
	        			}
	        			createDate();
        			
        			// if only part of the content is selected	
        			} else if (selectionLen < text.length) {
	        			var replacement:String = _displayMask.substring(this.selectionBeginIndex, this.selectionEndIndex + 1);
	        			
	        			text = text.substring(0, selectionBeginIndex) + replacement + text.substring(selectionEndIndex + 1, text.length); 
	        			createDate();
	        		}  
        		}
        		
        		this.setSelection(newSelectionBegin, newSelectionBegin);
        		
        		resetActiveDateComponent(newSelectionBegin, newSelectionBegin);	        		
        	}
        	
        	// only digits allowed ( digits in ASCII table are between 48 = 0 and 57 = 9)
        	if ((event.keyCode >= 48 && event.keyCode <= 57) 
        			|| (event.keyCode >= Keyboard.NUMPAD_0 && event.keyCode <= Keyboard.NUMPAD_9)) {
        				
        		// at the end of the text
        		if (newSelectionBegin == _displayMask.length ) {
        			return;
        		}
        		
        		var shifting:int = event.keyCode >= Keyboard.NUMPAD_0 ? 48 : 0;
        		
      			var key:String = String.fromCharCode(event.keyCode - shifting);
					
				// this can happen because of backspace	or because cursor can be set every where with the mouse
				if (!isPositionEditable(newSelectionBegin)) {
					newSelectionBegin ++;
					setSelection(newSelectionBegin, newSelectionBegin);
				}      			
				
      			this.text = text.substring(0, newSelectionBegin) + key + text.substring(newSelectionBegin + 1, text.length);
      			
      			newSelectionBegin += 1;
      			while (!isPositionEditable(newSelectionBegin) && newSelectionBegin < maxChars)
      				newSelectionBegin++;
      			
      			this.setSelection(newSelectionBegin, newSelectionBegin);
      			
      			resetActiveDateComponent(newSelectionBegin, newSelectionBegin);
      		}
      		
      		// numpad + => add currenty date 
      		if (event.keyCode == Keyboard.NUMPAD_ADD) {
      			currentDate = new Date();
      			this.setSelection(0, 0);
      			resetActiveDateComponent(0, 0);
      		}
      		
			if (event.keyCode == Keyboard.UP) {
				highlightActiveDateComponent();
				updateActiveComponent(1);
			}
        	
        	if (event.keyCode == Keyboard.DOWN) {
        		highlightActiveDateComponent();
				updateActiveComponent(-1); 
			}
			
			// compute new date and notify also on enter not only when loosing focus
			if (event.keyCode == Keyboard.ENTER) {
				createDate();
				notifyDateChanged();
			}
        }
		
		private function highlightActiveDateComponent():void {
			// BUG FIX
			// sometimes at first click - when all the field gets selected, there is no active component yet
			// when pressing UP the field will position automatically on first component without activating it
			// as a result it won't respond on further UP and DOWN button press
			// no active component to highlight then position on first
			if (_activeDateComponent == null) {
				this._activeDateComponent = getActiveDateComponent(0, 0);
			}
			
			if (_activeDateComponent == "y") {
				setSelection(_yearPosition, _yearPosition + 4);
				return; 
			} 
			
			var startPosition:int = 0;
			
			if (_activeDateComponent == "m") {
				startPosition = _monthPosition;
			}
			
			if (_activeDateComponent == "d") {
				startPosition  = _dayPosition;
			}
			
			if (_activeDateComponent == "j") {
				startPosition = _hourPosition;
			}
			
			if (_activeDateComponent == "n") {
				startPosition = _minutePosition;
			}
			
			if (_activeDateComponent == "s") {
				startPosition = _secondPosition;
			}
			
			setSelection(startPosition, startPosition + 2);
		}
		
		private function getActiveComponentEndPosition():int {
			
			switch(_activeDateComponent) {
				case "y":
					return _yearPosition + 4;
				case "m":
					return _monthPosition + 2;
				case "d":
					return _dayPosition + 2;
				case "j":
					return _hourPosition + 2;
				case "n":
					return _minutePosition + 2;
				case "s":
					return _secondPosition + 2;
				default:
					return 0;
			}
		}
		
		private function getActiveDateComponent(selectionBegin:int, selectionEnd:int):String {
			// position is on year
			if (_yearPosition >=0 && selectionBegin >= _yearPosition && selectionEnd <= _yearPosition + 4) {
				return "y";
			} 
				
			if (_monthPosition >= 0 && selectionBegin >= _monthPosition && selectionEnd <= _monthPosition + 2) {
				return "m";
			}
			
			if (_dayPosition >= 0 && selectionBegin >= _dayPosition && selectionEnd <= _dayPosition + 2) {
				return "d";
			}		
			
			if (_hourPosition >= 0 && selectionBegin >= _hourPosition && selectionEnd <= _hourPosition + 2) {
				return "j";
			}
			
			if (_minutePosition >= 0 && selectionBegin >= _minutePosition && selectionEnd <= _minutePosition + 2) {
				return "n";
			}
			
			if (_secondPosition >= 0 && selectionBegin >= _secondPosition && selectionEnd <= _secondPosition + 2) {
				return "s";
			}
			
			return null;
		}
		
		private function resetActiveDateComponent(selectionBegin:int, selectionEnd:int):void {
			var newActiveComponent:String = getActiveDateComponent(selectionBegin, selectionEnd);
			if (newActiveComponent != _activeDateComponent && _activeDateComponent != null) {
				fillBlanks(_activeDateComponent);
			}			
			_activeDateComponent = newActiveComponent;			 
		}
		
		private function updateActiveComponent(increment:int):void {
			if (_activeDateComponent == null) {
				return;
			}
			
			var value:Number;
			var currentDate:Date = new Date();
			
			switch(_activeDateComponent) {
				case "y":
					var yearString:String = text.substr(_yearPosition, 4);
					 // if empty then start with current year
					if (yearString == "____") {
						value = currentDate.getFullYear();
					} else {
						value = getValidNumberFromString(yearString.replace(/_/g, "0")) + increment; 
					}
					
					text = text.slice(0, _yearPosition) + validateNumber(value, 4, 0, 9999) + text.slice(_yearPosition + 4, text.length);
					break;
					
				case "m":
					var monthString:String = text.substr(_monthPosition, 2);
					// no month inserted then start with current month
					if (monthString == "__") {
						value = currentDate.getMonth() + 1;
					} else {
						value = getValidNumberFromString(monthString) + increment;
					}
					text = text.slice(0, _monthPosition) + validateNumber(value, 2, 1, 12) + text.slice(_monthPosition + 2, text.length);
					break;
					
				case "d":
					var dayString:String = text.substr(_dayPosition, 2);
					// no day inserted then get current date
					if (dayString == "__") {
						value = currentDate.getDate();
					} else {
						value = getValidNumberFromString(dayString) + increment;
					}
					var max:Number = getMaxDayAllowed();
					text = text.slice(0, _dayPosition) + validateNumber(value, 2, 1, max) + text.slice(_dayPosition + 2, text.length);
					break;
					
				case "j":
					var hourString:String = text.substr(_hourPosition, 2);
					
					if (hourString == "__") {
						value = currentDate.getHours();
						if (_hourType == HOUR_24 && value == 0) {
							value = 24;
						}
					} else {
						value = getValidNumberFromString(hourString) + increment;
					}
					
					var min:Number = 0; max = 23;
					
					if (_hourType == HOUR_24) {
						min ++; max ++;
					} 
					text = text.slice(0, _hourPosition) + validateNumber(value, 2, min, max) + text.slice(_hourPosition + 2, text.length);
					break;
					
				case "n":
					var minuteString:String = text.substr(_minutePosition, 2);
					if (minuteString == "__") {
						value = currentDate.getMinutes();
					} else {
						value = getValidNumberFromString(minuteString) + increment;	
					}
					  
					text = text.slice(0, _minutePosition) + validateNumber(value, 2, 0, 59) + text.slice(_minutePosition + 2, text.length);	
					break;
									
				case "s":
					var secondString:String = text.substr(_secondPosition, 2);
					if (secondString == "__") {
						value = currentDate.getSeconds();
					} else {
						value = getValidNumberFromString(secondString) + increment;
					} 
					text = text.slice(0, _secondPosition) + validateNumber(value, 2, 0, 59) + text.slice(_secondPosition + 2, text.length);
					break;							
			}
			
			// BUG FIX
			// if this date is complete - then immediately create it
			// when using this component as itemEditor it must provide the new date immediately after modification otherwise the changes are lost 
			// For instance if the user presses UP and then leaves the component - this component didn't reset its date value 
			// (because no numbers typed and no LEFT/RIGHT navigation - to trigger active date component change)
			if (text.indexOf("_") == -1) {
				createDate();
			}	
		}
		
		/**
		 * @private
		 * Transforms a String into number. All the "_" characters are ignored.
		 */ 
		private function getValidNumberFromString(str:String):Number {
			var validNumString:String = str.replace(/_/g, "");
			return Number(validNumString);
		}
		
		/**
		 * @private
		 * Obtains month from current text.
		 * If no valid month then <code>NaN</code> is returned.
		 */ 
		private function getCurrentMonth():Number {
			if (_currentDate != null) {
				return _currentDate.getMonth();
			
			} else if (_monthPosition >= 0) {
				var month:Number = getValidNumberFromString(text.substr(_monthPosition, 2)) - 1;
				// but month might be invalid in the string
				if (month < 1 || month > 12) {
					return NaN;
				}
				return month;
			}
			
			return NaN;
		}
		
		/**
		 * @private
		 * Obtains year from current text.
		 * If no valid year then <code>NaN</code> is returned.
		 */ 
		private function getCurrentYear():Number {
			var year:Number = NaN;
			
			if (_currentDate != null) {
				year =  _currentDate.getFullYear();
			
			} else if (_yearPosition >= 0) {
				year = Number(text.substr(_yearPosition, 4).replace(/_/g, "0"));
			}
			return year;
		}
		
		// verifica an bisect
		private function isLeapYear():Boolean {
			var year:Number = getCurrentYear();
			
			if (isNaN(year)) {
				return false;
			}
			
			if((year % 4 == 0 && year % 100 != 0 ) || year % 400 == 0) {
				return true;
			}
			return false;
		}
		
		private function getMaxDayAllowed():int {
			var max:Number = 31;					
			var month:Number = getCurrentMonth();
			
			if (!isNaN(month)) {
				max = _maxDaysPerMonth[month];
				// february
				if (month == 1 && isLeapYear()) { 
					max = 29;
				}
			}
			return max;
		}
				
		private function validateNumber(number:int, length:int, min:int, max:int):String {
			if (number < min) { 
				number = max;
			}
			if (number > max) {
				number = min;
			}
			var ret:String = number.toString();
			
			while(ret.length < length ) {
				ret="0" + ret;
			}
			return ret.slice(ret.length - width, ret.length);
		}
		
		
		private function isValidDate(text:String):Boolean {
			if (_monthPosition >= 0) {
				var monthString:String = text.substr(_monthPosition, 2);
				monthError = "";
				// check if month is completed
				if (monthString.indexOf("_") == -1) {
					var value:Number = getValidNumberFromString(monthString);
					if (value < 1 || value > 12) {
						monthError = UtilAssets.INSTANCE.getMessage('text.monthError');
					}
				}
			}
			
			if (_dayPosition >= 0) {
				var dayString:String = text.substr(_dayPosition, 2);
				dayError = "";
				if (dayString.indexOf("_") == -1) {
					value = getValidNumberFromString(dayString);
				
					var max:int = getMaxDayAllowed();
				
					if (value < 1 || value > max) {
						dayError = UtilAssets.INSTANCE.getMessage('text.dateError', [max]);
					}
				}				
			
			}
			
			if (_hourPosition >= 0) {
				var hourString:String = text.substr(_hourPosition, 2);
				
				hourError = "";
				if (hourString.indexOf("_") == -1) {
					value = getValidNumberFromString(hourString);
				
					var min:int = 0; max = 23;
					
					if (_hourType == HOUR_24) {
						min ++; max ++;
					} 
					
					if (value < min || value > max) {
						hourError = UtilAssets.INSTANCE.getMessage('text.hourError', [min, max]);
					}
				}
			}
						
			if (_minutePosition >= 0) {
				var minuteString:String = text.substr(_minutePosition, 2);
				
				minuteError = "";
				if (minuteString.indexOf("_") == -1) {
					value = getValidNumberFromString(minuteString);
				
					if (value < 0 || value > 59) {
						minuteError = UtilAssets.INSTANCE.getMessage('text.minuteError');
					}
				}				
			}		
		
			if (_secondPosition >= 0) {
				var secondString:String = text.substr(_secondPosition, 2);
				
				secondError = "";
				if (secondString.indexOf("_") == -1) {
					value = getValidNumberFromString(secondString);
				
					if (value < 0 || value > 59) {
						secondError = UtilAssets.INSTANCE.getMessage('text.secondError');
					}
				}
			}
			
			// no errors found
			return monthError.length + dayError.length + hourError.length + minuteError.length + secondError.length == 0;
		}
		
		/**
		 * Validate the date components of a text and returns a string error in case validation
		 * errors were found.
		 */ 
		private function validateComponents():void {		
			
			isValidDate(this.text);
			
			var newErrorString:String = monthError 
								+ (dayError.length > 0 ? "\n" + dayError : "")
								+ (hourError.length > 0 ? "\n" + hourError : "")
								+ (minuteError.length > 0 ? "\n" + minuteError : "")
								+ (secondError.length > 0 ? "\n" + secondError : "");
			
			if (newErrorString.charAt(0) == "\n") {
				this.errorString = newErrorString.slice(1, newErrorString.length);
			} else {
				this.errorString = newErrorString;
			}													 
		}
		
		/**
		 * Called each time a date component looses focus.
		 * If the component is partially completed (it has "_" characters), fills empty spaces with 0:
		 * For year, each "_" will be replaced with 0
		 * For other components only the first position is 0 if one character inserted (2_ => 02 and _2 => 02).
		 */ 
		private function fillBlanks(component:String):void {
			if (component == "y") {
				
				var yearText:String = text.substr(_yearPosition, 4);
				if (yearText != "____") {
					text = text.slice(0, _yearPosition) + yearText.replace(/_/g, "0") + text.slice(_yearPosition + 4, text.length);
				}
				return;  
			} 
			
			var startPos:int = -1;
			if (component == "m") {	
				startPos = _monthPosition;
				 
			} else if (component == "d") {
				startPos = _dayPosition;
				
			} else if (component == "j") {
				startPos = _hourPosition;
				
			} else if (component == "n") {
				startPos = _minutePosition;
			
			} else if (component == "s") {
				startPos = _secondPosition;
			}
			
			// all the others have length 2
			var componentText:String = text.substr(startPos, 2);
			// only if component is partially completed
			if (componentText != "__" && componentText.indexOf("_") != -1) {
				componentText = 0 + componentText.replace("_", "");
				text = text.slice(0, startPos) + componentText + text.slice(startPos + 2, text.length);
			}
		}		 
		
		private function onMouseUp(event:MouseEvent):void {
			var lastActiveDateComponent:String = _activeDateComponent;
	        resetActiveDateComponent(this.selectionBeginIndex, this.selectionBeginIndex);
	        if (lastActiveDateComponent != _activeDateComponent) {
	        	createDate();
	        }
		}
		
		override protected function focusOutHandler(event:FocusEvent):void {
			// when null is not allowed displayed Date can't be empty
			if (text == _displayMask) {
				
				if (!allowNull) {
					errorString = UtilAssets.INSTANCE.getMessage('text.incompleteDateError');
					super.focusOutHandler(event);
					return;
				}
			
			// if this component is not fully completed and the user leaves the component
			// a valid date can't be created so notify user	
			} else if (text.indexOf("_") != -1) {
				errorString = UtilAssets.INSTANCE.getMessage('text.incompleteDateError');
			}
			super.focusOutHandler(event);
			// notify only if the text looses focus
			// exclude case when focus comes back from the chooseButton (it is still on this component)
			if (event.relatedObject != chooseButton) {
				notifyDateChanged();
			}		
		}
		
		override protected function focusInHandler(event:FocusEvent):void {
			// when back in focus revalidate so a proper message cen be displayed
			validateComponents();
			// regain focus but date chooser is on => hide chooser and let the user update the field
			if (event.target == textField && showingChooser) {
				hideChooser();
			}
			super.focusInHandler(event);
			
		}
		
	}
}