package org.flowerplatform.flex_client.properties2 {
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.utils.ObjectUtil;
	
	import org.apache.flex.collections.VectorList;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilConstants;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.list.ComposedList;
	import org.flowerplatform.flexutil.list.SingletonList;
	import org.flowerplatform.flexutil.properties.PropertiesHelper;
	import org.flowerplatform.flexutil.properties.PropertyDescriptor;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class NodePropertiesHelper extends PropertiesHelper {
		
		protected static const uriTypeGroup:String = "uriTypeGroup";
		protected static const uri:String = "uri";
		protected static const type:String = "type";
		
		protected var uriTypeGroupDescriptor:IList;
		
		protected var uriDescriptor:IList;
		
		protected var typeDescriptor:IList;
		
		public function NodePropertiesHelper() {
			var descriptor:PropertyDescriptor;

			descriptor = new PropertyDescriptor();
			descriptor.name = uriTypeGroup;
			descriptor.label = Resources.getMessage("properties.uriTypeGroup");
			uriTypeGroupDescriptor = new ArrayList([descriptor]);
			
			
			descriptor = new PropertyDescriptor();
			descriptor.name = uri;
			descriptor.label = Resources.getMessage("properties.uri");
			descriptor.group = uriTypeGroup;
			descriptor.readOnly = true;
			uriDescriptor = new SingletonList(descriptor);

			descriptor = new PropertyDescriptor();
			descriptor.name = type;
			descriptor.label = Resources.getMessage("properties.type");
			descriptor.readOnly = true;
			descriptor.group = uriTypeGroup;
			typeDescriptor = new SingletonList(descriptor);
		}
		
		override protected function copyAllModelProperties(context:Object, model:Object):Object {
			var result:Object = ObjectUtil.copy(Node(model).properties);
			if (Utils.getPropertySafe(context, FlexUtilConstants.PROPERTIES_CONTEXT_INCLUDE_PROPERTIES_WITHOUT_DESCRIPTOR)) {
				if (!Utils.getPropertySafe(context, FlexUtilConstants.PROPERTIES_CONTEXT_IS_CREATE_MODE)) {
					result[uri] = Node(model).nodeUri;
				}
				result[type] = Node(model).type;
			}
			return result;
		}
		
		override protected function getDescriptorsForGroups(context:Object, model:Object):IList {
			var result:IList = CorePlugin.getInstance().nodeTypeDescriptorRegistry.getAdditiveControllers(FlexUtilConstants.FEATURE_PROPERTY_GROUP_DESCRIPTORS, model);
			if (Utils.getPropertySafe(context, FlexUtilConstants.PROPERTIES_CONTEXT_INCLUDE_PROPERTIES_WITHOUT_DESCRIPTOR)) {
				return new ComposedList([uriTypeGroupDescriptor, result]);
			} else {
				return result;
			}
		}
		
		override protected function getDescriptorsForProperties(context:Object, model:Object):IList {
			var result:IList = CorePlugin.getInstance().nodeTypeDescriptorRegistry.getAdditiveControllers(FlexUtilConstants.FEATURE_PROPERTY_DESCRIPTORS, model);
			if (Utils.getPropertySafe(context, FlexUtilConstants.PROPERTIES_CONTEXT_INCLUDE_PROPERTIES_WITHOUT_DESCRIPTOR)) {
				var lists:Array = new Array();
				if (!Utils.getPropertySafe(context, FlexUtilConstants.PROPERTIES_CONTEXT_IS_CREATE_MODE)) {
					lists.push(uriDescriptor);
				}
				lists.push(typeDescriptor);
				lists.push(result);
				return new ComposedList(lists);			
			} else {
				return result;
			}
		}
		
		override protected function getEventDispatcher(context:Object, model:Object):IEventDispatcher {
			return IEventDispatcher(Node(model).properties);
		}
	
	}
}