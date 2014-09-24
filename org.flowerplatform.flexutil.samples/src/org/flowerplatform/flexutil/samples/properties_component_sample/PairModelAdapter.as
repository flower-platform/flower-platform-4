package org.flowerplatform.flexutil.samples.properties_component_sample
{
	import org.flowerplatform.flexutil.Pair;
	import org.flowerplatform.flexutil.controller.IPropertyModelAdapter;

	public class PairModelAdapter implements IPropertyModelAdapter {
		
		public function getProperties(model:Object):Object{
			var result:BindableDictionary = new BindableDictionary();
			result["a"] = Pair(model).a;
			result["b"] = Pair(model).b;
			return result;	
		}
		
		public function getPropertyValue(model:Object, property:String):Object{
			if (property == "a") {
				return Pair(model).a;
			} else {
				return Pair(model).b;
			}
		}
		
		public function getPropertyValueOrWrapper(model:Object,property:String):*{
			//TODO: needs implementation
			return null;
		}
		
		public function commitPropertyValue(model:Object,propertyValueOrWrapper:Object, value:Object,propertyDescriptorName:String, callbackHandler:Function = null):void{
			//TODO: needs implementation
		}
		
	}
}
