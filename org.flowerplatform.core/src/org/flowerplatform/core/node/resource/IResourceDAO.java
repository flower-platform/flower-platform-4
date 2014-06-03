package org.flowerplatform.core.node.resource;

import java.util.List;

import org.flowerplatform.core.node.update.Command;
import org.flowerplatform.core.node.update.remote.Update;

/**
 * @author Mariana Gheorghe
 */
public interface IResourceDAO {

	static final String PROP_RESOURCE_UPDATES_MARGIN = "resourceUpdatesMargin"; 
	static final String PROP_DEFAULT_PROP_RESOURCE_UPDATES_MARGIN = "0"; 
	
	public void sessionSubscribedToResource(String resourceNodeId, String sessionId);
	
	public void sessionUnsubscribedFromResource(String resourceNodeId, String sessionId);
	
	public Object getRawResourceData(String resourceNodeId);
	
	public String getResourceCategory(String resourceNodeId);
	
	public void setRawResourceData(String resourceNodeId, Object rawResourceData, String resourceCategory);
	
	public void unsetRawResourceData(String resourceNodeId);
	
	public long getUpdateRequestedTimestamp(String resourceNodeId);
	
	public void sessionCreated(String sessionId);
	
	public void sessionRemoved(String sessionId);
	
	public List<String> getSubscribedSessions();
	
	public Object getSessionProperty(String sessionId, String property);
	
	public void updateSessionProperty(String sessionId, String property, Object value);
	
	public List<String> getSessionsSubscribedToResource(String resourceNodeId);
	
	public List<String> getResourcesSubscribedBySession(String sessionId);
	
	public List<String> getResources();
	
	public void addUpdate(String resourceNodeId, Update update);

	public List<Update> getUpdates(String resourceNodeId, long timestampOfLastRequest, long timestampOfThisRequest);
	
	/**
	 * @author Claudiu Matei
	 */
	public List<Update> getUpdates(String resourceNodeId, String firstUpdateId, String lastUpdateId);
	
	/**
	 * @author Claudiu Matei
	 */
	public Update getLastUpdate(String resourceNodeId);

	/**
	 * @author Claudiu Matei
	 */
	public void addCommand(Command command);
	
	/**
	 * @author Claudiu Matei
	 */
	public List<Command> getCommands(String resourceNodeId);

	/**
	 * @author Claudiu Matei
	 */
	public List<Command> getCommands(String resourceNodeId, String firstCommandId, String lastCommandId);

	/**
	 * @author Claudiu Matei
	 */
	public List<Command> getCommandsAfter(String resourceNodeId, String commandId);

	/**
	 * @author Claudiu Matei
	 */
	public List<Command> deleteCommandsAfter(String resourceNodeId, String commandId);
	
	/**
	 * @author Claudiu Matei
	 */
	public Command getCommand(String resourceNodeId, String commandId);
	
	/**
	 * @author Claudiu Matei
	 */
	public Command getCommandAfter(String resourceNodeId, String commandId);
	
	/**
	 * @author Claudiu Matei
	 */
	public Command getCommandBefore(String resourceNodeId, String commandId);
	
	/**
	 * @author Claudiu Matei
	 */
	public Integer compareCommands(String resourceNodeId, String leftCommandId, String rightCommandId);
	
	/**
	 * @author Claudiu Matei
	 */
	public String getCommandToUndoId(String resourceNodeId);

	/**
	 * @author Claudiu Matei
	 */
	public void setCommandToUndoId(String resourceNodeId, String commandId);

}
