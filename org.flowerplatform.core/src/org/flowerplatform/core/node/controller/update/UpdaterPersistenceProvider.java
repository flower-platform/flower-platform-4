package org.flowerplatform.core.node.controller.update;

import java.util.List;
import java.util.Map;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.update.remote.ChildrenListUpdate;
import org.flowerplatform.util.controller.AbstractController;

public abstract class UpdaterPersistenceProvider extends AbstractController {
	
	public static final String UPDATER_CONTROLLER = "updaterPersistenceProvider";
		
	public abstract Map<String, Object> getPropertyUpdates(Node node, long startingWithTimestamp);
	
	public abstract List<ChildrenListUpdate> getChildrenListUpdates(Node node, long startingWithTimestamp);
	
	public abstract void addPropertyUpdate(Node node, long timestamp, String property, Object value);
	
	public abstract void addChildrenListUpdate(Node node, long timestamp, String type, int index, Node childNode);
	
}
