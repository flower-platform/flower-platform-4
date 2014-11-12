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