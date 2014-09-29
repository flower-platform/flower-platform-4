package org.flowerplatform.flexdiagram.mindmap {
	import spark.components.RichText;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public interface IAbstractMindMapModelRenderer {
		
		function getLabelDisplay():RichText;
		
	}
}