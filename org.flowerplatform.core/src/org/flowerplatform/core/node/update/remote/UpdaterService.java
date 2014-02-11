package org.flowerplatform.core.node.update.remote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.update.UpdaterPersistenceProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdaterService {

	private final static Logger logger = LoggerFactory.getLogger(UpdaterService.class);
	
	private static final String FULL_CHILDREN_LIST_KEY = "fullChildrenList";
	
	protected UpdaterPersistenceProvider updaterProvider;
	
	public UpdaterService() {
		super();		
	}
	
	public UpdaterService(UpdaterPersistenceProvider updaterProvider) {
		super();
		this.updaterProvider = updaterProvider;
	}

	private NodeUpdate checkForNodeUpdates(ClientNodeStatus clientNodeStatus, Map<String, Object> context, List<ChildrenListUpdate> listUpdates) {
		NodeUpdate nodeUpdate = new NodeUpdate();
		nodeUpdate.setNode(clientNodeStatus.getNode());
		
		if (context != null && context.containsKey(FULL_CHILDREN_LIST_KEY)) {
			NodeService service = (NodeService) CorePlugin.getInstance().getServiceRegistry().getService("nodeService");
			
			nodeUpdate.setFullChildrenList(service.getChildren(nodeUpdate.getNode(), true));
			return nodeUpdate;
		}
				
		nodeUpdate.setUpdatedProperties(
				getPropertyUpdates(
						clientNodeStatus.getNode(), 
						clientNodeStatus.getTimestamp()));
					
		if (clientNodeStatus.getVisibleChildren() != null) {
			listUpdates.addAll(
					getChildrenListUpdates(
							clientNodeStatus.getNode(), 
							clientNodeStatus.getTimestamp()));
			
			for (ClientNodeStatus childNodeStatus : clientNodeStatus.getVisibleChildren()) {
				nodeUpdate.addNodeUpdatesForChild(checkForNodeUpdates(childNodeStatus, context, listUpdates));
			}
		}
		return nodeUpdate;
	}
	
	public NodeUpdate checkForNodeUpdates(ClientNodeStatus clientNodeStatus, Map<String, Object> context) {				
		List<ChildrenListUpdate> listUpdates = new ArrayList<ChildrenListUpdate>();
		
		NodeUpdate nodeUpdate = checkForNodeUpdates(clientNodeStatus, context, listUpdates);
		
		Collections.sort(listUpdates, new Comparator<ChildrenListUpdate>() {
			public int compare(ChildrenListUpdate u1, ChildrenListUpdate u2) {
				return Long.compare(u1.getTimestamp1(), u2.getTimestamp1());
            }
		});		
		
		nodeUpdate.setChildrenListUpdates(listUpdates);
		
		return nodeUpdate;
	}
	
	public Map<String, Object> getPropertyUpdates(Node node, long startingWithTimestamp) {
		return updaterProvider.getPropertyUpdates(node, startingWithTimestamp);		
	}
	
	public List<ChildrenListUpdate> getChildrenListUpdates(Node node, long startingWithTimestamp) {		
		return updaterProvider.getChildrenListUpdates(node, startingWithTimestamp);	
	}
	
	public void addPropertyUpdate(Node node, long timestamp, String property, Object value) {		
		updaterProvider.addPropertyUpdate(node, timestamp, property, value);		
	}
	
	public void addChildrenListUpdate(Node node, long timestamp, String type, int index, Node childNode) {		
		updaterProvider.addChildrenListUpdate(node, timestamp, type, index, childNode);	
	}
	
}
