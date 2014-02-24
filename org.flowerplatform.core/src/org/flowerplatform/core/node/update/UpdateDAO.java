package org.flowerplatform.core.node.update;

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
		
		// log update
		logger.debug("UPDATE ADDED for {} : {}", node, update);
	}
	
	protected abstract void doAddUpdate(Node node, Update update);

	public abstract List<Update> getUpdates(Node rootNode, long timestampOfLastRequest);
	
}
