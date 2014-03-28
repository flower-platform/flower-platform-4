package org.flowerplatform.flexdiagram {
	
	import flash.utils.Dictionary;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class DiagramShellContext extends Dictionary {
		
		public var diagramShell:DiagramShell;
		
		public function DiagramShellContext(diagramShell:DiagramShell, weakKeys:Boolean=false) {
			super(weakKeys);
			this.diagramShell = diagramShell;
		}
				
	}
}