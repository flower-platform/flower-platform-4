package org.flowerplatform.util.diff_update;

import java.util.List;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class DiffUpdateController {
	
	private IResourceSetProvider resourceSetProvider;	
	
	private DiffUpdateRegistry diffUpdateRegistry;
	
	public DiffUpdateController(IResourceSetProvider resourceSetProvider) {
		this.resourceSetProvider = resourceSetProvider;
		this.diffUpdateRegistry = new DiffUpdateRegistry();
	}
	
	/**
	 * 
	 * @param update
	 */
	public void addUpdate(DiffUpdate update) {
		String entityUid = update.getEntityUid();
		List<String> resourceSets = resourceSetProvider.getResourceSets(entityUid);
		if (resourceSets == null) {
			return;
		}
		for (String resourceSet : resourceSets) {
			diffUpdateRegistry.addUpdate(resourceSet, update);
		}
	}

	/**
	 * 
	 * @param resourceSet
	 * @param firstId
	 * @param lastId
	 */
	public void getUpdates(String resourceSet, long firstId, long lastId) {
		diffUpdateRegistry.getUpdates(resourceSet, firstId, lastId);
	}

	/**
	 * 
	 * @param resourceSet
	 */
	public void registerResourceSet(String resourceSet) {
		diffUpdateRegistry.registerResourceSet(resourceSet);
	}

	/**
	 * 
	 * @param resourceSet
	 */
	public void unregisterResourceSet(String resourceSet) {
		diffUpdateRegistry.unregisterResourceSet(resourceSet);
	}
	
}
