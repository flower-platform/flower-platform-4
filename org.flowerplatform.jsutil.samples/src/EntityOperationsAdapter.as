package {
	import flash.utils.Dictionary;
	import flash.utils.getQualifiedClassName;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.utils.ObjectUtil;
	
	public class EntityOperationsAdapter {

		// TODO CS: de incercat sa folosim const din js * CM: Nu merge
		
		public static var PROPERTY_FLAG_ONE_TO_MANY:int = 0x1;
		
		public static var PROPERTY_FLAG_MANY_TO_ONE:int = 0x2;
		
		public static var PROPERTY_FLAG_MANY_TO_MANY:int = 0x4;
		
		public static var PROPERTY_FLAG_IGNORE:int = 0x8;

		public static var PROPERTY_FLAG_NAVIGABLE:int = 0x10;


		private var objectPropertiesCache:Dictionary = new Dictionary();

		private var propertyFlagsMap:Dictionary;
		
		public function EntityOperationsAdapter() {
			propertyFlagsMap = new Dictionary();
			
			addOneToManyRelation(Mission, "objectActionGroups", true, ObjectActionGroup, "mission", false);
			addOneToManyRelation(Mission, "resources", true, HumanResource, "mission", false);
			addOneToManyRelation(Task, "objectActionGroups", true, ObjectActionGroup, "task", false);
			addOneToManyRelation(ObjectActionGroup, "objectActions", true, ObjectAction, "objectActionGroup", false);
			addOneToManyRelation(HumanResourceSchedule, "missions", true, Mission, "humanResourceSchedule", false);
			addOneToManyRelation(HumanResource, "humanResourceSchedules", false, HumanResourceSchedule, "humanResource", true);
			
			ignoreProperty(Mission, "instanceId");
			ignoreProperty(Task, "instanceId");
			ignoreProperty(ObjectActionGroup, "instanceId");
			ignoreProperty(ObjectAction, "instanceId");
			ignoreProperty(HumanResourceSchedule, "instanceId");
			ignoreProperty(HumanResource, "instanceId");
			
			
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Operations on list
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public function list_create(entity:*, property:String):void {
			entity[property] = new ArrayList();
		}
		
		public function list_addItem(list:IList, element:Object, index:int):void {
			if (index == -1) {
				list.addItem(element);
			} else {
				list.addItemAt(element, index);
			}
		}
		
		public function list_setItemAt(list:IList, element:Object, index:int):void {
			list.setItemAt(element, index);
		}
		
		public function list_removeItemAt(list:IList, index:int):void {
			list.removeItemAt(index);
		}
		
		public function list_removeItem(list:IList, element:Object):void {
			var index:int = list.getItemIndex(element);
			if (index >= 0) {
				list.removeItemAt(index);
			}
		}
		
		public function list_getLength(list:IList):int {
			return list.length;
		}
		
		public function list_getItemAt(list:IList, index:int):Object {
			return list.getItemAt(index);
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Operations on object
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// TODO CS de redenumit in object_isRoot
		public function object_isRoot(rootOrEntity:*):Boolean {
			if (rootOrEntity.hasOwnProperty("isRootEntity")) {
				return rootOrEntity.isRootEntity;
			}
			return false;
		}
		
		// TODO CS de redenumit in object_getEntityUid ***
		public function object_getEntityUid(entity:*):String {
//			return entity.entityUid;
			return (flash.utils.getQualifiedClassName(entity) as String) + ":" + entity.id;
		}
		
		public function object_hasDynamicProperties(entity:*):Boolean {
			return false;
		}
		
		public function object_iterateProperties(entity:Object, callback:Function):void {
			for each (var property:String in getObjectProperties(entity)) {
				callback(property, entity[property]);
			}
		}
		
		public function propertiesMap_iterateProperties(properties:Object, callback:Function):void {
			for (var property:String in properties) {
				callback(property, properties[property]);
			}
		}
		
		// TODO CS: de bagat direct in parinte
		private function getObjectProperties(obj:*):Array {
			var clazz:Class = Object(obj).constructor;
			if (clazz != Object) {
				// don't cache for anonymous objects
				var properties:Array = objectPropertiesCache[clazz];
			}
			if (properties == null || clazz == null) {
				properties = ObjectUtil.getClassInfo(obj, null, { includeReadOnly: false }).properties as Array;
				objectPropertiesCache[clazz] = properties;
			}
			return properties;
		}
		
		// TODO CS de scos getEntityType; de reden: object_getPropertyInfo ***
		public function object_getPropertyInfo(entity:Object, property:String):* {
			var entityType = entity.constructor;
			var propertyFlagsMap:Dictionary = getPropertyFlagsMap();
			if (propertyFlagsMap[entityType] != null) {
				return propertyFlagsMap[entityType][property];
			}
			return null;
		}
		
		private function addOneToManyRelation(parentType:Class, parentChildrenProperty:String, navigableFromParent:Boolean, childType:Class, childParentProperty:String, navigableFromChild:Boolean):void {
			var relationInfo:Object;
			
			var parentProperties:Dictionary = propertyFlagsMap[parentType];
			if (parentProperties == null) {
				parentProperties = new Dictionary();
				propertyFlagsMap[parentType] =  parentProperties;
			}

			relationInfo = new Object();
			relationInfo["oppositeProperty"] = childParentProperty;
			relationInfo["flags"] = PROPERTY_FLAG_ONE_TO_MANY | (navigableFromParent ? PROPERTY_FLAG_NAVIGABLE : 0);
			parentProperties[parentChildrenProperty] = relationInfo;
			
			var childProperties:Dictionary = propertyFlagsMap[childType];
			if (childProperties == null) {
				childProperties = new Dictionary();
				propertyFlagsMap[childType] = childProperties;
			}
			relationInfo = new Object();
			relationInfo["oppositeProperty"] = parentChildrenProperty;
			relationInfo["flags"] = PROPERTY_FLAG_MANY_TO_ONE  | (navigableFromChild ? PROPERTY_FLAG_NAVIGABLE : 0);
			childProperties[childParentProperty] = relationInfo;
			
		}
		
		private function ignoreProperty(type:Class, property:String) {
			var properties:Dictionary = propertyFlagsMap[type];
			if (properties == null) {
				properties = new Dictionary();
				propertyFlagsMap[type] =  properties;
			}

			var relationInfo:Object = new Object();
			relationInfo["property"] = property;
			relationInfo["flags"] = PROPERTY_FLAG_IGNORE;
			properties[property] = relationInfo;
		}
		
		// TODO de modificat in construcotr *** CM: aici erau relatiile adaugate la map
		public function getPropertyFlagsMap():Dictionary {
			return propertyFlagsMap;
		}

	}

}
