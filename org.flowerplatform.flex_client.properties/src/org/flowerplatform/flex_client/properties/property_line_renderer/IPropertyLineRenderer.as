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
package org.flowerplatform.flex_client.properties.property_line_renderer {
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;

	/**
	 * @author Cristina Constantinescu
	 */
	public interface IPropertyLineRenderer {
		
		function get propertyDescriptor():PropertyDescriptor;
		function set propertyDescriptor(value:PropertyDescriptor):void;
				
		function get node():Node;
		function set node(value:Node):void;
		
	}
}