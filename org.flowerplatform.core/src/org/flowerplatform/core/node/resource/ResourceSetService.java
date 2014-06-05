package org.flowerplatform.core.node.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.node.update.remote.Update;
import org.flowerplatform.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mariana Gheorghe
 */
public class ResourceSetService {

	private final static Logger logger = LoggerFactory.getLogger(ResourceSetService.class);
	
	private Map<String, ResourceSetInfo> resourceSetInfos = new HashMap<String, ResourceSetInfo>();
	
	public void addToResourceSet(String resourceSet, URI resourceUri) {
		ResourceSetInfo info = resourceSetInfos.get(resourceSet);
		if (info == null) {
			info = new ResourceSetInfo();
			resourceSetInfos.put(resourceSet, info);
		}
		info.getResourceUris().add(Utils.getString(resourceUri));
	}
	
	public List<Update> getUpdates(String resourceNodeId, long timestampOfLastRequest) {
		List<Update> updates = null;
		ResourceSetInfo info = resourceSetInfos.get(resourceNodeId);
		if (info != null) {
			updates = info.getUpdates();
		}
		List<Update> updatesAddedAfterLastRequest = new ArrayList<Update>();
		if (updates == null) {
			return updatesAddedAfterLastRequest;
		}
		
//		if (timestampOfLastRequest > 0 && 
//			info.getLoadedTimestamp() > timestampOfLastRequest + Integer.valueOf(CorePlugin.getInstance().getFlowerProperties().getProperty(IResourceDAO.PROP_RESOURCE_UPDATES_MARGIN))) {
//			// if resource was reloaded after -> tell client to perform full refresh
//			return null;
//		}
		
		// iterate updates reversed. Because last element in list is the most recent.
		// Most (99.99%) of the calls will only iterate a few elements at the end of the list
		for (int i = updates.size() - 1; i >= 0; i--) {
			Update update = updates.get(i);			
			if (update.getTimestamp() <= timestampOfLastRequest) { 
				// an update was registered before timestampOfLastRequest
				break;
			}
			updatesAddedAfterLastRequest.add(0, update);
		}
		return updatesAddedAfterLastRequest;
	}
	
	public void save(String resourceSet) {
		
	}

	public void addUpdate(String resourceSet, Update update) {
		logger.debug("Adding update {} for resource set {}", update, resourceSet);
		ResourceSetInfo info = resourceSetInfos.get(resourceSet);
		info.getUpdates().add(update);
	}
	
}
