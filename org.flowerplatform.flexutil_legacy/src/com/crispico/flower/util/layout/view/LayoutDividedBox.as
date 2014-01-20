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
package com.crispico.flower.util.layout.view {
	
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.event.LayoutDataChangedEvent;
	import com.crispico.flower.util.layout.persistence.SashLayoutData;
	import com.crispico.flower.util.layout.persistence.WorkbenchLayoutData;
	
	import mx.containers.BoxDirection;
	import mx.containers.DividedBox;
	import mx.core.ScrollPolicy;
	import mx.core.UIComponent;
	import mx.events.DividerEvent;
	
	/**
	 * DividedBox used by layout mechanism.
	 * 
	 * @author Cristina
	 * 
	 */
	 [Bindable]
	public class LayoutDividedBox extends DividedBox {
		
		private var workbench:Workbench;
		
		private var sashLayoutData:SashLayoutData;
		
		public function LayoutDividedBox(workbench:Workbench, sashLayoutData:SashLayoutData):void {
			this.workbench = workbench;
			this.sashLayoutData = sashLayoutData;
			
			this.horizontalScrollPolicy = ScrollPolicy.OFF;
			this.verticalScrollPolicy = ScrollPolicy.OFF;
			this.resizeToContent = true;
			
			setStyle("verticalGap", 3);
			setStyle("horizontalGap", 3);
			// divider release listener
			addEventListener(DividerEvent.DIVIDER_RELEASE, dividerReleaseHandler, false, -50);		
		}
			
		/**
		 * This recursive method is called when one of it's children changes it's state (minimized or not).
		 * It also updates sashLayoutData corresponding minimized ratio:
		 * <ul>
		 * 	<li> if this divided box must be minimized, then before making changes regarding it's state,
		 * 		gets the minimized ratio and stores it so that it will be used when restoring.
		 * 	<li> otherwise, after making changes regarding the state, 
		 * 		gets the minimized ratio stored previously and recalculates it's normal ratio to apply to graphical component.
		 * </ul>
		 * A <code>LayoutDividedBox</code> is considered to be minimized if all children aren't included in layout.
		 * 
		 * @see LayoutTabNavigator#setMinimized() 
		 * 
		 */
		public function computeMinimized(minimized:Boolean):void {
			if (sashLayoutData is WorkbenchLayoutData) {	
				return;
			}
			
			var index:Number = SashLayoutData(sashLayoutData.parent).children.getItemIndex(sashLayoutData);
			
			var allowMinimization:Boolean = true;
			try {
				for each (var child:UIComponent in this.getChildren()) {
					if (child.includeInLayout == true) {
						allowMinimization = false;
						break;
					}
				}	
			} catch (e:Error) {
				return;
			} 
			var ratio:Number;			
			if (allowMinimization) { // the box must be minimized
				
				// gets the minimized ratio and sets it only if necessary
				var oldRatio:Number = SashLayoutData(sashLayoutData.parent).mrmRatios[index];
				SashLayoutData(sashLayoutData.parent).mrmRatios[index] = 0;
				ratio = workbench.getMinimizedRatio(sashLayoutData);
				if (!isNaN(ratio) && ratio != 0) {					
					SashLayoutData(sashLayoutData.parent).mrmRatios[index] = ratio;
				} else {
					SashLayoutData(sashLayoutData.parent).mrmRatios[index] = oldRatio;
				}
				this.includeInLayout = false;
				this.visible = false;
				
				// recursively iterates through parents
				if (parent is LayoutDividedBox) {
					LayoutDividedBox(parent).computeMinimized(allowMinimization);
				}
			}
			// make changes only if needed
			if (includeInLayout == !allowMinimization) {
				return;
			}
			// change to minimize/normal state
			this.includeInLayout = !allowMinimization;
			this.visible = !allowMinimization;
							
			if (!allowMinimization) { // the box is restored
				// get its normal ratio
				ratio = workbench.getNormalRatio(sashLayoutData);				
				if (ratio != 0) {	
					// update children to their new ratios	
					if (Math.ceil(ratio) != 100 && Math.floor(ratio) != 100) {						
						workbench.updateVisualSashChildrenRatios(LayoutDividedBox(parent), 100 - ratio, this);
					}
					// set this box new ratio graphically
  					if (LayoutDividedBox(parent).direction == BoxDirection.HORIZONTAL) {
						this.percentWidth = ratio;
					} else {
						this.percentHeight = ratio;
					}
					LayoutDividedBox(parent).invalidateSize();					
					// it isn't considered minimized so empty the ratio from list of minimized ratios
					SashLayoutData(sashLayoutData.parent).mrmRatios[index] = 0;
				}
			}
			// recursively iterates through parents
			if (parent is LayoutDividedBox) {
				LayoutDividedBox(parent).computeMinimized(allowMinimization);
			}
		}
		
		/**
		 * Hack made in order to not allow DividedBox to resize if children min dimensions are achived.
		 * <p>
		 * When moving the divider, <code>explicitMinWidth</code> and <code>explicitMinHeight</code> are used to calculate 
		 * the divider's maximum allowing distance. Because we work with measured dimensions, 
		 * <code>explicitMinWidth</code> and <code>explicitMinHeight</code> aren't set.
		 * So when a measurement is done, they will be set to measured dimensions. 
		 * 
		 * @see DividedBox#cacheSizes()
		 */ 
		override protected function measure():void {
        	super.measure();
        	
        	explicitMinWidth = measuredMinWidth;
        	explicitMinHeight = measuredMinHeight;         	
        }
        
       	/**
         * When the divider is released, layout data changes so a <code>LayoutDataChangedEvent</code> is dispatched
         * to notify listeners about it.
         * <p>
         * Note : a callLater is used in order to get the new dimensiones 
         * (when this event is dispatched, they aren't set yet).
         */ 
        private function dividerReleaseHandler(event:DividerEvent):void {       
			this.invalidateSize();
			workbench.callLater(workbench.dispatchEvent, [new LayoutDataChangedEvent()]);
        }
	}
	
}