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
package  com.crispico.flower.util.layout {
	import com.crispico.flower.flexdiagram.ui.MoveResizePlaceHolder;
	import org.flowerplatform.flexutil.layout.LayoutData;
	import com.crispico.flower.util.layout.persistence.SashLayoutData;
	import com.crispico.flower.util.layout.persistence.StackLayoutData;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import com.crispico.flower.util.layout.persistence.WorkbenchLayoutData;
	
	import flash.display.DisplayObject;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	
	import flexlib.containers.ButtonScrollingCanvas;
	import flexlib.containers.SuperTabNavigator;
	import flexlib.controls.SuperTabBar;
	import flexlib.controls.tabBarClasses.SuperTab;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Button;
	import mx.core.UIComponent;
	import mx.managers.CursorManager;
	
	/**
	 * This class represents a state machine for the interactions of the mouse with the layout area.
	 * <p>
	 * There are 3 states:
	 * <ul>
	 * 	<li> IDLE - when nothing is going on; in this state we may end up after a DRAGGING state;
	 * 	<li> DRAG_ABOUT_TO_START - when a mouse down has been detected and conditions were met (like we are allowed to drag from there),
	 *    but a mouse move has not been performed yed; we end up in this state ony after IDLE state;
	 * 	<li>DRAGGING - when we have detected at least one mouse move, of course state possibly only after DRAG_ABOUT_TO_START has entered.
	 * </ul>
	 * 
	 * Mouse event handler change the state of this tool using the #arrangeState property, and are carefully intepreting the state
	 * in order to know if dragStart* properties need to be computed (once) and if dropEnd* properties need to be computed 
	 * (as lazily as possible caching the mouse graphical component target).
	 * 
	 * @author Cristina
	 * 
	 */
	public class ArrangeTool {
		
		/**
		 * Tool states
		 */ 		 
		private static const IDLE:Number = 1;
		
		private static const DRAG_ABOUT_TO_START:Number = 2;
		
		private static const DRAGGING:Number = 3;
			
		public static const MOVE_BY_DRAGGING:int = 0;
		
		public static const MOVE_BY_CLICKING:int = 1;
		
		/**
		 * Drag/Drop component type
		 */		 
		public static const OVER_TAB:Number = 1;
		
		public static const OVER_TAB_BAR:Number = 2;
		
		public static const NEW_TAB_NAVIGATOR:Number = 3;
			
		/**
		 * Side values
		 */ 
		public static const NONE:Number = 0;
		
		public static const LEFT:Number = 1;
		
		public static const RIGHT:Number = 2;
		
		public static const UP:Number = 3;
		
		public static const DOWN:Number = 4;
		
		public static const CENTER:Number = 5;
		
		/**
		 * Side padding.
		 * Based on this value, the CENTER is detected.
		 */ 
		private static const SIDE_PADDING:Number = 50;
		
		/**
		 * Cursors
		 */ 
		[Embed(source="/top_arrow.gif")]      
		private var topArrow:Class;
		
		[Embed(source="/bottom_arrow.gif")]      
		private var bottomArrow:Class;
		
		[Embed(source="/left_arrow.gif")]      
		private var leftArrow:Class;
		
		[Embed(source="/right_arrow.gif")]      
		private var rightArrow:Class;
		
		[Embed(source="/centerCursor.gif")]      
		private var centerCursor:Class;
		
		/**
		 * Keep a reference to the workbench in order to access data or graphical components from it, 
		 * like map from components to the models, layouable area.
		 */
		private var workbench:Workbench;
		
		/**
		 * A rectangle with border and semi-transparent background that is used during <code>DRAGGING</code> state.
		 * It's purpose is to show the space where the drag element will be placed.
		 */ 
		private var placeholder:MoveResizePlaceHolder;
		
		/**
		 * The tool's state.
		 */
		private var arrangeState:Number;
		
		/**
		 * Stores the drag layout data when starting to drag.
		 * @see computeDragStartDetails()
		 * @see mouseUpHandler()
		 */
		private var dragStartLayoutData:LayoutData;
		
		/**
		 * Used to determine the feedback rectangle. More exacly if the drop cursor is over the tab bar,
		 * depending on what we are dragging a tab or a stack, it would show as feedback a rectangle as a tab,
		 * or a rectangle as a stack.
		 * Can have one of the following values:
		 * <ul>
		 * 	<li> OVER_TAB - when drag was started from the tab
		 * 	<li> OVER_TAB_BAR - when the drag was started from the tab bar
		 * </ul>
		 */
		private var dragStartType:Number;
			
		/**
		 * Stores the drop layout data when dragging.
		 * @see mouseUpHandler()
		 */
		private var dropEndLayoutData:LayoutData;
		
		/**
		 * Used to determine the feedback rectangle. Can have one of the following values:
		 * <ul>
		 * 	<li> OVER_TAB - when the cursor is over the tab
		 * 	<li> OVER_TAB_BAR - when the cursor is over the tab bar
		 * 	<li> NEW_TAB_NAVIGATOR- when the cursor is neither on a tab or on the tab bar
		 * </ul>
		 */
		private var dropEndType:Number;
		
		/**
		 * In case that the dropping would be over a tab, and we started draging a tab, then this would
		 * keep the index of the tab over which to drop.
		 */
		private var dropEndTabIndex:Number;
		
		/**
		 * If the drop cursor position meets certain conditions this would be one of the values: LFET, RIGHT, UP, DOWN, CENTER.
		 * @see getSide()
		 * @see computeDropEndDetails()
		 */
		private var dropEndTabNavigatorSide:Number;
		
		/**
		 * Stores the current cursor.
		 * Used to change the new cursor only if necessary.
		 * 
		 * @see updateFeedbackRectangleAndCursor()
		 */ 		
		private var currentCursor:Class;
				
		public var moveState:int;
		
		/**
		 * Keeps the workbench on which to work and adds listeners for mouse.
		 */
		public function activate(workbench:Workbench):void {
			this.workbench = workbench;
						
			this.workbench.addEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler);
			this.workbench.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			this.workbench.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);			
		}
				
		/**
		 * Clears the workbench property and removes the mouse listeners.
		 * Also clears the state of the tool (dragStart*, dropEnd*, etc).
		 */
		public function deactivate():void {
			this.workbench = null;
			clear();
			
			this.workbench.removeEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler);
			this.workbench.removeEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			this.workbench.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);	
		}
				
		public function startDragging(point:Point, state:int):void {
			// get layout component under mouse
			var result:Object = getLayoutDetailsUnderMouse(point);
			// if not found or  isn't a tab or a tab bar, don't start the dragging process
			if (result.layout == null || (result.type == NEW_TAB_NAVIGATOR)) {
				return;
			}
			// if a stack is dragged and it has no children (Case for editor area), don't start the dragging process
			if (result.layout is StackLayoutData && StackLayoutData(result.layout).children.length == 0) {
				return;
			}
			arrangeState = DRAG_ABOUT_TO_START;		
			moveState = state;
			
			if (moveState == MOVE_BY_CLICKING) {
				arrangeState = DRAGGING;
				computeDragStartDetails(point);
			}
		}
		
		/**
		 * Sets the dragging data for a <code>ViewLayoutData</code>.
		 */ 
		public function startDraggingView(viewLayoutData:ViewLayoutData):void {			
			dragStartLayoutData = viewLayoutData;
			dragStartType = OVER_TAB;
						
			moveState = MOVE_BY_CLICKING;					
			arrangeState = DRAGGING;		
		}
		
		/**
		 * The tool enters in the <code>DRAG_ABOUT_TO_START</code> state only if an object under the mouse has a layout model associated and
		 * it's a tab or a tab bar. 
		 */ 
		private function mouseDownHandler(event:MouseEvent):void {
			if (moveState != MOVE_BY_CLICKING) {
				startDragging(new Point(event.stageX, event.stageY), MOVE_BY_DRAGGING);
			}
		}
		
		/**
		 * The tool functionality for mouse move event will be executed only if 
		 * the state is <code>DRAG_ABOUT_TO_START<code> or <code>DRAGGING</code>.
		 * <p>
		 * If the mouse button isn't down when moving (e.g. mouse was moved out of the application area and a mouse up was executed there)
		 * set the tool's state to <code>IDLE</code> and clear the details.
		 * <p>
		 * If tool's state is <code>DRAG_ABOUT_TO_START</code> (a mouse down event was executed on a tab/tab bar),
		 * stores the drag details and set the tool's state to <code>DRAGGING</code>.
		 * <p>
		 * Updates the drop details, the placeHolder positions/dimensions and the mouse cursor 
		 * each time a mouse move event is detected. 
		 */ 
		private function mouseMoveHandler(event:MouseEvent):void {
			// don't move further if we aren't in the dragging or about to start dragging state
			if (arrangeState != DRAG_ABOUT_TO_START && arrangeState != DRAGGING) {
				return;
			}
			if (!event.buttonDown && moveState != MOVE_BY_CLICKING) {
				clear();				
				// abort dragging
				arrangeState = IDLE;
				return;
			}
			// dragging is starting, so get/store the drag details
			if (arrangeState == DRAG_ABOUT_TO_START) {				
				arrangeState = DRAGGING;
				computeDragStartDetails(new Point(event.stageX, event.stageY));
			}
			// each time a mouse move is detected, update the drop details				
			computeDropEndDetails(event);
			// update the placeHolder and mouse cursor
			updateFeedbackRectangleAndCursor();
		}
		
		/**
		 * Having the drag and drop details, the method updates the model structure by calling 
		 * the <code>moveLayout</code> from workbench.
		 * This is done only if the tool's state is <code>DRAGGING</code>.
		 * <p>
		 * There are several cases when the move layout must not be applied:
		 * <ul>
		 * 	<il> source and target are the same
		 * 	<il> source is a ViewLayoutData and it is moved over its parent (target) and:
		 * 		- is a single ViewLayoutData
		 * 		- target is over tab bar and source is the last child
		 * 		- target index corresponds to source tab index and target is over tab navigator, in center
		 * 		- target index corresponds to source tab index and target is over tab
		 * </ul>
		 * The tool's state and details are set to their default values at the end.
		 * 
		 * 
		 */
		private function mouseUpHandler(event:MouseEvent):void {
			if (arrangeState != DRAGGING) {
				return;
			}			
			var isAllowed:Boolean = true;
			// don't allow if source and target are the same
			if (dragStartLayoutData == dropEndLayoutData) { 
				isAllowed = false;
			// if source is a ViewLayoutData and it is moved over its parent (target)
			} else if (dragStartLayoutData is ViewLayoutData && 
					dropEndLayoutData is StackLayoutData && 
					dropEndLayoutData.children.contains(dragStartLayoutData)) { 
				
				// don't allow if single ViewLayoutData
				if (dropEndLayoutData.children.length == 1) { 
					isAllowed = false;
				// don't allow if the target is over tab bar and the source is the last child
				} else if (dropEndType == OVER_TAB_BAR && dropEndLayoutData.children.getItemIndex(dragStartLayoutData) == dropEndLayoutData.children.length - 1) {
					isAllowed = false;
				// if the target index corresponds to source tab index				
				} else if (dropEndTabIndex != -1 && dragStartLayoutData == dropEndLayoutData.children.getItemAt(dropEndTabIndex)) {
					// don't allow if target is over tab navigator, in center
					if (dropEndTabNavigatorSide == CENTER)
						isAllowed = false;
					// don't allow if the target is over tab
					if (dropEndType == OVER_TAB)
						isAllowed = false;
				}
			}		
		
			if (isAllowed) {
				// get correct parameters for method
				var side:Number = NONE;
				var index:Number = -1;
				if (dropEndType == OVER_TAB) {
					index = dropEndTabIndex;
				} else {
					index = dropEndLayoutData.children.length;
				}
				if (dropEndTabNavigatorSide != CENTER && dropEndTabNavigatorSide != NONE) {
					side = dropEndTabNavigatorSide;
					index = -1;
				}
				workbench.moveLayout(dragStartLayoutData, dropEndLayoutData, index, side);
			}
			// finished dragging
			arrangeState = IDLE;			
			clear();
		}
		
		/**
		 * Finds and stores the drag* properties based on the mouse position.
		 * There are two different zones where the mouse can be:
		 * 1. under a tab 
		 * 	layout -> <code>ViewLayoutData</code> 
		 * 	type -> OVER_TAB
		 * 2. under a tab bar
		 * 	layout -> <code>StackLayoutData</code> 
		 * 	type -> OVER_TAB_BAR
		 * 
		 * 
		 */ 
		private function computeDragStartDetails(point:Point):void {			
			var result:Object = getLayoutDetailsUnderMouse(point);
			
			dragStartLayoutData = result.layout;
			dragStartType = result.type;
		}
		
		/**
		 * Finds and stores the drop* properties based on the mouse position.
		 * <p>
		 * There are three different zones where the mouse can be:
		 * 1. inside the workbench layout area under a tabNavigator with a layout model attached (SuperTabNavigator)
		 * 	The following cases are found:
		 *  a) mouse is under a tab
		 * 		layout -> ViewLayoutData
		 * 		type -> OVER_TAB
		 * 	b) mouse is under a tab bar (the region without any tabs)
		 * 		layout -> StackLayoutData	
		 * 		type -> OVER_TAB_BAR
		 *  c) mouse is over the tab navigator
		 * 		layout -> StackLayoutData
		 * 		type -> NEW_TAB_NAVIGATOR
		 * 
		 * 2. inside the workbench layout area under objects without any layout models (HDividedBox, VDividedBox, etc.)
		 * 		layout -> the nearest <code>StackLayoutData</code> found
		 * 		type -> NEW_TAB_NAVIGATOR 
		 * 
		 * 3. outside the workbench layout area (on its border)
		 * 		layout -> the <code>WorkbenchLayoutData</code>
		 * 		type -> NEW_TAB_NAVIGATOR 
		 * <p>
		 * Special case : if the drag component is a tab bar and the drop component is a tab or a tab bar, then the drop component 
		 * will be considered to be a StackLayoutData (for ViewLayoutData we take the parent) and type NEW_TAB_NAVIGATOR.
		 * 
		 * The index and side values are calculated at the end, after knowing the layout data and its type.
		 * Special case : if drag source is a tab/tab bar and target is the parent/same tab bar, then the side is considered to be CENTER 
		 * (no action can be made when dropping an object near it)
		 * 
		 * @returns (stores)
		 * layout -> ViewLayoutData, StackLayoutData
		 * type -> OVER_TAB, OVER_TAB_BAR, NEW_TAB_NAVIGATOR
		 * index -> tab's index
		 * side -> LEFT/RIGHT/UP/DOWN/CENTER
		 * 		
		 * 
		 */ 
		private function computeDropEndDetails(event:MouseEvent):void {	
			dropEndLayoutData = null;
			dropEndTabIndex = -1;
			dropEndTabNavigatorSide = NONE;
			dropEndType = -1;
			
			var mousePoint:Point = new Point(event.stageX, event.stageY); 
						
			var result:Object = getLayoutDetailsUnderMouse(mousePoint);
			if (result.layout != null) { // zone 1				
				dropEndLayoutData = result.layout;
				dropEndType = result.type;	
				// calculate index for over tab type and consider the parent (StackLayoutData) to be the drop layout data (not ViewLayoutData)
				if (dropEndType == OVER_TAB) {			
					dropEndTabIndex = dropEndLayoutData.parent.children.getItemIndex(dropEndLayoutData);					
					dropEndLayoutData = dropEndLayoutData.parent;
				}									
			} else { 	
				// verify if the mouse is outside the layout area (zone 3)				
				var layoutAreaPosition:Point = workbench.layoutArea.localToGlobal(new Point(workbench.layoutArea.x, workbench.layoutArea.y));
				var layoutAreaRect:Rectangle = new Rectangle(layoutAreaPosition.x, layoutAreaPosition.y, workbench.layoutArea.width, workbench.layoutArea.height);
				if (!layoutAreaRect.containsPoint(mousePoint)) {				
					var arr:Array = workbench.root.stage.getObjectsUnderPoint(mousePoint);
					for (var i:Number = arr.length - 1; i >= 0; i--) {			
						var obj:DisplayObject = arr[i];
						var layoutDataFound:Boolean = false;
						while (obj != null) {					
							if (obj is Workbench) {
								dropEndLayoutData = workbench.rootLayout;
								break;
							}
							obj = obj.parent;
						}					
					}
				} else { // zone 2
					dropEndLayoutData = getNearestStackLayoutData(mousePoint);
				}				
				dropEndType = NEW_TAB_NAVIGATOR;
			}			
			// special case
			if (dragStartType == OVER_TAB_BAR) {
				dropEndType = NEW_TAB_NAVIGATOR;
				if (dropEndLayoutData is ViewLayoutData) {
					dropEndLayoutData = dropEndLayoutData.parent;
				}
			}			
			// calculate side
			dropEndTabNavigatorSide = NONE;
			if (dropEndType == NEW_TAB_NAVIGATOR) {
				var dropComponent:UIComponent = workbench.layoutDataToComponent[dropEndLayoutData];					
				var point:Point = dropComponent.localToGlobal(new Point(dropComponent.stage.x, dropComponent.stage.y));
				var rect:Rectangle = new Rectangle(point.x, point.y, dropComponent.width, dropComponent.height);
				
				dropEndTabNavigatorSide = getSide(mousePoint, rect);
				// special case
				if ((dragStartLayoutData == dropEndLayoutData || dropEndLayoutData.children.contains(dragStartLayoutData)) &&
					dropEndLayoutData.children.length == 1) {
					dropEndTabNavigatorSide = CENTER;
				}
			}			
		}
		
		/**
		 * Based on the <code>dropEndType</code> value, it updates the placeHolder and mouse cursor in the following way:
		 * <ul>
		 * 	<li> NEW_TAB_NAVIGATOR - given the drop end component position/coordonates and the drop side value creates a rectangle
		 * and sets the corresponding mouse cursor (left/right/up/down).TODO ??? center
		 * 	<li> OVER_TAB_BAR - creates a rectangle at the end of tabs having the height the last tab height and a default width.
		 * 	<li> OVER_TAB - creates a rectangle over the drop end tab component having its position and dimensions.
		 * </ul> 
		 * 
		 */
		private function updateFeedbackRectangleAndCursor():void {
			var dropEndComponent:UIComponent = workbench.layoutDataToComponent[dropEndLayoutData];
			// create it if doesn't exist and add it to workbench
			if (placeholder == null) {
				placeholder = new MoveResizePlaceHolder();
				placeholder.setColorAndAlpha(0x00007F, 0.4);
				workbench.addChild(placeholder);
			}
			var newCursor:Class;
					
			// update the placeHolder coordonates and dimensions based on the drop details	
			switch(dropEndType) {
				case NEW_TAB_NAVIGATOR: 
					// set the placeHolder based on the drop component's coordonates/dimensions
					// get drop component global coordonates
					var point:Point = workbench.globalToContent(dropEndComponent.localToGlobal(new Point(dropEndComponent.stage.x, dropEndComponent.stage.y)));
					placeholder.x = point.x;
					placeholder.y = point.y;
					placeholder.width = dropEndComponent.width;
					placeholder.height = dropEndComponent.height;
					
					var percent:Number = Workbench.LAYOUT_CHILD_PERCENT/100;
					if (dropEndLayoutData is WorkbenchLayoutData) {
						percent = workbench.workbenchChildPercent/100;
					}				
					// based on the side, place it on left/right/up/down
					switch (dropEndTabNavigatorSide) {
						case UP :
							placeholder.height *= percent;
							newCursor = topArrow;														
							break;
						case DOWN :						
							placeholder.y += placeholder.height*(1-percent);
							placeholder.height *= percent;	
							newCursor = bottomArrow;												
							break;
						case LEFT :
							placeholder.width *= percent;				
							newCursor = leftArrow;										
							break;
						case RIGHT :
							placeholder.x += placeholder.width*(1-percent);
							placeholder.width *= percent;	
							newCursor = rightArrow;											
							break;
						case CENTER :
							newCursor = centerCursor;							
							break;
					}
					break;
				case OVER_TAB_BAR:
					// if it's dropped over an editor without children, then the placeholder is placed as the first tab
					if (dropEndLayoutData.children.length == 0 && SashLayoutData(dropEndLayoutData.parent).isEditor) {
						point = workbench.globalToContent(dropEndComponent.localToGlobal(new Point(dropEndComponent.stage.x, dropEndComponent.stage.y)));
						placeholder.x = point.x;
						placeholder.y = point.y;
						placeholder.width = dropEndComponent.width;
						placeholder.height = dropEndComponent.height;
					} else {
						var lastTab:Button = SuperTabNavigator(dropEndComponent).getTabAt(dropEndLayoutData.children.length - 1);
						point = workbench.globalToContent(lastTab.localToGlobal(new Point(lastTab.stage.x, lastTab.stage.y)));
						placeholder.x = point.x + lastTab.width;
						placeholder.y = point.y;
						placeholder.width = 50;
						placeholder.height = lastTab.height;
					}
					newCursor = centerCursor;					
					break;
				case OVER_TAB:
					 //OVER_TAB : set the placeHolder to have the tab coordonates/dimensions 				
					var tab:Button = SuperTabNavigator(workbench.layoutDataToComponent[dropEndLayoutData]).getTabAt(dropEndTabIndex);
					// get the tab global coordonates
					point = workbench.globalToContent(tab.localToGlobal(new Point(tab.stage.x, tab.stage.y)));
					// set the placeHolder dimensions
					placeholder.x = point.x;
					placeholder.y = point.y;
					placeholder.width = tab.width;
					placeholder.height = tab.height;
					newCursor = centerCursor;
					break;				
			}
			if (newCursor != currentCursor || CursorManager.currentCursorID == CursorManager.NO_CURSOR) {
				CursorManager.removeAllCursors();
				switch (newCursor) {
					case leftArrow:
						CursorManager.setCursor(newCursor, 1, 0, -5.5);
						break;
					case rightArrow:
						CursorManager.setCursor(newCursor, 1, -11, -5.5);
						break;
					case bottomArrow:
						CursorManager.setCursor(newCursor, 1, -5.5, -11);
						break;
					case topArrow:
						CursorManager.setCursor(newCursor, 1, -4, 0);
						break;
					case centerCursor:
						CursorManager.setCursor(newCursor, 1, -6, -6);
				}
				currentCursor = newCursor;
			}
		}
		
		/**
		 * Given a point, this method knows how to return the associated graphical component found under that point
		 * that has a layoutData. It is intended to be used when moving then mouse inside the workbench layout area.
		 * It makes the following steps:
		 * <ul>
		 * 	<li> iterates through all objects found under given point
		 * 	<li> for each object, iterates up on its parents to find the component that has a layoutData (it can be a tab, tab bar or a tab navigator)
		 * 	<li> if a tabNavigator is found on the way, verifies if at least one object under the mouse was found and has a layout data; if <code>false</code> searches for another tabNavigator.
		 * 	<li> returns the results in an object (see bellow its structure).
		 * 	<li> if no object found, returns a simple object will layout null.
		 * </ul> 
		 * 
		 * The following cases can appear:
		 * 1. mouse is found under a component that has a layout model attached
		 * 	a) mouse is over a tab
		 * 		layout -> ViewLayoutData
		 * 		type -> OVER_TAB
		 * 	b) mouse is over a tab bar
		 * 		layout -> StackLayoutData
		 * 		type -> OVER_TAB_BAR
		 * 	c) mouse is over the tab navigator
		 * 		layout -> StackLayoutData
		 * 		type -> NEW_TAB_NAVIGATOR
		 * 2. mouse is found under a component that hasn't a layout model attached
		 * 		layout -> null
		 * 		type -> not set
		 * 	
		 * @returns 
		 * An object having the following structure:
		 * <ul>
		 * 	<li> .layout - the associated layout model under mouse
		 * 	<li> .type - OVER_TAB/OVER_TAB_BAR/NEW_TAB_NAVIGATOR
		 * </ul>
		 * 
		 * @see computeDragStartDetails()
		 * @see computeDropEndDetails()
		 * 
		 * 
		 */
		public function getLayoutDetailsUnderMouse(point:Point):Object {			
			var arr:Array = workbench.root.stage.getObjectsUnderPoint(point);
			
			var result:Object = new Object();			
			for (var i:Number = arr.length - 1; i >= 0; i--) {
				var obj:DisplayObject = arr[i];
				
				// objects used to trace the correct graphical component 
				var cachedTab:SuperTab = null;
				var cachedTabBar:SuperTabBar = null;
				var cachedCanvas:ButtonScrollingCanvas = null;
								
				while (obj != null) {														
					if (obj is SuperTab) { // a tab was found in hierarchy
						cachedTab = SuperTab(obj);
					}
					if (obj is SuperTabBar) { // a tab bar found
						cachedTabBar = SuperTabBar(obj);
					}
					if (obj is ButtonScrollingCanvas) { // a canvas found
						cachedCanvas = ButtonScrollingCanvas(obj);
					}
					if (obj is SuperTabNavigator) { // a tab navigator found						
						result = new Object();						
						var graphicalObject:DisplayObject = obj;
						// if a tab and tabBar already detected, the point is positioned under a tab
						if (cachedTab != null && cachedTabBar != null) {
							graphicalObject = SuperTabNavigator(obj).getChildAt(SuperTabBar(cachedTabBar).getChildIndex(cachedTab));							
							result.type = OVER_TAB;								
						} else if (cachedCanvas != null) {
							// if only a canvas detected, the point is positioned under a tab bar
							result.type = OVER_TAB_BAR;							
						} else { 
							result.type = NEW_TAB_NAVIGATOR;
						}
						
						var layoutData:LayoutData = workbench.componentToLayoutData[graphicalObject];											
						// the component must have a layoutData attached
						if (layoutData != null) {
							result.layout = layoutData;							
							return result;
						}
						// clear the cached details, maybe another tab navigator will be found
						cachedTab = null;
						cachedTabBar = null;
						cachedCanvas = null;
					}
					// move up
					obj = obj.parent;
				}
			}
			// no object with a layoutData found -> result.layout = null
			return result;
		}
		
		/**
		 * Returns the nearest <code>StackLayoutData</code> based on the mouse position.
		 * The nearest layout is calculated in the following way: for each StackLayoutData component (rectangle), 
		 * gets the minimum distance between its edges and given point and compares it with a global min value.
		 * 
		 * At the end, the corresponding <code>StackLayoutData</code> for the global min value will be returned.
		 */
		private function getNearestStackLayoutData(point:Point):StackLayoutData {			
			// the mouse is inside layout area, search for the nearest StackLayoutData				
			// get all StackLayoutData objects
			var stacks:ArrayCollection = new ArrayCollection();
			workbench.getAllStackLayoutData(workbench.rootLayout, stacks);
					
			var nearestDistance:Number;
			var nearestStackLayout:StackLayoutData = null;
			// get the nearest StackLayoutData
			for each (var stackLayout:StackLayoutData in stacks) {
				// don't take in consideration a minimized stacks
				if (stackLayout.mrmState == StackLayoutData.FORCED_MINIMIZED || stackLayout.mrmState == StackLayoutData.USER_MINIMIZED) {
					continue;
				}
				var tabNavigator:SuperTabNavigator = workbench.layoutDataToComponent[stackLayout];
				// get the object's coordonates in a rectangle
				var globalCoords:Point = tabNavigator.localToGlobal(new Point(tabNavigator.stage.x, tabNavigator.stage.y));							
				// get the minimum distance from the point to rectangle
				var minDistance:Number = getDistanceToRectangle(
							point, 
							new Rectangle(globalCoords.x, globalCoords.y, tabNavigator.width, tabNavigator.height - 3));
				// store it if it is the nearest object until now
				if (nearestStackLayout == null) {
					nearestDistance = minDistance;
					nearestStackLayout = stackLayout;
				} else {								
					if (nearestDistance > minDistance) {
						nearestDistance = minDistance;
						nearestStackLayout = stackLayout;
					}
				}			
			}
			return nearestStackLayout;	
		}				
		
		/**
		 * Returns the minimum distance from a rectangle to a point found outside.
		 * The distance is calculated different based on the point position.
		 * There are 9 areas where the point can be:
		 * 5|	1	|6
		 * -----------
		 *  |		| 
		 * 4|	0	| 2
		 *  |		|
		 *  |		|
		 * -----------
		 * 8|	3	|7
		 * The 0 area means that the point is inside the rectangle. It will not be interpreted because it is not our case.
		 * 
		 * 
		 */ 
		private function getDistanceToRectangle(point:Point, rect:Rectangle):Number {			
			if (rect.containsPoint(point)) {
				return 0;
			}
			// for 5-8 areas the minimum distance is calculated based on the nearest rectangle point (topLeft/topRight/bottomLeft/bottomRight) 
			// area 5 - topLeft
			if (point.x < rect.x && point.y < rect.y) {
				return Math.abs(point.x - rect.x) + Math.abs(point.y - rect.y);	
			}
			// area 6 - topRight
			if (point.x > rect.x + rect.width && point.y < rect.y) {
				return Math.abs(point.x - rect.x - rect.width) + Math.abs(point.y - rect.y);	
			}
			// area 7 - bottomLeft
			if (point.x > rect.x + rect.width && point.y > rect.y + rect.height) {
				return Math.abs(point.x - rect.x - rect.width) + Math.abs(point.y - rect.y - rect.height);	
			}
			// area 8 - bottomRight
			if (point.x < rect.x && point.y > rect.y + rect.height) {
				return Math.abs(point.x - rect.x) + Math.abs(point.y - rect.y - rect.height);	
			}	
			// for 1-4 area, the minimum distance is calculated based on the nearest edge
			var a:Point = new Point(rect.x, rect.y);
			var b:Point = new Point(rect.x + rect.width, rect.y);
			var c:Point = new Point(rect.x + rect.width, rect.y + rect.height);	
			var d:Point = new Point(rect.x, rect.y + rect.height);					
			
			return Math.min(
						getDistanceToLine(a, b, point), // area 1 
						getDistanceToLine(b, c, point), // area 2 
						getDistanceToLine(c, d, point), // area 3 
						getDistanceToLine(d, a, point));// area 4 					
		}
		
		/**
		 * Given three points, it returns the distance between the line formed from the first two points (a, b) and the third point (p).
		 * <p>
		 * If the point isn't framed by the line, then an infinite (max int) value is returned.
		 * 
		 * 
		 */ 
		public function getDistanceToLine(a:Point, b:Point, p:Point):Number {
			var xDelta:Number = Math.abs(a.x - b.x);
		    var yDelta:Number = Math.abs(a.y - b.y);
				  		
		    var u:Number = (Math.abs(p.x - a.x) * xDelta + Math.abs(p.y - a.y) * yDelta) / (xDelta * xDelta + yDelta * yDelta);
		
		    var closestPoint:Point;
		    if (u < 0 || u > 1) {
		      return int.MAX_VALUE;		    
		    } else {
		    	var normalLength:Number = Math.sqrt((b.x - a.x) * (b.x - a.x) + (b.y - a.y) * (b.y - a.y));
   				return Math.abs((p.x - a.x) * (b.y - a.y) - (p.y - a.y) * (b.x - a.x))/normalLength;		      
		    }
		}
		
		/**
		 * Based on a point and a rectangle, return the side in the rectangle where the point is positioned.
		 * <p>
		 * Calculates the minimum distance from the point to each rectangle edge. If the distance is greater than the SIDE_PADDING, the point is considered to be in CENTER.
		 * Otherwise, based on the corresponding min distance edge, returns the correct side (LEFT, RIGHT, UP, DOWN).  
		 * 
		 * 
		 */ 
		private function getSide(point:Point, rect:Rectangle):Number {				
			var a:Point = new Point(rect.x, rect.y);
			var b:Point = new Point(rect.x + rect.width, rect.y);
			var c:Point = new Point(rect.x + rect.width, rect.y + rect.height);	
			var d:Point = new Point(rect.x, rect.y + rect.height);	
							
			var ab:Number = getDistanceToLine(a, b, point);
			var bc:Number = getDistanceToLine(b, c, point);
			var cd:Number = getDistanceToLine(c, d, point);
			var da:Number = getDistanceToLine(d, a, point);
			
			var	tmpMin:Number = Math.min(ab, bc, cd, da);
			if (tmpMin > SIDE_PADDING) {
				return CENTER;
			}
			switch (tmpMin) {
				case ab :
					return UP;						
				case bc :
					return RIGHT;						
				case cd :
					return DOWN;						
				default :
					return LEFT;						
			}
		}
		
		/**
		 * Clears the drag* drop* details and removes the placeHolder from workbench.
		 * Used when deactivating the tool or setting the <code>IDLE</code> state to be the current state of the tool. 
		 * 
		 * 
		 */ 
		private function clear():void {
			dragStartLayoutData = null;
			dragStartType = -1;
			
			dropEndLayoutData = null;
			dropEndTabIndex = -1;
			dropEndTabNavigatorSide = NONE;
			dropEndType = -1;
			
			moveState = -1;
			// remove placeHolder if added on stage
			if (placeholder != null) {
				workbench.removeChild(placeholder);
				placeholder = null;
			}
			CursorManager.removeAllCursors();
		}
	}
	
}