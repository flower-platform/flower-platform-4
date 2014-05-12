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
package org.flowerplatform.flexdiagram {
	
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.IComposedActionProviderProcessor;
	
	/**
	 * Sets the <tt>diagramShell</tt> that an <tt>IDiagramShellAware</tt> will use
	 * on execution.
	 * 
	 * @author Mariana Gheorghe
	 */
	public class DiagramShellAwareProcessor implements IComposedActionProviderProcessor {
		
		protected var diagramShellContext:DiagramShellContext;
		
		public function DiagramShellAwareProcessor(diagramShell:DiagramShell) {
			diagramShellContext = diagramShell.getNewDiagramShellContext();
		}
		
		public function processAction(action:IAction):void {
			if (action is IDiagramShellContextAware) {
				IDiagramShellContextAware(action).diagramShellContext = diagramShellContext;
			}
		}
		
	}
}