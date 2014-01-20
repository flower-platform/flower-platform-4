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
	 * Represents the entity that groups various <code>LayoutData</code> 
	 * (possibly other <code>SashLayoutData</code> entities) together as a unit. 
	 * <p>
	 * Stores the direction (horizontally/vertically) and size as a percentage for each child.
	 * The size of children that are minimized are stored in <code>mrmRatios</code>. This size will be used
	 * when restoring a given child.
	 * <p>
	 * A <code>SashLayoutData</code> with <code>isEditor</code> true is considered to be an editor layout data.
	 * In a workbench, if a single editor layout data exists, then it cannot be deleted or minimized.
	 * 
	 * @author Cristina
	 * 
	 */ 
	public class SashLayoutData extends LayoutData {
		
		public static const HORIZONTAL:Number = 0;
		
		public static const VERTICAL:Number = 1;
		
		public var direction:Number;
		
		public var isEditor:Boolean;
		
		public var ratios:ArrayCollection = new ArrayCollection();
		
		/**
		 * Stores the ratio for this sash layout data before changing it's state to minimized (USER_MINIMIZED/FORCED_MINIMIZED).
		 * It is applied again to corresponding graphical component when restoring (state changes to NORMAL).
		 * <p>
		 * The ratio is updated each time a child is removed from this parent layout data.
		 * 
		 * @see Workbench#updateMinimizedStackRatios()
		 */
		public var mrmRatios:ArrayCollection = new ArrayCollection();
	
	}
	
}