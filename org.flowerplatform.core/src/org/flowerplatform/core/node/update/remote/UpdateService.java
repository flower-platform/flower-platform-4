package org.flowerplatform.core.node.update.remote;

import java.util.List;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.update.UpdateDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristina Constantinescu
 */
public class UpdateService {

	private final static Logger logger = LoggerFactory.getLogger(UpdateService.class);
	
	protected UpdateDAO updateDAO;
	
	public UpdateDAO getUpdateDAO() {
		return updateDAO;
	}
	
	protected static ThreadLocal<List<Update>> currentMethodInvocationUpdates = new ThreadLocal<List<Update>>();
	
	public static ThreadLocal<List<Update>> getCurrentMethodInvocationUpdates() {
		return currentMethodInvocationUpdates;
	}

	public UpdateService() {
		super();		
	}
	
	public UpdateService(UpdateDAO updateDAO) {
		super();
		this.updateDAO = updateDAO;
	}
	
	public List<Update> getUpdates(Node rootNode, long timestampOfLastRequest) {
		return updateDAO.getUpdates(rootNode, timestampOfLastRequest);
	}
	
	public void subscribe(Node rootNode) {	
		// TODO CC: to be implemented
	}

	public void unsubscribe(Node rootNode) {	
		// TODO CC: to be implemented
	}

}
