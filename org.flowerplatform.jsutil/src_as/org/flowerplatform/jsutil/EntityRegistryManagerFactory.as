package org.flowerplatform.jsutil {
	
	public class EntityRegistryManagerFactory {

		public static const UPDATE_TYPE_UPDATED:String = "updated";
		public static const UPDATE_TYPE_ADDED:String = "added";
		public static const UPDATE_TYPE_REMOVED:String = "removed";
		public static const UPDATE_TYPE_EMPTY:String = "empty";
		public static const UPDATE_TYPE_REQUEST_REFRESH:String = "requestRefresh";
		public static const UPDATE_TYPE_INITIAL_INFO:String = "initialInfo";

		public static function createEntityRegistryManager(entityOperationsAdapter:*):* {
			var entityRegistryManager:* = new EntityRegistryManager(entityOperationsAdapter	);
			
			entityRegistryManager.addDiffUpdateProcessor(UPDATE_TYPE_ADDED, new AddEntityDiffUpdateProcessor());
			entityRegistryManager.addDiffUpdateProcessor(UPDATE_TYPE_UPDATED, new PropertiesDiffUpdateProcessor());
			entityRegistryManager.addDiffUpdateProcessor(UPDATE_TYPE_REMOVED, new RemoveEntityDiffUpdateProcessor());
			entityRegistryManager.addDiffUpdateProcessor(UPDATE_TYPE_INITIAL_INFO, new InitialInfoDiffUpdateProcessor());
			
			return entityRegistryManager;
		}
	}
}

include "../../../../src_js_as/diff_update/DiffUpdateProcessors.js"
include "../../../../src_js_as/diff_update/EntityRegistry.js"
include "../../../../src_js_as/diff_update/EntityRegistryManager.js"
