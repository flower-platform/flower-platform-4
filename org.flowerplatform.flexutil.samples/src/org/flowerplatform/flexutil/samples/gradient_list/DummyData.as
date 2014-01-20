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
package org.flowerplatform.flexutil.samples.gradient_list {
	
	import mx.collections.ArrayCollection;
	
	public class DummyData {
		
		public static function getLeftDummyData():ArrayCollection {
			return new ArrayCollection(['This list sets the', 'backgroundColor', 'and gradientColor', 'styles in MXML. ', 
				'The selectionTriangleBackgroundColor', 'style is set to the', 'background color', 'of the component', 'next to this list,', 
				'in this case the second list.']);
		}
		
		public static function getRightDummyData():ArrayCollection {
			return new ArrayCollection(['This list only sets', 'the borderStyle', 'to hide the left border', 'so it does not show under', 
				'the triangle from the first list.', 'Change the other styles', 'using the above controls.']);
		}
		
	}
}