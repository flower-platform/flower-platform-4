package org.flowerplatform.flexutil.properties
{
	import flash.events.IEventDispatcher;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.utils.LinkedList;
	import mx.utils.LinkedListNode;
	
	import org.flowerplatform.flexutil.FlexUtilConstants;
	import org.flowerplatform.flexutil.LinkedListWrapper;
	import org.flowerplatform.flexutil.controller.GenericDescriptor;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	import org.flowerplatform.flexutil.properties.editor.BooleanPropertyEditor;
	import org.flowerplatform.flexutil.properties.editor.StringPropertyEditor;

	/**
	 * The first feature, is the static method that registers in a <code>TypeDescriptorRegistry</code> the property
	 * editors available in this lib.
	 * 
	 * <p>
	 * The second feature: the generation of <code>PropertyEntry</code>s, based on a model, and existing descriptors:
	 * <ul>
	 * 	<li>for properties without descriptors
	 * 	<li>for properties with descriptors, that may belong to groups with or without descriptors
	 * </ul>
	 * 
	 * When we have descriptors (e.g. for properties or groups): we respect the order in which they were provided.
	 * 
	 * @author Cristian Spiescu
	 */
	public class PropertiesHelper {
		
		public static function registerPropertyRenderers(typeDescriptorRegistry:TypeDescriptorRegistry):TypeDescriptorRegistry {
			typeDescriptorRegistry.getOrCreateTypeDescriptor(FlexUtilConstants.NOTYPE_PROPERTY_EDITORS)
				.addSingleController(FlexUtilConstants.PROPERTY_EDITOR_TYPE_STRING, new GenericDescriptor(StringPropertyEditor))
				.addSingleController(FlexUtilConstants.PROPERTY_EDITOR_TYPE_BOOLEAN, new GenericDescriptor(BooleanPropertyEditor));
			return typeDescriptorRegistry;
		}
		
		protected const EMPTY_LIST:ArrayList = new ArrayList([]);
		
		protected function getEventDispatcher(model:Object):IEventDispatcher {
			throw new Error("This should be implemented");
		}
		
		protected function copyAllModelProperties(model:Object):Object {
			throw new Error("This should be implemented");
		}
		
		protected function getDescriptorsForGroups(model:Object):IList {
			throw new Error("This should be implemented");
		}
		
		protected function getDescriptorsForProperties(model:Object):IList {
			throw new Error("This should be implemented");
		}
		
		protected function getGroupForPropertiesWithoutDescriptor():String {
			return "propertiesWithoutDescriptor";
		}
		
		protected function getGroupForPropertiesWithoutGroup():String {
			return "propertiesWithoutGroup";
		}

		public function getPropertyEntries(typeDescriptorRegistry:TypeDescriptorRegistry, model:Object, includePropertiesWithoutDescriptor:Boolean):IList {
			if (model == null) {
				return EMPTY_LIST;
			}
			var result:LinkedList = new LinkedList();
			
			// add the groups to the result
			var groupInsertBefore:Dictionary = new Dictionary(); // for a group, gives the LinkedListNode before which we need to insert an item belonging to that group
			var lastGroup:String = null; // the map above doesn't contain info for the last group; i.e. we should insert elements with push
			var descriptorsForGroups:IList = getDescriptorsForGroups(model);
			for (var i:int = 0; i < descriptorsForGroups.length; i++) {
				var newGroupEntry:PropertyEntry = new PropertyEntry();
				newGroupEntry.isGroup = true;
				newGroupEntry.descriptor = PropertyDescriptor(descriptorsForGroups.getItemAt(i));
				
				var node:LinkedListNode = result.push(newGroupEntry);
				if (lastGroup != null) {
					// i.e. not the first iteration
					groupInsertBefore[lastGroup] = node;
				}
				
				lastGroup = newGroupEntry.descriptor.name;
			}
			
			var allModelProperties:Object = copyAllModelProperties(model);
			
			// add the properties to the result, at the right places (i.e. next to the category)
			var descriptorsForProperties:IList = getDescriptorsForProperties(model);
			for (i = 0; i < descriptorsForProperties.length; i++) {
				var entry:PropertyEntry = new PropertyEntry();
				entry.typeDescriptorRegistry = typeDescriptorRegistry;
				entry.model = model;
				entry.eventDispatcher = getEventDispatcher(model);
				entry.descriptor = PropertyDescriptor(descriptorsForProperties.getItemAt(i));
				if (allModelProperties.hasOwnProperty(entry.descriptor.name)) {
					// add the value to the entry
					entry.value = allModelProperties[entry.descriptor.name];
					// and remove it from this map (needed later, to know which properties don't have a descriptor)
					delete allModelProperties[entry.descriptor.name];
				}
				
				var groupForCurrentEntry:String = entry.descriptor.category;
				if (groupForCurrentEntry == null) {
					// if we used null for desc without group: if such a desc would be processed first => no group entry; else => group entry with name = "null"
					// i.e. not very consistent
					groupForCurrentEntry = getGroupForPropertiesWithoutGroup();
				}
				
				node = groupInsertBefore[groupForCurrentEntry];
				
				if (node != null) {
					// the property has an existing group; not the last
					result.insertBefore(entry, node);
				} else {
					// either new group, or last existing group
					if (groupForCurrentEntry != lastGroup) {
						// i.e. a new group =>
						// create PropertyEntry with a PropertyDescriptor created on the fly
						newGroupEntry = new PropertyEntry();
						newGroupEntry.isGroup = true;
						newGroupEntry.descriptor = new PropertyDescriptor();
						newGroupEntry.descriptor.name = groupForCurrentEntry;
						
						// and add it to the map
						node = result.push(newGroupEntry);
						if (lastGroup != null) {
							// the previous last group is added to the map
							groupInsertBefore[lastGroup] = node;
						}
						lastGroup = newGroupEntry.descriptor.name;
					}
					// in both cases: add at the end
					result.push(entry);
				}
			}
			
			if (includePropertiesWithoutDescriptor) {
				newGroupEntry = new PropertyEntry();
				newGroupEntry.isGroup = true;
				newGroupEntry.descriptor = new PropertyDescriptor();
				newGroupEntry.descriptor.name = getGroupForPropertiesWithoutDescriptor();

				// and add it to the map
				node = result.push(newGroupEntry);
				// no need to update groupInsertBefore and lastGroup
				
				for (var key:String in allModelProperties) {
					// the properties left don't have a descriptor
					entry = new PropertyEntry();
					entry.descriptor = new PropertyDescriptor();
					entry.descriptor.name = key;
					entry.typeDescriptorRegistry = typeDescriptorRegistry;
					entry.model = model;
					entry.eventDispatcher = getEventDispatcher(model);
					entry.value = allModelProperties[key];
					result.push(entry);
				}
			}
			
			return new LinkedListWrapper(result);
		}
	}
}