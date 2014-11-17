package org.flowerplatform.codesync.config_loader;

import org.flowerplatform.util.controller.TypeDescriptorRegistry;

/**
 * @author Mariana Gheorghe
 */
public interface ICodeSyncConfigLoader {

	/**
	 * 
	 * @param codeSyncConfigDirs
	 * @param codeSyncConfig
	 */
	void load(String[] codeSyncConfigDirs, TypeDescriptorRegistry codeSyncConfig);

}
