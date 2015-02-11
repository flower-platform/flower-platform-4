package org.flowerplatform.jsutil {
	
	public class EntityRegistryManagerFactory {
		public static function createEntityRegistryManager(entityOperationsAdapter:*):* {
			var entityRegistryManager:* = new EntityRegistryManager(entityOperationsAdapter	);
			
			entityRegistryManager.addDiffUpdateProcessor(Constants.ADDED, new AddEntityDiffUpdateProcessor());
			entityRegistryManager.addDiffUpdateProcessor(Constants.UPDATED, new PropertiesDiffUpdateProcessor());
			entityRegistryManager.addDiffUpdateProcessor(Constants.REMOVED, new RemoveEntityDiffUpdateProcessor());
			entityRegistryManager.addDiffUpdateProcessor(Constants.INITIAL_INFO, new InitialInfoDiffUpdateProcessor());
			
			return entityRegistryManager;
		}
	}
}

include "../../../../src_js_as/diff_update/DiffUpdateProcessors.js";
include "../../../../src_js_as/diff_update/EntityRegistry.js";
include "../../../../src_js_as/diff_update/EntityRegistryManager.js";
