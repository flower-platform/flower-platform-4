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
		
	import com.crispico.flower.flexdiagram.util.tabNavigator.FlowerSuperTab;
	import com.crispico.flower.util.UtilAssets;
	import com.crispico.flower.util.layout.actions.CloseAction;
	import com.crispico.flower.util.layout.actions.MaximizeAction;
	import com.crispico.flower.util.layout.actions.MinimizeAction;
	import com.crispico.flower.util.layout.actions.MoveAction;
	import com.crispico.flower.util.layout.actions.RestoreAction;
	import com.crispico.flower.util.layout.actions.UndockAction;
	import com.crispico.flower.util.layout.event.DockHandlerEvent;
	import com.crispico.flower.util.layout.event.LayoutDataChangedEvent;
	import com.crispico.flower.util.layout.event.ViewAddedEvent;
	import com.crispico.flower.util.layout.persistence.ILayoutSerializer;
	import com.crispico.flower.util.layout.persistence.SashLayoutData;
	import com.crispico.flower.util.layout.persistence.StackLayoutData;
	import com.crispico.flower.util.layout.persistence.WorkbenchLayoutData;
	import com.crispico.flower.util.layout.persistence.XMLStringLayoutSerializer;
	import com.crispico.flower.util.layout.view.LayoutDividedBox;
	import com.crispico.flower.util.layout.view.LayoutTabNavigator;
	import com.crispico.flower.util.layout.view.MinimizedStackBar;
	import com.crispico.flower.util.layout.view.ViewPopupWindow;
	import com.crispico.flower.util.layout.view.activeview.ActiveViewList;
	
	import flash.display.DisplayObject;
	import flash.events.IEventDispatcher;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.utils.Dictionary;
	
	import flexlib.containers.SuperTabNavigator;
	
	import mx.collections.ArrayCollection;
	import mx.containers.BoxDirection;
	import mx.containers.Canvas;
	import mx.containers.DividedBox;
	import mx.containers.HBox;
	import mx.containers.HDividedBox;
	import mx.containers.VBox;
	import mx.containers.VDividedBox;
	import mx.core.Container;
	import mx.core.EventPriority;
	import mx.core.FlexGlobals;
	import mx.core.IChildList;
	import mx.core.UIComponent;
	import mx.core.mx_internal;
	import mx.events.FlexEvent;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.action.ComposedAction;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.IActionProvider;
	import org.flowerplatform.flexutil.context_menu.FillContextMenuEvent;
	import org.flowerplatform.flexutil.layout.IViewProvider;
	import org.flowerplatform.flexutil.layout.IWorkbench;
	import org.flowerplatform.flexutil.layout.LayoutData;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.flexutil.layout.event.ViewRemovedEvent;
	import org.flowerplatform.flexutil.layout.event.ViewsRemovedEvent;

	import org.flowerplatform.flexutil.shortcut.Shortcut;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;

	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	import org.flowerplatform.flexutil.shortcut.KeyBindings;
	import org.flowerplatform.flexutil.shortcut.Shortcut;
	
	import spark.components.NavigatorContent;

	use namespace mx_internal;
	
	[Event(name="click", type="com.crispico.flower.util.layout.event.DockHandlerEvent")]
	
	/**
	 * A graphical component that uses a given layout to fill its content with views.
	 * Provides the following support for handling content:
	 * <ul>
	 * 	<li> drag&drop views from a parent to another
	 * 	<li> minimize/maximize/restore views
	 * 	<li> keeping a history of views dimensions in order to reuse them
	 * 	<li> addind/removing views
	 * 	<li> keeping a history of last active (focused) views
	 * 	<li> context menu mechanism implemented for each view
	 * </ul>
	 * 
	 * <p>
	 * In order to work with this class, an <code>IViewProvider</code> must be set.
	 *  
	 * @author Cristina 
	 * 
	 */
	[SecureSWF(rename="off")]
	public class Workbench extends Canvas implements IWorkbench {
		
		/**
		 * Represents the percent applied to newly added child on workbench.
		 * Used by DND functionality to positionate correctly the drag component over workbench.
		 */ 
		public static const DEFAULT_WORKBENCH_CHILD_PERCENT:Number = 25;
		
		/**
		 * Represents the percent applied to newly added child on layout area.
		 * Used by DND functionality to positionate correctly the drag component over drop element.
		 */
		public static const LAYOUT_CHILD_PERCENT:Number = 50;
		
		/**
		 * 
		 */
		public var padding:Number = 3;
		
		/**
		 * The initial min width for a <code>ViewPopupWindow</code>.
		 * 
		 * <p>
		 * It is used when undock-ing and only only if the graphical component has a smaller width.		
		 */ 
		public var viewPopup_initialMinWidth:Number = 700;
		
		/**
		 * The initial min height for a <code>ViewPopupWindow</code>.
		 * 
		 * <p>
		 * It is used when undock-ing and only if the graphical component has a smaller height.
		 */ 
		public var viewPopup_initialMinHeight:Number = 400;
		
		/**
		 * Used to serialize/deserialize the <code>rootLayout</code>.
		 * By default, it's set to <code>XMLStringLayoutSerializer</code>. <br>
		 * It can be modified using the corresponding setter.
		 * 
		 * 
		 */
		private var _layoutSerializer:ILayoutSerializer = new XMLStringLayoutSerializer();
		
		/**
		 * @see Getter doc.
		 * 
		 */
		private var _viewProvider:IViewProvider;
		
		private var _activeViewList:ActiveViewList;
		
		/**
		 * 
		 */		
		private var _rootLayout:WorkbenchLayoutData;
		
		/**
		 * @see Getter doc.
		 * 
		 */
		private var _layoutDataToComponent:Dictionary;
		
		/**
		 * @seem Getter doc.
		 * 
		 */
		private var _componentToLayoutData:Dictionary;
		
		/**
		 * 
		 */
		private var leftToolbarArea:VDividedBox;
		
		/**
		 * 
		 */
		private var bottomToolbarArea:HDividedBox;
		
		/**
		 * 
		 */
		private var rightToolbarArea:VDividedBox;
		
		/**
		 * @see Getter doc.
		 * 
		 */
		private var _layoutArea:Canvas;
		
		/**
		 * 
		 */
		private var _arrangeTool:ArrangeTool;
		
		/**
		 * Keeps the selected <code>ViewLayoutData</code> to be used by context menu mechanism.
		 * 
		 */ 
		public var selectedViewLayoutData:ViewLayoutData;
		
		private var _workbenchChildPercent:Number = DEFAULT_WORKBENCH_CHILD_PERCENT;

		/**
		 * Default actions for the right click menu on a tab name
		 * 
		 * @author Mircea Negreanu
		 */
		private var _actions:Vector.<IAction> = new Vector.<IAction>();
		
		/**		
		 * Initializes the objects and adds required listeners.
		 * 	  
		 * @author Sebastian Solomon
		 * @author Cristina
		 * @author Mircea Negreanu
		 */
		public function Workbench() {			
			_componentToLayoutData = new Dictionary();
			_layoutDataToComponent = new Dictionary();
			
			_activeViewList = new ActiveViewList(this);
						
			addEventListener("rightClick", rightClickHandler);			
		
			addEventListener(DockHandlerEvent.CLICK, viewPopup_dockClickHandler, false, EventPriority.DEFAULT_HANDLER);

			addEventListener(FillContextMenuEvent.FILL_CONTEXT_MENU, fillContextMenuHandler);
			
			// Adds CTRL+M as shortcut to maximize/minimize the active view layout data.
			//(new KeyBindings()).registerBinding(new Shortcut(true, false, "m"), maximizeRestoreActiveStackLayoutData);
			FlexUtilGlobals.getInstance().keyBindings.registerBinding(new Shortcut(true, false, "m"), maximizeRestoreActiveStackLayoutData); // CTRL + M
			
			// prepare default actions for the right click menu on a tab name
			fillActions();
		}
		
		/**
		 * Fills data for context menu (actions and selection).
		 * <p>
		 * If the viewProvider wants to add something it needs to return an actionProvider for viewProvider.getTabCustomizer()</p>
		 * 
		 * @author Mircea Negreanu
		 */
		private function fillContextMenuHandler(event:FillContextMenuEvent):void {
			if (selectedViewLayoutData != null) {
				var actions:Vector.<IAction>;
				
				// interogate the tabCustomizer
				var tabCustomizer:Object = _viewProvider.getTabCustomizer(selectedViewLayoutData);
				
				var tabCustomizerActions:Vector.<org.flowerplatform.flexutil.action.IAction>;
				if (tabCustomizer != null && tabCustomizer is IActionProvider) {
					tabCustomizerActions = IActionProvider(tabCustomizer).getActions(new ArrayCollection([selectedViewLayoutData]));
				}
				
				if (tabCustomizerActions != null && tabCustomizerActions.length > 0) {
					// comasate with our actions
					actions = new Vector.<IAction>();
					actions.concat(_actions);
					actions.concat(tabCustomizerActions);
				} else { 
					// only ours
					actions = _actions;
				}
				
				event.allActions = actions;
				event.selection = new ArrayCollection([selectedViewLayoutData]);
			} else {
				event.allActions = null;
			}
		}
		
		/**
		 * Just add actions from the right click on a tab
		 * <ul>
		 * <li>Undock</li> 
		 * <li>Move</li>
		 * <li>Minimize</li> 
		 * <li>Maximize</li>
		 * <li>Close</li>
		 * <li>Close All</li> 
		 * <li>Close Others</li>
		 * </ul>
		 * 
		 * @author Mircea Negreanu
		 */
		private function fillActions():void {
			_actions.push(new UndockAction(this));
			
			var action:ActionBase = new ComposedAction();
			action.label = UtilAssets.INSTANCE.getMessage("layout.action.move");
			action.icon = UtilAssets.INSTANCE._moveViewIcon;
			action.orderIndex = 20;
			action.id = "layout.action.move";
			_actions.push(action);
			
			action = new MoveAction(this, MoveAction.VIEW);
			action.parentId = "layout.action.move";
			_actions.push(action);
			
			action = new MoveAction(this, MoveAction.GROUP);
			action.parentId = "layout.action.move";
			_actions.push(action);
			
			_actions.push(new MinimizeAction(this));
			_actions.push(new MaximizeAction(this));
			_actions.push(new RestoreAction(this));
			_actions.push(new CloseAction(this, CloseAction.CLOSE));
			_actions.push(new CloseAction(this, CloseAction.CLOSE_OTHERS));
			_actions.push(new CloseAction(this, CloseAction.CLOSE_ALL));
		}

		/**
		 * Workbench has the following structure:
		 * <ul>
		 * 	<li> VBox
		 * 		<ul>
		 * 			<li> HBox
		 * 				<ul>
		 * 					<li> VDividedBox -> left area
		 * 					<li> Canvas -> layout area
		 * 					<li> VDividedBox -> right area
		 * 				</ul>
		 * 			<li> VDividedBox -> bottom area
		 * 		</ul>
		 * 	</ul>
		 * 
		 * <p>
		 * Padding is applied to <code>VBox</code> in order to set an empty area on each sides that will be used by
		 * the <code>_arrangeTool</code> to determined the exact drop operation. <br>
		 * Based on the operation, the drop will be made under some layout area component or under the workbench area.
		 * 
		 * 
		 */ 
		override protected function createChildren():void {
			super.createChildren();
						
			_layoutArea = new Canvas();
			_layoutArea.percentWidth = 100;
			_layoutArea.percentHeight = 100;		
			
			leftToolbarArea = new VDividedBox();
			leftToolbarArea.setStyle("verticalGap", 5);	
			rightToolbarArea = new VDividedBox();
			rightToolbarArea.setStyle("verticalGap", 5);			
			bottomToolbarArea = new HDividedBox();
			bottomToolbarArea.setStyle("horizontalGap", 5);	
						
			var hBox:HBox = new HBox();
			hBox.percentWidth = 100;
			hBox.percentHeight = 100;
			hBox.setStyle("horizontalGap", 0);
			hBox.addChild(leftToolbarArea);
			hBox.addChild(_layoutArea);
			hBox.addChild(rightToolbarArea);
						
			var vBox:VBox = new VBox();
			vBox.setStyle("verticalGap", 0);
			vBox.setStyle("paddingRight", padding);
			vBox.setStyle("paddingLeft", padding);
			vBox.setStyle("paddingTop", padding);
			vBox.setStyle("paddingBottom", padding);
			
			vBox.percentWidth = 100;
			vBox.percentHeight = 100;		
			
			vBox.addChild(hBox);
			vBox.addChild(bottomToolbarArea);		
						
			addChild(vBox);			
		}
		
		public function get arrangeTool():ArrangeTool {
			return this._arrangeTool;
		}
		
		/**
		 * 
		 */
		public function get rootLayout():LayoutData {
			return this._rootLayout;
		}
		
		public function get activeViewList():ActiveViewList {
			return this._activeViewList;
		}
						
		/**
		 * @see ILayoutSerializer
		 * 
		 */
		public function set layoutSerializer(value:ILayoutSerializer):void {
			this._layoutSerializer = value;
		}
		
		/**
		 * Represents the graphical components provider for <code>ViewLayoutData</code> objects.
		 * 
		 * <p>
		 * Used to get graphical information regarding a given view: 
		 * graphical component, label, image etc.
		 * 
		 * @see IViewProvider
		 */ 
		public function get viewProvider():IViewProvider {
			return this._viewProvider;
		}
		
		/**
		 * 
		 */
		public function set viewProvider(value:IViewProvider):void {
			this._viewProvider = value;
		}
		
		/**
		 * Represents the container used to store the content of <code>_rootLayout</code> graphical structure.
		 * 
		 * <p>
		 * Intended to be used by the <code>_arrangeTool</code> to know 
		 * on which graphical component to listen for mouse events.
		 * 
		 * 
		 */
		public function get layoutArea():Container {			
			return _layoutArea;
		}
		
		/**		
		 * Keeps an association from the graphical component to the layoutData. <br>
		 * It is updated when adding/removing graphical components from layout area.
		 * 
		 * 
		 */
		public function get componentToLayoutData():Dictionary {
			return _componentToLayoutData;
		}
		
		/**
		 * Keeps an association from the layoutData to the graphical component.
		 * It is updated when adding/removing graphical components from layout area.
		 * 
		 * 
		 */
		public function get layoutDataToComponent():Dictionary {
			return _layoutDataToComponent;
		}
		
		public function get workbenchChildPercent():Number {
			return this._workbenchChildPercent;
		}
				
		public function set workbenchChildPercent(value:Number):void {
			this._workbenchChildPercent = value;
		}
		
		/**
		 * Represents the main entry point for loading the workbench content. <br>
		 * Must be called when loading a certain layout structure.
		 * 
		 * <p>
		 * Receives as parameter an <code>Object</code> which is interpreted in the following way:
		 * <ul>
		 * 	<li> if is a <code>LayoutData</code> then it assigns it to <code>_rootLayout</code> and uses it;
		 * 	<li> if not, it uses <code>_layoutSerializer</code> to deserialize the information and stores it in <code>_rootLayout</code>.
		 * 
		 * <p>
		 * If <code>_rootLayout</code> is already populated, then a clean is done first. <br>
		 * If <code>reuseExistingView</code> is set to <code>true</code>,
		 * existing components from old layout are stored in a <code>RecycledViews</code> object be used in the following way:
		 * <ul>
		 * 	<li> in case of a normal view (non-editor) -> reuses the old component if the new layout must load one with the same id.
		 *  <li> in case of an editor view -> the old component will be added in the new editor sash component.
		 * </ul>
		 * 
		 * <p>
		 * If <code>keepNewLayoutEditors</code> is set to <code>true</code>,
		 * editors from new layout aren't removed.
		 * 
		 * <p>
		 * Creates the visual content for the given layout.
		 * <p> 
		 * At the end, activates the drag&drop tool.
		 * 
		 * @see loadVisualWorkbench()
		 * @see clearVisualWorkbench()
		 * 
		 * 
		 */
		public function load(layoutData:Object, reuseExistingViews:Boolean = false, keepNewLayoutEditors:Boolean = false):void {
			var recycledViews:RecycledViews = new RecycledViews();
			if (_rootLayout != null) {
				// fills recycled maps with old views
				for (var key:* in _layoutDataToComponent) {
					if (key is ViewLayoutData && !ViewLayoutData(key).isUndocked) { // reuse only docked views
						var view:ViewLayoutData = ViewLayoutData(key);
						if (view.isEditor) {
							recycledViews.editorViews.addItem(new RecycledViewEntry(view, _layoutDataToComponent[view]));
						} else {
							recycledViews.normalViews[RecycledViews.getNormalViewKey(view)] = new RecycledViewEntry(view, _layoutDataToComponent[view]);
						}
					}
				}			
				if (!reuseExistingViews) { // if don't reuse existing views, then dispatch now the remove events
					dispatchRemoveEventsOnLoad(recycledViews);
				}
				// cleans the workbench
				clearVisualWorkbench();
			}
			// sets the new root layout structure
			if (layoutData is LayoutData) {
				_rootLayout = WorkbenchLayoutData(layoutData);				
			} else {			
				_rootLayout = WorkbenchLayoutData(_layoutSerializer.deserialize(String(layoutData)));				
			}			
			
			if (!keepNewLayoutEditors) {
				// remove all editiors from new layout
				removeLayoutEditorsFromLayout();
			}
			// loads the new graphical content 
			loadVisualWorkbench(reuseExistingViews, recycledViews);
						
			if (reuseExistingViews) { // if reuse existing views, dispatch remove event for those who weren't reused after load
				dispatchRemoveEventsOnLoad(recycledViews);
			}
			// activate drag&drop tool
			if (_arrangeTool == null) {
				_arrangeTool = new ArrangeTool();
				_arrangeTool.activate(this);
			}
			
			dispatchEvent(new LayoutDataChangedEvent());
		}
				
		/**
		 * Removes all <code>rootLayout</code> editors. <br>
		 * Uses only the layout structure.
		 * 
		 * @see load()
		 */ 
		private function removeLayoutEditorsFromLayout():void {
			var editors:ArrayCollection = new ArrayCollection();
			getAllViewLayoutData(_rootLayout, editors, true);
			while (editors.length > 0) {
				removeEditorLayout(LayoutData(editors.getItemAt(0)));
				
				editors.removeAll();
				getAllViewLayoutData(_rootLayout, editors, true);
			}		
		}
		
		/**
		 * Removes the <code>layoutData</code> from <code>rootLayout</code>.
		 * 
		 * @see load()
		 */ 
		private function removeEditorLayout(layoutData:LayoutData):void {
			var parent:LayoutData = layoutData.parent;
			var index:Number = parent.children.getItemIndex(layoutData);				
			if (!(layoutData is ViewLayoutData)) {
				var ratio:Number = SashLayoutData(parent).ratios[index];
				var mrmRatio:Number = SashLayoutData(parent).mrmRatios[index];
			}
			
			// verify if single editor
			if (layoutData is SashLayoutData && SashLayoutData(layoutData).isEditor ||
				(layoutData is StackLayoutData && SashLayoutData(parent).isEditor)) {
				var parentEditor:SashLayoutData = (layoutData is SashLayoutData) ? SashLayoutData(layoutData) : SashLayoutData(parent);
				var editors:ArrayCollection = new ArrayCollection();
				getAllSashEditorLayoutData(_rootLayout, editors);
				if (editors.length == 1 && parentEditor.children.length == 1) {					
					return;
				}
			}	
			// update ratios and mrmRatios
			if (layoutData.parent is SashLayoutData) {
				var newRatios:ArrayCollection = new ArrayCollection();
				var newMrmRatios:ArrayCollection = new ArrayCollection();	
				
				var numRatios:int = 0;
				var numMrmRatios:int = 0;
				for (var i:int = 0; i < SashLayoutData(parent).ratios.length; i++) {
					if (i != index && SashLayoutData(parent).ratios.getItemAt(i) != 0) {
						numRatios++;
					}
				}
				for (var j:int = 0; j < SashLayoutData(parent).mrmRatios.length; j++) {
					if (j != index && SashLayoutData(parent).mrmRatios.getItemAt(j) != 0) {
						numMrmRatios++;
					}
				}
				for (var k:int = 0; k < SashLayoutData(parent).children.length; k++) {
					if (k != index) {
						if (SashLayoutData(parent).ratios.getItemAt(k) != 0) {
							newRatios.addItem(numRatios == 0 ? 100 : Number(SashLayoutData(parent).ratios.getItemAt(k))  + ratio/numRatios);
						} else {
							newRatios.addItem(0);
						}
						if (SashLayoutData(layoutData.parent).mrmRatios.getItemAt(k) != 0) {
							newMrmRatios.addItem(numMrmRatios == 0 ? 100 : Number(SashLayoutData(parent).mrmRatios.getItemAt(k)) + mrmRatio/numMrmRatios);
						} else {
							newMrmRatios.addItem(0);
						}						
					}
				}		
				SashLayoutData(parent).ratios = newRatios;
				SashLayoutData(parent).mrmRatios = newMrmRatios;
			}
			
			// remove from parent
			layoutData.parent.children.removeItemAt(index);
			
			// stop if parent is WorkbenchLayoutData
			if (layoutData.parent is WorkbenchLayoutData) {
				return;
			}			
			// stop if parent respects the rules
			if ((parent is StackLayoutData && parent.children.length > 0) ||
				(parent is SashLayoutData && parent.children.length > 1)) {
				return;
			}
			
			if (!(parent is ViewLayoutData) && parent.children.length == 0) {
				// remove the parent layout also if it hasn't any children
				removeEditorLayout(parent);
			}			
		}
		
		/**
		 * Dispatches remove event after load for recycled views.
		 * 
		 * @see load()
		 */ 
		private function dispatchRemoveEventsOnLoad(recycledViews:RecycledViews):void {
			// dispatch remove event for non-used recycled views 
			var components:ArrayCollection = new ArrayCollection();
			for (var normalViewId:String in recycledViews.normalViews) {
				components.addItem(RecycledViewEntry(recycledViews.normalViews[normalViewId]).existingView);
			}
			for each (var editor:RecycledViewEntry in recycledViews.editorViews) {
				components.addItem(editor.existingView);
			}
			if (components.length > 0) {
				dispatchRemoveEvents(components);
			}
		}
			
		/**
		 * Called to clear workbench data.
		 * Used before setting a new layout content on workbench.
		 * 
		 * @see load()
		 * 
		 * 
		 */
		private function clearVisualWorkbench():void {
			_layoutArea.removeAllChildren();
			
			leftToolbarArea.removeAllChildren();
			leftToolbarArea.width = 0;
			
			rightToolbarArea.removeAllChildren();
			rightToolbarArea.width = 0;
			
			bottomToolbarArea.removeAllChildren();
			bottomToolbarArea.height = 0;
			
			_componentToLayoutData = new Dictionary();
			_layoutDataToComponent = new Dictionary();	
		
			for each (var viewPopup:ViewPopupWindow in getViewPopupWindows()) {
				viewPopup.close(false);
			}
		}
		
		private function getViewPopupWindowsFromList(childList:IChildList, array:ArrayCollection):void {				
			for (var i:int = 0; i < childList.numChildren; i++) {
				if (!(childList.getChildAt(i) is UIComponent) || !UIComponent(childList.getChildAt(i)).isPopUp) {
					continue;
				}
				var d:UIComponent = UIComponent(childList.getChildAt(i));
				if (d is ViewPopupWindow) {
					array.addItem(d);
				}
			}			
		}
	
		private function getViewPopupWindows():ArrayCollection {
			var viewPopups:ArrayCollection = new ArrayCollection();
			getViewPopupWindowsFromList(FlexGlobals.topLevelApplication.systemManager, viewPopups);
			getViewPopupWindowsFromList(FlexGlobals.topLevelApplication.systemManager.popUpChildren, viewPopups);
			
			return viewPopups;
		}
		
		/**
		 * Loads the graphical content for the workbench area 
		 * by loading the content for the <code>_layoutArea</code>, 
		 * populating the left/right/botom toolbar areas with minimized stacks and
		 * openning undock views.
		 * 
		 * 
		 */
		private function loadVisualWorkbench(reuseExistingViews:Boolean, recycledViews:RecycledViews):void {
			loadVisualSash(_rootLayout, _layoutArea, reuseExistingViews, recycledViews);
			
			for each (var stack:StackLayoutData in _rootLayout.minimizedStacks) {
				addVisualMinimizedStack(stack);
			}
			for each (var view:ViewLayoutData in _rootLayout.undockedViews) {
				addViewInPopupWindow(view, view.dimensions[0], view.dimensions[1], view.dimensions[2],  view.dimensions[3]);
			}
		}
		
		/**
		 * Adds to the <code>sashParentComponent</code> the graphical component corresponding to <code>sashLayoutData</code>.
		 * <p>
		 * Loads the graphical component for the newly sash content.
		 * <p>
		 * Otherwise, for each child found loads its content and
		 * stores its ratio by setting their percentHeight/percentWidth properties based on the sash direction.
		 * 
		 * 
		 */
		private function loadVisualSash(sashLayoutData:SashLayoutData, sashParentComponent:UIComponent, reuseExistingViews:Boolean, recycledViews:RecycledViews):void {
			// gets the sash's graphical component and adds it to parent
			var dividedBox:DividedBox = createVisualSash(sashLayoutData);			 
			 sashParentComponent.addChild(dividedBox);
			 // adds it in maps
			 _componentToLayoutData[dividedBox] = sashLayoutData; 
			 _layoutDataToComponent[sashLayoutData] = dividedBox;
	
			 // loads graphical component for each child
			 for each (var child:LayoutData in sashLayoutData.children) {
			 	if (child is SashLayoutData) {
			 		loadVisualSash(SashLayoutData(child), dividedBox, reuseExistingViews, recycledViews);			 		
			 	} else {			 		
			 		loadVisualStack(StackLayoutData(child), dividedBox, reuseExistingViews, recycledViews);
			 	}
			 	// if no ratios found on sash layout data, continue
			 	if (sashLayoutData.ratios.length != sashLayoutData.children.length) {
			 		continue;
			 	}
			 	// adds ratio from model to graphical component
			 	if (sashLayoutData.direction == SashLayoutData.HORIZONTAL) {
			 		UIComponent(_layoutDataToComponent[child]).percentWidth = sashLayoutData.ratios[sashLayoutData.children.getItemIndex(child)];
			 	} else {			 		
			 		UIComponent(_layoutDataToComponent[child]).percentHeight = sashLayoutData.ratios[sashLayoutData.children.getItemIndex(child)];
			 	}
			 }
		}
		
		/**
		 * Given a <code>stackLayoutData</code> and a <code>sashParentComponent</code>, 
		 * creates the appropriate grahical component (a tab navigator),
		 * inserts it into the <code>sashParentComponent</code> and for each of it's children, 
		 * it will create a view and insert it into the tab navigator.
		 * 
		 * 
		 */
		private function loadVisualStack(stackLayoutData:StackLayoutData, sashParentComponent:UIComponent, reuseExistingViews:Boolean, recycledViews:RecycledViews):void {			
			// gets the stack's graphical component and adds it to parent
			var tabNavigator:SuperTabNavigator = createVisualStack(stackLayoutData);
			sashParentComponent.addChild(tabNavigator);
			// adds it in maps
			_componentToLayoutData[tabNavigator] = stackLayoutData; 
			_layoutDataToComponent[stackLayoutData] = tabNavigator;
			
			// loads graphical component for each child
			for each (var child:ViewLayoutData in stackLayoutData.children) {
				var view:Container;
				var key:String = RecycledViews.getNormalViewKey(child);
				if (reuseExistingViews && key in recycledViews.normalViews) { // view used in the old layout, reuses it
					view = Container(RecycledViewEntry(recycledViews.normalViews[key]).existingView);
					delete recycledViews.normalViews[key];
				} else { // creates it
					view = getNewViewComponentInstance(child);
					view.label = _viewProvider.getTitle(child);
					view.setStyle("iconURL", _viewProvider.getIcon(child));
				}
				// adds view to parent
				tabNavigator.addChild(DisplayObject(view));
				// adds it in maps
				_componentToLayoutData[view] = child; 
				_layoutDataToComponent[child] = view;				
			}
			
			// in case of an editor, adds recicled views to stack as children
			if (reuseExistingViews && SashLayoutData(_componentToLayoutData[sashParentComponent]).isEditor) {
				for each (var recycledEditor:RecycledViewEntry in recycledViews.editorViews) {
					// parent changed
					stackLayoutData.children.addItem(recycledEditor.oldViewLayoutData);
					recycledEditor.oldViewLayoutData.parent = stackLayoutData;
					
					var editorview:Container = Container(recycledEditor.existingView);
					tabNavigator.addChild(editorview);
					_componentToLayoutData[editorview] = recycledEditor.oldViewLayoutData; 
					_layoutDataToComponent[recycledEditor.oldViewLayoutData] = editorview;	
				}
				recycledViews.editorViews.removeAll();
			}
		}
				
		/**
		 * This method performs the move operation of a given layoutData, over other layoutData, using the provided details.
		 * More exacly this method can:
		 * <ul>
		 * 	<li> move a panel or multiple panels between other panels on a certain index
		 * 		source - ViewLayoutData/StackLayoutData
		 * 		target - StackLayoutData
		 * 		childIndex - specified
		 * 	<li> move a panel/stack near a stack by dividing the last one in 2 stacks
		 * 	 	source - ViewLayoutData/StackLayoutData
		 * 		target - StackLayoutData
		 * 		side - specified (LEFT/RIGHT/UP/DOWN)
		 * 	<li> move a panel/stack near the workbench by recreating the last one.
		 * 		source - ViewLayoutData/StackLayoutData
		 * 		target - WorkbenchLayoutData
		 * 		side - specified (LEFT/RIGHT/UP/DOWN)
		 * </ul>
		 * The move mechanism involves removing the source layout from its parent and adding it to/near target layout.
		 * 
		 * All above operation work directly on the layoutData hierarchy and imediately updates the graphical components by
		 * repareting graphical components if necessary.
		 * <p>
		 * At the end, a <code>LayoutDataChangedEvent</code> is dispatched to notify listeneres about changes.
		 * 
		 * @see removeLayout()
		 * @see addOverStackLayoutData()
		 * @see addNearStackLayoutData()
		 * @see addNearWorkbenchLayoutData()
		 * 
		 * 
		 */
		public function moveLayout(source:LayoutData, target:LayoutData, childIndex:Number, side:Number):void {
			// view isn't contained in workbench
			var sourceViewFromOutside:Boolean = (source.parent == null);
			
			var viewToBeActivatedAfterMove:UIComponent = null;
			if (sourceViewFromOutside) {
				viewToBeActivatedAfterMove = _layoutDataToComponent[source];
			} else {
				viewToBeActivatedAfterMove = activeViewList.getActiveView();
				activeViewList.removeActiveView(false);
				
				removeLayout(source);
			}
			
			if (childIndex != -1) { // index specified
				addOverStackLayoutData(source, StackLayoutData(target), childIndex);
			} else if (target is WorkbenchLayoutData) {
				addNearWorkbenchLayoutData(source, WorkbenchLayoutData(target), side);
			} else {
				addNearStackLayoutData(source, StackLayoutData(target), side);
			}		
			
			callLater(activeViewList.setActiveView, [viewToBeActivatedAfterMove]);
						
			callLater(dispatchEvent, [new LayoutDataChangedEvent()]);
		}
		
		/**
		 * Removes the layout data from its parent and reorganize the structure by iterating recursively through parents and removing unnecessary layouts.
		 * Stops when founds a <code>WorkbenchLayoutData</code>.
		 * While removing a layout data, its grafical component will be also removed and <code>_componentToLayoutData</code>, <code>_layoutDataToComponent</code> will be updated (if necessary):
		 * <ul>
		 * 	<li> for a <code>ViewLayoutData</code>, the tab will be romoved from tab navigator. 
		 * 	<li> for a <code>StackLayoutData</code>, the tab navigator will be removed from its parent (a divided box) and the maps updated.
		 * 	<li> for a <code>SashLayoutData</code>, the divided box will be removed from its parent (a divided box) and the maps updated.
		 * 			If it's a sash editor, then removes it only if another one exists on workbench.
		 * </ul>
		 * Note 1: a <code>ViewLayoutData</code> and its grafical component aren't removed from maps because they will always be present in workbench, only their parent is changing.
		 * Note 2: updates ratios for sash component children if the list of children has been modified while removing.
		 * <p>
		 * Rules to apply when reorganizing:
		 * <ul>
		 * 	<li> a <code>StackLayoutData</code> must contain at least one <code>ViewLayoutData</code>, otherwise it must be removed
		 * 	<li> a <code>SashLayoutData</code> must contain at least 2 children, otherwise the single child must be added to sash's parent and the sash removed.
		 * </ul>
		 * 
		 * @param layoutData - layout from where the remove mechanism starts. Can be <code>ViewLayoutData</code> or <code>StackLayoutData</code>
		 * 
		 * @see updateVisualSashChildrenRatios(...)
		 * 
		 * 
		 */
		private function removeLayout(layoutData:LayoutData):void {		
			var index:Number = layoutData.parent.children.getItemIndex(layoutData);
			var component:DisplayObject = _layoutDataToComponent[layoutData];			
			if (layoutData is StackLayoutData) {
				var ratio:Number = getMinimizedRatio(layoutData);
			}
			// removes a sash editor only if isn't the only one
			// doesn't remove the stack layout data from sash editor if its the only one
			// (this way we keep the minimize/maximize button)
			if (layoutData is SashLayoutData && SashLayoutData(layoutData).isEditor ||
			(layoutData is StackLayoutData && SashLayoutData(layoutData.parent).isEditor)) {
				var parent:SashLayoutData = (layoutData is SashLayoutData) ? SashLayoutData(layoutData) : SashLayoutData(layoutData.parent);
				var editors:ArrayCollection = new ArrayCollection();
				getAllSashEditorLayoutData(_rootLayout, editors);
				if (editors.length == 1 && parent.children.length == 1) {					
					return;
				}
			}
			// delete from parent			
			layoutData.parent.children.removeItemAt(index); // model			
			_layoutDataToComponent[layoutData.parent].removeChild(component); 
			
			// if the layout data was a child of a sash layout data, update all children ratios				
			if (layoutData.parent is SashLayoutData) {
				updateVisualSashChildrenRatios(_layoutDataToComponent[layoutData.parent]);					
			}
						
			if (layoutData is StackLayoutData) {
				SashLayoutData(layoutData.parent).mrmRatios.removeItemAt(index);
				updateMinimizedStackRatios(SashLayoutData(layoutData.parent), ratio); 
			}
					
			if(!(layoutData is ViewLayoutData)) {
				// update maps only for stack or sash
				delete _componentToLayoutData[component];
				delete _layoutDataToComponent[layoutData];
				
				if (layoutData is SashLayoutData) {
					var sashComponent:LayoutDividedBox = _layoutDataToComponent[layoutData.parent];	
					if (layoutData.children.length > 0) {
						// add it's only child to its parent
						var child:LayoutData = LayoutData(layoutData.children.getItemAt(0));
						
						layoutData.parent.children.addItemAt(child, index); // model
						child.parent = layoutData.parent;			
						
						// graphical component
						sashComponent.addChildAt(_layoutDataToComponent[child], index);	
						
					}
					// the list of children has been modified, update all children ratios
					updateVisualSashChildrenRatios(sashComponent);					
				}
			}
			// stop if parent is WorkbenchLayoutData
			if (layoutData.parent is WorkbenchLayoutData) {
				return;
			}			
			// stop if parent respects the rules
			if ((layoutData.parent is StackLayoutData && layoutData.parent.children.length > 0) ||
				(layoutData.parent is SashLayoutData && layoutData.parent.children.length > 1)) {
				return;
			}
			
			removeLayout(layoutData.parent);
		}
		
		/**
		 * Adds a panel between other panels.
		 * Significance: <code>ViewLayoutData</code> is added in <code>StackLayoutData</code> at a given index.
		 * <p>
		 * While adding the layout, the graphical component will be updated: a new tab is inserted in tab navigator.
		 * 
		 * 
		 */
		public function addOverStackLayoutData(source:LayoutData, target:StackLayoutData, childIndex:Number):void {
			if (source is ViewLayoutData) {
				// create a stack with a single child and store it in source
				var stack:StackLayoutData = new StackLayoutData();
				stack.children.addItem(source);
				source = stack;
			}			
			childIndex = (childIndex == target.children.length + 1 ? childIndex - 1 : childIndex);
			
			// add children to target starting from childIndex
			var targetComponent:DisplayObject = _layoutDataToComponent[target];
			for each (var child:ViewLayoutData in source.children) {							
				target.children.addItemAt(child, childIndex); // model
				child.parent = target;
				SuperTabNavigator(targetComponent).addChildAt(_layoutDataToComponent[child], childIndex); // graphical component								
				childIndex++;
			}
			// the source will have no children
			source.children.removeAll();		
		}
		
		/**
		 * Creates a panel/stack near other stack.
		 * Significance : adds the source near target by dividing the target in 2 stacks.
		 * <p>
		 * Source is a ViewLayoutData or a StackLayoutData.
		 * Target is a StackLayoutData.
		 * Side is specified (LEFT/RIGHT/UP/DOWN).
		 * <p>
		 * Based on the parent direction (horizontal/vertical):
		 * <ul>
		 * 	<li>the stackLayoutData from source is added as a brother to target stackLayoutData (before/after it)
		 * 	<li>the target stackLayoutData is added in a new created sashLayoutData; the stackLayoutData from source is also added here. 		
		 * </ul>
		 * While performing these changes, the graphical component will also be updated, including its ratio.
		 * 
		 * @see getRatios(...)
		 * 
		 * 
		 */
		private function addNearStackLayoutData(source:LayoutData, target:StackLayoutData, side:Number):void {
			source = createStackFromViews(source);
						
			var sashLayout:SashLayoutData = SashLayoutData(target.parent);
			var sashComponent:LayoutDividedBox = LayoutDividedBox(_layoutDataToComponent[sashLayout]);
			var index:Number = sashLayout.children.getItemIndex(target);
			var oldTargetRatio:Number = getRatios(sashComponent)[index];
						
			var targetComponent:UIComponent = UIComponent(_layoutDataToComponent[target]);
			var sourceComponent:UIComponent = UIComponent(_layoutDataToComponent[source]);
			
			if (sashLayout.direction == SashLayoutData.HORIZONTAL && (side == ArrangeTool.LEFT || side == ArrangeTool.RIGHT) ||
				sashLayout.direction == SashLayoutData.VERTICAL && (side == ArrangeTool.UP || side == ArrangeTool.DOWN)) {
					// add source as a brother to target before/ after it based on side
					if (side == ArrangeTool.RIGHT || side == ArrangeTool.DOWN) {
						index++; // after
					}													
					sashLayout.children.addItemAt(source, index); // model
					sashLayout.mrmRatios.addItemAt(0, index);
					source.parent = sashLayout;					
					sashComponent.addChildAt(sourceComponent, index); // graphical component
					
					// the target ratio is divided in two
					if (sashLayout.direction == SashLayoutData.HORIZONTAL) {
						sourceComponent.percentWidth = oldTargetRatio/2;
						targetComponent.percentWidth = oldTargetRatio/2;						
					} else {
						sourceComponent.percentHeight = oldTargetRatio/2;
						targetComponent.percentHeight = oldTargetRatio/2;						
					}					
			} else {
				// create a new sash to store source and target
				var newSash:SashLayoutData = new SashLayoutData();
				newSash.mrmRatios = new ArrayCollection([0, 0]);
				newSash.isEditor = sashLayout.isEditor;								
				if (side == ArrangeTool.LEFT || side == ArrangeTool.RIGHT) {
					newSash.direction = SashLayoutData.HORIZONTAL;					
				} else {
					newSash.direction = SashLayoutData.VERTICAL;					
				}
				var newSashComponent:DividedBox = createVisualSash(newSash);
				
				// update maps for new sash
				_componentToLayoutData[newSashComponent] = newSash;
				_layoutDataToComponent[newSash] = newSashComponent;
				
				// remove target from its parent
				sashLayout.children.removeItemAt(index); // model
				sashComponent.removeChild(targetComponent); // graphical component
				// calculate new ratios for component children (they will represent (100% - the removed component ratio) from parent
				updateVisualSashChildrenRatios(sashComponent, 100 - oldTargetRatio);
				
				// add the new sash to parent
				sashLayout.children.addItemAt(newSash, index); // model
				newSash.parent = sashLayout;
				sashComponent.addChildAt(newSashComponent, index); // graphical component	
								
				// set the new sash component ratio to old target ratio	(this way the sum of children ratios will be 100%)			
				if (sashLayout.direction == SashLayoutData.HORIZONTAL) {
					newSashComponent.percentWidth = oldTargetRatio;					
				} else {
					newSashComponent.percentHeight = oldTargetRatio;					
				}

				// add children to new sash (source/target)
				newSash.children.addItem(target);// model
				target.parent = newSash;
				newSashComponent.addChild(targetComponent); // graphical component
				if (side == ArrangeTool.RIGHT || side == ArrangeTool.DOWN) { // children's order : target, source
					newSash.children.addItem(source);	// model
					source.parent = newSash;
					newSashComponent.addChild(sourceComponent); // graphical component			
				} else { // children's order : source, target
					newSash.children.addItemAt(source, 0); // model
					source.parent = newSash;
					newSashComponent.addChildAt(sourceComponent, 0); // graphical component
				}								
				// set source/target component to 50/50
				if (newSash.direction == SashLayoutData.HORIZONTAL) {
					sourceComponent.percentWidth = LAYOUT_CHILD_PERCENT;
					targetComponent.percentWidth = LAYOUT_CHILD_PERCENT;
					sourceComponent.percentHeight = 100;
					targetComponent.percentHeight = 100;
				} else {
					sourceComponent.percentWidth = 100;
					targetComponent.percentWidth = 100;
					sourceComponent.percentHeight = LAYOUT_CHILD_PERCENT;
					targetComponent.percentHeight = LAYOUT_CHILD_PERCENT;
				}				
			}			
		}
		
		/**
		 * Creates a panel/stack near the workbench area.
		 * Significance: adds source near worbench and recreates the former one.
		 * <p>
		 * Source is a ViewLayoutData or a StackLayoutData.
		 * Target is a WorkbenchLayoutData.
		 * Side is specified (LEFT/RIGHT/UP/DOWN).
		 * <p>
		 * Based on the workbenchLayoutData direction (horizontal/vertical):
		 * <ul>
		 * 	<li>the source is added as a child in workbench (first/last).	
		 * 	<li>all children from workbench are added in a new created sashLayoutData having the workbench direction; 
		 * 		the workbench direction is changed and it contains as children the new sashLayoutData and the stackLayoutData from source (first/last).		
		 * </ul>
		 * While performing these changes, the graphical component is also updated.
		 * 
		 * @see getRatios(...)
		 * 
		 * 
		 */
		public function addNearWorkbenchLayoutData(source:LayoutData, target:WorkbenchLayoutData, side:Number):void {
			source = createStackFromViews(source);			
			
			var targetComponent:LayoutDividedBox = LayoutDividedBox(_layoutDataToComponent[target]);
			var sourceComponent:UIComponent = UIComponent(_layoutDataToComponent[source]);
			
			if (target.direction == SashLayoutData.HORIZONTAL && (side == ArrangeTool.LEFT || side == ArrangeTool.RIGHT) ||
				target.direction == SashLayoutData.VERTICAL && (side == ArrangeTool.UP || side == ArrangeTool.DOWN)) {					
					// calculate new ratios for component
					// the current children will represent 75% from parent
					updateVisualSashChildrenRatios(targetComponent, 100 - workbenchChildPercent);

					// add source as a child in target workbench first/last based on side
					if (side == ArrangeTool.RIGHT || side == ArrangeTool.DOWN) {
						target.children.addItem(source); // model
						target.mrmRatios.addItem(0);
						source.parent = target;	
						targetComponent.addChild(sourceComponent); // graphical component
					} else {
						target.children.addItemAt(source, 0); // model
						target.mrmRatios.addItemAt(0, 0);
						source.parent = target;	
						targetComponent.addChildAt(sourceComponent, 0); // graphical component
					}
					// set source ratio to 25%
					if (target.direction == SashLayoutData.HORIZONTAL) {
						sourceComponent.percentWidth = workbenchChildPercent;						
					} else {
						sourceComponent.percentHeight = workbenchChildPercent;						
					}						
			} else {
				// create a new sash and add all workbench children in it
				var newSash:SashLayoutData = new SashLayoutData();
				if (target.direction == SashLayoutData.HORIZONTAL) {
					newSash.direction = SashLayoutData.HORIZONTAL;					
				} else {
					newSash.direction = SashLayoutData.VERTICAL;					
				}				
				var newSashComponent:DividedBox = createVisualSash(newSash);
				
				for each (var child:LayoutData in target.children) {
					newSash.children.addItem(child);
					newSash.mrmRatios.addItem(SashLayoutData(target).mrmRatios[SashLayoutData(target).children.getItemIndex(child)]);
					child.parent = newSash;
					newSashComponent.addChild(_layoutDataToComponent[child]);
				}				
				// update maps with new sash
				_componentToLayoutData[newSashComponent] = newSash;
				_layoutDataToComponent[newSash] = newSashComponent;
				
				// remove all children from workbench		
				target.children.removeAll(); // model		
				targetComponent.removeAllChildren();	// graphical component
				// change workbench direction																		
				if (target.direction == SashLayoutData.HORIZONTAL) {
					target.direction = SashLayoutData.VERTICAL;				
					targetComponent.direction = BoxDirection.VERTICAL;
				} else {
					target.direction = SashLayoutData.HORIZONTAL;
					targetComponent.direction = BoxDirection.HORIZONTAL;
				}
				
				// add new children in workbench (source and new sash)
				target.children.addItem(newSash); // model
				newSash.parent = target;
				targetComponent.addChild(newSashComponent); // graphical component	
				target.mrmRatios = new ArrayCollection([0, 0]);
				if (side == ArrangeTool.RIGHT || side == ArrangeTool.DOWN) { // children's order : new sash, source	 				
					target.children.addItem(source); // model
					source.parent = target;		 
					targetComponent.addChild(sourceComponent); // graphical component								
				} else { // children's order : source, new sash
					target.children.addItemAt(source, 0); // model
					source.parent = target;		
					targetComponent.addChildAt(sourceComponent, 0); // graphical component			
				}				
				// set new ratio for chidren (by default 25/75)
				if (target.direction == SashLayoutData.HORIZONTAL) {					
					sourceComponent.percentWidth = workbenchChildPercent;
					newSashComponent.percentWidth = 100 - workbenchChildPercent;
				} else {					
					sourceComponent.percentHeight = workbenchChildPercent;
					newSashComponent.percentHeight = 100 - workbenchChildPercent;
				}				
			}			
		}
		
		/**
		 * Creates a <code>DividedBox</code> based on the <code>SashLayoutData</code> properties and returns it.
		 * 
		 * 
		 */
		private function createVisualSash(sashLayoutData:SashLayoutData):LayoutDividedBox {
			var dividedBox:LayoutDividedBox = new LayoutDividedBox(this, sashLayoutData);						
			if (sashLayoutData.direction == SashLayoutData.HORIZONTAL) {				
				dividedBox.direction = BoxDirection.HORIZONTAL;
			} else {				
				dividedBox.direction = BoxDirection.VERTICAL;
			}					
			dividedBox.percentHeight = 100;
			dividedBox.percentWidth = 100;
						
			return dividedBox;
		}
					
		/**
		 * Creates a <code>TabNavigator</code> based on a <code>StackLayoutData</code> properties and returns it.
		 * 		
		 * 
		 */
		private function createVisualStack(stackLayoutData:StackLayoutData):LayoutTabNavigator {
			var tabNavigator:LayoutTabNavigator = new LayoutTabNavigator(this, stackLayoutData); 
			tabNavigator.percentWidth = 100;
			tabNavigator.percentHeight = 100;
					
			return tabNavigator;
		}
					
		/**
		 * Creates a <code>StackLayoutData</code> and registers it in maps.
		 * If layoutData is a ViewLayoutData, it adds it as child to stack, otherwise adds all
		 * children to stack.
		 *  
		 * @param layoutData - ViewLayoutData/StackLayoutData
		 * @return - the stack created
		 * 
		 * @see addNearWorkbenchLayoutData()
		 * @see addNearStackLayoutData()
		 * 
		 */ 
		private function createStackFromViews(layoutData:LayoutData):StackLayoutData {
			// create list of children
			var children:ArrayCollection = new ArrayCollection();
			if (layoutData is ViewLayoutData) {
				children.addItem(layoutData);							
			} else {
				children.addAll(layoutData.children);
				layoutData.children.removeAll();				
			}
			// create stack with children		
			var stackLayoutData:StackLayoutData = new StackLayoutData(); // model											
			var tabNavigator:SuperTabNavigator = createVisualStack(stackLayoutData); // graphical component	
			for each (var child:LayoutData in children) {
				stackLayoutData.children.addItem(child); // model
				child.parent = stackLayoutData;
				tabNavigator.addChild(_layoutDataToComponent[child]); // graphical component
			}						
			
			// update maps for new stackLayoutData
			_componentToLayoutData[tabNavigator] = stackLayoutData;
			_layoutDataToComponent[stackLayoutData] = tabNavigator;
			
			return stackLayoutData;		
		}
		
		/**
		 * Creates a <code>MinimizedStackBar</code> component for <code>StackLayoutData</code> 
		 * and adds it in corresponding toolbar area, based on stack layout data <code>mrmSide</code>.
		 * 
		 * 
		 */
		private function addVisualMinimizedStack(stackLayoutData:StackLayoutData):void {
			if (stackLayoutData.mrmSide == StackLayoutData.NONE) {
				// get the point found in the middle 
				// based on it, the side of the minimized bar is calculated
				var component:UIComponent = _layoutDataToComponent[stackLayoutData];
				var middlePoint:Point = component.localToGlobal(new Point(component.stage.x, component.stage.y));
				
				var workbenchPoint:Point = this.localToGlobal(new Point(x, y));
				var mouseX:Number = middlePoint.x + component.width/2;
				var mouseY:Number = middlePoint.y + component.height/2;
				var point:Point = new Point(mouseX, mouseY);
				
				var a:Point = new Point(workbenchPoint.x, workbenchPoint.y);
				var b:Point = new Point(workbenchPoint.x + width, workbenchPoint.y);
				var c:Point = new Point(workbenchPoint.x + width, workbenchPoint.y + height);	
				var d:Point = new Point(workbenchPoint.x, workbenchPoint.y + height);	
							
				var bc:Number = _arrangeTool.getDistanceToLine(b, c, point);
				var cd:Number = _arrangeTool.getDistanceToLine(c, d, point);
				var da:Number = _arrangeTool.getDistanceToLine(d, a, point);
				
				var	tmpMin:Number = Math.min(bc, cd, da);
				switch (tmpMin) {										
					case bc:
						stackLayoutData.mrmSide = StackLayoutData.RIGHT;
						break;					
					case cd:
						stackLayoutData.mrmSide = StackLayoutData.BOTTOM;
						break;						
					case da:
						stackLayoutData.mrmSide = StackLayoutData.LEFT;
						break;				
				}				
			}
			
			var minimizedStackBar:MinimizedStackBar = new MinimizedStackBar(stackLayoutData.mrmSide, stackLayoutData, this);
			switch (stackLayoutData.mrmSide) {
				case StackLayoutData.LEFT:
					if (leftToolbarArea.getChildren().length == 0) {
						leftToolbarArea.width = 20;
					}
					leftToolbarArea.addChild(minimizedStackBar);
					break;
				case StackLayoutData.RIGHT:
					if (rightToolbarArea.getChildren().length == 0) {
						rightToolbarArea.width = 30;
					}
					rightToolbarArea.addChild(minimizedStackBar);
					break;
				case StackLayoutData.BOTTOM:
					if (bottomToolbarArea.getChildren().length == 0) {
						bottomToolbarArea.height = 20;
					}
					bottomToolbarArea.addChild(minimizedStackBar);
					break;
			}
		}
		
		/**
		 * Removes <code>MinimizedStackBar</code> component for <code>StackLayoutData</code> 
		 * from corresponding toolbar area, based on stack layout data <code>mrmSide</code>.
		 * 
		 * 
		 */
		private function removeVisualMinimizedStack(stackLayoutData:StackLayoutData):void {
			var toolbar:UIComponent;
			switch (stackLayoutData.mrmSide) {
				case StackLayoutData.LEFT:
					toolbar = leftToolbarArea;
					break;
				case StackLayoutData.RIGHT:
					toolbar = rightToolbarArea;
					break;
				case StackLayoutData.BOTTOM:
					toolbar = bottomToolbarArea;
					break;
			}
			for (var i:int = 0; i < toolbar.numChildren; i++) {
				var child:MinimizedStackBar = MinimizedStackBar(toolbar.getChildAt(i));
				if (child.stackLayoutData == stackLayoutData) {
					toolbar.removeChildAt(i);
					break;
				}
			}
			if (toolbar.numChildren == 0) {
				if (toolbar == bottomToolbarArea) {
					toolbar.height = 0;
				} else {
					toolbar.width = 0;
				}
			}
		}
		/**
		 * Given the <code>stackLayoutData</code>, the <code>state</code> in which to end up,
		 * this method updates the workbench layout data structure and the toobar areas regarding the minimized stack layout data and
		 * their graphical components:
		 * <ul>
		 * 	<li> from NORMAL to USER_MINIMIZED/FORCED_MINIMIZED
				 from MAXIMIZED to USER_MINIMIZED
		 * 			
		 * 		- gets the minimized ratio and store it to be used when restoring	
		 * 		- adds the stack to <code>minimizedStacks</code> list from the workbench layout data. 
		 * 		- adds new minimized component to corresponding toolbar area by calling <code>addVisualMinimizedStack()</code>. 	
		 * 		
		 * 		If "from MAXIMIZED to USER_MINIMIZED", all stacks in FORCED_MINIMIZED state are restored through calling  #resize with NORMAL parameter.
		 * 	
		 * 	<li> from MAXIMIZED to NORMAL
		 * 		- all stacks in FORCED_MINIMIZED state are restored through calling  #resize with NORMAL parameter.	
		 * 
		 * 	<li> from USER_MINIMIZED to NORMAL
		 * 		 from FORCED_MINIMIZED to NORMAL
		 * 		
		 * 		- removes the stack from the <code>minimizedStack</code> list of the workbench layout data.
		 * 		- removes the grafical component from corresponding toolbar area by calling <code>removeVisualMinimizedStack()</code>. 
		 * 		- the parent children ratios will be updated based on the <code>mrmRatios</code> stored
		 *   	
		 * 		A maximized stackLayoutData will be also restored to NORMAL if it exist.
		 * 
		 * 	<li> from NORMAL to MAXIMIZED 
		 * 		- for all stacks not minimized (NORMAL state), calls #resize with FORCED_MINIMIZED parameter. 		
		 * </ul>
		 * 
		 * On each of the following cases, the new stackLayoutData state is set to the one given as parameter.
		 * The graphical changes for corresponding components are updated automatically each time the stackLayoutData changes its state.
		 * <p>
		 * At the end, a <code>LayoutDataChangedEvent</code> is dispatched to notify listeneres about changes.
		 * 
		 * 
		 */
		public function resize(stackLayoutData:StackLayoutData, state:Number):void {	
			resizeInternal(stackLayoutData, state);
			
			dispatchEvent(new LayoutDataChangedEvent());
		}
		/**
		 * @see resize()
		 */ 
		private function resizeInternal(stackLayoutData:StackLayoutData, state:Number):void {					
			// get all stackLayoutData objects from root
			var stacks:ArrayCollection = new ArrayCollection();
			getAllStackLayoutData(_rootLayout, stacks);
			
			var oldStackState:Number = stackLayoutData.mrmState;
			var stack:StackLayoutData;
			
			switch (state) {
				case StackLayoutData.USER_MINIMIZED:
				case StackLayoutData.FORCED_MINIMIZED:
					// store the ratio before minimizing
					SashLayoutData(stackLayoutData.parent).mrmRatios[SashLayoutData(stackLayoutData.parent).children.getItemIndex(stackLayoutData)] = getMinimizedRatio(stackLayoutData);

					// store new set
					stackLayoutData.mrmState = state;
					// add graphical component for a minimized stack
					addVisualMinimizedStack(stackLayoutData);					
					// add it in list
					_rootLayout.minimizedStacks.addItem(stackLayoutData);
					// if the old state was maximized, then restore all forced_minimized objects					
					if (oldStackState == StackLayoutData.MAXIMIZED) {						
						for each (stack in stacks) {
							if (stack.mrmState == StackLayoutData.FORCED_MINIMIZED) {
								resizeInternal(stack, StackLayoutData.NORMAL);
							}
						}
					}					
					break;
				case StackLayoutData.NORMAL:					
					if (oldStackState == StackLayoutData.MAXIMIZED) {
						// if the old state was maximized, then restore all forced_minimized objects
						stackLayoutData.mrmState = state;
						for each (stack in stacks) {
							if (stack.mrmState == StackLayoutData.FORCED_MINIMIZED) {
								resizeInternal(stack, StackLayoutData.NORMAL);
							}
						}
					} else {
						// get the ratio to set to restoring object
						var ratio:Number = getNormalRatio(stackLayoutData);
						if (Math.ceil(ratio) != 100 && Math.floor(ratio) != 100) {
							// update other ratios before adding it
							updateVisualSashChildrenRatios(_layoutDataToComponent[stackLayoutData.parent], 100 - ratio);
						}
						if (SashLayoutData(stackLayoutData.parent).direction == SashLayoutData.HORIZONTAL) {
							_layoutDataToComponent[stackLayoutData].percentWidth = ratio;
						} else {							
							_layoutDataToComponent[stackLayoutData].percentHeight = ratio;
						}
						_layoutDataToComponent[stackLayoutData.parent].invalidateSize();
						
						// store new set
						stackLayoutData.mrmState = state;
						// remove it from list
						_rootLayout.minimizedStacks.removeItemAt(_rootLayout.minimizedStacks.getItemIndex(stackLayoutData));
						// remove graphical component
						removeVisualMinimizedStack(stackLayoutData);
						// reset values for stackLayoutData
						stackLayoutData.mrmSide = StackLayoutData.NONE;
						// set ratio to restored element
						
						// empty ratio from list of minimized stack ratios
						SashLayoutData(stackLayoutData.parent).mrmRatios[SashLayoutData(stackLayoutData.parent).children.getItemIndex(stackLayoutData)] = 0;
												
						// restore the maximized stack if it exists
						for each (stack in stacks) {								
							if (stack.mrmState == StackLayoutData.MAXIMIZED) {
								resizeInternal(stack, StackLayoutData.NORMAL);
								break;
							}
						}						
					}
					break;
				case StackLayoutData.MAXIMIZED:
					// store new state	
					stackLayoutData.mrmState = state;	
					// all stacks in normal state will be set to forced_minimized			
					for each (stack in stacks) {
						if (stack.mrmState == StackLayoutData.NORMAL) {
							resizeInternal(stack, StackLayoutData.FORCED_MINIMIZED);
						}
					}
					break;
			}			
		}
		/**
		 * Intended to be used by the user for saving the layout state.
		 * 
		 * Before saving data, the sash ratios are updated by calling <code>updateSashRatios()</code>.
		 * 
		 * Depending on the <code>alreadySerialized</code> parameter it returns
		 * by default either a String containing the serialized structure, or the structure
		 * directly unserialized of type LayoutData.
		 * 
		 * 
		 */
		public function getLayoutData(alreadySerialized:Boolean=true):Object {
			// this is done to make all validations now, before getting the layout
			// because we want to know exactly the correct properties/sizes of workbench components
			// when executing updateLayoutDataRatios
			validateNow();
			updateLayoutDataRatios(_rootLayout);
			
			// set the popup views dimensions
			for each (var viewPopup:ViewPopupWindow in getViewPopupWindows()) {
				viewPopup.viewLayoutData.dimensions = new ArrayCollection([viewPopup.x, viewPopup.y, viewPopup.width, viewPopup.height]);
			}
			
			if (alreadySerialized) {
				return _rootLayout;
			}
			return _layoutSerializer.serialize(_rootLayout);
		}
				
		/**
		 * Updates the sash component children ratios.
		 * 
		 * @param sashComponent - a <code>SashLayoutData</code> graphical component.
		 * @param percentFromParent - represents the sum percent of all sash component's children. Used to calculate their individual percent.
		 * 
		 * @see getRatios(...)
		 * @see removeLayout(...)
		 * @see addNearWorkbenchLayoutData()
		 * @see addNearStackLayoutData()
		 * 
		 */ 
		public function updateVisualSashChildrenRatios(sashComponent:LayoutDividedBox, percentFromParent:Number=100, excludeChild:Object = null):void {		
			sashComponent.validateNow();
						
			var ratios:Array = getRatios(sashComponent, percentFromParent, excludeChild);			
			for each (var child:UIComponent in sashComponent.getChildren()) {
				var ratio:Number = ratios[sashComponent.getChildIndex(child)];
				if (sashComponent.direction == BoxDirection.HORIZONTAL) {
					child.percentWidth = ratio;								
				} else {				
					child.percentHeight = ratio;		
				}
			}			
		}
		/**
		 * Calculates the ratios for all sash component children, excluding the one set in <code>excludeChild</code>.
		 * Based on parent's direction :
		 * - horizontal -> component width ratio = component width /  S * percentFromParent	     
		 * - vertical -> component height ratio = component height /  S * percentFromParent
		 * where S = sum of width/height for all parent's children with layout
		 * 
		 * For minimized children (not included in layout), stores 0 as their ratio.
		 * 
		 * @param sashComponent - a <code>SashLayoutData</code> graphical component.
		 * @param percentFromParent - represents the sum percent of all sash component's children. Used to calculate their individual percent.
		 * @param excludeChild - child that must be excluded from recalculating the ratio.
		 * 
		 * @see addNearStackLayoutData(...)
		 * @see addNearWorkbenchLayoutData(...)
		 * 
		 */
		public function getRatios(sashComponent:LayoutDividedBox, percentFromParent:Number=100, excludeChild:Object = null):Array {	
			var ratios:Array = new Array();
			var child:UIComponent;
			var sum:Number = 0;		
			
			for each (child in sashComponent.getChildren()) {
				if (child == excludeChild) {
					continue;
				}
				if (child.includeInLayout == false) {					
					continue;
				}
			  	if (sashComponent.direction == BoxDirection.HORIZONTAL) {
			 		sum += child.width;			 	
			 	} else {
			 		sum += child.height;		 	
			 	}
			}
			for each (child in sashComponent.getChildren()) {				
				if (child.includeInLayout == false || percentFromParent == 0) {
					ratios.push(0);
					continue;
				}
			  	if (sashComponent.direction == BoxDirection.HORIZONTAL) {	
					if (child == excludeChild) {
						ratios.push(child.percentWidth);						
					} else {
						ratios.push(child.width / sum * percentFromParent);
					}
				} else {
					if (child == excludeChild) {
						ratios.push(child.percentHeight);
					} else {
						ratios.push(child.height / sum * percentFromParent);
					}
				}
			}
			return ratios;
		}
		/**
		 * Iterates recursively through all layout data children to find all sash layout data objects.
		 * If a <code>SashLayoutData</code> found, updates its ratios property 
		 * by getting ratios from all children found in corresponding graphical component.
		 * <p>
		 * Layout ratios are used when:
		 * <ul>
		 * 	<li> loading the layout data - are applied to corresponding graphical components
		 * 	<li> saving the layout data -  are recalculated based on graphical components and stored in sash layout data.
		 * </ul>
		 * Graphical ratios are updated every time is necessary: removing a stack/sash from its parent,
		 * adding a stask/sash to another sash, resizing sash component dividers etc.
		 * 
		 * @see getLayoutData(...) 
		 * 
		 * 
		 */
		private function updateLayoutDataRatios(layoutData:LayoutData):void {
			var child:LayoutData;
			if (layoutData is SashLayoutData) {
				SashLayoutData(layoutData).ratios.removeAll();				
				var sashComponent:UIComponent = _layoutDataToComponent[layoutData];
								
				var ratios:Array = getRatios(LayoutDividedBox(sashComponent));
				for (var i:int = 0; i < ratios.length; i++) {
					SashLayoutData(layoutData).ratios.addItem(ratios[i]);
				}							
			}
			for each (child in layoutData.children) {
				updateLayoutDataRatios(child);
			}
		}
		/**
		 * The method iterates through all minimized children of a <code>SashLayoutData</code> and 
		 * recalculates their <code>mrmRatio</code> by using the given ratio parameter in formula:
		 * 	mrmRatio = mrmRatio * (1 + ratio)/100
		 * 
		 * @see removeLayout()
		 * 
		 * 
		 */
		private function updateMinimizedStackRatios(sashLayoutData:SashLayoutData, ratio:Number):void {
			for (var i:int = 0; i < sashLayoutData.mrmRatios.length; i++) {
				var child:LayoutData = LayoutData(sashLayoutData.children.getItemAt(i));
				sashLayoutData.mrmRatios[i] = sashLayoutData.mrmRatios[i] * (1 + ratio / 100);				
			}
		}
		/**
		 * Calculates the ratio to be set when minimizing a layout data.
		 * If the parent has no minimized children, then this ratio has the same value with one
		 * obtained from given layout data.
		 * Otherwise, the ratio is calculated by taking in consideration the ratio occupied by minimized children
		 * if they weren't in minimized state.
		 * 
		 * @see resize()
		 * @see LayoutDividedBox#computeMinimized()
		 * 
		 * 
		 */
		public function getMinimizedRatio(layoutData:LayoutData):Number {
			var sum:Number = 0;
			var sashLayoutData:SashLayoutData = SashLayoutData(layoutData.parent);
			for (var i:int = 0; i < sashLayoutData.mrmRatios.length; i++) {
				sum += Number(sashLayoutData.mrmRatios[i]); 				
			}
			return getRatios(_layoutDataToComponent[layoutData.parent], 100 - sum)[layoutData.parent.children.getItemIndex(layoutData)];			
		}
		/**
		 * Calculates the ratio to be set when restoring a layout data from minimized state.
		 * Assuming that all children (minimized or not) are restored to their parent in normal state,
		 * their sum of ratios must be 100.
		 * Because we want to restore a single child, it's ratio will be decreased with the sum of
		 * children that will remain minimized. 
		 * 
		 * So, we have the following formula : normal ratio = (ratio - ratio * S/100)
		 * where:
		 * ratio = the ratio stores at minimizing for given layout data.
		 * S = the sum of all minimized children ratios, 
		 * 	   excluding the one for which the normal ratio must be calculated.
		 * 
		 * @see LayoutDividedBox#computeMinimized()
		 * @see resize()
		 * 
		 */ 
		public function getNormalRatio(layoutData:LayoutData):Number {
			var sum:Number = 0;
			var totalSum:Number = 0;
			var sashLayoutData:SashLayoutData = SashLayoutData(layoutData.parent);
			var currentRatio:Number = 0;
			for (var i:int = 0; i < sashLayoutData.mrmRatios.length; i++) {
				var child:LayoutData = LayoutData(sashLayoutData.children.getItemAt(i));
				if (child == layoutData) {
					currentRatio = sashLayoutData.mrmRatios[i];
					continue;
				}		
				sum += Number(sashLayoutData.mrmRatios[i]); 				
			}
			if (100 - sum == currentRatio) { // special case: the formula will return 100 (wrong), we want the current ratio
				return currentRatio;
			}
			return currentRatio * 100/(100 - sum);			
		}
		/**
		 * Gets all <code>StackLayoutData</code> objects found in a root <code>LayoutData</code> given as parameter.
		 * The result is stores in an array given as parameter.
		 */ 
		public function getAllStackLayoutData(root:LayoutData, array:ArrayCollection):void {
			if (root is ViewLayoutData) {
				return;
			}
			if (root is StackLayoutData) {
				array.addItem(root);
			}
			for each(var layoutData:LayoutData in root.children) {
				getAllStackLayoutData(layoutData, array);
			}
		}
		/**
		 * Computes all <code>ViewLayoutData</code> objects found in the <code>root</code> given as parameter,
		 * taking in consideration the <code>onlyEditors</code> parameter.
  		 * 
		 * @param root <code>LayoutData</code> from where do descent in finding <code>ViewLayoutData</code>s.
		 * 				When null the search will start  from <code> rootLayout WorkbenchLayoutData</code>.
		 * @param array where the result will be kept.
		 * 
		 * @param onlyEditors flag for filtering
		 * 
		 */ 
		public function getAllViewLayoutData(root:LayoutData, array:ArrayCollection, onlyEditors:Boolean = false):void {
			if (root == null)
				root = _rootLayout;
				
			if (root is ViewLayoutData) {
				if (!onlyEditors || (onlyEditors && ViewLayoutData(root).isEditor)) {
					array.addItem(root);
				}				
			}			
			for each(var layoutData:LayoutData in root.children) {
				getAllViewLayoutData(layoutData, array, onlyEditors);
			}
		}
		
		/**
		 * Computes all <code>ViewLayoutData</code> objects from workbench that their component is visible to the user.
		 * Takes in consideration the <code>onlyEditors</code> parameter.
		 * <p>
		 * Note: It does not handle the case when <code>StackLayoutData</code> is minimized.
		 * 
		 * @return a list <code>ViewLayoutData</code> corresponding to components that are visible to the user.
		 */
		public function getAllVisibleViewLayoutData(onlyEditors:Boolean = false):ArrayCollection /* of ViewLayoutData*/ {
			var allViewLayoutData:ArrayCollection /* of ViewLayoutData */ = new ArrayCollection();
			getAllViewLayoutData(null, allViewLayoutData, onlyEditors);
			
			var allVisibleViewLayoutData:ArrayCollection /* of ViewLayoutData */ = new ArrayCollection();
			for each (var viewLayoutData:ViewLayoutData in allViewLayoutData) {
				var component:UIComponent = layoutDataToComponent[viewLayoutData];
				
				var parentLayoutData:StackLayoutData = viewLayoutData.parent as StackLayoutData;
				var parentComponent:LayoutTabNavigator = layoutDataToComponent[parentLayoutData] as LayoutTabNavigator;
				
				if (parentComponent.selectedChild == component) 
					allVisibleViewLayoutData.addItem(viewLayoutData);
			}
			return allVisibleViewLayoutData;
		}
		
		/**
		 * Gets all <code>SashLayoutData</code> objects with <code>isEditor</code> true found in a root <code>LayoutData</code> given as parameter.
		 * The result is stores in an array given as parameter.
		 * 
		 * @see removeLayout()
		 * @see LayoutDividedBox#computeMinimized()
		 * 
		 */ 
		public function getAllSashEditorLayoutData(root:LayoutData, array:ArrayCollection):void {
			if (root is ViewLayoutData || root is StackLayoutData) {
				return;
			}
			if (root is SashLayoutData && SashLayoutData(root).isEditor) {
				array.addItem(root);
			}
			for each(var layoutData:LayoutData in root.children) {
				getAllSashEditorLayoutData(layoutData, array);
			}
		}
		
		/**
		 * Gets the tab under mouse when right click.
		 * If available, sets the context menu selection.
		 * 
		 */ 
		private function rightClickHandler(event:MouseEvent):void {
			selectedViewLayoutData = null;
			// gets object under mouse
			var result:Object = _arrangeTool.getLayoutDetailsUnderMouse(new Point(event.stageX, event.stageY));		
			if (result.layout != null) {
				if (result.type == ArrangeTool.OVER_TAB) { // tab available
					// gets graphic component
					var component:UIComponent = _layoutDataToComponent[result.layout];
					// selects it					
					activeViewList.setActiveView(component, true);
					// sets selection
					selectedViewLayoutData = result.layout;					
				}
			}			
		}
		
		/**
		 * Default behavior for docking mechanism.
		 * 
		 * <p>
		 * Starts the dragging for given view.
		 */ 
		private function viewPopup_dockClickHandler(event:DockHandlerEvent):void {
			if (event.isDefaultPrevented()) {
				return;
			}			
			arrangeTool.startDraggingView(event.viewLayoutData);
		}
				
		/**
		 * Finds all <code>ViewLayoutData</code> having view id the one given as parameter.
		 */ 
		public function findLayoutDatasById(viewId:String):ArrayCollection { /* of ViewLayoutData */ 
			var layoutDatas:ArrayCollection = new ArrayCollection();
			for each (var layoutData:LayoutData in componentToLayoutData) {
       			if (layoutData is ViewLayoutData && ViewLayoutData(layoutData).viewId == viewId) {
       				layoutDatas.addItem(layoutData);
       			}
			} 
			return layoutDatas;
		}
		
		/**
		 * Obtains the graphical component by providing a viewId and optionally a customData.
		 * @author Sorin
		 */ 
		public function getComponent(viewId:String, customData:String = null):UIComponent {
			var viewLayoutDataList:ArrayCollection /* of ViewLayoutData */ = findLayoutDatasById(viewId);
			if (customData != null) {
				for each (var viewLayoutData:ViewLayoutData in viewLayoutDataList) 
					if (viewLayoutData.customData == customData)
						return _layoutDataToComponent[viewLayoutData] as UIComponent;
				return null; // none found to match
			} else {
				if (viewLayoutDataList.length >= 1)
					return _layoutDataToComponent[viewLayoutDataList[0]] as UIComponent;
				else
					return null;
			}
		}
		
		/**
		 * Creates a view and its corresponding graphical component and adds it
		 * in the prevoius parent stack or in the right area if no previous parent saved.
		 * 
		 * @param setFocusOnView - sets focus after creating the view
		 * @param workbenchSide - the side where the view will be created if it is added near workbench
		 * @param addInFirstNormalStack - if <code>true</code>, gets the first normal stack (without editor views in it) as parent for created view.
		 * 								Otherwise creates the view in previous parent saved.
		 * @param parentStackLayoutData - if not null, adds the view in this stack layout data.
		 * @param existingComponent - if not null, it is considered the view's graphical component (no new component will be created)
		 * 
		 * 
		 */
		public function addNormalView(view:Object, setFocusOnView:Boolean = false, workbenchSide:Number = -1, addInFirstNormalStack:Boolean = false, parentStackLayoutData:StackLayoutData = null, existingComponent:UIComponent = null):UIComponent {
			if (workbenchSide == -1) {
				workbenchSide = ArrangeTool.RIGHT;
			}
			// create view layout data
			var viewLayoutData:ViewLayoutData;
			if (view is String) {
				viewLayoutData = new ViewLayoutData();
				viewLayoutData.viewId = String(view);
			} else {
				viewLayoutData = ViewLayoutData(view);
			}
			
			var stacks:ArrayCollection = new ArrayCollection();
			getAllStackLayoutData(_rootLayout, stacks);
			
			// search for previous parent
			var parent:StackLayoutData;
			for each (var stack:StackLayoutData in stacks) {
				if (addInFirstNormalStack && parent == null && !SashLayoutData(stack.parent).isEditor) {
					parent = stack;
				}
				if (stack.closedViews.contains(viewLayoutData.viewId)) {
					if (!addInFirstNormalStack) parent = stack;
					stack.closedViews.removeItemAt(stack.closedViews.getItemIndex(viewLayoutData.viewId));
					break;
				}
			}
			if (parentStackLayoutData != null) {
				parent = parentStackLayoutData;
			}
			// create view
			var component:UIComponent = createView(viewLayoutData, existingComponent);
		
			// add view in stack or near workbench
			if (parent != null) {
				addOverStackLayoutData(componentToLayoutData[component], parent, parent.children.length);
			} else {
				addNearWorkbenchLayoutData(componentToLayoutData[component], WorkbenchLayoutData(rootLayout), workbenchSide);
			}
			UIComponent(component.parent).validateNow();
			
			if (setFocusOnView) {
				callLater(activeViewList.setActiveView, [component]);
			}
			// layout changed
			dispatchEvent(new LayoutDataChangedEvent());
						
			return component;
		}
		
		/**
		 * Adds a view on editor area.
		 * <p>
		 * The method behaves in the following way:
		 * <ul>
		 * 	<li> gets the first editor from workbench layout area.
		 * 	<li> if stack exists on editor, adds the view at the end of children list.
		 * 	<li> otherwise, creates one and adds the view as first child.
		 * </ul> 
		 * While performing these changes, the graphical components will also be updated/created.
		 * <p>
		 * At the end, a <code>LayoutDataChangedEvent</code> is dispatched to notify listeneres about changes.
		 * 
		 * @param setFocusOnView - sets focus after creating the view
		 * @param existingComponent - if not null, it is used as view's graphical component (no new component will be created)
		 */ 
		public function addEditorView(viewLayoutData:ViewLayoutData, setFocusOnView:Boolean = false, existingComponent:UIComponent= null):UIComponent {
			var array:ArrayCollection = new ArrayCollection;
			getAllSashEditorLayoutData(_rootLayout, array);	
			if (array.length == 0) {
				throw new Error("At least one editor area must exist in order to execute this method." +
					"Please provide a 'sashLayoutData' entry with 'isEditor=true' in your layout!");
			}
			var sashEditorLayoutData:SashLayoutData = array[0];
			var stack:StackLayoutData;
			var tabNavigator:SuperTabNavigator;
			if (sashEditorLayoutData.children.length == 0) {
				stack = new StackLayoutData();
				stack.parent = sashEditorLayoutData;	
				sashEditorLayoutData.children.addItem(stack);
				sashEditorLayoutData.mrmRatios.addItem(0);
				
				tabNavigator = createVisualStack(stack);
				
				layoutDataToComponent[sashEditorLayoutData].addChild(tabNavigator);
				_componentToLayoutData[tabNavigator] = stack; 
				_layoutDataToComponent[stack] = tabNavigator;			
			} else {
				var stacks:ArrayCollection = new ArrayCollection();
				getAllStackLayoutData(sashEditorLayoutData, stacks);
				stack = StackLayoutData(stacks.getItemAt(0));
				tabNavigator = layoutDataToComponent[stack];
			}
			viewLayoutData.parent = stack;
			stack.children.addItem(viewLayoutData);
			
			var component:UIComponent = createView(viewLayoutData, existingComponent);
			tabNavigator.addChild(component);
			
			if (setFocusOnView) {
				// at the "very end" when everything has finished updating, set it as active view
				UIComponent(FlexGlobals.topLevelApplication).callLater(activeViewList.setActiveView, [component]);
			}
			
			dispatchEvent(new LayoutDataChangedEvent());
			return component;
		}
		
		/**
		 * Gets the graphical component for a <code>viewLayoutData</code>.
		 * <p>
		 * If component is <code>null</code>, an exception is thrown (this must not happen).
		 */ 
		private function getNewViewComponentInstance(viewLayoutData:ViewLayoutData):Container {
			var component:UIComponent = _viewProvider.createView(viewLayoutData);
			if (component == null) {
				throw new Error("A graphical component must be associated for view id '" + viewLayoutData.viewId + "'!");
			}
			
			if (component is IViewContent) {
				component = new WorkbenchViewHost(IViewContent(component));
			} else if (!(component is Container)) {
				throw new Error("A view should be either a Container or an IViewContent");
			} 

			component.addEventListener(FlexEvent.CREATION_COMPLETE, componentCreationCompleteHandler);
			return Container(component);
		}
		
		private function componentCreationCompleteHandler(event:FlexEvent):void {
			UIComponent(event.currentTarget).dispatchEvent(new ViewAddedEvent(UIComponent(event.currentTarget)));
			UIComponent(event.currentTarget).removeEventListener(FlexEvent.CREATION_COMPLETE, componentCreationCompleteHandler);
		}
				
		/**
		 * Creates a <code>ViewLayoutData</code> and its corresponding graphical component
		 * based on the <code>viewId</code> given as parameter.
		 * 
		 * 
		 */ 
		public function createView(view:Object, existingComponent:UIComponent = null):UIComponent {
			// create view layout data
			var viewLayoutData:ViewLayoutData;
			if (view is String) {
				viewLayoutData = new ViewLayoutData();
				viewLayoutData.viewId = String(view);
			} else {
				viewLayoutData = ViewLayoutData(view);
			}
			// create graphical component
			var component:Container = (existingComponent != null) ? Container(existingComponent) : Container(getNewViewComponentInstance(viewLayoutData));
			
			layoutDataToComponent[viewLayoutData] = component;
			componentToLayoutData[component] = viewLayoutData;
			
			component.label = _viewProvider.getTitle(viewLayoutData);
			component.setStyle("iconURL", _viewProvider.getIcon(viewLayoutData));
						
			return component;
		}
		
		/**
		 * Dispatches remove events and removes each view from workbench if requested.
		 * 
		 * <p>
		 * A view is removed if the event dispatched doesn't block it 
		 * (by populating <code>ViewsRemovedEvent.dontRemoveViews</code> or <code>ViewRemovedEvent.dontRemoveView</code>)
		 * or if <code>shouldDispatchEvent</code> is <code>true</code>.
		 * 
		 * <p>
		 * If a view represents the current active view, 
		 * then it will be removed from <code>activeViewList</code>.
		 * 		
		 * 
		 */
		public function closeViews(views:ArrayCollection /* of UIComponent */, shouldDispatchEvent:Boolean = true):void {			
			var viewsRemovedEvent:ViewsRemovedEvent = new ViewsRemovedEvent(views);			
			if (shouldDispatchEvent) {
				dispatchEvent(viewsRemovedEvent);
			}
			for each (var view:UIComponent in views) {
				if (!viewsRemovedEvent.dontRemoveViews.contains(view)) {
					var viewRemovedEvent:ViewRemovedEvent = new ViewRemovedEvent();
					if (shouldDispatchEvent) {
						view.dispatchEvent(viewRemovedEvent);
					}
					if (!viewRemovedEvent.dontRemoveView) {
						removeViewInternal(view);
					}
				}				
			}
		}
		
		/**
		 * Dispatches remove events and removes the view from workbench if requested.
		 * 
		 * <p>
		 * A view is removed if the event dispatched doesn't block it 
		 * (by populating <code>ViewsRemovedEvent.dontRemoveViews</code> or <code>ViewRemovedEvent.dontRemoveView</code>)
		 * or if <code>shouldDispatchEvent</code> is <code>true</code>.
		 * 
		 * <p>
		 * If a view represents the current active view, 
		 * then it will be removed from <code>activeViewList</code>.
		 */ 		
		public function closeView(view:IEventDispatcher, shouldDispatchEvent:Boolean = true):void {
			closeViews(new ArrayCollection([view]), shouldDispatchEvent);			
		}

		/**
		 * Minimize the given <code>StackLayoutData</code>.
		 * 
		 * <p>
		 * If a stack child is the current  active view, it will be removed from <code>activeViewList</code>.
		 * 
		 * 
		 */
		public function minimize(stack:StackLayoutData):void {
			if (stack.children.contains(_componentToLayoutData[activeViewList.getActiveView()])) {
				activeViewList.removeActiveView(false);
			}
			resize(stack, StackLayoutData.USER_MINIMIZED);
		}
		
		/**
		 * Maximize the given <code>StackLayoutData</code>.
		 * 
		 * 
		 */
		public function maximize(stack:StackLayoutData):void {
			resize(stack, StackLayoutData.MAXIMIZED);
		}
		
		/**
		 * Restores the given <code>StackLayoutData</code> to its normal state.
		 * 
		 * 
		 */
		public function restore(stack:StackLayoutData):void {
			resize(stack, StackLayoutData.NORMAL);
		}
		
		/**
		 * Removes a view from layout structure.
		 * The graphical component is removed also.
		 * <p>
		 * At the end, a <code>LayoutDataChangedEvent</code> is dispatched to notify listeneres about changes.
		 * 
		 * @param view represents the view to remove; it can be the layout data (<code>ViewLayoutData</code>)
		 * 		or the graphical component (<code>UIComponent</code>)
		 * 
		 * 
		 */ 		
		private function removeViewInternal(view:Object):void {
			var component:UIComponent;
			var viewLayoutData:ViewLayoutData;
			if (view is UIComponent) { 
				component = UIComponent(view);
				viewLayoutData = _componentToLayoutData[view];
			} else {
				viewLayoutData = ViewLayoutData(view);
				component = _layoutDataToComponent[view];
			}
			if (viewLayoutData == null && component != null) { // view removed after load()
				return;
			}
			if (viewLayoutData.isUndocked) {
				// update layout data
				viewLayoutData.isUndocked = false;
				viewLayoutData.dimensions = new ArrayCollection([x, y, getExplicitOrMeasuredWidth(), getExplicitOrMeasuredHeight()]);			
				var index:int = WorkbenchLayoutData(_rootLayout).undockedViews.getItemIndex(viewLayoutData);
				if (index != -1) {
					WorkbenchLayoutData(_rootLayout).undockedViews.removeItemAt(index);
				}
			} else {
				if (!viewLayoutData.isEditor) {
					StackLayoutData(viewLayoutData.parent).closedViews.addItem(viewLayoutData.viewId);			
				}
				if (viewLayoutData.parent != null) {
					removeLayout(viewLayoutData);		
				}
			}
			
			if (activeViewList.getPreviousActiveViews().contains(component)) {
				var activeView:UIComponent = activeViewList.getActiveView();
				if (activeView == component) {
					activeViewList.removeActiveView(true);					
				} else {
					// remove from active view list
					activeViewList.getPreviousActiveViews().removeItemAt(activeViewList.getPreviousActiveViews().getItemIndex(component));
				}				
			}
			
			delete _componentToLayoutData[component];
			delete _layoutDataToComponent[viewLayoutData];
	
			dispatchEvent(new LayoutDataChangedEvent());
		}

		/**
		 * Method used by CTRL+M and double click on tab events.
		 * 
		 * <p>
		 * If the active stack is in normal state, it will be maximized,
		 * otherwise it will be restored to normal state.
		 *  
		 * 
		 */
		public function maximizeRestoreActiveStackLayoutData():void {
			var view:UIComponent = activeViewList.getActiveView();
			var stack:StackLayoutData = _componentToLayoutData[view].parent;
			if (stack.mrmState == StackLayoutData.NORMAL) {
				maximize(stack);				
			} else {										
				restore(stack);					
			}
		}
		/**
		 * Based on the <code>ViewLayoutData</code> given as parameter,
		 * returns the corresponding tab.
		 * If the tab navigator isn't created completely (hasn't a contentPane), it returns <code>null</code>.
		 * 
		 * <p>
		 * Note:
		 * This method can be used to set styles on a specific tab.
		 * 
		 * 
		 */
		public function getTabComponentForViewLayoutData(viewLayoutData:ViewLayoutData):UIComponent {
			var parentComponent:SuperTabNavigator = SuperTabNavigator(_layoutDataToComponent[viewLayoutData.parent]);
			var index:Number = viewLayoutData.parent.children.getItemIndex(viewLayoutData);
			
			if (parentComponent.contentPane == null) {
				return null;
			}
			return UIComponent(parentComponent.getTabBar().getChildAt(index));			
		}
		
		/**
		 * Opens the given view in a <code>ViewPopupWindow</code>.
		 *
		 * <p>
		 * If <code>existingComponent</code> not null, it is considered to be the
		 * graphical component for given <code>viewLayoutData</code>.
		 * Otherwise a new graphical component will be created.
		 * 
		 * <p>
		 * If x/y/width/height isn't set, the view's
		 * saved dimensions will be used, if available. Otherwise, if the view's component
		 * width/height is too small, 
		 * it will be set to <code>viewPopup_initialMinWidth</code>/<code>viewPopup_initialMinHeight</code>.
		 * 
		 * @param view - the view as <code>ViewLayoutData</code> or the view's id
		 * @param x
		 * @param y
		 * @param width
		 * @param height 
		 * @param isModal
		 * @param existingComponent - if not null, adds it as view's graphical component.
		 * @param existingViewPopupWindowInstance - if not null, use this instance as <code>ViewPopupWindow</code>.
		 * @return - the popup window
		 * 
		 * @author Cristina
		 */ 
		public function addViewInPopupWindow(view:Object, x:Number= NaN, y:Number=NaN, width:Number=NaN, height:Number=NaN, isModal:Boolean = false, existingComponent:UIComponent = null, existingViewPopupWindowInstance:ViewPopupWindow = null):ViewPopupWindow {
			// get viewLayoutData
			var viewLayoutData:ViewLayoutData;
			if (view is String) {
				viewLayoutData = new ViewLayoutData();
				viewLayoutData.viewId = String(view);
			} else {
				viewLayoutData = ViewLayoutData(view);
			}
			
			if (!viewLayoutData.isUndocked) { // doens't come from load
				viewLayoutData.isUndocked = true;
				_rootLayout.undockedViews.addItem(viewLayoutData);
			}
				
			// get a popupWindow instance
			var popup:ViewPopupWindow;
			if (existingViewPopupWindowInstance != null) {
				popup = existingViewPopupWindowInstance;
			} else {			
				popup = ViewPopupWindow(_viewProvider.getViewPopupWindow(viewLayoutData));
				if (popup == null) { // set default instance
					popup = new ViewPopupWindow();				
				}
			}
			// add view's graphical component as child			
			var component:UIComponent = existingComponent != null ? existingComponent : getNewViewComponentInstance(viewLayoutData);
			component.percentWidth = 100;
			component.percentHeight = 100;
			popup.addChild(component);
			
			
			if (viewLayoutData.dimensions.length != 0) {
				if (isNaN(x)) {
					x = viewLayoutData.dimensions[0];
				}
				if (isNaN(y)) {
					y = viewLayoutData.dimensions[1];
				}
				if (isNaN(width)) {
					width = viewLayoutData.dimensions[2];
					}
				if (isNaN(height)) {
					height = viewLayoutData.dimensions[3];	
				}							
			} else {
				// if component width too small, set width to viewPopup_initialMinWidth
				if (isNaN(width) && viewPopup_initialMinWidth > component.getExplicitOrMeasuredWidth()) {
					width = viewPopup_initialMinWidth;
				}
				// if component height too small, set width to viewPopup_initialMinHeight
				if (isNaN(height) && viewPopup_initialMinHeight > component.getExplicitOrMeasuredHeight()) {
					height = viewPopup_initialMinHeight;
				}
			}			
				
			// even if the view isn't part of workbench structure, it is added in map
			// to make things easier
			_layoutDataToComponent[viewLayoutData] = component;
			_componentToLayoutData[component] = viewLayoutData;
			
			popup.title = _viewProvider.getTitle(viewLayoutData);
			popup.titleIconURL = String(_viewProvider.getIcon(viewLayoutData));
			
			popup.workbench = this;
			popup.viewLayoutData = viewLayoutData;
			popup.component = component;
			
			popup.showPopup(width, height, null, isModal);
			
			// set coordonates
			if (!isNaN(x) && !isNaN(y)) {
				popup.move(x, y);
			}
			
			if (existingComponent == null) {
				component.dispatchEvent(new ViewAddedEvent(component));
			}
			
			dispatchEvent(new LayoutDataChangedEvent());
			
			return popup;
		}
			
		/**
		 * Searches vertical for the first stack found below the first editor sash.
		 * If not found, creates one.
		 * 
		 * <p>
		 * This method can be used as parameter for <code>addNormalView</code>
		 * to mention the parent stack where the view must be created.
		 */
		public function getStackBelowEditorSash():StackLayoutData {
			var array:ArrayCollection = new ArrayCollection;
			getAllSashEditorLayoutData(_rootLayout, array);	
			
			if (array.length == 0) {
				throw new Error("At least one editor area must exist in order to execute this method." +
					"Please provide a 'sashLayoutData' entry with 'isEditor=true' in your layout!");
			}
			var sashEditorLayoutData:SashLayoutData = array[0];
			var sashEditorComponent:UIComponent = _layoutDataToComponent[sashEditorLayoutData];
			
			var parent:SashLayoutData = SashLayoutData(sashEditorLayoutData.parent);
			var parentComponent:LayoutDividedBox = _layoutDataToComponent[parent];
			
			var sashIndex:int = parent.children.getItemIndex(sashEditorLayoutData);
			var oldTargetRatio:Number = getRatios(parentComponent)[sashIndex];
						
			var stackBelowEditor:StackLayoutData;
			var stackBelowEditorComponent:LayoutTabNavigator;
			
			if ((parent.direction == SashLayoutData.VERTICAL && sashIndex == parent.children.length - 1)) {
				// create stack
				stackBelowEditor = new StackLayoutData();
				stackBelowEditor.parent = parent;	
				
				// add in layout parent
				parent.children.addItemAt(stackBelowEditor, parent.children.length - 1);
				parent.mrmRatios.addItemAt(0, parent.children.length - 1);
				
				// create graphical component
				stackBelowEditorComponent = createVisualStack(stackBelowEditor);
				
				// add in graphical parent
				parentComponent.addChild(stackBelowEditorComponent);
				
				// add in maps
				_componentToLayoutData[stackBelowEditorComponent] = stackBelowEditor; 
				_layoutDataToComponent[stackBelowEditor] = stackBelowEditorComponent;		
				
				// set new percentages
				sashEditorComponent.percentHeight = oldTargetRatio/2;
				tabNavigator.percentHeight = oldTargetRatio/2;		
								
				return stackBelowEditor;
			}
			
			if (parent.direction == SashLayoutData.HORIZONTAL) {
				// create sash
				var newSash:SashLayoutData = new SashLayoutData();
				newSash.isEditor = false;
				newSash.direction = SashLayoutData.VERTICAL;
				newSash.ratios = new ArrayCollection([50, 50]);
				newSash.mrmRatios = new ArrayCollection([0, 0]); 
				
				// create graphical component
				var newSashComponent:UIComponent = createVisualSash(newSash);
				
				// add in maps
				_componentToLayoutData[newSashComponent] = newSash; 
				_layoutDataToComponent[newSash] = newSashComponent;		
				
				// remove sash editor from parent
				parent.children.removeItemAt(sashIndex);
				parentComponent.removeChild(sashEditorComponent);
				
				// calculate new ratios for component children (they will represent (100% - the removed component ratio) from parent
				updateVisualSashChildrenRatios(parentComponent, 100 - oldTargetRatio);
							
				// set new sash editor parent
				sashEditorLayoutData.parent = newSash;
				newSash.children.addItem(sashEditorLayoutData);
				newSashComponent.addChild(sashEditorComponent);
				
				// replace old editor sash with new sash
				newSash.parent = parent;
				parent.children.addItemAt(newSash, sashIndex);
				parentComponent.addChildAt(newSashComponent, sashIndex);
				
				// create new stack
				var stack:StackLayoutData = new StackLayoutData();
				stack.parent = newSash;	
				newSash.children.addItem(stack);
				
				// create new graphical component
				var tabNavigator:LayoutTabNavigator = createVisualStack(stack);				
				newSashComponent.addChild(tabNavigator);
				
				// add in maps
				_componentToLayoutData[tabNavigator] = stack; 
				_layoutDataToComponent[stack] = tabNavigator;		
					
				// set new percentages
				newSashComponent.percentWidth = oldTargetRatio; 
				
				sashEditorComponent.percentHeight = 50;
				sashEditorComponent.percentWidth = 100;
				tabNavigator.percentHeight = 50;
				tabNavigator.percentWidth = 100;
				
				return stack;
			}
			// otherwise get next child
			var layoutBelowEditor:LayoutData = LayoutData(parent.children.getItemAt(sashIndex + 1));
							
			if (layoutBelowEditor is StackLayoutData) { // what we need
				return StackLayoutData(layoutBelowEditor);
			}
			
			// otherwise is sash, so find first stack and return it
			array.removeAll();
			getAllStackLayoutData(layoutBelowEditor, array);
			
			return StackLayoutData(array.getItemAt(0));			
		}
		
		/**
		 * Refreshes the labels for views.
		 * 
		 * If <code>viewLayoutData</code> not null, 
		 * refreshes only its corresponding graphical component label. 
		 * 
		 * @author Mariana
		 * 
		 */
		public function refreshLabels(viewLayoutData:ViewLayoutData = null):void {
			if (viewLayoutData != null) {
				refreshViewLabel(viewLayoutData);
			} else {
				for each (var layoutData:LayoutData in _componentToLayoutData) {
					if (layoutData is ViewLayoutData) {
						refreshViewLabel(ViewLayoutData(layoutData));
					}
				}
			}
		}
		
		private function refreshViewLabel(viewLayoutData:ViewLayoutData):void {
			var view:UIComponent = _layoutDataToComponent[viewLayoutData];
			if (view is Container) {
				Container(view).label = _viewProvider.getTitle(viewLayoutData);
				if (viewLayoutData.isUndocked) {
					ViewPopupWindow(Container(view).parent).title = Container(view).label;
				}
			}
		}
		
		/**
		 * Refreshes the icons for views.
		 * 
		 * If <code>viewLayoutData</code> not null, 
		 * refreshes only its corresponding graphical component icon. 
		 */
		public function refreshIcons(viewLayoutData:ViewLayoutData = null):void {
			if (viewLayoutData != null) {
				refreshViewIcon(viewLayoutData);
			} else {
				for each (var layoutData:LayoutData in _componentToLayoutData) {
					if (layoutData is ViewLayoutData) {
						refreshViewIcon(ViewLayoutData(layoutData));
					}
				}
			}
		}
		
		private function refreshViewIcon(viewLayoutData:ViewLayoutData):void {
			var view:UIComponent = _layoutDataToComponent[viewLayoutData];
			if (view is Container) {
				var newIcon:Object = _viewProvider.getIcon(viewLayoutData);				
				view.setStyle("iconURL", newIcon);
				if (!viewLayoutData.isUndocked) {
					var tab:FlowerSuperTab = getTabComponentForViewLayoutData(viewLayoutData) as FlowerSuperTab;
					if (tab == null) {
						return;
					}
					// need to set also on tab, otherwise will not work
					tab.setStyle("iconURL", newIcon);	
				} else {
					ViewPopupWindow(Container(view).parent).titleIconURL = String(newIcon);
				}
			}
		}
		
		private function dispatchRemoveEvents(views:ArrayCollection):void {
			dispatchEvent(new ViewsRemovedEvent(views));
			for each (var view:UIComponent in views) {
				view.dispatchEvent(new ViewRemovedEvent());
			}
		}
		
		/**
		 * @author Mariana Gheorghe
		 */
		public function setActiveView(newActiveView:UIComponent, setFocusOnNewView:Boolean = true, dispatchActiveViewChangedEvent:Boolean = true, restoreIfMinimized:Boolean = true):void {
			activeViewList.setActiveView(newActiveView, setFocusOnNewView, dispatchActiveViewChangedEvent, restoreIfMinimized);
		}
		
	}
		
}
