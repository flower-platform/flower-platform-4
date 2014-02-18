package org.flowerplatform.core.node.update;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.update.remote.Update;
import org.flowerplatform.util.controller.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see UpdateService
 * @author Cristina Constantinescu
 */
public abstract class UpdateDAO extends AbstractController {
	
	private final static Logger logger = LoggerFactory.getLogger(UpdateDAO.class);
	
	public void addUpdate(Node node, Update update) {		
		doAddUpdate(node, update);
		
		// store update in thread local -> updates will be sent back to client with the remote method result
		List<Update> updates = UpdateService.getCurrentMethodInvocationUpdates().get();
		if (updates == null) {
			updates = new ArrayList<Update>();
		}
		updates.add(update);
		UpdateService.getCurrentMethodInvocationUpdates().set(updates);
		
		// log update
		logger.debug("UPDATE ADDED for {} : {}", node, update);
	}
	
	protected abstract void doAddUpdate(Node node, Update update);
	
	/**
	 * @return a list of all {@link Update update}s registered after <code>timestampOfLastRequest</code>, sorted ascending on <code>timestamp</code>.
	 */
	public abstract List<Update> getUpdates(Node rootNode, long timestampOfLastRequest);
	
}
