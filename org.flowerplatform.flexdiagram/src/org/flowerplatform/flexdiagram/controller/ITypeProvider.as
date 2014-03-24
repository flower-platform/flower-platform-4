package org.flowerplatform.flexdiagram.controller {
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	
	/**
	 * @see DiagramShell
	 * @see ControllerUtils
	 * @author Cristina Constantinescu
	 */ 
	public interface ITypeProvider {
		
		function getType(context:DiagramShellContext, model:Object):String;
		
	}
}