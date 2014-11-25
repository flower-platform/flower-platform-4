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
package org.flowerplatform.codesync.template.config_loader;

import static org.flowerplatform.codesync.CodeSyncConstants.CODE_SYNC_CONFIG_EXTENSION;
import static org.flowerplatform.codesync.CodeSyncConstants.CODE_SYNC_CONFIG_NOTYPE;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.CODE_SYNC_CONFIG_TEMPLATES;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.CODE_SYNC_CONFIG_VELOCITY_ENGINE;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.config_loader.ICodeSyncConfigLoader;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ResourceServiceRemote;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;

/**
 * @author Mariana Gheorghe
 */
public class TemplatesCodeSyncConfigLoader implements ICodeSyncConfigLoader {

	@Override
	public void load(String[] codeSyncConfigDirs, TypeDescriptorRegistry codeSyncConfig) {
		TemplatesEngineController engine = getVelocityEngineController(codeSyncConfig);
		
		for (String dir : codeSyncConfigDirs) {
			// get the templates config from each dir
			Node templatesConfig = getTemplatesConfig(dir);
			
			// TODO read descriptors
			
			// get the templates folders
			String path = getPath(dir, CODE_SYNC_CONFIG_TEMPLATES);
			engine.addTemplatesDirectory(path);
		}
	}
	
	private TemplatesEngineController getVelocityEngineController(TypeDescriptorRegistry codeSyncConfig) {
		TypeDescriptor descriptor = codeSyncConfig.getExpectedTypeDescriptor(CODE_SYNC_CONFIG_NOTYPE);
		TemplatesEngineController engine = new TemplatesEngineController();
		descriptor.addSingleController(CODE_SYNC_CONFIG_VELOCITY_ENGINE, engine);
		return engine;
	}
	
	private Node getTemplatesConfig(String codeSyncConfigDir) {
		IFileAccessController controller = CorePlugin.getInstance().getFileAccessController();
		String path = getPath(codeSyncConfigDir, CODE_SYNC_CONFIG_TEMPLATES
				+ CodeSyncConstants.getFileExtension(CODE_SYNC_CONFIG_EXTENSION));
		Object file;
		try {
			file = controller.getFile(path);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (controller.exists(file)) {
			String nodeUri = "fpp:|" + path; // TODO how do I know the repo?
			new ResourceServiceRemote().subscribeToParentResource(nodeUri);
			return CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		}
		return null;
	}

	private String getPath(String base, String relative) {
		IPath path = new Path(base);
		path = path.append(relative);
		return path.toString();
	}
}
