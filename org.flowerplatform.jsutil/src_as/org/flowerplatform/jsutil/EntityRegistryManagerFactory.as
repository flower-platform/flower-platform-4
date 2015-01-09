package org.flowerplatform.jsutil {
	import com.crispico.xops.util.EntityOperationsAdapter;
	
	public class EntityRegistryManagerFactory {
		public static function createEntityRegistryManager():* {
			var entityRegistryManager:* = new EntityRegistryManager(new EntityOperationsAdapter());
			
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
