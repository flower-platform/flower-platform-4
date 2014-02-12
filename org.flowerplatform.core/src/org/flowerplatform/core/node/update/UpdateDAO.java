package org.flowerplatform.core.node.update;

import java.util.List;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.update.remote.Update;
import org.flowerplatform.core.node.update.remote.UpdateService;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @see UpdateService
 * @author Cristina Constantinescu
 */
public abstract class UpdateDAO extends AbstractController {
	
	public abstract void addUpdate(Node node, Update update);
	
	/**
	 * @return a list of all {@link Update update}s registered after <code>timestampOfLastRequest</code>, sorted ascending on <code>timestamp</code>.
	 */
	public abstract List<Update> getUpdates(Node rootNode, long timestampOfLastRequest);
	
}
