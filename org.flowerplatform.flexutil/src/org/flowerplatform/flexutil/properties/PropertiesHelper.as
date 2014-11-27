/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flexutil.properties {
	import flash.events.IEventDispatcher;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.utils.LinkedList;
	import mx.utils.LinkedListNode;
	import mx.utils.ObjectUtil;
	
	import org.apache.flex.collections.VectorList;
	import org.flowerplatform.flexutil.FlexUtilConstants;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.controller.GenericDescriptor;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	import org.flowerplatform.flexutil.list.EmptyList;
	import org.flowerplatform.flexutil.list.LinkedListWrapper;
	import org.flowerplatform.flexutil.properties.editor.BooleanPropertyEditor;
	import org.flowerplatform.flexutil.properties.editor.StringCsvListPropertyEditor;
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
				.addSingleController(FlexUtilConstants.PROPERTY_EDITOR_TYPE_STRING_CSV_LIST, new GenericDescriptor(StringCsvListPropertyEditor))
				.addSingleController(FlexUtilConstants.PROPERTY_EDITOR_TYPE_BOOLEAN, new GenericDescriptor(BooleanPropertyEditor));
			
			var stringCsvListPropertyEditor:GenericDescriptor = new GenericDescriptor(StringCsvListPropertyEditor);
			
			typeDescriptorRegistry.getOrCreateTypeDescriptor(FlexUtilConstants.NOTYPE_CLASSES_TO_PROPERTY_EDITORS)
				.addSingleController(Utils.getClassNameForObject(String, true), new GenericDescriptor(StringPropertyEditor))
				.addSingleController(Utils.getClassNameForObject(ArrayCollection, true), stringCsvListPropertyEditor)
				.addSingleController(Utils.getClassNameForObject(ArrayList, true), stringCsvListPropertyEditor)
				.addSingleController(Utils.getClassNameForObject(VectorList, true), stringCsvListPropertyEditor)
				.addSingleController(Utils.getClassNameForObject(Boolean, true), new GenericDescriptor(BooleanPropertyEditor));
			return typeDescriptorRegistry;
		}
		
		public var propertyDescriptors:IList;
		
		public var groupDescriptors:IList;
		
		protected function getEventDispatcher(context:Object, model:Object):IEventDispatcher {
			return null;
		}
		
		protected function copyAllModelProperties(context:Object, model:Object):Object {
			return ObjectUtil.copy(model);
		}
		
		protected function getDescriptorsForGroups(context:Object, typeDescriptorRegistry:TypeDescriptorRegistry, model:Object):IList {
			return groupDescriptors;
		}
		
		protected function getDescriptorsForProperties(context:Object, typeDescriptorRegistry:TypeDescriptorRegistry, model:Object):IList {
			return propertyDescriptors;
		}
		
		protected function getGroupForPropertiesWithoutDescriptor(context:Object):String {
			return "";
		}
		
		protected function getGroupForPropertiesWithoutGroup(context:Object):String {
			return "";
		}
		
		protected function createPropertyEntry(context:Object, isGroup:Boolean, propertyDescriptor:PropertyDescriptor, typeDescriptorRegistry:TypeDescriptorRegistry, model:Object):PropertyEntry {
			var entry:PropertyEntry = new PropertyEntry();
			entry.context = context;
			entry.descriptor = propertyDescriptor;
			entry.isGroup = isGroup;
			entry.typeDescriptorRegistry = typeDescriptorRegistry;
			entry.model = model;
			if (model != null) {
				entry.eventDispatcher = getEventDispatcher(context, model);
			}
			return entry;
		}


		public function getPropertyEntries(newContext:Object, typeDescriptorRegistry:TypeDescriptorRegistry, model:Object):IList {
			if (model == null) {
				return EmptyList.INSTANCE;
			}
			var result:LinkedList = new LinkedList();
			
			// add the groups to the result
			var groupInsertBefore:Dictionary = new Dictionary(); // for a group, gives the LinkedListNode before which we need to insert an item belonging to that group
			var lastGroup:String = null; // the map above doesn't contain info for the last group; i.e. we should insert elements with push
			var descriptorsForGroups:IList = getDescriptorsForGroups(newContext, typeDescriptorRegistry, model);
			if (descriptorsForGroups == null) {
				descriptorsForGroups = EmptyList.INSTANCE;
			}
			for (var i:int = 0; i < descriptorsForGroups.length; i++) {
				var newGroupEntry:PropertyEntry = createPropertyEntry(newContext, true, PropertyDescriptor(descriptorsForGroups.getItemAt(i)), typeDescriptorRegistry, null); 
				var node:LinkedListNode = result.push(newGroupEntry);
				if (lastGroup != null) {
					// i.e. not the first iteration
					groupInsertBefore[lastGroup] = node;
				}
				
				lastGroup = newGroupEntry.descriptor.name;
			}
			
			var allModelProperties:Object = copyAllModelProperties(newContext, model);
			
			// add the properties to the result, at the right places (i.e. next to the category)
			var descriptorsForProperties:IList = getDescriptorsForProperties(newContext, typeDescriptorRegistry, model);
			if (descriptorsForProperties == null) {
				descriptorsForProperties = EmptyList.INSTANCE;
			}
			for (i = 0; i < descriptorsForProperties.length; i++) {
				var entry:PropertyEntry = createPropertyEntry(newContext, false, PropertyDescriptor(descriptorsForProperties.getItemAt(i)), typeDescriptorRegistry, model);
				if (allModelProperties.hasOwnProperty(entry.descriptor.name)) {
					// add the value to the entry
					entry.value = allModelProperties[entry.descriptor.name];
					// and remove it from this map (needed later, to know which properties don't have a descriptor)
					delete allModelProperties[entry.descriptor.name];
				} else {
					// not in the model, get the default value from the descriptor
					entry.value = entry.descriptor.defaultValue;
				}
				
				var groupForCurrentEntry:String = entry.descriptor.group;
				if (groupForCurrentEntry == null) {
					// if we used null for desc without group: if such a desc would be processed first => no group entry; else => group entry with name = "null"
					// i.e. not very consistent
					groupForCurrentEntry = getGroupForPropertiesWithoutGroup(newContext);
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
						newGroupEntry = createPropertyEntry(newContext, true, new PropertyDescriptor(), typeDescriptorRegistry, null); 
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
			
			if (Utils.getPropertySafe(newContext, FlexUtilConstants.PROPERTIES_CONTEXT_INCLUDE_PROPERTIES_WITHOUT_DESCRIPTOR)) {
				var first:Boolean = true;
				// the properties left don't have a descriptor
				for (var key:String in allModelProperties) {
					if (first) {
						newGroupEntry = createPropertyEntry(newContext, true, new PropertyDescriptor(), typeDescriptorRegistry, null);
						newGroupEntry.descriptor.name = getGroupForPropertiesWithoutDescriptor(newContext);
						
						// and add it to the map
						node = result.push(newGroupEntry);
						// no need to update groupInsertBefore and lastGroup
						
						first = false;
					}
					entry = createPropertyEntry(newContext, false, new PropertyDescriptor(), typeDescriptorRegistry, model);
					entry.descriptor.name = key;
					entry.value = allModelProperties[key];
					result.push(entry);
				}
			}
			
			return new LinkedListWrapper(result);
		}
	}
}