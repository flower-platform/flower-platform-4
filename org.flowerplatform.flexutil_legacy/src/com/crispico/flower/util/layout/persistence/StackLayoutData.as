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
package  com.crispico.flower.util.layout.persistence {
	import mx.collections.ArrayCollection;
	import org.flowerplatform.flexutil.layout.LayoutData;
	
	
	/**
	 * Represents the entity that contains one or more <code>ViewLayoutData</code> as its children.
	 * It can be minimized/maximized.
	 * 
	 * @author Cristina
	 * 
	 */
	[Bindable] 
	public class StackLayoutData extends LayoutData {
		
		/**
		 * State set to stack layout data to restore it to it's previous dimensions after a minimization/maximization.
		 */
		public static const NORMAL:Number = 0;
		
		/**
		 * State set to stack layout data by pressing a maximize button.
		 */
		public static const MAXIMIZED:Number = 1;
		
		/**
		 * Minimized state set to stack layout data when a maximization has been detected.
		 */
		public static const FORCED_MINIMIZED:Number = 2;
		
		/**
		 * Minimized state set to stack layout data by pressing a minimized button.
		 * 
		 * 
		 */
		public static const USER_MINIMIZED:Number = 3;
		
		public static const NONE:Number = -1;
		
		public static const LEFT:Number = 0;
		
		public static const RIGHT:Number = 1;
		
		
		public static const BOTTOM:Number = 2;
		
		/**
		 * Represents the minimized state of the <code>LayoutData</code>.
		 * Default state is NORMAL.
		 * 
		 * 
		 */ 
		public var mrmState:Number = NORMAL;
		
		/**
		 * Stores the side where this stack layout data must create a <code>MinimizedStackBar</code> when minimized.
		 * Can take the following values: LEFT, RIGHT, BOTTOM.
		 * 
		 * 
		 */
		public var mrmSide:Number = NONE;
		
		/**
		 * 
		 */
		public var closedViews:ArrayCollection = new ArrayCollection();				
	}
	
}