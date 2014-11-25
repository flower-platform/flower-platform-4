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
package org.flowerplatform.codesync.template.controller;

import org.flowerplatform.codesync.template.CodeSyncTemplateConstants;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana Gheorghe
 */
public abstract class GeneratedFileSyncPropertiesController extends AbstractController {

	public GeneratedFileSyncPropertiesController() {
		setSharedControllerAllowed(true);
	}
	
	/**
	 * Return true if the node has the
	 * {@link CodeSyncTemplateConstants#TEMPLATE} set.
	 */
	protected boolean isGeneratedFile(Node node) {
		if (node.getProperties().get(CodeSyncTemplateConstants.TEMPLATE) != null) {
			return true;
		}
		return false;
	}

}