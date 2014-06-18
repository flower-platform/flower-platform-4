/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flex_client.core.editor.action {
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.IDiagramShellContextAware;
	import org.flowerplatform.flexutil.action.MultipleSelectionActionBase;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class DiagramShellAwareActionBase extends MultipleSelectionActionBase implements IDiagramShellContextAware {
		
		private var _context:DiagramShellContext;
		
		public function DiagramShellAwareActionBase() {
			super();
		}
		
		public function get diagramShell():DiagramShell {		
			return _context.diagramShell;
		}
				
		public function get diagramShellContext():DiagramShellContext {			
			return _context;
		}
		
		public function set diagramShellContext(value:DiagramShellContext):void	{
			this._context = value;
		}		
					
	}
}