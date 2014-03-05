package org.flowerplatform.core.node.update;

import java.util.List;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.update.remote.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristina Constantinescu
 */
public class UpdateService {

	private final static Logger logger = LoggerFactory.getLogger(UpdateService.class);
	
	public static final String TIMESTAMP_OF_LAST_REQUEST = "timestampOfLastRequest";
	
	protected UpdateDAO updateDAO;
	
	public UpdateDAO getUpdateDAO() {
		return updateDAO;
	}
	
	public UpdateService() {
		super();		
	}
	
	public UpdateService(UpdateDAO updateDAO) {
		super();
		this.updateDAO = updateDAO;
	}
	
	/**
	 * @return A list of all {@link Update update}s recorded after <code>timestampOfLastRequest</code>, sorted ascending on <code>timestamp</code>.
	 * 	May return <code>null</code>, meaning that the timestamp is too old, and we don't remember everything that happened. In this case, the 
	 * 	client should perform a refresh.
	 */
	public List<Update> getUpdates(Node rootNode) {
		CorePlugin.getInstance().getNodeService().stillSubscribedPing(rootNode);
		
		String sessionId = CorePlugin.getInstance().getSessionId();
		// get the last timestamp when this client requested updates
		long timestampOfLastRequest = (long) CorePlugin.getInstance().getNodeService().getRootNodeInfoDAO()
				.getSessionProperty(sessionId, TIMESTAMP_OF_LAST_REQUEST);
		return updateDAO.getUpdates(rootNode, timestampOfLastRequest);
	}

}
