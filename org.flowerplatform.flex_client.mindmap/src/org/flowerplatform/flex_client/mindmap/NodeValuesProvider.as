package org.flowerplatform.flex_client.mindmap {
	import flash.events.IEventDispatcher;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flexutil.controller.ValuesProvider;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class NodeValuesProvider extends ValuesProvider {
		public function NodeValuesProvider(featurePrefix:String="", orderIndex:int=0) {
			super(featurePrefix, orderIndex);
		}
		
		override public function getActualObject(object:IEventDispatcher):IEventDispatcher {
			return IEventDispatcher(Node(object).properties);
		}
		
	}
}