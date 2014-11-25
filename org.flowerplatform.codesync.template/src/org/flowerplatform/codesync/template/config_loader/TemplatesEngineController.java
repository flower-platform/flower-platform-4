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

import java.io.StringWriter;
import java.util.Vector;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana Gheorghe
 */
public class TemplatesEngineController extends AbstractController {

	private VelocityEngine engine = new VelocityEngine();

	/**
	 * Add the templates to this engine.
	 */
	public void addTemplatesDirectory(String templatesDir) {
		if (templatesDir != null) {
			Object library = null;
			try {
				library = CorePlugin.getInstance().getFileAccessController().getFile(templatesDir);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			// set path
			String path = CorePlugin.getInstance().getFileAccessController().getAbsolutePath(library);
			engine.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH,
					addToCsv(engine.getProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH), path));

			// add templates
			String macros = getMacros(library, "");
			Object vmLib = engine.getProperty(VelocityEngine.VM_LIBRARY);
			engine.setProperty(VelocityEngine.VM_LIBRARY, addToCsv(vmLib, macros));
		}
	}
	
	/**
	 * Initialize this engine.
	 */
	public void init() {
		engine.init();
	}

	/**
	 * Merge the base template with the context containing the node hierarchy.
	 */
	public String merge(VelocityContext context) {
		StringWriter writer = new StringWriter();
		engine.mergeTemplate("base/base.vm", "UTF-8", context, writer);
		String output = writer.toString();
		System.out.println(output);
		return output;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String addToCsv(Object existing, String added) {
		if (existing == null) {
			// empty
			return added;
		}
		if (existing instanceof String) {
			// just one => append
			return existing + "," + added;
		}
		return String.join(",", (Vector) existing) + "," + added;
	}

	private String getMacros(Object location, String relativeLocation) {
		IFileAccessController controller = CorePlugin.getInstance().getFileAccessController();
		Object[] files = controller.listFiles(location);
		String csv = "";
		if (files != null) {
			for (Object file : files) {
				if (controller.isDirectory(file)) {
					csv += getMacros(file, relativeLocation + controller.getName(file) + "/");
				} else {
					csv += relativeLocation + controller.getName(file) + ",";
				}
			}
		}
		return csv;
	}

}
